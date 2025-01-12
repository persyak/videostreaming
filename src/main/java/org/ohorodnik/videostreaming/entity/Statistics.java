package org.ohorodnik.videostreaming.entity;

import jakarta.persistence.Entity;
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

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statistics_sequence_generator")
    @SequenceGenerator(name = "statistics_sequence_generator", sequenceName = "statistics_id_seq", allocationSize = 1)
    private Integer id;
    private Integer impression;
    private Integer view;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;
}
