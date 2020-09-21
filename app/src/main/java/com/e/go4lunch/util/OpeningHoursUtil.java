package com.e.go4lunch.util;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OpeningHoursUtil {


    public static int getTodayWeekDay() {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        //-1 to start at monday
        int day = Calendar.getInstance().get(Integer.parseInt(dayOfTheWeek));
        int googleApiIndex;
        switch (day) {
            case Calendar.MONDAY:
                googleApiIndex = 0;
                break;
            case Calendar.TUESDAY:
                googleApiIndex = 1;
                break;
            case Calendar.WEDNESDAY:
                googleApiIndex = 2;
                break;
            case Calendar.THURSDAY:
                googleApiIndex = 3;
                break;
            case Calendar.FRIDAY:
                googleApiIndex = 4;
                break;
            case Calendar.SATURDAY:
                googleApiIndex = 5;
                break;
            case Calendar.SUNDAY:
                googleApiIndex = 6;
                break;
            default:
                googleApiIndex = 0;
                break;
        }
        return googleApiIndex;
    }
}
