/*
 * Copyright (C) 2013 SecuGen Corporation
 *
 */

package com.sa.noproxy; 

import java.io.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import com.sa.noproxy.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import SecuGen.FDxSDKPro.*;

public class JSGDActivity extends Activity
        implements View.OnClickListener, java.lang.Runnable, SGFingerPresentEvent {

    private static final String TAG = "SecuGen USB";
    AttendanceMarker register;
    String url1 = "http://192.168.150.8/noproxy/post.php";
    String url = "http://172.20.205.74/noproxy/post.php";
    String json,course,flag;
   // private Button mCapture; 
    private Button mButtonRegister;
    private Button mButtonMatch;
    private Button mButtonLed;
   // private Button mSDKTest;
   // private Button mSend;
    //private Button mnext;
    private EditText mEditLog,roll,verifyRoll,mCourse;
  // private android.widget.TextView mTextViewResult;
    private android.widget.CheckBox mCheckBoxMatched;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String currentDate = sdf.format(new Date());
    String code = encryptPassword("noproxy");
    //private android.widget.ToggleButton mToggleButtonSmartCapture;
    //private android.widget.ToggleButton mToggleButtonAutoOn;
    private PendingIntent mPermissionIntent;
    //private ImageView mImageViewFingerprint;
    private ImageView mImageViewRegister;
   // private ImageView mImageViewVerify;
    private byte[] mRegisterImage;
    private byte[] studentImage;
    private byte[] mVerifyImage;
    private byte[] mRegisterTemplate;
    private byte[] studentTemplate;
    private byte[] mVerifyTemplate;
	private int[] mMaxTemplateSize;
	private int mImageWidth;
	private int mImageHeight;
	private int[] grayBuffer;
    private Bitmap grayBitmap;
    private IntentFilter filter; //2014-04-11
    private SGAutoOnEventNotifier autoOn;
    private boolean mLed;
    private boolean mAutoOnEnabled;
  
    private JSGFPLib sgfplib;
    JSONArray array = new JSONArray();
    JSONObject pass = new JSONObject();
	

    private void debugMessage(String message) {
        this.mEditLog.append(message);
        this.mEditLog.invalidate(); //TODO trying to get Edit log to update after each line written
    }

    //RILEY
    //This broadcast receiver is necessary to get user permissions to access the attached USB device
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
    	public void onReceive(Context context, Intent intent) {
    		String action = intent.getAction();
    		//DEBUG Log.d(TAG,"Enter mUsbReceiver.onReceive()");
    		if (ACTION_USB_PERMISSION.equals(action)) {
    			synchronized (this) {
    				UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
    				if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
    					if(device != null){
    						//DEBUG Log.d(TAG, "Vendor ID : " + device.getVendorId() + "\n");
    						//DEBUG Log.d(TAG, "Product ID: " + device.getProductId() + "\n");
    						debugMessage("Vendor ID : " + device.getVendorId() + "\n");
    						debugMessage("Product ID: " + device.getProductId() + "\n");
    					}
    					else
        					Log.e(TAG, "mUsbReceiver.onReceive() Device is null");    						
    				} 
    				else
    					Log.e(TAG, "mUsbReceiver.onReceive() permission denied for device " + device);    				
    			}
    		}
    	}
    };  
    
    //RILEY
    //This message handler is used to access local resources not
    //accessible by SGFingerPresentCallback() because it is called by
    //a separate thread.
   /* public Handler fingerDetectedHandler = new Handler(){ 
    	// @Override
	    public void handleMessage(Message msg) {
	       //Handle the message
			CaptureFingerPrint();
	    	/*if (mAutoOnEnabled) {
				mToggleButtonAutoOn.toggle();	
		    	EnableControls();		
	    	}
	    }
    };*/
//
	public void EnableControls(){
		//this.mCapture.setClickable(true);
		//this.mCapture.setTextColor(getResources().getColor(android.R.color.white));		
		this.mButtonRegister.setClickable(true);
		this.mButtonRegister.setTextColor(getResources().getColor(android.R.color.white));		
		//this.mButtonMatch.setClickable(true);
		//this.mSend.setClickable(true);
		//this.mnext.setClickable(true);
		//this.mButtonMatch.setTextColor(getResources().getColor(android.R.color.white));		
	}
