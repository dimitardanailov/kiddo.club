package kiddo.club;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class DefaultActivity extends Activity {
	
	protected void loadHomePageMainMenu(Context context) {
		// Run next activity
		Intent intent = new Intent(context, KiddoMainActivity.class);
		context.startActivity(intent);
	}
}
