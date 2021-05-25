package cn.z9n.demo;

import cn.z9n.delivery.MessagePathExtractor;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

/**
 * @Author: 张子玄(罗小黑) YCKJ3690
 * @Date: 2021/5/24 14:03
 */
@Component
public class TestExtractor implements MessagePathExtractor<JSONObject> {
    @Override
    public String getDispatchPath(JSONObject o) throws Exception {
        return o.getString("path");
    }
}
