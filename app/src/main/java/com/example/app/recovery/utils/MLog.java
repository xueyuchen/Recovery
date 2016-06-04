package com.example.app.recovery.utils;

import com.example.app.recovery.tools.ApiConstants;
import android.util.Log;

/**
 * custom log
 * 
 * @author
 * 
 */
public class MLog {
	public static void d(String tag, String message) {
		if (ApiConstants.DEBUG) {
			if (message == null) {
				message = "message is null";
			}
			Log.d(tag, message);
		}
	}

	public static void i(String tag, String message) {
		if (ApiConstants.DEBUG) {
			if (message == null) {
				message = "message is null";
			}
			Log.i(tag, message);
		}
	}

	public static void w(String tag, String message) {
		if (ApiConstants.DEBUG) {
			if (message == null) {
				message = "message is null";
			}
			Log.w(tag, message);
		}
	}

	public static void e(String tag, String message) {
		if (ApiConstants.DEBUG) {
			if (message == null) {
				message = "message is null";
			}
			Log.e(tag, message);
		}
	}

	public static void v(String tag, String message) {
		if (ApiConstants.DEBUG) {
			if (message == null) {
				message = "message is null";
			}
			Log.v(tag, message);
		}
	}
}
