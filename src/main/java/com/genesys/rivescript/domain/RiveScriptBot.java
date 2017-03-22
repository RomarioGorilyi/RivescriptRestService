package com.genesys.rivescript.domain;

import com.genesys.rivescript.ClassPathLoader;
import com.rivescript.RiveScript;
import org.springframework.stereotype.Component;

/**
 * Created by RomanH on 22.03.2017.
 */
@Component
public class RiveScriptBot {

    private final static String DEFAULT_RESOURCES_PATH = "src/main/resources/static/rivescript";

    private RiveScript bot;

    public RiveScriptBot() {
        bot = new RiveScript();
        new ClassPathLoader(rs).loadDirectory("Aiden");
        initBot(DEFAULT_RESOURCES_PATH);
    }

    public RiveScriptBot(String resourcesPath) {
        bot = new RiveScript();
        initBot(resourcesPath);
    }

    private void initBot(String resourcesPath) {
        bot.loadDirectory(resourcesPath);
        bot.sortReplies();
    }
}
