package com.qingjin.mapper;

import com.qingjin.dto.LearnPlanDTO;
import com.qingjin.dto.RegisterDTO;
import com.qingjin.entity.LearnPlan;
import com.qingjin.entity.User;
import com.qingjin.entity.UserInformation;
import com.qingjin.vo.LearnPlanVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 登录接口持久层
 */
@Mapper
public interface LoginMapper {
    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Select("select * from users where username = #{username}")
    User getByUsername(String username);

    /**
     * 向用户表插入信息
     * @param registerDTO
     */
    @Insert("insert into users(username, password, email) " +
            "VALUE (#{username}," +
            "#{password}," +
            "#{email});")
    void insertUser(RegisterDTO registerDTO);

    /**
     * 向用户信息表插入数据
     * @param id
     */
    @Insert("insert into user_information(user_id) " +
            "VALUE(#{id})")
    void insertUserInform(long id);

    /**
     * 通过id获取用户表数据
     * @param id
     * @return
     */
    @Select("SELECT * from users where id = #{id}")
    User getUserById(long id);

    /**
     * 根据用户id查询用户信息
     * @param id
     * @return
     */
    @Select("SELECT * from user_information where user_id = #{id}")
    UserInformation getUserInformByUserId(long id);

    /**
     * 根据id获取用户名
     * @param user_id
     * @return
     */
    @Select("SELECT username from users where id = #{user_id}")
    String getUsername(Long user_id);

    /**
     * 重置用户密码
     * @param password
     * @param id
     */
    @Update("update users set password=#{password} where id=#{id}")
    void resetPassword(String password,Long id);

    /**
     * 学习计划表数据插入
     * @param id
     */
    @Insert("insert into learn_plan(user_id,target_data) value(#{id},NOW()) ")
    void insertLearnPath(Long id);

    /**
     * 根据用户id查找学习计划
     * @param user_id
     * @return
     */
    @Select("SELECT * from learn_plan where user_id = #{user_id}")
    LearnPlan getLearnPlan(Long user_id);

    @Update("update learn_plan set daily_new = #{dailyNew}, " +
            "daily_old = #{dailyOld}, " +
            "target_data = #{targetData}, " +
            "target_test = #{targetTest}, " +
            "learn_mode = #{learnMode} " +
            "where user_id = #{userId}; ")
    void updateLearnPlan(LearnPlanDTO learnPlanDTO);

    /**
     * 新增学习登记表项
     * @param id
     */
    @Insert("insert into study_data(user_id, last_time) " +
            " VALUE (#{id},now())")
    void insertStudyDay(Long id);

    /**
     * 签到功能 —— 日期增加
     * @param id
     */
    void updateLearnDay(Long id);

    @Update("update study_data " +
            "  set last_time = now() " +
            "  where user_id = #{id};")
    void updataLastTime(long id);

    void sumLearnWord(long id);

    void getStudyLogById(long id);
}
