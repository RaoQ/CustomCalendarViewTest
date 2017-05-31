package study.rq.com.customcalendar.calendar.data;

import android.animation.TypeEvaluator;

/**
 * @author qicheng.qing
 * @description 用于计算选中背景的位置
 * @create 17/5/17,11:33
 */
public class BgCirlcePointEvaluator implements TypeEvaluator {

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        BGCircle startPoint = (BGCircle) startValue;
        BGCircle endPoint = (BGCircle) endValue;
        float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
        float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());
        BGCircle point = new BGCircle(x, y);
        return point;
    }

}
