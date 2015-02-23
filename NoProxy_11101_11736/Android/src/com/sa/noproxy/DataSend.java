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

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class DataSend {
	
	String json;
	Context context;
	String url = "http://172.20.205.74/noproxy/post.php";
	String result = "default";
	//JSGDActivity check = new JSGDActivity();
	public String POST(String url){
        InputStream inputStream = null;
     
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
          //  ack = result;
            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
 
        } catch (Exception e) {
        	//Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            Log.d("InputStream", e.getLocalizedMessage());
            Toast.makeText(context, "error", Toast.LENGTH_LONG).show();
        }
 
        // 11. return result
        //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
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

/*public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected())
                return true;
            else
                return false;    
    }*/
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
 
           
            return POST(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        	Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        	Log.d("result in datasend",result);
        	//check.checkResult(result);
        	
        	
       }
    }

	public void getString(String string,Context _context) {
		// TODO Auto-generated method stub
		json = string;
		context = _context;
		new HttpAsyncTask().execute(url);
		
		
	}
}
