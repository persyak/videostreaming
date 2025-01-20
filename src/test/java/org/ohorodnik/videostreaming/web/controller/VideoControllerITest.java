package org.ohorodnik.videostreaming.web.controller;

import org.junit.jupiter.api.Test;
import org.ohorodnik.videostreaming.MinioBaseContainerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class VideoControllerITest extends MinioBaseContainerImpl {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPublish() throws Exception {
        MockMultipartFile multipartFile;
        File file = new File("src/test/resources/multipart/test.txt");
        try (FileInputStream input = new FileInputStream(file)) {
            multipartFile = new MockMultipartFile("file",
                    "test.txt", "video/quicktime", input);
        }

        mockMvc.perform(multipart("/api/v1/video/publish").file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.originalFileName").value("test.txt"));

    }
}
