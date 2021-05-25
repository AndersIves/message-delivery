package cn.z9n.delivery.annos;

import java.lang.annotation.*;

/**
 * @Author: 张子玄(罗小黑) YCKJ3690
 * @Date: 2021/5/24 13:42
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DeliveryMapping {
    String[] value() default {};
}
