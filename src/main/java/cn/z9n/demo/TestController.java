package cn.z9n.demo;

import cn.z9n.delivery.annos.DeliveryController;
import cn.z9n.delivery.annos.DeliveryMapping;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: 张子玄(罗小黑) YCKJ3690
 * @Date: 2021/5/24 14:05
 */
@Slf4j
@DeliveryController(value = "/home", extractor = TestExtractor.class)
public class TestController {

    @DeliveryMapping("/true")
    private void homeTrue(JSONObject o) {
        log.info("homeTrue:{}", JSON.toJSONString(o));
    }

}
