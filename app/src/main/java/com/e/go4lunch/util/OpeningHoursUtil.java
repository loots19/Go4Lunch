package com.e.go4lunch.util;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.e.go4lunch.R;
import com.e.go4lunch.models.Restaurant;
import com.e.go4lunch.models.placeDetail.Period;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OpeningHoursUtil {


    private Restaurant mRestaurant;
    private int localTime;
    private int day;
    private TextView mTvTime;
    private Context mContext;


    public OpeningHoursUtil(Restaurant restaurant, TextView textView, Context context) {
        this.mRestaurant = restaurant;
        this.mTvTime = textView;
        this.mContext = context;
    }

    public void checkOpening() {
        getActualTimeAndDay();
        getOpeningHoursToday();
    }

    private void getActualTimeAndDay() {
        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HHmm", Locale.FRANCE);

        localTime = Integer.parseInt(date.format(currentLocalTime));
        //Check which day it is
        day = cal.get(Calendar.DAY_OF_WEEK);
    }


    private void getOpeningHoursToday() {
        //Get list of opening hours for the restaurant
        List<Period> openHours = mRestaurant.getOpenHours();
        int dayOfWeek = 0;

        //Check which day it is and set dayOfWeek to get the right opening hours
        switch (day) {
            case Calendar.SUNDAY:
                // Current day is Sunday
                dayOfWeek = 0;
                break;
            case Calendar.MONDAY:
                dayOfWeek = 1;
                break;
            case Calendar.TUESDAY:
                dayOfWeek = 2;
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = 3;
                break;
            case Calendar.THURSDAY:
                dayOfWeek = 4;
                break;
            case Calendar.FRIDAY:
                dayOfWeek = 5;
                break;
            case Calendar.SATURDAY:
                dayOfWeek = 6;
                break;
        }

        for (int i = 0; i < openHours.size(); i++) {

            //Find the corresponding open hours object in list depending on weekday

            if (openHours.get(i).getOpen().getDay() == dayOfWeek) {

                //Opening and closing lunch
                String opening = openHours.get(i).getOpen().getTime();
                String closing = openHours.get(i).getClose().getTime();
                int closingInt = Integer.parseInt(closing);
                int openingInt = Integer.parseInt(opening);


                //check if restaurant is open and if is closing in 30 minutes;
                if ((closingInt - localTime) < 30 && (localTime - closingInt) < 0) {
                    mTvTime.setText(R.string.closing_soon);
                    mTvTime.setTextColor(Color.RED);
                }
                //check If restaurant is closed
                else if (localTime > closingInt) {
                    mTvTime.setText(R.string.Close);

                }
                //check If restaurant is not yet open for lunch
                else if (localTime < openingInt) {
                    String str = Integer.toString(openingInt);
                    str = new StringBuilder(str).insert(str.length() - 2, ".").toString();
                    String text = mContext.getResources().getString(R.string.OpenAt) + " " + str + "pm";
                    mTvTime.setText(text);
                }
                // check if restaurant is open, show closing time
                else if (localTime < closingInt) {
                    String str = Integer.toString(closingInt);
                    str = new StringBuilder(str).insert(str.length() - 2, ".").toString();
                    String text = mContext.getResources().getString(R.string.OpenUntil) + " " + str + "pm";
                    mTvTime.setText(text);

                }
               
            }
        }
    }
}
