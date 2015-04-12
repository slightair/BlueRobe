package cc.clv.BlueRobe.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class StageLoader implements Loader {

    private final AssetManager assetManager;

    public StageLoader(AssetManager assetManager) {
        this.assetManager = assetManager;

        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
    }

    private static final String[] STAGE_NAMES = {
            "debug"
    };

    @Override
    public void load() {
        for (String stageName : STAGE_NAMES) {
            assetManager.load(stageFileName(stageName), TiledMap.class);
        }
    }

    @Override
    public void complete() {

    }

    private String stageFileName(String stageName) {
        return "stages/" + stageName + ".tmx";
    }

    public TiledMap get(String stageName) {
        return assetManager.get(stageFileName(stageName));
    }
}
