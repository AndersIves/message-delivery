package cn.z9n.delivery.service;

import cn.z9n.delivery.MessageDeliveryService;
import cn.z9n.delivery.MessagePathExtractor;
import cn.z9n.delivery.config.DeliveryConfig;
import cn.z9n.delivery.param.MethodInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: 张子玄(罗小黑) YCKJ3690
 * @Date: 2021/5/24 14:02
 */
@Slf4j
public class MessageDeliveryServiceImpl implements MessageDeliveryService {
    private final DeliveryControllerMappingService deliveryControllerMappingService;

    private final ThreadPoolExecutor executor;

    public MessageDeliveryServiceImpl(DeliveryConfig deliveryConfig, DeliveryControllerMappingService deliveryControllerMappingService) {
        this.deliveryControllerMappingService = deliveryControllerMappingService;
        this.executor = new ThreadPoolExecutor(
                deliveryConfig.getCorePoolSize(),
                deliveryConfig.getMaximumPoolSize(),
                deliveryConfig.getKeepAliveTime(),
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(deliveryConfig.getCacheQueueSize()),
                new ThreadFactory() {

                    private final AtomicInteger mThreadNum = new AtomicInteger(1);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "messageDelivery-thread-" + mThreadNum.getAndIncrement());
                    }
                });
    }

    @Override
    public <Model> void delivery(Model model, MessagePathExtractor<Model> extractor) throws Exception {
        long startTime = System.currentTimeMillis();
        String dispatchPath;
        try {
            dispatchPath = extractor.getDispatchPath(model);
        } catch (Exception e) {
            log.error("MessagePathExtractor解析分发路径异常, message:{}",e.getMessage(), e);
            throw e;
        }
        if (System.currentTimeMillis() - startTime > 500) {
            log.warn("MessagePathExtractor解析分发路径时间超过0.5s, 请考虑优化分发路径处理逻辑, extractor:{}", extractor.getClass().getName());
        }

        log.debug("开始分发调用controller, 目标path:{}", dispatchPath);
        Set<MethodInfo> methodInfos = deliveryControllerMappingService.routeToMethodInfos(dispatchPath, extractor);
        for (MethodInfo methodInfo : methodInfos) {
            executor.execute(() -> {
                try {
                    methodInfo.getMethod().invoke(methodInfo.getController(), model);
                } catch (Exception e) {
                    log.error("MessageDeliveryService分发异常, message:{}", e.getMessage(), e);
                }
            });
        }
    }
}
