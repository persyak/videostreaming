package org.ohorodnik.videostreaming.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class MetadataGeneralDto {

    private Integer id;
    private String title;
    private String director;
    private String actors;
    private String genre;
    private Duration runningTime;
}
