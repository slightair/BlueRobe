package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

import cc.clv.BlueRobe.assets.AssetMaster;
import cc.clv.BlueRobe.engine.Object;

public class ObjectModelInstance extends ModelInstance implements AnimatableModelInstance {

    @lombok.Getter
    private final cc.clv.BlueRobe.engine.Object object;

    private final AnimationController animationController;

    public ObjectModelInstance(Object object, Model model) {
        super(model, object.getName());

        this.object = object;

        animationController = new AnimationController(this);
        setUpAnimation();
    }

    private void setUpAnimation() {
        animationController.setAnimation("stretch", -1);
    }

    public static ObjectModelInstance create(Object object) {
        Model model = AssetMaster.getModelLoader().getObjectModel();
        return new ObjectModelInstance(object, model);
    }

    @Override
    public void updateAnimation(float deltaTime) {
        animationController.update(deltaTime);
    }
}
