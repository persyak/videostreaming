package org.ohorodnik.videostreaming.web.controller;

import org.ohorodnik.videostreaming.BaseContainerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class MetadataControllerITest extends BaseContainerImpl {

    @Autowired
    private MockMvc mockMvc;
}
