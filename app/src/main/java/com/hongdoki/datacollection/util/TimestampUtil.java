package com.hongdoki.datacollection.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampUtil {
    private static SimpleDateFormat dataFormatWithMillis
            =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static String currentStringWithMillis() {
        return dataFormatWithMillis.format(new Date());
    }
}
