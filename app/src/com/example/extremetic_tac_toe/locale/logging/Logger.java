package com.example.extremetic_tac_toe.locale.logging;

import java.util.Calendar;

public final class Logger {

    public static final short DEBUG = 2;
    public static final short ERR = 4;
    public static final short FATAL_ERR = 5;
    public static final short INFO = 1;
    public static final short TEXT = 0;
    public static final short WARN = 3;

    public static void post(final short messageLevel, final String message, final String sourceClassName) {
	final Calendar c = Calendar.getInstance();
	final int milisecond = c.get(Calendar.MILLISECOND);
	final int second = c.get(Calendar.SECOND);
	final int minute = c.get(Calendar.MINUTE);
	final int hour = c.get(Calendar.HOUR_OF_DAY);
	final int day = c.get(Calendar.DAY_OF_MONTH);
	final int year = c.get(Calendar.YEAR);
	final int month = c.get(Calendar.MONTH);
	String ret = "[";
	ret += Integer.toString(year) + "-";
	ret += Integer.toString(day) + "-";
	ret += Integer.toString(month) + " ";
	ret += Integer.toString(hour) + ":";
	ret += Integer.toString(minute) + ":";
	ret += Integer.toString(second) + ":";
	ret += Integer.toString(milisecond) + " - " + sourceClassName + "]";
	switch (messageLevel) {
	case TEXT:
	    ret += message;
	    System.out.println(ret);
	    break;
	case INFO:
	    ret += "[INFO]" + message;
	    System.out.println(ret);
	    break;
	case DEBUG:
	    ret += "[DEBUG]" + message;
	    System.out.println(ret);
	    break;
	case WARN:
	    ret += "[!WARNNING]" + message;
	    System.out.println(ret);
	    break;
	case ERR:
	    ret += "[!!ERROR]" + message;
	    System.err.println(ret);
	    break;
	case FATAL_ERR:
	    ret += "[!!FATAL ERROR!!]" + message;
	    System.err.println(ret);
	    break;
	default:
	    ret += message;
	    System.out.println(ret);
	    break;
	}
    }

    public static void post(final String message) {
	Logger.post(Logger.TEXT, message, "Logger");
    }

    public static void post(final String message, final String sourceClassName) {
	Logger.post(Logger.TEXT, message, sourceClassName);
    }

    public static <T extends Object> void postClass(final short messageLevel, final String message,
	    final T sourceClass) {
	Logger.post(messageLevel, message, sourceClass.getClass().getName());
    }

}
