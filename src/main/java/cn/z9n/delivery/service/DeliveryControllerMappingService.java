package cn.z9n.delivery.service;

import cn.z9n.delivery.MessagePathExtractor;
import cn.z9n.delivery.param.MethodInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @Author: 张子玄(罗小黑) YCKJ3690
 * @Date: 2021/5/25 14:52
 */
@Slf4j
public class DeliveryControllerMappingService {

    private final MessageDeliveryRegister messageDeliveryRegister;

    private final ApplicationContext applicationContext;

    public DeliveryControllerMappingService(ApplicationContext applicationContext, MessageDeliveryRegister messageDeliveryRegister) {
        this.messageDeliveryRegister = messageDeliveryRegister;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    private void init() {
        for (Map<String, Set<MethodInfo>> pathRoute : messageDeliveryRegister.getRouteMap().values()) {
            for (Set<MethodInfo> methodInfos : pathRoute.values()) {
                for (MethodInfo methodInfo : methodInfos) {
                    methodInfo.setController(applicationContext.getBean(methodInfo.getControllerType()));
                }
            }
        }
    }

    public <Model> Set<MethodInfo> routeToMethodInfos(String dispatchPath, MessagePathExtractor<Model> extractor) {
        Map<Class<? extends MessagePathExtractor<?>>, Map<String, Set<MethodInfo>>> routeMap = messageDeliveryRegister.getRouteMap();
        Map<String, Set<MethodInfo>> pathRouteMap = routeMap.get(extractor.getClass());
        if (pathRouteMap == null) {
            log.warn("未找到extractor:{}对应的路由", extractor.getClass().getName());
            return Collections.emptySet();
        } else {
            Set<MethodInfo> methodRouts = pathRouteMap.get(dispatchPath);
            if (methodRouts == null) {
                log.warn("未找到extractor:{}, path:{}对应的路由", extractor.getClass().getName(),dispatchPath);
                return Collections.emptySet();
            } else {
                return methodRouts;
            }
        }
    }
}
