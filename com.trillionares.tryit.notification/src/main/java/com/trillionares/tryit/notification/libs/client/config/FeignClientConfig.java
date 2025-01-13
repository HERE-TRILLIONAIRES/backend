package com.trillionares.tryit.notification.libs.client.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class FeignClientConfig {

  private final RequestHeaderProvider requestHeaderProvider;

  public FeignClientConfig(RequestHeaderProvider requestHeaderProvider) {
    this.requestHeaderProvider = requestHeaderProvider;
  }

  @Bean
  public RequestInterceptor requestInterceptor() {
    return requestTemplate -> {
      String username = requestHeaderProvider.getUsername();
      String role = requestHeaderProvider.getRole();

      if (username != null) {
        requestTemplate.header("X-Auth-Username", username);
      }
      if (role != null) {
        requestTemplate.header("X-Auth-Role", role);
      }
    };
  }

}
