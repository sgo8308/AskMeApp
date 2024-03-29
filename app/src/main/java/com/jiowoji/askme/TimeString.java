package com.jiowoji.askme;

public class TimeString {
    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;

    public static String formatTimeString(long regTime) {
        long curTime = System.currentTimeMillis();
        long diffTime = (curTime - regTime) / 1000;
        String msg = null;

        if (diffTime < TimeString.SEC) {
            msg = "방금 전";
        } else if ((diffTime /= TimeString.SEC) < TimeString.MIN) {
            msg = diffTime + "분전";
        } else if ((diffTime /= TimeString.MIN) < TimeString.HOUR) {
            msg = (diffTime) + "시간전";
        } else if ((diffTime /= TimeString.HOUR) < TimeString.DAY) {
            msg = (diffTime) + "일전";
        } else if ((diffTime /= TimeString.DAY) < TimeString.MONTH) {
            msg = (diffTime) + "달전";
        } else {
            msg = (diffTime) + "년전";
        }
        return msg;
    }
}