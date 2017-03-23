package com.genesys.rivescript.component;

import com.genesys.rivescript.configLoader.ClassPathLoader;
import com.genesys.rivescript.configLoader.ConfigStreamLoaderImpl;
import com.rivescript.Config;
import com.rivescript.RiveScript;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by RomanH on 22.03.2017.
 */
@Component
@Slf4j
public class RiveScriptBot {

    private RiveScript riveScriptEngine;
    private ConfigStreamLoaderImpl configStreamReader;

    public RiveScriptBot() {
        riveScriptEngine = new RiveScript(Config.newBuilder()
                .throwExceptions(true)           // Whether exception throwing is enabled
                .strict(true)                    // Whether strict syntax checking is enabled
                .utf8(true)                      // Whether UTF-8 mode is enabled
                .unicodePunctuation("[.,!?;:]")  // The unicode punctuation pattern
                .forceCase(false)                // Whether forcing triggers to lowercase is enabled
                .build());
        configStreamReader = new ConfigStreamLoaderImpl(riveScriptEngine);
        initBot();
    }

    private void initBot() {
        try {
            new ClassPathLoader(configStreamReader).loadDirectory("rivescript");
        } catch (IOException e) {
            log.error("RiveScript initialization error", e);
            e.printStackTrace();
        }

        riveScriptEngine.sortReplies();
    }

    public String reply(String message) {
        return riveScriptEngine.reply("localuser", message); // TODO define username
    }
}
