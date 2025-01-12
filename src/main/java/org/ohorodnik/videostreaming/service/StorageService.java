package org.ohorodnik.videostreaming.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

public interface StorageService {

    void save(MultipartFile file, UUID uuid) throws Exception;

    InputStream getInputStream(UUID uuid, long offset, long length) throws Exception;

    void download(UUID uuid, String fileName) throws Exception;
}
