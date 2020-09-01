package com.itmuch.cloud.study.config;

import com.itmuch.cloud.config.RibbonConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;

/**
 * 使用RibbonClient，为特定name的Ribbon Client自定义配置.使用注解方法向ribbon中注入新的配置
 * 使用@RibbonClient的configuration属性，指定Ribbon的配置类.
 * 可参考的示例：
 * http://spring.io/guides/gs/client-side-load-balancing/
 * @author 周立
 */
/** 通过java 脚本修改Ribbon的配置
@Configuration
@RibbonClient(name = "demo-provider-user", configuration = RibbonConfiguration.class)
public class TestConfiguration {
}
 */