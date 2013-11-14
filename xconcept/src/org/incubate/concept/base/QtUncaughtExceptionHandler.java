package org.incubate.concept.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.widget.Toast;

public class QtUncaughtExceptionHandler implements UncaughtExceptionHandler
{	
	// CrashHandler实例
	private static QtUncaughtExceptionHandler	instance = null;
	// 程序的Context对象
	private Context	mContext;
	// 用来存储设备信息和异常信息
	private Map<String, String>	infos;
	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler		mDefaultHandler;
	// crash文件保存目录
	private String	dirForCrashFile	= null;
	
	public static QtUncaughtExceptionHandler getInstance ()
	{
		if (instance == null) {
			instance = new QtUncaughtExceptionHandler();
		}
		return instance;
	}

	private QtUncaughtExceptionHandler ()
	{
		StringBuffer path = new StringBuffer(Environment.getExternalStorageDirectory().getAbsolutePath());
		path.append("/Tencent/Qtl/crash/");
		dirForCrashFile = path.toString();
		infos= new HashMap<String, String>();
	}

	public void init (Context context)
	{
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		// 收集设备参数信息
		collectDeviceInfo();
	}

	public void uncaughtException (Thread thread, Throwable ex)
	{
		if (!handleException(ex) && mDefaultHandler != null)
		{
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		}
		else
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
			}
			// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException (Throwable ex)
	{
		if (ex == null)
		{
			return false;
		}
		// 使用Toast来显示异常信息
		new Thread()
		{
			@Override
			public void run ()
			{
				Looper.prepare();
				Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出。请不要删除crash文件，联系asherchen、devilxie、lostjin", Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
		
		// 保存日志文件
		saveCrashInfo2File(ex);
		return true;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo ()
	{
		try
		{
			PackageManager pm = mContext.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null)
			{
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		}
		catch (NameNotFoundException e)
		{
		}
		
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields)
		{
			try
			{
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
			}
			catch (Exception e)
			{
			}
		}
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private boolean saveCrashInfo2File (Throwable ex)
	{
		String path = "/data/data/com.tencent.qt.qtl/QTL/";
		// SD卡未挂载，保存失败
//		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
//		{
//			return false;
//		}
		// 检查目录是否存在，不存在则创建
		//File dir = new File(dirForCrashFile);
		File dir = new File(path);
		if (!dir.exists() && !dir.mkdirs())
		{
			return false;
		}
		
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);

		for (Map.Entry<String, String> entry : infos.entrySet())
		{
			String key = entry.getKey();
			String value = entry.getValue();
			printWriter.write(key);
			printWriter.write("=");
			printWriter.write(value);
			printWriter.write("\n");			
		}
		
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null)
		{
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		
		printWriter.close();		
		
		// 用于格式化日期,作为日志文件名的一部分
		String fileName = "crash.log";
		FileOutputStream fos = null;
		try
		{			
			fos = new FileOutputStream(path + fileName);
			fos.write(writer.toString().getBytes());
			return true;
		}
		catch (Exception e)
		{
			return false;
		} finally {
			if (fos != null) {
				try
				{
					fos.close();
				}
				catch (IOException e)
				{
				}
			}
		}	
		
	}
}
