package com.qingjin.mapper;

import com.qingjin.entity.Word;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface WordMapper {

    boolean existsByWord(String word);
    Set<String> existsByWords(@Param("words") List<String> words);
    int insert(Word word);
    int batchInsert(List<Word> words);
    Long findIdByWord(String word);
    Map<String, Long> findIdsByWords(@Param("words") List<String> words);


    /*void getOrCreateWordId(@Param("word") String word,
                           @Param("phonetic") String phonetic,
                           @Param("definition") String definition,
                           @Param("example") String example,
                           @Param("wordId") Long wordId);*/

    /*List<Word> getWordList(
            @Param("dailyNew") Integer dailyNew,
            @Param("dailyOld") Integer dailyOld,
            @Param("user_id") Long user_id
    );*/

}
