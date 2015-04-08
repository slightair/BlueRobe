package cc.clv.BlueRobe.engine;

/**
 * Created by slightair on 15/03/13.
 */


@lombok.Value
public class GroundLine {

    public enum Type {
        DebugWhite,
        DebugGray,
    }

    Type type;
    int index;
}
