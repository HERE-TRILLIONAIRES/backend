package com.trillionares.tryit.product.presentation.controller;

import com.trillionares.tryit.product.application.service.RecruitmentService;
import com.trillionares.tryit.product.presentation.dto.RecruitmentExistAndStatusDto;
import com.trillionares.tryit.product.presentation.dto.common.base.BaseResponseDto;
import com.trillionares.tryit.product.presentation.dto.request.CreateRecruitmentRequest;
import com.trillionares.tryit.product.presentation.dto.request.UpdateRecruitmentRequest;
import com.trillionares.tryit.product.presentation.dto.request.UpdateRecruitmentStatusRequest;
import com.trillionares.tryit.product.presentation.dto.response.GetRecruitmentResponse;
import com.trillionares.tryit.product.presentation.dto.response.RecruitmentIdResponse;
import com.trillionares.tryit.product.presentation.dto.response.UpdateRecruitmentStatusResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recruitments")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @PostMapping
    public ResponseEntity<RecruitmentIdResponse> createRecruitment(
            @Valid @RequestBody CreateRecruitmentRequest request,
            @RequestHeader("X-Auth-Username") String username,
            @RequestHeader("X-Auth-Role") String role
    ) {
        return ResponseEntity.status(201).body(recruitmentService.createRecruitment(request, username, role));
    }

    @PatchMapping("{recruitmentId}")
    public ResponseEntity<RecruitmentIdResponse> updateRecruitment(
            @PathVariable UUID recruitmentId,
            @Valid @RequestBody UpdateRecruitmentRequest request,
            @RequestHeader("X-Auth-Username") String username,
            @RequestHeader("X-Auth-Role") String role
    ) {
        return ResponseEntity.ok(recruitmentService.updateRecruitment(recruitmentId, request, username, role));
    }


    @DeleteMapping("{recruitmentId}")
    public ResponseEntity<RecruitmentIdResponse> deleteRecruitment(
            @PathVariable UUID recruitmentId,
            @RequestHeader("X-Auth-Username") String username,
            @RequestHeader("X-Auth-Role") String role
    ) {
        return ResponseEntity.ok(recruitmentService.deleteRecruitment(recruitmentId, username, role));
    }


    @GetMapping("{recruitmentId}")
    public ResponseEntity<GetRecruitmentResponse> getRecruitment(
            @PathVariable UUID recruitmentId
    ) {
        return ResponseEntity.ok(recruitmentService.getRecruitment(recruitmentId));
    }

    @GetMapping
    public ResponseEntity<Slice<GetRecruitmentResponse>> getListRecruitment(
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(recruitmentService.getListRecruitment(pageable));
    }

    @PatchMapping("{recruitmentId}/status")
    public ResponseEntity<UpdateRecruitmentStatusResponse> updateRecruitmentStatus(
            @PathVariable UUID recruitmentId,
            @RequestBody UpdateRecruitmentStatusRequest request,
            @RequestHeader("X-Auth-Username") String username,
            @RequestHeader("X-Auth-Role") String role
    ) {
        return ResponseEntity.ok(
                recruitmentService.updateRecruitmentStatus(recruitmentId, request, username, role));
    }

    @GetMapping("/isExist/{recruitmentId}")
    public BaseResponseDto<RecruitmentExistAndStatusDto> isExistRecruitmentById(
            @PathVariable("recruitmentId") UUID recruitmentId) {
        RecruitmentExistAndStatusDto responseDto = recruitmentService.isExistRecruitmentById(recruitmentId);

        return BaseResponseDto.from(HttpStatus.OK.value(), HttpStatus.OK, "OK", responseDto);
    }
}
