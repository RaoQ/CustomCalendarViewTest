package study.rq.com.customcalendar.oldcalendar;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Cao-Human on 2016/4/22
 */
public class DateHelper {
    public static int[] getDate(int position) {
        int[] data = new int[4];
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, position - Integer.MAX_VALUE / 2);
        data[1] = calendar.get(Calendar.MONTH) + 1;
        data[2] = calendar.get(Calendar.YEAR);
        calendar.add(Calendar.MONTH, 1);
        data[3] = calendar.get(Calendar.MONTH) + 1;
        calendar.add(Calendar.MONTH, -2);
        data[0] = calendar.get(Calendar.MONTH) + 1;
        return data;
    }

    public static List<BeanDate> make() {
        return make(Integer.MAX_VALUE / 2);
    }

    public static List<BeanDate> make(int position) {
        Calendar calendar = Calendar.getInstance();
        final int toYear = calendar.get(Calendar.YEAR);
        final int toMonth = calendar.get(Calendar.MONTH) + 1;
        final int toDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, position - Integer.MAX_VALUE / 2);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        final int currentYear = calendar.get(Calendar.YEAR);
        final int currentMonth = calendar.get(Calendar.MONTH) + 1;

        int prev = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        calendar.add(Calendar.DAY_OF_MONTH, -prev);

        List<BeanDate> data = new ArrayList<>();
        for (int ver = 0; ver < 6; ver++) {
            BeanDate date = new BeanDate();
            for (int hor = 0; hor < 7; hor++) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                boolean isToday = (toYear == year && toMonth == month && day == toDay);
                boolean isMonth = (currentYear == year && currentMonth == month);
                BeanDate.DateItem item = new BeanDate.DateItem(year, month, day);
                item.setBelong(isMonth, isToday);
                item.setLine(ver, hor);
                date.mDates.add(item);

                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            data.add(date);
        }
        return data;
    }

    public static boolean matchLongTime(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        } else {
            String matcher = "1[0-9]{12,}";
            return phone.matches(matcher);
        }
    }

    public static boolean matchPassword(String pass) {
        if (TextUtils.isEmpty(pass)) {
            return false;
        } else {
            String matcher = "(?=.*[0-9].*)(?=.*[A-Za-z].*).{1,100}";
            return pass.matches(matcher);
        }
    }

    public static String getTextTimeFromLong(String longTime) {
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return mFormat.format(Long.parseLong(longTime));
    }

    public static String getTextTimeFromLong(long longTime) {
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return mFormat.format(longTime);
    }
}