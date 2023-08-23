package com.biit.infographic.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

@Configuration
@TestPropertySource("classpath:application.properties")
@ComponentScan({"com.biit.infographic.core"})
public class TestConfiguration {

}
