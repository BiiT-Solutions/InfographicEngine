package com.biit.infographic.rest.tests;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Locale;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@Test(groups = "emails")
public class InfographicEmailTests extends AbstractTestNGSpringContextTests {

    @Autowired
    private MessageSource messageSource;

    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("language/language");
        return source;
    }

    @Test
    public void checkTranslations() {
        Assert.assertEquals(messageSource().getMessage("pdf.infographic.mail.subject", null, Locale.ENGLISH), "New infographic received!");
    }

    @Test
    public void checkTranslationsBean() {
        Assert.assertEquals(messageSource.getMessage("pdf.infographic.mail.subject", null, Locale.ENGLISH), "New infographic received!");
    }
}
