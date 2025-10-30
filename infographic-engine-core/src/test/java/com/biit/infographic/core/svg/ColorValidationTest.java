package com.biit.infographic.core.svg;

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

import com.biit.infographic.core.models.svg.utils.Color;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "colorValidation")
public class ColorValidationTest {

    @Test
    public void checkColorWithTransparency() {
        Assert.assertTrue(Color.isValidWithTransparency("#55aa7711"));
    }

    @Test
    public void checkColorWithoutTransparency() {
        Assert.assertTrue(Color.isValidWithoutTransparency("#55AA77"));
    }

    @Test
    public void checkInvalidColorValueWithoutTransparency() {
        Assert.assertFalse(Color.isValidWithoutTransparency("#55667G"));
    }

    @Test
    public void checkInvalidSizeColorWithoutTransparency() {
        Assert.assertFalse(Color.isValidWithoutTransparency("#5566772"));
    }

    @Test
    public void checkInvalidColorValueWithTransparency() {
        Assert.assertFalse(Color.isValidWithTransparency("#55667G"));
    }

    @Test
    public void checkInvalidSizeColorWithTransparency() {
        Assert.assertFalse(Color.isValidWithTransparency("#556677221"));
    }
}
