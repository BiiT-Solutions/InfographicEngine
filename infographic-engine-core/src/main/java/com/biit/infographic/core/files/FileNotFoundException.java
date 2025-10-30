package com.biit.infographic.core.files;

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
import com.biit.server.exceptions.NotFoundException;

import java.io.Serial;

public class FileNotFoundException extends NotFoundException {
    @Serial
    private static final long serialVersionUID = 7131894111678894371L;

    public FileNotFoundException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public FileNotFoundException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public FileNotFoundException(Class<?> clazz) {
        this(clazz, "GlobalVariables not found");
    }

    public FileNotFoundException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}
