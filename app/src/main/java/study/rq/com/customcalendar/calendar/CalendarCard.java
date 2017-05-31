/*
 * Copyright (c) 2016.湖南六翼传媒
 */

package study.rq.com.customcalendar.calendar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.Calendar;
import java.util.Map;

import study.rq.com.customcalendar.R;
import study.rq.com.customcalendar.calendar.data.BGCircle;
import study.rq.com.customcalendar.calendar.data.BgCirlcePointEvaluator;
import study.rq.com.customcalendar.calendar.data.CalendarOneScreenDataMonth;
import study.rq.com.customcalendar.calendar.data.CalendarOneScreenDataWeek;
import study.rq.com.customcalendar.calendar.data.DateUtil;
import study.rq.com.customcalendar.calendar.data.DayData;

/**
 * @ClassName: CalendarCard
 * @Description: 日历的card 周视图和月视图两种模式
 */
public class CalendarCard extends View {

    private final String TAG = CalendarCard.class.getName();

    /**
     * 选中的sell
     */
    private Cell selectedSell;

    /**
     * 日历中 "日" 数字的大小(动态生成)
     */
    private float dayTextSize = 0;

    /**
     * 点击事件的阀值
     */
    private int touchSlop;

    /**
     * 该View所需要的数据集 月数据
     */
    public CalendarOneScreenDataMonth dataMonth;

    public CalendarOneScreenDataMonth getDataMonth() {
        return dataMonth;
    }

    /**
     * 该View所需要的数据集 周数据
     */
    public CalendarOneScreenDataWeek dataWeek;

    /**
     * 日历的高度
     */
    private int height = 0;

    /**
     * 日历的宽度
     */
    private int width = 0;

    /**
     * 每个表格的边长(表格为正方形)
     */
    private float cellWidth = 0;
    private float cellHeight = 0;

    float margin = 10;

    /**
     * 行数组
     */
    public Row[] rows;


    //******************* paint ***********************

    /**
     * "日" 黑 白 灰 画笔
     */
    private Paint paintDayText;


    /**
     * 活动事件画笔
     */
    private Paint paintActivityEventSign;

    /**
     * 赞助事件画笔
     */
    private Paint paintSupportEventSign;

    /**
     * "日" 选中的空心圆的画笔
     */
    private Paint paintDaySelected;


    /**
     * "日" 上方文字的画笔
     */
    private Paint paintTopText;

    /**
     * action_down:X轴的位置
     */
    private float mDownX;
    /**
     * action_down:Y轴的位置
     */
    private float mDownY;

    /**
     * cell的点击事件监听器
     */
    private OnCellClickListener onCellClickListener;

    /**
     * true 月视图模式
     * false 周视图模式
     */
    private boolean monthType = true;


//    BGCircle currentBgCircle;//当前的选中背景
//    BGCircle lastBgCircle;

    float cx = 0;
    float cy = 0;

    public float startCx = 0;
    public float startCy = 0;
    public float endCx = 0;
    public float endCy = 0;

    float bgCircleRadius = 0;


    public CalendarCard(Context context) {
        super(context);
    }

    public CalendarCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CalendarCard(Context context, CalendarOneScreenDataMonth dataMonth) {
        super(context);
        if (dataMonth == null) {
            throw new RuntimeException("参数异常,dataMonth为null");
        }
        init(dataMonth, null);
    }

    public void initMonthData(CalendarOneScreenDataMonth dataMonth) {
        if (dataMonth == null) {
            throw new RuntimeException("参数异常,dataMonth为null");
        }
        init(dataMonth, null);
    }

    public void initWeekData(CalendarOneScreenDataWeek dataWeek) {
        if (dataWeek == null) {
            throw new RuntimeException("参数异常,dataWeek为null");
        }
        init(null, dataWeek);
    }

    public CalendarCard(Context context, CalendarOneScreenDataWeek dataWeek) {
        super(context);
        if (dataWeek == null) {
            throw new RuntimeException("参数异常,dataWeek为null");
        }
        init(null, dataWeek);
    }


