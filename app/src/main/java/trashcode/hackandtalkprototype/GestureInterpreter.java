package trashcode.hackandtalkprototype;

import android.util.Log;
import java.util.ArrayList;

final class GestureInterpreter {

    private final GestureManager gestureManager;
    private final ArrayList<GestureAnalyzer> gestureAnalyzers;

    GestureInterpreter(GestureManager gestureManager) {
        this.gestureManager = gestureManager;
        gestureAnalyzers = new ArrayList<>();
        gestureAnalyzers.add(new WaveGesture());
        gestureAnalyzers.add(new PointGesture());
        gestureAnalyzers.add(new RotationGesture());
        gestureAnalyzers.add(new DiagonalGesture());
    }

    void analyzeValues(float[] values) {
        for(int i = 0; i < values.length; i++) {
            values[i] = (float)Math.ceil(values[i]);
        }
        Log.d("V", (values[0] + " " + values[1] + " " + values[2]));
        for(GestureAnalyzer gestureAnalyzer : gestureAnalyzers) {
            gestureAnalyzer.update(values);
        }
    }

    private interface GestureAnalyzer {
        void update(float[] values);
    }

    private class WaveGesture implements GestureAnalyzer {

        private final float MAX_RANGE = 5.0f;
        private final int MINIMAL_OCCURRENCES = 4;

        private float[] values = new float[3];
        private boolean directionRight;
        private float range;
        private int occurrences;

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
                        gestureManager.chatActivity.informAboutGesture(Gesture.WAVE);
                    }
                    if(range >= MAX_RANGE) {
                        occurrences++;
                    }
                    else {
                        occurrences = 0;
                    }
                    range = 0;
                    directionRight = !directionRight;
                }
                System.arraycopy(values, 0, this.values, 0, this.values.length);
            }
        }
    }

    private class PointGesture implements GestureAnalyzer {

        private final float MIN_RANGE = 8.0f;
        private final float MAX_RANGE = 11.0f;
        private final int MINIMAL_OCCURRENCES = 80;

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
                    gestureManager.chatActivity.informAboutGesture(Gesture.POINT_RIGHT);
                }
                else {
                    gestureManager.chatActivity.informAboutGesture(Gesture.POINT_LEFT);
                }
            }
        }
    }

    private class RotationGesture implements GestureAnalyzer {

        private float[] values = new float[3];
        private int stage = 0;

        @Override
        public void update(float[] values) {
            if(this.values[0] == 0.0f && this.values[1] == 0.0f && this.values[2] == 0.0f) {
                System.arraycopy(values, 0, this.values, 0, this.values.length);
            }
            else {
                if(stage == 0) {
                    if(values[1] == 10.0f && Math.abs(values[0]) <= 2.0f) {
                        stage = 1;
                    }
                }
                else if(stage == 1) {
                    if(values[1] == -10.0f && Math.abs(values[0]) <= 2.0f) {
                        stage = 2;
                    }
                }
                else if(stage == 2) {
                    if(values[1] == 10.0f && Math.abs(values[0]) <= 2.0f) {
                        stage = 1;
                        gestureManager.chatActivity.informAboutGesture(Gesture.ROTATION);
                    }
                }
            }
        }
    }

    private class DiagonalGesture implements GestureAnalyzer {

        private final float MIN_RANGE_DIAGONAL = 6.0f;
        private final float MIN_RANGE_VERTICAL = 8.0f;
        private final int MINIMAL_OCCURRENCES = 30;

        private int occurrences;

        @Override
        public void update(float[] values) {
            if(values[0] >= MIN_RANGE_DIAGONAL && values[1] >= MIN_RANGE_VERTICAL) {
                occurrences++;
            }
            else {
                occurrences = 0;
            }
            if(occurrences == MINIMAL_OCCURRENCES) {
                gestureManager.chatActivity.informAboutGesture(Gesture.DIAGONAL);
            }
        }
    }
}