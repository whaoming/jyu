package com.jyu.commonUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
public class CommonUtil {

	/**
	 * 要求把得到pic的地址对应的bitmap然后再分别base64转换存到String中然后还是以逗号隔开
	 * @param pic 以逗号隔开的一个记录本地图片地址的字符串
	 * @return
	 */
	public static String processPic(String pic) {
		FileInputStream fis = null;
		String result = "";
		try {
			String[] temp = pic.split(",");
			for(int i = 0;i<temp.length;i++){
				String path = temp[i];
//				Log.i("wang", " temp[i]=="+ temp[i]);
				fis = new FileInputStream(path);
				Bitmap bitmap = BitmapFactory.decodeStream(fis);
				String r = getBitmapString(bitmap);
				if(i==0){					
					result = r;
				}else{
					result+=","+r;
				}
			}
			
			fis.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getBitmapString(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
		byte[] b = stream.toByteArray();
		// 将图片流以字符串形式存储下来
		String file = new String(Base64Coder.encodeLines(b));
		return file;
		
	}
	
	/** 
     * 得到本地或者网络上的bitmap url - 网络或者本地图片的绝对路径,比如: 
     *  
     * A.网络路径: url="http://blog.foreverlove.us/girl2.png" ; 
     *  
     * B.本地路径:url="file://mnt/sdcard/photo/image.png"; 
     *  
     * C.支持的图片格式 ,png, jpg,bmp,gif等等 
     *  
     * @param url 
     * @return 
     */  
    public static Bitmap GetLocalOrNetBitmap(String url)  
    {  
        Bitmap bitmap = null;  
        InputStream in = null;  
        BufferedOutputStream out = null;  
        try  
        {  
            in = new BufferedInputStream(new URL(url).openStream(), 2*1024);  
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();  
            out = new BufferedOutputStream(dataStream,2*1024);  
            copy(in, out);  
            out.flush();  
            byte[] data = dataStream.toByteArray();  
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);  
            data = null;  
            return bitmap;  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
            return null;  
        }  
    }  
    
    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[2*1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }
	
	public static void showInfoDialog(Context context, String message) {
		showInfoDialog(context, message, "提示", "确定", null);
	}

	public static void showInfoDialog(Context context, String message,
			String titleStr, String positiveStr,
			DialogInterface.OnClickListener onClickListener) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
		localBuilder.setTitle(titleStr);
		localBuilder.setMessage(message);
		if (onClickListener == null)
			onClickListener = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

				}
			};
		localBuilder.setPositiveButton(positiveStr, onClickListener);
		localBuilder.show();
	}

	public static String md5(String paramString) {
		String returnStr;
		try {
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(paramString.getBytes());
			returnStr = byteToHexString(localMessageDigest.digest());
			return returnStr;
		} catch (Exception e) {
			return paramString;
		}
	}

	/**
	 * 将指定byte数组转换成16进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byteToHexString(byte[] b) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString.append(hex.toUpperCase());
		}
		return hexString.toString();
	}

	/**
	 * 判断当前是否有可用的网络以及网络类型 0：无网络 1：WIFI 2：CMWAP 3：CMNET
	 * 
	 * @param context
	 * @return
	 */
	public static int isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return 0;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						NetworkInfo netWorkInfo = info[i];
						if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
							return 1;
						} else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
							String extraInfo = netWorkInfo.getExtraInfo();
							if ("cmwap".equalsIgnoreCase(extraInfo)
									|| "cmwap:gsm".equalsIgnoreCase(extraInfo)) {
								return 2;
							}
							return 3;
						}
					}
				}
			}
		}
		return 0;
	}

	/**
	 * 获取现在时间
	 * 
	 * @return 返回短时间字符串格式yyyy-MM-dd HH:mm:ss
	 */

	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int getScreenPicHeight(int screenWidth, Bitmap bitmap) {
		int picWidth = bitmap.getWidth();
		int picHeight = bitmap.getHeight();
		int picScreenHeight = 0;
		picScreenHeight = (screenWidth * picHeight) / picWidth;
		return picScreenHeight;
	}

	/**
	 * 
	 * @param context
	 * @param button
	 * @param nornalImageFileName
	 * @param pressedImageFileName
	 * @throws Exception
	 */
	public static void bindViewSelector(Context context, final View view,
			String nornalImageurl, final String pressedImageUrl) {
		final StateListDrawable stateListDrawable = new StateListDrawable();
		final BitmapUtils utils = new BitmapUtils(context);
		utils.display(view, nornalImageurl, new BitmapLoadCallBack<View>() {

			@Override
			public void onLoadCompleted(View container, String uri,
					Bitmap bitmap, BitmapDisplayConfig config,
					BitmapLoadFrom from) {
				@SuppressWarnings("deprecation")
				Drawable normalDrawable = new BitmapDrawable(bitmap);
				stateListDrawable.addState(
						new int[] { android.R.attr.state_active },
						normalDrawable);
				stateListDrawable.addState(new int[] {
						android.R.attr.state_focused,
						android.R.attr.state_enabled }, normalDrawable);
				stateListDrawable.addState(
						new int[] { android.R.attr.state_enabled },
						normalDrawable);
				utils.display(container, pressedImageUrl,
						new BitmapLoadCallBack<View>() {

							@SuppressWarnings("deprecation")
							@Override
							public void onLoadCompleted(View container,
									String uri, Bitmap bitmap,
									BitmapDisplayConfig config,
									BitmapLoadFrom from) {
								stateListDrawable.addState(new int[] {
										android.R.attr.state_pressed,
										android.R.attr.state_enabled },
										new BitmapDrawable(bitmap));

								view.setBackgroundDrawable(stateListDrawable);

							}

							@Override
							public void onLoadFailed(View container,
									String uri, Drawable drawable) {
								// TODO Auto-generated method stub

							}
						});
			}

			@Override
			public void onLoadFailed(View container, String uri,
					Drawable drawable) {

			}
		});

	}

	@SuppressWarnings("deprecation")
	private static Drawable createDrawable(Drawable d, Paint p) {

		BitmapDrawable bd = (BitmapDrawable) d;
		Bitmap b = bd.getBitmap();
		Bitmap bitmap = Bitmap.createBitmap(bd.getIntrinsicWidth(),
				bd.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(b, 0, 0, p); // 关键代码，使用新的Paint画原图，

		return new BitmapDrawable(bitmap);
	}

	/** 设置Selector。 本次只增加点击变暗的效果，注释的代码为更多的效果 */
	public static StateListDrawable createSLD(Context context, Drawable drawable) {
		StateListDrawable bg = new StateListDrawable();
		int brightness = 50 - 127;
		ColorMatrix cMatrix = new ColorMatrix();
		cMatrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0,
				brightness,// 改变亮度
				0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });

		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

		Drawable normal = drawable;
		Drawable pressed = createDrawable(drawable, paint);
		bg.addState(new int[] { android.R.attr.state_pressed, }, pressed);
		bg.addState(new int[] { android.R.attr.state_focused, }, pressed);
		bg.addState(new int[] { android.R.attr.state_selected }, pressed);
		bg.addState(new int[] {}, normal);
		return bg;
	}

	public static Bitmap getImageFromAssetsFile(Context ct, String fileName) {
		Bitmap image = null;
		AssetManager am = ct.getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;

	}

