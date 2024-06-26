package com.example.limit;
import com.example.constant.RedisConstant;
import com.example.exception.LimiterException;
import com.example.holder.UserHolder;
import com.example.util.RedisCacheUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Aspect
@Component
public class LimiterAop {

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    /**
     * 拦截
     * @param joinPoint
     * @param limiter
     * @return
     * @throws Throwable
     */
    @Around("@annotation(limiter)")
    public Object restriction(ProceedingJoinPoint joinPoint, Limit limiter) throws Throwable {
        final Long userId = UserHolder.get();
        final int limitCount = limiter.limit();
        final String msg = limiter.msg();
        final long time = limiter.time();
        // 缓存是否存在
        String key = RedisConstant.VIDEO_LIMIT + userId;
        final Object o1 = redisCacheUtil.get(key);
        if (ObjectUtils.isEmpty(o1)){
            redisCacheUtil.set(key,1,time);
        }else {
            if (Integer.parseInt(o1.toString()) > limitCount){
                throw new LimiterException(msg);
            }
            redisCacheUtil.incr(key,1);
        }
        Object o = joinPoint.proceed();
        return o;
    }


}