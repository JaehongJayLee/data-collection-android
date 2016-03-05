package com.hongdoki.datacollection.util;

import java.util.Collection;
import java.util.Iterator;

public class StringUtil {

    public static String join(final Collection<?> strings, String delimeter) {
        if (delimeter == null) {
            delimeter = ",";
        }
        if (strings.isEmpty()) {
            return "";
        }
        StringBuilder joined = new StringBuilder();
        Iterator<?> stringIter = strings.iterator();
        joined.append(stringIter.next().toString());
        while (stringIter.hasNext()) {
            joined.append(delimeter);
            joined.append(stringIter.next().toString());
        }
        return joined.toString();
    }

    public static String parseTwoDigits(int i) {
        return String.format("%02d", i);
    }
}
