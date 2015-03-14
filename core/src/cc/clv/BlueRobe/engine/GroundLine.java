package cc.clv.BlueRobe.engine;

import java.util.ArrayList;

/**
 * Created by slightair on 15/03/13.
 */
public class GroundLine {

    public static int NUM_BLOCKS = 9;

    @lombok.Getter
    private final ArrayList<GroundBlock> blocks;

    @lombok.Getter
    private final int index;

    public GroundLine(ArrayList<GroundBlock> blocks, int index) {
        this.blocks = blocks;
        this.index = index;
    }
}
