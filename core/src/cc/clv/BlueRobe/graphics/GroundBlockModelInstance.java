package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

import java.util.HashMap;

import cc.clv.BlueRobe.engine.GroundBlock;

/**
 * Created by slightair on 15/03/14.
 */
public class GroundBlockModelInstance extends ModelInstance {

    @lombok.Getter
    private final GroundBlock groundBlock;

    @lombok.Getter
    private final AnimationController animationController;

    private final static HashMap<GroundBlock.Type, Model> modelHashMap
            = new HashMap<GroundBlock.Type, Model>();

    public GroundBlockModelInstance(Model model, GroundBlock groundBlock) {
        super(model);

        this.groundBlock = groundBlock;

        animationController = new AnimationController(this);
        animationController.allowSameAnimation = true;
    }

    public static GroundBlockModelInstance create(GroundBlock groundBlock) {
        GroundBlock.Type type = groundBlock.getType();
        Model blockModel = modelHashMap.get(type);
        if (blockModel == null) {
            GroundBlockModel model = new GroundBlockModel(type);
            modelHashMap.put(type, model);
            blockModel = model;
        }

        return new GroundBlockModelInstance(blockModel, groundBlock);
    }
}
