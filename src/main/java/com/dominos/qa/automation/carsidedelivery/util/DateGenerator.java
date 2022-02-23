package com.dominos.qa.automation.carsidedelivery.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateGenerator {
    public static String generateDate(String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        return date;
    }
}
