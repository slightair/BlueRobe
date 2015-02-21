package cc.clv.BlueRobe.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import cc.clv.BlueRobe.BlueRobe;
import rx.Observable;
import rx.Observer;

/**
 * Created by slightair on 15/02/21.
 */
public class GameSceneInput extends InputMultiplexer {

    private class GestureListener implements GestureDetector.GestureListener {

        private static final float flingThreshold = 200.0f;

        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            Observable.just(BlueRobe.Action.PREPARE_JUMP).subscribe(actionObserver);
            return false;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            Observable.just(BlueRobe.Action.JUMP).subscribe(actionObserver);
            return false;
        }

        @Override
        public boolean longPress(float x, float y) {
            return false;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            if (velocityX > flingThreshold) {
                Observable.just(BlueRobe.Action.MOVE_RIGHT).subscribe(actionObserver);
            } else if (velocityX < -flingThreshold) {
                Observable.just(BlueRobe.Action.MOVE_LEFT).subscribe(actionObserver);
            }

            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            return false;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            Observable.just(BlueRobe.Action.CANCEL_JUMP).subscribe(actionObserver);
            return false;
        }

        @Override
        public boolean zoom(float initialDistance, float distance) {
            return false;
        }

        @Override
        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1,
                Vector2 pointer2) {
            return false;
        }
    }

    private class KeyboardInputProcessor implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            switch (keycode) {
                case Input.Keys.A:
                case Input.Keys.LEFT:
                    Observable.just(BlueRobe.Action.MOVE_LEFT).subscribe(actionObserver);
                    break;
                case Input.Keys.D:
                case Input.Keys.RIGHT:
                    Observable.just(BlueRobe.Action.MOVE_RIGHT).subscribe(actionObserver);
                    break;
            }
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }

    private Observer<BlueRobe.Action> actionObserver;

    public GameSceneInput(Observer<BlueRobe.Action> observer) {
        this.actionObserver = observer;

        addProcessor(new GestureDetector(new GestureListener()));
        addProcessor(new KeyboardInputProcessor());
    }
}
