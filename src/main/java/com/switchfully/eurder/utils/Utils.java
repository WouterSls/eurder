package com.switchfully.eurder.utils;

import java.util.regex.Pattern;

public class Utils {

    public static boolean isValidUUIDFormat(String id){
        Pattern UUID_REGEX =
                Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        if (id == null){
            return false;
        }
        return UUID_REGEX.matcher(id).matches();
    }
}
