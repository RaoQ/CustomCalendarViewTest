package study.rq.com.customcalendar.oldcalendar;

import java.util.ArrayList;

/**
 * Created by Cao-Human on 2016/4/19
 */
public class BeanDate {
    public ArrayList<DateItem> mDates = new ArrayList<>();

    public static class DateItem {
        public DateItem(int arg1, int arg2, int arg3) {
            year = arg1;
            month = arg2;
            day = arg3;
        }

        public void setBelong(boolean arg1, boolean arg2) {
            current = arg1;
            today = arg2;
        }

        public int year, month, day;
        public boolean current, today;

        public int line, column;

        public void setLine(int i, int c) {
            line = i;
            column = c;
        }
    }

}