//
	public void DisableControls(){
		//this.mCapture.setClickable(false);
		//this.mCapture.setTextColor(getResources().getColor(android.R.color.black));		
		this.mButtonRegister.setClickable(false);
		this.mButtonRegister.setTextColor(getResources().getColor(android.R.color.black));		
		//this.mButtonMatch.setClickable(false);
		//this.mButtonMatch.setTextColor(getResources().getColor(android.R.color.black));		
		//this.mSend.setClickable(false);
		//this.mnext.setClickable(false);
	}
//	
	
    //RILEY
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.launcher);
        Intent intentmain = getIntent();
        course = intentmain.getStringExtra(MainActivity.COURSE);
        flag = intentmain.getStringExtra(MainActivity.FLAG);
        Log.d("course",course);
        Log.d("flag",flag);

    	
     
        mButtonRegister = (Button)findViewById(R.id.buttonRegister);
        if(flag.equals("1")){
        	mButtonRegister.setText("REGISTER");
        }
        else if(flag.equals("2")){
        	mButtonRegister.setText("MARK");
        }
        mButtonRegister.setOnClickListener(this);
       // mButtonMatch = (Button)findViewById(R.id.buttonMatch);
       // mButtonMatch.setOnClickListener(this);
       
        //mButtonLed = (Button)findViewById(R.id.buttonLedOn);
        //mButtonLed.setOnClickListener(this);
       // mSDKTest = (Button)findViewById(R.id.buttonSDKTest);
        //mSDKTest.setOnClickListener(this);
        mEditLog = (EditText)findViewById(R.id.editLog);
      //  mTextViewResult = (android.widget.TextView)findViewById(R.id.textViewResult);
        mCheckBoxMatched = (android.widget.CheckBox) findViewById(R.id.checkBoxMatched);
        roll = (EditText)findViewById(R.id.rollNum);
    	//String a = isEmpty(roll);
    	//mCourse = (EditText)findViewById(R.id.coursetext);
        //mToggleButtonSmartCapture = (android.widget.ToggleButton) findViewById(R.id.toggleButtonSmartCapture);
        //mToggleButtonSmartCapture.setOnClickListener(this);
        //mToggleButtonAutoOn = (android.widget.ToggleButton) findViewById(R.id.toggleButtonAutoOn);
        //mToggleButtonAutoOn.setOnClickListener(this);        
       // mImageViewFingerprint = (ImageView)findViewById(R.id.imageViewFingerprint);
        mImageViewRegister = (ImageView)findViewById(R.id.imageViewRegister);
       // mImageViewVerify = (ImageView)findViewById(R.id.imageViewVerify);
        grayBuffer = new int[JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES*JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES];
        for (int i=0; i<grayBuffer.length; ++i)
        	grayBuffer[i] = android.graphics.Color.GRAY;
        grayBitmap = Bitmap.createBitmap(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES, Bitmap.Config.ARGB_8888);
        grayBitmap.setPixels(grayBuffer, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, 0, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES); 
       // mImageViewFingerprint.setImageBitmap(grayBitmap);

        int[] sintbuffer = new int[(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES/2)*(JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES/2)];
        for (int i=0; i<sintbuffer.length; ++i)
        	sintbuffer[i] = android.graphics.Color.GRAY;
        Bitmap sb = Bitmap.createBitmap(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES/2, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES/2, Bitmap.Config.ARGB_8888);
        sb.setPixels(sintbuffer, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES/2, 0, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES/2, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES/2); 
        mImageViewRegister.setImageBitmap(grayBitmap);
        //mImageViewVerify.setImageBitmap(grayBitmap); 
        
        mMaxTemplateSize = new int[1];

        //USB Permissions
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
       	filter = new IntentFilter(ACTION_USB_PERMISSION);
       	registerReceiver(mUsbReceiver, filter);       	        
        sgfplib = new JSGFPLib((UsbManager)getSystemService(Context.USB_SERVICE));
       // this.mToggleButtonSmartCapture.toggle();

        
		debugMessage("jnisgfplib version: " + sgfplib.Version() + "\n");
		mLed = false;	
		autoOn = new SGAutoOnEventNotifier (sgfplib, this);
		mAutoOnEnabled = false;
		
    }

    @Override
    public void onPause() {
    	Log.d(TAG, "onPause()");	
		autoOn.stop();
		EnableControls();
    	sgfplib.CloseDevice();
    	unregisterReceiver(mUsbReceiver);
    	mRegisterImage = null;
    	studentImage = null;
    	mVerifyImage = null;
    	mRegisterTemplate = null;
    	mVerifyTemplate = null;
       // mImageViewFingerprint.setImageBitmap(grayBitmap);
        mImageViewRegister.setImageBitmap(grayBitmap);
      //  mImageViewVerify.setImageBitmap(grayBitmap); 
        super.onPause(); 
    }
    
    @Override
    public void onResume(){
    	Log.d(TAG, "onResume()");	
        super.onResume();
       	registerReceiver(mUsbReceiver, filter);       	        
        long error = sgfplib.Init( SGFDxDeviceName.SG_DEV_AUTO);
        if (error != SGFDxErrorCode.SGFDX_ERROR_NONE){
        	AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        	if (error == SGFDxErrorCode.SGFDX_ERROR_DEVICE_NOT_FOUND)
        		dlgAlert.setMessage("The attached fingerprint device is not supported on Android");
        	else
        		dlgAlert.setMessage("Fingerprint device initialization failed!");
        	dlgAlert.setTitle("SecuGen Fingerprint SDK");
        	dlgAlert.setPositiveButton("OK",
        			new DialogInterface.OnClickListener() {
        		      public void onClick(DialogInterface dialog,int whichButton){
        		        	finish();
        		        	return;        		    	  
        		      }        			
        			}
        	);
        	dlgAlert.setCancelable(false);
        	dlgAlert.create().show();        	
        }
        else {
	        UsbDevice usbDevice = sgfplib.GetUsbDevice();
	        if (usbDevice == null){
	        	AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
	        	dlgAlert.setMessage("SDU04P or SDU03P fingerprint sensor not found!");
	        	dlgAlert.setTitle("SecuGen Fingerprint SDK");
	        	dlgAlert.setPositiveButton("OK",
	        			new DialogInterface.OnClickListener() {
	        		      public void onClick(DialogInterface dialog,int whichButton){
	        		        	finish();
	        		        	return;        		    	  
	        		      }        			
	        			}
	        	);
	        	dlgAlert.setCancelable(false);
	        	dlgAlert.create().show();
	        }
	        else {
		        sgfplib.GetUsbManager().requestPermission(usbDevice, mPermissionIntent);
		        error = sgfplib.OpenDevice(0);
				debugMessage("OpenDevice() ret: " + error + "\n");
		        SecuGen.FDxSDKPro.SGDeviceInfoParam deviceInfo = new SecuGen.FDxSDKPro.SGDeviceInfoParam();
		        error = sgfplib.GetDeviceInfo(deviceInfo);
				debugMessage("GetDeviceInfo() ret: " + error + "\n");
		    	mImageWidth = deviceInfo.imageWidth;
		    	mImageHeight= deviceInfo.imageHeight;
		        sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400);
				sgfplib.GetMaxTemplateSize(mMaxTemplateSize);
				debugMessage("TEMPLATE_FORMAT_SG400 SIZE: " + mMaxTemplateSize[0] + "\n");
		        mRegisterTemplate = new byte[mMaxTemplateSize[0]];
		        mVerifyTemplate = new byte[mMaxTemplateSize[0]];
		        boolean smartCaptureEnabled = true;//this.mToggleButtonSmartCapture.isChecked();
		        if (smartCaptureEnabled)
		        	sgfplib.WriteData((byte)5, (byte)1);
		        	
		        else
		        	sgfplib.WriteData((byte)5, (byte)0);
		        if (mAutoOnEnabled){
		        	autoOn.start();
		        	DisableControls();
		        }
		        //Thread thread = new Thread(this);
		        //thread.start();
	        }
        }
    }

    @Override
    public void onDestroy() {
    	Log.d(TAG, "onDestroy()");
    	sgfplib.CloseDevice();
    	mRegisterImage = null;
    	studentImage = null;
    	mVerifyImage = null;
    	mRegisterTemplate = null;
    	mVerifyTemplate = null;
    	sgfplib.Close();
        super.onDestroy();
    }
    private static String encryptPassword(String password)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
    //Converts image to grayscale (NEW)
    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {        
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();    
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int y=0; y< height; ++y) {
            for (int x=0; x< width; ++x){
            	int color = bmpOriginal.getPixel(x, y);
            	int r = (color >> 16) & 0xFF;
            	int g = (color >> 8) & 0xFF;
            	int b = color & 0xFF;           	
            	int gray = (r+g+b)/3;
            	color = Color.rgb(gray, gray, gray);
            	//color = Color.rgb(r/3, g/3, b/3);
            	bmpGrayscale.setPixel(x, y, color); 
            }
        }
        return bmpGrayscale;
    }
 
    //Converts image to binary (OLD)
    public Bitmap toBinary(Bitmap bmpOriginal)
    {        
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();    
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }
    
    
    public void DumpFile(String fileName, byte[] buffer)
    {
    	//Uncomment section below to dump images and templates to SD card
    	
        try {
        	File folder = new File(Environment.getExternalStorageDirectory() + "/No-Proxy");
        	boolean success = true;
        	if (!folder.exists()) {
        	    success = folder.mkdir();
        	}
        	if (success) {
        	    // Do something on success
        		File myFile = new File(Environment.getExternalStorageDirectory() + "/No-Proxy/" + fileName);
                myFile.createNewFile();
                FileOutputStream fOut = new FileOutputStream(myFile);
                fOut.write(buffer,0,buffer.length);
                fOut.close();
        		
        	} else {
        	    // Do something else on failure 
        	}
            
        } catch (Exception e) {
            debugMessage("Exception when writing file" + fileName);
        }
       
    }   

    public void SGFingerPresentCallback (){
		autoOn.stop();
		//fingerDetectedHandler.sendMessage(new Message());
    }
    
	
    public boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }
    public void onClick(View v) {
		long dwTimeStart = 0, dwTimeEnd = 0, dwTimeElapsed = 0;

        if (v == this.mButtonRegister && flag.equals("1")) {
        	debugMessage("Clicked REGISTER\n");
        	
        	if(!isEmpty(roll)){
        	
        			String studentRoll = roll.getText().toString();
        			register=new AttendanceMarker(this,course);
        	        register.open();
                    if (register.getSinlgeEntry("REGISTER", studentRoll) == "NOT EXIST") { 
                    	register.insertEntry("REGISTER",studentRoll);
                    	if (mRegisterImage != null)
                        	mRegisterImage = null;
                        mRegisterImage = new byte[mImageWidth*mImageHeight];

                    	this.mCheckBoxMatched.setChecked(false);
                        ByteBuffer byteBuf = ByteBuffer.allocate(mImageWidth*mImageHeight);
                        dwTimeStart = System.currentTimeMillis();          
                        long result = sgfplib.GetImage(mRegisterImage);
                       
                       
                        //DumpFile(studentRoll, mRegisterImage);
                        dwTimeEnd = System.currentTimeMillis();
                        dwTimeElapsed = dwTimeEnd-dwTimeStart;
                        debugMessage("GetImage() ret:" + result + " [" + dwTimeElapsed + "ms]\n");
                        Bitmap b = Bitmap.createBitmap(mImageWidth,mImageHeight, Bitmap.Config.ARGB_8888);
                        byteBuf.put(mRegisterImage);
                        int[] intbuffer = new int[mImageWidth*mImageHeight];
                        for (int i=0; i<intbuffer.length; ++i)
                        	intbuffer[i] = (int) mRegisterImage[i];
                        b.setPixels(intbuffer, 0, mImageWidth, 0, 0, mImageWidth, mImageHeight); 
                        //DEBUG Log.d(TAG, "Show Register image");
                     //   mImageViewFingerprint.setImageBitmap(this.toGrayscale(b));  
                        dwTimeStart = System.currentTimeMillis();          
                        result = sgfplib.SetTemplateFormat(SecuGen.FDxSDKPro.SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400);
                        dwTimeEnd = System.currentTimeMillis();
                        dwTimeElapsed = dwTimeEnd-dwTimeStart;
                        debugMessage("SetTemplateFormat(SG400) ret:" +  result + " [" + dwTimeElapsed + "ms]\n");
                        SGFingerInfo fpInfo = new SGFingerInfo();
                        for (int i=0; i< mRegisterTemplate.length; ++i)
                        	mRegisterTemplate[i] = 0;
                        dwTimeStart = System.currentTimeMillis();          
                        result = sgfplib.CreateTemplate(fpInfo, mRegisterImage, mRegisterTemplate);
                        DumpFile(studentRoll+".min", mRegisterTemplate);
                        dwTimeEnd = System.currentTimeMillis();
                        dwTimeElapsed = dwTimeEnd-dwTimeStart;
                        debugMessage("CreateTemplate() ret:" + result + " [" + dwTimeElapsed + "ms]\n");
                        this.mImageViewRegister.setImageBitmap(this.toGrayscale(b));  
//                    	mTextViewResult.setText("Click Verify");
                    }
                    else {
                    	Context context = getApplicationContext();
                    	Toast.makeText(context, "Error: The Roll Number Already Exists" , Toast.LENGTH_LONG).show();
                    }
                    register.close();
                	//DEBUG Log.d(TAG, "Clicked REGISTER");

        		
        		
        	}
        	else{
        		Toast.makeText(getApplicationContext(), "Error: enter roll number for registration" , Toast.LENGTH_LONG).show();
        	}
                    }
/*This condition handles the verification part*/
        if (v == this.mButtonRegister && flag.equals("2")) {
        	//liveSend("CS654A", "11101", 1);
     		register = new AttendanceMarker(getApplicationContext(), course);
    		register.open();
        	//DEBUG Log.d(TAG, "Clicked MATCH");
            debugMessage("Clicked MATCH\n");
            verifyRoll = (EditText)findViewById(R.id.rollNum);
        	String checkRoll = verifyRoll.getText().toString();
            if (mVerifyImage != null)
            	mVerifyImage = null;
            mVerifyImage = new byte[mImageWidth*mImageHeight];
            studentTemplate = new byte[mImageWidth*mImageHeight];
            ByteBuffer regBuf = ByteBuffer.allocate(mImageWidth*mImageHeight);
            ByteBuffer byteBuf = ByteBuffer.allocate(mImageWidth*mImageHeight);
            dwTimeStart = System.currentTimeMillis();          
            long result = sgfplib.GetImage(mVerifyImage);
            //DumpFile("verify.raw", mVerifyImage);
            dwTimeEnd = System.currentTimeMillis();
            dwTimeElapsed = dwTimeEnd-dwTimeStart;
            debugMessage("GetImage() ret:" + result + " [" + dwTimeElapsed + "ms]\n");
            FileInputStream fis = null;
            
            try {
				fis = new FileInputStream("sdcard/No-Proxy/"+checkRoll+".min");
				int i = fis.read(studentTemplate);
				Context context = getApplicationContext();
	            CharSequence text = "read .min file";
	            int duration = Toast.LENGTH_LONG;
	            //studentRoll = studentRoll+".raw";
	            //Toast toast = Toast.makeText(context, text, duration);
	            //toast.show();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            
            // read bytes to the buffer
  
            Bitmap b = Bitmap.createBitmap(mImageWidth,mImageHeight, Bitmap.Config.ARGB_8888);
            byteBuf.put(mVerifyImage);
            int[] intbuffer = new int[mImageWidth*mImageHeight];
            for (int i=0; i<intbuffer.length; ++i)
            	intbuffer[i] = (int) mVerifyImage[i];
            b.setPixels(intbuffer, 0, mImageWidth, 0, 0, mImageWidth, mImageHeight); 
            //DEBUG Log.d(TAG, "Show Verify image");
          //  mImageViewFingerprint.setImageBitmap(this.toGrayscale(b));  
            this.mImageViewRegister.setImageBitmap(this.toGrayscale(b)); 
            dwTimeStart = System.currentTimeMillis();          
            result = sgfplib.SetTemplateFormat(SecuGen.FDxSDKPro.SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400);
            dwTimeEnd = System.currentTimeMillis();
            dwTimeElapsed = dwTimeEnd-dwTimeStart;
            debugMessage("SetTemplateFormat(SG400) ret:" +  result + " [" + dwTimeElapsed + "ms]\n");
            SGFingerInfo fpInfo = new SGFingerInfo();
            for (int i=0; i< mVerifyTemplate.length; ++i)
            	mVerifyTemplate[i] = 0;
            dwTimeStart = System.currentTimeMillis();          
            result = sgfplib.CreateTemplate(fpInfo, mVerifyImage, mVerifyTemplate);
            //DumpFile("verify.min", mVerifyTemplate);
            dwTimeEnd = System.currentTimeMillis();
            dwTimeElapsed = dwTimeEnd-dwTimeStart;
            debugMessage("CreateTemplate() ret:" + result+ " [" + dwTimeElapsed + "ms]\n");
            boolean[] matched = new boolean[1];
            dwTimeStart = System.currentTimeMillis();          
            //result = sgfplib.MatchTemplate(mRegisterTemplate, mVerifyTemplate, SGFDxSecurityLevel.SL_NORMAL, matched);
            result = sgfplib.MatchTemplate(studentTemplate, mVerifyTemplate, SGFDxSecurityLevel.SL_NORMAL, matched);
            dwTimeEnd = System.currentTimeMillis();
            dwTimeElapsed = dwTimeEnd-dwTimeStart;
            debugMessage("MatchTemplate() ret:" + result+ " [" + dwTimeElapsed + "ms]\n");
            if (matched[0]) {
            	/*mark the attendance corresponding to that roll number.
            	 */
            	
            	String output = register.getSinlgeEntry("REGISTER",checkRoll);
            	//Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
            	/*checking if student registered in course*/
            	if (output.equals("EXIST")) {
            		/*checking if student's entry has been marked*/
            		if (register.getSinlgeEntry("ATTENDANCE", checkRoll) == "EXIST") {
            			String temp = register.checkAttendance(checkRoll).toString();
            			//Toast.makeText(getApplicationContext(), "check: "+"*"+ temp+"*", Toast.LENGTH_LONG).show();
            			/*checking if earler entry was wrong*/
            			if(temp.equals("2")){
            				/*updating the atendance table*/
            				register.updateEntry(checkRoll, "1");
            				liveSend(course,checkRoll,1);
            				//Toast.makeText(getApplicationContext(), "check: "+ register.checkAttendance(checkRoll), Toast.LENGTH_LONG).show();
            				Toast.makeText(getApplicationContext(), "Attendance marked earlier was dubious.. marking correct attendance now ", Toast.LENGTH_LONG).show();
            			}
            			else{
            				Toast.makeText(getApplicationContext(), "ERROR: Attendance already marked ", Toast.LENGTH_LONG).show();
            			}
            		}else if(register.getSinlgeEntry("ATTENDANCE", checkRoll) == "NOT EXIST"/*register.checkAttendance(checkRoll).toString() == "2"*/)
            			{/*mark attendance*/
            			register.markAttendance(checkRoll, "1");
            			liveSend(course,checkRoll,1);	/*send to server*/
            			Toast.makeText(getApplicationContext(), "Attendance marked ", Toast.LENGTH_LONG).show();
            			//Toast.makeText(getApplicationContext(), "ERROR: Attendance already marked ", Toast.LENGTH_LONG).show();
            		}	
            	}
            	else {
            		Toast.makeText(getApplicationContext(), "ERROR: you are not registered in course ", Toast.LENGTH_LONG).show();
            	}
            	this.mCheckBoxMatched.setChecked(true);
                debugMessage("MATCHED!!\n");
            }
            else {
            	this.mCheckBoxMatched.setChecked(false);
            	String output = register.getSinlgeEntry("REGISTER",checkRoll);
            	if(output.equals("EXIST")){
            		if (register.getSinlgeEntry("ATTENDANCE", checkRoll) == "NOT EXIST") {
            			register.markAttendance(checkRoll, "2");
            			liveSend(course,checkRoll,2);
            			Toast.makeText(getApplicationContext(), "Attendance marked is dubious ", Toast.LENGTH_LONG).show();
            		} else {
            			Toast.makeText(getApplicationContext(), "ERROR: Attendance was already marked by someone", Toast.LENGTH_LONG).show();
            		}	
            	}
            	else {
            		Toast.makeText(getApplicationContext(), "ERROR: you are not registered in course ", Toast.LENGTH_LONG).show();
            	}
            	
                debugMessage("NOT MATCHED!!\n");
            }
            register.close();
      }
        
    }
    /*public void checkResult(String result){
    	if(result.equals("OK")){
    		json = "";
    		array = null;
    		Log.d("checkResult",result);	
    	}
    	//Log.d("check json array",array.toString()+"*");
    	//Log.d("check json String",json+"*");
    }*/
    private void liveSend(String _course, String checkRoll, int i) {
		// TODO Auto-generated method stub
    	JSONObject js = new JSONObject();
    	try {
			js.put("code", code);
			js.put("course",_course);
	    	js.put("roll", checkRoll);
	        js.put("present", i);
	        js.put("date",currentDate);
	        array.put(js);
	        json = array.toString();
	        Log.d("data sent",json);
//	        new HttpAsyncTask().execute();
	       // DataSend dataSend = new DataSend();
	      //  Log.d("check json array before",array.toString()+"*");
	    //	Log.d("check json String before",json+"*");;
	        //dataSend.getString(json,this);
	    	new HttpAsyncTask().execute(url);
	    	
	       
	        
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        
		
	}

	public String POST(String url){
        InputStream inputStream = null;
        String result = "default";
        try {
        	//Toast.makeText(getApplicationContext(), "try", Toast.LENGTH_LONG).show();
            HttpClient httpclient = new DefaultHttpClient();
 
            HttpPost httpPost = new HttpPost(url);
           
            StringEntity se = new StringEntity(json);
 
          
            httpPost.setEntity(se);
 
             
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
 
            HttpResponse httpResponse = httpclient.execute(httpPost);
 
         
            inputStream = httpResponse.getEntity().getContent();
 
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
       }
    }
 
    @Override
    public void run() {
    	
    	Log.d(TAG, "Enter run()");
        ByteBuffer buffer = ByteBuffer.allocate(1);
       // UsbRequest request = new UsbRequest();
        //request.initialize(mSGUsbInterface.getConnection(), mEndpointBulk);
        //byte status = -1;
        //while (true) {
        	
        	
            // queue a request on the interrupt endpoint
            //request.queue(buffer, 1);
            // send poll status command
          //  sendCommand(COMMAND_STATUS);
            // wait for status event
            /*
            if (mSGUsbInterface.getConnection().requestWait() == request) {
                byte newStatus = buffer.get(0);
                if (newStatus != status) {
                    Log.d(TAG, "got status " + newStatus);
                    status = newStatus;
                    if ((status & COMMAND_FIRE) != 0) {
                        // stop firing
                        sendCommand(COMMAND_STOP);
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            } else {
                Log.e(TAG, "requestWait failed, exiting");
                break;
            }
            */
       // }
    }
}