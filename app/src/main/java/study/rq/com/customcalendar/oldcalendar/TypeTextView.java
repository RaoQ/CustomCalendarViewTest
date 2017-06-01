package study.rq.com.customcalendar.oldcalendar;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Cao-Human on 2016/4/7
 */
public class TypeTextView extends TextView {
    public TypeTextView(Context context) {
        this(context, null);
    }

    public TypeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TypeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "font/abcdef.ttf");
        setTypeface(face);
    }
}
