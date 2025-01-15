package org.ohorodnik.videostreaming.web.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ohorodnik.videostreaming.BaseContainerImpl;
import org.ohorodnik.videostreaming.dto.AddUpdateMetadataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class MetadataControllerITest extends BaseContainerImpl {

    private static final String ADDED_METADATA_DATASET = "datasets/metadata/added-metadata-dataset.json";
    private static final String METADATA_FOR_UPDATE_DATASET = "datasets/metadata/metadata-for-update-dataset.json";
    private static final String UPDATED_METADATA_DATASET = "datasets/metadata/updated-metadata-dataset.json";
    private static final String FINDALL_METADATA_DATASET = "datasets/metadata/findall-metadata-dataset.json";
    private static final String VIDEO_FOR_ADDED_METADATA_DATASET = "datasets/video/video-for-added-metadata-dataset.json";
    private static final String VIDEO_FOR_UPDATE_METADATA_DATASET = "datasets/video/video-for-update-metadata-dataset.json";
    private static final String FINDALL_VIDEO_DATASET = "datasets/video/findall-video-dataset.json";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = VIDEO_FOR_ADDED_METADATA_DATASET, cleanAfter = true, skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = {ADDED_METADATA_DATASET, VIDEO_FOR_ADDED_METADATA_DATASET})
    @DisplayName("Test while adding metadata, it's added and OK status is received")
    public void whenAddMetadata_thenAddedMetadataReturnedAndOkStatusReceived() throws Exception {
        mockMvc.perform(post("/api/v1/metadata/add/065b1c00-22ca-43e6-bd1a-3a571b394041")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAddDtoWithAllData())))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(getResponseAsString("responses/metadata/added-metadata.json")));
    }

    @Test
    @DisplayName("When video is not available, then throw exception when adding metadata for this video")
    public void whenAddingMetadataForNonExistingVideo_thenThrowVideoNotFoundException() throws Exception {
        mockMvc.perform(post("/api/v1/metadata/add/065b1c00-22ca-43e6-bd1a-3a571b394041")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAddDtoWithAllData())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No such video found"));
    }

    @Test
    @DataSet(value = {METADATA_FOR_UPDATE_DATASET, VIDEO_FOR_UPDATE_METADATA_DATASET}, cleanAfter = true, skipCleaningFor = "flyway_schema_history")
    @ExpectedDataSet(value = {UPDATED_METADATA_DATASET, VIDEO_FOR_UPDATE_METADATA_DATASET})
    @DisplayName("Test while updating metadata, it's updated and OK status is received")
    public void whenUpdateMetadata_thenOkStatusAndUpdatedMetadataAreReceived() throws Exception {
        mockMvc.perform(post("/api/v1/metadata/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUpdateDtoWithAllData())))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(getResponseAsString("responses/metadata/updated-metadata.json")));
    }

    @Test
    @DisplayName("Throw exception, when updating metadata, that does not exist")
    public void whenRequestedMetadataIsNotAvailable_thenThrowExceptionWhenUpdateMetadata() throws Exception {
        mockMvc.perform(post("/api/v1/metadata/update/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUpdateDtoWithAllData())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Metadata not found"));
    }

    @Test
    @DataSet(value = {FINDALL_METADATA_DATASET, FINDALL_VIDEO_DATASET},
            cleanAfter = true,
            skipCleaningFor = "flyway_schema_history")
    public void whenFindAll_thenTwoItemsAreReceived() throws Exception {
        mockMvc.perform(get("/api/v1/metadata")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(content()
                        .json(getResponseAsString("responses/metadata/find-all-metadata.json")));
    }

    @Test
    @DataSet(value = {FINDALL_METADATA_DATASET, FINDALL_VIDEO_DATASET},
            cleanAfter = true,
            skipCleaningFor = "flyway_schema_history")
    public void whenFindByDirector_thenOneItemIsReturned() throws Exception {
        mockMvc.perform(get("/api/v1/metadata/second")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(content()
                        .json(getResponseAsString("responses/metadata/find-all-by-director-metadata.json")));
    }

    private AddUpdateMetadataDto createAddDtoWithAllData() {
        return AddUpdateMetadataDto.builder()
                .title("testTitle")
                .synopsis("test synopsis")
                .director("testDirector")
                .actors("actorFirst, actorSecond")
                .yearOfRelease("2019-01-01")
                .genre("testGenre")
                .runningTime("PT5M")
                .build();
    }

    private AddUpdateMetadataDto createUpdateDtoWithAllData() {
        return AddUpdateMetadataDto.builder()
                .title("testTitleSecond")
                .synopsis("test synopsis second")
                .director("testDirectorSecond")
                .actors("actorThird, actorFourth")
                .yearOfRelease("2020-01-01")
                .genre("testGenreSecond")
                .runningTime("PT6M")
                .build();
    }
}
