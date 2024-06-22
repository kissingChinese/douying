package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.code.kaptcha.Producer;
import com.example.constant.RedisConstant;
import com.example.entity.Captcha;
import com.example.exception.BaseException;
import com.example.mapper.CaptchaMapper;
import com.example.service.CaptchaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.service.EmailService;
import com.example.util.DateUtil;
import com.example.util.RedisCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Date;

/**
 * <p>
 * 系统验证码 服务实现类
 * </p>
 *
 */
@Service
public class CaptchaServiceImpl extends ServiceImpl<CaptchaMapper, Captcha> implements CaptchaService {


    @Autowired
    private Producer producer;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Override
    public BufferedImage getCaptcha(String uuId) {
        String code = this.producer.createText();
        Captcha captcha = new Captcha();
        captcha.setUuid(uuId);
        captcha.setCode(code);
        captcha.setExpireTime(DateUtil.addDateMinutes(new Date(),5));
        this.save(captcha);
        return producer.createImage(code);
    }

    @Override
    public boolean validate(Captcha captcha) throws Exception {
        String email = captcha.getEmail();
        final String code1 = captcha.getCode();
        captcha = this.getOne(new LambdaQueryWrapper<Captcha>().eq(Captcha::getUuid, captcha.getUuid()));
        if (captcha == null) throw new BaseException("uuId为空");

        this.removeById(captcha.getUuid());

        if (!captcha.getCode().equalsIgnoreCase(captcha.getCode()) && captcha.getExpireTime().getTime() >= System.currentTimeMillis()){
            throw new BaseException("uuid过期");
        }
        if (!code1.equals(captcha.getCode())){
            return false;
        }


        String code = getSixCode();
        redisCacheUtil.set(RedisConstant.EMAIL_CODE+email,code,RedisConstant.EMAIL_CODE_TIME);
        emailService.send(email,"注册验证码:"+code+",验证码5分钟之内有效");
        return true;
    }


    public static String getSixCode(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int code = (int) (Math.random()*10);
            builder.append(code);
        }
        return builder.toString();
    }
}
