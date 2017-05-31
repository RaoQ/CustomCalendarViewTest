package study.rq.com.customcalendar.calendar.data;

/**
 * @author qicheng.qing
 * @description
 * @create 17/5/17,11:35
 */
public class BGCircle {
    private float x, y,radius;

    public BGCircle(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius=radius;
    }
    public BGCircle(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "BGCircle{" +
                "x=" + x +
                ", y=" + y +
                ", radius=" + radius +
                '}';
    }
}
