package cc.clv.BlueRobe.graphics.animations;

import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodeAnimation;
import com.badlogic.gdx.graphics.g3d.model.NodeKeyframe;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by slightair on 2015/02/08.
 */
public class ShrinkAnimation extends Animation {

    public static final float defaultDuration = 0.1f;

    public ShrinkAnimation(Node node) {
        this(node, defaultDuration);
    }

    public ShrinkAnimation(Node node, float duration) {
        this.id = "shrink";
        this.duration = duration;

        NodeAnimation animation = new NodeAnimation();
        animation.node = node;

        Array<NodeKeyframe<Vector3>> scaling = new Array<NodeKeyframe<Vector3>>();
        scaling.add(new NodeKeyframe(0, new Vector3(1.0f, 1.0f, 1.0f)));
        scaling.add(new NodeKeyframe(duration, new Vector3(1.1f, 0.9f, 1.1f)));
        animation.scaling = scaling;

        nodeAnimations.add(animation);
    }
}
