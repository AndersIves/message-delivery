package cn.z9n.delivery.param;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.HashSet;

/**
 * @Author: 张子玄(罗小黑) YCKJ3690
 * @Date: 2021/5/24 15:43
 */
@Data
@Builder
public class MethodInfo {
    private Class<?> controllerType;
    private Object controller;
    private Method method;
}
