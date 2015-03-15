package cc.clv.BlueRobe;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import cc.clv.BlueRobe.engine.GameMaster;
import cc.clv.BlueRobe.graphics.CameraMan;
import cc.clv.BlueRobe.graphics.CharacterModelInstance;
import cc.clv.BlueRobe.graphics.Groundlayouter;
import cc.clv.BlueRobe.input.GameSceneInput;

public class BlueRobe extends ApplicationAdapter {

    public Environment environment;
    public DirectionalShadowLight shadowLight;
    public OrthographicCamera camera;
    public ModelBatch modelBatch;
    public ModelBatch shadowBatch;
    public AssetManager assetManager;
    public Array<AnimationController> animationControllers = new Array<AnimationController>();
    public boolean loading;

    private CharacterModelInstance characterInstance;
    private Groundlayouter groundlayouter;
    private CameraMan cameraMan;
    private GameMaster gameMaster;

    private void doneLoading() {
        characterInstance = CharacterModelInstance.create(gameMaster.getCharacter(),
                assetManager);
        animationControllers.add(characterInstance.getAnimationController());

//        Model item = assetManager.get("models/items.g3db", Model.class);
//        item.animations.add(new StretchAnimation(item.getNode("mushroom")));
//
//        public int numTileHorizontal = 9;
//        public int numTileVertical = 24;
//        int numTileVerticalHalf = numTileVertical / 2;
//        int numTileHorizontalHalf = numTileHorizontal / 2;
//        for (int z = -numTileVerticalHalf; z <= numTileVerticalHalf; z++) {
//            for (int x = -numTileHorizontalHalf; x <= numTileHorizontalHalf; x++) {
//                if (Math.random() > 0.1) {
//                    continue;
//                }
//
//                ModelInstance itemInstance = new ModelInstance(item, "mushroom");
//                itemInstance.transform
//                        .translate(x * GroundBlockModel.SIZE, 0, z * GroundBlockModel.SIZE);
//                instances.add(itemInstance);
//
//                AnimationController animationController = new AnimationController(itemInstance);
//                animationController.setAnimation("stretch", -1);
//                animationControllers.add(animationController);
//            }
//        }

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

        modelBatch = new ModelBatch();
        shadowBatch = new ModelBatch(new DepthShaderProvider());

        GameSceneInput input = new GameSceneInput();
        Gdx.input.setInputProcessor(input);

        gameMaster = new GameMaster(input.getActions());
        cameraMan = new CameraMan(camera, gameMaster.getCharacter());
        groundlayouter = new Groundlayouter(gameMaster.getGround());

        groundlayouter.layoutGround();
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

        gameMaster.update(deltaTime);
        groundlayouter.update(deltaTime);

        for (AnimationController animationController : animationControllers) {
            animationController.update(deltaTime);
        }

        cameraMan.update(deltaTime);
        camera.update();

        shadowLight.begin(Vector3.Zero, camera.direction);
        shadowBatch.begin(shadowLight.getCamera());
        if (characterInstance != null) {
            shadowBatch.render(characterInstance);
        }
        shadowBatch.render(groundlayouter.getBlockInstances());
        shadowBatch.end();
        shadowLight.end();

        modelBatch.begin(camera);
        if (characterInstance != null) {
            modelBatch.render(characterInstance, environment);
        }
        modelBatch.render(groundlayouter.getBlockInstances(), environment);
        modelBatch.end();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
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
