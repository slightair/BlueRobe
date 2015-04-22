package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.g3d.Model;

import cc.clv.BlueRobe.assets.AssetMaster;
import cc.clv.BlueRobe.engine.Object;

public class ItemModelInstance extends ObjectModelInstance {

    public ItemModelInstance(Object object, Model model) {
        super(object, model);
    }

    public static ItemModelInstance create(Object object) {
        Model model = AssetMaster.getModelLoader().getObjectModel();
        return new ItemModelInstance(object, model);
    }
}
