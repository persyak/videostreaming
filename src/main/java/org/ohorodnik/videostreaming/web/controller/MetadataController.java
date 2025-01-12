package org.ohorodnik.videostreaming.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ohorodnik.videostreaming.dto.AddMetadataDto;
import org.ohorodnik.videostreaming.dto.MetadataGeneralDto;
import org.ohorodnik.videostreaming.dto.UpdateMetadataDto;
import org.ohorodnik.videostreaming.dto.MetadataDto;
import org.ohorodnik.videostreaming.service.MetadataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/metadata")
public class MetadataController {

    private final MetadataService metadataService;

    @PostMapping("/add/{uuid}")
    protected MetadataDto addMetadata(@Valid @RequestBody AddMetadataDto addMetadataDto,
                                      @PathVariable UUID uuid) {
        return metadataService.addMetadata(addMetadataDto, uuid);
    }

    @PostMapping("/update/{id}")
    protected MetadataDto updateMetadata(@Valid @RequestBody UpdateMetadataDto updateMetadataDto,
                                       @PathVariable Integer id){
        return metadataService.updateMetadata(updateMetadataDto, id);
    }

    @GetMapping
    protected List<MetadataGeneralDto> findAll(){
        return metadataService.findAll();
    }

    @GetMapping("/{director}")
    protected List<MetadataGeneralDto> findByDirector(@PathVariable String director){
        return metadataService.findByDirector(director);
    }
}
