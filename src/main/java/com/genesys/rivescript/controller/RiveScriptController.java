package com.genesys.rivescript.controller;

import com.genesys.rivescript.service.RiveScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by RomanH on 22.03.2017.
 */
@RestController
public class RiveScriptController {

    private final RiveScriptService rsManager;

    @Autowired
    public RiveScriptController(RiveScriptService rsManager) {
        this.rsManager = rsManager;
    }

    @RequestMapping(value = "/chatbot/dialog/{username}", method = RequestMethod.POST, produces = "application/json")
    public String replyToMessage(@RequestBody String message, @PathVariable String username) {
        return rsManager.reply(username, message);
    }
}
