package in.dra.myfriendlygpsapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TextView latitudeField;
	private TextView longitudeField;
	private TextView accuracyField;
	private Location thelocation;
	private Button collectbutton;
	public String bigString;
	public String storedLat;
	public String storedLon;
	public String storedAcc;
	private EditText attributetext;
	protected String storedAtt;
	private EditText usertext;
	private String storedUser;
	
	// wifi related variables
	WifiManager mainWifi;
	WifiReceiver receiverWifi;
    List<ScanResult> wifiList;
    StringBuilder sb = new StringBuilder();
	private TextView wifistrengthField;
	private TextView wifistrengthLabel;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		latitudeField = (TextView) findViewById(R.id.TextVLat);
		longitudeField = (TextView) findViewById(R.id.TextVLon);
		accuracyField = (TextView) findViewById(R.id.TextVAcc);
		
		wifistrengthField = (TextView) findViewById(R.id.TextVWifi);
		wifistrengthLabel = (TextView) findViewById(R.id.TextVWifiLabel);

		latitudeField.setText("waiting for location");
		longitudeField.setText("waiting for location");
		accuracyField.setText("waiting for location");
		// wifistrengthField.setText("waiting for network");

		LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		thelocation = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		double doublelat = thelocation.getLatitude();
		String myLat = Double.toString(doublelat);
		latitudeField.setText(myLat);

		double doublelon = thelocation.getLongitude();
		String myLon = Double.toString(doublelon);
		longitudeField.setText(myLon);
		
		float doubleacc = thelocation.getAccuracy();
		String myAcc = Float.toString(doubleacc);
		accuracyField.setText(myAcc);
		
		storedLat = myLat;
		storedLon = myLon;
		storedAcc = myAcc;
			

		LocationListener mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1,
				mlocListener);

		addListenerOnButton();		
		
		
		
		/// lets get some wifi signal strength!
		
		receiverWifi = new WifiReceiver();
		// do the thing 'receiverwifi' when results are available
		registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		mainWifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		mainWifi.startScan();
		wifistrengthField.setText("Scanning...");
			
		/// finished with wifi signal strength stuff
		
	}
	
    class WifiReceiver extends BroadcastReceiver {
        
        // This method call when number of wifi connections changed
        public void onReceive(Context c, Intent intent) {
             
        	wifistrengthField.setText("data ready");
            sb = new StringBuilder();
            wifiList = mainWifi.getScanResults(); 
            
            //sb.append("\nNumber Of Wifi connections :"+wifiList.size()+"\n\n");
             
            // for(int i = 0; i < wifiList.size(); i++){
                 
           // 	double strengthvaluefromscan = (wifiList.get(i).level);
           // 	double strengthquality = ((strengthvaluefromscan + 100) * 2); 
           	
           //     sb.append(new Integer(i+1).toString() + ". ");
           //     sb.append((wifiList.get(i)).toString());
           //     sb.append(" ... ");
           //     String strengthstring = String.valueOf(strengthquality);
           //     sb.append("rrsi: " + strengthstring +"%");
           //     sb.append("\n\n");
           // }
             
            // Toast.makeText(getApplicationContext(), sb, Toast.LENGTH_LONG).show();
            
            // average eduroam values?
            
            // wifistrengthLabel.setText(sb);  
        }


         
    }	
	
	

	public class MyLocationListener implements LocationListener

	{

		@Override
		public void onLocationChanged(Location newlocation)

		{
			
			double doublelat2 = newlocation.getLatitude();
			String myLat2 = Double.toString(doublelat2);
			latitudeField.setText(myLat2);
			
			double doublelon2 = newlocation.getLongitude();
			String myLon2 = Double.toString(doublelon2);
			longitudeField.setText(myLon2);
			
			float doubleacc2 = newlocation.getAccuracy();
			String myAcc2 = Float.toString(doubleacc2);
			accuracyField.setText(myAcc2);
			
			storedLat = myLat2;
			storedLon = myLon2;
			storedAcc = myAcc2;
			
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
	}
	
	
	class postData extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			HttpClient httpclient = new DefaultHttpClient();
			
			attributetext   = (EditText)findViewById(R.id.editTextAttribute);
			usertext   = (EditText)findViewById(R.id.editTextUser);
			storedAtt = attributetext.getText().toString();
		    storedUser = usertext.getText().toString();
			
			String postingLat = storedLat;
			String postingLon = storedLon;
			String postingAcc = storedAcc;
			String postingAtt = storedAtt;
			String postingUse = storedUser;

			
			for(int i = 0; i < wifiList.size(); i++){
			
				HttpPost httppost = new HttpPost("http://54.200.220.71/wifireceiver.php");
			
				try {
					// Add your data
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							2);
					nameValuePairs.add(new BasicNameValuePair("lat", postingLat));
					nameValuePairs.add(new BasicNameValuePair("lon", postingLon));
					nameValuePairs.add(new BasicNameValuePair("acc", postingAcc));	
					nameValuePairs.add(new BasicNameValuePair("att", postingAtt));	
					nameValuePairs.add(new BasicNameValuePair("use", postingUse));
					
					int mysizer = wifiList.size();
					String mysizerString = String.valueOf(mysizer);
					
					nameValuePairs.add(new BasicNameValuePair("sizer", mysizerString));
										
					int levellevel = wifiList.get(i).level;
					int levellevel2 = (levellevel + 100) * 2; //change to percentage of goodness
					String macmac = wifiList.get(i).BSSID;
					String ssidssid = wifiList.get(i).SSID;
					long lastping = wifiList.get(i).timestamp;
					// long lastping = System.currentTimeMillis();
					
					nameValuePairs.add(new BasicNameValuePair("ssid", ssidssid));
					nameValuePairs.add(new BasicNameValuePair("mac", macmac));
					
					String mylastping = String.valueOf(lastping);
					nameValuePairs.add(new BasicNameValuePair("ping", mylastping));
					
					String mylevellevel = String.valueOf(levellevel2);
					nameValuePairs.add(new BasicNameValuePair("str", mylevellevel));
					
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					
					// Execute HTTP Post Request
					HttpResponse response = httpclient.execute(httppost);
					
				} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}
			return null;
		}

	}
	
	public void addListenerOnButton() {

		collectbutton = (Button) findViewById(R.id.buttonGo);
		collectbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// send the results to the server
				postData mypostData = new postData();
				mypostData.execute();
				
				Toast.makeText(getApplicationContext(), "Data Transmitted", Toast.LENGTH_LONG).show();
				
			}

		});

	}

}



