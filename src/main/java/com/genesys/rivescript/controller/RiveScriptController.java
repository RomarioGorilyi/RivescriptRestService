package com.genesys.rivescript.controller;

import com.genesys.rivescript.domain.Request;
import com.genesys.rivescript.domain.Response;
import com.genesys.rivescript.service.KnowledgeService;
import com.genesys.rivescript.service.RiveScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by RomanH on 22.03.2017.
 */
@RestController
public class RiveScriptController {

    private RiveScriptService rsService;
    private KnowledgeService knowledgeService;

    @Autowired
    public RiveScriptController(RiveScriptService rsService, KnowledgeService knowledgeService) {
        this.rsService = rsService;
        this.knowledgeService = knowledgeService;
    }

    @RequestMapping(value = "/chatbot/", method = RequestMethod.POST, produces = "application/json")
    public Response replyToMessage(@RequestBody Request request, @RequestHeader(value = "Username") String username) {
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
}
