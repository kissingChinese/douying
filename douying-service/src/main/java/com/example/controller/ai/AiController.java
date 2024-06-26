package com.example.controller.ai;
import cn.hutool.core.util.StrUtil;
import com.example.client.XfXhStreamClient;
import com.example.config.XfXhConfig;
import com.example.dto.MsgDTO;
import com.example.listen.XfXhWebSocketListener;
import com.example.util.R;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("/douying/ai")
@Slf4j
public class AiController {

    @Resource
    private XfXhStreamClient xfXhStreamClient;

    @Resource
    private XfXhConfig xfXhConfig;


    /**
     * 发送问题
     *
     * @param question 问题
     * @return 星火大模型的回答
     */
    @GetMapping("/sendQuestion")
    public R sendQuestion(@RequestParam("question") String question) {
        log.info("==============================================>接口已经被请求");
        // 如果是无效字符串，则不对大模型进行请求
        if (StrUtil.isBlank(question)) {
            return R.error().data( "无效问题，请重新输入");
        }
        // 获取连接令牌
        if (!xfXhStreamClient.operateToken(XfXhStreamClient.GET_TOKEN_STATUS)) {
            return R.error().data("当前大模型连接数过多，请稍后再试");
        }

        // 创建消息对象
        MsgDTO msgDTO = MsgDTO.createUserMsg(question);
        // 创建监听器
        XfXhWebSocketListener listener = new XfXhWebSocketListener();
        // 发送问题给大模型，生成 websocket 连接
        WebSocket webSocket = xfXhStreamClient.sendMsg(UUID.randomUUID().toString().substring(0, 10), Collections.singletonList(msgDTO), listener);
        if (webSocket == null) {
            // 归还令牌
            xfXhStreamClient.operateToken(XfXhStreamClient.BACK_TOKEN_STATUS);
            return R.error().data("系统内部错误，请联系管理员");
        }
        try {
            int count = 0;
            // 为了避免死循环，设置循环次数来定义超时时长
            int maxCount = xfXhConfig.getMaxResponseTime() * 5000;
            while (count <= maxCount) {
                Thread.sleep(200);
                if (listener.isWsCloseFlag()) {
                    break;
                }
                count++;
            }
            if (count > maxCount) {
                return R.error().data("大模型响应超时，请联系管理员");
            }
            // 响应大模型的答案
            return R.ok().data(listener.getAnswer().toString());
        } catch (InterruptedException e) {
            log.error("错误：" + e.getMessage());
            return R.error().data("系统内部错误，请联系管理员");
        } finally {
            // 关闭 websocket 连接
            webSocket.close(1000, "");
            // 归还令牌
            xfXhStreamClient.operateToken(XfXhStreamClient.BACK_TOKEN_STATUS);
        }
    }
}

