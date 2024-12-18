package com.biit.infographic.persistence.repositories;

import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.server.persistence.repositories.ElementRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public interface GeneratedInfographicRepository extends ElementRepository<GeneratedInfographic, Long> {

    /**
     * Find all infographics that match the search parameters.
     * If startTime and endTime are defined, will search any appointment inside this range.
     *
     * @param formName          the form name
     * @param formVersion       the version (can be null for any version).
     * @param organization      the organization of the infographic (can be null for any organization).
     * @param unit              the team, department, related to the infographic (can be null).
     * @param createdBy         who has created the infographic or its owner.
     * @param lowerTimeBoundary the lower limit on time for searching an appointment (can be null for no limit).
     * @param upperTimeBoundary the upper limit on time for searching an appointment (can be null for no limit).
     * @return a list of infographics.
     */
    @Query("""
            SELECT a FROM GeneratedInfographic a WHERE
            (:formName IS NULL OR a.formName = :formName) AND
            (:formVersion IS NULL OR a.formVersion = :formVersion) AND
            (:organization IS NULL OR a.organization = :organization) AND
            (:unit IS NULL OR a.unit = :unit) AND
            (:createdBy IS NULL OR a.createdBy = :createdBy) AND
            ((cast(:lowerTimeBoundary as date) IS NULL OR a.createdAt >= :lowerTimeBoundary) AND
            (cast(:upperTimeBoundary as date) IS NULL OR a.createdAt <= :upperTimeBoundary))
            ORDER BY a.createdAt DESC
            """)
    List<GeneratedInfographic> findBy(String formName, Integer formVersion, String organization, String unit,
                                      String createdBy, LocalDateTime lowerTimeBoundary, LocalDateTime upperTimeBoundary);

    /**
     * Find all infographics that match the search parameters.
     * If startTime and endTime are defined, will search any appointment inside this range.
     *
     * @param formName     the form name
     * @param formVersion  the version (can be null for any version).
     * @param organization the organization of the infographic (can be null for any organization).
     * @param unit         the team, department, related to the infographic (can be null).
     * @param createdBy    who has created the infographic or its owner.
     * @return a list of infographics.
     */
    @Query("""
            SELECT a FROM GeneratedInfographic a WHERE
            (:formName IS NULL OR a.formName = :formName) AND
            (:formVersion IS NULL OR a.formVersion = :formVersion) AND
            (:organization IS NULL OR a.organization = :organization) AND
            (:unit IS NULL OR a.unit = :unit) AND
            (:createdBy IS NULL OR a.createdBy = :createdBy)
            ORDER BY a.createdAt DESC
            """)
    List<GeneratedInfographic> findBy(String formName, Integer formVersion, String createdBy, String organization, String unit);

}
