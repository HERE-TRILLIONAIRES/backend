package com.trillionares.tryit.statistics.application.service;

import com.trillionares.tryit.statistics.application.dto.request.StatisticsCreateRequestDto;
import com.trillionares.tryit.statistics.application.dto.response.StatisticsCreateResponseDto;
import com.trillionares.tryit.statistics.application.dto.response.StatisticsGetResponseDto;
import com.trillionares.tryit.statistics.domain.model.Statistics;
import com.trillionares.tryit.statistics.domain.respository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    @Transactional
    public StatisticsCreateResponseDto createStatistics(StatisticsCreateRequestDto statisticsCreateRequestDto) {
        Statistics statistics =  statisticsRepository.save(Statistics.of(statisticsCreateRequestDto.userId(),
                statisticsCreateRequestDto.productId(),statisticsCreateRequestDto.highestScore(),
                statisticsCreateRequestDto.lowestScore(),statisticsCreateRequestDto.reviewCount(),statisticsCreateRequestDto.durationTime()));
        return StatisticsCreateResponseDto.of(statistics.getStatisticsId(),statistics.getCreatedAt());
    }

    public List<StatisticsGetResponseDto> getAllStatistics() {
        return statisticsRepository.findAll().stream().map(statistics -> StatisticsGetResponseDto.of(
                statistics.getStatisticsId(),statistics.getUserId(),statistics.getProductId(),
                statistics.getHighestScore(),statistics.getLowestScore(), statistics.getReviewCount(),
                statistics.getDurationTime(),statistics.getCreatedAt())).collect(Collectors.toList());
    }

    public List<StatisticsGetResponseDto> getStatistics(UUID userId) {
        return statisticsRepository.findAllByUserId(userId).stream().map(statistics -> StatisticsGetResponseDto.of(
                statistics.getStatisticsId(),statistics.getUserId(),statistics.getProductId(),
                statistics.getHighestScore(),statistics.getLowestScore(), statistics.getReviewCount(),
                statistics.getDurationTime(),statistics.getCreatedAt())).collect(Collectors.toList());
    }
}
