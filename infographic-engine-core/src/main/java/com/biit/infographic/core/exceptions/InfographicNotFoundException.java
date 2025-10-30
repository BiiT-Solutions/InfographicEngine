package com.biit.infographic.core.exceptions;

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

import com.biit.logger.ExceptionType;
import com.biit.server.logger.LoggedException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class InfographicNotFoundException extends LoggedException {

    @Serial
    private static final long serialVersionUID = 5636450789353772632L;

    public InfographicNotFoundException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public InfographicNotFoundException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.DEBUG, HttpStatus.NOT_FOUND);
    }

    public InfographicNotFoundException(Class<?> clazz) {
        this(clazz, "No infographics found!");
    }

    public InfographicNotFoundException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}
