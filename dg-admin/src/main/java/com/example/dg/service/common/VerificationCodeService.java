package com.example.dg.service.common;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.example.dg.common.core.redis.RedisCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.aliyun.teautil.Common.toJSONString;

/**
 * @author xinxin
 * @version 1.0
 */
@Service
public class VerificationCodeService {
    @Resource
    private RedisCache redisCache;

    public String generateVerificationCode() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int num = random.nextInt(10); // 生成0-9的数字
            builder.append(num);
        }
        return builder.toString();
    }

    public void sendVerificationCode(String phoneNumber) throws Exception {
        String verificationCode = generateVerificationCode();
        Config config = new Config()
                // 请确保代码运行环境配置了相应环境变量
                .setAccessKeyId(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID"))
                .setAccessKeySecret(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET"));
        config.endpoint = "dysmsapi.aliyuncs.com";
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phoneNumber)
                .setSignName("粤韵非遗文化传承平台")
                .setTemplateCode("SMS_494800046")
                .setTemplateParam("{\"code\":" + verificationCode + "}");
        Client client = new Client(config);
        SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);// TemplateParam为序列化后的JSON字符串
        System.out.println(toJSONString(sendSmsResponse));
        // 将验证码存储到 Redis，并设置过期时间为1分钟
        redisCache.setCacheObject("code:" + phoneNumber, verificationCode, 5, TimeUnit.MINUTES);
    }

    public void removeVerificationCode(String phoneNumber) {
        // 从Redis中删除验证码
        redisCache.deleteObject("code:" + phoneNumber);
    }
}
