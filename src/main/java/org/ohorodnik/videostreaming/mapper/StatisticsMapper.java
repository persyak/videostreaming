package org.ohorodnik.videostreaming.mapper;

import org.mapstruct.Mapper;
import org.ohorodnik.videostreaming.dto.StatisticsDto;
import org.ohorodnik.videostreaming.entity.Statistics;

@Mapper(componentModel = "spring")
public interface StatisticsMapper {

    StatisticsDto toStatisticsDto(Statistics statistics);
}
