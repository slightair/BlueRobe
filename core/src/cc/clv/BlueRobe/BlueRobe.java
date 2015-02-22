package cc.clv.BlueRobe;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import cc.clv.BlueRobe.engine.GroundBlock;
import cc.clv.BlueRobe.graphics.GroundBlockModel;
import cc.clv.BlueRobe.graphics.animations.JumpAnimation;
import cc.clv.BlueRobe.graphics.animations.ReturnShrinkAnimation;
import cc.clv.BlueRobe.graphics.animations.ShrinkAnimation;
import cc.clv.BlueRobe.graphics.animations.StretchAnimation;
import cc.clv.BlueRobe.input.GameSceneInput;
import rx.Observer;

public class BlueRobe extends ApplicationAdapter {

    public enum Action {
        MOVE_LEFT,
        MOVE_RIGHT,
        PREPARE_JUMP,
        JUMP,
        CANCEL_JUMP,
    }

    public Environment environment;
    public DirectionalShadowLight shadowLight;
    public OrthographicCamera camera;
    public ModelBatch modelBatch;
    public ModelBatch shadowBatch;
    public AssetManager assetManager;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public Array<AnimationController> animationControllers = new Array<AnimationController>();
    private AnimationController characterAnimationController;
    private ModelInstance characterInstance;
    public boolean loading;
    public int numTileHorizontal = 9;
    public int numTileVertical = 24;
    private TweenManager cameraMoveManager = new TweenManager();
    private static final float cameraMoveDuration = 0.5f;

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

    private void performMoveLeftAction() {
        characterInstance.transform.translate(GroundBlockModel.SIZE, 0.0f, 0.0f);

        Tween.to(camera, CameraTween.POSITION_X, cameraMoveDuration)
                .targetRelative(-GroundBlockModel.SIZE)
                .ease(TweenEquations.easeOutExpo)
                .start(cameraMoveManager);
    }

    private void performMoveRightAction() {
        characterInstance.transform.translate(-GroundBlockModel.SIZE, 0.0f, 0.0f);

        Tween.to(camera, CameraTween.POSITION_X, cameraMoveDuration)
                .targetRelative(GroundBlockModel.SIZE)
                .ease(TweenEquations.easeOutExpo)
                .start(cameraMoveManager);
    }

    private void performPrepareJumpAction() {
        if (characterAnimationController != null) {
            characterAnimationController.animate("shrink", 0.0f);
        }
    }

    private void performJumpAction() {
        if (characterAnimationController != null) {
            characterAnimationController.animate("returnShrink", 0.0f);
            characterAnimationController.animate("jump",
                    ReturnShrinkAnimation.defaultDuration);
        }
    }

    private void performCancelJumpAction() {
        if (characterAnimationController != null) {
            characterAnimationController.animate("returnShrink", 0.0f);
        }
    }

    private void doneLoading() {
        Model character = assetManager.get("models/hikari.g3db", Model.class);
        Node node = character.getNode("hikari_root");
        character.animations.add(new JumpAnimation(node, 10.0f));
        character.animations.add(new ShrinkAnimation(node));
        character.animations.add(new ReturnShrinkAnimation(node));

        characterInstance = new ModelInstance(character);
        characterInstance.transform.translate(0f, 0f, 80f);
        characterInstance.transform.rotate(new Vector3(0f, 1f, 0f), 180);
        instances.add(characterInstance);

        characterAnimationController = new AnimationController(characterInstance);
        characterAnimationController.allowSameAnimation = true;
        animationControllers.add(characterAnimationController);

        Model item = assetManager.get("models/items.g3db", Model.class);
        item.animations.add(new StretchAnimation(item.getNode("mushroom")));

        int numTileVerticalHalf = numTileVertical / 2;
        int numTileHorizontalHalf = numTileHorizontal / 2;
        for (int z = -numTileVerticalHalf; z <= numTileVerticalHalf; z++) {
            for (int x = -numTileHorizontalHalf; x <= numTileHorizontalHalf; x++) {
                if (Math.random() > 0.1) {
                    continue;
                }

                ModelInstance itemInstance = new ModelInstance(item, "mushroom");
                itemInstance.transform
                        .translate(x * GroundBlockModel.SIZE, 0, z * GroundBlockModel.SIZE);
                instances.add(itemInstance);

                AnimationController animationController = new AnimationController(itemInstance);
                animationController.setAnimation("stretch", -1);
                animationControllers.add(animationController);
            }
        }

        loading = false;
    }

