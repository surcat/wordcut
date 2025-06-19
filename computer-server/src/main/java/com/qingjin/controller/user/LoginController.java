package com.qingjin.controller.user;

import com.qingjin.constant.JwtClaimsConstant;
import com.qingjin.dto.LearnPlanDTO;
import com.qingjin.dto.LoginDTO;
import com.qingjin.dto.RegisterDTO;
import com.qingjin.entity.User;
import com.qingjin.entity.UserInformation;
import com.qingjin.properties.JwtProperties;
import com.qingjin.result.Result;
import com.qingjin.service.LoginService;
import com.qingjin.utils.JwtUtil;
import com.qingjin.vo.LearnPlanVO;
import com.qingjin.vo.LoginVO;
import com.qingjin.vo.UserInformVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 员工登录接口
     * @param loginDTO
     * @return
     */
    @CrossOrigin
    @PostMapping("/login")
    @ApiOperation("用户登录接口")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO){
        log.info("接收到登录请求:{}",loginDTO);

        // 根据传入的登录信息获取对应的数据库里面的数据
        User user = loginService.login(loginDTO);

        // 创建claims字典，用于存放JWT令牌里面存储的数据信息
        Map<String,Object> claims = new HashMap<>();
        // 将用户id存入JWT令牌
        claims.put(JwtClaimsConstant.USER_ID,user.getId());

        // 生成JWT令牌 —— 签名、持续时间、数据
        String token = JwtUtil.createJWT(jwtProperties.getKey(),
                                        jwtProperties.getTtl(),
                                        claims);

        // 将数据转化为前端需求的数据
        LoginVO loginVO = LoginVO.builder()
                                .id(user.getId())
                                .username(user.getUsername())
                                .token(token)
                                .build();


        return Result.success(loginVO,"登录成功");
    }

    /**
     * 员工注册接口
     * @return
     */
    @CrossOrigin
    @PostMapping("/reg")
    @ApiOperation("用户注册接口")
    public Result register(@RequestBody RegisterDTO registerDTO){
        log.info("接收到注册请求:{}",registerDTO);
        loginService.register(registerDTO);
        return Result.success("注册成功");
    }

    /**
     * 获取用户信息接口
     * @return
     */
    @CrossOrigin
    @GetMapping("/inform")
    @ApiOperation("获取用户信息接口")
    public Result getUserInform(){
        log.info("接收到账号信息查询请求");

        UserInformVO userInformVO = loginService.getUserInform();

        return Result.success(userInformVO,"账号信息");
    }

    /**
     * 获取用户名接口
     * @return
     */
    @CrossOrigin
    @GetMapping("/get/username")
    @ApiOperation("获取用户名接口")
    public Result getUsername(){
        log.info("接收到账号名查询请求");

        String username = loginService.getUsername();

        return Result.success(username,"账号名");
    }

    /**
     * 重置账户密码接口
     * @return
     */
    @CrossOrigin
    @GetMapping("/reset/password")
    @ApiOperation("获取用户名接口")
    public Result resetPassword(@RequestParam(name="old") String old_password,@RequestParam(name="new") String new_password){
        log.info("接收到账号名查询请求");

        loginService.resetPassword(old_password,new_password);

        return Result.success("密码重置成功");
    }

    /**
     * 获取用户学习计划接口
     * @return
     */
    @CrossOrigin
    @GetMapping("/get/plan")
    @ApiOperation("获取用户学习计划接口")
    public Result getPlan(){
        log.info("接收到获取用户学习计划查询请求");

        LearnPlanVO learnPlanVO = loginService.getPlan();

        return Result.success(learnPlanVO,"学习计划");
    }

    @CrossOrigin
    @PostMapping("/save/plan")
    @ApiOperation("更新用户学习计划接口")
    public Result savePlan(@RequestBody LearnPlanDTO learnPlanDTO){
        log.info("接收到更新用户学习计划查询请求");

        loginService.savePlan(learnPlanDTO);

        return Result.success();
    }

}
