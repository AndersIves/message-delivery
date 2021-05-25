package cn.z9n.delivery.configuration;

import cn.z9n.delivery.config.DeliveryConfig;
import cn.z9n.delivery.service.DeliveryControllerMappingService;
import cn.z9n.delivery.service.MessageDeliveryRegister;
import cn.z9n.delivery.service.MessageDeliveryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 张子玄(罗小黑) YCKJ3690
 * @Date: 2021/5/25 14:56
 */
@Configuration
@ConditionalOnProperty(prefix = "z9n.delivery", name = "enable", havingValue = "true")
public class MessageDeliveryConfiguration {
    @Bean("messageDeliveryRegister")
    protected MessageDeliveryRegister messageDeliveryRegister() {
        return new MessageDeliveryRegister();
    }

    @Bean("deliveryConfig")
    protected DeliveryConfig deliveryConfig() {
        return new DeliveryConfig();
    }

    @Bean("deliveryControllerMappingService")
    protected DeliveryControllerMappingService deliveryControllerMappingService(
            @Autowired ApplicationContext applicationContext,
            @Qualifier("messageDeliveryRegister") MessageDeliveryRegister messageDeliveryRegister
    ) {
        return new DeliveryControllerMappingService(applicationContext, messageDeliveryRegister);
    }

    @Bean("messageDeliveryService")
    protected MessageDeliveryServiceImpl messageDeliveryService(
            @Qualifier("deliveryConfig") DeliveryConfig deliveryConfig,
            @Qualifier("deliveryControllerMappingService") DeliveryControllerMappingService deliveryControllerMappingService
    ) {
        return new MessageDeliveryServiceImpl(deliveryConfig,deliveryControllerMappingService);
    }

}
