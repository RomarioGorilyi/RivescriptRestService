package com.genesys.rivescript.controller;

import com.genesys.rivescript.domain.Request;
import com.genesys.rivescript.domain.Response;
import com.genesys.rivescript.service.KnowledgeService;
import com.genesys.rivescript.service.RiveScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by RomanH on 22.03.2017.
 */
@RestController
public class RiveScriptController {

    @Autowired
    private RiveScriptService rsService;
    @Autowired
    private KnowledgeService knowledgeService;

    @RequestMapping(value = "/chatbot/", method = RequestMethod.POST, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Response replyToMessage(@RequestBody Request request, @RequestHeader("username") String username) {
        String keyWord = "knowledge ";

        String requestMessage = request.getMessage();

        String rsResponseMessage = rsService.reply(username, requestMessage);
        if (rsResponseMessage.contains(keyWord)) {
            String knowledgeResponseMessage = knowledgeService.processRequest(username, rsResponseMessage);
            return new Response(knowledgeResponseMessage);
        } else {
            return new Response(rsResponseMessage);
        }
    }

    @RequestMapping(value = "/registration/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void registerUserData(@RequestHeader("username") String username,
                                 @RequestHeader("lang") String language,
                                 @RequestHeader("topic") String topic, HttpSession session) {
        if (!topic.equals("")) {
            rsService.reply(username, "set topic " + topic);
        }
        session.setAttribute("lang", language); // TODO think over usage of this feature
    }
}
