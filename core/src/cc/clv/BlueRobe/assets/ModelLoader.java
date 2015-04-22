package cc.clv.BlueRobe.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.Node;

import cc.clv.BlueRobe.graphics.animations.ReturnShrinkAnimation;
import cc.clv.BlueRobe.graphics.animations.ShrinkAnimation;
import cc.clv.BlueRobe.graphics.animations.StretchAnimation;

public class ModelLoader implements Loader {

    private final AssetManager assetManager;

    public ModelLoader(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    private static final String[] OBJECT_NAMES = {
            "mushroom",
            "grass"
    };

    @Override
    public void load() {
        assetManager.load("models/hikari.g3db", Model.class);
        assetManager.load("models/items.g3db", Model.class);
    }

    @Override
    public void complete() {
        setUpCharacterModel();
        setUpObjectModel();
    }

    private void setUpCharacterModel() {
        Model model = getCharacterModel();

        Node node = model.getNode("hikari_root");
        model.animations.add(new ShrinkAnimation(node));
        model.animations.add(new ReturnShrinkAnimation(node));
    }

    private void setUpObjectModel() {
        Model model = getObjectModel();

        for (String objectName : OBJECT_NAMES) {
            Node node = model.getNode(objectName);
            model.animations.add(new StretchAnimation(node));
        }
    }

    public Model getCharacterModel() {
        return assetManager.get("models/hikari.g3db", Model.class);
    }

    public Model getObjectModel() {
        return assetManager.get("models/items.g3db", Model.class);
    }
}
