package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;
import java.util.List;

import cc.clv.BlueRobe.engine.GroundBlock;
import cc.clv.BlueRobe.engine.Item;
import rx.Observable;
import rx.functions.Func1;

public class GroundDivision {

    @lombok.Getter
    private GroundBlockModelInstance blockModelInstance;

    @lombok.Getter
    private ArrayList<ItemModelInstance> itemModelInstances;

    public GroundDivision(GroundBlock groundBlock) {
        blockModelInstance = GroundBlockModelInstance.create(groundBlock);
        itemModelInstances = new ArrayList<ItemModelInstance>();

        for (Item item : groundBlock.getItems()) {
            itemModelInstances.add(ItemModelInstance.create(item));
        }
    }

    public ArrayList<ModelInstance> getModelInstances() {
        ArrayList<ModelInstance> instances = new ArrayList<ModelInstance>();

        instances.add(blockModelInstance);
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
