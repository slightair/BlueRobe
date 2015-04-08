package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;
import java.util.List;

import cc.clv.BlueRobe.engine.GroundLine;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by slightair on 15/03/26.
 */

public class GroundDivision {

    @lombok.Getter
    GroundLineModelInstance lineModelInstance;

    @lombok.Getter
    ArrayList<ItemModelInstance> itemModelInstances;

    public GroundDivision(GroundLine groundLine) {
        lineModelInstance = GroundLineModelInstance.create(groundLine);
        itemModelInstances = new ArrayList<ItemModelInstance>();

        createItemModelInstances(groundLine);
    }

    private void createItemModelInstances(GroundLine groundLine) {

    }

    public ArrayList<ModelInstance> getModelInstances() {
        ArrayList<ModelInstance> instances = new ArrayList<ModelInstance>();

        instances.add(lineModelInstance);
        instances.addAll(itemModelInstances);

        return instances;
    }

    public List<AnimatableModelInstance> getAnimatableModelInstances() {
        return Observable.from(getModelInstances())
                .filter(new Func1<ModelInstance, Boolean>() {
                    @Override
                    public Boolean call(ModelInstance modelInstance) {
                        return AnimatableModelInstance.class
                                .isAssignableFrom(modelInstance.getClass());
                    }
                }).cast(AnimatableModelInstance.class).toList().toBlocking().single();
    }
}
