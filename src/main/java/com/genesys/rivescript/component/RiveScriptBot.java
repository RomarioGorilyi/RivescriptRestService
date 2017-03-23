package com.genesys.rivescript.component;

import com.genesys.rivescript.util.ClassPathLoader;
import com.genesys.rivescript.util.ConfigStreamReaderImpl;
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
    private ConfigStreamReaderImpl configStreamReader;

    public RiveScriptBot() {
        riveScriptEngine = new RiveScript(Config.newBuilder()
                .throwExceptions(true)           // Whether exception throwing is enabled
                .strict(true)                    // Whether strict syntax checking is enabled
                .utf8(true)                      // Whether UTF-8 mode is enabled
                .unicodePunctuation("[.,!?;:]")  // The unicode punctuation pattern
                .forceCase(false)                // Whether forcing triggers to lowercase is enabled
                .build());
        configStreamReader = new ConfigStreamReaderImpl(riveScriptEngine);
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
        return riveScriptEngine.reply("localuser", message);
    }
}
