package com.genesys.rivescript.rest;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.ServletWebRequest;

import javax.naming.NamingException;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by RomanH on 24.03.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RsControllerTest {

    @Autowired
    WebApplicationContext wac; // cached

    private MockMvc mockMvc;

    @Rule
    public JUnitRestDocumentation restDocumentation =
            new JUnitRestDocumentation("target/generated-snippets");

    private RestDocumentationResultHandler document;

    @BeforeClass
    public static void init() throws NamingException {
        SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
        builder.activate();
    }

    @Before
    public void setUp() {
        this.document = document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()));
    }

    private void initMVC() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.document)
                .build();
    }

    @Test
    public void testRsControllerHandleRequestByRsService() throws Exception {
        initMVC();
        ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders.post("/chatbot/")
                .header("Username", "localuser")
                .content("{\"message\" : \"Hello\"}")
                .contentType(MediaType.APPLICATION_JSON));
        MockHttpServletResponse response = actions.andExpect(status().isOk())
                .andDo(this.document.document(
                        requestFields (
                                fieldWithPath("message").type(JsonFieldType.STRING).description("user request message")
                        ),

                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("generated response message")
                        ))
                )
                .andReturn().getResponse();
    }

    @Test
    public void testRsControllerHandleRequestByKnowledgeService() throws Exception {
        initMVC();
        ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders.post("/chatbot/")
                .header("Username", "localuser")
                .content("{\"message\" : \"KnowledGE help me with something\"}")
                .contentType(MediaType.APPLICATION_JSON));
        MockHttpServletResponse response = actions.andExpect(status().isOk())
                .andDo(this.document.document(
                        requestFields (
                                fieldWithPath("message").type(JsonFieldType.STRING).description("user request message")
                        ),

                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("generated response message")
                        ))
                )
                .andReturn().getResponse();
    }
}
