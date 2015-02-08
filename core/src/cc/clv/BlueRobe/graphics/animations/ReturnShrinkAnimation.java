package cc.clv.BlueRobe.graphics.animations;

import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodeAnimation;
import com.badlogic.gdx.graphics.g3d.model.NodeKeyframe;

/**
 * Created by slightair on 2015/02/08.
 */
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

        NodeKeyframe keyframe;

        keyframe = new NodeKeyframe();
        keyframe.keytime = 0;
        keyframe.scale.x = 1.1f;
        keyframe.scale.y = 0.9f;
        keyframe.scale.z = 1.1f;
        animation.keyframes.add(keyframe);

        keyframe = new NodeKeyframe();
        keyframe.keytime = duration;
        keyframe.scale.x = 1.0f;
        keyframe.scale.y = 1.0f;
        keyframe.scale.z = 1.0f;
        animation.keyframes.add(keyframe);

        nodeAnimations.add(animation);
    }
}
