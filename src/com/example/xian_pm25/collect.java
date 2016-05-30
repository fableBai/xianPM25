package com.example.xian_pm25;



import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fable.util.Utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;


public class collect extends Activity implements OnClickListener{
	 private static final String TAG = "collect"; 
	 private static final String xi_anEnv= "http://www.xianemc.gov.cn/sxmpcp_qt1.asp";
	 private RiddleRepository repository;
	 
	 private ProgressDialog mProgressDialog = null;
	 private Spinner mCityNames;
	 private int mCityNum = 1;
	 private String CityAverage = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect);
        Log.d(TAG, "onCreate:");
        Button btnModule1 = (Button) findViewById(R.id.find_table);
        
        repository = RiddleRepository.getInstance(getApplicationContext());
        
    	btnModule1.setOnClickListener(this);
    	mCityNames = (Spinner) findViewById(R.id.city_names);
    	loadCityNames(mCityNames, "", 0, "");
    	mCityNames.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Log.d(TAG,"Spinner edit "+position);
				//setVisibility(View.VISIBLE);
				Spinner spinner = (Spinner) parent;
				Log.d(TAG, "id = " + id + "("  + spinner.getSelectedItem().toString() + ")");
				mCityNum = position;
				update(mCityNum);
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				Log.d(TAG, "onNothingSelected");
			}});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        boolean consumed = true;
        int itemId = item.getItemId();

        switch (itemId) {
        case R.id.menu_about:
   		 	Intent intent = new Intent(this, about.class);
   		 	startActivity(intent);
            break;
        default:
            consumed = super.onContextItemSelected(item);
            break;
        }

        return consumed;
    }
    void SetTextInfo(View view,int ResId,String text)
    {
    	TextView textView = (TextView)findViewById(ResId);
    	if(text.length() == 0){
    		textView.setText("N/A");
    	}else{
    		textView.setText(text);
    	}
    }
    void update(int num)
    {    
    	Log.d(TAG, "update:");
    	PollInfo polls = repository.getRiddleByNum(num + 2);//1 代表第一个地方
    	if(polls == null)
    		return;
    	Log.d(TAG, "update:"+polls.getCityName());
    	
        LayoutInflater inflater = LayoutInflater.from(collect.this);
 		View view2=inflater.inflate(R.layout.collect, null);
        SetTextInfo(view2, R.id.city_name, polls.getCityName());
        SetTextInfo(view2, R.id.poll_so2, polls.getSO2());
        SetTextInfo(view2, R.id.poll_no2, polls.getNO2());
        SetTextInfo(view2, R.id.poll_pm10, polls.getPM10());
        SetTextInfo(view2, R.id.poll_co, polls.getCO());
        SetTextInfo(view2, R.id.poll_o31, polls.getO31());
        SetTextInfo(view2, R.id.poll_o38, polls.getO38());
        SetTextInfo(view2, R.id.poll_pm25, polls.getPM25());
        SetTextInfo(view2, R.id.poll_state, polls.getState());
        SetTextInfo(view2, R.id.poll_aver, CityAverage);   
    }
    int updatePM25()
    {
    	try {
    		FindTds(xi_anEnv);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}
		return 0;
    }
    Elements FindTds(String Url) throws IOException
    {
    	Elements links = null;
    	Elements tds = null;
    	int i = 0,j=0;
    	links = linkFindTrs(Url);
    	if(links!= null){
        	for (Element link : links) {
        		Log.d(TAG, "link=: " + j++); 
        		tds = link.select("td");
        		if(j>=3 && j<=17){
	        		i = 0;
	        		PollInfo polls = new PollInfo();
	        		polls.setNum(j);
	        		for (Element td : tds) {
	            		Log.d(TAG, "td=: " + td.text() +"  "+ i); 
	            		polls.SetValuebyNum(td.text(), i++);
	            		if(j==17&& i==2)	
	            			CityAverage = td.text();
	            	}
	        		if(j != 17)
	        			repository.addProfile(polls);
        		}
        	}
        }
		return tds;
    }
    Elements linkFindTrs(String Url) throws IOException
	{
		Document doc = null;
		Elements links = null;

		doc = Jsoup.connect(Url).timeout(60000).get();

        Elements divs = doc.select("table.text");
        if (divs != null) {
        	links = divs.select("tr");
        	for (Element link : links) {
               // Log.d(TAG, "link: " + link.toString());   
            }  
        } 
		return links;
	}
	void read_html(String Url)
	{
		String st;
		st = Utils.LoadUrlData(Url);
		Log.d(TAG, "read_html:" + st);
		//LayoutInflater inflater = LayoutInflater.from(this);
		//final View view2=inflater.inflate(R.layout.collect, null);


	}
	private void loadCityNames(Spinner spinner, String prefix, int firstId, String selected)
    {
   	 	String cityNames = "";
        Context context = getBaseContext();
        String first = (firstId == 0) ? "" : context.getString(firstId);
        
    	List<PollInfo> allpolls = repository.getAllRiddles();
		for (PollInfo poll : allpolls){
			cityNames = cityNames + poll.getCityName() + " ";
		}
		
		Log.d(TAG,"CityNames:" + cityNames);
		String[] certificates = cityNames.split(" ");

        if (certificates == null || certificates.length == 0 || cityNames.length() == 0){
            certificates = new String[] {first};
            Log.d(TAG,"CityNames == null");
        }else{
            String[] array = new String[certificates.length + 1];
            array[0] = first;
            System.arraycopy(certificates, 0, array, 1, certificates.length);
            certificates = array;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
       		 context, android.R.layout.simple_spinner_item, certificates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        for (int i = 1; i < certificates.length; ++i) {
            if (certificates[i].equals(selected)) {
           	 Log.d(TAG,"setSelection:"+i);
                spinner.setSelection(i);
                break;
            }
        }
    }
	Handler myhandler = new Handler()
    {
        public void handleMessage(Message msg){
        	Log.d(TAG, "mProgressDialog " + mProgressDialog +" is show :"+mProgressDialog.isShowing());
        	if(mProgressDialog.isShowing())
        		mProgressDialog.cancel();
            switch(msg.what){
            case 0:
                Toast.makeText(getApplicationContext(), R.string.get_data_succeed, Toast.LENGTH_SHORT).show();
                
                loadCityNames(mCityNames, "", 0, "");
                break;
            case 1:
                Toast.makeText(getApplicationContext(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                break;
            default:
            	Toast.makeText(getApplicationContext(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
            	break;
            }
        }
    };

    //LoginThread线程类
    class LoginThread implements Runnable
    {
        public void run() {

            Message msg = myhandler.obtainMessage();
            
            Log.d(TAG,"run LoginThread ");
            msg.what = updatePM25();
            myhandler.sendMessage(msg);
        }
    };
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onClick");
		
		
		switch (v.getId()) {

			case R.id.find_table:
				Log.d(TAG, "find_table:");
				repository.clean();
				mProgressDialog = new ProgressDialog(this);
				mProgressDialog.setTitle(R.string.update_url);
				mProgressDialog.setMessage(getString(R.string.geting_date));
				mProgressDialog.show();
				
		    	Thread loginThread = new Thread(new LoginThread());
		        loginThread.start();
				//find_table();
				break;
		default:
			break;
		}
	}
    
}
