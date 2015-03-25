package cc.clv.BlueRobe;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.math.Vector3;

import cc.clv.BlueRobe.graphics.GameSceneDirector;

public class BlueRobe extends ApplicationAdapter {

    private Environment environment;
    private DirectionalShadowLight shadowLight;
    private OrthographicCamera camera;
    private ModelBatch modelBatch;
    private ModelBatch shadowBatch;
    private GameSceneDirector sceneDirector;

    private void setUpEnvironment() {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 1f));
        DirectionalLight directionalLight = new DirectionalLight()
                .set(0.8f, 0.8f, 0.8f, 24f, -24f, 0f);
        environment.add(directionalLight);
        shadowLight = new DirectionalShadowLight(1024, 1024, 320f, 100f, 1f, 300f);
        shadowLight.set(directionalLight);
        environment.shadowMap = shadowLight;

        modelBatch = new ModelBatch();
        shadowBatch = new ModelBatch(new DepthShaderProvider());
    }

    private void setUpCamera() {
        float viewportSize = 128.0f;
        float aspectRatio = Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
        camera = new OrthographicCamera(viewportSize, viewportSize * 2 * aspectRatio);
        camera.position.set(camera.viewportWidth * 0.15f, camera.viewportHeight * 0.5f,
                camera.viewportHeight * 0.3f);
        camera.lookAt(0, 0, 0);
        camera.near = -100f;
        camera.far = 300f;
    }

    private void renderModelShadows() {
        shadowLight.begin(Vector3.Zero, camera.direction);
        shadowBatch.begin(shadowLight.getCamera());
        shadowBatch.render(sceneDirector.modelInstances());
        shadowBatch.end();
        shadowLight.end();
    }

    private void renderModels() {
        modelBatch.begin(camera);
        modelBatch.render(sceneDirector.modelInstances(), environment);
        modelBatch.end();
    }

    @Override
    public void create() {
        setUpEnvironment();
        setUpCamera();

        sceneDirector = new GameSceneDirector(camera);
        Gdx.input.setInputProcessor(sceneDirector.getInput());
    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.12f, 0.56f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        sceneDirector.update(Gdx.graphics.getDeltaTime());

        renderModelShadows();
        renderModels();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        shadowBatch.dispose();
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
