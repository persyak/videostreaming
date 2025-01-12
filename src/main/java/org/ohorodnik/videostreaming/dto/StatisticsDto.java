package org.ohorodnik.videostreaming.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class StatisticsDto {

    private Integer impression;
    private Integer view;
}
