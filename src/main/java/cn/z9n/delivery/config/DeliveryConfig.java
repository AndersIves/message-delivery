package cn.z9n.delivery.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 张子玄(罗小黑) YCKJ3690
 * @Date: 2021/5/25 15:45
 */
@Data
@ConfigurationProperties(prefix = "z9n.delivery")
public class DeliveryConfig {
    /**
     * 是否启用delivery
     */
    private boolean enable = false;
    /**
     * 分发核心线程池大小
     */
    private int corePoolSize = 20;
    /**
     * 分发最大线程池大小
     */
    private int maximumPoolSize = 50;
    /**
     * keepAlive时间 毫秒
     */
    private long keepAliveTime = 200;
    /**
     * 缓冲队列大小
     */
    private int cacheQueueSize = 500;
}
