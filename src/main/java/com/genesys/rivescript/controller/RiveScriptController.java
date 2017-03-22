package com.genesys.rivescript.controller;

import com.genesys.rivescript.domain.RiveScriptBot;
import com.rivescript.RiveScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @RequestMapping(value = "/chatBot/dialog", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String answerQuestion(@RequestBody String question) {


        return null;
    }
}
