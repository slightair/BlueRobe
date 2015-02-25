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
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenManager;
import cc.clv.BlueRobe.engine.GameMaster;
import cc.clv.BlueRobe.engine.GroundBlock;
import cc.clv.BlueRobe.graphics.CharacterModelInstance;
import cc.clv.BlueRobe.graphics.GroundBlockModel;
import cc.clv.BlueRobe.graphics.animations.StretchAnimation;
import cc.clv.BlueRobe.input.GameSceneInput;

public class BlueRobe extends ApplicationAdapter {

    public enum Action {
        CHARACTER_MOVE_LEFT,
        CHARACTER_MOVE_RIGHT,
        CHARACTER_PREPARE_JUMP,
        CHARACTER_JUMP,
        CHARACTER_CANCEL_JUMP,
    }

    public Environment environment;
    public DirectionalShadowLight shadowLight;
    public OrthographicCamera camera;
    public ModelBatch modelBatch;
    public ModelBatch shadowBatch;
    public AssetManager assetManager;
    public Array<ModelInstance> instances = new Array<ModelInstance>();
    public Array<AnimationController> animationControllers = new Array<AnimationController>();
    public boolean loading;
    public int numTileHorizontal = 9;
    public int numTileVertical = 24;
    private TweenManager cameraMoveManager = new TweenManager();
    private static final float cameraMoveDuration = 0.5f;

    private GameMaster gameMaster;

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

    private void doneLoading() {
        CharacterModelInstance characterInstance = CharacterModelInstance
                .create(gameMaster.getCharacter(), assetManager);
        instances.add(characterInstance);
        animationControllers.add(characterInstance.getAnimationController());

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

        Tween.registerAccessor(OrthographicCamera.class, new CameraTween());

        GameSceneInput input = new GameSceneInput();
        Gdx.input.setInputProcessor(input);

        gameMaster = new GameMaster(input.getActions());
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
