package cc.clv.BlueRobe.engine;

@lombok.Builder
public class GroundBlock {

    public enum Type {
        DebugWhite,
        DebugGray,
        Grass,
    }

    @lombok.Getter
    private Type type;

    @lombok.Getter
    private int index;
}
