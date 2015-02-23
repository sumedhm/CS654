package com.sa.noproxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import com.sa.noproxy.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,OnItemSelectedListener{
	private Button reg,attend,send;
	AttendanceMarker register;
	EditText editPin;
	Spinner spinnerCourse;
	boolean permit = false;
	String selectedCourse,json;
	String url = "http://172.20.205.74/noproxy/post.php";
	public final static String COURSE = "course";
	public final static String FLAG = "flag";
	 private String[] course = { "CS654A" , "CS632A" , "CS498A" , "IME636A"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		editPin = (EditText)findViewById(R.id.password);
		 reg = (Button)findViewById(R.id.registerbutton);
		 reg.setOnClickListener(this);
		 attend = (Button)findViewById(R.id.startbutton);
		 attend.setOnClickListener(this);
		 send = (Button)findViewById(R.id.sendbutton);
		 send.setOnClickListener(this);
		 spinnerCourse = (Spinner) findViewById(R.id.spinnercourse);
		  ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
		    android.R.layout.simple_spinner_item, course);
		  adapter_state
		    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		  spinnerCourse.setAdapter(adapter_state);
		  spinnerCourse.setOnItemSelectedListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == reg){
			String pin = editPin.getText().toString();
			if(pin.equals("1234")){
			editPin.setText("");
			Intent intent1 = new Intent(this,JSGDActivity.class);
			intent1.putExtra(COURSE, selectedCourse);
			intent1.putExtra(FLAG, "1");
			startActivity(intent1);
			}
			else{
				Toast.makeText(getApplicationContext(), "Enter Valid PIN", Toast.LENGTH_LONG).show();
			}
			
		}
		else if(v == attend){
			String pin = editPin.getText().toString();
			if(pin.equals("1234")){
				editPin.setText("");
				Intent intent2 = new Intent(this,JSGDActivity.class);
				intent2.putExtra(COURSE, selectedCourse);
				intent2.putExtra(FLAG, "2");
				startActivity(intent2);
			}
			else{
				Toast.makeText(getApplicationContext(), "Enter Valid PIN", Toast.LENGTH_LONG).show();
			}
			

		}
		else if(v == send){
			String pin = editPin.getText().toString();
			if(pin.equals("1234")){
			String result;
        	editPin.setText("");
    		
    		register = new AttendanceMarker(getApplicationContext(), selectedCourse);
    		register.open();
    		try {
				json = register.composeJSONfromSQLite(selectedCourse);
				Log.d("jsonstring",json);
				new HttpAsyncTask().execute(url);
				//Toast.makeText(getApplicationContext(),json1, Toast.LENGTH_LONG).show();
				//Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
				//Toast.makeText(getApplicationContext(),json, Toast.LENGTH_LONG).show();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
    		register.close();
		}
			else{
				Toast.makeText(getApplicationContext(), "Enter Valid PIN", Toast.LENGTH_LONG).show();
			}
	
		}
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		spinnerCourse.setSelection(position);
		  selectedCourse = (String) spinnerCourse.getSelectedItem();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	public String POST(String url){
        InputStream inputStream = null;
        String result = "default";
        try {
        	//Toast.makeText(getApplicationContext(), "try", Toast.LENGTH_LONG).show();
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
 
            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            //String url = "http://yourserver";
           
      
 
            // 3. build jsonObject
          
 
            // 4. convert JSONObject to JSON to String
            //String json = data;
           // Log.d("json", json);
 
        
 
            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);
 
            // 6. set httpPost Entity
            httpPost.setEntity(se);
 
            // 7. Set some headers to inform server about the type of the content   
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
 
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
 
            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
 
            // 10. convert inputstream to string
            if(inputStream != null){
                result = convertInputStreamToString(inputStream);
           
            }
            else
                result = "Did not work!";
       
 
        } catch (Exception e) {
        	
            Log.d("InputStream", e.getLocalizedMessage());
        }
 
        // 11. return result
        
        Log.d("result",result);
        return result;
    }
 
	private String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
       // Res = result;
        return result;
 
    }   


	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
 
           
            return POST(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        	//checkResult(result);
        	//Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        	//Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        	if(result.equals("OK")){
        		
        		Toast.makeText(getApplicationContext(), "DATA SENT", Toast.LENGTH_LONG).show();
        		register = new AttendanceMarker(getApplicationContext(), selectedCourse);
        		register.open();
        		register.emptyTable("ATTENDANCE");
        		register.close();
        	}
        	else{
        		Toast.makeText(getApplicationContext(), "ERROR IN SENDING DATA", Toast.LENGTH_LONG).show();
        	}
       }
    }

	
}
