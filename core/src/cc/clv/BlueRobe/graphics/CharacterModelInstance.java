package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;

import cc.clv.BlueRobe.engine.Character;
import rx.functions.Action1;

/**
 * Created by slightair on 15/02/27.
 */
public class CharacterModelInstance extends ModelInstance implements AnimatableModelInstance {

    @lombok.Getter
    private final Character character;

    private final AnimationController animationController;

    public CharacterModelInstance(Character character, Model model) {
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
                        break;
                    case CANCEL_JUMP:
                        animationController.animate("returnShrink", 0.0f);
                        break;
                }
            }
        });

        animationController = new AnimationController(this);
    }

    public static CharacterModelInstance create(Character character) {
        Model model = AssetLoader.getInstance().getCharacterModel();
        return new CharacterModelInstance(character, model);
    }

    @Override
    public void updateAnimation(float deltaTime) {
        animationController.update(deltaTime);
    }
}
