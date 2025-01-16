package com.trillionares.tryit.notification.libs.client.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 내부 통신용
 */
@Configuration
public class InternalFeignClientConfig {

  @Bean
  public RequestInterceptor internalRequestInterceptor() {
    return requestTemplate ->
        requestTemplate.header("X-Internal-Call", "true");
  }
}
