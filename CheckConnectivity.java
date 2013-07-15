package mkat.apps.wotd;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
 
public class CheckConnectivity{
    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
 
    /** BORROWED CODE FROM http://www.andygup.net/check-network-connectivity-in-your-android-app/
     * Check for <code>TYPE_WIFI</code> and <code>TYPE_MOBILE</code> connection using <code>isConnected()</code>
     * Checks for generic Exceptions and writes them to logcat as <code>CheckConnectivity Exception</code>. 
     * Make sure AndroidManifest.xml has appropriate permissions.
     * @param con Application context
     * @return Boolean
     */
    public Boolean checkNow(Context con){
         
        try{
            connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);   
             
            if(wifiInfo.isConnected() || mobileInfo.isConnected())
            {
                return true;
            }
        }
        catch(Exception e){
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
        }
         
        return false;
    }   
}