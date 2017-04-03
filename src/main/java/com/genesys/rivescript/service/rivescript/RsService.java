package com.genesys.rivescript.service.rivescript;

import com.genesys.rivescript.configLoader.ClassPathLoader;
import com.genesys.rivescript.configLoader.ConfigStreamLoaderImpl;
import com.rivescript.Config;
import com.rivescript.RiveScript;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Created by RomanH on 22.03.2017.
 */
@Slf4j
public class RsService {

    private RiveScript rsEngine;

    public RsService(String pathToResources) {
        rsEngine = new RiveScript(Config.newBuilder()
                .throwExceptions(true)           // Whether exception throwing is enabled
                .strict(true)                    // Whether strict syntax checking is enabled
                .utf8(true)                      // Whether UTF-8 mode is enabled
                .unicodePunctuation("[.,!?;:]")  // The unicode punctuation pattern
                .forceCase(false)                // Whether forcing triggers to lowercase is enabled
                .build());

        initRsScripts(new ConfigStreamLoaderImpl(rsEngine), pathToResources);
    }

    private void initRsScripts(ClassPathLoader.ConfigStreamLoader configStreamLoader, String pathToResources) {
        try {
            new ClassPathLoader(configStreamLoader).loadDirectory("rivescript\\" + pathToResources);
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
