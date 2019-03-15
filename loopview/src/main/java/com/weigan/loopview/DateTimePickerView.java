package com.weigan.loopview;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateTimePickerView extends FrameLayout {

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
    private static SimpleDateFormat DATE_FORMAT_TO_SHOW = new SimpleDateFormat("EEE MMM d");


    private static final int DEFAULT_MIN_HOUR = 1;
    private static final int DEFAULT_MIN_MIN = 0;
    private static final int DEFAULT_MAX_HOUR = 12;
    private static final int DEFAULT_MAX_MIN = 55;

    public LoopView dateLoopView;
    public LoopView hourLoopView;
    public LoopView minLoopView;
    public LoopView timeMeridiemView;

    private int datePos = 0;
    private int hourPos = 0;
    private int minPos = 0;
    private int timeMeridiemPos = 0;
    List<Date> dates = new ArrayList<>();
    ArrayList dateList = new ArrayList();
    ArrayList hourList = new ArrayList();
    ArrayList minList = new ArrayList();
    ArrayList merediumList = new ArrayList();

    private int viewTextSize = 17;
    int minHour;
    int maxHour;
    int minMin;
    int maxMin;

    private String startDate, endDate;

    public DateTimePickerView(Context context) {
        super(context);
        initView();
    }

    public DateTimePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DateTimePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.layout_date_time_picker, null);
        addView(view);
        //Optio
        minHour = DEFAULT_MIN_HOUR;
        maxHour = DEFAULT_MAX_HOUR;
        minMin = DEFAULT_MIN_MIN;
        maxMin = DEFAULT_MAX_MIN;


        long milliseconds = getLongFromyyyyMMdd(getStrTime());
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 2);

        startDate = DATE_FORMAT.format(calendar.getTime());
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, mCalendar.get(Calendar.YEAR) + 2);
        endDate = DATE_FORMAT.format(mCalendar.getTime());
        if (milliseconds != -1) {

            calendar.setTimeInMillis(milliseconds);
            hourPos = calendar.get(Calendar.HOUR) - 1;
            minPos = minList.indexOf(format2LenStr(calendar.get(Calendar.MINUTE)));
            String[] date = getStrTime().split(" ");
            if (date[1].equals("AM")) {
                timeMeridiemPos = 0;
            } else if (date[1].equals("PM")) {
                timeMeridiemPos = 1;
            }
        }

        initialiseTimeWheel(view);
    }

    private static List<Date> getDates(String dateString1, String dateString2) {
        ArrayList<Date> dates = new ArrayList<Date>();


        Date date1 = null;
        Date date2 = null;

        try {
            date1 = DATE_FORMAT.parse(dateString1);
            date2 = DATE_FORMAT.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while (!cal1.after(cal2)) {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public static long getLongFromyyyyMMdd(String time) {
        SimpleDateFormat mFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        Date parse = null;
        try {
            parse = mFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (parse != null) {
            return parse.getTime();
        } else {
            return -1;
        }
    }

    public static String getStrTime() {
        SimpleDateFormat dd = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        return dd.format(new Date());
    }


    public void initialiseTimeWheel(View view) {

        dateLoopView = (LoopView) view.findViewById(R.id.picker_date);
        hourLoopView = (LoopView) view.findViewById(R.id.picker_hour);
        minLoopView = (LoopView) view.findViewById(R.id.picker_min);
        timeMeridiemView = (LoopView) view.findViewById(R.id.picker_meridiem);


        //do not loop,default can loop
        dateLoopView.setNotLoop();
        hourLoopView.setNotLoop();
        minLoopView.setNotLoop();
        timeMeridiemView.setNotLoop();

        //set loopview text btnTextsize
        dateLoopView.setTextSize(viewTextSize);
        hourLoopView.setTextSize(viewTextSize);
        minLoopView.setTextSize(viewTextSize);
        timeMeridiemView.setTextSize(viewTextSize);

        hourLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                hourPos = index;
            }
        });
        minLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                minPos = index;
            }
        });
        dateLoopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                datePos = index;
            }
        });
        timeMeridiemView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                timeMeridiemPos = index;
            }
        });

        initPickerViews();


    }

    private void initPickerViews() {


        int hourCount = maxHour;
        int minCount = maxMin;

        for (int i = 1; i <= hourCount; i++) {
            hourList.add(format2LenStr(i));
        }

        for (int j = 0; j <= minCount; j = j + 5) {
            minList.add(format2LenStr(j));
        }


        if (!dates.isEmpty()) {
            dates.clear();
        }
        dates.addAll(getDates(startDate, endDate));

        for (int k = 0; k < dates.size(); k++) {
            if (DateUtils.isToday(dates.get(k).getTime())) {
                datePos = k;
                dateList.add("Today");
            } else {
                dateList.add(DATE_FORMAT_TO_SHOW.format(dates.get(k)));
            }
        }


        hourLoopView.setItems(hourList);
        hourLoopView.setInitPosition(hourPos);

        minLoopView.setItems(minList);
        minLoopView.setInitPosition(minPos);

        dateLoopView.setItems(dateList);
        dateLoopView.setInitPosition(datePos);

        merediumList.add("AM");
        merediumList.add("PM");
        timeMeridiemView.setItems(merediumList);
        timeMeridiemView.setInitPosition(timeMeridiemPos);

    }


    public static String format2LenStr(int num) {

        return (num < 10) ? "0" + num : String.valueOf(num);
    }


    public String getSelectedDate() {
        int hour = hourPos < 0 ? hourPos + 2 : hourPos + 1;
        int min = minPos < 0 ? 0 : minPos * 5;
        int sec = 0;
        int meredium = timeMeridiemPos;
        String merediumText = "";
        if (meredium == 0) {
            merediumText = "AM";
        } else if (meredium == 1) {
            merediumText = "PM";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dates.get(datePos));
        if (hour == 12) {
            calendar.set(Calendar.HOUR, 0);
        } else {
            calendar.set(Calendar.HOUR, hour);
        }
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);
        if (merediumText.equalsIgnoreCase("am")) {
            calendar.set(Calendar.AM_PM, Calendar.AM);
        } else {
            calendar.set(Calendar.AM_PM, Calendar.PM);
            //calendar.add(Calendar.AM_PM, Calendar.PM);
        }

        return DATE_FORMAT.format(calendar.getTime());
    }

    public void setDateTime(String dateTimeText) {
        try {
            Date dateString = DATE_FORMAT.parse(dateTimeText);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateString);

            hourPos = calendar.get(Calendar.HOUR) - 1;
            minPos = minList.indexOf(format2LenStr(calendar.get(Calendar.MINUTE)));
            String[] date = getStrTime().split(" ");
            if (date[1].equalsIgnoreCase("AM")) {
                timeMeridiemPos = 0;
            } else if (date[1].equalsIgnoreCase("PM")) {
                timeMeridiemPos = 1;
            }
            for (int k = 0; k < dates.size(); k++) {
                if (dates.get(k).after(dateString)) {
                    datePos = k - 1;
                    break;
                }
            }

            hourLoopView.setInitPosition(hourPos);
            minLoopView.setInitPosition(minPos);
            dateLoopView.setInitPosition(datePos);
            timeMeridiemView.setInitPosition(timeMeridiemPos);
        } catch (Exception e) {
        }

    }
}
