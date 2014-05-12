package kiddo.club;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class KiddoMainActivity extends DefaultActivity implements OnClickListener {

	// Layout elements
	ImageButton buttonDoctor;
	ImageButton buttonVideoCamera;
	ImageButton buttonKinderKitchen;
	ImageButton buttonCryDetector;
	ImageButton buttonNannies;
	ImageButton buttonForum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_kiddo_main);
		
		initializeLayoutElements();
	}

	/**
	 * Initialize all layout elements
	 */
	private void initializeLayoutElements() {
		buttonDoctor = (ImageButton) findViewById(R.id.button_doctor);
		buttonVideoCamera = (ImageButton) findViewById(R.id.button_video_camera);
		buttonKinderKitchen = (ImageButton) findViewById(R.id.button_kinder_kitchen);
		buttonCryDetector = (ImageButton) findViewById(R.id.button_cry_detector);
		buttonNannies = (ImageButton) findViewById(R.id.button_nannies);
		buttonForum = (ImageButton) findViewById(R.id.button_forum);
		
		initializeClickListeners();
	}

	/**
	 * Initialize Click Listeners
	 */
	private void initializeClickListeners() {
		buttonDoctor.setOnClickListener(this);
		buttonVideoCamera.setOnClickListener(this);
		buttonKinderKitchen.setOnClickListener(this);
		buttonCryDetector.setOnClickListener(this);
		buttonNannies.setOnClickListener(this);
		buttonForum.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		Intent intent = null;
		
		switch (id) {

		// Doctor
		case R.id.button_doctor:
			intent = new Intent(this, KiddoDoctorActivity.class);
			break;

		// Video camera
		case R.id.button_video_camera:
			intent = new Intent(this, KiddoVideoCameraActivity.class);
			break;

		// Kinder Kitchens
		case R.id.button_kinder_kitchen:
			intent = new Intent(this, KiddoKinderKitchensActivity.class);
			break;

		// Cry Detector
		case R.id.button_cry_detector:
			intent = new Intent(this, SoundLevel.class);
			break;

		// Nannies
		case R.id.button_nannies:
			intent = new Intent(this, KiddoNannies.class);
			break;

	    // Forum
		case R.id.button_forum:
			intent = new Intent(this, KiddoForumActivity.class);
			break;
			
		default:
			break;
		} // switch (id)
		
		// Run next activity
		if (intent != null) {
			startActivity(intent);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
}
