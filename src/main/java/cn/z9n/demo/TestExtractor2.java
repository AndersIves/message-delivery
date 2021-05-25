package cn.z9n.demo;

import cn.z9n.delivery.MessagePathExtractor;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

/**
 * @Author: 张子玄(罗小黑) YCKJ3690
 * @Date: 2021/5/24 14:03
 */
@Component
public class TestExtractor2 implements MessagePathExtractor<JSONObject> {
    @Override
    public String getDispatchPath(JSONObject o) throws Exception {
        Thread.sleep(500);
        return o.getString("path2");
    }
}
