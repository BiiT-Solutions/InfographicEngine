package com.biit.infographic.rest.tests;

import com.biit.infographic.core.controllers.GeneratedInfographicController;
import com.biit.infographic.core.models.GeneratedInfographicDTO;
import com.biit.server.security.AuthenticatedUserProvider;
import com.biit.server.security.model.AuthRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@Test(groups = "infographicRest")
public class InfographicRestTests extends AbstractTestNGSpringContextTests {
    private final static String USER_NAME = "user";
    private final static String USER_PASSWORD = "password";

    private final static String JWT_SALT = "4567";
    private final static String FORM_NAME = "CADT";
    private final static int FORM_VERSION = 1;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private GeneratedInfographicController generatedInfographicController;

    private MockMvc mockMvc;

    private String jwtToken;

    private <T> String toJson(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    private <T> T fromJson(String payload, Class<T> clazz) throws IOException {
        return objectMapper.readValue(payload, clazz);
    }

    @BeforeClass
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }


    @BeforeClass(dependsOnMethods = "setUp")
    public void addUser() {
        //Create the admin user
        authenticatedUserProvider.createUser(USER_NAME, USER_NAME, USER_PASSWORD);
    }


    @Test
    public void checkAuthentication() {
        //Check the admin user
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USER_NAME, JWT_SALT + USER_PASSWORD));
    }


    @Test
    public void setAuthentication() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername(USER_NAME);
        request.setPassword(USER_PASSWORD);

        MvcResult createResult = this.mockMvc
                .perform(post("/auth/public/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();

        jwtToken = createResult.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
        Assert.assertNotNull(jwtToken);
    }


    @Test
    public void checkPublicRestService() throws Exception {
        //Info services are opened in rest-server library
        mockMvc.perform(get("/info/health-check")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }


    //NO data, 404 returned.
    @Test(dependsOnMethods = "setAuthentication")
    public void getLatestInfographicWithoutData() throws Exception {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("form", FORM_NAME);
        requestParams.add("version", String.valueOf(FORM_VERSION));
        requestParams.add("createdBy", USER_NAME);

        mockMvc.perform(get("/infographic/find/latest")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .params(requestParams)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }


    @Test(dependsOnMethods = "getLatestInfographicWithoutData")
    public void populateDatabase() {
        final GeneratedInfographicDTO generatedInfographic = new GeneratedInfographicDTO();
        generatedInfographic.setFormName(FORM_NAME);
        generatedInfographic.setFormVersion(FORM_VERSION);
        generatedInfographicController.create(generatedInfographic, USER_NAME);


        final GeneratedInfographicDTO generatedInfographic2 = new GeneratedInfographicDTO();
        generatedInfographic2.setFormName(FORM_NAME + "_bad");
        generatedInfographic2.setFormVersion(FORM_VERSION);
        generatedInfographicController.create(generatedInfographic2, USER_NAME);

        final GeneratedInfographicDTO generatedInfographic3 = new GeneratedInfographicDTO();
        generatedInfographic3.setFormName(FORM_NAME);
        generatedInfographic3.setFormVersion(FORM_VERSION);
        generatedInfographicController.create(generatedInfographic3, USER_NAME + "_bad");

        final GeneratedInfographicDTO generatedInfographic4 = new GeneratedInfographicDTO();
        generatedInfographic4.setFormName(FORM_NAME);
        generatedInfographic4.setFormVersion(FORM_VERSION + 1);
        generatedInfographicController.create(generatedInfographic4, USER_NAME);
    }


    @Test(dependsOnMethods = "populateDatabase")
    public void getLatestInfographicWithData() throws Exception {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("form", FORM_NAME);
        requestParams.add("version", String.valueOf(FORM_VERSION));
        requestParams.add("createdBy", USER_NAME);

        mockMvc.perform(get("/infographic/find/latest")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .params(requestParams)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }


    @Test(dependsOnMethods = "populateDatabase")
    public void getLatestInfographicWithDataInvalidVersion() throws Exception {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("form", FORM_NAME);
        requestParams.add("version", "500");
        requestParams.add("createdBy", USER_NAME);

        mockMvc.perform(get("/infographic/find/latest")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .params(requestParams)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    @Test(dependsOnMethods = "populateDatabase")
    public void getLatestInfographicWithDataInvalidUser() throws Exception {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("form", FORM_NAME);
        requestParams.add("version", String.valueOf(FORM_VERSION));
        requestParams.add("createdBy", USER_NAME + "_not_me");

        mockMvc.perform(get("/infographic/find/latest")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .params(requestParams)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    @Test(dependsOnMethods = "populateDatabase")
    public void getLatestInfographicWithDataInvalidFormName() throws Exception {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("form", FORM_NAME + "_incorrect");
        requestParams.add("version", String.valueOf(FORM_VERSION));
        requestParams.add("createdBy", USER_NAME);

        mockMvc.perform(get("/infographic/find/latest")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .params(requestParams)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }


    @Test(dependsOnMethods = "populateDatabase")
    public void getAllInfographicWithData() throws Exception {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("form", FORM_NAME);
        requestParams.add("version", String.valueOf(FORM_VERSION));
        requestParams.add("createdBy", USER_NAME);

        MvcResult createResult = mockMvc.perform(get("/infographic/find")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .params(requestParams)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final List<GeneratedInfographicDTO> results = Arrays.asList(objectMapper.readValue(createResult.getResponse().getContentAsString(), GeneratedInfographicDTO[].class));
        Assert.assertEquals(results.size(), 1);
    }


    @Test(dependsOnMethods = "populateDatabase")
    public void getAllInfographicByUser() throws Exception {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("createdBy", USER_NAME);

        MvcResult createResult = mockMvc.perform(get("/infographic/find")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .params(requestParams)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final List<GeneratedInfographicDTO> results = Arrays.asList(objectMapper.readValue(createResult.getResponse().getContentAsString(), GeneratedInfographicDTO[].class));
        Assert.assertEquals(results.size(), 3);
    }


    @Test(dependsOnMethods = "populateDatabase")
    public void getAllInfographicByFormName() throws Exception {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("form", FORM_NAME);

        MvcResult createResult = mockMvc.perform(get("/infographic/find")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .params(requestParams)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final List<GeneratedInfographicDTO> results = Arrays.asList(objectMapper.readValue(createResult.getResponse().getContentAsString(), GeneratedInfographicDTO[].class));
        Assert.assertEquals(results.size(), 3);
    }


    @Test(dependsOnMethods = "populateDatabase")
    public void getAllInfographicByVersion() throws Exception {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("version", String.valueOf(FORM_VERSION));

        MvcResult createResult = mockMvc.perform(get("/infographic/find")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .params(requestParams)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final List<GeneratedInfographicDTO> results = Arrays.asList(objectMapper.readValue(createResult.getResponse().getContentAsString(), GeneratedInfographicDTO[].class));
        Assert.assertEquals(results.size(), 3);
    }


    @Test(dependsOnMethods = "populateDatabase")
    public void getAllInfographicByWrongVersion() throws Exception {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("version", String.valueOf(3));

        MvcResult createResult = mockMvc.perform(get("/infographic/find")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .params(requestParams)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final List<GeneratedInfographicDTO> results = Arrays.asList(objectMapper.readValue(createResult.getResponse().getContentAsString(), GeneratedInfographicDTO[].class));
        Assert.assertEquals(results.size(), 0);
    }


    @Test(dependsOnMethods = "populateDatabase")
    public void getAllInfographicByDate() throws Exception {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("from", "2023-01-01T00:00:00.00Z");
        requestParams.add("to", "2050-01-01T00:00:00.00Z");

        MvcResult createResult = mockMvc.perform(get("/infographic/find")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .params(requestParams)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final List<GeneratedInfographicDTO> results = Arrays.asList(objectMapper.readValue(createResult.getResponse().getContentAsString(), GeneratedInfographicDTO[].class));
        Assert.assertEquals(results.size(), 4);
    }

    @Test(dependsOnMethods = "populateDatabase")
    public void getAllInfographicByWrongDate() throws Exception {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("from", "2049-01-01T00:00:00.00Z");
        requestParams.add("to", "2050-01-01T00:00:00.00Z");

        MvcResult createResult = mockMvc.perform(get("/infographic/find")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .params(requestParams)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        final List<GeneratedInfographicDTO> results = Arrays.asList(objectMapper.readValue(createResult.getResponse().getContentAsString(), GeneratedInfographicDTO[].class));
        Assert.assertEquals(results.size(), 0);
    }
}
