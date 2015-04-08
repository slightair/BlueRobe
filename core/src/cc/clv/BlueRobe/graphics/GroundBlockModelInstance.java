package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

import java.util.HashMap;

import cc.clv.BlueRobe.engine.GroundBlock;

/**
 * Created by slightair on 15/03/14.
 */
public class GroundBlockModelInstance extends ModelInstance implements AnimatableModelInstance {

    @lombok.Getter
    private final GroundBlock groundBlock;

    private final AnimationController animationController;

    private final static HashMap<GroundBlock.Type, Constructor> constructorHashMap
            = new HashMap<GroundBlock.Type, Constructor>();

    public static class Constructor {

        private final Model model;

        public Constructor(GroundBlock.Type type) {
            model = new GroundBlockModel(type);
        }

        public GroundBlockModelInstance construct(GroundBlock groundBlock) {
            return new GroundBlockModelInstance(model, groundBlock);
        }
    }

    public static GroundBlockModelInstance create(GroundBlock groundBlock) {
        GroundBlock.Type type = groundBlock.getType();
        Constructor constructor = constructorHashMap.get(type);
        if (constructor == null) {
            Constructor instanceConstructor = new Constructor(type);
            constructorHashMap.put(type, instanceConstructor);
            constructor = instanceConstructor;
        }
        return constructor.construct(groundBlock);
    }

    public GroundBlockModelInstance(Model model, GroundBlock groundBlock) {
        super(model);

        this.groundBlock = groundBlock;

        animationController = new AnimationController(this);
    }

    @Override
    public void updateAnimation(float deltaTime) {
        animationController.update(deltaTime);
    }
}