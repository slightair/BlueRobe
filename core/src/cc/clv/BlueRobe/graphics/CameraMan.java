package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import cc.clv.BlueRobe.engine.Character;
import rx.functions.Action1;

/**
 * Created by slightair on 15/03/10.
 */
public class CameraMan {

    private static final float cameraMoveDuration = 0.5f;

    private final OrthographicCamera camera;
    private final Character character;
    private final TweenManager tweenManager = new TweenManager();

    public CameraMan(final OrthographicCamera camera, Character character) {
        this.camera = camera;
        this.character = character;

        character.getActions().subscribe(new Action1<Character.Action>() {
            @Override
            public void call(Character.Action action) {
                switch (action) {

                    case MOVE_LEFT:
                        Tween.to(camera, CameraTween.POSITION_X, cameraMoveDuration)
                                .targetRelative(-GroundBlockModel.SIZE)
                                .ease(TweenEquations.easeOutExpo)
                                .start(tweenManager);
                        break;
                    case MOVE_RIGHT:
                        Tween.to(camera, CameraTween.POSITION_X, cameraMoveDuration)
                                .targetRelative(GroundBlockModel.SIZE)
                                .ease(TweenEquations.easeOutExpo)
                                .start(tweenManager);
                        break;
                }
            }
        });

        Tween.registerAccessor(Camera.class, new CameraTween());
    }

    public void update(float deltaTime) {
        tweenManager.update(deltaTime);
        camera.update();
    }

    private class CameraTween implements TweenAccessor<OrthographicCamera> {

        public static final int POSITION_X = 1;

        @Override
        public int getValues(OrthographicCamera orthographicCamera, int tweenType,
                float[] returnValues) {
            switch (tweenType) {
                case POSITION_X:
                    returnValues[0] = orthographicCamera.position.x;
                    return 1;
                default:
                    assert false;
                    return -1;
            }
        }

        @Override
        public void setValues(OrthographicCamera orthographicCamera, int tweenType,
                float[] newValues) {
            switch (tweenType) {
                case POSITION_X:
                    orthographicCamera.position.x = newValues[0];
                    break;
                default:
                    assert false;
                    break;
            }
        }
    }
}
