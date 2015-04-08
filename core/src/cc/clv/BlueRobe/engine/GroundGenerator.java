package cc.clv.BlueRobe.engine;

import java.util.LinkedList;

/**
 * Created by slightair on 15/02/22.
 */
public class GroundGenerator {

    @lombok.Getter
    private final Ground ground;

    private int currentBlockIndex = 0;

    public GroundGenerator() {
        this.ground = initialGround();
    }

    private Ground initialGround() {
        LinkedList<GroundBlock> blocks = new LinkedList();
        for (int i = 0; i < Ground.NUM_BLOCKS; i++) {
            blocks.add(createNewBlock());
        }

        return new Ground(blocks);
    }

    private GroundBlock createNewBlock() {
        GroundBlock.Type type = currentBlockIndex % 2 == 0 ?
                GroundBlock.Type.DebugWhite : GroundBlock.Type.DebugGray;
        GroundBlock block = new GroundBlock(type, currentBlockIndex);
        currentBlockIndex++;

        return block;
    }

    public void next() {
        ground.putBlock(createNewBlock());
    }
}
