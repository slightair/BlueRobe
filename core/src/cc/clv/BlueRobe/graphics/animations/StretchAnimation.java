package cc.clv.BlueRobe.graphics.animations;

import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodeAnimation;
import com.badlogic.gdx.graphics.g3d.model.NodeKeyframe;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class StretchAnimation extends Animation {

    public static final float defaultDuration = 1.2f;

    public StretchAnimation(Node node) {
        this(node, defaultDuration);
    }

    public StretchAnimation(Node node, float duration) {
        this.id = "stretch";
        this.duration = duration;

        NodeAnimation animation = new NodeAnimation();
        animation.node = node;

        Array<NodeKeyframe<Vector3>> scaling = new Array<NodeKeyframe<Vector3>>();
        scaling.add(new NodeKeyframe(0, new Vector3(1.1f, 0.9f, 1.1f)));
        scaling.add(new NodeKeyframe(duration / 2, new Vector3(0.9f, 1.1f, 0.9f)));
        scaling.add(new NodeKeyframe(duration, new Vector3(1.1f, 0.9f, 1.1f)));
        animation.scaling = scaling;

        nodeAnimations.add(animation);
    }
}

