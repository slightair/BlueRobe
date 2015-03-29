package cc.clv.BlueRobe.graphics;

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

    class ObjectContactListener extends ContactListener {

        @Override
        public boolean onContactAdded(int userValue0, int partId0, int index0,
                int userValue1, int partId1, int index1) {
            return super.onContactAdded(userValue0, partId0, index0, userValue1, partId1, index1);
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
    }

}
