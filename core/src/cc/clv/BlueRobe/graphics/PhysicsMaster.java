package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;

/**
 * Created by slightair on 15/03/28.
 */
public class PhysicsMaster {

    private final btCollisionConfiguration collisionConfiguration;
    private final btDispatcher dispatcher;
    private final btBroadphaseInterface broadphaseInterface;
    private final ObjectContactListener contactListener;
    private final btConstraintSolver constraintSolver;

    @lombok.Getter
    private final btDynamicsWorld dynamicsWorld;

    private static final short COLLISION_FLAG_GROUND = 1 << 8;
    private static final short COLLISION_FLAG_OBJECT = 1 << 9;
    private static final short COLLISION_FLAG_CHARACTER = 1 << 10;
    private static final short COLLISION_FLAG_ALL = -1;

    class ObjectContactListener extends ContactListener {

        @Override
        public boolean onContactAdded(int userValue0, int partId0, int index0,
                int userValue1, int partId1, int index1) {
            return true;
        }
    }

    public PhysicsMaster() {
        collisionConfiguration = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfiguration);
        broadphaseInterface = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphaseInterface,
                constraintSolver, collisionConfiguration);
        contactListener = new ObjectContactListener();

        dynamicsWorld.setGravity(new Vector3(0, -980.0f, 0));
    }

    public void stepSimulation(float deltaTime) {
        final float delta = Math.min(1f / 30f, deltaTime);
        dynamicsWorld.stepSimulation(delta, 5, 1f / 60f);
    }

    public void addPhantomGround(PhantomGround phantomGround) {
        dynamicsWorld.addRigidBody(phantomGround.getBody(),
                COLLISION_FLAG_GROUND, COLLISION_FLAG_CHARACTER);
    }

    public void addItem(ItemModelInstance itemModelInstance) {

    }

    public void removeItem(ItemModelInstance itemModelInstance) {

    }

    public void addCharacter(CharacterModelInstance characterModelInstance) {
        dynamicsWorld.addRigidBody(characterModelInstance.getBody(),
                COLLISION_FLAG_CHARACTER, COLLISION_FLAG_ALL);
    }
}
