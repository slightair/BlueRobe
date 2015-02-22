package cc.clv.BlueRobe.engine;

/**
 * Created by slightair on 15/02/22.
 */

@lombok.Value
public class GroundBlock {

    public enum Type {
        DebugWhite,
        DebugGray,
    }

    private final Type type;
}
