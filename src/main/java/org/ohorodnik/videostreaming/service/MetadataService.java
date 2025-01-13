package org.ohorodnik.videostreaming.service;

import org.ohorodnik.videostreaming.dto.AddUpdateMetadataDto;
import org.ohorodnik.videostreaming.dto.MetadataGeneralDto;
import org.ohorodnik.videostreaming.dto.MetadataDto;

import java.util.List;
import java.util.UUID;

public interface MetadataService {

    MetadataDto addMetadata(AddUpdateMetadataDto addUpdateMetadataDto, UUID videoUuid);

    MetadataDto updateMetadata(AddUpdateMetadataDto addUpdateMetadataDto, Integer id);

    List<MetadataGeneralDto> findAll();

    List<MetadataGeneralDto> findByDirector(String director);
}
