package com.example.service;
import com.example.entity.Captcha;
import com.baomidou.mybatisplus.extension.service.IService;
import java.awt.image.BufferedImage;
public interface CaptchaService extends IService<Captcha> {
    BufferedImage getCaptcha(String uuId);

    boolean validate(Captcha captcha) throws Exception;
}
