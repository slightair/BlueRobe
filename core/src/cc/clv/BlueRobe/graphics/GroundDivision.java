package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by slightair on 15/03/26.
 */

@lombok.Value
public class GroundDivision {

    GroundBlockModelInstance blockModelInstance;
    ItemModelInstance itemModelInstance;

    public ArrayList<ModelInstance> getModelInstances() {
        ArrayList<ModelInstance> instances = new ArrayList<ModelInstance>();

        if (blockModelInstance != null) {
            instances.add(blockModelInstance);
        }

        if (itemModelInstance != null) {
            instances.add(itemModelInstance);
        }

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
