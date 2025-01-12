package org.ohorodnik.videostreaming.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDate;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class MetadataDto {

    private Integer id;
    private String title;
    private String synopsis;
    private String director;
    private String actors;
    private LocalDate yearOfRelease;
    private String genre;
    private Duration runningTime;
}
