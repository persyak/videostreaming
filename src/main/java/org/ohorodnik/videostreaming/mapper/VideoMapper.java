package org.ohorodnik.videostreaming.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.ohorodnik.videostreaming.dto.PlayingVideoDto;
import org.ohorodnik.videostreaming.dto.VideoDto;
import org.ohorodnik.videostreaming.entity.Video;

@Mapper(componentModel = "spring", uses = MetadataMapper.class)
public interface VideoMapper {

    PlayingVideoDto toPlayingVideoDto(Video video);

    @Mapping(target = "metadataDto", source = "metadata")
    VideoDto toVideoDto(Video video);
}
