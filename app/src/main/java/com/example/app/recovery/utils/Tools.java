package com.example.app.recovery.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Tools {
    public static boolean isEmpty(String s){
        if(TextUtils.isEmpty(s))return true;
        s=replaceBlank(s);
        return TextUtils.isEmpty(s);
    }
    
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
    public static void openGooglePlayWithBrowser(Context context,String packageName){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+packageName));
        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(browserIntent);
    }
    
    public static void openGooglePlayWithApp(Context context,String packageName){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + packageName));
        context.startActivity(intent);
    }
    
    public static void openGooglePlay(Context context,String packageName){
        if(isAppInstalled(context, "com.android.vending")){
            openGooglePlayWithApp(context, packageName);
        }else{
            openGooglePlayWithBrowser(context, packageName);
        }
    }
    
    public static boolean isAppInstalled(Context context,String packageName){
        PackageInfo packageInfo=null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo!=null;   
    }
    
    public static String getAppVersionName(Context context){
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static int getAppVersionCode(Context context){
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    @SuppressWarnings("deprecation")
    public static void setViewSameHeightScale(Activity activity,int scaleScreenHeight,Bitmap bitmap,View view){
        setViewSameHeightScale(activity, scaleScreenHeight, bitmap.getWidth(),bitmap.getHeight(), view);
        view.setBackgroundDrawable(new BitmapDrawable(bitmap));
    }
    
    public static void setViewSameHeightScale(Activity activity,int scaleScreenHeight,int width,int height,View view){
        int screenHeight=getDM(activity).heightPixels;
        float scale=screenHeight/(float)scaleScreenHeight;
        int viewHeight=(int) (height*scale);
        int viewWidth=(int) (width*scale);
       
        LayoutParams params=view.getLayoutParams();
        if(viewWidth!=0)
            params.width=viewWidth;
        params.height=viewHeight;
//        MLog.i("scale", "width="+viewWidth+" height="+viewHeight);
        view.setLayoutParams(params);
    }
    
	/**
	 * set the time of animation while switch view.
	 * @param pager viewpager to set
	 * @param timemills time of animation in milliseconds
	 */
//	public static void setViewPagerFlipSpeed(ViewPager pager){
//		if (pager != null) {
//			 Interpolator sInterpolator = new DecelerateInterpolator();
//			 try {
//		            Field mScroller;
//		            mScroller = ViewPager.class.getDeclaredField("mScroller");
//		            mScroller.setAccessible(true);
//		            FixedSpeedScroller scroller = new FixedSpeedScroller(pager.getContext(), sInterpolator);
//		            scroller.setDuration(App.VIEWPAGER_FLIP_SPEED);
//		            mScroller.set(pager, scroller);
//		        } catch (NoSuchFieldException e) {
//		        } catch (IllegalArgumentException e) {
//		        } catch (IllegalAccessException e) {
//		        }
//		}
//	}
	
	/**
	 * @param text text of the textview
	 * @param textView the textview to show text.
	 * @param indexs start and end indexs.
	 * @param listeners OnClickListener of the special texts.
	 * @param linkColor the color of the special texts.
	 * @param withUnderLine if the underline show.
	 */
//	public static void setTextViewWithLink(String text,TextView textView,ArrayList<TextIndex> indexs,ArrayList<OnClickListener> listeners,int linkColor,boolean withUnderLine){
//	    setTextViewWithLink(text, textView, indexs, listeners, linkColor, withUnderLine,0,false);
//	}
//
//	public static void setTextViewWithLink(String text,TextView textView,ArrayList<TextIndex> indexs,ArrayList<OnClickListener> listeners,int linkColor,boolean withUnderLine,float addSize,boolean bold){
//        textView.setMovementMethod(LinkMovementMethod.getInstance());
//        textView.setHighlightColor(Color.TRANSPARENT);
//
//        SpannableString spanableInfo = new SpannableString(text);
//        for(int i=0;i<indexs.size();i++){
//            TextIndex index=indexs.get(i);
//            OnClickListener ls=null;
//            if(listeners!=null)
//                ls=listeners.get(i);
//            spanableInfo.setSpan(new Clickable(ls,linkColor,withUnderLine,addSize,bold), index.getStart(), index.getEnd(),
//                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//        textView.setText(spanableInfo);
//    }
	
	
	/**
	 * @param text text of the textview
	 * @param textView the textview to show text.
	 * @param index start and end index.
	 * @param listener OnClickListener of the special text.
	 * @param linkColor the color of the special text.
	 * @param withUnderLine if the underline show.
	 */
//	public static void setTextViewWithLink(String text,TextView textView,TextIndex index,OnClickListener listener,int linkColor,boolean withUnderLine){
//	    setTextViewWithLink(text, textView, index, listener, linkColor, withUnderLine,0,false);
//	}
//
//	public static void setTextViewWithLink(String text,TextView textView,TextIndex index,OnClickListener listener,int linkColor,boolean withUnderLine,float addSize,boolean bold){
//        textView.setMovementMethod(LinkMovementMethod.getInstance());
//        textView.setHighlightColor(Color.TRANSPARENT);
//
//        SpannableString spanableInfo = new SpannableString(text);
//        spanableInfo.setSpan(new Clickable(listener,linkColor,withUnderLine,addSize,bold), index.getStart(), index.getEnd(),
//                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        textView.setText(spanableInfo);
//    }

//	public static void restartApp(Context context){
//		Intent intent=new Intent(context,RebootService.class);
//		context.startService(intent);
//        System.exit(0);
//	}
	
	public static String formatPrice(long price) {
	    return "¥"+formatNumber(String.valueOf(price));
	}
	
	public static String formatPoint(long point) {
        return formatNumber(String.valueOf(point))+"pt";
    }
	
	public static String formatNumber(long price) {
	       return formatNumber(String.valueOf(price));
	}
	public static String formatNumber(String price) {
		price = new StringBuilder(price).reverse().toString(); 
		String str2 = "";
		for (int i = 0; i < price.length(); i++) {
			if (i * 3 + 3 > price.length()) {
				str2 += price.substring(i * 3, price.length());
				break;
			}
			str2 += price.substring(i * 3, i * 3 + 3) + ",";
		}
		if (str2.endsWith(",")) {
			str2 = str2.substring(0, str2.length() - 1);
		}
		return new StringBuilder(str2).reverse().toString();
	}

	/**
	 * encode the url with utf8.
	 * 
	 * @param str
	 * @return
	 */
	public static String urlEncode(String str) {
		try {
			return URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("failed to encode", e);
		}
	}

	/**
	 * decode the url with utf8
	 * 
	 * @param str
	 * @return
	 */
	public static String urlDecode(String str) {
		try {
			return URLDecoder.decode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("failed to decode", e);
		}
	}

	public static final String SHA1 = "SHA-1";
	public static final String MD5 = "MD5";

	/**
	 * @param str
	 * @return
	 */
	public static String getMD5(String str) {
		try {
			return getHash(str, MD5);
		} catch (Exception e) {
			return null;
		}
	}

	public static String getSHA1(String str) {
		try {
			return getHash(str, SHA1);
		} catch (Exception e) {
			return null;
		}
	}

	public static String getHash(String str, String algorithm)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		byte[] bytes = str.getBytes("utf-8");
		MessageDigest md = MessageDigest.getInstance(algorithm);
		byte[] digest = md.digest(bytes);
		BigInteger bigInt = new BigInteger(1, digest);
		String hash = bigInt.toString(16);
		while (hash.length() < 32) {
			hash = "0" + hash;
		}
		return hash;
	}

	/**
	 * make the activity no title.
	 * 
	 * @param activity
	 */
	public static void setNoTitle(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * set the activity orientation.
	 * 
	 * @param activity
	 * @param orientation
	 */
	public static void setScreenOrientation(Activity activity, int orientation) {
		activity.setRequestedOrientation(orientation);
	}

	/**
	 * make the activity fill the screen.
	 * 
	 * @param activity
	 */
	public static void setFullScreen(Activity activity) {
		activity.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * quit full screen .
	 * 
	 * @param activity
	 */
	public static void quitFullScreen(Activity activity) {
		final WindowManager.LayoutParams attrs = activity.getWindow()
				.getAttributes();
		attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
		activity.getWindow().setAttributes(attrs);
		activity.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}

	/**
	 * if there is a available sdcard in the phone.
	 * 
	 * @return
	 */
	public static boolean isHasSdcard() {
		String status = Environment.getExternalStorageState();
		return status.equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * get the root dir of the application,if a sdcard available, it will create
	 * in the sdcard ,or it will create in the application package dir.
	 * 
	 * @param context
	 * @param name
	 *            the name of the folder.
	 * @return
	 */
//	public static String getRootDir(Context context) {
//		if (isHasSdcard()) {
//		    String root=Environment.getExternalStorageDirectory().getAbsolutePath()
//                    + "/" + App.APP_ROOT_DIR_NAME + "/";
//		    if(FileUtil.isFileExist(root)||new File(root).mkdirs()){
//		        return root;
//		    }else{
//		        return "/data/data/" + context.getPackageName() + "/" + App.APP_ROOT_DIR_NAME + "/";
//		    }
//		} else {
//			return "/data/data/" + context.getPackageName() + "/" + App.APP_ROOT_DIR_NAME + "/";
//		}
//	}
//
//	public static String getImageCacheDir(Context context){
//		return getRootDir(context)+"image/";
//	}
//
//	public static String getDatabasePath(Activity activity){
//        String path="/data/data/" + activity.getPackageName() + "/databases/";
//        if(!FileUtil.isFileExist(path)){
//            FileUtil.creatSDDir(path);
//        }
//        return path;
//    }
	/**
	 * get the root view of a activity.
	 * 
	 * @param activity
	 * @return
	 */
	public static ViewGroup getRootView(Activity activity) {
		return ((ViewGroup) activity.findViewById(android.R.id.content));
	}

	/**
	 * if the app is running foreground.
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isRunningForeground(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = cn.getPackageName();
		if (!isEmpty(currentPackageName)
				&& currentPackageName.equals(context.getPackageName())) {
			return true;
		}

		return false;
	}

	// public static boolean isAppOnForeground(Context context) {
	// ActivityManager activityManager = (ActivityManager)
	// context.getSystemService(Context.ACTIVITY_SERVICE);
	// String packageName = context.getPackageName();
	// List<RunningAppProcessInfo> appProcesses =
	// activityManager.getRunningAppProcesses();
	// if (appProcesses == null)
	// return false;
	//
	// for (RunningAppProcessInfo appProcess : appProcesses) {
	// if (appProcess.processName.equals(packageName)
	// && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
	// {
	// return true;
	// }
	// }
	//
	// return false;
	// }

	/**
	 * get the display metrics of the activity.
	 * 
	 * @param activity
	 * @return
	 */
	public static DisplayMetrics getDM(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		// Catching a Null Pointer Exception a Code smells
		// NullPointerException happens after discarding activity and relaunching.
		try {
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            dm.heightPixels=dm.heightPixels-getStatusBarHeight(activity);			
		} catch (NullPointerException e) {
            // do nothing, probably better to show an error dialog as follows.
            // 'Sorry, Please relaunch the application.'
            e.printStackTrace();
		}
		return dm;
	}

	public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
	/**
	 * get the file name of the url or path.
	 * 
	 * @param url
	 * @return
	 */
	public static String getFileName(String url) {
		return url.substring(url.lastIndexOf("/") + 1);
	}

	/**
	 * get the file name of the url or path with out extension.
	 * 
	 * @param url
	 * @return
	 */
	public static String getFileNameWithOutADD(String url) {
		String filename = getFileName(url);
		return filename.substring(0, filename.lastIndexOf("."));
	}

	public static final int getStringWidth(TextView txt) {
		return getStringWidth(txt.getPaint(), txt.getText().toString());
	}

	public static final int getStringWidth(Paint paint, String txt) {
		return (int) paint.measureText(txt);
	}

	public static long getTimeMillisFromDataStr(String datestr, String pattern)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new SimpleDateFormat(pattern).parse(datestr));
		return calendar.getTimeInMillis();
	}

	public static String getTimeFromTimeMillis(long milltime, String pattern) {
		return new SimpleDateFormat(pattern).format(new Date(milltime));
	}

	public static void hideKeyBord(Activity context) {
		try {
			((InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(context.getCurrentFocus()
							.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	public static void showKeyBord(Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public static boolean emailFormat(String email) {
		boolean flag = true;
		if (isEmpty(email))
			return false;
		final String pattern1 = "^([a-zA-Z0-9\\+_\\-]+)(\\.[a-zA-Z0-9\\+_\\-]+)*@([a-zA-Z0-9\\-]+\\.)+[a-zA-Z]{2,6}$";
		final Pattern patter = Pattern.compile(pattern1);
		final Matcher matcher = patter.matcher(email);
		if (!matcher.find()) {
			return false;
		}
		return flag;
	}
	
	public static boolean checkPassword(String password){
	    password=password.replaceAll("[a-z]*[A-Z]*\\d*_*", "");
        String special="!\"#$%&'()=~|-^\\@[;:],./`{+*}>?_";
        for(int i=0;i<special.length();i++){
            String c=special.charAt(i)+"";
            if(c.equals("(")||c.equals("[")||c.equals("{")||c.equals("/")||c.equals("^")||c.equals("-")
               ||c.equals("$")||c.equals("¦")||c.equals("}")||c.equals("]")||c.equals(")")
               ||c.equals("?")||c.equals("*")||c.equals("+")||c.equals(".")||c.equals("|")
               ||c.equals("\\")){
                c="\\"+c;
            }
            password=password.replaceAll(c, "");
        }
        return Tools.isEmpty(password);
    }
	
//	 public static boolean containsEmoji(String source) { 
//         int len = source.length(); 
//         for (int i = 0; i < len; i++) { 
//             char codePoint = source.charAt(i); 
//             if (!isEmojiCharacter(codePoint)) { 
//                 return true; 
//             } 
//         } 
//         return false; 
//    } 
	 
//	private static boolean isEmojiCharacter(char codePoint) { 
//	     return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)); 
//	}
	
	/**
     * 引数の文字列に絵文字が含まれていればtrue, 含まれていなければfalse
     * @see https://github.com/delight-im/Emoji
     * @param str
     * @return
     */
    public static boolean isIncludeEmoji(String str) {

        if (str == null || str.isEmpty()) {
            return false;
        }


        // extract the single chars that will be operated on
        final char[] chars = str.toCharArray();

        int codePoint = 0;
        for (int i = 0; i < chars.length; i++) {

            if (Character.isLowSurrogate(chars[i])) {
                if (i > 0 && Character.isSurrogatePair(chars[i-1], chars[i])) {
                    // get the Unicode code point for the surrogate pair
                    codePoint = Character.toCodePoint(chars[i-1], chars[i]);
                }
            } else {
                // get the Unicode code point by simply casting the char to int
                codePoint = (int) chars[i];
            }

            if (codePoint != 0 && isEmoji(codePoint)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Compares the given code point against 722 emoji code points from Unicode 6.3
     * <p>
     * Reference: EmojiSources.txt by Unicode, Inc. (http://www.unicode.org/Public/UNIDATA/EmojiSources.txt)
     * 
     * @param codePoint the code point to check
     * @return whether the code point represents an emoji or not
     */
    private static boolean isEmoji(int codePoint) {
        return
            // Digits and number sign on keys (actually defined in concatenated form)
            // codePoint == 0x0023 0x20E3 ||
            // codePoint == 0x0030 0x20E3 ||
            // codePoint == 0x0031 0x20E3 ||
            // codePoint == 0x0032 0x20E3 ||
            // codePoint == 0x0033 0x20E3 ||
            // codePoint == 0x0034 0x20E3 ||
            // codePoint == 0x0035 0x20E3 ||
            // codePoint == 0x0036 0x20E3 ||
            // codePoint == 0x0037 0x20E3 ||
            // codePoint == 0x0038 0x20E3 ||
            // codePoint == 0x0039 0x20E3 ||

            codePoint == 0x00A9 ||
            codePoint == 0x00AE ||
            codePoint == 0x2002 ||
            codePoint == 0x2003 ||
            codePoint == 0x2005 ||
            codePoint == 0x203C ||
            codePoint == 0x2049 ||
            codePoint == 0x2122 ||
            codePoint == 0x2139 ||
            codePoint == 0x2194 ||
            codePoint == 0x2195 ||
            codePoint == 0x2196 ||
            codePoint == 0x2197 ||
            codePoint == 0x2198 ||
            codePoint == 0x2199 ||
            codePoint == 0x21A9 ||
            codePoint == 0x21AA ||
            codePoint == 0x231A ||
            codePoint == 0x231B ||
            codePoint == 0x23E9 ||
            codePoint == 0x23EA ||
            codePoint == 0x23EB ||
            codePoint == 0x23EC ||
            codePoint == 0x23F0 ||
            codePoint == 0x23F3 ||
            codePoint == 0x24C2 ||
            codePoint == 0x25AA ||
            codePoint == 0x25AB ||
            codePoint == 0x25B6 ||
            codePoint == 0x25C0 ||
            codePoint == 0x25FB ||
            codePoint == 0x25FC ||
            codePoint == 0x25FD ||
            codePoint == 0x25FE ||
            codePoint == 0x2600 ||
            codePoint == 0x2601 ||
            codePoint == 0x260E ||
            codePoint == 0x2611 ||
            codePoint == 0x2614 ||
            codePoint == 0x2615 ||
            codePoint == 0x261D ||
            codePoint == 0x263A ||
            codePoint == 0x2648 ||
            codePoint == 0x2649 ||
            codePoint == 0x264A ||
            codePoint == 0x264B ||
            codePoint == 0x264C ||
            codePoint == 0x264D ||
            codePoint == 0x264E ||
            codePoint == 0x264F ||
            codePoint == 0x2650 ||
            codePoint == 0x2651 ||
            codePoint == 0x2652 ||
            codePoint == 0x2653 ||
            codePoint == 0x2660 ||
            codePoint == 0x2663 ||
            codePoint == 0x2665 ||
            codePoint == 0x2666 ||
            codePoint == 0x2668 ||
            codePoint == 0x267B ||
            codePoint == 0x267F ||
            codePoint == 0x2693 ||
            codePoint == 0x26A0 ||
            codePoint == 0x26A1 ||
            codePoint == 0x26AA ||
            codePoint == 0x26AB ||
            codePoint == 0x26BD ||
            codePoint == 0x26BE ||
            codePoint == 0x26C4 ||
            codePoint == 0x26C5 ||
            codePoint == 0x26CE ||
            codePoint == 0x26D4 ||
            codePoint == 0x26EA ||
            codePoint == 0x26F2 ||
            codePoint == 0x26F3 ||
            codePoint == 0x26F5 ||
            codePoint == 0x26FA ||
            codePoint == 0x26FD ||
            codePoint == 0x2702 ||
            codePoint == 0x2705 ||
            codePoint == 0x2708 ||
            codePoint == 0x2709 ||
            codePoint == 0x270A ||
            codePoint == 0x270B ||
            codePoint == 0x270C ||
            codePoint == 0x270F ||
            codePoint == 0x2712 ||
            codePoint == 0x2714 ||
            codePoint == 0x2716 ||
            codePoint == 0x2728 ||
            codePoint == 0x2733 ||
            codePoint == 0x2734 ||
            codePoint == 0x2744 ||
            codePoint == 0x2747 ||
            codePoint == 0x274C ||
            codePoint == 0x274E ||
            codePoint == 0x2753 ||
            codePoint == 0x2754 ||
            codePoint == 0x2755 ||
            codePoint == 0x2757 ||
            codePoint == 0x2764 ||
            codePoint == 0x2795 ||
            codePoint == 0x2796 ||
            codePoint == 0x2797 ||
            codePoint == 0x27A1 ||
            codePoint == 0x27B0 ||
            codePoint == 0x2934 ||
            codePoint == 0x2935 ||
            codePoint == 0x2B05 ||
            codePoint == 0x2B06 ||
            codePoint == 0x2B07 ||
            codePoint == 0x2B1B ||
            codePoint == 0x2B1C ||
            codePoint == 0x2B50 ||
            codePoint == 0x2B55 ||
            codePoint == 0x3030 ||
            codePoint == 0x303D ||
            codePoint == 0x3297 ||
            codePoint == 0x3299 ||
            codePoint == 0x1F004 ||
            codePoint == 0x1F0CF ||
            codePoint == 0x1F170 ||
            codePoint == 0x1F171 ||
            codePoint == 0x1F17E ||
            codePoint == 0x1F17F ||
            codePoint == 0x1F18E ||
            codePoint == 0x1F191 ||
            codePoint == 0x1F192 ||
            codePoint == 0x1F193 ||
            codePoint == 0x1F194 ||
            codePoint == 0x1F195 ||
            codePoint == 0x1F196 ||
            codePoint == 0x1F197 ||
            codePoint == 0x1F198 ||
            codePoint == 0x1F199 ||
            codePoint == 0x1F19A ||

            // Regional Indicator Symbols (actually defined in concatenated form)
            (codePoint == 0x1F1E8 || codePoint == 0x1F1F3) ||
            (codePoint == 0x1F1E9 || codePoint == 0x1F1EA) ||
            (codePoint == 0x1F1EA || codePoint == 0x1F1F8) ||
            (codePoint == 0x1F1EB || codePoint == 0x1F1F7) ||
            (codePoint == 0x1F1EC || codePoint == 0x1F1E7) ||
            (codePoint == 0x1F1EE || codePoint == 0x1F1F9) ||
            (codePoint == 0x1F1EF || codePoint == 0x1F1F5) ||
            (codePoint == 0x1F1F0 || codePoint == 0x1F1F7) ||
            (codePoint == 0x1F1F7 || codePoint == 0x1F1FA) ||
            (codePoint == 0x1F1FA || codePoint == 0x1F1F8) ||

            codePoint == 0x1F201 ||
            codePoint == 0x1F202 ||
            codePoint == 0x1F21A ||
            codePoint == 0x1F22F ||
            codePoint == 0x1F232 ||
            codePoint == 0x1F233 ||
            codePoint == 0x1F234 ||
            codePoint == 0x1F235 ||
            codePoint == 0x1F236 ||
            codePoint == 0x1F237 ||
            codePoint == 0x1F238 ||
            codePoint == 0x1F239 ||
            codePoint == 0x1F23A ||
            codePoint == 0x1F250 ||
            codePoint == 0x1F251 ||
            codePoint == 0x1F300 ||
            codePoint == 0x1F301 ||
            codePoint == 0x1F302 ||
            codePoint == 0x1F303 ||
            codePoint == 0x1F304 ||
            codePoint == 0x1F305 ||
            codePoint == 0x1F306 ||
            codePoint == 0x1F307 ||
            codePoint == 0x1F308 ||
            codePoint == 0x1F309 ||
            codePoint == 0x1F30A ||
            codePoint == 0x1F30B ||
            codePoint == 0x1F30C ||
            codePoint == 0x1F30F ||
            codePoint == 0x1F311 ||
            codePoint == 0x1F313 ||
            codePoint == 0x1F314 ||
            codePoint == 0x1F315 ||
            codePoint == 0x1F319 ||
            codePoint == 0x1F31B ||
            codePoint == 0x1F31F ||
            codePoint == 0x1F320 ||
            codePoint == 0x1F330 ||
            codePoint == 0x1F331 ||
            codePoint == 0x1F334 ||
            codePoint == 0x1F335 ||
            codePoint == 0x1F337 ||
            codePoint == 0x1F338 ||
            codePoint == 0x1F339 ||
            codePoint == 0x1F33A ||
            codePoint == 0x1F33B ||
            codePoint == 0x1F33C ||
            codePoint == 0x1F33D ||
            codePoint == 0x1F33E ||
            codePoint == 0x1F33F ||
            codePoint == 0x1F340 ||
            codePoint == 0x1F341 ||
            codePoint == 0x1F342 ||
            codePoint == 0x1F343 ||
            codePoint == 0x1F344 ||
            codePoint == 0x1F345 ||
            codePoint == 0x1F346 ||
            codePoint == 0x1F347 ||
            codePoint == 0x1F348 ||
            codePoint == 0x1F349 ||
            codePoint == 0x1F34A ||
            codePoint == 0x1F34C ||
            codePoint == 0x1F34D ||
            codePoint == 0x1F34E ||
            codePoint == 0x1F34F ||
            codePoint == 0x1F351 ||
            codePoint == 0x1F352 ||
            codePoint == 0x1F353 ||
            codePoint == 0x1F354 ||
            codePoint == 0x1F355 ||
            codePoint == 0x1F356 ||
            codePoint == 0x1F357 ||
            codePoint == 0x1F358 ||
            codePoint == 0x1F359 ||
            codePoint == 0x1F35A ||
            codePoint == 0x1F35B ||
            codePoint == 0x1F35C ||
            codePoint == 0x1F35D ||
            codePoint == 0x1F35E ||
            codePoint == 0x1F35F ||
            codePoint == 0x1F360 ||
            codePoint == 0x1F361 ||
            codePoint == 0x1F362 ||
            codePoint == 0x1F363 ||
            codePoint == 0x1F364 ||
            codePoint == 0x1F365 ||
            codePoint == 0x1F366 ||
            codePoint == 0x1F367 ||
            codePoint == 0x1F368 ||
            codePoint == 0x1F369 ||
            codePoint == 0x1F36A ||
            codePoint == 0x1F36B ||
            codePoint == 0x1F36C ||
            codePoint == 0x1F36D ||
            codePoint == 0x1F36E ||
            codePoint == 0x1F36F ||
            codePoint == 0x1F370 ||
            codePoint == 0x1F371 ||
            codePoint == 0x1F372 ||
            codePoint == 0x1F373 ||
            codePoint == 0x1F374 ||
            codePoint == 0x1F375 ||
            codePoint == 0x1F376 ||
            codePoint == 0x1F377 ||
            codePoint == 0x1F378 ||
            codePoint == 0x1F379 ||
            codePoint == 0x1F37A ||
            codePoint == 0x1F37B ||
            codePoint == 0x1F380 ||
            codePoint == 0x1F381 ||
            codePoint == 0x1F382 ||
            codePoint == 0x1F383 ||
            codePoint == 0x1F384 ||
            codePoint == 0x1F385 ||
            codePoint == 0x1F386 ||
            codePoint == 0x1F387 ||
            codePoint == 0x1F388 ||
            codePoint == 0x1F389 ||
            codePoint == 0x1F38A ||
            codePoint == 0x1F38B ||
            codePoint == 0x1F38C ||
            codePoint == 0x1F38D ||
            codePoint == 0x1F38E ||
            codePoint == 0x1F38F ||
            codePoint == 0x1F390 ||
            codePoint == 0x1F391 ||
            codePoint == 0x1F392 ||
            codePoint == 0x1F393 ||
            codePoint == 0x1F3A0 ||
            codePoint == 0x1F3A1 ||
            codePoint == 0x1F3A2 ||
            codePoint == 0x1F3A3 ||
            codePoint == 0x1F3A4 ||
            codePoint == 0x1F3A5 ||
            codePoint == 0x1F3A6 ||
            codePoint == 0x1F3A7 ||
            codePoint == 0x1F3A8 ||
            codePoint == 0x1F3A9 ||
            codePoint == 0x1F3AA ||
            codePoint == 0x1F3AB ||
            codePoint == 0x1F3AC ||
            codePoint == 0x1F3AD ||
            codePoint == 0x1F3AE ||
            codePoint == 0x1F3AF ||
            codePoint == 0x1F3B0 ||
            codePoint == 0x1F3B1 ||
            codePoint == 0x1F3B2 ||
            codePoint == 0x1F3B3 ||
            codePoint == 0x1F3B4 ||
            codePoint == 0x1F3B5 ||
            codePoint == 0x1F3B6 ||
            codePoint == 0x1F3B7 ||
            codePoint == 0x1F3B8 ||
            codePoint == 0x1F3B9 ||
            codePoint == 0x1F3BA ||
            codePoint == 0x1F3BB ||
            codePoint == 0x1F3BC ||
            codePoint == 0x1F3BD ||
            codePoint == 0x1F3BE ||
            codePoint == 0x1F3BF ||
            codePoint == 0x1F3C0 ||
            codePoint == 0x1F3C1 ||
            codePoint == 0x1F3C2 ||
            codePoint == 0x1F3C3 ||
            codePoint == 0x1F3C4 ||
            codePoint == 0x1F3C6 ||
            codePoint == 0x1F3C8 ||
            codePoint == 0x1F3CA ||
            codePoint == 0x1F3E0 ||
            codePoint == 0x1F3E1 ||
            codePoint == 0x1F3E2 ||
            codePoint == 0x1F3E3 ||
            codePoint == 0x1F3E5 ||
            codePoint == 0x1F3E6 ||
            codePoint == 0x1F3E7 ||
            codePoint == 0x1F3E8 ||
            codePoint == 0x1F3E9 ||
            codePoint == 0x1F3EA ||
            codePoint == 0x1F3EB ||
            codePoint == 0x1F3EC ||
            codePoint == 0x1F3ED ||
            codePoint == 0x1F3EE ||
            codePoint == 0x1F3EF ||
            codePoint == 0x1F3F0 ||
            codePoint == 0x1F40C ||
            codePoint == 0x1F40D ||
            codePoint == 0x1F40E ||
            codePoint == 0x1F411 ||
            codePoint == 0x1F412 ||
            codePoint == 0x1F414 ||
            codePoint == 0x1F417 ||
            codePoint == 0x1F418 ||
            codePoint == 0x1F419 ||
            codePoint == 0x1F41A ||
            codePoint == 0x1F41B ||
            codePoint == 0x1F41C ||
            codePoint == 0x1F41D ||
            codePoint == 0x1F41E ||
            codePoint == 0x1F41F ||
            codePoint == 0x1F420 ||
            codePoint == 0x1F421 ||
            codePoint == 0x1F422 ||
            codePoint == 0x1F423 ||
            codePoint == 0x1F424 ||
            codePoint == 0x1F425 ||
            codePoint == 0x1F426 ||
            codePoint == 0x1F427 ||
            codePoint == 0x1F428 ||
            codePoint == 0x1F429 ||
            codePoint == 0x1F42B ||
            codePoint == 0x1F42C ||
            codePoint == 0x1F42D ||
            codePoint == 0x1F42E ||
            codePoint == 0x1F42F ||
            codePoint == 0x1F430 ||
            codePoint == 0x1F431 ||
            codePoint == 0x1F432 ||
            codePoint == 0x1F433 ||
            codePoint == 0x1F434 ||
            codePoint == 0x1F435 ||
            codePoint == 0x1F436 ||
            codePoint == 0x1F437 ||
            codePoint == 0x1F438 ||
            codePoint == 0x1F439 ||
            codePoint == 0x1F43A ||
            codePoint == 0x1F43B ||
            codePoint == 0x1F43C ||
            codePoint == 0x1F43D ||
            codePoint == 0x1F43E ||
            codePoint == 0x1F440 ||
            codePoint == 0x1F442 ||
            codePoint == 0x1F443 ||
            codePoint == 0x1F444 ||
            codePoint == 0x1F445 ||
            codePoint == 0x1F446 ||
            codePoint == 0x1F447 ||
            codePoint == 0x1F448 ||
            codePoint == 0x1F449 ||
            codePoint == 0x1F44A ||
            codePoint == 0x1F44B ||
            codePoint == 0x1F44C ||
            codePoint == 0x1F44D ||
            codePoint == 0x1F44E ||
            codePoint == 0x1F44F ||
            codePoint == 0x1F450 ||
            codePoint == 0x1F451 ||
            codePoint == 0x1F452 ||
            codePoint == 0x1F453 ||
            codePoint == 0x1F454 ||
            codePoint == 0x1F455 ||
            codePoint == 0x1F456 ||
            codePoint == 0x1F457 ||
            codePoint == 0x1F458 ||
            codePoint == 0x1F459 ||
            codePoint == 0x1F45A ||
            codePoint == 0x1F45B ||
            codePoint == 0x1F45C ||
            codePoint == 0x1F45D ||
            codePoint == 0x1F45E ||
            codePoint == 0x1F45F ||
            codePoint == 0x1F460 ||
            codePoint == 0x1F461 ||
            codePoint == 0x1F462 ||
            codePoint == 0x1F463 ||
            codePoint == 0x1F464 ||
            codePoint == 0x1F466 ||
            codePoint == 0x1F467 ||
            codePoint == 0x1F468 ||
            codePoint == 0x1F469 ||
            codePoint == 0x1F46A ||
            codePoint == 0x1F46B ||
            codePoint == 0x1F46E ||
            codePoint == 0x1F46F ||
            codePoint == 0x1F470 ||
            codePoint == 0x1F471 ||
            codePoint == 0x1F472 ||
            codePoint == 0x1F473 ||
            codePoint == 0x1F474 ||
            codePoint == 0x1F475 ||
            codePoint == 0x1F476 ||
            codePoint == 0x1F477 ||
            codePoint == 0x1F478 ||
            codePoint == 0x1F479 ||
            codePoint == 0x1F47A ||
            codePoint == 0x1F47B ||
            codePoint == 0x1F47C ||
            codePoint == 0x1F47D ||
            codePoint == 0x1F47E ||
            codePoint == 0x1F47F ||
            codePoint == 0x1F480 ||
            codePoint == 0x1F481 ||
            codePoint == 0x1F482 ||
            codePoint == 0x1F483 ||
            codePoint == 0x1F484 ||
            codePoint == 0x1F485 ||
            codePoint == 0x1F486 ||
            codePoint == 0x1F487 ||
            codePoint == 0x1F488 ||
            codePoint == 0x1F489 ||
            codePoint == 0x1F48A ||
            codePoint == 0x1F48B ||
            codePoint == 0x1F48C ||
            codePoint == 0x1F48D ||
            codePoint == 0x1F48E ||
            codePoint == 0x1F48F ||
            codePoint == 0x1F490 ||
            codePoint == 0x1F491 ||
            codePoint == 0x1F492 ||
            codePoint == 0x1F493 ||
            codePoint == 0x1F494 ||
            codePoint == 0x1F495 ||
            codePoint == 0x1F496 ||
            codePoint == 0x1F497 ||
            codePoint == 0x1F498 ||
            codePoint == 0x1F499 ||
            codePoint == 0x1F49A ||
            codePoint == 0x1F49B ||
            codePoint == 0x1F49C ||
            codePoint == 0x1F49D ||
            codePoint == 0x1F49E ||
            codePoint == 0x1F49F ||
            codePoint == 0x1F4A0 ||
            codePoint == 0x1F4A1 ||
            codePoint == 0x1F4A2 ||
            codePoint == 0x1F4A3 ||
            codePoint == 0x1F4A4 ||
            codePoint == 0x1F4A5 ||
            codePoint == 0x1F4A6 ||
            codePoint == 0x1F4A7 ||
            codePoint == 0x1F4A8 ||
            codePoint == 0x1F4A9 ||
            codePoint == 0x1F4AA ||
            codePoint == 0x1F4AB ||
            codePoint == 0x1F4AC ||
            codePoint == 0x1F4AE ||
            codePoint == 0x1F4AF ||
            codePoint == 0x1F4B0 ||
            codePoint == 0x1F4B1 ||
            codePoint == 0x1F4B2 ||
            codePoint == 0x1F4B3 ||
            codePoint == 0x1F4B4 ||
            codePoint == 0x1F4B5 ||
            codePoint == 0x1F4B8 ||
            codePoint == 0x1F4B9 ||
            codePoint == 0x1F4BA ||
            codePoint == 0x1F4BB ||
            codePoint == 0x1F4BC ||
            codePoint == 0x1F4BD ||
            codePoint == 0x1F4BE ||
            codePoint == 0x1F4BF ||
            codePoint == 0x1F4C0 ||
            codePoint == 0x1F4C1 ||
            codePoint == 0x1F4C2 ||
            codePoint == 0x1F4C3 ||
            codePoint == 0x1F4C4 ||
            codePoint == 0x1F4C5 ||
            codePoint == 0x1F4C6 ||
            codePoint == 0x1F4C7 ||
            codePoint == 0x1F4C8 ||
            codePoint == 0x1F4C9 ||
            codePoint == 0x1F4CA ||
            codePoint == 0x1F4CB ||
            codePoint == 0x1F4CC ||
            codePoint == 0x1F4CD ||
            codePoint == 0x1F4CE ||
            codePoint == 0x1F4CF ||
            codePoint == 0x1F4D0 ||
            codePoint == 0x1F4D1 ||
            codePoint == 0x1F4D2 ||
            codePoint == 0x1F4D3 ||
            codePoint == 0x1F4D4 ||
            codePoint == 0x1F4D5 ||
            codePoint == 0x1F4D6 ||
            codePoint == 0x1F4D7 ||
            codePoint == 0x1F4D8 ||
            codePoint == 0x1F4D9 ||
            codePoint == 0x1F4DA ||
            codePoint == 0x1F4DB ||
            codePoint == 0x1F4DC ||
            codePoint == 0x1F4DD ||
            codePoint == 0x1F4DE ||
            codePoint == 0x1F4DF ||
            codePoint == 0x1F4E0 ||
            codePoint == 0x1F4E1 ||
            codePoint == 0x1F4E2 ||
            codePoint == 0x1F4E3 ||
            codePoint == 0x1F4E4 ||
            codePoint == 0x1F4E5 ||
            codePoint == 0x1F4E6 ||
            codePoint == 0x1F4E7 ||
            codePoint == 0x1F4E8 ||
            codePoint == 0x1F4E9 ||
            codePoint == 0x1F4EA ||
            codePoint == 0x1F4EB ||
            codePoint == 0x1F4EE ||
            codePoint == 0x1F4F0 ||
            codePoint == 0x1F4F1 ||
            codePoint == 0x1F4F2 ||
            codePoint == 0x1F4F3 ||
            codePoint == 0x1F4F4 ||
            codePoint == 0x1F4F6 ||
            codePoint == 0x1F4F7 ||
            codePoint == 0x1F4F9 ||
            codePoint == 0x1F4FA ||
            codePoint == 0x1F4FB ||
            codePoint == 0x1F4FC ||
            codePoint == 0x1F503 ||
            codePoint == 0x1F50A ||
            codePoint == 0x1F50B ||
            codePoint == 0x1F50C ||
            codePoint == 0x1F50D ||
            codePoint == 0x1F50E ||
            codePoint == 0x1F50F ||
            codePoint == 0x1F510 ||
            codePoint == 0x1F511 ||
            codePoint == 0x1F512 ||
            codePoint == 0x1F513 ||
            codePoint == 0x1F514 ||
            codePoint == 0x1F516 ||
            codePoint == 0x1F517 ||
            codePoint == 0x1F518 ||
            codePoint == 0x1F519 ||
            codePoint == 0x1F51A ||
            codePoint == 0x1F51B ||
            codePoint == 0x1F51C ||
            codePoint == 0x1F51D ||
            codePoint == 0x1F51E ||
            codePoint == 0x1F51F ||
            codePoint == 0x1F520 ||
            codePoint == 0x1F521 ||
            codePoint == 0x1F522 ||
            codePoint == 0x1F523 ||
            codePoint == 0x1F524 ||
            codePoint == 0x1F525 ||
            codePoint == 0x1F526 ||
            codePoint == 0x1F527 ||
            codePoint == 0x1F528 ||
            codePoint == 0x1F529 ||
            codePoint == 0x1F52A ||
            codePoint == 0x1F52B ||
            codePoint == 0x1F52E ||
            codePoint == 0x1F52F ||
            codePoint == 0x1F530 ||
            codePoint == 0x1F531 ||
            codePoint == 0x1F532 ||
            codePoint == 0x1F533 ||
            codePoint == 0x1F534 ||
            codePoint == 0x1F535 ||
            codePoint == 0x1F536 ||
            codePoint == 0x1F537 ||
            codePoint == 0x1F538 ||
            codePoint == 0x1F539 ||
            codePoint == 0x1F53A ||
            codePoint == 0x1F53B ||
            codePoint == 0x1F53C ||
            codePoint == 0x1F53D ||
            codePoint == 0x1F550 ||
            codePoint == 0x1F551 ||
            codePoint == 0x1F552 ||
            codePoint == 0x1F553 ||
            codePoint == 0x1F554 ||
            codePoint == 0x1F555 ||
            codePoint == 0x1F556 ||
            codePoint == 0x1F557 ||
            codePoint == 0x1F558 ||
            codePoint == 0x1F559 ||
            codePoint == 0x1F55A ||
            codePoint == 0x1F55B ||
            codePoint == 0x1F5FB ||
            codePoint == 0x1F5FC ||
            codePoint == 0x1F5FD ||
            codePoint == 0x1F5FE ||
            codePoint == 0x1F5FF ||
            codePoint == 0x1F601 ||
            codePoint == 0x1F602 ||
            codePoint == 0x1F603 ||
            codePoint == 0x1F604 ||
            codePoint == 0x1F605 ||
            codePoint == 0x1F606 ||
            codePoint == 0x1F609 ||
            codePoint == 0x1F60A ||
            codePoint == 0x1F60B ||
            codePoint == 0x1F60C ||
            codePoint == 0x1F60D ||
            codePoint == 0x1F60F ||
            codePoint == 0x1F612 ||
            codePoint == 0x1F613 ||
            codePoint == 0x1F614 ||
            codePoint == 0x1F616 ||
            codePoint == 0x1F618 ||
            codePoint == 0x1F61A ||
            codePoint == 0x1F61C ||
            codePoint == 0x1F61D ||
            codePoint == 0x1F61E ||
            codePoint == 0x1F620 ||
            codePoint == 0x1F621 ||
            codePoint == 0x1F622 ||
            codePoint == 0x1F623 ||
            codePoint == 0x1F624 ||
            codePoint == 0x1F625 ||
            codePoint == 0x1F628 ||
            codePoint == 0x1F629 ||
            codePoint == 0x1F62A ||
            codePoint == 0x1F62B ||
            codePoint == 0x1F62D ||
            codePoint == 0x1F630 ||
            codePoint == 0x1F631 ||
            codePoint == 0x1F632 ||
            codePoint == 0x1F633 ||
            codePoint == 0x1F635 ||
            codePoint == 0x1F637 ||
            codePoint == 0x1F638 ||
            codePoint == 0x1F639 ||
            codePoint == 0x1F63A ||
            codePoint == 0x1F63B ||
            codePoint == 0x1F63C ||
            codePoint == 0x1F63D ||
            codePoint == 0x1F63E ||
            codePoint == 0x1F63F ||
            codePoint == 0x1F640 ||
            codePoint == 0x1F645 ||
            codePoint == 0x1F646 ||
            codePoint == 0x1F647 ||
            codePoint == 0x1F648 ||
            codePoint == 0x1F649 ||
            codePoint == 0x1F64A ||
            codePoint == 0x1F64B ||
            codePoint == 0x1F64C ||
            codePoint == 0x1F64D ||
            codePoint == 0x1F64E ||
            codePoint == 0x1F64F ||
            codePoint == 0x1F680 ||
            codePoint == 0x1F683 ||
            codePoint == 0x1F684 ||
            codePoint == 0x1F685 ||
            codePoint == 0x1F687 ||
            codePoint == 0x1F689 ||
            codePoint == 0x1F68C ||
            codePoint == 0x1F68F ||
            codePoint == 0x1F691 ||
            codePoint == 0x1F692 ||
            codePoint == 0x1F693 ||
            codePoint == 0x1F695 ||
            codePoint == 0x1F697 ||
            codePoint == 0x1F699 ||
            codePoint == 0x1F69A ||
            codePoint == 0x1F6A2 ||
            codePoint == 0x1F6A4 ||
            codePoint == 0x1F6A5 ||
            codePoint == 0x1F6A7 ||
            codePoint == 0x1F6A8 ||
            codePoint == 0x1F6A9 ||
            codePoint == 0x1F6AA ||
            codePoint == 0x1F6AB ||
            codePoint == 0x1F6AC ||
            codePoint == 0x1F6AD ||
            codePoint == 0x1F6B2 ||
            codePoint == 0x1F6B6 ||
            codePoint == 0x1F6B9 ||
            codePoint == 0x1F6BA ||
            codePoint == 0x1F6BB ||
            codePoint == 0x1F6BC ||
            codePoint == 0x1F6BD ||
            codePoint == 0x1F6BE ||
            codePoint == 0x1F6C0;
    }
    /**
     * re_calculate the height of listview to show all data(it's bug for listview and scrollview)
     * @param listView
     * @author chenchen  2014-11-14
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    
    /**
     * re_calculate the height of gridview to show all data(it's bug for gridview and scrollview)
     * @param gridView
     * @author chenchen  2014-11-14
     */
//    public static void setGridViewHeightBasedOnChildren(GridView gridView , Context context ,int verticalSpacingDp,int columes) {
//        ListAdapter listAdapter = gridView.getAdapter();
//        if (listAdapter == null) {
//            return;
//        }
//
//        float totalHeight = 0;
//        int total=listAdapter.getCount();
//        int rows=(int) Math.ceil(total/(float)columes);
//        for (int i = 0; i < rows; i++) {
//            View listItem = listAdapter.getView(i, null, gridView);
//            listItem.measure(0, 0);
//            float itemHeight=listItem.getMeasuredHeight();
//            float space=Dip2px.dip2px(context, verticalSpacingDp);
//            totalHeight += itemHeight;
//            totalHeight += space;
//        }
//
//        LayoutParams params = gridView.getLayoutParams();
//        params.height = (int) totalHeight;
//        gridView.setLayoutParams(params);
//    }

    public static String getTimeDifference(long startTime,long endTime) {
    	Integer ss = 1000;  
        Integer mi = ss * 60;  
        Integer hh = mi * 60;  
        Integer dd = hh * 24;
        
        long ms = endTime - startTime;
      
        Long day = ms / dd;  
        Long hour = (ms - day * dd) / hh;  
        Long minute = (ms - day * dd - hour * hh) / mi;  
//        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;  
//        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;  
        
        if (day > 183) {
			return "半年前";
		}else if(day > 31){
			return day/31+"ヶ月前";
		}else if (day > 0) {
			return day+"日前";
		}else if (hour > 0) {
			return hour+"時間前";
		}else if (minute > 0) {
			return minute+"分前";
		}else {
			return "1分以内";
		}
	}
    
    public static boolean isKata(String katakana){
        return katakana.matches("^[\\u30A0-\\u30FF]+$"); 
    }
    
    public static boolean isZenkaku(String zenkaku){
        return zenkaku.matches("^[\\u3040-\\u30FF]+$"); 
    }
    
    public static String getDeviceId(Context context){
        String id=null;
        TelephonyManager tm=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        id=tm.getDeviceId();
        MLog.i("GCM", "deviceid1="+id);
        if(TextUtils.isEmpty(id)){
            id=android.os.Build.SERIAL;
            MLog.i("GCM", "deviceid2="+id);
        }
        if(TextUtils.isEmpty(id)){
            id=tm.getSimSerialNumber();
            MLog.i("GCM", "deviceid3="+id);
        }
        return id;
    }
}
