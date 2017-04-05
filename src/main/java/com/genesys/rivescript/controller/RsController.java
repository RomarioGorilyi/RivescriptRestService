package com.genesys.rivescript.controller;

import com.genesys.rivescript.domain.Request;
import com.genesys.rivescript.domain.Response;
import com.genesys.rivescript.service.knowledge.KnowledgeService;
import com.genesys.rivescript.service.rivescript.RsService;
import com.genesys.rivescript.service.rivescript.RsServicePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by RomanH on 22.03.2017.
 */
@RestController
public class RsController {

    @Autowired
    private RsServicePool rsServicePool;
    @Autowired
    private KnowledgeService knowledgeService;

    @RequestMapping(value = "/chatbot/", method = RequestMethod.POST, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Response replyToMessage(@RequestBody Request request, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            username = session.getId();
        }

        String language = (String) session.getAttribute("lang");
        if (language == null) {
            language = "eng";
        }
        RsService rsService = rsServicePool.getRsService(language);

        String requestMessage = request.getMessage();

        String rsResponseMessage = rsService.reply(username, requestMessage);
        String keyWord = "knowledge ";
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
        RsService rsService;
        if (!language.equals("")) {
            session.setAttribute("lang", language.toLowerCase().trim());
            rsService = rsServicePool.getRsService(language.toLowerCase());
        } else {
            rsService = rsServicePool.getRsService("eng");
        }

        session.setAttribute("username", username);

        if (!topic.equals("")) {
            rsService.reply(username, "set topic " + topic);
        }
    }
}
