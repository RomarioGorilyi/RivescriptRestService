package com.genesys.rivescript.configLoader;

import com.rivescript.RiveScript;
import com.rivescript.parser.ParserException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by RomanH on 23.03.2017.
 */
@Slf4j
public class ConfigStreamLoaderImpl implements ClassPathLoader.ConfigStreamLoader {

    private RiveScript rsEngine;

    public ConfigStreamLoaderImpl(RiveScript rsEngine) {
        this.rsEngine = rsEngine;
    }

    @Override
    public boolean loadStreamCode(InputStream inputStream) throws IOException {
        ArrayList<String> lines = new ArrayList<>();

        DataInputStream dis = new DataInputStream(inputStream);
        BufferedReader br  = new BufferedReader(new InputStreamReader(dis));

        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        dis.close();
        String[] code = lines.toArray(new String[lines.size()]);

        try {
            rsEngine.stream(code);
            return true;
        } catch (ParserException e) {
            log.error("Parser exception is caught", e);
            return false;
        }
    }
}
