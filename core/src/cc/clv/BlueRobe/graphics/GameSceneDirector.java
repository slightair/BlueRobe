package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;

import cc.clv.BlueRobe.engine.GameMaster;
import cc.clv.BlueRobe.input.GameSceneInput;
import rx.Observer;

/**
 * Created by slightair on 15/03/22.
 */
public class GameSceneDirector {

    private final OrthographicCamera camera;
    private final GameMaster gameMaster;
    private final GroundLayouter groundLayouter;

    @lombok.Getter
    private final GameSceneInput input = new GameSceneInput();

    private CharacterModelInstance characterInstance;
    private CameraMan cameraMan;

    public GameSceneDirector(OrthographicCamera camera) {
        this.camera = camera;
        gameMaster = new GameMaster(input.getActions());
        groundLayouter = new GroundLayouter(gameMaster.getGround());

        AssetLoader assetLoader = AssetLoader.getInstance();
        assetLoader.load().subscribe(new Observer<Float>() {
            @Override
            public void onCompleted() {
                doneLoading();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Float aFloat) {

            }
        });

        groundLayouter.layoutGround();
    }

    private void doneLoading() {
        characterInstance = CharacterModelInstance.create(gameMaster.getCharacter());
        cameraMan = new CameraMan(camera, characterInstance);
    }

    public void update(float deltaTime) {
        AssetLoader assetLoader = AssetLoader.getInstance();
        if (!assetLoader.isCompleted()) {
            assetLoader.update();
        }

        if (characterInstance != null) {
            gameMaster.update(deltaTime);
            groundLayouter.update(deltaTime);

            characterInstance.updateAnimation(deltaTime);

            cameraMan.update(deltaTime);
        }
    }

    public ArrayList<ModelInstance> modelInstances() {
        ArrayList<ModelInstance> list = new ArrayList<ModelInstance>();

        if (characterInstance != null) {
            list.add(characterInstance);
        }
        list.addAll(groundLayouter.getModelInstances());

        return list;
    }
}
