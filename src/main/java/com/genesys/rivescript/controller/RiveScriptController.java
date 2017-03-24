package com.genesys.rivescript.controller;

import com.genesys.rivescript.service.RiveScriptManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by RomanH on 22.03.2017.
 */
@RestController
public class RiveScriptController {

    private final RiveScriptManager rsManager;

    @Autowired
    public RiveScriptController(RiveScriptManager rsManager) {
        this.rsManager = rsManager;
    }

    @RequestMapping(value = "/chatbot/dialog/{username}", method = RequestMethod.POST, produces = "application/json")
    public String replyToMessage(@RequestBody String message, @PathVariable String username) {
        return rsManager.reply(username, message);
    }
}
