package com.biit.infographic.core.svg;

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
