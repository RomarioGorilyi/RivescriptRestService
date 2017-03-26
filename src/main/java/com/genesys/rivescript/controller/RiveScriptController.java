package com.genesys.rivescript.controller;

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
    public String replyToMessage(@RequestBody String message, @RequestHeader(value = "Username") String username) {
        if (message.toLowerCase().startsWith("knowledge")) {
            return knowledgeService.processRequest(username, message);
        } else {
            return rsService.reply(username, message);
        }
    }
}
