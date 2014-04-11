## TrackJoystickView - Android
![mksmbrtsh](http://1.bp.blogspot.com/-KE_eLk23Gxg/U0g_7MS2t2I/AAAAAAAAJO0/Pe6q53Qjo3Y/s1600/screen.png)

Android TrackJoystickView is a Custom View that simulates a two multitouch joysticks for interactive control two motors for Android, 
as simple aim is allows access values ​​of power tracks of the virtual Joystck movement.

Power range from -100 to 100, a zero is neuntals;

Project base on JoystickView via zerokol:
https://github.com/zerokol/JoystickView/

Api level > 11

### SHOW THE CODE

From this point you can inflate the TrackJoystickView in your layouts or referencing it in your Activities.

```java
package maximsblog.blogspot.com.trackjoystickview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TrackJoystickView extends View implements Runnable {
	// Constants
	public final static long DEFAULT_LOOP_INTERVAL = 100; // 100 ms
	// Variables
	private OnTrackJoystickViewMoveListener onTrackJoystickViewMoveListener; // Listener
	private Thread thread = new Thread(this);
	private long loopInterval = DEFAULT_LOOP_INTERVAL;
	private int xPosition1 = 0; // Touch x1 track position
	private int xPosition2 = 0; // Touch x2 track position
	private int yPosition1 = 0; // Touch y1 track position
	private int yPosition2 = 0; // Touch y2 track position
	private double centerX1 = 0; // Center view x1 position
	private double centerY1 = 0; // Center view y1 position
	private double centerX2 = 0; // Center view x2 position
	private double centerY2 = 0; // Center view y2 position
	private Paint mButton;
	private Paint mLine;
	private int joystickRadius;
	private int buttonRadius;
	private int leftTrackTouch;
	private int rightTrackTouch;

	public TrackJoystickView(Context context) {
		super(context);
	}

	public TrackJoystickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initJoystickView();
	}

	public TrackJoystickView(Context context, AttributeSet attrs,
			int defaultStyle) {
		super(context, attrs, defaultStyle);
		initJoystickView();
	}

	protected void initJoystickView() {
		mLine = new Paint();
		mLine.setStrokeWidth(10);
		mLine.setColor(Color.DKGRAY);

		mButton = new Paint(Paint.ANTI_ALIAS_FLAG);
		mButton.setColor(Color.DKGRAY);
		mButton.setStyle(Paint.Style.FILL);
	}

	@Override
	protected void onFinishInflate() {
	}

	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
		super.onSizeChanged(xNew, yNew, xOld, yOld);
		buttonRadius = (int) (yNew / 2 * 0.25);
		joystickRadius = yNew - 2 * buttonRadius; // (int) (yNew / 2 * 0.75);
		// before measure, get the center of view
		xPosition1 = xNew - 2 * buttonRadius;
		xPosition2 = xNew * buttonRadius;
		yPosition1 = (int) yNew / 2;
		yPosition2 = (int) yNew / 2;
		centerX1 = getWidth() - 1 * buttonRadius;
		centerY1 = (getHeight()) / 2;

		centerX2 = 1 * buttonRadius;
		centerY2 = (getHeight()) / 2;

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// setting the measured values to resize the view to a certain width and
		// height
		int d = Math.min(measure(widthMeasureSpec), measure(heightMeasureSpec));

		setMeasuredDimension(d, d);

	}

	private int measure(int measureSpec) {
		int result = 0;

		// Decode the measurement specifications.
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.UNSPECIFIED) {
			// Return a default size of 200 if no bounds are specified.
			result = 200;
		} else {
			// As you want to fill the available space
			// always return the full available bounds.
			result = specSize;
		}
		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint p = new Paint();
		p.setColor(Color.CYAN);

		canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), p);
		// buttons
		canvas.drawCircle((float) centerX1, yPosition1, buttonRadius, mButton);
		canvas.drawCircle((float) centerX2, yPosition2, buttonRadius, mButton);
		// vertical lines
		canvas.drawLine((float) centerX1,
				(float) centerY1 + joystickRadius / 2, (float) centerX1,
				(float) (centerY1 - joystickRadius / 2), mLine);
		canvas.drawLine((float) centerX2,
				(float) centerY2 + joystickRadius / 2, (float) centerX2,
				(float) (centerY2 - joystickRadius / 2), mLine);
		// main horisontal lines
		// neuntral
		canvas.drawLine((float) (centerX1 - buttonRadius * 0.75),
				(float) centerY1, (float) (centerX1 + buttonRadius * 0.75),
				(float) centerY1, mLine);
		canvas.drawLine((float) (centerX2 - buttonRadius * 0.75),
				(float) centerY2, (float) (centerX2 + buttonRadius * 0.75),
				(float) centerY2, mLine);
		// low
		canvas.drawLine((float) (centerX1 - buttonRadius * 0.6),
				(float) centerY1 + joystickRadius / 2,
				(float) (centerX1 + buttonRadius * 0.6), (float) centerY1
						+ joystickRadius / 2, mLine);
		canvas.drawLine((float) (centerX2 - buttonRadius * 0.6),
				(float) centerY2 + joystickRadius / 2,
				(float) (centerX2 + buttonRadius * 0.6), (float) centerY2
						+ joystickRadius / 2, mLine);
		// high
		canvas.drawLine((float) (centerX1 - buttonRadius * 0.6),
				(float) centerY1 - joystickRadius / 2,
				(float) (centerX1 + buttonRadius * 0.6), (float) centerY1
						- joystickRadius / 2, mLine);
		canvas.drawLine((float) (centerX2 - buttonRadius * 0.6),
				(float) centerY2 - joystickRadius / 2,
				(float) (centerX2 + buttonRadius * 0.6), (float) centerY2
						- joystickRadius / 2, mLine);
		// second horisontal lines
		canvas.drawLine((float) (centerX1 - 0.4 * buttonRadius),
				(float) (centerY1 + (joystickRadius) / 4),
				(float) (centerX1 + 0.4 * buttonRadius),
				(float) (centerY1 + (joystickRadius) / 4), mLine);
		canvas.drawLine((float) (centerX2 - 0.4 * buttonRadius),
				(float) (centerY2 + (joystickRadius) / 4),
				(float) (centerX2 + 0.4 * buttonRadius),
				(float) (centerY2 + (joystickRadius) / 4), mLine);

		canvas.drawLine((float) (centerX1 - 0.4 * buttonRadius),
				(float) (centerY1 - (joystickRadius) / 4),
				(float) (centerX1 + 0.4 * buttonRadius),
				(float) (centerY1 - (joystickRadius) / 4), mLine);
		canvas.drawLine((float) (centerX2 - 0.4 * buttonRadius),
				(float) (centerY2 - (joystickRadius) / 4),
				(float) (centerX2 + 0.4 * buttonRadius),
				(float) (centerY2 - (joystickRadius) / 4), mLine);
		//
		canvas.drawLine((float) (centerX1 - 0.4 * buttonRadius),
				(float) (centerY1 - (joystickRadius) / 8),
				(float) (centerX1 + 0.4 * buttonRadius),
				(float) (centerY1 - (joystickRadius) / 8), mLine);
		canvas.drawLine((float) (centerX2 - 0.4 * buttonRadius),
				(float) (centerY2 - (joystickRadius) / 8),
				(float) (centerX2 + 0.4 * buttonRadius),
				(float) (centerY2 - (joystickRadius) / 8), mLine);

		canvas.drawLine((float) (centerX1 - 0.4 * buttonRadius),
				(float) (centerY1 - 3 * (joystickRadius) / 8),
				(float) (centerX1 + 0.4 * buttonRadius),
				(float) (centerY1 - 3 * (joystickRadius) / 8), mLine);
		canvas.drawLine((float) (centerX2 - 0.4 * buttonRadius),
				(float) (centerY2 - 3 * (joystickRadius) / 8),
				(float) (centerX2 + 0.4 * buttonRadius),
				(float) (centerY2 - 3 * (joystickRadius) / 8), mLine);
		//
		canvas.drawLine((float) (centerX1 - 0.4 * buttonRadius),
				(float) (centerY1 + (joystickRadius) / 8),
				(float) (centerX1 + 0.4 * buttonRadius),
				(float) (centerY1 + (joystickRadius) / 8), mLine);
		canvas.drawLine((float) (centerX2 - 0.4 * buttonRadius),
				(float) (centerY2 + (joystickRadius) / 8),
				(float) (centerX2 + 0.4 * buttonRadius),
				(float) (centerY2 + (joystickRadius) / 8), mLine);

		canvas.drawLine((float) (centerX1 - 0.4 * buttonRadius),
				(float) (centerY1 + 3 * (joystickRadius) / 8),
				(float) (centerX1 + 0.4 * buttonRadius),
				(float) (centerY1 + 3 * (joystickRadius) / 8), mLine);
		canvas.drawLine((float) (centerX2 - 0.4 * buttonRadius),
				(float) (centerY2 + 3 * (joystickRadius) / 8),
				(float) (centerX2 + 0.4 * buttonRadius),
				(float) (centerY2 + 3 * (joystickRadius) / 8), mLine);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//
		int actionMask = event.getActionMasked();

		switch (actionMask) {
		case MotionEvent.ACTION_DOWN: { // first down
			int i1 = event.getActionIndex();
			int y = (int) event.getY(i1);
			int x = (int) event.getX(i1);
			double abs1 = Math.sqrt((x - centerX1) * (x - centerX1)
					+ (y - centerY1) * (y - centerY1));
			double abs2 = Math.sqrt((x - centerX2) * (x - centerX2)
					+ (y - centerY2) * (y - centerY2));
			if (abs1 < abs2) {
				yPosition1 = y;
				leftTrackTouch = event.getPointerId(i1);
				if (abs1 > joystickRadius / 2) {
					yPosition1 = (int) ((yPosition1 - centerY1)
							* joystickRadius / 2 / abs1 + centerY1);
				}
			} else {
				yPosition2 = y;
				rightTrackTouch = event.getPointerId(i1);
				if (abs2 > joystickRadius / 2) {
					yPosition2 = (int) ((yPosition2 - centerY2)
							* joystickRadius / 2 / abs2 + centerY2);
				}
			}
			invalidate();
			if (thread != null && thread.isAlive()) {
				thread.interrupt();
			}
			thread = new Thread(this);
			thread.start();
		}
			break;
		case MotionEvent.ACTION_POINTER_DOWN: { // next downs
			for (int i1 = 0; i1 < event.getPointerCount(); i1++) {
				int y = (int) event.getY(i1);
				int x = (int) event.getX(i1);

				if (leftTrackTouch == -1 && rightTrackTouch != event.getPointerId(i1)) {
					leftTrackTouch = event.getPointerId(i1);
				} else if (rightTrackTouch == -1 && leftTrackTouch != event.getPointerId(i1)) {
					rightTrackTouch = event.getPointerId(i1);
				}

				if (leftTrackTouch == event.getPointerId(i1)) {
					double abs1 = Math.sqrt((x - centerX1) * (x - centerX1)
							+ (y - centerY1) * (y - centerY1));
					yPosition1 = y;
					if (abs1 > joystickRadius / 2) {
						yPosition1 = (int) ((yPosition1 - centerY1)
								* joystickRadius / 2 / abs1 + centerY1);
					}
				} else if (rightTrackTouch == event.getPointerId(i1)) {
					double abs2 = Math.sqrt((x - centerX2) * (x - centerX2)
							+ (y - centerY2) * (y - centerY2));
					yPosition2 = y;
					if (abs2 > joystickRadius / 2) {
						yPosition2 = (int) ((yPosition2 - centerY2)
								* joystickRadius / 2 / abs2 + centerY2);
					}
				}
			}
			invalidate();
		}
			break;
		case MotionEvent.ACTION_UP: { // last up
			yPosition1 = (int) centerY1;
			yPosition2 = (int) centerY2;
			rightTrackTouch = -1;
			leftTrackTouch = -1;
			invalidate();
			thread.interrupt();
			onTrackJoystickViewMoveListener.onValueChanged(getPower2(),
					getPower1());
		}
			break;
		case MotionEvent.ACTION_POINTER_UP: { // next up
				int i = event.getPointerId(event.getActionIndex());
				if (leftTrackTouch == event.getPointerId(i)) {
					yPosition1 = (int) centerY1;
					leftTrackTouch = -1;

				} else if (rightTrackTouch == event.getPointerId(i)) {
					yPosition2 = (int) centerY2;
					rightTrackTouch = -1;
				}
			invalidate();

		}
			break;

		case MotionEvent.ACTION_MOVE: { // moving
			for (int i1 = 0; i1 < event.getPointerCount(); i1++) {
				int y = (int) event.getY(i1);
				int x = (int) event.getX(i1);
				if (leftTrackTouch == event.getPointerId(i1)) {
					double abs1 = Math.sqrt((x - centerX1) * (x - centerX1)
							+ (y - centerY1) * (y - centerY1));
					yPosition1 = y;
					if (abs1 > joystickRadius / 2) {
						yPosition1 = (int) ((yPosition1 - centerY1)
								* joystickRadius / abs1 / 2 + centerY1);
					}
				} else if (rightTrackTouch == event.getPointerId(i1)) {
					double abs2 = Math.sqrt((x - centerX2) * (x - centerX2)
							+ (y - centerY2) * (y - centerY2));
					yPosition2 = y;
					if (abs2 > joystickRadius / 2) {
						yPosition2 = (int) ((yPosition2 - centerY2)
								* joystickRadius / abs2 / 2 + centerY2);
					}
				}
			}
			invalidate();
		}
			break;
		}
		return true;
	}

	private int getPower1() {
		int y = yPosition1 - buttonRadius;

		return (int) Math.round(100.0 - y * 200.0 / (getHeight() - 2.0 * buttonRadius));
	}

	private int getPower2() {
		int y = yPosition2 - buttonRadius;

		return (int)Math.round(100.0 - y * 200.0 / (getHeight() - 2.0 * buttonRadius));
	}

	public void setOnTrackJoystickViewMoveListener(
			OnTrackJoystickViewMoveListener listener, long repeatInterval) {
		this.onTrackJoystickViewMoveListener = listener;
		this.loopInterval = repeatInterval;
	}

	public static interface OnTrackJoystickViewMoveListener {
		public void onValueChanged(int y1, int y2);
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			post(new Runnable() {
				public void run() {
					onTrackJoystickViewMoveListener.onValueChanged(getPower2(),
							getPower1());
				}
			});
			try {
				Thread.sleep(loopInterval);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
