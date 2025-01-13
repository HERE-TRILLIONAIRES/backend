package com.trillionares.tryit.gateway.filter;

import com.trillionares.tryit.gateway.jwt.JwtUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements GatewayFilter {

  private final JwtUtil jwtUtil;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String path = exchange.getRequest().getURI().getPath();

    // 인증 제외 경로를 직접 설정
    List<String> excludedPaths = List.of("/auth/signin", "/users/signup", "/users/internals/**");

    if (excludedPaths.stream().anyMatch(path::startsWith)) {
      log.info("인증 제외 경로 접근: {}", path);
      return chain.filter(exchange);
    }

    // JWT 추출
    String token = jwtUtil.extractToken(exchange.getRequest());
    if (token == null) {
      log.warn("JWT 없음");
      exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST); // 잘못된 요청
      return exchange.getResponse().setComplete();
    }

    try {
      // JWT 검증 및 사용자 정보 추출
      String userName = jwtUtil.extractUsername(token);
      String userRole = jwtUtil.extractRole(token);
      log.info("사용자 이름: {}, 역할: {}", userName, userRole);

      // 사용자 정보를 헤더에 추가하고 필터 체인 계속
      return chain.filter(addHeaders(exchange, userName, userRole));

    } catch (io.jsonwebtoken.ExpiredJwtException e) {
      log.error("JWT 만료: {}", e.getMessage());
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED); // 인증 실패
      return exchange.getResponse().setComplete();
    } catch (io.jsonwebtoken.SignatureException e) {
      log.error("JWT 서명 불일치: {}", e.getMessage());
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    } catch (Exception e) {
      log.error("JWT 검증 실패: {}", e.getMessage());
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }
  }

  // 사용자 정보 헤더에 추가
  private ServerWebExchange addHeaders(ServerWebExchange exchange, String userName, String userRole) {
    return exchange.mutate()
        .request(exchange.getRequest().mutate()
            .header("X-Auth-Username", userName)
            .header("X-Auth-Role", userRole)
            .build())
        .build();
  }

}