    @Override
    public void create() {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 1f));
        DirectionalLight directionalLight = new DirectionalLight()
                .set(0.8f, 0.8f, 0.8f, 24f, -24f, 0f);
        environment.add(directionalLight);
        shadowLight = new DirectionalShadowLight(1024, 1024, 320f, 100f, 1f, 300f);
        shadowLight.set(directionalLight);
        environment.shadowMap = shadowLight;

        float viewportSize = 128.0f;
        float aspectRatio = Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
        camera = new OrthographicCamera(viewportSize, viewportSize * 2 * aspectRatio);
        camera.position.set(camera.viewportWidth * 0.15f, camera.viewportHeight * 0.5f,
                camera.viewportHeight * 0.3f);
        camera.lookAt(0, 0, 0);
        camera.near = -100f;
        camera.far = 300f;
        camera.update();

        assetManager = new AssetManager();
        assetManager.load("models/hikari.g3db", Model.class);
        assetManager.load("models/items.g3db", Model.class);
        loading = true;

        Model whiteGround = new GroundBlockModel(new GroundBlock(GroundBlock.Type.DebugWhite));
        Model grayGround = new GroundBlockModel(new GroundBlock(GroundBlock.Type.DebugGray));

        int numTileVerticalHalf = numTileVertical / 2;
        int numTileHorizontalHalf = numTileHorizontal / 2;
        for (int z = -numTileVerticalHalf; z <= numTileVerticalHalf; z++) {
            for (int x = -numTileHorizontalHalf; x <= numTileHorizontalHalf; x++) {
                Model model = (x + z % 2) % 2 == 0 ? whiteGround : grayGround;
                ModelInstance instance = new ModelInstance(model);
                instance.transform
                        .translate(x * GroundBlockModel.SIZE, -32 / 2, z * GroundBlockModel.SIZE);
                instances.add(instance);
            }
        }

        modelBatch = new ModelBatch();
        shadowBatch = new ModelBatch(new DepthShaderProvider());

        Observer<Action> sceneControllerObserver = new Observer<Action>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Action action) {
                switch (action) {
                    case MOVE_LEFT:
                        performMoveLeftAction();
                        break;
                    case MOVE_RIGHT:
                        performMoveRightAction();
                        break;
                    case PREPARE_JUMP:
                        performPrepareJumpAction();
                        break;
                    case JUMP:
                        performJumpAction();
                        break;
                    case CANCEL_JUMP:
                        performCancelJumpAction();
                        break;
                }
            }
        };

        Tween.registerAccessor(OrthographicCamera.class, new CameraTween());

        Gdx.input.setInputProcessor(new GameSceneInput(sceneControllerObserver));
    }

    @Override
    public void render() {
        if (loading && assetManager.update()) {
            doneLoading();
        }

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.12f, 0.56f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        float deltaTime = Gdx.graphics.getDeltaTime();
        for (AnimationController animationController : animationControllers) {
            animationController.update(deltaTime);
        }

        cameraMoveManager.update(deltaTime);
        camera.update();

        shadowLight.begin(Vector3.Zero, camera.direction);
        shadowBatch.begin(shadowLight.getCamera());
        shadowBatch.render(instances);
        shadowBatch.end();
        shadowLight.end();

        modelBatch.begin(camera);
        modelBatch.render(instances, environment);
        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        instances.clear();
        animationControllers.clear();
        assetManager.dispose();
    }

    @Override
    public void resume() {
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }
}
