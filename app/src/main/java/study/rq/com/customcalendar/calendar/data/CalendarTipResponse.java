package study.rq.com.customcalendar.calendar.data;

import java.util.List;

/**
 * @author qicheng.qing
 * @description
 * @create 16/12/29,11:18
 */
public class CalendarTipResponse {

    /**
     * message :
     * statusCode : 10000
     * success : true
     * data : [{"d":31,"len":0,"m":12,"type":"0"},{"d":9,"len":0,"m":12,"type":"0"},{"d":10,"len":2,"m":12,"type":"0"},{"d":1,"len":29,"m":12,"type":"0"},{"d":3,"len":0,"m":12,"type":"0"},{"d":3,"len":1,"m":12,"type":"0"},{"d":2,"len":0,"m":12,"type":"0"},{"d":10,"len":0,"m":12,"type":"0"},{"d":5,"len":0,"m":12,"type":"0"},{"d":10,"len":1,"m":12,"type":"0"},{"d":10,"len":5,"m":12,"type":"0"},{"d":9,"len":1,"m":12,"type":"0"},{"d":15,"len":1,"m":12,"type":"0"},{"d":19,"len":1,"m":12,"type":"0"},{"d":22,"len":4,"m":12,"type":"0"},{"d":9,"len":1,"m":12,"type":"1"},{"d":9,"len":6,"m":12,"type":"1"},{"d":22,"len":3,"m":12,"type":"1"},{"d":23,"len":4,"m":12,"type":"1"},{"d":23,"len":"0","m":"12","type":"1"},{"d":27,"len":"0","m":"12","type":"1"},{"d":10,"len":"0","m":"12","type":"1"},{"d":26,"len":"0","m":"12","type":"1"},{"d":11,"len":"0","m":"12","type":"1"},{"d":25,"len":"0","m":"12","type":"1"},{"d":24,"len":"0","m":"12","type":"1"},{"d":14,"len":"0","m":"12","type":"1"},{"d":15,"len":"0","m":"12","type":"1"},{"d":12,"len":"0","m":"12","type":"1"},{"d":13,"len":"0","m":"12","type":"1"}]
     */

    private String message;
    private int statusCode;
    private boolean success;
    /**
     * d : 31
     * len : 0
     * m : 12
     * type : 0
     */

    private List<DataEntity> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {
        private int d;
        private int len;
        private int m;
        private String type;

        public int getD() {
            return d;
        }

        public void setD(int d) {
            this.d = d;
        }

        public int getLen() {
            return len;
        }

        public void setLen(int len) {
            this.len = len;
        }

        public int getM() {
            return m;
        }

        public void setM(int m) {
            this.m = m;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
