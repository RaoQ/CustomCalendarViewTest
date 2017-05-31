package study.rq.com.customcalendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import study.rq.com.customcalendar.calendar.CalendarCard;
import study.rq.com.customcalendar.calendar.data.CalendarOneScreenDataMonth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalendarOneScreenDataMonth dataPre = DateCalculatorUtils.getMonthDate(2015,1, 2);
        CalendarCard calendarCard = (CalendarCard) findViewById(R.id.calendar_view);
        calendarCard.initMonthData(dataPre);
    }
}
