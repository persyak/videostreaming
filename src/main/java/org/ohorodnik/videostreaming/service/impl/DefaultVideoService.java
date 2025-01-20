package org.ohorodnik.videostreaming.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ohorodnik.videostreaming.dto.PlayingVideoDto;
import org.ohorodnik.videostreaming.dto.UploadVideoDto;
import org.ohorodnik.videostreaming.dto.VideoDto;
import org.ohorodnik.videostreaming.entity.Statistics;
import org.ohorodnik.videostreaming.entity.Video;
import org.ohorodnik.videostreaming.exception.StorageException;
import org.ohorodnik.videostreaming.exception.VideoNotFoundException;
import org.ohorodnik.videostreaming.mapper.VideoMapper;
import org.ohorodnik.videostreaming.repository.VideoRepository;
import org.ohorodnik.videostreaming.service.StorageService;
import org.ohorodnik.videostreaming.service.VideoService;
import org.ohorodnik.videostreaming.utils.range.Range;
import org.ohorodnik.videostreaming.utils.records.ChunkWithMetadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.ohorodnik.videostreaming.utils.enums.Status.A;
import static org.ohorodnik.videostreaming.utils.enums.Status.D;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultVideoService implements VideoService {

    private static final String STORAGE_EXCEPTION_MESSAGE = "Exception occurred when trying to read file with ID %s";

    private final StorageService storageService;
    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;

    private final Cache<UUID, Video> cache = Caffeine.newBuilder()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .initialCapacity(100)
            .maximumSize(10000)
            .build();

    @Override
    @Transactional
    public UploadVideoDto publish(MultipartFile file) {
        try {
            UUID fileUuid = UUID.randomUUID();
            Video video = Video.builder()
                    .size(file.getSize())
                    .httpContentType(file.getContentType())
                    .originalFileName(file.getOriginalFilename())
                    .uuid(fileUuid.toString())
                    .status(A)
                    .build();
            Video savedVideo = videoRepository.save(video);
            storageService.save(file, fileUuid);

            Statistics statistics = Statistics.builder()
                    .impression(0)
                    .view(0)
                    .video(savedVideo)
                    .build();
            savedVideo.setStatistics(statistics);
            videoRepository.save(savedVideo);

            log.info("Video has been saved to storage under uuid {}", fileUuid);
            return videoMapper.toUploadVideoDto(savedVideo);
        } catch (Exception ex) {
            log.error("Exception has occurred when trying to save the file", ex);
            throw new StorageException("Exception has occurred when trying to save the file");
        }
    }

    @Override
    @Transactional
    public ChunkWithMetadata play(UUID uuid, Range range) {
        updateStatisticsView(uuid, range);
        PlayingVideoDto playingVideoDto = videoMapper.toPlayingVideoDto(findByUuidAndStatus(uuid));
        String contentRange = constructContentRangeHeader(range, playingVideoDto.getSize());
        log.debug("playing range {}", contentRange);
        return new ChunkWithMetadata(playingVideoDto,
                calculateContentLengthHeader(range, playingVideoDto.getSize()),
                contentRange,
                readChunk(uuid, range, playingVideoDto.getSize()));
    }

    @Override
    @Transactional
    public UUID delist(UUID uuid) {
        Video video = videoRepository.findByUuid(uuid.toString())
                .orElseThrow(() -> new VideoNotFoundException("No such video found"));
        video.setStatus(D);
        video.getMetadata().setStatus(D);
        videoRepository.save(video);
        cache.invalidate(uuid);
        log.info("video {} delisted", uuid);
        return uuid;
    }

    //TODO: to check how does it work in Windows with different \
    @Override
    @Transactional
    public VideoDto load(UUID uuid, String path) throws Exception {
        Video video = findByUuidAndStatus(uuid);
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        String fileFullPath = path.concat(video.getOriginalFileName());
        storageService.download(uuid, fileFullPath);
        log.debug("video saved under {}", fileFullPath);

        updateStatisticsImpression(uuid);
        return videoMapper.toVideoDto(video);
    }

    private Video findByUuidAndStatus(UUID uuid) {
        return cache.get(uuid, v -> videoRepository.findByUuidAndStatus(uuid.toString(), A)
                .orElseThrow(() -> new VideoNotFoundException("No such video found")));
    }

    private byte[] readChunk(UUID uuid, Range range, long fileSize) {
        long startPosition = range.getRangeStart();
        long endPosition = range.getRangeEnd(fileSize);
        int chunkSize = (int) (endPosition - startPosition + 1);
        try (InputStream inputStream = storageService.getInputStream(uuid, startPosition, chunkSize)) {
            return inputStream.readAllBytes();
        } catch (Exception exception) {
            log.error("Exception occurred when trying to read file with ID = {}", uuid);
            throw new StorageException(String.format(STORAGE_EXCEPTION_MESSAGE, uuid));
        }
    }

    private String calculateContentLengthHeader(Range range, long fileSize) {
        return String.valueOf(range.getRangeEnd(fileSize) - range.getRangeStart() + 1);
    }

    private String constructContentRangeHeader(Range range, long fileSize) {
        return "bytes " + range.getRangeStart() + "-" + range.getRangeEnd(fileSize) + "/" + fileSize;
    }

    private void updateStatisticsView(UUID uuid, Range range) {
        if (range.getIsNewView()) {
            Video video = videoRepository.findByUuidAndStatus(uuid.toString(), A)
                    .orElseThrow(() -> new VideoNotFoundException("No such video found"));
            Integer view = video.getStatistics().getView();
            video.getStatistics().setView(++view);
            videoRepository.save(video);
        }
    }

    private void updateStatisticsImpression(UUID uuid) {
        Video videoStatistics = videoRepository.findByUuidAndStatus(uuid.toString(), A)
                .orElseThrow(() -> new VideoNotFoundException("No such video found"));
        Integer impression = videoStatistics.getStatistics().getImpression();
        videoStatistics.getStatistics().setImpression(++impression);
        videoRepository.save(videoStatistics);
    }
}
