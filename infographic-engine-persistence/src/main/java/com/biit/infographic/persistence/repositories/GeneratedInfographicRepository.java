package com.biit.infographic.persistence.repositories;

import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.server.persistence.repositories.ElementRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface GeneratedInfographicRepository extends ElementRepository<GeneratedInfographic, Long> {

    /**
     * Find all infographic that matches the search parameters. If startTime and endTime is defined, will search any appointment inside this range.
     *
     * @param formName          the organization of the parameters (can be null for any organization).
     * @param formVersion       who must resolve the appointment (can be null for any organizer).
     * @param organizationId    the status of the appointment (can be null for any status).
     * @param createdBy         the type of the appointment (can be null for any type).
     * @param lowerTimeBoundary the lower limit on time for searching an appointment  (can be null for no limit).
     * @param upperTimeBoundary the upper limit on time for searching an appointment  (can be null for no limit).
     * @return a list of infographics.
     */
    @Query("""
            SELECT a FROM GeneratedInfographic a WHERE
            (:formName IS NULL OR a.formName = :formName) AND
            (:formVersion IS NULL OR a.formVersion = :formVersion) AND
            (:organizationId IS NULL OR a.organizationId = :organizationId) AND
            (:createdBy IS NULL OR a.createdBy = :createdBy) AND
            ((:lowerTimeBoundary IS NULL OR a.createdAt >= :lowerTimeBoundary) AND
            (:upperTimeBoundary IS NULL OR a.createdAt <= :upperTimeBoundary))
            ORDER BY a.createdAt DESC
            """)
    List<GeneratedInfographic> findBy(String formName, Integer formVersion, Long organizationId, String createdBy,
                                      LocalDateTime lowerTimeBoundary, LocalDateTime upperTimeBoundary);

    /**
     * Find all infographic that matches the search parameters. If startTime and endTime is defined, will search any appointment inside this range.
     *
     * @param formName       the organization of the parameters (can be null for any organization).
     * @param formVersion    who must resolve the appointment (can be null for any organizer).
     * @param organizationId the status of the appointment (can be null for any status).
     * @param createdBy      the type of the appointment (can be null for any type).
     * @return a list of infographics.
     */
    @Query(value = """
            SELECT TOP 1 a FROM GeneratedInfographic a WHERE
            (:formName IS NULL OR a.formName = :formName) AND
            (:formVersion IS NULL OR a.formVersion = :formVersion) AND
            (:organizationId IS NULL OR a.organizationId = :organizationId) AND
            (:createdBy IS NULL OR a.createdBy = :createdBy)
            ORDER BY a.createdAt DESC
            """, nativeQuery = true)
    Optional<GeneratedInfographic> findLatest(String formName, Integer formVersion, String createdBy, Long organizationId);

}
