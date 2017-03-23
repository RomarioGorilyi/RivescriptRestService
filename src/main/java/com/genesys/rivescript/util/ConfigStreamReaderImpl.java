package com.genesys.rivescript.util;

import com.rivescript.RiveScript;
import com.rivescript.parser.ParserException;
import com.rivescript.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

/**
 * Created by RomanH on 23.03.2017.
 */
public class ConfigStreamReaderImpl implements ClassPathLoader.ConfigStreamReader {

    private RiveScript riveScriptEngine;

    public ConfigStreamReaderImpl(RiveScript riveScriptEngine) {
        this.riveScriptEngine = riveScriptEngine;
    }

    @Override
    public boolean loadStream(String file, InputStream is) throws IOException {
        // Slurp the file's contents.
        Vector<String> lines = new Vector<>();

        DataInputStream dis = new DataInputStream(is);
        BufferedReader br  = new BufferedReader(new InputStreamReader(dis));

        // Read all the lines.
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }

        dis.close();
        // Convert the vector into a string array.
        String[] code = StringUtil.convertVectorToStringArray(lines);
        // Send the code to the parser.
        try {
            riveScriptEngine.stream(code);
            return true;
        } catch (ParserException e) {
            return false;
        }
    }
}
