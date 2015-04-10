package cc.clv.BlueRobe.engine;

@lombok.Value
public class GroundBlock {

    public enum Type {
        DebugWhite,
        DebugGray,
    }

    Type type;
    int index;
}
