package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

import cc.clv.BlueRobe.engine.GroundLine;

/**
 * Created by slightair on 15/03/29.
 */
public class PhantomGround {

    @lombok.Getter
    private btRigidBody body;

    private ModelInstance modelInstance;
    private btCollisionShape shape;
    private btRigidBody.btRigidBodyConstructionInfo constructionInfo;
    private Vector3 localInertia = new Vector3();

    public PhantomGround() {
        createModelInstance();
        createPhysicsBody();

        body.setWorldTransform(modelInstance.transform);
    }

    private void createModelInstance() {
        ModelBuilder mb = new ModelBuilder();
        mb.begin();
        mb.node().id = "phsyicsGround";
        mb.part("physicsGround", GL20.GL_TRIANGLES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
                new Material(ColorAttribute.createDiffuse(Color.RED)))
                .box(GroundBlockModel.SIZE * GroundLine.NUM_BLOCKS,
                        GroundBlockModel.HEIGHT,
                        GroundBlockModel.SIZE);
        Model model = mb.end();
        modelInstance = new ModelInstance(model);
        modelInstance.transform.translate(0f, -GroundBlockModel.HEIGHT / 2, 80f);
    }

    private void createPhysicsBody() {
        float mass = 0.0f;
        shape = new btBoxShape(new Vector3(
                GroundBlockModel.SIZE * GroundLine.NUM_BLOCKS / 2,
                GroundBlockModel.HEIGHT / 2,
                GroundBlockModel.SIZE / 2
        ));
        if (mass > 0f) {
            shape.calculateLocalInertia(mass, localInertia);
        } else {
            localInertia.set(0, 0, 0);
        }
        constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, shape,
                localInertia);

        body = new btRigidBody(constructionInfo);
    }
}
