package org.ohorodnik.videostreaming.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.ohorodnik.videostreaming.dto.AddMetadataDto;
import org.ohorodnik.videostreaming.dto.MetadataDto;
import org.ohorodnik.videostreaming.dto.MetadataGeneralDto;
import org.ohorodnik.videostreaming.dto.UpdateMetadataDto;
import org.ohorodnik.videostreaming.entity.Metadata;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MetadataMapper {

    @Mapping(target = "yearOfRelease", source = "yearOfRelease", qualifiedByName = "mapYearOfRelease")
    @Mapping(target = "runningTime", source = "runningTime", qualifiedByName = "mapRunningTime")
    Metadata toMetadata(AddMetadataDto addMetadataDto);

    MetadataDto toMetadataDto(Metadata metadata);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "yearOfRelease", source = "yearOfRelease", qualifiedByName = "mapYearOfRelease")
    @Mapping(target = "runningTime", source = "runningTime", qualifiedByName = "mapRunningTime")
    Metadata update(@MappingTarget Metadata metadata, UpdateMetadataDto updateMetadataDto);

    List<MetadataGeneralDto> toMetadataGeneralDtoList(List<Metadata> list);

    @Named("mapYearOfRelease")
    default LocalDate mapYearOfRelease(String yearOfRelease) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(yearOfRelease, format);
    }

    @Named("mapRunningTime")
    default Duration mapRunningTime(String runningTime) {
        return Duration.parse(runningTime);
    }
}
