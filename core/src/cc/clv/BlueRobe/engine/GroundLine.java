package cc.clv.BlueRobe.engine;

import java.util.ArrayList;

/**
 * Created by slightair on 15/03/13.
 */


@lombok.Value
public class GroundLine {

    public static final int NUM_BLOCKS = 9;

    ArrayList<GroundBlock> blocks;
    int index;
}
