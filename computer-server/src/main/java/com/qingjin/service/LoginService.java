package com.qingjin.service;

import com.qingjin.constant.MessageConstant;
import com.qingjin.context.BaseContext;
import com.qingjin.dto.LearnPlanDTO;
import com.qingjin.dto.LoginDTO;
import com.qingjin.dto.RegisterDTO;
import com.qingjin.entity.LearnPlan;
import com.qingjin.entity.User;
import com.qingjin.entity.UserInformation;
import com.qingjin.exception.AccountDataIllegalException;
import com.qingjin.exception.AccountHadExistException;
import com.qingjin.exception.AccountNotExistException;
import com.qingjin.exception.PasswordErrorException;
import com.qingjin.mapper.LoginMapper;
import com.qingjin.vo.LearnPlanVO;
import com.qingjin.vo.UserInformVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 登录接口逻辑层
 */
@Service
@Slf4j
public class LoginService {
    @Autowired
    private LoginMapper loginMapper;


    /**
     * 登录验证
     * @param loginDTO
     * @return 登录验证是否成功
     */
    public User login(LoginDTO loginDTO) {
        // 提取loginDTO中的数据对象
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        // 调用登录持久层的getByUsername接口，来查询对应员工姓名
        User user = loginMapper.getByUsername(username);

        if(user == null){
            throw new AccountNotExistException(MessageConstant.ACCOUNT_NOT_EXIST);
        }

        // 将密码字段进行MD-5加密
        // password = DigestUtils.md5DigestAsHex(password.getBytes());

        if(!password.equals(user.getPassword())){
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        // 集成签到功能
        loginMapper.updateLearnDay(user.getId());

        return user;

    }

    /**
     * 用户注册服务
     * @param registerDTO
     */
    public void register(RegisterDTO registerDTO) {
        // 先对数据进行合法检验
        if(registerDTO.getUsername().length() < 1 || registerDTO.getPassword().length() < 1){
            throw new AccountDataIllegalException();
        }

        // 提取loginDTO中的数据对象
        String username = registerDTO.getUsername();

        // 调用登录持久层的getByUsername接口，来查询对应员工姓名
        User user = loginMapper.getByUsername(username);

        // 判断账号名是否重复
        if(user != null){
            log.info("用户已存在");
            throw new AccountHadExistException();
        }

        // 添加新账号
        loginMapper.insertUser(registerDTO);

        // 在用户数据表中添加对应的用户信息
        Long id = loginMapper.getByUsername(username).getId();
        loginMapper.insertUserInform(id);

        // 在学习计划表中添加对应用户的学习计划
        loginMapper.insertLearnPath(id);

        // 在学习登记表中添加表项
        loginMapper.insertStudyDay(id);
        loginMapper.updataLastTime(id);
    }

    /**
     * 用户信息提供服务
     * @return
     */
    public UserInformVO getUserInform() {
        Long user_id = (Long) BaseContext.getContext();

        log.info("数据线程数据:{}",user_id);

        // 获取用户信息对象
        loginMapper.sumLearnWord(user_id);
        User user = loginMapper.getUserById(user_id);
        UserInformation userInformation = loginMapper.getUserInformByUserId(user_id);

        // 根据提供数据，封装转成前端类
        UserInformVO userInformVO = UserInformVO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .learnDay(userInformation.getLearnDay())
                .learnWord(userInformation.getLearnWord())
                .build();

        return userInformVO;
    }

    /**
     * 用户名提供服务
     * @return
     */
    public String getUsername() {
        Long user_id = (Long) BaseContext.getContext();

        log.info("数据线程数据:{}",user_id);

        String username = loginMapper.getUsername(user_id);

        return username;
    }

    /**
     * 重置密码服务
     * @param old_password
     * @param new_password
     */
    public void resetPassword(String old_password, String new_password) {
        Long user_id = (Long) BaseContext.getContext();

        log.info("数据线程数据:{}",user_id);
        User user = loginMapper.getUserById(user_id);

        if(!user.getPassword().equals(old_password)){
            throw new PasswordErrorException();
        }

        loginMapper.resetPassword(new_password,user_id);

    }

    /**
     * 获取学习计划服务
     * @return
     */
    public LearnPlanVO getPlan() {
        Long user_id = (Long) BaseContext.getContext();

        log.info("数据线程数据:{}",user_id);
        LearnPlan plan = loginMapper.getLearnPlan(user_id);
        log.info("学习计划：{}",plan);

        return LearnPlanVO.fromEntity(plan);
    }

    /**
     * 保存学习计划服务
     * @param learnPlanDTO
     */
    public void savePlan(LearnPlanDTO learnPlanDTO) {
        Long user_id = (Long) BaseContext.getContext();

        learnPlanDTO.setUserId(user_id);

        log.info("数据线程数据:{}",user_id);
        loginMapper.updateLearnPlan(learnPlanDTO);

    }
}
