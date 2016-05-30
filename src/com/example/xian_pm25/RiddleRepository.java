

package com.example.xian_pm25;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;


public final class RiddleRepository {

    private static final String TAG = "RidRep";

    private static RiddleRepository instance;

    private Context context;
    private List<PollInfo> pollInfos;

    private RiddleRepository(final Context ctx) {
        this.context = ctx;
        pollInfos = new ArrayList<PollInfo>();
    }

    /**
     * Retrieves the single instance of repository.
     *
     * @param ctx
     *            Context
     * @return singleton
     */
    public static RiddleRepository getInstance(final Context ctx) {
        if (instance == null)  {
            instance = new RiddleRepository(ctx);
            //instance.load();
        }

        return instance;
    }

    private void load() {
    	try {
			loadProfiles();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private void loadProfiles() throws Exception {
    	InputStream is = null;
    	InputStreamReader isr = null;
    	BufferedReader br = null; //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
    	String str;
    	PollInfo riddle;
        try {
        	is = context.getResources().getAssets().open("log0.brain");
        	isr = new InputStreamReader(is);// InputStreamReader 是字节流通向字符流的桥梁,
            br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
           
		while ((str = br.readLine()) != null && str.length() > 5) {
			riddle = loadObj(str);
			addProfile(riddle);
           }
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    private PollInfo loadObj(String data)
    {
    	
    	String Content, Answer;
    	int num;
    	PollInfo riddle = null;
    	JSONArray array;
    	Log.d(TAG, "loadObj:" + data);
		try {
			array = new JSONArray("[\n"+data +"\n]");
			riddle = new PollInfo();

			if (array.length() > 0) {
				JSONObject obj = array.getJSONObject(0);
				try {
					num = Integer.parseInt(obj.getString("id"));
					
					Content = obj.getString("content");
					Answer = obj.getString("answer");
					
					Log.i("num obj", num + "@");
					//Log.i("concet obj", Content + "@" + Answer);
					String newStr = new String(Answer.getBytes(), "utf-8");
					Log.i(TAG, "answer:" + Answer);
					//riddle.setall(num, Content, Answer);
					
				} catch (Exception e) {
					num = -1;
					Content = "";
					Answer = "";
				}
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return riddle;
    }
    public List<PollInfo> getAllRiddles() {
        return Collections.unmodifiableList(pollInfos);
    }
    public PollInfo getRiddleByNum(final int id) {
        for (PollInfo p : pollInfos) {
            if (p.getNum()==id)
                return p;
        }
        return null;
    }
    public synchronized void addProfile(final PollInfo p) {
    	Log.d(TAG, p.getNum() + " addProfile: " + p.getCityName());
    	pollInfos.add(p);
    }

    public void clean() {
    	pollInfos.clear();
    }

}
