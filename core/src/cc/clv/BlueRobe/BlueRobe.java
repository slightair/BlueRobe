package cc.clv.BlueRobe;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class BlueRobe extends ApplicationAdapter {
	public Environment environment;
	public DirectionalShadowLight shadowLight;
	public OrthographicCamera camera;
	public ModelBatch modelBatch;
	public ModelBatch shadowBatch;
	public AssetManager assetManager;
	public Array<ModelInstance> instances = new Array<ModelInstance>();
	public Array<AnimationController> animationControllers = new Array<AnimationController>();
	public boolean loading;
	public int numTileHorizontal = 12;
	public int numTileVertical = 40;

	@Override
	public void create () {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 1f));
		DirectionalLight directionalLight = new DirectionalLight().set(0.8f, 0.8f, 0.8f, 24f, -24f, 0f);
		environment.add(directionalLight);
		shadowLight = new DirectionalShadowLight(1024, 1024, 320f, 100f, 1f, 300f);
		shadowLight.set(directionalLight);
		environment.shadowMap = shadowLight;

		float viewportSize = 96.0f;
		float aspectRatio = Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
		camera = new OrthographicCamera(viewportSize, viewportSize * 2 * aspectRatio);
		camera.position.set(camera.viewportWidth * 0.15f, viewportSize * 0.5f, camera.viewportHeight * 0.3f);
		camera.lookAt(0, 0, 0);
		camera.near = -100f;
		camera.far = 300f;
		camera.update();

		assetManager = new AssetManager();
		assetManager.load("models/hikari.g3db", Model.class);
		assetManager.load("models/mushroom.g3db", Model.class);
		loading = true;

		float tileSize = 10.0f;
		float tileHeight = 30.0f;
		ModelBuilder modelBuilder = new ModelBuilder();
		Model whiteGround = modelBuilder.createBox(tileSize, tileHeight, tileSize,
				new Material(ColorAttribute.createDiffuse(Color.WHITE)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		Model grayGround = modelBuilder.createBox(tileSize, tileHeight, tileSize,
				new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

		for (int z = -numTileVertical / 2; z < numTileVertical / 2; z++) {
			for (int x = -numTileHorizontal / 2; x < numTileHorizontal / 2; x++){
				Model model = (x + z % 2) % 2 == 0 ? whiteGround : grayGround;
				ModelInstance instance = new ModelInstance(model);
				instance.transform.setToTranslation(x * tileSize + tileSize / 2, -tileHeight / 2, z * tileSize + tileSize / 2);
				instances.add(instance);
			}
		}

		modelBatch = new ModelBatch();
		shadowBatch = new ModelBatch(new DepthShaderProvider());
	}

	private void doneLoading() {
		Model character = assetManager.get("models/hikari.g3db", Model.class);
		ModelInstance characterInstance = new ModelInstance(character);
		characterInstance.transform.translate(0f, 0f, 80f);
		characterInstance.transform.rotate(new Vector3(0f, 1f, 0f), 180);
		instances.add(characterInstance);

		float tileSize = 10.0f;
		for (int z = -numTileVertical / 2; z < numTileVertical / 2; z++) {
			for (int x = -numTileHorizontal / 2; x < numTileHorizontal / 2; x++){
				if (Math.random() > 0.1) {
					continue;
				}

				Model item = assetManager.get("models/mushroom.g3db", Model.class);
				ModelInstance itemInstance = new ModelInstance(item);
				itemInstance.transform.translate(x * tileSize + tileSize / 2, 0, z * tileSize + tileSize / 2);
				itemInstance.transform.scale(0.4f, 0.4f, 0.4f);
				instances.add(itemInstance);

				AnimationController animationController = new AnimationController(itemInstance);
				animationController.setAnimation("mushroom|mushroomAction", -1);
				animationControllers.add(animationController);
			}
		}

		loading = false;
	}

	@Override
	public void render () {
		if (loading && assetManager.update()) {
			doneLoading();
		}

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		float deltaTime = Gdx.graphics.getDeltaTime();
		for (AnimationController animationController: animationControllers) {
			animationController.update(deltaTime);
		}

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
