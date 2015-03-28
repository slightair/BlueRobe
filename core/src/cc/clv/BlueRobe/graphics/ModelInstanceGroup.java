package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.ArrayList;

/**
 * Created by slightair on 15/03/26.
 */
public class ModelInstanceGroup {

    @lombok.Getter
    private final ArrayList<ModelInstance> instances = new ArrayList<ModelInstance>();

    public void add(ModelInstance instance) {
        instances.add(instance);
    }
}
