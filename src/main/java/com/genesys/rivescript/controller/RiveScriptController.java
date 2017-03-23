package com.genesys.rivescript.controller;

import com.genesys.rivescript.component.RiveScriptBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by RomanH on 22.03.2017.
 */
@RestController
public class RiveScriptController {

    private final RiveScriptBot bot;

    @Autowired
    public RiveScriptController(RiveScriptBot bot) {
        this.bot = bot;
    }

    @RequestMapping(value = "/chatbot/dialog/", method = RequestMethod.POST, produces = "application/json")
    public String replyToMessage(@RequestBody String message) {
        return bot.reply(message);
    }
}
