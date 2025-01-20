package org.ohorodnik.videostreaming.web.controller;

import lombok.RequiredArgsConstructor;
import org.ohorodnik.videostreaming.dto.UploadVideoDto;
import org.ohorodnik.videostreaming.dto.VideoDto;
import org.ohorodnik.videostreaming.service.VideoService;
import org.ohorodnik.videostreaming.utils.range.Range;
import org.ohorodnik.videostreaming.utils.records.ChunkWithMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.springframework.http.HttpHeaders.ACCEPT_RANGES;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.CONTENT_RANGE;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/video")
public class VideoController {

    public static final String ACCEPTS_RANGES_VALUE = "bytes";

    private final VideoService videoService;

    @Value("${app.streaming.default-chunk-size}")
    private Integer defaultChunkSize;

    @PostMapping(value = "/publish",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    protected UploadVideoDto publish(@RequestPart("file") MultipartFile file) {
        return videoService.publish(file);
    }

    @GetMapping("/{uuid}")
    protected ResponseEntity<byte[]> readChunk(
            @RequestHeader(value = HttpHeaders.RANGE, required = false) String range,
            @PathVariable UUID uuid) {
        Range parsedRange = Range.parseHttpRangeString(range, defaultChunkSize);
        ChunkWithMetadata chunkWithMetadata = videoService.play(uuid, parsedRange);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(CONTENT_TYPE, chunkWithMetadata.playingVideoDto().getHttpContentType())
                .header(ACCEPT_RANGES, ACCEPTS_RANGES_VALUE)
                .header(CONTENT_LENGTH, chunkWithMetadata.contentLength())
                .header(CONTENT_RANGE, chunkWithMetadata.contentRange())
                .body(chunkWithMetadata.chunk());
    }

    @DeleteMapping("/{uuid}")
    protected UUID delist(@PathVariable UUID uuid) {
        return videoService.delist(uuid);
    }

    @GetMapping("/load/{uuid}")
    protected VideoDto load(@PathVariable UUID uuid,
                            @RequestParam("path") String path) throws Exception {
        return videoService.load(uuid, path);
    }
}