//	public static <Params, Progress, Result> void executeAsyncTask(
//			AsyncTask<Params, Progress, Result> task, Params... params) {
//		if (Build.VERSION.SDK_INT >= 11) {
//			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
//		} else {
//			task.execute(params);
//		}
//	}

	public static String getUploadtime(long created) {
		StringBuffer when = new StringBuffer();
		int difference_seconds;
		int difference_minutes;
		int difference_hours;
		int difference_days;
		int difference_months;
		long curTime = System.currentTimeMillis();
		difference_months = (int) (((curTime / 2592000) % 12) - ((created / 2592000) % 12));
		if (difference_months > 0) {
			when.append(difference_months + "月");
		}

		difference_days = (int) (((curTime / 86400) % 30) - ((created / 86400) % 30));
		if (difference_days > 0) {
			when.append(difference_days + "天");
		}

		difference_hours = (int) (((curTime / 3600) % 24) - ((created / 3600) % 24));
		if (difference_hours > 0) {
			when.append(difference_hours + "小时");
		}

		difference_minutes = (int) (((curTime / 60) % 60) - ((created / 60) % 60));
		if (difference_minutes > 0) {
			when.append(difference_minutes + "分钟");
		}

		difference_seconds = (int) ((curTime % 60) - (created % 60));
		if (difference_seconds > 0) {
			when.append(difference_seconds + "秒");
		}

		return when.append("前").toString();
	}

	// public static boolean hasToken(Context ct) {
	// String token = SharePrefUtil.getString(ct, "token", "");
	// if (TextUtils.isEmpty(token)) {
	// return false;
	// } else {
	// return true;
	// }
	// }

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	@SuppressLint("SimpleDateFormat")
	public static String formatDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(new Date());
	}
	
	/** 
	    * java反射bean的get方法 
	    *  
	    * @param objectClass 
	    * @param fieldName 
	    * @return 
	    */  
	   public static Method getGetMethod(Class<?> objectClass, String fieldName) {  
	       StringBuffer sb = new StringBuffer();  
	       sb.append("get");  
	       sb.append(fieldName.substring(0, 1).toUpperCase());  
	       sb.append(fieldName.substring(1));  
	       try {  
	           return objectClass.getMethod(sb.toString());  
	       } catch (Exception e) {  
	       }  
	       return null;  
	   }  
	   
	   /** 
	     * java反射bean的set方法 
	     *  
	     * @param objectClass 
	     * @param fieldName 
	     * @return 
	     */  
	    public static Method getSetMethod(Class<?> objectClass, String fieldName) {  
	        try {  
	            @SuppressWarnings("rawtypes")
				Class[] parameterTypes = new Class[1];  
	            Field field = objectClass.getDeclaredField(fieldName);  
	            parameterTypes[0] = field.getType();  
	            StringBuffer sb = new StringBuffer();  
	            sb.append("set");  
	            sb.append(fieldName.substring(0, 1).toUpperCase());  
	            sb.append(fieldName.substring(1));  
	            Method method = objectClass.getMethod(sb.toString(), parameterTypes);  
	            return method;  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return null;  
	    }  
	    
	    /** 
	     * 执行set方法 
	     *  
	     * @param o 执行对象 
	     * @param fieldName 属性 
	     * @param value 值 
	     */  
	    public static void invokeSet(Object o, String fieldName, Object value) {  
	        Method method = getSetMethod(o.getClass(), fieldName);  
	        try {  
	            method.invoke(o, new Object[] { value });  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }  
	  
	    /** 
	     * 执行get方法 
	     *  
	     * @param o 执行对象 
	     * @param fieldName 属性 
	     */  
	    public static Object invokeGet(Object o, String fieldName) {  
	        Method method = getGetMethod(o.getClass(), fieldName);  
	        try {  
	            return method.invoke(o, new Object[0]);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return null;  
	    }  

}
