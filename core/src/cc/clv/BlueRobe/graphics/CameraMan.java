package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import cc.clv.BlueRobe.engine.Character;
import rx.functions.Action1;

public class CameraMan {

    private static final float cameraMoveDuration = 0.3f;

    private final OrthographicCamera camera;
    private final float cameraDefaultX;
    private final CharacterModelInstance characterModelInstance;
    private final TweenManager tweenManager = new TweenManager();

    public CameraMan(final OrthographicCamera camera,
            final CharacterModelInstance characterModelInstance) {
        this.camera = camera;
        this.cameraDefaultX = camera.position.x;
        this.characterModelInstance = characterModelInstance;

        characterModelInstance.getCharacter().getActions()
                .subscribe(new Action1<Character.Action>() {
                    @Override
                    public void call(Character.Action action) {
                        Vector3 characterPosition = characterModelInstance.transform
                                .getTranslation(new Vector3());

                        Tween.to(camera, CameraTween.POSITION_X, cameraMoveDuration)
                                .target(characterPosition.x + cameraDefaultX)
                                .ease(TweenEquations.easeOutExpo)
                                .start(tweenManager);
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
