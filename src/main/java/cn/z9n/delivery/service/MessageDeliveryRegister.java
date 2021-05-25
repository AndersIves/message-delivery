package cn.z9n.delivery.service;

import cn.z9n.delivery.MessagePathExtractor;
import cn.z9n.delivery.annos.DeliveryController;
import cn.z9n.delivery.annos.DeliveryMapping;
import cn.z9n.delivery.param.MethodInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author: 张子玄(罗小黑) YCKJ3690
 * @Date: 2021/5/24 14:40
 */
@Slf4j
public class MessageDeliveryRegister implements PriorityOrdered, BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] deliveryControllerBeanNames = beanFactory.getBeanNamesForAnnotation(DeliveryController.class);
        for (String deliveryControllerBeanName : deliveryControllerBeanNames) {
            Class<?> controllerType = beanFactory.getType(deliveryControllerBeanName);
            assert controllerType != null;
            DeliveryController deliveryController = controllerType.getAnnotation(DeliveryController.class);
            String basePath = deliveryController.value();
            Class<? extends MessagePathExtractor<?>> extractorType = deliveryController.extractor();
            Method[] deliveryControllerMethods = controllerType.getDeclaredMethods();
            for (Method method : deliveryControllerMethods) {
                DeliveryMapping deliveryMapping = method.getAnnotation(DeliveryMapping.class);
                if (deliveryMapping == null) {
                    continue;
                }
                Class<?>[] methodParameterTypes = method.getParameterTypes();
                try {
                    extractorType.getDeclaredMethod("getDispatchPath", methodParameterTypes);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("controller入参有误, 应与对应extractor泛型保持一致, 或定义为java.lang.Object");
                }
                String[] paths = deliveryMapping.value();
                addExtractor(extractorType, basePath, paths, controllerType, method);
            }
        }
        if (log.isDebugEnabled()) {
            JSONObject route = new JSONObject();
            for (Map.Entry<Class<? extends MessagePathExtractor<?>>, Map<String, Set<MethodInfo>>> classMapEntry : routeMap.entrySet()) {
                JSONObject extractorRoute = new JSONObject();
                for (Map.Entry<String, Set<MethodInfo>> stringSetEntry : classMapEntry.getValue().entrySet()) {
                    JSONArray pathRoute = new JSONArray();
                    for (MethodInfo methodInfo : stringSetEntry.getValue()) {
                        pathRoute.add(methodInfo.getControllerType().getName() + "#" + methodInfo.getMethod().getName());
                    }
                    extractorRoute.put(stringSetEntry.getKey(), pathRoute);
                }
                route.put(classMapEntry.getKey().getName(), extractorRoute);
            }
            log.debug("完成DeliveryController扫描加载, 加载结果:{}", JSON.toJSONString(route, true));
        }
    }

    private final Map<Class<? extends MessagePathExtractor<?>>, Map<String, Set<MethodInfo>>> routeMap = new HashMap<>();

    private void addExtractor(Class<? extends MessagePathExtractor<?>> extractor, String basePath, String[] paths, Class<?> deliveryControllerType, Method method) {
        Map<String, Set<MethodInfo>> methodRouteMap = routeMap.computeIfAbsent(extractor, key -> new HashMap<>(paths.length));
        for (String path : paths) {
            Set<MethodInfo> methodInfos = methodRouteMap.computeIfAbsent(basePath + path, key -> new HashSet<>());
            method.setAccessible(true);
            methodInfos.add(MethodInfo.builder()
                    .controllerType(deliveryControllerType)
                    .method(method)
                    // 此处不能取出bean, 会导致后期注入问题, bean在DeliveryControllerMappingService中指定
                    .controller(null)
                    .build());
        }
    }

    public Map<Class<? extends MessagePathExtractor<?>>, Map<String, Set<MethodInfo>>> getRouteMap() {
        return routeMap;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
