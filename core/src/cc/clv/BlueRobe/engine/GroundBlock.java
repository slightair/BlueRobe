package cc.clv.BlueRobe.engine;

/**
 * Created by slightair on 15/03/13.
 */

@lombok.Value
public class GroundBlock {

    public enum Type {
        DebugWhite,
        DebugGray,
    }

    Type type;
    int index;
    int lineIndex;

    @lombok.Getter(lombok.AccessLevel.NONE)
    boolean initial;

    Item item;

    public boolean isInitial() {
        return initial;
    }
}