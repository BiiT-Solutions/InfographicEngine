package com.biit.infographic.core.converters;

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

import com.biit.infographic.core.converters.models.DroolsResultConverterRequest;
import com.biit.infographic.core.models.DroolsResultDTO;
import com.biit.infographic.persistence.entities.DroolsResult;
import com.biit.server.controller.converters.ElementConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class DroolsResultConverter extends ElementConverter<DroolsResult, DroolsResultDTO, DroolsResultConverterRequest> {


    @Override
    protected DroolsResultDTO convertElement(DroolsResultConverterRequest from) {
        final DroolsResultDTO droolsResultDTO = new DroolsResultDTO();
        BeanUtils.copyProperties(from.getEntity(), droolsResultDTO);
        return droolsResultDTO;
    }

    @Override
    public DroolsResult reverse(DroolsResultDTO to) {
        if (to == null) {
            return null;
        }
        final DroolsResult droolsResult = new DroolsResult();
        BeanUtils.copyProperties(to, droolsResult);
        return droolsResult;
    }
}
