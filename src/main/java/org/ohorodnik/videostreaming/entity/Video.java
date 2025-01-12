package org.ohorodnik.videostreaming.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ohorodnik.videostreaming.utils.enums.Status;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "video_sequence_generator")
    @SequenceGenerator(name = "video_sequence_generator", sequenceName = "video_id_seq", allocationSize = 1)
    private Integer id;
    private Long size;
    private String httpContentType;
    private String originalFileName;
    private String uuid;
    @Enumerated(value = EnumType.STRING)
    @Column(length = 1)
    private Status status;

    @OneToOne(mappedBy = "video", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private Metadata metadata;

    @OneToOne(mappedBy = "video", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private Statistics statistics;
}
