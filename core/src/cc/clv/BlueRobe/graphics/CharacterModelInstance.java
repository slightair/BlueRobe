package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

import cc.clv.BlueRobe.engine.Character;
import rx.functions.Action1;

/**
 * Created by slightair on 15/02/27.
 */
public class CharacterModelInstance extends ModelInstance implements AnimatableModelInstance {

    @lombok.Getter
    private final Character character;

    @lombok.Getter
    private btRigidBody body;

    private final AnimationController animationController;

    private final static Constructor constructor = new Constructor();

    public static class Constructor {

        private final Model model;
        private final btCollisionShape shape;
        private final btRigidBody.btRigidBodyConstructionInfo constructionInfo;
        private final Vector3 localInertia = new Vector3();

        public Constructor() {
            float mass = 1.0f;
            model = AssetLoader.getInstance().getCharacterModel();
            shape = Bullet.obtainStaticNodeShape(model.nodes);
            if (mass > 0f) {
                shape.calculateLocalInertia(mass, localInertia);
            } else {
                localInertia.set(0, 0, 0);
            }
            constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, shape,
                    localInertia);
        }

        public CharacterModelInstance construct(Character character) {
            return new CharacterModelInstance(model, character, constructionInfo);
        }
    }

    public static CharacterModelInstance create(Character character) {
        return constructor.construct(character);
    }

    public CharacterModelInstance(Model model, Character character,
            btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
        super(model);

        this.character = character;
        transform.translate(0f, 32f, 80f);
        transform.rotate(new Vector3(0f, 1f, 0f), 180);

        character.getActions().subscribe(new Action1<Character.Action>() {
            @Override
            public void call(Character.Action action) {
                body.activate();
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
                        body.applyImpulse(new Vector3(0, 300, 0), new Vector3());

                        break;
                    case CANCEL_JUMP:
                        animationController.animate("returnShrink", 0.0f);
                        break;
                }
                updateWorldTransform();
            }
        });

        animationController = new AnimationController(this);

        body = new btRigidBody(constructionInfo);
        body.setWorldTransform(transform);
        body.setCollisionFlags(body.getCollisionFlags()
                | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        body.setLinearFactor(new Vector3(0f, 1f, 0f));
        body.setAngularFactor(new Vector3());
    }

    public void updateWorldTransform() {
        body.setWorldTransform(transform);
    }

    @Override
    public void updateAnimation(float deltaTime) {
        animationController.update(deltaTime);
        body.getWorldTransform(transform);
    }
}