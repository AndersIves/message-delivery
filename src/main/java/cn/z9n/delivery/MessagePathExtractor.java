package cn.z9n.delivery;

/**
 * @Author: 张子玄(罗小黑) YCKJ3690
 * @Date: 2021/5/24 14:01
 */
public interface MessagePathExtractor<Model> {
    String getDispatchPath(Model model) throws Exception;
}
