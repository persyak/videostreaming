package org.ohorodnik.videostreaming.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ohorodnik.videostreaming.dto.AddUpdateMetadataDto;
import org.ohorodnik.videostreaming.dto.MetadataDto;
import org.ohorodnik.videostreaming.dto.MetadataGeneralDto;
import org.ohorodnik.videostreaming.entity.Metadata;
import org.ohorodnik.videostreaming.entity.Video;
import org.ohorodnik.videostreaming.exception.MetadataNotFoundException;
import org.ohorodnik.videostreaming.exception.VideoNotFoundException;
import org.ohorodnik.videostreaming.mapper.MetadataMapper;
import org.ohorodnik.videostreaming.repository.MetadataRepository;
import org.ohorodnik.videostreaming.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.ohorodnik.videostreaming.utils.enums.Status.A;

@SpringBootTest
public class DefaultMetadataServiceTest {

    private static final UUID uuid = UUID.randomUUID();

    @MockitoBean
    private VideoRepository videoRepository;
    @MockitoBean
    private MetadataRepository metadataRepository;
    @MockitoBean
    private MetadataMapper metadataMapper;
    @Autowired
    private DefaultMetadataService defaultMetadataService;

    private Video testVideo;
    private Metadata testMetadataAdded;
    private AddUpdateMetadataDto testAddMetadataDto;
    private AddUpdateMetadataDto testUpdateMetadataDto;

    @BeforeEach
    public void setup() {
        testVideo = Video.builder()
                .id(1)
                .size(108504721L)
                .httpContentType("video/quicktime")
                .originalFileName("testFileName.mov")
                .uuid(uuid.toString())
                .status(A)
                .build();

        testAddMetadataDto = AddUpdateMetadataDto.builder()
                .title("testTitle")
                .synopsis("test synopsis")
                .director("testDirector")
                .actors("actorFirst, actorSecond")
                .yearOfRelease("2019-01-01")
                .genre("testGenre")
                .runningTime("PT5M")
                .build();

        testMetadataAdded = Metadata.builder()
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

        testUpdateMetadataDto = AddUpdateMetadataDto.builder()
                .title("testTitle updated")
                .synopsis("synopsis updated")
                .director("testDirector updated")
                .actors("actorFirst, actorSecond, updated")
                .yearOfRelease("2020-01-01")
                .genre("testGenre updated")
                .runningTime("PT6M")
                .build();
    }

    @Test
    @DisplayName("Add metadata and return it when metadata is available and video is available")
    public void whenMetadataAndVideoAreAvailable_thenAddMetadata() {
        Metadata testAddMetadata = Metadata.builder()
                .title("testTitle")
                .synopsis("test synopsis")
                .director("testDirector")
                .actors("actorFirst, actorSecond")
                .yearOfRelease(LocalDate.of(2019, 1, 1))
                .genre("testGenre")
                .runningTime(Duration.parse("PT5M"))
                .build();

        MetadataDto metadataDto = MetadataDto.builder()
                .id(1)
                .title("testTitle")
                .synopsis("test synopsis")
                .director("testDirector")
                .actors("actorFirst, actorSecond")
                .yearOfRelease(LocalDate.of(2019, 1, 1))
                .genre("testGenre")
                .runningTime(Duration.parse("PT5M"))
                .build();

        when(videoRepository.findByUuid(uuid.toString()))
                .thenReturn(Optional.ofNullable(testVideo));
        when(metadataMapper.toMetadata(testAddMetadataDto)).thenReturn(testAddMetadata);
        when(metadataMapper.toMetadataDto(testMetadataAdded)).thenReturn(metadataDto);
        when(metadataRepository.save(testAddMetadata)).thenReturn(testMetadataAdded);

        MetadataDto actual = defaultMetadataService.addMetadata(testAddMetadataDto, uuid);

        assertEquals(1, actual.getId());
        assertEquals("testTitle", actual.getTitle());
        assertEquals("test synopsis", actual.getSynopsis());
        assertEquals("testDirector", actual.getDirector());
        assertEquals("actorFirst, actorSecond", actual.getActors());
        assertEquals(LocalDate.of(2019, 1, 1), actual.getYearOfRelease());
        assertEquals("testGenre", actual.getGenre());
        assertEquals(Duration.parse("PT5M"), actual.getRunningTime());

        verify(videoRepository).findByUuid(uuid.toString());
        verify(metadataRepository).save(testAddMetadata);
        verify(metadataMapper).toMetadata(testAddMetadataDto);
        verify(metadataMapper).toMetadataDto(testMetadataAdded);
    }

