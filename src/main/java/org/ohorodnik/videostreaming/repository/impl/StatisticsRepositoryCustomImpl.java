package org.ohorodnik.videostreaming.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import org.ohorodnik.videostreaming.entity.Statistics;
import org.ohorodnik.videostreaming.entity.Video;
import org.ohorodnik.videostreaming.repository.StatisticsRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StatisticsRepositoryCustomImpl implements StatisticsRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public Statistics retrieveStatistics(UUID uuid) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Statistics> criteriaQuery = criteriaBuilder.createQuery(Statistics.class);
        Root<Statistics> statistics = criteriaQuery.from(Statistics.class);

        criteriaQuery.where(criteriaBuilder.in(statistics.get("id")).value(
                createSubquery(uuid, criteriaBuilder, criteriaQuery)));

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    private Subquery<Integer> createSubquery(UUID uuid, CriteriaBuilder criteriaBuilder, CriteriaQuery<Statistics> criteriaQuery) {
        Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
        Root<Statistics> subqueryStat = subquery.from(Statistics.class);
        Join<Video, Statistics> subqueryVideo = subqueryStat.join("video");
        subquery.select(subqueryStat.get("id")).where(
                criteriaBuilder.equal(subqueryVideo.get("uuid"), uuid.toString()));

        return subquery;
    }
}
