package com.trillionares.tryit.auth.presentation.controller;


import com.trillionares.tryit.auth.application.dto.UserAuthorityResponseDto;
import com.trillionares.tryit.auth.application.service.UserService;
import com.trillionares.tryit.auth.infrastructure.config.CustomUserDetails;
import com.trillionares.tryit.auth.presentation.dto.BaseResponse;
import com.trillionares.tryit.auth.presentation.dto.requestDto.PasswordUpdateReqDto;
import com.trillionares.tryit.auth.presentation.dto.requestDto.SignUpRequestDto;
import com.trillionares.tryit.auth.presentation.dto.requestDto.UserInfoUpdateReqDto;
import com.trillionares.tryit.auth.presentation.dto.responseDto.UserResponseDto;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  @PostMapping("/signup")
  public BaseResponse<UserResponseDto> signup(@RequestBody @Valid SignUpRequestDto reqDto) {
    return BaseResponse.of(201, HttpStatus.CREATED, "회원가입에 성공했습니다.",userService.signup(reqDto));
  }

  @PutMapping("/password")
  public BaseResponse updatePassword(@RequestBody @Valid PasswordUpdateReqDto reqDto,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    userService.updatePassword(reqDto, userDetails.getUserId());

    return BaseResponse.of(201, HttpStatus.ACCEPTED, "비밀번호가 수정되었습니다.", null);
  }

  @PutMapping("/{userId}")
  public BaseResponse<UserResponseDto> updateUserInfo(@PathVariable UUID userId,
      @Valid @RequestBody UserInfoUpdateReqDto reqDto,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    userService.updateUserInfo(userDetails.getUserId(), reqDto);

    UserResponseDto updatedUserInfo = userService.updateUserInfo(userDetails.getUserId(), reqDto);

    return BaseResponse.of(201, HttpStatus.ACCEPTED, "회원정보가 수정되었습니다.", updatedUserInfo);
  }

  @DeleteMapping("/{userId}")
  public BaseResponse deleteUser(@PathVariable UUID userId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    userService.deleteUser(userDetails.getUserId());

    return BaseResponse.of(204, HttpStatus.NO_CONTENT, "회원탈퇴 완료", null);

  }

  @GetMapping("/internals/username/{username}")
  public BaseResponse<UserAuthorityResponseDto> getUserByUsername(@PathVariable String username,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    UserAuthorityResponseDto resDto = userService.getUserByUsername(username);
    return BaseResponse.of(200, HttpStatus.OK, "사용자 정보 조회 성공", resDto);
  }

  @GetMapping("/{userId}")
  public BaseResponse<UserResponseDto> getUser(@PathVariable UUID userId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    UserResponseDto resDto = userService.getUser(userDetails.getUserId());
    return BaseResponse.of(200, HttpStatus.OK, "사용자가 조회되었습니다.", resDto);
  }

  @GetMapping("/internals/{userId}")
  public BaseResponse<UserResponseDto> getUserInfo(@PathVariable UUID userId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    UserResponseDto resDto = userService.getUser(userDetails.getUserId());
    return BaseResponse.of(200, HttpStatus.OK, "사용자가 조회되었습니다.", resDto);
  }

}
