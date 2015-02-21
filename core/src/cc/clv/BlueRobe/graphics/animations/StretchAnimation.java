package cc.clv.BlueRobe.graphics.animations;

import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodeAnimation;
import com.badlogic.gdx.graphics.g3d.model.NodeKeyframe;

/**
 * Created by slightair on 15/02/21.
 */
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

        NodeKeyframe keyframe;

        keyframe = new NodeKeyframe();
        keyframe.keytime = 0;
        keyframe.scale.x = 1.1f;
        keyframe.scale.y = 0.9f;
        keyframe.scale.z = 1.1f;
        animation.keyframes.add(keyframe);

        keyframe = new NodeKeyframe();
        keyframe.keytime = duration / 2;
        keyframe.scale.x = 0.9f;
        keyframe.scale.y = 1.1f;
        keyframe.scale.z = 0.9f;
        animation.keyframes.add(keyframe);

        keyframe = new NodeKeyframe();
        keyframe.keytime = duration;
        keyframe.scale.x = 1.1f;
        keyframe.scale.y = 0.9f;
        keyframe.scale.z = 1.1f;
        animation.keyframes.add(keyframe);

        nodeAnimations.add(animation);
    }
}

