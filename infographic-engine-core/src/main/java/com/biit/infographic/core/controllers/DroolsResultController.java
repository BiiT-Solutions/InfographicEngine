package com.biit.infographic.core.controllers;

/*-
 * #%L
 * Infographic Engine v2 (Core)
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


import com.biit.infographic.core.converters.DroolsResultConverter;
import com.biit.infographic.core.converters.models.DroolsResultConverterRequest;
import com.biit.infographic.core.exceptions.FormNotFoundException;
import com.biit.infographic.core.models.DroolsResultDTO;
import com.biit.infographic.core.providers.DroolsResultProvider;
import com.biit.infographic.persistence.entities.DroolsResult;
import com.biit.infographic.persistence.repositories.DroolsResultRepository;
import com.biit.server.controller.ElementController;
import com.biit.server.security.IUserOrganizationProvider;
import com.biit.server.security.model.IUserOrganization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class DroolsResultController extends ElementController<DroolsResult, Long, DroolsResultDTO, DroolsResultRepository,
        DroolsResultProvider, DroolsResultConverterRequest, DroolsResultConverter> {

    @Autowired
    protected DroolsResultController(DroolsResultProvider provider, DroolsResultConverter converter,
                                     List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProvider) {
        super(provider, converter, userOrganizationProvider);
    }

    @Override
    protected DroolsResultConverterRequest createConverterRequest(DroolsResult droolsResult) {
        return new DroolsResultConverterRequest(droolsResult);
    }


    public DroolsResultDTO findLatest(String name, Integer version, String organization, String unit, String createdBy) {
        return convert(getProvider()
                .findLatest(name, version, createdBy, organization, unit)
                .orElseThrow(() -> new FormNotFoundException(this.getClass(),
                        "No drools result found with name '" + name + "', version '" + version + "', creator '"
                                + createdBy + "' and organization '" + organization + "'.")));
    }


    public List<DroolsResultDTO> findBy(String name, Integer version, String organization, String unit, String createdBy,
                                        LocalDateTime lowerTimeBoundary, LocalDateTime upperTimeBoundary) {
        return convertAll(getProvider().findBy(name, version, organization, unit, createdBy, lowerTimeBoundary, upperTimeBoundary));
    }
}
