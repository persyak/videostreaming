package org.ohorodnik.videostreaming.service;

import org.ohorodnik.videostreaming.dto.UploadVideoDto;
import org.ohorodnik.videostreaming.dto.VideoDto;
import org.ohorodnik.videostreaming.utils.range.Range;
import org.ohorodnik.videostreaming.utils.records.ChunkWithMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface VideoService {

    UploadVideoDto publish(MultipartFile video);

    ChunkWithMetadata play(UUID uuid, Range range);

    UUID delist(UUID uuid);

    VideoDto load(UUID uuid, String path) throws Exception;
}
