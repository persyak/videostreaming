package org.ohorodnik.videostreaming.repository;

import org.ohorodnik.videostreaming.entity.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetadataRepository extends JpaRepository<Metadata, Integer> {

    List<Metadata> findByDirectorContainingIgnoreCase(String director);
}
