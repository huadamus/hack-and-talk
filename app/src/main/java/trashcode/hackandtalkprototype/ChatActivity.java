package trashcode.hackandtalkprototype;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ChatActivity extends Activity {

    GestureManager gestureManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        gestureManager = new GestureManager(this);
    }

    void updateGesture(Gesture gesture) {
        TextView gestureDetectorTextView = findViewById(R.id.gestureDetectorTextView);
        switch (gesture) {
            case WAVE:
                gestureDetectorTextView.setText(getString(R.string.gesture_wave));
                break;
            case POINT_RIGHT:
                gestureDetectorTextView.setText(getString(R.string.gesture_point_right));
                break;
            case POINT_LEFT:
                gestureDetectorTextView.setText(getString(R.string.gesture_point_left));
                break;
        }
    }

    //debug method
    void debugGesture(String debug) {
        TextView gestureDetectorTextView = findViewById(R.id.gestureDetectorTextView);
        gestureDetectorTextView.setText(debug);
    }
}