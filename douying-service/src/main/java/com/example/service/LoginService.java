package com.example.service;
import com.example.entity.Captcha;
import com.example.entity.vo.FindPWVO;
import com.example.entity.vo.RegisterVO;
import com.example.user.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
public interface LoginService {
    /**
     * 登录
     * @param user
     * @return
     */
    User login(User user);

    /**
     * 检查验证码
     * @param email
     * @param code
     * @return
     */
    Boolean checkCode(String email,Integer code);

    /**
     * 生成图形验证码
     * @param uuid
     * @param response
     * @throws IOException
     */
    void captcha(String uuid, HttpServletResponse response) throws IOException;

    /**
     * 获取验证码
     * @param captcha
     * @return
     * @throws Exception
     */
    Boolean getCode(Captcha captcha) throws Exception;

    /**
     * 注册账号
     * @param registerVO
     * @return
     * @throws Exception
     */
    Boolean register(RegisterVO registerVO) throws Exception;

    /**
     * 找回密码
     * @param findPWVO
     * @return
     */
    Boolean findPassword(FindPWVO findPWVO);
}
