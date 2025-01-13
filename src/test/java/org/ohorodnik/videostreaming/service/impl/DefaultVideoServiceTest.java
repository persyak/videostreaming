package org.ohorodnik.videostreaming.service.impl;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ohorodnik.videostreaming.dto.MetadataDto;
import org.ohorodnik.videostreaming.dto.PlayingVideoDto;
import org.ohorodnik.videostreaming.dto.VideoDto;
import org.ohorodnik.videostreaming.entity.Metadata;
import org.ohorodnik.videostreaming.entity.Statistics;
import org.ohorodnik.videostreaming.entity.Video;
import org.ohorodnik.videostreaming.mapper.VideoMapper;
import org.ohorodnik.videostreaming.repository.VideoRepository;
import org.ohorodnik.videostreaming.service.StorageService;
import org.ohorodnik.videostreaming.utils.range.Range;
import org.ohorodnik.videostreaming.utils.records.ChunkWithMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.ohorodnik.videostreaming.utils.enums.Status.A;

@SpringBootTest
public class DefaultVideoServiceTest {

    @MockitoBean
    private StorageService storageService;
    @MockitoBean
    private VideoRepository videoRepository;
    @MockitoBean
    private VideoMapper videoMapper;
    @Autowired
    private DefaultVideoService defaultVideoService;

    private Video testVideo;
    private UUID uuid;
    private Metadata metadata;

    @BeforeEach
    public void beforeEach() {
        uuid = UUID.randomUUID();

        testVideo = Video.builder()
                .id(1)
                .size(108504721L)
                .httpContentType("video/quicktime")
                .originalFileName("testFileName.mov")
                .uuid(uuid.toString())
                .status(A)
                .build();

        metadata = Metadata.builder()
                .id(1)
                .title("testTitle")
                .synopsis("test synopsis")
                .director("testDirector")
                .actors("actorFirst, actorSecond")
                .yearOfRelease(LocalDate.of(2019, 1, 1))
                .genre("testGenre")
                .runningTime(Duration.parse("PT5M"))
                .status(A)
                .video(testVideo)
                .build();

        when(videoRepository.findByUuidAndStatus(uuid.toString(), A)).thenReturn(Optional.ofNullable(testVideo));
    }

    @Test
    @SneakyThrows
    public void testPublish() {
        MultipartFile multipartFile;

        new File("inputs").mkdir();
        new File("inputs/test.txt").createNewFile();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Inputs/test.txt"))) {
            bufferedWriter.write("This is a test file.");
        }

        File file = new File("inputs/test.txt");
        try (FileInputStream input = new FileInputStream(file)) {
            multipartFile = new MockMultipartFile("test", input);
        }

        when(videoRepository.save(any(Video.class))).thenReturn(testVideo);

        UUID actual = defaultVideoService.publish(multipartFile);

        Assertions.assertNotNull(actual);

        verify(videoRepository, times(2)).save(any(Video.class));
        verify(storageService).save(any(), any());

        new File("Inputs/test.txt").delete();
        new File("Inputs").delete();
    }

    @Test
    @SneakyThrows
    public void testPlay() {
        Range range = Range.builder()
                .start(0L)
                .end(10L)
                .isNewView(false)
                .build();

        PlayingVideoDto testPlayingVideoDto = PlayingVideoDto.builder()
                .id(1)
                .size(108504721L)
                .httpContentType("video/quicktime")
                .build();

        InputStream inputStream = IOUtils.toInputStream("some test data", "UTF-8");

        when(videoMapper.toPlayingVideoDto(testVideo)).thenReturn(testPlayingVideoDto);
        when(storageService.getInputStream(uuid, 0, 11)).thenReturn(inputStream);

        ChunkWithMetadata actual = defaultVideoService.play(uuid, range);
        assertEquals(testPlayingVideoDto, actual.playingVideoDto());
        assertEquals("11", actual.contentLength());
        assertEquals("bytes 0-10/108504721", actual.contentRange());

        verify(videoRepository).findByUuidAndStatus(uuid.toString(), A);
        verify(videoMapper).toPlayingVideoDto(testVideo);
        verify(storageService).getInputStream(uuid, 0, 11);
    }

    @Test
    public void testDelist() {
        testVideo.setMetadata(metadata);

        when(videoRepository.findByUuid(uuid.toString())).thenReturn(Optional.ofNullable(testVideo));
        when(videoRepository.save(any(Video.class))).thenReturn(testVideo);

        assertEquals(uuid, defaultVideoService.delist(uuid));

        verify(videoRepository).findByUuid(uuid.toString());
        verify(videoRepository).save(any(Video.class));
    }

    @Test
    @SneakyThrows
    public void testLoad() {
        Statistics testStatistics = Statistics.builder()
                .id(1)
                .view(2)
                .impression(3)
                .build();

        testVideo.setMetadata(metadata);
        testVideo.setStatistics(testStatistics);

        MetadataDto testMetadataDto = MetadataDto.builder()
                .id(1)
                .title("testTitle")
                .synopsis("test synopsis")
                .director("testDirector")
                .actors("actorFirst, actorSecond")
                .yearOfRelease(LocalDate.of(2019, 1, 1))
                .genre("testGenre")
                .runningTime(Duration.parse("PT5M"))
                .build();


        VideoDto testVideoDto = VideoDto.builder()
                .id(1)
                .originalFileName("testFileName.mov")
                .uuid(uuid.toString())
                .metadataDto(testMetadataDto)
                .build();

        when(videoMapper.toVideoDto(testVideo)).thenReturn(testVideoDto);

        VideoDto actual = defaultVideoService.load(uuid, "/testPath");

        assertEquals(1, actual.getId());
        assertEquals("testFileName.mov", actual.getOriginalFileName());
        assertEquals(uuid.toString(), actual.getUuid());
        assertEquals(testMetadataDto, actual.getMetadataDto());

        verify(videoRepository, times(2)).findByUuidAndStatus(uuid.toString(), A);
        verify(videoMapper).toVideoDto(testVideo);

        verify(videoRepository).save(testVideo);
    }
}