    private void init(CalendarOneScreenDataMonth dataMonth, CalendarOneScreenDataWeek dataWeek) {
        monthType = dataMonth != null;
        this.dataMonth = dataMonth;
        this.dataWeek = dataWeek;
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
//        setBackgroundColor(getResources().getColor(R.color.month_bg_color));
        initSomeSize();
        initCell();

    }

    private void initCell() {
        rows = new Row[monthType ? CalendarConstant.TOTAL_ROW_MONTH : CalendarConstant.TOTAL_ROW_WEEK];
        for (int j = 0; j < rows.length; j++) {
            Row mRow = new Row(j);
            rows[j] = mRow;
            for (int i = 0; i < Calendar.SATURDAY; i++) {
                Cell mCell = new Cell(i + 1, j);
                rows[j].cells[i] = mCell;
            }
        }
    }

    public String getCenter() {
        return getFromRow(rows);
    }

    private String getFromRow(Row[] rows) {
        try {
            Cell[] cell = rows[rows.length / 2].cells.clone();
            return getDataMonth().getMapDay().get(getPosition(cell[cell.length / 2].j, cell[cell.length / 2].week)).getTopType() + "";
        } catch (Exception e) {
            return "error";
        }
    }

    private String mDefaultGrayColor = "#6C6A6A";
    private String mDefaultWhiteColor = "#ffffff";// TODO: 2017/5/31

    private void initPaint() {

        paintDayText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintDayText.setColor(Color.parseColor(mDefaultWhiteColor));
//        paintDayText.setTypeface(BaseApplication.getCalendarFontTypeface());
        paintDayText.setTextSize(dayTextSize);
        String defaultWhiteColor = "#ffffff00";
        paintTopText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTopText.setColor(Color.parseColor(defaultWhiteColor));
        paintTopText.setTextSize(cellWidth * 0.2f);

        paintActivityEventSign = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintActivityEventSign.setStyle(Paint.Style.FILL);
        paintActivityEventSign.setColor(getResources().getColor(R.color.colorPrimary));
        String red = "#ffffff00";
        paintSupportEventSign = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintSupportEventSign.setStyle(Paint.Style.FILL);
        paintSupportEventSign.setColor(Color.parseColor(red));

        paintDaySelected = new Paint(Paint.ANTI_ALIAS_FLAG);

        paintDaySelected.setStyle(Paint.Style.FILL);
        paintDaySelected.setStrokeWidth(4);
        paintDaySelected.setShadowLayer(5, 0, 5, Color.parseColor("#000000"));
        paintDaySelected.setColor(getResources().getColor(R.color.colorPrimary));
        bgCircleRadius = (1 / 2f - CalendarConstant.BACKGROUND_DELTA0) * cellWidth;

    }

    /**
     * 初始化一些尺寸
     */
    private void initSomeSize() {
        width = getResources().getDisplayMetrics().widthPixels;
        int row = monthType ? CalendarConstant.TOTAL_ROW_MONTH : CalendarConstant.TOTAL_ROW_WEEK;
        height = (width / CalendarConstant.TOTAL_COL + 1) * row;
        initSomeSize0(height, width);
    }


    private void initSomeSize0(int height, int width) {
        Log.e("test", "height-->>" + height + ", width-->>" + width);
        float t = monthType ? CalendarConstant.TOTAL_ROW_MONTH : 1;
        // TODO: 2017/5/31  
        cellWidth = height / t - getResources().getDimensionPixelSize(R.dimen.w3);
        cellHeight = height / t - getResources().getDimensionPixelSize(R.dimen.w15);
        dayTextSize = cellWidth * 0.3f;
        initPaint();
    }

