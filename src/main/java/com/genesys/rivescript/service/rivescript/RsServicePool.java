package com.genesys.rivescript.service.rivescript;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Pool for the RiveScript services that handle conversations in different languages separately.
 */
@Service
public class RsServicePool {

    @Getter
    private final Map<String, RsService> rsServices;

    public RsServicePool() {
        rsServices = new HashMap<>();
        initServices();
    }

    public RsService getRsService(String language) {
        return rsServices.get(language);
    }

    private void initServices() {
        List<String> supportedLanguages = listSupportedLanguages();
        for (String lang : supportedLanguages) {
            rsServices.put(lang, new RsService(lang));
        }
    }

    /**
     * Lists all supported languages by parsing resource folder with RiveScript sources files.
     *
     * @return {@code List<String>} of supported languages
     */
    private List<String> listSupportedLanguages() {
        List<String> supportedLanguages = new ArrayList<>();

        File resourcesFolder = new File("src/main/resources/rivescript/");
        File[] files = resourcesFolder.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                supportedLanguages.add(file.getName());
            }
        }

        return supportedLanguages;
    }
}
