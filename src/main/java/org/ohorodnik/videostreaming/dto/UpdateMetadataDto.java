package org.ohorodnik.videostreaming.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class UpdateMetadataDto {

    @NotBlank(message = "Please add title")
    @Size(min = 3)
    private String title;
    @NotBlank(message = "Please add synopsis")
    private String synopsis;
    @NotBlank(message = "Please specify director")
    private String director;
    @NotBlank(message = "Please add actors")
    private String actors;
    @NotBlank(message = "Year of release should not be blank")
    private String yearOfRelease;
    @NotBlank(message = "Please add genre")
    private String genre;
    @NotBlank(message = "Year of release should not be blank")
    private String runningTime;
}
