package kiddo.club;

import kiddo.club.KiddoMainActivity.PlaceholderFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Create this Class from tutorial : 
 * http://www.itcuties.com/android/how-to-create-android-splash-screen/
 */

public class SplashActivity extends DefaultActivity {

	private static String TAG = SplashActivity.class.getName();
	private static long SLEEP_TIME = 1; // Sleep for some time

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        
		setContentView(R.layout.fragment_kiddo_splash_screen);
		
		// Start timer and launch in activity
		IntentLauncher launcher = new IntentLauncher();
		launcher.start();
	}

	private class IntentLauncher extends Thread {

		/**
		 * Sleep for some time and than start new activity.
		 */
		@Override 
		public void run() 
		{
			try 
			{
				// Sleeping
				Thread.sleep(SLEEP_TIME * 5000);
			}
			catch(InterruptedException e)
			{
				Log.e(TAG, e.getMessage());
			}

			// Run next activity
			Intent intent = new Intent(SplashActivity.this, KiddoMainActivity.class);
			SplashActivity.this.startActivity(intent);
			SplashActivity.this.finish();
		}
	}
}
