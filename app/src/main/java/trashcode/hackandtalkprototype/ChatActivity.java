package trashcode.hackandtalkprototype;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChatActivity extends Activity {

    private ConnectionManager connectionManager;
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //noinspection all
        GestureManager gestureManager = new GestureManager(this);
        connectionManager = new ConnectionManager(this);
        final Button loginSendButton = findViewById(R.id.loginSendButton);
        loginSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText messageEditText = findViewById(R.id.messageEditText);
                if(!loggedIn) {
                    if(!messageEditText.getText().toString().equals("")) {
                        connectionManager.login(messageEditText.getText().toString());
                        loggedIn = true;
                        TextView loginMessageTextView = findViewById(R.id.loginMessageTextView);
                        loginMessageTextView.setText("");
                        loginSendButton.setText(R.string.button_send);
                    }
                }
                else {
                    connectionManager.sendMessage(messageEditText.getText().toString());
                }
                messageEditText.setText("");
            }
        });
    }

    void addMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView loginMessageTextView = findViewById(R.id.loginMessageTextView);
                if(!loginMessageTextView.getText().equals("")) {
                    loginMessageTextView.append(System.lineSeparator());
                }
                loginMessageTextView.append(message);
            }
        });
    }

    void informAboutGesture(Gesture gesture) {
        if(loggedIn) {
            switch (gesture) {
                case WAVE:
                    connectionManager.sendMessage(getString(R.string.gesture_wave));
                    break;
                case POINT_RIGHT:
                    connectionManager.sendMessage(getString(R.string.gesture_point_right));
                    break;
                case POINT_LEFT:
                    connectionManager.sendMessage(getString(R.string.gesture_point_left));
                    break;
                case ROTATION:
                    connectionManager.sendMessage(getString(R.string.gesture_rotation));
                    break;
                case DIAGONAL:
                    connectionManager.sendMessage(getString(R.string.gesture_diagonal));
                    break;
            }
        }
    }
}