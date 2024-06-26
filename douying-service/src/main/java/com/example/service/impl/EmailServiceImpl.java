package com.example.service.impl;

import com.example.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
@Service
public class EmailServiceImpl implements EmailService {


    @Autowired
    private SimpleMailMessage simpleMailMessage;

    @Autowired
    JavaMailSender javaMailSender;


    /**
     * 邮箱接收验证码
     * @param email
     * @param context
     */
    @Override
    @Async
    public void send(String email, String context) {
        simpleMailMessage.setSubject("抖音验证码");
        simpleMailMessage.setFrom("2934128626@qq.com");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setText(context);
        javaMailSender.send(simpleMailMessage);
    }
}
