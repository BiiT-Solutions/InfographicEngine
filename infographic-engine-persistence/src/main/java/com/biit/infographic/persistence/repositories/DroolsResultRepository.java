package com.biit.infographic.persistence.repositories;

import com.biit.infographic.persistence.entities.DroolsResult;
import com.biit.server.persistence.repositories.ElementRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public interface DroolsResultRepository extends ElementRepository<DroolsResult, Long> {

    /**
     * Find all forms that match the search parameters. If startTime and endTime are defined, will search any appointment inside this range.
     *
     * @param formName          the organization of the parameters (can be null for any organization).
     * @param formVersion       who must resolve the appointment (can be null for any organizer).
     * @param organization      the organization (can be null for any status).
     * @param unit              the team, department, related to the infographic (can be null).
     * @param createdBy         the type of the appointment (can be null for any type).
     * @param lowerTimeBoundary the lower limit on time for searching an appointment (can be null for no limit).
     * @param upperTimeBoundary the upper limit on time for searching an appointment (can be null for no limit).
     * @return a list of appointments.
     */
    @Query("""
            SELECT a FROM DroolsResult a WHERE
            (:formName IS NULL OR a.formName = :formName) AND
            (:formVersion IS NULL OR a.formVersion = :formVersion) AND
            (:organization IS NULL OR a.organization = :organization) AND
            (:unit IS NULL OR a.unit = :unit) AND
            (:createdBy IS NULL OR a.createdBy = :createdBy) AND
            ((:lowerTimeBoundary IS NULL OR a.createdAt >= :lowerTimeBoundary) AND
            (:upperTimeBoundary IS NULL OR a.createdAt <= :upperTimeBoundary))
            ORDER BY a.createdAt DESC
            """)
    List<DroolsResult> findBy(String formName, Integer formVersion, String organization, String unit, String createdBy,
                              LocalDateTime lowerTimeBoundary, LocalDateTime upperTimeBoundary);

    /**
     * Find all forms that match the search parameters. If startTime and endTime are defined, will search any appointment inside this range.
     *
     * @param formName     the organization of the parameters (can be null for any organization).
     * @param formVersion  who must resolve the appointment (can be null for any organizer).
     * @param organization the organization (can be null for any status).
     * @param unit         the team, department, related to the infographic (can be null).
     * @param createdBy    the type of the appointment (can be null for any type).
     * @return a list of appointments.
     */
    @Query("""
            SELECT a FROM DroolsResult a WHERE
            (:formName IS NULL OR a.formName = :formName) AND
            (:formVersion IS NULL OR a.formVersion = :formVersion) AND
            (:organization IS NULL OR a.organization = :organization) AND
            (:unit IS NULL OR a.unit = :unit) AND
            (:createdBy IS NULL OR a.createdBy = :createdBy)
            ORDER BY a.createdAt DESC
            """)
    List<DroolsResult> findBy(String formName, Integer formVersion, String createdBy, String organization, String unit);

}
