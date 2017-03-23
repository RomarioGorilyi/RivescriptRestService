package com.genesys.rivescript.util;

import java.util.Vector;

/**
 * Created by RomanH on 23.03.2017.
 */
public class StringUtil {

    public static String[] convertVectorToStringArray(Vector<String> vector) {
        String[] result = new String [vector.size()];

        int counter = 0;
        for (String string : vector) {
            result[counter++] = string;
        }
        return result;
    }
}
