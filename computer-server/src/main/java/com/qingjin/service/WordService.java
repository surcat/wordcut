package com.qingjin.service;

import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.qingjin.context.BaseContext;
import com.qingjin.dto.StudyLogDTO;
import com.qingjin.entity.*;
import com.qingjin.mapper.LoginMapper;
import com.qingjin.mapper.UserWordMapper;
import com.qingjin.mapper.WordMapper;
import com.qingjin.vo.DailyWordCount;
import com.qingjin.vo.DataAnalysisVO;
import com.qingjin.vo.InformCardVO;
import com.qingjin.vo.StudyDanciVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @className: WordService
 * @author: qingjin
 * @description: 单词服务类，提供单词上传、用户关联管理等功能
 * @date: 2025/6/19 7:01
 * @version: 1.0
 */
@Service
@Slf4j
public class WordService {
    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private WordMapper wordMapper;

    @Autowired
    private UserWordMapper userWordMapper;

    // 批量大小（优化数据库性能）
    private static final int BATCH_SIZE = 500;

    /**
     * 可靠的上传方法 - 处理所有情况
     *
     * 功能描述：
     * 1. 处理重复单词，确保不影响整体导入
     * 2. 建立用户单词关联
     * 3. 修复主键回填问题
     *
     * 实现策略：
     * 1. 先获取所有单词ID（避免批量插入问题）
     * 2. 只插入不存在的单词
     * 3. 建立用户关联
     *
     * @param wordList 单词列表
     * @param userId 用户ID
     */
    @Transactional
    public void reliableUpload(List<Word> wordList, Long userId) {
        if (CollectionUtils.isEmpty(wordList)) {
            log.warn("上传的单词列表为空，跳过处理");
            return;
        }

        log.info("【开始上传单词】——【用户：{}】",userId);

        LocalDate today = LocalDate.now();
        List<UserWord> userWords = new ArrayList<>();
        int duplicateCount = 0;
        int newWordCount = 0;
        int associationCount = 0;

        // 1. 处理每个单词
        for (Word word : wordList) {
            try {
                // 检查单词是否已存在
                Long wordId = wordMapper.findIdByWord(word.getWord());

                // 如果单词不存在，插入新单词
                if (wordId == null) {
                    wordMapper.insert(word); // 单条插入，确保ID回填
                    wordId = word.getId();

                    if (wordId == null) {
                        // 如果ID仍未设置，尝试再次查询
                        wordId = wordMapper.findIdByWord(word.getWord());
                    }

                    if (wordId != null) {
                        newWordCount++;
                    } else {
                        log.error("单词插入失败: {}", word.getWord());
                        continue;
                    }
                } else {
                    duplicateCount++;
                }

                // 创建用户单词关联
                userWords.add(UserWord.builder()
                        .userId(userId)
                        .wordId(wordId)
                        .isMastered(UserWord.MasteryLevel.UNKNOWN)
                        .lastReview(today)
                        .build());

            } catch (Exception e) {
                log.error("处理单词失败: {} - {}", word.getWord(), e.getMessage());
            }
        }

        // 2. 批量创建用户关联（分批处理）
        if (!userWords.isEmpty()) {
            for (int i = 0; i < userWords.size(); i += BATCH_SIZE) {
                List<UserWord> batch = userWords.subList(i, Math.min(i + BATCH_SIZE, userWords.size()));
                try {
                    userWordMapper.batchUpsertUserWords(batch);
                    associationCount += batch.size();
                } catch (Exception e) {
                    log.error("批量关联失败: {}", e.getMessage());
                    // 降级为单条处理
                    for (UserWord uw : batch) {
                        try {
                            userWordMapper.upsertUserWord(
                                    uw.getUserId(),
                                    uw.getWordId(),
                                    uw.getIsMastered(),
                                    uw.getLastReview()
                            );
                            associationCount++;
                        } catch (Exception ex) {
                            log.error("单条关联失败: {} - {}", uw.getWordId(), ex.getMessage());
                        }
                    }
                }
            }
        }

        log.info("单词上传完成: 新增 {} 个单词, 重复 {} 个单词, 建立 {} 个关联",
                newWordCount, duplicateCount, associationCount);
    }

