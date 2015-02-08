package cc.clv.BlueRobe.graphics.animations;

import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodeAnimation;
import com.badlogic.gdx.graphics.g3d.model.NodeKeyframe;

/**
 * Created by slightair on 2015/02/01.
 */
public class JumpAnimation extends Animation {
    public static final float defaultDuration = 1.0f;

    public JumpAnimation(Node node, float height) {
        this(node, height, defaultDuration);
    }

    public JumpAnimation(Node node, float height, float duration) {
        this.id = "jump";
        this.duration = duration;

        float deltaTime = duration / 21;
        NodeAnimation animation = new NodeAnimation();
        animation.node = node;
        for(int t = 0; t < 21; t++) {
            float process = 0.1f * t;
            NodeKeyframe keyframe = new NodeKeyframe();
            keyframe.keytime = deltaTime * t;
            keyframe.translation.y = height * (9.8f * process - 4.9f * (process * process));
            animation.keyframes.add(keyframe);
        }
        nodeAnimations.add(animation);
    }
}
