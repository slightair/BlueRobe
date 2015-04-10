package cc.clv.BlueRobe.graphics.animations;

import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodeAnimation;
import com.badlogic.gdx.graphics.g3d.model.NodeKeyframe;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class ReturnShrinkAnimation extends Animation {

    public static final float defaultDuration = 0.1f;

    public ReturnShrinkAnimation(Node node) {
        this(node, defaultDuration);
    }

    public ReturnShrinkAnimation(Node node, float duration) {
        this.id = "returnShrink";
        this.duration = duration;

        NodeAnimation animation = new NodeAnimation();
        animation.node = node;

        Array<NodeKeyframe<Vector3>> scaling = new Array<NodeKeyframe<Vector3>>();
        scaling.add(new NodeKeyframe(0, new Vector3(1.1f, 0.9f, 1.1f)));
        scaling.add(new NodeKeyframe(duration, new Vector3(1.0f, 1.0f, 1.0f)));
        animation.scaling = scaling;

        nodeAnimations.add(animation);
    }
}
