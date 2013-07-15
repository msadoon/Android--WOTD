package mkat.apps.wotd;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import mkat.apps.wotd.WotdWidget.UpdateService;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class MainActivity extends Activity implements OnClickListener {
	

	Context context = this;
	String monthString;
	int Day;
	Calendar c2, cal;
	URL url2;
	private GestureDetectorCompat mDetector;
	ScrollView scrollView;
	TextView tvWord, tvDefinition, tvDate;
	Button bClose;
	
	static final String baseURL = "http://en.wiktionary.org/w/api.php?action=query&titles=Wiktionary:Word_of_the_day/";
	
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		setContentView(R.layout.activity_main);
		
		CheckConnectivity check = new CheckConnectivity();
		Boolean conn = check.checkNow(this.getApplicationContext());
		if(conn==true){
			
		
		
		mDetector = new GestureDetectorCompat(this, new MyGestureListener());
			scrollView = (ScrollView) findViewById(R.id.svWotd);
			//dpWotd = (DatePicker) findViewById(R.id.dpWotd);
			tvWord = (TextView) findViewById(R.id.tvWord);
			tvDefinition = (TextView) findViewById(R.id.tvDefinition);
			tvDate = (TextView) findViewById(R.id.tvDate);
			bClose = (Button) findViewById(R.id.bClose);
			//tvWW = (TextView) findViewById(R.id.tvWordWidget); //widget textview
		
			c2= Calendar.getInstance();
			url2 = gimmeURL(c2);;
		
			//view = new RemoteViews(context.getPackageName(), R.layout.wotd_layout);
			RetrieveXmlFeed Task = new RetrieveXmlFeed();
			Task.execute(url2);
		
			Intent intent = new Intent(context, UpdateService.class);
			PendingIntent pintent = PendingIntent.getService(context, 0, intent, 0);

			AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			// Start every 30 seconds
			c2 = Calendar.getInstance();
			//9AM
			c2.set(Calendar.HOUR_OF_DAY, 9);
			c2.set(Calendar.MINUTE, 0);
			c2.set(Calendar.SECOND, 0);
			alarm.setRepeating(AlarmManager.RTC_WAKEUP, c2.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent); 
        
			//Configure close button and widget
			//Calendar calendar = null;
			bClose.setOnClickListener(this);
			//dpWotd.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), this);
		
			AdView ad = (AdView)findViewById(R.id.ad);
			ad.loadAd(new AdRequest());
		}
		else {
			connectivityMessage("Check Network Connection.");
			this.finish();
		}
	} 
	
	public void connectivityMessage(String msg){
	     Context context = getApplicationContext();
	     Toast toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
	     toast.setGravity(Gravity.CENTER, 0, 0);
	     toast.setText(msg);
	     toast.show();
	}
	
	public URL gimmeURL(Calendar c3){
			try {
        	
				switch (c2.get(Calendar.MONTH)) {
				case 0:  monthString = "January";
					break;
				case 1:  monthString = "February";
                     break;
				case 2:  monthString = "March";
                     break;
				case 3:  monthString = "April";
                     break;
				case 4:  monthString = "May";
                     break;
				case 5:  monthString = "June";
                     break;
				case 6:  monthString = "July";
                     break;
				case 7:  monthString = "August";
                     break;
				case 8:  monthString = "September";
                     break;
				case 9: monthString = "October";
                     break;
				case 10: monthString = "November";
                     break;
				case 11: monthString = "December";
                     break;
				default: monthString = "Invalid month";
                     break;
				}
				tvDate.setText(monthString + " " + c2.get(Calendar.DAY_OF_MONTH));
				String fullURL = baseURL + monthString + "_" + c2.get(Calendar.DAY_OF_MONTH) +"&format=xml&prop=info|revisions&rvprop=content&inprop=url";
				url2 = new URL(fullURL);
				return url2;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			  return null;
			}
			
	}
	
	public boolean onTouchEvent(MotionEvent event){
		this.mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	
	class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		private static final String DEBUG_TAG = "Gestures";
		
		private static final int SWIPE_MIN_DISTANCE = 120;
		private static final int SWIPE_MAX_OFF_PATH = 250;
		private static final int SWIPE_THRESHOLD_VELOCITY = 200;
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			try {
				//do not do anything if the swipe doesn't reach a certain length of distance
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				
				//right to left swipe
				if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
					//c2 = Calendar.getInstance();
					c2.add(Calendar.DATE, 1);
					//Date newDate = c2.getTime();
			        URL url3  = gimmeURL(c2);
			        //view = new RemoteViews(context.getPackageName(), R.layout.wotd_layout);
			        RetrieveXmlFeed Task = new RetrieveXmlFeed();
			        Task.execute(url3);
					
				}
				else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
					//c2 = Calendar.getInstance();
			        c2.add(Calendar.DATE, -1);
			        URL url4  = gimmeURL(c2);
			        //view = new RemoteViews(context.getPackageName(), R.layout.wotd_layout);
			        RetrieveXmlFeed Task = new RetrieveXmlFeed();
			        Task.execute(url4);
				}
				
			} catch (Exception e) {
				//nothing.
			}
		
		
			return false;
		}
		
		
	}
	
	class RetrieveXmlFeed extends AsyncTask<URL, Void, String[]>{

		private String[] Word = new String[2];
		@Override
		protected String[] doInBackground(URL... params) {
			// TODO Auto-generated method stub
			//getting xmlreader to parse data
			try{
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			ParseXML doingWork = new ParseXML();
			xr.setContentHandler(doingWork);
			
			InputSource is = new InputSource(params[0].openStream());
			//is.getEncoding();
			//Log.i("This is the encoding:",is.getEncoding().toString());
			xr.parse(is);
			Word[0] = doingWork.getWord();
			Word[1] = doingWork.getDefinition();
			return Word;
			}
			catch (Exception e){
				e.printStackTrace();
				Log.i("This is the Exception:",e.toString());
				return null;
			}
			
		}
		
		protected void onPostExecute(String[] result){
			  
			tvWord.setText(result[0]);
			tvDefinition.setText(result[1]);
			Log.d("When this displays", "AsyncTask runs on MainActivity");
			//Log.d("This is the word", resultWord);
		}
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		//String e = tvDate.getText().toString();
		//RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.wotd_layout);
		//c2 = Calendar.getInstance();
		//views.setTextViewText(R.id.tvDate2, c2.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) + " " + c2.get(Calendar.DAY_OF_MONTH));
		Intent i = new Intent(context, UpdateService.class);
		context.startService(i);
		/**
		Intent i = new Intent(context, MainActivity.class);
		PendingIntent pi  = PendingIntent.getActivity(context, 0, i, 0);
		views.setOnClickPendingIntent(R.id.widget, pi);
		
		ComponentName thisWidget = new ComponentName(this, WotdWidget.class);
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		appWidgetManager.updateAppWidget(thisWidget, views);
		**/
		this.finish();
		
		
	}
		
	

		
}
