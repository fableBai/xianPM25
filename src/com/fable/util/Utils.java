package com.fable.util;



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;

import org.jsoup.select.Elements;


public final class Utils {

    private static final Logger LOG =  Logger.getAnonymousLogger();
    static final String DOWNLOAD_DIR = "download";
    private static final String TAG = "aipnUtils";
    public Utils() {
    	Log.d(TAG,"utils constration!");
    	
    }
    public static void SetTextInfo(View view,int ResId,String text)
    {
    	TextView textView = (TextView)view.findViewById(ResId);
    	textView.setText(text);
    }
    public static void showErrMessage(final Activity ctx, final Exception e) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(true).setMessage("error");

        AlertDialog dlg = builder.create();
        dlg.setOwnerActivity(ctx);
        dlg.show();
    }
    public static void FileDel(String fileName)
	{
    	File delFile = new File(fileName);
		delFile.delete();
	}
    public static String LoadUrlData(String Url) {
    	  StringBuffer sBuffer = new StringBuffer();
    	  try {
    	   URL url = new URL(Url);
    	   InputStreamReader inReader = new InputStreamReader(url.openStream(), "utf-8");
    	   BufferedReader bufferedReader = new BufferedReader(inReader);
    	   while (bufferedReader.ready()) {

    	    sBuffer.append(bufferedReader.readLine());
    	   }

    	   bufferedReader.close();
    	   inReader.close();
    	  } catch (MalformedURLException e) {
    	   e.printStackTrace();
    	  } catch (IOException e) {
    	   e.printStackTrace();
    	  }
    	  System.out.println(sBuffer.toString());

    	  return sBuffer.toString();
    	 }
    private static String getDocument(File html) { 
        String text = ""; 
        try { 
            // 设置编码集 
            org.jsoup.nodes.Document doc = Jsoup.parse(html, "UTF-8"); 
            // 提取标题信息 
            Elements title = doc.select("title"); 
            for (org.jsoup.nodes.Element link : title) { 
                text += link.text() + " "; 
            } 
            // 提取table中的文本信息 
            Elements links = doc.select("table"); 
            for (org.jsoup.nodes.Element link : links) { 
                text += link.text() + " "; 
            } 
            // 提取div中的文本信息 
            Elements divs = doc.select("div[class=post]"); 
            for (org.jsoup.nodes.Element link : divs) { 
                text += link.text() + " "; 
            } 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
 
        return text; 
    }
    @TargetApi(9)
	public static void setFileWriteable(String fileName)
	{
    	File File = new File(fileName);
    	File.setWritable(false, false);
	}
	public static void WriteFileData(String fileName, InputStream message, int filesize)
	{
		try{
			FileDel(fileName);
		  byte[] bytes = new byte[filesize];

		  if(message.read(bytes, 0, filesize) == -1){
			  Log.d(TAG,"message.read error ");
			  return;
		  }
		  for(int i=0;i<10;i++)
			  System.out.print(bytes[i]+",");
		  FileOutputStream fout = new FileOutputStream(fileName);
		  fout.write(bytes);
		  fout.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.d(TAG,"writeFileData  FileNotFoundException");
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d(TAG,"writeFileData  IOException error ");
			e.printStackTrace();
		}
	}
    /**
     * 删除证书文件
     * 
     * @param ctx context
     * @param resId text resource id
     * @param formatArgs arguments for formatting the message
     */
    public static  int delCertFileTo560(String dir, String Name)
	 {
		File root = Environment.getExternalStorageDirectory();
		File delFile = new File(root.getPath()+ "/"+ Name);
		if(!delFile.canRead() || !delFile.canWrite())
			Log.d(TAG,"delfile is not to read ");
		try {
			InputStream input =  new BufferedInputStream(new FileInputStream(delFile));
			Log.d(TAG,"del name "+root.getPath() + "/" + Name);
			WriteFileData(dir, input, 564);
			input.close();
			delFile.delete();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d(TAG,"writeFileData  IOException error ");
			e.printStackTrace();
		}
		return 0;
	 }
    /**
     * 备份文件到另一个目录
     * 
     * @param ctx context
     * @param resId text resource id
     * @param formatArgs arguments for formatting the message
     */
    public static void doEncodeTo(final File dir, final String name) throws Exception {
    	String PathName = dir.getAbsolutePath()+"/"+name;
        InputStream in = new FileInputStream(new File(Environment.getExternalStorageDirectory(), name));
        Log.d(TAG,"dobackup:" +PathName.substring(0, PathName.indexOf(".ini")) + ".cer" );
        //StreamCrypto.SMS4encrypt(in, PathName.substring(0, PathName.indexOf(".ini")) + ".cer");
        
        setFileWriteable(PathName);
    }

    public static void doDecodeTo(final File dir, final String CertDir, final String name) throws Exception {
    	String PathName = dir.getAbsolutePath()+"/"+name;
        InputStream in = new FileInputStream(new File( Utils.ensureDir(CertDir), name));
        Log.d(TAG,"doDecode:" +PathName.substring(0, PathName.indexOf(".cer")) + ".ini" );
       
    }
    /**
     * 显示一个短时间的Toast提示信息(duration为short).
     * 
     * @param ctx context
     * @param resId text resource id
     * @param formatArgs arguments for formatting the message
     */
    public static void showToast(final Context ctx, final int resId, final Object... formatArgs)
    {
        Toast.makeText(ctx, ctx.getString(resId, formatArgs), Toast.LENGTH_SHORT).show();
    }
	 public static List<File> getAllCertFiles(String dir, String Filter)
	 {
	        List<File> allFiles = new ArrayList<File>();
	        File root = Utils.ensureDir(dir);

	        File download = new File(root, DOWNLOAD_DIR);
	        if (download != null) {
	            File[] files = download.listFiles( Utils.getFileExtensionFilter(Filter));
	            if (files != null) {
	                Collections.addAll(allFiles, files);
	            }
	        }

	        File[] files = root.listFiles( Utils.getFileExtensionFilter(Filter));
	        if (files != null) {
	            Collections.addAll(allFiles, files);
	        }

	        return allFiles;
	 }
	 public static FilenameFilter getFileExtensionFilter(String extension)
	 {   
		    final String _extension = extension;   
		    return new FilenameFilter() {   
		        public boolean accept(File file, String name) {   
		            boolean ret = name.endsWith(_extension);    
		            return ret;   
		        }   
		    };   
	 }
		public void writeFileData(String fileName, InputStream message, int filesize)
		{
			try{

			  byte[] bytes = new byte[filesize];

			  if(message.read(bytes, 0, filesize) == -1){
				  Log.d(TAG,"message.read error ");
				  return;
			  }
			  for(int i=0;i<10;i++)
				  System.out.print(bytes[i]+",");
			  FileOutputStream fout = new FileOutputStream(fileName);
			  fout.write(bytes);
			  
			  fout.close();
			}
			catch (Exception e){
				Log.d(TAG,"writeFileData error ");
				e.printStackTrace();
			}
		}
		public static boolean fileExists(final String path)
		{
			if(path.length() == 0)
				return false;
			Log.d(TAG, "fileExists:" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/aipn/keystore/" + path);
			File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/aipn/keystore/" + path);
			return dir.exists();
		}
		public static File ensureDir(final String path)
		{
			Log.d(TAG, "ensureDir:" + path);
			File dir = new File(path);
			ensureDir(dir);

			return dir;
		}
    /**
    * 创建目录
    * @param destDirName 目标目录名
    * @return 目录创建成功返回true，否则返回false
    */
    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if(dir.exists()) {
         System.out.println("创建目录" + destDirName + "失败，目标目录已存在！");
         return false;
        }
        if(!destDirName.endsWith(File.separator))
         destDirName = destDirName + File.separator;
        // 创建单个目录
        if(dir.mkdirs()) {
         System.out.println("创建目录" + destDirName + "成功！");
         return true;
        } else {
         System.out.println("创建目录" + destDirName + "成功！");
         return false;
        }
    }
    public static boolean existsDir(String destDirName) {
        File dir = new File(destDirName);
        if(dir.exists()) {
         System.out.println("创建目录" + destDirName + "失败，目标目录已存在！");
         return true;
        }
        return false;
    }
    public static boolean CreateFile(String destFileName)
    {
        File file = new File(destFileName);
        if (file.exists()) {
         System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");
         return false;
        }
        if (destFileName.endsWith(File.separator)) {
         System.out.println("创建单个文件" + destFileName + "失败，目标不能是目录！");
         return false;
        }
        if (!file.getParentFile().exists()) {
         System.out.println("目标文件所在路径不存在，准备创建。。。");
         if (!file.getParentFile().mkdirs()) {
          System.out.println("创建目录文件所在的目录失败！");
          return false;
         }
        }

        // 创建目标文件
        try {
         if (file.createNewFile()) {
          System.out.println("创建单个文件" + destFileName + "成功！");
          return true;
         } else {
          System.out.println("创建单个文件" + destFileName + "失败！");
          return false;
         }
        } catch (IOException e) {
         e.printStackTrace();
         System.out.println("创建单个文件" + destFileName + "失败！");
         return false;
        }
    }
    public static void ensureDir(final File dir)
    {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state))
			try {
				throw new Exception("no writable storage found, state=" + state);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (!dir.exists())
			try {
				throw new Exception("failed to mkdir: " + dir);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }

    

 
    
    /*
     * 检查ip地址是否符合 61.185.224.17:655 等样式
     */
    public static boolean ipValid(String s)
    {
    	String regex2 = 
    			 "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
    			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
    			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." 
    			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\x3a"
    			+ "(\\d{1,5})$";
    	return s.matches(regex2);
    }
 


    //
    // *****************************************************************************************************
    //
    // Preferences Utilities
    //
    // *****************************************************************************************************
    //

    /**
     * Retrieves a boolean from default SharedPreferences
     * 
     * @param key resource defines the preference key
     * @param defaultValue resource defines the default value
     * @param ctx Context
     * @return boolean preference
     */
    public static boolean getPrefBool(final int key, final int defaultValue, final Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);

        String prefKey = ctx.getString(key);
        boolean prefDefault = ctx.getResources().getBoolean(defaultValue);

        return sp.getBoolean(prefKey, prefDefault);
    }

    public static boolean checkNetworkInfo(Context mContext)
    {
		ConnectivityManager manager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);  
		
		if (manager == null){  
		    return false;  
		}  
		 
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();  
		 
		if (networkinfo == null || !networkinfo.isAvailable() || State.CONNECTED != networkinfo.getState()){  
		    return false;  
		}  
		return true;
    }

    /**
     * 用来判断服务是否运行.
     * @param context
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context mContext,String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
        mContext.getSystemService(Context.ACTIVITY_SERVICE); 
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
       if (!(serviceList.size()>0)) {
            return false;
        }
        for (int i=0; i<serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
    /**
     * Retrieves an integer from default SharedPreferences
     * 
     * @param key resource defines the preference key
     * @param defaultValue resource defines the default value
     * @param ctx Context
     * @return int preference
     */
    public static int getPrefInt(final int key, final int defaultValue, final Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);

        String prefKey = ctx.getString(key);
        int value = ctx.getResources().getInteger(defaultValue);

        try {
            // 在xml中定义的数字型preference只能按字符串读取
            value = Integer.parseInt(sp.getString(prefKey, String.valueOf(value)));
        } catch (NumberFormatException e) {
            LOG.warning("invalid NumberFormat: int prefs '{}'" + prefKey);
        }
        return value;
    }
}