    boolean isAnimationing = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int j = 0; j < rows.length; j++) {
            if (rows[j] != null) {
                rows[j].drawCells(canvas);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float disX = event.getX() - mDownX;
                float disY = event.getY() - mDownY;
                if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) {
                    int col = (int) (mDownX / cellWidth);
                    int row = (int) (mDownY / cellHeight);
                    measureClickCell(col, row);
                }
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 计算点击的单元格
     *
     * @param col
     * @param row
     */
    private void measureClickCell(int col, int row) {
        if (col >= CalendarConstant.TOTAL_COL || row >= CalendarConstant.TOTAL_ROW_MONTH)
            return;
        if (!monthType) {
            row = 0;
        }
        int week = col + 1;
        selectedSell = new Cell(
                week, row);
        rows[row].cells[col] = selectedSell;
        DayData dayData;
        int index;
        if (monthType) {
            Map<Integer, DayData> map = dataMonth.getMapDay();
            index = getPosition(row, week);
            dayData = map.get(index);
        } else {
            Map<Integer, DayData> map = dataWeek.getMapDay();
            index = getPosition(row, week);
            dayData = map.get(index);
        }
        if (endCx > 0) {
            startCx = endCx;
            startCy = endCy;
        }
        endCx = (week - 1 + 1 / 2f) * cellWidth;
        endCy = (0 + 1 / 2f) * cellHeight;

        TLog.error(TAG, "点击的单元格为-->>" + dayData.toString() + ", index-->>" + index + ", row-->>" + row);
//        if (dayData.isClickable()) {
//
//        }
        selectedSell.startCircleAnimation();
        // 刷新界面
        update(row, week, dayData);
        if (onCellClickListener != null) {

            onCellClickListener.clickDate(dayData, index, row, col);
        }
    }

    public void updatePoint(int col, int row) {
        if (null == rows) {
            TLog.error("rows null,col=" + col + "-row:" + row);
            return;

        }
        if (col >= CalendarConstant.TOTAL_COL || row >= CalendarConstant.TOTAL_ROW_MONTH)
            return;
        if (!monthType) {
            row = 0;
        }
        int week = col + 1;
        selectedSell = new Cell(
                week, row);
        rows[row].cells[col] = selectedSell;

        DayData dayData;
        int index;
        if (monthType) {
            Map<Integer, DayData> map = dataMonth.getMapDay();
            index = getPosition(row, week);
            dayData = map.get(index);
        } else {
            Map<Integer, DayData> map = dataWeek.getMapDay();
            index = getPosition(row, week);
            dayData = map.get(index);
        }
        if (endCx > 0) {
            startCx = endCx;
            startCy = endCy;
        }
        endCx = (week - 1 + 1 / 2f) * cellWidth;
        endCy = (selectedSell.j + 1 / 2f) * cellHeight;

        selectedSell.startCircleAnimation();
        // 刷新界面
        update(row, week, dayData);
    }

    /**
     * @description 下一天动画
     * @author qicheng.qing
     * @create 17/5/20 16:55
     */
    public void nextDayAnimation(int col) {
        int week = col + 1;
        selectedSell = new Cell(
                week, 0);
        rows[0].cells[col] = selectedSell;

        DayData dayData;
        int index;
        if (monthType) {
            Map<Integer, DayData> map = dataMonth.getMapDay();
            index = getPosition(0, week);
            dayData = map.get(index);
        } else {
            Map<Integer, DayData> map = dataWeek.getMapDay();
            index = getPosition(0, week);
            dayData = map.get(index);
        }
        if (endCx > 0) {
            startCx = endCx;
            startCy = endCy;
        }
        endCx = (week - 1 + 1 / 2f) * cellWidth;
        endCy = (selectedSell.j + 1 / 2f) * cellHeight;

        selectedSell.startCircleAnimation();
        // 刷新界面
        update(0, week, dayData);
        TLog.error("loadMoreAnimation startCx:" + startCx);
        TLog.error("loadMoreAnimation endCx:" + endCx);

    }

    /**
     * 更新界面
     */
    private void update(int row, int week, DayData dayData) {
        int position = getPosition(row, week);

        TLog.error(TAG, "position:" + position);

        if (monthType) {
            //更新月视图
            updateMonth(position);
            TLog.error(TAG, "更新月视图");
        } else {
            //更新周视图
            updateWeek(week);
            TLog.error(TAG, "更新周视图");
        }
        TLog.error("CalendarCard", "dayData = " + dayData.getTopType());

    }

    private void updateMonth(int position) {
        dataMonth.setSelectedPosition(position);
        invalidate();
    }

    private void updateWeek(int position) {
        dataWeek.setSelectedPosition(position);
        invalidate();
    }

    public interface OnCellClickListener {
        /**
         * @param data  被选中的 CustomDate数据
         * @param index
         */
        void clickDate(DayData data, int index, int row, int col);

    }

    /**
     * 绘制行
     */
    private class Row {
        /**
         * j: 行
         **/
        public int j;

        public Cell[] cells;

        Row(int j) {
            this.j = j;
            cells = new Cell[CalendarConstant.TOTAL_COL];
        }

        // 绘制单元格
        public void drawCells(Canvas canvas) {
            for (int i = 0; i < Calendar.SATURDAY; i++) {
                if (cells[i] != null) {
                    cells[i].drawSelf(canvas);
                }
            }
        }

    }

    /**
     * 绘制每个单元格
     */
    private class Cell {

        /**
         * week: 星期
         **/
        public int week;

        /**
         * j: 行
         **/
        public int j;

        public Cell(int week, int j) {
            this.week = week;
            this.j = j;
        }

        /**
         * 绘制顺序
         * 1.cell的行经期和易孕期的色块儿背景
         * 2.排卵日色块儿
         * 3."日"上方文字
         * 4."日"下方圆点
         * 5.选中天的圆圈
         * 6."日"字符
         *
         * @param canvas canvas
         */
        public void drawSelf(Canvas canvas) {

            DayData dayData;
            if (monthType) {
                dayData = dataMonth.getMapDay().get(getPosition(j, week));
            } else {
                dayData = dataWeek.getMapDay().get(getPosition(j, week));

            }
            //********3."日"上方文字
//            drawTopText(canvas, dayData);
            //********4."日"下方圆点
            drawBottomCircle(canvas, dayData);

            //********5.选中天的圆圈
            drawSelectedCircle(canvas);

            //********6."日"字符
            drawText(canvas, dayData);


        }

        private void startCircleAnimation() {
            final ValueAnimator anim = ValueAnimator.ofObject(new BgCirlcePointEvaluator(), new BGCircle(startCx, startCy, bgCircleRadius), new BGCircle(endCx, endCy, bgCircleRadius));
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    cx = ((BGCircle) animation.getAnimatedValue()).getX();
                    cy = ((BGCircle) animation.getAnimatedValue()).getY();
                    invalidate();
                }
            });
            anim.setDuration(300);
            anim.start();
        }

        /**
         * 绘制选中的空心圆环
         *
         * @param canvas
         */
        private void drawSelectedCircle(Canvas canvas) {
            int currentKey = getPosition(j, week);
            int selectedPosition = monthType ? dataMonth.getSelectedPosition() : dataWeek.getSelectedPosition();
            if (currentKey == selectedPosition) {
                TLog.error(TAG, "drawSelectedCircle:" + currentKey);
                if (!monthType) {//周日历才显示动画
                    if (cx == 0 && cy == 0) {
                        cx = (week - 1 + 1 / 2f) * cellWidth;
                        cy = (j + 1 / 2f) * cellHeight;
                    }
                    canvas.drawCircle(cx, cy, bgCircleRadius, paintDaySelected);
                } else {
                    canvas.drawCircle((week - 1 + 1 / 2f) * cellWidth, (j + 1 / 2f) * cellHeight, bgCircleRadius, paintDaySelected);
                }


            }
        }


        /**
         * 绘制下方事件标记的圆点
         *
         * @param canvas
         * @param dayData
         */
        private void drawBottomCircle(Canvas canvas, DayData dayData) {
            int currentKey = getPosition(j, week);
            int selectedPosition = monthType ? dataMonth.getSelectedPosition() : dataWeek.getSelectedPosition();
            if (selectedPosition != currentKey) {//当前的日期不显点

                if (dayData.isHasSupport() && dayData.isHasActivity()) {
                    canvas.drawCircle((week - 1 + 0.4f) * cellWidth, (j + 1 - CalendarConstant.BACKGROUND_DELTA0 / 2) * cellHeight - margin, CalendarConstant.EVENT_SIGN_RADIUS, paintActivityEventSign);
                    canvas.drawCircle((week - 1 + 0.6f) * cellWidth, (j + 1 - CalendarConstant.BACKGROUND_DELTA0 / 2) * cellHeight - margin, CalendarConstant.EVENT_SIGN_RADIUS, paintSupportEventSign);
                }
                if (dayData.isHasActivity() && dayData.isHasSupport() == false) {//该日是否有活动
                    canvas.drawCircle((week - 1 + 0.5f) * cellWidth, (j + 1 - CalendarConstant.BACKGROUND_DELTA0 / 2) * cellHeight - margin, CalendarConstant.EVENT_SIGN_RADIUS, paintActivityEventSign);
                }
                if (dayData.isHasSupport() && dayData.isHasActivity() == false) {//该日是否有赞助
                    canvas.drawCircle((week - 1 + 0.5f) * cellWidth, (j + 1 - CalendarConstant.BACKGROUND_DELTA0 / 2) * cellHeight - margin, CalendarConstant.EVENT_SIGN_RADIUS, paintSupportEventSign);
                }
            }


        }

        /**
         * 绘制上方的字符
         *
         * @param canvas
         * @param dayData
         */
        private void drawTopText(Canvas canvas, DayData dayData) {
            //绘制上方的字符
            long unix = dayData.getUnix();
            int topType = dayData.getTopType();
            String content;
            float textWidth;
            float textX;
            float deltaBaseLine = 0.24f;
            switch (topType) {
                case CalendarConstant.TOP_TYPE_TODAY:
                    content = getResources().getString(R.string.today);
                    textWidth = paintTopText.measureText(content);
                    textX = (week - 1 + 0.5f) * cellWidth - textWidth / 2f;
                    canvas.drawText(content, textX, (j + deltaBaseLine) * cellHeight - paintTopText.measureText(
                            content, 0, 1) / 2, paintTopText);
                    break;
                case CalendarConstant.TOP_TYPE_FIRST_DAY:
                    content = DateUtil.unix2Month(unix, getContext());
                    textWidth = paintTopText.measureText(content);
                    textX = (week - 1 + 0.5f) * cellWidth - textWidth / 2f;
                    canvas.drawText(content, textX, (j + deltaBaseLine) * cellHeight - paintTopText.measureText(
                            content, 0, 1) / 2, paintTopText);
                    break;

                default:
                    //don't forget default
                    break;
            }

        }


        private void drawText(Canvas canvas, DayData dayData) {
            if (!dayData.isClickable()) {
                paintDayText.setColor(Color.parseColor(mDefaultGrayColor));
            } else {
                paintDayText.setColor(Color.parseColor(mDefaultWhiteColor));
            }

            int currentKey = getPosition(j, week);
            int selectedPosition = monthType ? dataMonth.getSelectedPosition() : dataWeek.getSelectedPosition();
            if (currentKey == selectedPosition) {
                paintDayText.setColor(Color.parseColor(mDefaultWhiteColor));
            }

            int day = dayData.getDay();
            String content = String.valueOf(day);
            TLog.error("Cell", "drawText.content = " + content);
            float textWidth = paintDayText.measureText(content);
            float textX = (week - 1 + 0.5f) * cellWidth - textWidth / 2f;

            canvas.drawText(content, textX,
                    (j + 0.7f) * cellHeight - paintDayText.measureText(
                            content, 0, 1) / 2, paintDayText);
        }
    }


    public OnCellClickListener getOnCellClickListener() {
        return onCellClickListener;
    }

    public void setOnCellClickListener(OnCellClickListener onCellClickListener) {
        this.onCellClickListener = onCellClickListener;
    }


    private int getPosition(int row, int week) {
        return monthType ? CalendarConstant.CELL_ARRAY_MONTH[row][week] : CalendarConstant.CELL_ARRAY_WEEK[row][week];

    }

}
