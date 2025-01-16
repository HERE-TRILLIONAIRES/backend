package com.trillionares.tryit.notification.libs.client.auth;

import com.trillionares.tryit.notification.presentation.dto.BaseResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "${openapi.service.url}")
public interface AuthServiceClient {

  @GetMapping("users/internals/{userId}")
  BaseResponse<UserResponseDto> getUserInfo(@PathVariable UUID userId);
}
