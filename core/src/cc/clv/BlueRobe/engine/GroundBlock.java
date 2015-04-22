package cc.clv.BlueRobe.engine;

import java.util.ArrayList;

@lombok.Builder
@lombok.Value
public class GroundBlock {

    public static final int NUM_VERTICAL_CELLS = 10;

    public enum Type {
        DebugWhite,
        DebugGray,
        Grass,
    }

    Type type;
    int index;
    ArrayList<Object> objects;
    ArrayList<Object> items;
    ArrayList<Object> obstacles;
}