    @Test
    @DisplayName("Throw VideoNotFoundException when add metadata for not available video")
    public void whenVideoIsNotAvailable_thenThrowVideoNotFoundExceptionWhenAddMetadata() {
        when(videoRepository.findByUuid(uuid.toString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(VideoNotFoundException.class, () -> {
            defaultMetadataService.addMetadata(testAddMetadataDto, uuid);
        });

        assertTrue(exception.getMessage().contains("No such video found"));
    }

    @Test
    @DisplayName("Update metadata test")
    public void whenMetadataIsAvailable_thenUpdateMetadata() {

        Metadata testUpdatedMetadata = Metadata.builder()
                .id(1)
                .title("testTitle updated")
                .synopsis("synopsis updated")
                .director("testDirector updated")
                .actors("actorFirst, actorSecond, updated")
                .yearOfRelease(LocalDate.of(2020, 1, 1))
                .genre("testGenre updated")
                .runningTime(Duration.parse("PT6M"))
                .status(A)
                .video(testVideo)
                .build();

        MetadataDto metadataDto = MetadataDto.builder()
                .id(1)
                .title("testTitle updated")
                .synopsis("synopsis updated")
                .director("testDirector updated")
                .actors("actorFirst, actorSecond, updated")
                .yearOfRelease(LocalDate.of(2020, 1, 1))
                .genre("testGenre updated")
                .runningTime(Duration.parse("PT6M"))
                .build();

        when(metadataRepository.findById(1)).thenReturn(Optional.ofNullable(testMetadataAdded));
        when(metadataMapper.update(testMetadataAdded, testUpdateMetadataDto))
                .thenReturn(testUpdatedMetadata);
        when(metadataRepository.save(testUpdatedMetadata)).thenReturn(testUpdatedMetadata);
        when(metadataMapper.toMetadataDto(testUpdatedMetadata)).thenReturn(metadataDto);

        MetadataDto actual = defaultMetadataService.updateMetadata(testUpdateMetadataDto, 1);

        assertEquals(1, actual.getId());
        assertEquals("testTitle updated", actual.getTitle());
        assertEquals("synopsis updated", actual.getSynopsis());
        assertEquals("testDirector updated", actual.getDirector());
        assertEquals("actorFirst, actorSecond, updated", actual.getActors());
        assertEquals(LocalDate.of(2020, 1, 1), actual.getYearOfRelease());
        assertEquals("testGenre updated", actual.getGenre());
        assertEquals(Duration.parse("PT6M"), actual.getRunningTime());

        verify(metadataRepository).findById(1);
        verify(metadataMapper).update(testMetadataAdded, testUpdateMetadataDto);
        verify(metadataMapper).toMetadataDto(testUpdatedMetadata);
        verify(metadataRepository).save(testUpdatedMetadata);
    }

    @Test
    @DisplayName("Throw MetadataNotFoundException when updating metadata if original metadata is not availabe")
    public void whenMetadataIsNotAvailable_thenThrowMetadataNotFoundExceptionWhenUpdateMetadata() {
        when(metadataRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(MetadataNotFoundException.class, () -> {
            defaultMetadataService.updateMetadata(testUpdateMetadataDto, 1);
        });

        assertTrue(exception.getMessage().contains("Metadata not found"));
    }

    @Test
    public void testFindAll() {
        UUID uuidSecond = UUID.randomUUID();

        Video testVideoSecond = Video.builder()
                .id(2)
                .size(108504800L)
                .httpContentType("video/quicktime")
                .originalFileName("testFileNameSecond.mov")
                .uuid(uuidSecond.toString())
                .status(A)
                .build();

        Metadata metadataFirst = Metadata.builder()
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

        Metadata metadataSecond = Metadata.builder()
                .id(2)
                .title("testTitleSecond")
                .synopsis("test synopsisSecond")
                .director("testDirectorSecond")
                .actors("actorThird, actorFourth")
                .yearOfRelease(LocalDate.of(2020, 1, 1))
                .genre("testGenreSecond")
                .runningTime(Duration.parse("PT6M"))
                .status(A)
                .video(testVideoSecond)
                .build();

        List<Metadata> list = List.of(metadataFirst, metadataSecond);

        MetadataGeneralDto metadataGeneralDtoFirst = MetadataGeneralDto.builder()
                .id(1)
                .title("testTitle")
                .director("testDirector")
                .actors("actorFirst, actorSecond")
                .genre("testGenre")
                .runningTime(Duration.parse("PT5M"))
                .build();

        MetadataGeneralDto metadataGeneralDtoSecond = MetadataGeneralDto.builder()
                .id(2)
                .title("testTitleSecond")
                .director("testDirectorSecond")
                .actors("actorThird, actorFourth")
                .genre("testGenreSecond")
                .runningTime(Duration.parse("PT6M"))
                .build();

        when(metadataRepository.findAll()).thenReturn(list);
        when(metadataMapper.toMetadataGeneralDtoList(list)).thenReturn(
                List.of(metadataGeneralDtoFirst, metadataGeneralDtoSecond));

        List<MetadataGeneralDto> actualList = defaultMetadataService.findAll();

        assertEquals(2, actualList.size());

        MetadataGeneralDto actual = actualList.getFirst();

        assertEquals(1, actual.getId());
        assertEquals("testTitle", actual.getTitle());
        assertEquals("testDirector", actual.getDirector());
        assertEquals("actorFirst, actorSecond", actual.getActors());
        assertEquals("testGenre", actual.getGenre());
        assertEquals(Duration.parse("PT5M"), actual.getRunningTime());

        verify(metadataRepository).findAll();
        verify(metadataMapper).toMetadataGeneralDtoList(list);
    }

    @Test
    public void testFindByDirector() {
        List<Metadata> list = List.of(
                Metadata.builder()
                        .id(2)
                        .title("testTitleSecond")
                        .synopsis("test synopsisSecond")
                        .director("testDirectorSecond")
                        .actors("actorThird, actorFourth")
                        .yearOfRelease(LocalDate.of(2020, 1, 1))
                        .genre("testGenreSecond")
                        .runningTime(Duration.parse("PT6M"))
                        .status(A)
                        .video(
                                Video.builder()
                                        .id(2)
                                        .size(108504800L)
                                        .httpContentType("video/quicktime")
                                        .originalFileName("testFileNameSecond.mov")
                                        .uuid(UUID.randomUUID().toString())
                                        .status(A)
                                        .build()
                        )
                        .build());

        List<MetadataGeneralDto> listDto = List.of(
                MetadataGeneralDto.builder()
                        .id(2)
                        .title("testTitleSecond")
                        .director("testDirectorSecond")
                        .actors("actorThird, actorFourth")
                        .genre("testGenreSecond")
                        .runningTime(Duration.parse("PT6M"))
                        .build());

        when(metadataRepository.findByDirectorContainingIgnoreCase("Second")).thenReturn(list);
        when(metadataMapper.toMetadataGeneralDtoList(list)).thenReturn(listDto);

        List<MetadataGeneralDto> actualList = defaultMetadataService.findByDirector("Second");

        assertEquals(1, actualList.size());

        MetadataGeneralDto actual = actualList.getFirst();

        assertEquals(2, actual.getId());
        assertEquals("testTitleSecond", actual.getTitle());
        assertEquals("testDirectorSecond", actual.getDirector());
        assertEquals("actorThird, actorFourth", actual.getActors());
        assertEquals("testGenreSecond", actual.getGenre());
        assertEquals(Duration.parse("PT6M"), actual.getRunningTime());

        verify(metadataRepository).findByDirectorContainingIgnoreCase("Second");
        verify(metadataMapper).toMetadataGeneralDtoList(list);
    }
}
