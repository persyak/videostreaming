package org.ohorodnik.videostreaming.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ohorodnik.videostreaming.utils.enums.Status;

import java.time.Duration;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Metadata {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metadata_sequence_generator")
    @SequenceGenerator(name = "metadata_sequence_generator", sequenceName = "metadata_id_seq", allocationSize = 1)
    private Integer id;
    private String title;
    private String synopsis;
    private String director;
    private String actors;
    private LocalDate yearOfRelease;
    private String genre;
    private Duration runningTime;
    @Enumerated(value = EnumType.STRING)
    @Column(length = 1)
    private Status status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;
}
