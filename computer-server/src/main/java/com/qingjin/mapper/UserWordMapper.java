package com.qingjin.mapper;

import com.qingjin.entity.StudyData;
import com.qingjin.entity.StudyLog;
import com.qingjin.entity.UserWord;
import com.qingjin.entity.Word;
import com.qingjin.vo.DailyWordCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserWordMapper {
    int upsertUserWord(@Param("userId") Long userId, 
                      @Param("wordId") Long wordId, 
                      @Param("isMastered") Integer isMastered, 
                      @Param("lastReview") java.time.LocalDate lastReview);
    
    int batchUpsertUserWords(@Param("list") List<UserWord> userWords);

    List<Word> getWordList(Integer dailyNew, Integer dailyOld, Long userId);

    /**
     *
     * 单词状态更新函数
     * @param user_id
     * @param state
     * @param id
     */
    void setState(Long user_id, Integer state, Long id);

    void updateWord(Long user_id, Integer state);

    /**
     * 根据ID获取学习登记表
     * @param id
     * @return
     */
    @Select("select * from study_data where user_id=#{id};")
    StudyData getStudyDataById(Long id);

    /**
     * 插入日志
     * @param studyLog
     */
    void addLog(StudyLog studyLog);

    long getWordMastered(Long user_id);

    /**
     * 获取用户过去一周的单词学习量
     * @param userId 用户ID
     * @return 按日期分组的学习量统计
     */
    List<DailyWordCount> getWeeklyWordCount(Long userId);

    List<DailyWordCount> getWeeklyWordMaster(Long userId);
}