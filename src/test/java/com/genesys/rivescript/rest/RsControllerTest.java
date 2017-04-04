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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.naming.NamingException;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
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
    private MockHttpSession mockSession;

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
        this.mockSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
    }

    @Test
    public void testHandleRequestByDefaultEnglishRsService() throws Exception {
        initMVC();

        String responseMessage = getResponseMessageFromChatbot("Hello");
        String[] expectedResponses = {
                "How do you do. Please state your problem.",
                "Hi. What seems to be your problem?"
        };
        assertThat(responseMessage, anyOf(equalTo(expectedResponses[0]), equalTo(expectedResponses[1])));
    }

    @Test
    public void testHandleRequestByKnowledgeService() throws Exception {
        initMVC();

        String responseMessage = getResponseMessageFromChatbot(
                "make request to knowledge server to do something");
        String expectedResponses = "Something has been done.";
        assertThat(responseMessage, equalTo(expectedResponses));
    }

    @Test
    public void testHandleRequestByUkrainianRsLanguageService() throws Exception {
        String username = "роман";

        initMVC();
        mockSession.setAttribute("lang", "ukr");

        String responseMessage = getResponseMessageFromChatbot("мене звати " + username);
        String[] expectedResponses = {
                "Приємно познайомитись, " + username + ".",
                username + ", приємно з Вами познайомитись."
        };
        assertThat(responseMessage, anyOf(equalTo(expectedResponses[0]), equalTo(expectedResponses[1])));
    }

    @Test
    public void testRegisterUserDataAndHandleTopicRequest() throws Exception {
        initMVC();
        ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders.post("/registration/")
                .header("username", "testUser")
                .header("lang", "eng")
                .header("topic", "topic1"));
        mockSession.setAttribute("lang", "eng");
        mockSession.setAttribute("username", "testUser");

        String responseMessage = getResponseMessageFromChatbot("hello topic");
        String expectedResponse = "hi from topic1";
        assertThat(responseMessage, equalTo(expectedResponse));
    }

    private String getResponseMessageFromChatbot(String messageToProcess) throws Exception {
        ResultActions actions = mockMvc.perform(RestDocumentationRequestBuilders.post("/chatbot/")
                .content("{\"message\" : \"" + messageToProcess + "\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .session(mockSession));

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
        String jsonResponse = response.getContentAsString();
        String responseMessage = jsonResponse.substring(12, jsonResponse.length() - 2); // get only a value of the JSON response

        return responseMessage;
    }
}
