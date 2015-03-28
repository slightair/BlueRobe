package cc.clv.BlueRobe.graphics.animations;

import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodeAnimation;
import com.badlogic.gdx.graphics.g3d.model.NodeKeyframe;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by slightair on 2015/02/01.
 */
public class JumpAnimation extends Animation {

    public static final float defaultDuration = 0.5f;

    public JumpAnimation(Node node, float height) {
        this(node, height, defaultDuration);
    }

    public JumpAnimation(Node node, float height, float duration) {
        this.id = "jump";
        this.duration = duration;

        float deltaTime = duration / 20;
        NodeAnimation animation = new NodeAnimation();
        animation.node = node;

        Array<NodeKeyframe<Vector3>> translation = new Array<NodeKeyframe<Vector3>>();
        for (int t = 0; t < 21; t++) {
            float process = 0.1f * t;
            float y = height * (9.8f * process - 4.9f * (process * process));
            Vector3 value = new Vector3(0f, y, 0f);
            translation.add(new NodeKeyframe<Vector3>(deltaTime * t, value));
        }
        animation.translation = translation;

        nodeAnimations.add(animation);
    }
}
