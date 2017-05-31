/*
 * Copyright (c) 2016.湖南六翼传媒
 */

package study.rq.com.customcalendar.calendar;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;

/**
 * Created by damon on 1/8/16.
 */
public class CalendarConstant {

    /**
     * 日历(月视图)的开始时间点: 2010/01/01 00:00    ==1262275200
     */
    public static final int CALENDAR_START_MONTH = 1262275200;

    /**
     * 日历(周视图)的开始时间点: 2009/12/27 00:00    ==1261843200
     */
    public static final int CALENDAR_START_WEEK = 1261843200;

    //测试 2015/10/01 00:00
//    public static final int CALENDAR_START = 1443628800;

    /**
     * TOTAL_COL: 7列
     **/
    public static final int TOTAL_COL = Calendar.SATURDAY;

    /**
     * TOTAL_ROW_MONTH: 6行
     **/
    public static final int TOTAL_ROW_MONTH = 5;
    /**
     * 周视图行数
     */
    public static final int TOTAL_ROW_WEEK = 1;


    /**
     * "日"上方文字 "今天"
     */
    public static final int TOP_TYPE_TODAY = 0;
    /**
     * "日"上方文字 月份 比如:"3月"
     */
    public static final int TOP_TYPE_FIRST_DAY = 1;
    /**
     * "日"上方文字 没有
     */
    public static final int TOP_TYPE_NONE = -1;

    @IntDef({TOP_TYPE_TODAY, TOP_TYPE_FIRST_DAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TopType {
    }


    /**
     * 月份枚举
     */
    @IntDef({Calendar.JANUARY, Calendar.FEBRUARY, Calendar.MARCH, Calendar.APRIL, Calendar.MAY, Calendar.JUNE, Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER, Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Month {
    }

    /**
     * 星期枚举
     */
    @IntDef({Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Week {
    }

    /**
     * cell的背景颜色的top和Bottom的偏移
     */
    public static final float BACKGROUND_DELTA0 = 0.2F;
    /**
     * cell 绘制圆弧相对cell背景颜色圆心的偏移
     */
    public static final float BACKGROUND_DELTA1 = 0.1F;


    /**
     * 圆弧sweep的度数
     */
    public static final float SWEEP_ANGLE = 180F;
    /**
     * 绘制事件的圆点
     */
    public static final float EVENT_SIGN_RADIUS = 8F;

    /**
     * 该数组正好和日历中的显示一样的
     * 注意: 列对应的Calendar.SUNDAY--Calendar.SATURDAY(第一列没有用)
     */
    public final static int[][] CELL_ARRAY_MONTH = new int[][]{
            {0, 1, 2, 3, 4, 5, 6, 7},
            {0, 8, 9, 10, 11, 12, 13, 14},
            {0, 15, 16, 17, 18, 19, 20, 21},
            {0, 22, 23, 24, 25, 26, 27, 28},
            {0, 29, 30, 31, 32, 33, 34, 35},
            {0, 36, 37, 38, 39, 40, 41, 42}
    };
    /**
     * 该数组正好和日历中的显示一样的
     * 注意: 列对应的Calendar.SUNDAY--Calendar.SATURDAY(第一列没有用)
     */
    public final static int[][] CELL_ARRAY_WEEK = new int[][]{
            {0, 1, 2, 3, 4, 5, 6, 7}
    };

    /**
     * 通过位置获得行数
     *
     * @param position 位置 比如:28;
     * @return
     */
    public static int getRowFromPosition(int position, boolean isMonth) {
        int row = 0;
        if (isMonth) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 8; j++) {
                    if (CELL_ARRAY_MONTH[i][j] == position) {
                        return i;
                    }
                }
            }
        } else {
            return 0;
        }
        return row;
    }
}
