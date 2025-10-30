package com.biit.infographic.persistence.repositories;

/*-
 * #%L
 * Infographic Engine v2 (Persistence)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
            (:createdBy IS NULL OR a.createdByHash = :createdBy) AND
            ((cast(:lowerTimeBoundary as date) IS NULL OR a.createdAt >= :lowerTimeBoundary) AND
            (cast(:upperTimeBoundary as date) IS NULL OR a.createdAt <= :upperTimeBoundary))
            ORDER BY a.createdAt DESC
            """)
    List<GeneratedInfographic> findByHash(String formName, Integer formVersion, String organization, String unit,
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
            (:createdBy IS NULL OR a.createdByHash = :createdBy)
            ORDER BY a.createdAt DESC
            """)
    List<GeneratedInfographic> findByHash(String formName, Integer formVersion, String createdBy, String organization, String unit);

}
