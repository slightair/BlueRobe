package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;

import cc.clv.BlueRobe.engine.Item;

/**
 * Created by slightair on 15/03/25.
 */
public class ItemModelInstance extends ModelInstance {

    private final Item item;

    @lombok.Getter
    private final AnimationController animationController;

    public ItemModelInstance(Item item, Model model) {
        super(model, item.getName());

        this.item = item;

        animationController = new AnimationController(this);
        setUpAnimation();
    }

    private void setUpAnimation() {
        animationController.setAnimation("stretch", -1);
    }

    public static ItemModelInstance create(Item item) {
        Model model = AssetLoader.getInstance().getItemModel();
        return new ItemModelInstance(item, model);
    }
}
