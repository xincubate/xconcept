package org.incubate.concept.activity.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.IBinder;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;


public class UIUtil
{
	/***
	 * 手机震动
	 */
	public static void vibrate(Context context, long milliseconds)
	{
		Vibrator vb = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
		vb.vibrate(milliseconds);
	}

	/**
	 * 隐藏键盘
	 * @param context 上下文
	 * @param binder 键盘绑定
	 */
	public static void hideKeyboard(Context context, IBinder binder) 
	{
        InputMethodManager m = (InputMethodManager) 
        		context.getSystemService(Context.INPUT_METHOD_SERVICE);
        IBinder localIBinder = binder;
        m.hideSoftInputFromWindow(localIBinder, 0);
    }
	
	public static View createView(Context context, int layout) 
	{
		LayoutInflater	inflater = (LayoutInflater) 
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
		View view = inflater.inflate(layout, null);
		return view;
	}
	
	public static boolean enable_toast_visible = true;
	private static android.widget.Toast app_toast = null; 
	/**
	 * 默认样式的提�?
	 * 
	 * @param context
	 * @param str	显示的内容
	 * @param longToast 长时间显示还是短时间显示
	 */
	public static void showToast(Context context, CharSequence text, boolean longToast) {	
		if (!enable_toast_visible) {
		    return;
		}

		if (app_toast != null  && app_toast.getView() != null) {
			app_toast.setText(text);
            app_toast.setDuration(longToast ? Toast.LENGTH_LONG:Toast.LENGTH_SHORT);
            app_toast.show();
            return;		    
		}
		
		app_toast = null;
		Toast toast = new Toast(context);
		toast.setText(text);
		toast.setDuration(longToast ? Toast.LENGTH_LONG: Toast.LENGTH_SHORT);
		app_toast = toast;
		toast.show();
	}
	
	/**
	 * 自定义样式的提示
	 * 
	 * @param context
	 * @param textId 资源id
	 * @param longToast 长时间显示还是短时间显示
	 */
	public static void showToast(Context context, int textId, boolean longToast) 
	{
	    if (!enable_toast_visible) 
	    {
            return;
        }
		CharSequence text = context.getResources().getText(textId);
		showToast(context, text, longToast);
	}


	public static AlertDialog showDialog(Context context, OnClickListener dialogOnClickListener, 
			int titleId, int positiveButtonId, 
			int neutralButtonId, int negativeButtonId, int message) 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (titleId > 0) 
	        builder.setTitle(titleId);
		
