package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.Node;

import cc.clv.BlueRobe.graphics.animations.JumpAnimation;
import cc.clv.BlueRobe.graphics.animations.ReturnShrinkAnimation;
import cc.clv.BlueRobe.graphics.animations.ShrinkAnimation;
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

    public void update() {
        completed = assetManager.update();
        progressSubject.onNext(assetManager.getProgress());

        if (completed) {
            setUpCharacterModel();

            progressSubject.onCompleted();
        }
    }

    public boolean isCompleted() {
        return completed;
    }

    public Model getCharacterModel() {
        return assetManager.get("models/hikari.g3db", Model.class);
    }
}

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