    /**
     * 批量上传优化版（解决重复单词问题）
     *
     * 更可靠的方法，避免批量插入的主键回填问题
     */
    @Transactional
    public void safeBatchUpload(List<Word> wordList, Long userId) {
        if (CollectionUtils.isEmpty(wordList)) {
            return;
        }

        LocalDate today = LocalDate.now();

        // 1. 提取所有单词文本
        Set<String> allWords = wordList.stream()
                .map(Word::getWord)
                .collect(Collectors.toSet());

        // 2. 获取已存在的单词映射
        Map<String, Long> existingWordMap = wordMapper.findIdsByWords(new ArrayList<>(allWords));

        // 3. 筛选出新单词
        List<Word> newWords = wordList.stream()
                .filter(word -> !existingWordMap.containsKey(word.getWord()))
                .collect(Collectors.toList());

        // 4. 插入新单词（单条插入避免主键回填问题）
        for (Word word : newWords) {
            try {
                wordMapper.insert(word);
                // 确保ID设置
                if (word.getId() == null) {
                    Long id = wordMapper.findIdByWord(word.getWord());
                    if (id != null) {
                        word.setId(id);
                        existingWordMap.put(word.getWord(), id);
                    }
                }
            } catch (Exception e) {
                log.error("插入单词失败: {} - {}", word.getWord(), e.getMessage());
            }
        }

        // 5. 准备用户单词关联
        List<UserWord> userWords = new ArrayList<>();
        for (Word word : wordList) {
            Long wordId = existingWordMap.get(word.getWord());
            if (wordId != null) {
                userWords.add(UserWord.builder()
                        .userId(userId)
                        .wordId(wordId)
                        .isMastered(UserWord.MasteryLevel.UNKNOWN)
                        .lastReview(today)
                        .build());
            }
        }

        // 6. 批量创建用户关联
        if (!userWords.isEmpty()) {
            // 分批处理
            for (int i = 0; i < userWords.size(); i += BATCH_SIZE) {
                List<UserWord> batch = userWords.subList(i, Math.min(i + BATCH_SIZE, userWords.size()));
                try {
                    userWordMapper.batchUpsertUserWords(batch);
                } catch (Exception e) {
                    log.error("批量关联失败，降级为单条处理: {}", e.getMessage());
                    for (UserWord uw : batch) {
                        try {
                            userWordMapper.upsertUserWord(
                                    uw.getUserId(),
                                    uw.getWordId(),
                                    uw.getIsMastered(),
                                    uw.getLastReview()
                            );
                        } catch (Exception ex) {
                            log.error("单条关联失败: {}", ex.getMessage());
                        }
                    }
                }
            }
        }

        log.info("批量上传完成: 总单词 {} 个, 新单词 {} 个, 建立关联 {} 个",
                wordList.size(), newWords.size(), userWords.size());
    }


    /**
     * 加载个人单词本
     * @param user_id
     * @return
     */
    public List<Word> getList(Long user_id) {
        LearnPlan plan = loginMapper.getLearnPlan(user_id);
        StudyData studyData = userWordMapper.getStudyDataById(user_id);

        // 处理可能的null值
        Integer dailyNew = plan.getDailyNew() != null ? plan.getDailyNew() : 20;
        Integer dailyOld = plan.getDailyOld() != null ? plan.getDailyOld() : 20;

        // 2. 计算实际可学量
        int remainingTotal = (dailyNew + dailyOld) - studyData.getTodayWord();
        if (remainingTotal <= 0) {
            return Collections.emptyList(); // 今日已学完
        }

        // 3. 分配新词和复习词数量
        int remainingNew = Math.min(dailyNew, remainingTotal);
        int remainingOld = Math.min(dailyOld, remainingTotal - remainingNew);

        // 4. 获取单词列表
        List<Word> wordList = userWordMapper.getWordList(remainingNew,remainingOld,user_id);

        return wordList;
    }

    /**
     * 单词状态更新
     * @param user_id
     * @param state
     * @param id
     */
    public void setState(Long user_id, Integer state, Long id) {

        // 在用户单词表中，更新用户学习状态
        userWordMapper.setState(user_id,state,id);

        // 更新每日单词学习量
        if(state == 2){
            userWordMapper.updateWord(user_id,state);
        }
    }


    /**
     * 单词背诵页面数据渲染请求
     * @return
     */
    public StudyDanciVO study() {
        Long user_id =(Long) BaseContext.getContext();

        // 1. 加载出累计学习天数
        UserInformation inform = loginMapper.getUserInformByUserId(user_id);
        Long learnDay = inform.getLearnDay();

        // 2. 获取今日新词数 和 旧词数
        LearnPlan learnPlan = loginMapper.getLearnPlan(user_id);
        Integer dailyNew = learnPlan.getDailyNew();
        Integer dailyOld = learnPlan.getDailyOld();

        // 3. 获取已完成单词数
        StudyData studyData = userWordMapper.getStudyDataById(user_id);
        Integer todayWord = studyData.getTodayWord();

        // 4. 数据封装
        StudyDanciVO studyDanciVO = StudyDanciVO.builder()
                .learnDay(learnDay)
                .dailyNew(dailyNew)
                .dailyOld(dailyOld)
                .todayWord(todayWord)
                .build();

        return studyDanciVO;
    }

