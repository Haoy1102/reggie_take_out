package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author haoy
 * @description
 * @date 2022/12/3 16:52
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 发送手机短信验证码
     *
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotBlank(phone)) {
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(6).toString();
            log.info("code={}", code);
            //调用阿里云提供的短信服务API完成发送短信
//            SMSUtils.sendMessage("","",phone,code);
            //需要将生成的验证码保存到Session
//            session.setAttribute(phone,code);

            //保存到redis
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);

            return R.success("手机验证码发送成功");
        }
        return R.error("短信发送失败");
    }

    /**
     * 移动端用户登陆
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        //获取手机号和验证码
        String phone = (String) map.get("phone");
        String code = (String) map.get("code");

        //从session中获取保存的验证码
//        String veriCode = (String) session.getAttribute(phone);
        //从redis中获取保存的验证码
        String veriCode=redisTemplate.opsForValue().get(phone);
        //验证成功
        if (null != veriCode && phone != null && veriCode.equals(code)) {
            //当前手机号是否为新用户
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(userLambdaQueryWrapper);
            //不存在
            if (user == null) {
                user=new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            Boolean delete = redisTemplate.delete(phone);
            return R.success(user);
        }
        return R.error("登录失败");
    }
}
