package mkat.apps.wotd;



import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;


import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.IntentService;
import android.app.PendingIntent;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;


public class WotdWidget extends AppWidgetProvider {
	//Intent i = null; 
	Calendar cal;
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
   
        	Log.d("When this displays", "Service Runs onUpdate");
        	Intent i = new Intent(context, UpdateService.class);
    		context.startService(i);
    		Log.d("When this displays", "Service is done onUpdate");

	  }


	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		Intent i = new Intent(context, UpdateService.class);
		context.startService(i);
	}


	public static class UpdateService extends IntentService { //Service!! Should run every 12 hours using an AlarmManager
		RemoteViews view = null;
		static final String baseURL = "http://en.wiktionary.org/w/api.php?action=query&titles=Wiktionary:Word_of_the_day/";
		Calendar c1;
		String resultWord = "";
		String monthString;
		URL url1;
		
		
		
        public UpdateService() {
           super("UpdateService");
            
        }
		
        @Override
		protected void onHandleIntent(Intent arg0) {
			// TODO Auto-generated method stub
        	//RetrieveXmlFeed Task = new RetrieveXmlFeed();
            //Task.execute();
			
        	String[] Word = new String[2];
        	
        	c1 = Calendar.getInstance();
            
            try {
            	
            	switch (c1.get(Calendar.MONTH)) {
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
            	
            	String fullURL = baseURL + monthString + "_" + c1.get(Calendar.DAY_OF_MONTH) +"&format=xml&prop=info|revisions&rvprop=content&inprop=url";
            	url1 = new URL(fullURL);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//getting xmlreader to parse data
            
            	try{
            		SAXParserFactory spf = SAXParserFactory.newInstance();
            		SAXParser sp = spf.newSAXParser();
            		XMLReader xr = sp.getXMLReader();
            		ParseXML doingWork = new ParseXML();
            		xr.setContentHandler(doingWork);
			
			
            		InputSource is = new InputSource(url1.openStream());
            		//is.getEncoding();
            		//Log.i("This is the encoding:",is.getEncoding().toString());
            		xr.parse(is);
            		view = new RemoteViews(this.getPackageName(), R.layout.wotd_layout);
            		view.setTextViewText(R.id.tvWordWidget, doingWork.getWord()); 
            		view.setTextViewText(R.id.tvDate2,monthString + " " + c1.get(Calendar.DAY_OF_MONTH));
            		Word[1] = doingWork.getDefinition();
            		Intent defineIntent = new Intent(this, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this,
                            0 /* no requestCode */, defineIntent, 0 /* no flags */);
                    view.setOnClickPendingIntent(R.id.widget, pendingIntent);  
                    
                    ComponentName thisWidget = new ComponentName(this, WotdWidget.class);
                    AppWidgetManager manager = AppWidgetManager.getInstance(this);
                    manager.updateAppWidget(thisWidget, view);
            		//Log.d("test","test");
            		//resultWord = Word[0];
            		//return Word;
            	}
            
			catch (Exception e){
				e.printStackTrace();
				//Log.i("This is the Exception:",e.toString());
				//return null;
			}
            
            
            
		}
      
   
        
    		
        
    }
		
		
		
	}	
		

	
