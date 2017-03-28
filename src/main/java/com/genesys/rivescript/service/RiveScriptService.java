package com.genesys.rivescript.service;

import com.genesys.rivescript.configLoader.ClassPathLoader;
import com.genesys.rivescript.configLoader.ConfigStreamLoaderImpl;
import com.rivescript.Config;
import com.rivescript.RiveScript;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by RomanH on 22.03.2017.
 */
@Service
@Slf4j
public class RiveScriptService {

    private RiveScript rsEngine;

    public RiveScriptService() {
        rsEngine = new RiveScript(Config.newBuilder()
                .throwExceptions(true)           // Whether exception throwing is enabled
                .strict(true)                    // Whether strict syntax checking is enabled
                .utf8(true)                      // Whether UTF-8 mode is enabled
                .unicodePunctuation("[.,!?;:]")  // The unicode punctuation pattern
                .forceCase(false)                // Whether forcing triggers to lowercase is enabled
                .build());
        initRsEngine(new ConfigStreamLoaderImpl(rsEngine));
    }

    private void initRsEngine(ClassPathLoader.ConfigStreamLoader configStreamLoader) {
        try {
            new ClassPathLoader(configStreamLoader).loadDirectory("rivescript");
        } catch (IOException e) {
            log.error("RiveScript initialization error", e);
            e.printStackTrace();
        }

        rsEngine.sortReplies();
    }

    public String reply(String username, String message) {
        return rsEngine.reply(username, message);
    }
}