    public void studylog(Long user_id, StudyLogDTO studyLogDTO) {
        StudyLog studyLog = StudyLog.builder()
                .wordId(studyLogDTO.getWordId())
                .operationType(studyLogDTO.getOperation())
                .logDate(studyLogDTO.getDate())
                .timePeriod(studyLogDTO.getTimePeriod())

                .userId(user_id)
                .build();

        userWordMapper.addLog(studyLog);
    }

    public InformCardVO informCard(Long user_id) {
        StudyData studyData = userWordMapper.getStudyDataById(user_id);
        loginMapper.sumLearnWord(user_id);
        LearnPlan learnPlan = loginMapper.getLearnPlan(user_id);
        UserInformation inform = loginMapper.getUserInformByUserId(user_id);

        // 获取掌握单词总数
        long wordMastered = userWordMapper.getWordMastered(user_id);

        // 获取
        List<DailyWordCount> dailyWordList = getWeeklyWordCount(user_id);
        Long lastWeekLearned = 0L;
        for (int i = 0; i < dailyWordList.size(); i++) {
            lastWeekLearned += dailyWordList.get(i).getWordCount();
        }

        List<DailyWordCount> lastWeekMasterList = getWeeklyWordMaster(user_id);
        Long lastWeekMaster = 0L;
        for (int i = 0; i < lastWeekMasterList.size(); i++) {
            lastWeekMaster += lastWeekMasterList.get(i).getWordCount();
        }

        int totalToday = learnPlan.getDailyNew() + learnPlan.getDailyOld();

        Long predictTime = 0L;
        // 预计用时
        if(( lastWeekMaster / 7 / 24) > 0){
            predictTime =  ( totalToday / ( lastWeekMaster / 7 / 24) ) * 60;
        }
        else {
            predictTime = ( totalToday / 20L ) * 60;
        }

        // 计算相距天数（自动处理闰年/闰月）
        long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), learnPlan.getTargetData()==null?LocalDate.now() : learnPlan.getTargetData());

        List<Integer> efficiencyDataList = new ArrayList<>();
        lastWeekMasterList.forEach(dailyWordCount -> efficiencyDataList.add(dailyWordCount.getWordCount()));

        InformCardVO informCardVO = InformCardVO.builder()
                .wordLearned(inform.getLearnWord())
                .wordMastered(wordMastered)
                .lastWeekLearned(lastWeekLearned)
                .lastWeekMastered(lastWeekLearned)
                .dailyNew(learnPlan.getDailyNew())
                .todayLearned(studyData.getTodayWord())
                .totalToday(totalToday)
                .learnedDays(inform.getLearnDay())
                .predictTime(predictTime)
                .daysUntilExam(daysBetween)
                .efficiencyData(efficiencyDataList)
                .build();


        return informCardVO;
    }

    /**
     * 获取用户过去一周的单词学习量
     * @param userId 用户ID
     * @return 按日期分组的学习量统计
     */
    public List<DailyWordCount> getWeeklyWordCount(Long userId) {
        try {
            // 获取数据库查询结果
            List<DailyWordCount> dbResults = userWordMapper.getWeeklyWordCount(userId);

            // 生成过去7天的日期范围
            List<LocalDate> dateRange = new ArrayList<>();
            for (int i = 6; i >= 0; i--) {
                dateRange.add(LocalDate.now().minusDays(i));
            }

            // 创建完整的结果列表（确保包含所有日期）
            Map<LocalDate, DailyWordCount> resultMap = dbResults.stream()
                    .collect(Collectors.toMap(DailyWordCount::getDate, Function.identity()));

            List<DailyWordCount> fullResults = new ArrayList<>();
            for (LocalDate date : dateRange) {
                DailyWordCount count = resultMap.getOrDefault(date,
                        new DailyWordCount(date, 0, 0));
                fullResults.add(count);
            }

            return fullResults;
        } catch (Exception e) {
            log.error("获取周学习量失败: 用户={}, 错误={}", userId, e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 获取用户过去一周的单词学习量
     * @param userId 用户ID
     * @return 按日期分组的学习量统计
     */
    public List<DailyWordCount> getWeeklyWordMaster(Long userId) {
        try {
            // 获取数据库查询结果
            List<DailyWordCount> dbResults = userWordMapper.getWeeklyWordCount(userId);

            // 生成过去7天的日期范围
            List<LocalDate> dateRange = new ArrayList<>();
            for (int i = 6; i >= 0; i--) {
                dateRange.add(LocalDate.now().minusDays(i));
            }

            // 创建完整的结果列表（确保包含所有日期）
            Map<LocalDate, DailyWordCount> resultMap = dbResults.stream()
                    .collect(Collectors.toMap(DailyWordCount::getDate, Function.identity()));

            List<DailyWordCount> fullResults = new ArrayList<>();
            for (LocalDate date : dateRange) {
                DailyWordCount count = resultMap.getOrDefault(date,
                        new DailyWordCount(date, 0, 0));
                fullResults.add(count);
            }

            return fullResults;
        } catch (Exception e) {
            log.error("获取周学习量失败: 用户={}, 错误={}", userId, e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 数据分析接口
     * @param user_id
     * @return
     */
    public DataAnalysisVO dataAnalysis(Long user_id) {
        StudyData studyData = userWordMapper.getStudyDataById(user_id);
        loginMapper.sumLearnWord(user_id);
        LearnPlan learnPlan = loginMapper.getLearnPlan(user_id);
        UserInformation inform = loginMapper.getUserInformByUserId(user_id);

        // 已掌握单词数据
        long wordMastered = userWordMapper.getWordMastered(user_id);

        List<DailyWordCount> dailyWordList = getWeeklyWordCount(user_id);
        Long lastWeekLearned = 0L;
        for (int i = 0; i < dailyWordList.size(); i++) {
            lastWeekLearned += dailyWordList.get(i).getWordCount();
        }

        List<DailyWordCount> lastWeekMasterList = getWeeklyWordMaster(user_id);
        Long lastWeekMaster = 0L;
        for (int i = 0; i < lastWeekMasterList.size(); i++) {
            lastWeekMaster += lastWeekMasterList.get(i).getWordCount();
        }

        int totalToday = learnPlan.getDailyNew() + learnPlan.getDailyOld();

        Long predictTime = 0L;
        // 预计用时
        if(( lastWeekMaster / 7 / 24) > 0){
            predictTime =  ( totalToday / ( lastWeekMaster / 7 / 24) ) * 60;
        }
        else {
            predictTime = ( totalToday / 20L ) * 60;
        }

        Double averageEfficiency = (double)inform.getLearnWord() / inform.getLearnDay();
        BigDecimal avg = new BigDecimal(averageEfficiency);
        double avg_form = avg.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();

        Double efficiencyChange = (double) lastWeekMaster / 7 ;
        Double ecchange = (averageEfficiency - efficiencyChange) / efficiencyChange;
        log.info("【学习效率变化率】：{}",ecchange);
        double ec_change = 0;
        if(ecchange.isNaN()){
            log.info("【学习效率变化率】【NaN异常】");
            ec_change = Double.NaN;
        }
        else {
            avg = new BigDecimal(ecchange);
            ec_change = avg.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }


        Double memoryPersistence =(double) wordMastered / inform.getLearnWord();

        Double persistenceChange = (double) lastWeekMaster / (double) lastWeekLearned ;
        Double pcchange = (memoryPersistence - persistenceChange) / persistenceChange;

        double pc_change = 0;
        if(pcchange.isNaN()){
            pc_change = Double.NaN;
        }
        else {
            avg = new BigDecimal(pcchange);
            pc_change = avg.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }



        Double mwchange =((double)wordMastered - (double)lastWeekMaster) / (double)lastWeekMaster;
        double mw_change = 0;
        if (mwchange.isNaN()){
            mw_change = Double.NaN;
        }
        else {
            avg = new BigDecimal(mwchange);
            mw_change = avg.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        DataAnalysisVO dataAnalysisVO = DataAnalysisVO.builder()
                .totalLearningDays(inform.getLearnDay())
                .continuousLearningDays(3L)
                .masteredWords(wordMastered)
                .masteredWordsChange(mw_change)
                .averageEfficiency(avg_form)
                .memoryPersistence(memoryPersistence)
                .efficiencyChange(ec_change)
                .persistenceChange(pc_change).build();

        return dataAnalysisVO;
    }
}