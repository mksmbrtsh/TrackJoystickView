package maximsblog.blogspot.com.trackjoystickview;

import maximsblog.blogspot.com.trackjoystickview.TrackJoystickView.OnTrackJoystickViewMoveListener;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final TextView status = (TextView)findViewById(R.id.status);
		TrackJoystickView joystick = (TrackJoystickView) findViewById(R.id.trackjoystickView);
		joystick.setOnTrackJoystickViewMoveListener(
				new OnTrackJoystickViewMoveListener() {
					@Override
					public void onValueChanged(int y1, int y2) {
						status.setText("L:" + String.valueOf(y1) +" R:" + String.valueOf(y2));
					}
				}, TrackJoystickView.DEFAULT_LOOP_INTERVAL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
