package cc.clv.BlueRobe.assets;

import com.badlogic.gdx.assets.AssetManager;

import java.util.ArrayList;

import rx.Observable;
import rx.subjects.PublishSubject;

public class AssetMaster {

    private static final AssetMaster instance = new AssetMaster();

    public static ModelLoader getModelLoader() {
        return instance.modelLoader;
    }

    public static StageLoader getStageLoader() {
        return instance.stageLoader;
    }

    private final AssetManager assetManager = new AssetManager();

    private final ModelLoader modelLoader = new ModelLoader(assetManager);
    private final StageLoader stageLoader = new StageLoader(assetManager);

    private final ArrayList<Loader> loaders = new ArrayList();
    private PublishSubject<Float> progressSubject;
    private boolean completed = false;

    private AssetMaster() {
        loaders.add(modelLoader);
        loaders.add(stageLoader);
    }

    public static Observable<Float> load() {
        for (Loader loader : instance.loaders) {
            loader.load();
        }

        instance.progressSubject = PublishSubject.create();

        return instance.progressSubject;
    }

    public static void update() {
        AssetManager assetManager = instance.assetManager;
        PublishSubject progressSubject = instance.progressSubject;

        instance.completed = assetManager.update();
        progressSubject.onNext(assetManager.getProgress());

        if (instance.completed) {
            for (Loader loader : instance.loaders) {
                loader.complete();
            }

            progressSubject.onCompleted();
        }
    }

    public static boolean isLoadCompleted() {
        return instance.completed;
    }
}
