package com.mookaps.cms.helpers;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class Common {

    public static String currentDateTime() {
        LocalDateTime date = LocalDateTime.now();
        String[] list = date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).split("T");
        return list[0] + " " + list[1].substring(0, 12);
    }

}
