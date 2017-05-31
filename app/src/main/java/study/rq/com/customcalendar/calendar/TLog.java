package study.rq.com.customcalendar.calendar;

import android.text.TextUtils;
import android.util.Log;


public class TLog {
    public static final String LOG_TAG = "YkwLog";
    public static boolean DEBUG = true;//是否处在debug

    public TLog() {
    }

    public static final void analytics(String log) {
        if (DEBUG)
            Log.d(LOG_TAG, log);
    }

    public static final void error(String log) {
        if (DEBUG)
            Log.e(LOG_TAG, "" + log);
    }

    public static final void error(String tag, String log) {
        if (DEBUG)
            Log.e(tag, "" + log);
    }

    public static final void bean(String tag, Object obj) {
//        bean(tag, new Gson().toJson(obj).trim());
    }

    private static final void bean(String tag, String log) {
        if (DEBUG) {
            Log.e(tag, "------------------------------" + tag + "------------------------------");
            Log.e(tag, "=====>" + log);
            String outPut = log.replaceAll(":\\{", ":,{").replaceAll(":\\[\\{", ":[,{")
                    .replaceAll("\\}", "},").replaceAll("\\]", "],").replaceAll("\\\\\"", "");
            TLog.error(tag, "=====>" + outPut);
            String[] outs = outPut.split(",");
            String SPACE = "";
            for (int i = 0; i < outs.length; i++) {
//                if (outs[i].endsWith("null")) {
//                    continue;
//                }
                if (outs[i].contains("{") || outs[i].contains("[")) {
                    SPACE = SPACE + "\t";
                } else if (outs[i].contains("}") || outs[i].contains("]")) {
                    if (!TextUtils.isEmpty(SPACE)) {
                        SPACE = SPACE.substring(0, SPACE.lastIndexOf("\t"));
                    }
                }
                TLog.error(tag, "    --" + i + "->" + SPACE + outs[i]);
            }
            Log.e(tag, "------------------------------" + tag + ".end------------------------------");
        }
    }

    public static final void tagLog(String tag, String log) {
        if (DEBUG) {
            Log.e(tag, "------------------------------" + tag + "------------------------------");
            Log.e(tag, log);
            Log.e(tag, "------------------------------" + tag + ".end--------------------------");
        }
    }

    public static final void log(String log) {
        if (DEBUG) {
            Log.e(LOG_TAG, log);
        }
    }

    public static final void log(String... log) {
        if (DEBUG) {
            StringBuilder sb = new StringBuilder();
            for (String logI : log) {
                sb = sb.append("     ").append(logI);
            }
            log(sb.toString());
        }
    }

    public static final void logI(String log) {
        if (DEBUG)
            Log.i(LOG_TAG, log);
    }

    public static final void warn(String log) {
        if (DEBUG)
            Log.w(LOG_TAG, log);
    }
}
