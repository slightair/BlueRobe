package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;

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
    private final PhysicsMaster physicsMaster;
    private final PhantomGround phantomGround;
    private final GroundLayouter groundLayouter;
    private final DebugDrawer debugDrawer;

    @lombok.Getter
    private final GameSceneInput input = new GameSceneInput();

    private CharacterModelInstance characterInstance;
    private CameraMan cameraMan;

    public GameSceneDirector(OrthographicCamera camera) {
        this.camera = camera;
        gameMaster = new GameMaster(input.getActions());
        physicsMaster = new PhysicsMaster();
        phantomGround = new PhantomGround();
        groundLayouter = new GroundLayouter(gameMaster.getGround(), physicsMaster);

        physicsMaster.addPhantomGround(phantomGround);

        debugDrawer = new DebugDrawer();
        debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        physicsMaster.getDynamicsWorld().setDebugDrawer(debugDrawer);

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

        physicsMaster.addCharacter(characterInstance);
    }

    public void update(float deltaTime) {
        AssetLoader assetLoader = AssetLoader.getInstance();
        if (!assetLoader.isCompleted()) {
            assetLoader.update();
        }

        physicsMaster.stepSimulation(deltaTime);

        if (characterInstance != null) {
            gameMaster.update(deltaTime);
            groundLayouter.update(deltaTime);

            characterInstance.updateAnimation(deltaTime);

            cameraMan.update(deltaTime);
        }

//        debugDrawer.begin(camera);
//        physicsMaster.getDynamicsWorld().debugDrawWorld();
//        debugDrawer.end();
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