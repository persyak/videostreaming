package org.ohorodnik.videostreaming.service.impl;

import org.junit.jupiter.api.Test;
import org.ohorodnik.videostreaming.dto.StatisticsDto;
import org.ohorodnik.videostreaming.entity.Statistics;
import org.ohorodnik.videostreaming.mapper.StatisticsMapper;
import org.ohorodnik.videostreaming.repository.StatisticsRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DefaultStatisticsServiceTest {

    @MockitoBean
    private StatisticsRepositoryCustom statisticsRepositoryCustom;
    @MockitoBean
    private StatisticsMapper statisticsMapper;
    @Autowired
    private DefaultStatisticsService defaultStatisticsService;

    @Test
    public void testRetrieveStatistics() {
        Statistics testStatistics = Statistics.builder()
                .id(1)
                .view(2)
                .impression(3)
                .build();

        UUID uuid = UUID.randomUUID();

        StatisticsDto testStatisticsDto = StatisticsDto.builder()
                .view(2)
                .impression(3)
                .build();

        when(statisticsRepositoryCustom.retrieveStatistics(uuid)).thenReturn(testStatistics);
        when(statisticsMapper.toStatisticsDto(testStatistics)).thenReturn(testStatisticsDto);

        StatisticsDto actual = defaultStatisticsService.retrieveStatistics(uuid);

        assertEquals(2, actual.getView());
        assertEquals(3, actual.getImpression());

        verify(statisticsRepositoryCustom).retrieveStatistics(uuid);
        verify(statisticsMapper).toStatisticsDto(testStatistics);
    }
}
