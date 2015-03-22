package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;

import cc.clv.BlueRobe.engine.Character;
import cc.clv.BlueRobe.graphics.animations.JumpAnimation;
import cc.clv.BlueRobe.graphics.animations.ReturnShrinkAnimation;
import cc.clv.BlueRobe.graphics.animations.ShrinkAnimation;
import rx.functions.Action1;

/**
 * Created by slightair on 15/02/27.
 */
public class CharacterModelInstance extends ModelInstance {

    private final Character character;

    @lombok.Getter
    private final AnimationController animationController;

    public CharacterModelInstance(Model model, Character character) {
        super(model);

        this.character = character;
        transform.translate(0f, 0f, 80f);
        transform.rotate(new Vector3(0f, 1f, 0f), 180);

        character.getActions().subscribe(new Action1<Character.Action>() {
            @Override
            public void call(Character.Action action) {
                switch (action) {

                    case MOVE_LEFT:
                        transform.translate(GroundBlockModel.SIZE, 0.0f, 0.0f);
                        break;
                    case MOVE_RIGHT:
                        transform.translate(-GroundBlockModel.SIZE, 0.0f, 0.0f);
                        break;
                    case PREPARE_JUMP:
                        animationController.animate("shrink", 0.0f);
                        break;
                    case JUMP:
                        animationController.animate("returnShrink", 0.0f);
                        animationController.animate("jump",
                                ReturnShrinkAnimation.defaultDuration);
                        break;
                    case CANCEL_JUMP:
                        animationController.animate("returnShrink", 0.0f);
                        break;
                }
            }
        });

        animationController = new AnimationController(this);
    }

    public static CharacterModelInstance create(Character character,
            AssetManager assetManager) {

        Model model = assetManager.get("models/hikari.g3db", Model.class);
        Node rootNode = model.getNode("hikari_root");
        model.animations.add(new JumpAnimation(rootNode, 10.0f));
        model.animations.add(new ShrinkAnimation(rootNode));
        model.animations.add(new ReturnShrinkAnimation(rootNode));

        return new CharacterModelInstance(model, character);
    }
}
