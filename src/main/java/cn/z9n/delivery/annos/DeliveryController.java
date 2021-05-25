package cn.z9n.delivery.annos;

import cn.z9n.delivery.MessagePathExtractor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author: 张子玄(罗小黑) YCKJ3690
 * @Date: 2021/5/24 13:41
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface DeliveryController {
    String value() default "";
    Class<? extends MessagePathExtractor<?>> extractor();
}
