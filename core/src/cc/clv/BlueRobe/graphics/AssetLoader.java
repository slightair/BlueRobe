package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.Node;

import cc.clv.BlueRobe.graphics.animations.JumpAnimation;
import cc.clv.BlueRobe.graphics.animations.ReturnShrinkAnimation;
import cc.clv.BlueRobe.graphics.animations.ShrinkAnimation;
import cc.clv.BlueRobe.graphics.animations.StretchAnimation;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by slightair on 15/03/23.
 */

public class AssetLoader {

    private static final AssetLoader instance = new AssetLoader();

    private AssetLoader() {
    }

    public static AssetLoader getInstance() {
        return AssetLoader.instance;
    }

    private final AssetManager assetManager = new AssetManager();

    private PublishSubject<Float> progressSubject;
    private boolean completed = false;

    public Observable<Float> load() {
        assetManager.load("models/hikari.g3db", Model.class);
        assetManager.load("models/items.g3db", Model.class);

        progressSubject = PublishSubject.create();

        return progressSubject;
    }

    private void setUpCharacterModel() {
        Model model = getCharacterModel();

        Node node = model.getNode("hikari_root");
        model.animations.add(new JumpAnimation(node, 10.0f));
        model.animations.add(new ShrinkAnimation(node));
        model.animations.add(new ReturnShrinkAnimation(node));
    }

    private void setUpItemModel() {
        Model model = getItemModel();

        Node node = model.getNode("mushroom");
        model.animations.add(new StretchAnimation(node));
    }

    public void update() {
        completed = assetManager.update();
        progressSubject.onNext(assetManager.getProgress());

        if (completed) {
            setUpCharacterModel();
            setUpItemModel();

            progressSubject.onCompleted();
        }
    }

    public boolean isCompleted() {
        return completed;
    }

    public Model getCharacterModel() {
        return assetManager.get("models/hikari.g3db", Model.class);
    }

    public Model getItemModel() {
        return assetManager.get("models/items.g3db", Model.class);
    }
}
