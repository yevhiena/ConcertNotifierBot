package com.alevel.concertnotifierbot.util;

public class ParseCallbackDataUtil {

    public static String getAction(String data){
        return data.split(" ")[0];
    }

    public static long getConcertId(String data){
        return Long.parseLong(data.split(" ")[1]);
    }
}
