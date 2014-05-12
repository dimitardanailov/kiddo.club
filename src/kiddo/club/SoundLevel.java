package kiddo.club;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.util.Log;
import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SoundLevel extends DefaultActivity implements OnClickListener {

	// Debug
	private static String TAG = SoundLevel.class.getName();
	
	// Decibels
	public static final int SAMPLE_RATE = 16000;

    // The AudioRecord class manages the audio resources for Java applications
    // to record audio from the audio input hardware of the platform.
	private AudioRecord mRecorder;

    // An "abstract" representation of a file system entity identified by a pathname.
    // The pathname may be absolute (relative to the root directory of the file system) or
    // relative to the current directory in which the program is running.
    private File mRecording;

    // Arrays
	private short[] mBuffer;

    // Labels strings
    private final String startRecordingLabel = "Start recording";
	private final String stopRecordingLabel = "Stop recording";

    // Booleans
    private boolean mIsRecording = false;
	
	// Layout elements
	private ProgressBar mProgressBar;
	private ImageButton recordingButton;
	
	// Storage info
	private static final String STORAGE_FOLDER = "Kiddo";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_kiddo_sound_level);

		initRecorder();
		
		initializeLayoutElements();
	}
	
	/**
	 * Initialize recording settings
	 */
	private void initRecorder() {
		int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		mBuffer = new short[bufferSize];
		mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT, bufferSize);
	}
	
	
	/**
	 * Initialize all layout elements
	 */
	private void initializeLayoutElements() {
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		// Calculate maximum units of progress bar
		int progressBarMaxim = SAMPLE_RATE / 4;
		mProgressBar.setMax(progressBarMaxim);
		//Toast.makeText(SoundLevel.this, "progressBarMaxim : " + progressBarMaxim, Toast.LENGTH_SHORT).show();
		
		recordingButton = (ImageButton) findViewById(R.id.recordingButton);
		recordingButton.setContentDescription(startRecordingLabel);
		
		initializeClickListeners();
	}
	
	/**
	 * Initialize Click Listeners
	 */
	private void initializeClickListeners() {
		recordingButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.recordingButton) {
			loadSoundLevelRecognition();
		}
	}

	/**
	 * User can start or stop sound decibels recording
	 */
	private void loadSoundLevelRecognition() {
		
		if (!mIsRecording) {
			startSoundLevelRecognition();
		}
		else {
			stopSoundLevelRecognition();
		}
	}
	
	/**
	 * Start sound level recognition.
	 * Create raw file and try to read buffer.
	 */
	private void startSoundLevelRecognition() {
		recordingButton.setContentDescription(stopRecordingLabel);
		recordingButton.setImageResource(R.drawable.stop_sound_level_recognition);
		mIsRecording = true;
		mRecorder.startRecording();
		mRecording = getFile("raw");
		startBufferedWrite(mRecording);
	}
	
	/**
	 * Get File name
	 * @param suffix file type
	 * @return
	 */
	private File getFile(final String suffix) {
		
		// Generate Unique name using current Time
		Time time = new Time();
		time.setToNow();
		String timeToString = time.format("%Y%m%d%H%M%S");
		
		// Create File path
		String folderPath = Environment.getExternalStorageDirectory() + "/" + STORAGE_FOLDER + "/"; 
		
		return new File(Environment.getExternalStorageDirectory(), time.format("%Y%m%d%H%M%S") + "." + suffix);
	}
	
	/**
	 * Update progress bar
	 * @param file
	 */
	private void startBufferedWrite(final File file) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				DataOutputStream output = null;
				try {
					output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
					while (mIsRecording) {
						double sum = 0;
						int readSize = mRecorder.read(mBuffer, 0, mBuffer.length);
						for (int i = 0; i < readSize; i++) {
							output.writeShort(mBuffer[i]);
							sum += mBuffer[i] * mBuffer[i];
						}
						if (readSize > 0) {
							final double amplitude = sum / readSize;
							mProgressBar.setProgress((int) Math.sqrt(amplitude));
						}
					}
				} catch (IOException e) {
					Toast.makeText(SoundLevel.this, e.getMessage(), Toast.LENGTH_SHORT).show();
				} finally {
					mProgressBar.setProgress(0);
					if (output != null) {
						try {
							output.flush();
						} catch (IOException e) {
							Toast.makeText(SoundLevel.this, e.getMessage(), Toast.LENGTH_SHORT)
									.show();
						} finally {
							try {
								output.close();
							} catch (IOException e) {
								Toast.makeText(SoundLevel.this, e.getMessage(), Toast.LENGTH_SHORT)
										.show();
							}
						}
					}
				}
			}
		}).start();
	}
	
	/**
	 * Stop sound level recognition.
	 * Create wave file from raw file
	 */
	private void stopSoundLevelRecognition() {
		recordingButton.setContentDescription(startRecordingLabel);
		recordingButton.setImageResource(R.drawable.start_sound_level_recognition);
		
		mIsRecording = false;
		mRecorder.stop();
		File waveFile = getFile("wav");
		
		try {
			rawToWave(mRecording, waveFile);
		} catch (IOException e) {
			Toast.makeText(SoundLevel.this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		
		//String filePath = STORAGE_FOLDER + "/" + waveFile.getName();
		String filePath = waveFile.getName();
		Toast.makeText(SoundLevel.this, "Recorded to " + filePath, Toast.LENGTH_SHORT).show();
	}

	private void rawToWave(final File rawFile, final File waveFile) throws IOException {

		byte[] rawData = new byte[(int) rawFile.length()];
		DataInputStream input = null;
		try {
			input = new DataInputStream(new FileInputStream(rawFile));
			input.read(rawData);
		} finally {
			if (input != null) {
				input.close();
			}
		}

		DataOutputStream output = null;
		try {
			output = new DataOutputStream(new FileOutputStream(waveFile));
			// WAVE header
			// see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
			writeString(output, "RIFF"); // chunk id
			writeInt(output, 36 + rawData.length); // chunk size
			writeString(output, "WAVE"); // format
			writeString(output, "fmt "); // subchunk 1 id
			writeInt(output, 16); // subchunk 1 size
			writeShort(output, (short) 1); // audio format (1 = PCM)
			writeShort(output, (short) 1); // number of channels
			writeInt(output, SAMPLE_RATE); // sample rate
			writeInt(output, SAMPLE_RATE * 2); // byte rate
			writeShort(output, (short) 2); // block align
			writeShort(output, (short) 16); // bits per sample
			writeString(output, "data"); // subchunk 2 id
			writeInt(output, rawData.length); // subchunk 2 size
			// Audio data (conversion big endian -> little endian)
			short[] shorts = new short[rawData.length / 2];
			ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
			ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
			for (short s : shorts) {
				bytes.putShort(s);
			}
			output.write(bytes.array());
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

	private void writeInt(final DataOutputStream output, final int value) throws IOException {
		output.write(value >> 0);
		output.write(value >> 8);
		output.write(value >> 16);
		output.write(value >> 24);
	}

	private void writeShort(final DataOutputStream output, final short value) throws IOException {
		output.write(value >> 0);
		output.write(value >> 8);
	}

	private void writeString(final DataOutputStream output, final String value) throws IOException {
		for (int i = 0; i < value.length(); i++) {
			output.write(value.charAt(i));
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		loadHomePageMainMenu(this);
		
		return true;
	}
}
