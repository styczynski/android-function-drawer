package com.example.extremetic_tac_toe.locale.logging;

import java.util.Calendar;

public final class Logger {
	
	public static final short TEXT = 0;
	public static final short INFO = 1;
	public static final short DEBUG = 2;
	public static final short WARN = 3;
	public static final short ERR = 4;
	public static final short FATAL_ERR = 5;
	
	public static <T extends Object> void postClass(short messageLevel, String message, T sourceClass) {
		post(messageLevel, message, sourceClass.getClass().getName());
	}
	
	public static void post(String message, String sourceClassName) {
		post(TEXT, message, sourceClassName);
	}
	
	public static void post(String message) {
		post(TEXT, message, "Logger");
	}
	
	public static void post(short messageLevel, String message, String sourceClassName) {
		Calendar c = Calendar.getInstance();
		int milisecond = c.get(Calendar.MILLISECOND);
		int second = c.get(Calendar.SECOND);
		int minute = c.get(Calendar.MINUTE);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		String ret = "[";
		ret+=Integer.toString(year)+"-";
		ret+=Integer.toString(day)+"-";
		ret+=Integer.toString(month)+" ";
		ret+=Integer.toString(hour)+":";
		ret+=Integer.toString(minute)+":";
		ret+=Integer.toString(second)+":";
		ret+=Integer.toString(milisecond)+" - "+sourceClassName+"]";
		switch(messageLevel) {
			case TEXT:
				ret+=message;
				System.out.println(ret);
				break;
			case INFO:
				ret+="[INFO]"+message;
				System.out.println(ret);
				break;
			case DEBUG:
				ret+="[DEBUG]"+message;
				System.out.println(ret);
				break;
			case WARN:
				ret+="[!WARNNING]"+message;
				System.out.println(ret);
				break;
			case ERR:
				ret+="[!!ERROR]"+message;
				System.err.println(ret);
				break;
			case FATAL_ERR:
				ret+="[!!FATAL ERROR!!]"+message;
				System.err.println(ret);
				break;
			default:
				ret+=message;
				System.out.println(ret);
				break;
		}
	}
	
}
