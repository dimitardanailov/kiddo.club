package kiddo.club;

import android.os.Bundle;
import android.view.Menu;
import android.view.Window;

public class KiddoKinderKitchensActivity extends DefaultActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_kiddo_kitchen);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		loadHomePageMainMenu(this);
		
		return true;
	}
}
