package cn.z9n.delivery;

/**
 * @Author: 张子玄(罗小黑) YCKJ3690
 * @Date: 2021/5/24 11:40
 */
public interface MessageDeliveryService {
    /**
     *
     * @param model 待传递的参数
     * @param extractor 特征提取器 用于分发
     * @param <Model>
     */
    <Model> void delivery(Model model, MessagePathExtractor<Model> extractor) throws Exception;
}
