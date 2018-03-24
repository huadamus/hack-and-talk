package trashcode.hackandtalkprototype;

import java.util.ArrayList;

final class GestureInterpreter {

    private final ChatActivity chatActivity;
    private final ArrayList<GestureAnalyzer> gestureAnalyzers;

    GestureInterpreter(ChatActivity chatActivity) {
        this.chatActivity = chatActivity;
        gestureAnalyzers = new ArrayList<>();
        gestureAnalyzers.add(new WaveGesture());
        gestureAnalyzers.add(new PointGesture());
    }

    void analyzeValues(float[] values) {
        for(int i = 0; i < values.length; i++) {
            values[i] = (float)Math.ceil(values[i]);
        }
        //chatActivity.debugGesture(values[0] + " " + values[1] + " " + values[2]);
        for(GestureAnalyzer gestureAnalyzer : gestureAnalyzers) {
            gestureAnalyzer.update(values);
        }
    }

    private interface GestureAnalyzer {
        void update(float[] values);
    }

    private class WaveGesture implements GestureAnalyzer {

        final float MAX_RANGE = 2.0f;
        final int MINIMAL_OCCURRENCES = 3;

        private float[] values = new float[3];
        boolean directionRight;
        float range;
        int occurrences;

        @Override
        public void update(float[] values) {
            if(this.values[0] == 0.0f && this.values[1] == 0.0f && this.values[2] == 0.0f) {
                System.arraycopy(values, 0, this.values, 0, this.values.length);
            }
            else {
                boolean changeDirection = false;
                if(values[0] > this.values[0]) {
                    if(!directionRight) {
                        range += values[0] - this.values[0];
                    }
                    else {
                        changeDirection = true;
                    }
                }
                else if(values[0] < this.values[0]) {
                    if(directionRight) {
                        range += this.values[0] - values[0];
                    }
                    else {
                        changeDirection = true;
                    }
                }
                if(changeDirection) {
                    if(occurrences == MINIMAL_OCCURRENCES) {
                        chatActivity.updateGesture(Gesture.WAVE);
                        occurrences = 0;
                    }
                    if(range >= MAX_RANGE) {
                        occurrences++;
                    }
                    else {
                        occurrences = 0;
                    }
                    range = 0;
                }
                System.arraycopy(values, 0, this.values, 0, this.values.length);
            }
        }
    }

    private class PointGesture implements GestureAnalyzer {

        final float MIN_RANGE = 8.0f;
        final float MAX_RANGE = 11.0f;
        final int MINIMAL_OCCURRENCES = 50;

        private boolean pointingRight;
        private int occurrences;

        @Override
        public void update(float[] values) {
            if(values[0] >= MIN_RANGE && values[0] <= MAX_RANGE) {
                if(!pointingRight) {
                    occurrences = 0;
                }
                pointingRight = true;
                occurrences++;
            }
            else if(values[0] <= -MIN_RANGE && values[0] >= -MAX_RANGE) {
                if(pointingRight) {
                    occurrences = 0;
                }
                pointingRight = false;
                occurrences++;
            }
            else {
                occurrences = 0;
            }
            if(occurrences == MINIMAL_OCCURRENCES) {
                if(pointingRight) {
                    chatActivity.updateGesture(Gesture.POINT_RIGHT);
                }
                else {
                    chatActivity.updateGesture(Gesture.POINT_LEFT);
                }
            }
        }
    }
}
