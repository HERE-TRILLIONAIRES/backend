package com.trillionares.tryit.product.application.service;

import com.trillionares.tryit.product.domain.model.recruitment.Recruitment;
import com.trillionares.tryit.product.domain.model.recruitment.type.RecruitmentStatus;
import com.trillionares.tryit.product.domain.repository.RecruitmentRepository;
import com.trillionares.tryit.product.presentation.dto.request.CreateRecruitmentRequest;
import com.trillionares.tryit.product.presentation.dto.request.UpdateRecruitmentRequest;
import com.trillionares.tryit.product.presentation.dto.request.UpdateRecruitmentStatusRequest;
import com.trillionares.tryit.product.presentation.dto.response.GetRecruitmentResponse;
import com.trillionares.tryit.product.presentation.dto.response.RecruitmentIdResponse;
import com.trillionares.tryit.product.presentation.dto.response.UpdateRecruitmentStatusResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;


    @Transactional
    public RecruitmentIdResponse createRecruitment(CreateRecruitmentRequest request) {
        Recruitment recruitment = Recruitment.builder()
                .productId(request.productId())
                .recruitmentTitle(request.title())
                .recruitmentDescription(request.description())
                .recruitmentStartDate(request.startTime())
                .recruitmentDuration(request.during())
                .recruitmentEndDate(request.endTime())
                .maxParticipants(request.maxParticipants())
                .recruitmentStatus(RecruitmentStatus.WAITING)
                .build();

        recruitmentRepository.save(recruitment);

        return new RecruitmentIdResponse(recruitment.getRecruitmentId());
    }

    @Transactional
    public RecruitmentIdResponse updateRecruitment(UUID recruitmentId,
                                                   UpdateRecruitmentRequest request) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new RuntimeException(""));

        recruitment.updateRecruitment(request.title(), request.description(), request.startTime(),
                request.during(), request.endTime(), request.maxParticipants());

        recruitmentRepository.save(recruitment);

        return new RecruitmentIdResponse(recruitment.getRecruitmentId());
    }

    @Transactional
    public RecruitmentIdResponse deleteRecruitment(UUID recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new RuntimeException(""));

        // BaseEntity 구현 후 soft delete 로 변경
        recruitmentRepository.delete(recruitment);

        return new RecruitmentIdResponse(recruitment.getRecruitmentId());
    }

    public GetRecruitmentResponse getRecruitment(UUID recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new RuntimeException(""));

        return GetRecruitmentResponse.fromEntity(recruitment);
    }

    public Slice<GetRecruitmentResponse> getListRecruitment(Pageable pageable) {
        return recruitmentRepository.getRecruitmentList(pageable);
    }

    public UpdateRecruitmentStatusResponse updateRecruitmentStatus(UUID recruitmentId,
                                                                   UpdateRecruitmentStatusRequest request) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new RuntimeException(""));

        recruitment.updateStatus(request.status());

        recruitmentRepository.save(recruitment);

        return new UpdateRecruitmentStatusResponse(recruitment.getRecruitmentId(),
                recruitment.getRecruitmentStatus());
    }


}
