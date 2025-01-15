package org.ohorodnik.videostreaming.web.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;
import org.ohorodnik.videostreaming.BaseContainerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class StatisticsControllerITest extends BaseContainerImpl {

    private static final String STATISTICS_DATASET = "datasets/statistics/statistics-dataset.json";
    private static final String STATISTICS_VIDEO_DATASET = "datasets/video/statistics-video-dataset.json";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DataSet(value = {STATISTICS_DATASET, STATISTICS_VIDEO_DATASET}, cleanAfter = true, skipCleaningFor = "flyway_schema_history")
    public void whenStatisticsIsAvailable_thanRetrieve() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/065b1c00-22ca-43e6-bd1a-3a571b394041")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(getResponseAsString("responses/statistics/retrieve-statistics.json")));
    }

    @Test
    @DataSet(value = {STATISTICS_DATASET, STATISTICS_VIDEO_DATASET}, cleanAfter = true, skipCleaningFor = "flyway_schema_history")
    public void whenStatisticsIsNotAvailable_thenThrowError() throws Exception {
        mockMvc.perform(get("/api/v1/statistics/065b1c00-22ca-43e6-bd1a-3a571b394042")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message")
                        .value("No result found for current query"));
    }
}
