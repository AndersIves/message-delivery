package cn.z9n.demo;

import cn.z9n.delivery.MessageDeliveryService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @Author: 张子玄(罗小黑) YCKJ3690
 * @Date: 2021/5/24 14:13
 */
@Component
public class Runner implements ApplicationRunner {

    @Autowired
    MessageDeliveryService messageDeliveryService;

    @Autowired
    TestExtractor testExtractor;

    @Autowired
    TestExtractor2 testExtractor2;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //noinspection AlibabaAvoidManuallyCreateThread
        new Thread(() -> {
            while (true) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("path", "/home/" + (Math.random() > 0.5));
                    jsonObject.put("path2", "/home/" + (Math.random() > 0.5));
                    jsonObject.put("time", System.currentTimeMillis());
                    messageDeliveryService.delivery(jsonObject, testExtractor);
                    messageDeliveryService.delivery(jsonObject, testExtractor2);
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
