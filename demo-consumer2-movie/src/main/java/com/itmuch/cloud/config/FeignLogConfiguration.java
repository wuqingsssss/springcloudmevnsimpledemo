package com.itmuch.cloud.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignLogConfiguration {
  @Bean
  Logger.Level feignLoggerLevel() {
    /**
     *NONE 不记录任何日志
     *BASIC 仅记录请求方法，url，响应代码，执行时间
     * HEADERS 在basic的基础上记录headers
     * FULL 记录请求和响应的header，body，元数据
     * */
    return Logger.Level.FULL;
  }
}
