package org.ohorodnik.videostreaming.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class VideoDto {

    private Integer id;
    private String originalFileName;
    private String uuid;
    private MetadataDto metadataDto;
}
