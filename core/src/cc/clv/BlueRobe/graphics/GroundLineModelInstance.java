package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

import java.util.HashMap;

import cc.clv.BlueRobe.engine.GroundLine;

/**
 * Created by slightair on 15/03/14.
 */
public class GroundLineModelInstance extends ModelInstance implements AnimatableModelInstance {

    @lombok.Getter
    private final GroundLine groundLine;

    private final AnimationController animationController;

    private final static HashMap<GroundLine.Type, Constructor> constructorHashMap
            = new HashMap<GroundLine.Type, Constructor>();

    public static class Constructor {

        private final Model model;

        public Constructor(GroundLine.Type type) {
            model = new GroundLineModel(type);
        }

        public GroundLineModelInstance construct(GroundLine groundLine) {
            return new GroundLineModelInstance(model, groundLine);
        }
    }

    public static GroundLineModelInstance create(GroundLine groundLine) {
        GroundLine.Type type = groundLine.getType();
        Constructor constructor = constructorHashMap.get(type);
        if (constructor == null) {
            Constructor instanceConstructor = new Constructor(type);
            constructorHashMap.put(type, instanceConstructor);
            constructor = instanceConstructor;
        }
        return constructor.construct(groundLine);
    }

    public GroundLineModelInstance(Model model, GroundLine groundLine) {
        super(model);

        this.groundLine = groundLine;

        animationController = new AnimationController(this);
    }

    @Override
    public void updateAnimation(float deltaTime) {
        animationController.update(deltaTime);
    }
}