package com.trillionares.tryit.notification.libs.client.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestHeaderProvider {

  private final HttpServletRequest request;

  public String getUsername() {
    return request.getHeader("X-Auth-Username");
  }

  public String getRole() {
    return request.getHeader("X-Auth-Role");
  }
}
