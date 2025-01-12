package org.ohorodnik.videostreaming.repository;

import org.ohorodnik.videostreaming.entity.Video;
import org.ohorodnik.videostreaming.utils.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {

    Optional<Video> findByUuid(String uuid);

    Optional<Video> findByUuidAndStatus(String uuid, Status status);
}
