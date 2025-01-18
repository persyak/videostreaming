package org.ohorodnik.videostreaming.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ohorodnik.videostreaming.dto.AddUpdateMetadataDto;
import org.ohorodnik.videostreaming.dto.MetadataGeneralDto;
import org.ohorodnik.videostreaming.dto.MetadataDto;
import org.ohorodnik.videostreaming.entity.Metadata;
import org.ohorodnik.videostreaming.entity.Video;
import org.ohorodnik.videostreaming.exception.MetadataNotFoundException;
import org.ohorodnik.videostreaming.exception.VideoNotFoundException;
import org.ohorodnik.videostreaming.mapper.MetadataMapper;
import org.ohorodnik.videostreaming.repository.MetadataRepository;
import org.ohorodnik.videostreaming.repository.VideoRepository;
import org.ohorodnik.videostreaming.service.MetadataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.ohorodnik.videostreaming.utils.enums.Status.A;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMetadataService implements MetadataService {

    private final VideoRepository videoRepository;
    private final MetadataRepository metadataRepository;
    private final MetadataMapper metadataMapper;

    @Override
    @Transactional
    public MetadataDto addMetadata(AddUpdateMetadataDto addUpdateMetadataDto, UUID videoUuid) {
        Video video = videoRepository.findByUuid(videoUuid.toString())
                .orElseThrow(() -> new VideoNotFoundException("No such video found"));

        Metadata metadataToSave = metadataMapper.toMetadata(addUpdateMetadataDto);
        metadataToSave.setStatus(A);
        metadataToSave.setVideo(video);

        Metadata saved = metadataRepository.save(metadataToSave);
        log.info("metadata saved under id {} for video {}", saved.getId(), videoUuid);

        return metadataMapper.toMetadataDto(saved);
    }

    @Override
    @Transactional
    public MetadataDto updateMetadata(AddUpdateMetadataDto addUpdateMetadataDto, Integer id) {
        Metadata originalMetadata = metadataRepository.findById(id).orElseThrow(
                () -> new MetadataNotFoundException("Metadata not found"));
        Metadata updatedMetadata = metadataMapper.update(originalMetadata, addUpdateMetadataDto);
        log.info("metadata {} updated", id) ;
        return metadataMapper.toMetadataDto(metadataRepository.save(updatedMetadata));
    }

    @Override
    @Transactional
    public List<MetadataGeneralDto> findAll() {
        log.debug("findAll queried");
        return metadataMapper.toMetadataGeneralDtoList(metadataRepository.findAll());
    }

    @Override
    @Transactional
    public List<MetadataGeneralDto> findByDirector(String director) {
        log.debug("search by {}", director);
        return metadataMapper.toMetadataGeneralDtoList(metadataRepository.findByDirectorContainingIgnoreCase(director));
    }
}
