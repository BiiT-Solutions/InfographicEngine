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
     * @param formName          the organization of the parameters (can be null for any organization).
     * @param formVersion       who must resolve the appointment (can be null for any organizer).
     * @param organization      the status of the appointment (can be null for any status).
     * @param createdBy         the type of the appointment (can be null for any type).
     * @param lowerTimeBoundary the lower limit on time for searching an appointment (can be null for no limit).
     * @param upperTimeBoundary the upper limit on time for searching an appointment (can be null for no limit).
     * @return a list of infographics.
     */
    @Query("""
            SELECT a FROM GeneratedInfographic a WHERE
            (:formName IS NULL OR a.formName = :formName) AND
            (:formVersion IS NULL OR a.formVersion = :formVersion) AND
            (:organization IS NULL OR a.organization = :organization) AND
            (:createdBy IS NULL OR a.createdBy = :createdBy) AND
            ((cast(:lowerTimeBoundary as date) IS NULL OR a.createdAt >= :lowerTimeBoundary) AND
            (cast(:upperTimeBoundary as date) IS NULL OR a.createdAt <= :upperTimeBoundary))
            ORDER BY a.createdAt DESC
            """)
    List<GeneratedInfographic> findBy(String formName, Integer formVersion, String organization, String createdBy,
                                      LocalDateTime lowerTimeBoundary, LocalDateTime upperTimeBoundary);

    /**
     * Find all infographics that match the search parameters.
     * If startTime and endTime are defined, will search any appointment inside this range.
     *
     * @param formName     the organization of the parameters (can be null for any organization).
     * @param formVersion  who must resolve the appointment (can be null for any organizer).
     * @param organization the status of the appointment (can be null for any status).
     * @param createdBy    the type of the appointment (can be null for any type).
     * @return a list of infographics.
     */
    @Query("""
            SELECT a FROM GeneratedInfographic a WHERE
            (:formName IS NULL OR a.formName = :formName) AND
            (:formVersion IS NULL OR a.formVersion = :formVersion) AND
            (:organization IS NULL OR a.organization = :organization) AND
            (:createdBy IS NULL OR a.createdBy = :createdBy)
            ORDER BY a.createdAt DESC
            """)
    List<GeneratedInfographic> findBy(String formName, Integer formVersion, String createdBy, String organization);

}