		if (positiveButtonId > 0) {
			builder.setPositiveButton(positiveButtonId, dialogOnClickListener);
		}
		if (neutralButtonId > 0) {
			builder.setNeutralButton(neutralButtonId, dialogOnClickListener);
		}
		if (negativeButtonId > 0) {
			builder.setNegativeButton(negativeButtonId, dialogOnClickListener);
		}
		builder.setMessage(message);
		try {
		    AlertDialog dialog = builder.show();
	        dialog.setCanceledOnTouchOutside(true);
	        return dialog;
        } catch (Exception e) {
            return null;
        }
	}
	
	/**
	 * 显示三按钮消息对话框
	 * @param context UI上下�?
	 * @param dialogOnClickListener 对话框按钮监听器
	 * @param titleId 标题ID，小�?无效，将不显示标�?
	 * @param positiveButtonId 第一个按钮标题ID，小�?无效，将不显示该按钮
	 * @param neutralButtonId 第二个按钮标题ID，小�?无效，将不显示该按钮
	 * @param negativeButtonId 第三个按钮标题ID，小�?无效，将不显示该按钮
	 * @param message 消息文本
	 * @return 返回对话�?
	 */
	public static AlertDialog showDialog(Context context, OnClickListener dialogOnClickListener, 
			int titleId, int positiveButtonId, 
            int neutralButtonId, int negativeButtonId, CharSequence message) 
	{
	    AlertDialog.Builder builder = new AlertDialog.Builder(context);    
	    
	    if (titleId > 0) 
	        builder.setTitle(titleId);
	    
	    if (positiveButtonId > 0) {
	        builder.setPositiveButton(positiveButtonId, dialogOnClickListener);
	    }
	    if (neutralButtonId > 0) {
	        builder.setNeutralButton(neutralButtonId, dialogOnClickListener);
	    }
	    if (negativeButtonId > 0) {
	        builder.setNegativeButton(negativeButtonId, dialogOnClickListener);
	    }
	    
	    builder.setMessage(message);
	    try {
	        AlertDialog dialog = builder.show();
	        dialog.setCanceledOnTouchOutside(true);
	        return dialog;
	    } catch (Exception e) {
	        return null;
	    }
	    
	}
	
	/**
	 * 显示三按钮自定义的消息对话框
	 * @param context UI上下�?
	 * @param listener 对话框监听器
	 * @param titleId 标题ID，小�?无效，将不显示标�?
	 * @param positiveButtonId 第一个按钮标题ID，小�?无效，将不显示该按钮
	 * @param neutralButtonId 第二个按钮标题ID，小�?无效，将不显示该按钮
	 * @param negativeButtonId 第三个按钮标题ID，小�?无效，将不显示该按钮
	 * @param view 自定义视�?
	 * @return 返回创建的对话框
	 */
	public static AlertDialog showDialog(Context context, OnClickListener listener, 
			int titleId, int positiveButtonId, 
            int neutralButtonId, int negativeButtonId, View view) {
	    
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (titleId > 0)
            builder.setTitle(titleId);
        
        if (positiveButtonId > 0 ) {
            builder.setPositiveButton(positiveButtonId, listener);
        }
        if (neutralButtonId > 0) {
            builder.setNeutralButton(neutralButtonId, listener);
        }
        if (negativeButtonId > 0) {
            builder.setNegativeButton(negativeButtonId, listener);
        }
        builder.setView(view);
        try {
            AlertDialog dialog = builder.show();
            dialog.setCanceledOnTouchOutside(true);
            return dialog;
        } catch (Exception e) {
            return null;
        }        
    }
	
	/**
	 * 显示三按钮自定义的消息对话框
	 * @param context UI上下�?
	 * @param listener 对话框监听器
	 * @param title 标题，null将不显示标题
	 * @param positiveButtonId 第一个按钮标题ID，小�?无效，将不显示该按钮
	 * @param neutralButtonId 第二个按钮标题ID，小�?无效，将不显示该按钮
	 * @param negativeButtonId 第三个按钮标题ID，小�?无效，将不显示该按钮
	 * @param view 自定义视�?
	 * @return 返回创建的对话框
	 */
	public static AlertDialog showDialog(Context context, OnClickListener listener, 
			CharSequence title, int positiveButtonId, 
			int neutralButtonId, int negativeButtonId, View view) 
	{    
		AlertDialog.Builder builder = new AlertDialog.Builder(context);		
		if (!TextUtils.isEmpty(title)) 
		    builder.setTitle(title);
		    
		if (positiveButtonId > 0) {
			builder.setPositiveButton(positiveButtonId, listener);
		}
		if (neutralButtonId > 0) {
			builder.setNeutralButton(neutralButtonId, listener);
		}
		if (negativeButtonId > 0) {
			builder.setNegativeButton(negativeButtonId, listener);
		}
		builder.setView(view);
		try {
		    AlertDialog dialog = builder.show();
	        dialog.setCanceledOnTouchOutside(true);
	        return dialog;
	    } catch (Exception e) {
	        return null;
	    }	    	
	}
	
	/**
	 * 显示三按钮自定义的消息对话框
	 * @param context UI上下�?
	 * @param listener 对话框监听器，可为null
	 * @param title 自定义标题视图，null将不显示标题
	 * @param positiveButtonId 第一个按钮标题ID，小�?无效，将不显示该按钮
	 * @param neutralButtonId 第二个按钮标题ID，小�?无效，将不显示该按钮
	 * @param negativeButtonId 第三个按钮标题ID，小�?无效，将不显示该按钮
	 * @param view 自定义视�?
	 * @return 返回创建的对话框
	 */
	public static AlertDialog showDialog(Context context, OnClickListener listener, 
			View title, int positiveButtonId,
	        int neutralButtonId, int negativeButtonId, View view) 
	{
	    AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    builder.setCustomTitle(title);
        if (positiveButtonId > 0) {
            builder.setPositiveButton(positiveButtonId, listener);
        }
        if (neutralButtonId > 0) {
            builder.setNeutralButton(neutralButtonId, listener);
        }
        if (negativeButtonId > 0) {
            builder.setNegativeButton(negativeButtonId, listener);
        }
        builder.setView(view); 
        try {
            AlertDialog dialog = builder.show();
            dialog.setCanceledOnTouchOutside(true);
            return dialog; 
        } catch (Exception e) {
            return null;
        }               
	}
	
	
	/**
	 * 显示选项对话�?
	 * @param context 上下�?
	 * @param listener 选择监听�?
	 * @param title 标题
	 * @param items 选项
	 * @param negativeButtonId 第三按钮标题
	 * @return
	 */
	public static AlertDialog showOptionDialog(Context context, OnClickListener listener, 
			CharSequence title,
			CharSequence[] items, int negativeButtonId)
	{
		Builder builder = new AlertDialog.Builder(context);
		if (title != null)
			builder.setTitle(title);
		
		if (negativeButtonId > 0)
		{
			builder.setNegativeButton(negativeButtonId, null);
		}
		
		builder.setItems(items, listener);
		return builder.show();
	}
	
	/**
	 * 显示三按钮消息对话框
	 * @param context UI上下�?
	 * @param dialogOnClickListener 对话框按钮监听器
	 * @param titleId 标题ID，小�?无效，将不显示标�?
	 * @param positiveButtonText 第一个按钮标题，等于null无效，将不显示该按钮
	 * @param neutralButtonText 第二个按钮标题，等于null无效，将不显示该按钮
	 * @param negativeButtonText 第三个按钮标题，等于null无效，将不显示该按钮
	 * @param message 消息文本
	 * @return 返回对话�?
	 */
	public static AlertDialog showDialog(Context context, OnClickListener dialogOnClickListener, 
			CharSequence title, CharSequence positiveButtonText, 
			CharSequence neutralButtonText, CharSequence negativeButtonText, CharSequence message) 
	{
	    AlertDialog.Builder builder = new AlertDialog.Builder(context);    
	    
	    if (title != null) 
	        builder.setTitle(title);
	    
	    if (positiveButtonText !=null) {
	        builder.setPositiveButton(positiveButtonText, dialogOnClickListener);
	    }
	    if (neutralButtonText !=null) {
	        builder.setNeutralButton(neutralButtonText, dialogOnClickListener);
	    }
	    if (negativeButtonText !=null) {
	        builder.setNegativeButton(negativeButtonText, dialogOnClickListener);
	    }
	    
	    builder.setMessage(message);
	    try {
	        AlertDialog dialog = builder.show();
	        dialog.setCanceledOnTouchOutside(false);
	        return dialog;
	    } catch (Exception e) {
	        return null;
	    }
	    
	}
	
	/**
	 * 显示三按钮消息对话框
	 * @param context UI上下�?
	 * @param title 标题，为null无效，将不显示标�?
	 * @param positiveButtonText 第一个按钮标题，等于null无效，将不显示该按钮
	 * @param neutralButtonText 第二个按钮标题，等于null无效，将不显示该按钮
	 * @param negativeButtonText 第三个按钮标题，等于null无效，将不显示该按钮
	 * @param message 消息文本
	 * @return 返回对话�?
	 */
	public static AlertDialog showDialog(Context context,CharSequence title, CharSequence positiveButtonText, 
			CharSequence neutralButtonText, CharSequence negativeButtonText, CharSequence message) 
	{
		DialogInterface.OnClickListener listener = new OnClickListener()
		{
			public void onClick (DialogInterface dialog, int which)
			{
								
			}
		};
		
	    AlertDialog.Builder builder = new AlertDialog.Builder(context);    
	    
	    if (title != null) 
	        builder.setTitle(title);
	    
	    if (positiveButtonText !=null) {
	        builder.setPositiveButton(positiveButtonText, listener);
	    }
	    if (neutralButtonText !=null) {
	        builder.setNeutralButton(neutralButtonText, listener);
	    }
	    if (negativeButtonText !=null) {
	        builder.setNegativeButton(negativeButtonText, listener);
	    }
	    
	    builder.setMessage(message);
	    try {
	        AlertDialog dialog = builder.show();
	        dialog.setCanceledOnTouchOutside(false);
	        return dialog;
	    } catch (Exception e) {
	        return null;
	    }
	    
	}
	
	/**
	 * 显示单按钮（确定）消息对话框
	 * @param context UI上下�?
	 * @param title 标题，为null无效，将不显示标�?
	 * @param message 消息文本
	 * @return 返回对话�?
	 */
	public static AlertDialog showDialog(Context context,CharSequence title, CharSequence message) 
	{
		DialogInterface.OnClickListener listener = new OnClickListener()
		{
			public void onClick (DialogInterface dialog, int which)
			{
								
			}
		};
		
	    AlertDialog.Builder builder = new AlertDialog.Builder(context);    
	    
	    if (title != null) 
	        builder.setTitle(title);
	    
	    builder.setPositiveButton("确定", listener);
	    
	    builder.setMessage(message);
	    try {
	        AlertDialog dialog = builder.show();
	        dialog.setCanceledOnTouchOutside(false);
	        return dialog;
	    } catch (Exception e) {
	        return null;
	    }
	    
	}
	
	public static Drawable tilefy(Context context, Drawable drawable, boolean clip){
		Bitmap mSampleTile = null;
        if (drawable instanceof LayerDrawable) {
            LayerDrawable background = (LayerDrawable) drawable;
            final int N = background.getNumberOfLayers();
            Drawable[] outDrawables = new Drawable[N];
            
            for (int i = 0; i < N; i++) {
                int id = background.getId(i);
                outDrawables[i] = tilefy(context, background.getDrawable(i),
                        (id == android.R.id.progress || id == android.R.id.secondaryProgress));
            }

            LayerDrawable newBg = new LayerDrawable(outDrawables);
            
            for (int i = 0; i < N; i++) {
                newBg.setId(i, background.getId(i));
            }
            
            return newBg;
            
        }else if (drawable instanceof BitmapDrawable) {
        	((BitmapDrawable) drawable).setTargetDensity(context.getResources().getDisplayMetrics());
            final Bitmap tileBitmap = ((BitmapDrawable) drawable).getBitmap();
            if (mSampleTile == null) {
                mSampleTile = tileBitmap;
            }
            final ShapeDrawable shapeDrawable = new ShapeDrawable(getDrawableShape());
            final BitmapShader bitmapShader = new BitmapShader(tileBitmap,
                    Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
            shapeDrawable.getPaint().setShader(bitmapShader);

            return (clip) ? new ClipDrawable(shapeDrawable, Gravity.LEFT,
                    ClipDrawable.HORIZONTAL) : shapeDrawable;
        }
        return drawable;
	}
	
	static Shape getDrawableShape() 
	{
        final float[] roundedCorners = new float[] { 5, 5, 5, 5, 5, 5, 5, 5 };
        return new RoundRectShape(roundedCorners, null, null);
    }
}
