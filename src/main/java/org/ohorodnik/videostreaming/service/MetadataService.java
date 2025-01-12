package org.ohorodnik.videostreaming.service;

import org.ohorodnik.videostreaming.dto.AddMetadataDto;
import org.ohorodnik.videostreaming.dto.MetadataGeneralDto;
import org.ohorodnik.videostreaming.dto.UpdateMetadataDto;
import org.ohorodnik.videostreaming.dto.MetadataDto;

import java.util.List;
import java.util.UUID;

public interface MetadataService {

    MetadataDto addMetadata(AddMetadataDto addMetadataDto, UUID videoUuid);

    MetadataDto updateMetadata(UpdateMetadataDto editMetadataDto, Integer id);

    List<MetadataGeneralDto> findAll();

    List<MetadataGeneralDto> findByDirector(String director);
}
