package com.qingjin.controller.user;

import com.qingjin.context.BaseContext;
import com.qingjin.vo.InformCardVO;
import com.qingjin.dto.StudyLogDTO;
import com.qingjin.entity.Word;
import com.qingjin.result.Result;
import com.qingjin.service.WordService;
import com.qingjin.vo.StudyDanciVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @className: WordController
 * @author: qingjin
 * @description: TODO
 * @date: 2025/6/19 6:59
 * @version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/word")
public class WordController {
    @Autowired
    private WordService wordService;

    @CrossOrigin
    @PostMapping("/upload")
    @ApiOperation("单词上传接口")
    public Result upload(@RequestBody List<Word> wordList){
        Long user_id =(Long) BaseContext.getContext();
        log.info("获取到【用户{}】上传单词请求：上传单词量：【{}】",user_id,wordList.size());

        // 单词更新函数
        wordService.reliableUpload(wordList,user_id);

        return Result.success("上传成功");
    }

    @CrossOrigin
    @GetMapping("/getList")
    @ApiOperation("单词获取接口")
    public Result<List<Word>> getList(){
        Long user_id =(Long) BaseContext.getContext();
        log.info("获取到上传单词请求，用户ID：【{}】",user_id);

        // 单词更新函数
        List<Word> wordList = wordService.getList(user_id);

        return Result.success(wordList,"单词列表");
    }

    @CrossOrigin
    @PutMapping("/setState")
    @ApiOperation("单词状态变更接口")
    public Result setLearnWord(@RequestParam Integer state,@RequestParam Long id){
        Long user_id =(Long) BaseContext.getContext();
        log.info("获取到单词请求，用户ID：【{}】",user_id);

        wordService.setState(user_id,state,id);

        return Result.success("单词状态更新成功");
    }

    @CrossOrigin
    @GetMapping("/study")
    @ApiOperation("单词背诵页面数据加载接口")
    public Result<StudyDanciVO> study(){
        log.info("获取到单词背诵页面数据渲染请求");

        StudyDanciVO studyDanciVO = wordService.study();

        return Result.success(studyDanciVO,"背诵页面数据加载成功");
    }

    @CrossOrigin
    @PostMapping("/log")
    @ApiOperation("学习日志保存接口")
    public Result studylog(@RequestBody StudyLogDTO StudyLogDTO){
        Long user_id =(Long) BaseContext.getContext();
        log.info("获取到学习日志保存请求，【用户{}】",user_id);

        // 记录日志
        wordService.studylog(user_id,StudyLogDTO);

        return Result.success("学习日志保存成功");
    }

    @CrossOrigin
    @GetMapping("/informCard")
    @ApiOperation("学习日志保存接口")
    public Result<InformCardVO> informCard(){
        Long user_id =(Long) BaseContext.getContext();
        log.info("获取到学习日志保存请求，【用户{}】",user_id);

        // 记录日志
        InformCardVO informCardVO = wordService.informCard(user_id);

        return Result.success(informCardVO,"学习信息卡");
    }

}
