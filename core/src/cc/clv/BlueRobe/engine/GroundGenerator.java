package cc.clv.BlueRobe.engine;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by slightair on 15/02/22.
 */
public class GroundGenerator {

    @lombok.Getter
    private final Ground ground;

    private int currentLineIndex = 0;

    public GroundGenerator() {
        this.ground = initialGround();
    }

    private Ground initialGround() {
        LinkedList<GroundLine> lines = new LinkedList();

        for (int y = 0; y < Ground.NUM_LINES; y++) {
            ArrayList<GroundBlock> blocks = new ArrayList();
            for (int x = 0; x < GroundLine.NUM_BLOCKS; x++) {
                GroundBlock.Type blockType = (x + y % 2) % 2 == 0 ?
                        GroundBlock.Type.DebugWhite : GroundBlock.Type.DebugGray;
                blocks.add(createBlock(blockType, x, y, true));
            }
            lines.add(new GroundLine(blocks, y));
        }
        currentLineIndex = Ground.NUM_LINES - 1;

        return new Ground(lines);
    }

    private GroundBlock createBlock(GroundBlock.Type type, int x, int y, boolean isInitial) {
        Item item = null;

        if (!isInitial && Math.random() < 0.01) {
            item = new Item("mushroom");
        }
        GroundBlock block = new GroundBlock(type, x, y, isInitial, item);

        return block;
    }

    public void next() {
        currentLineIndex++;

        ArrayList<GroundBlock> blocks = new ArrayList();
        for (int x = 0; x < GroundLine.NUM_BLOCKS; x++) {
            GroundBlock.Type blockType = (x + currentLineIndex % 2) % 2 == 0 ?
                    GroundBlock.Type.DebugWhite : GroundBlock.Type.DebugGray;
            blocks.add(createBlock(blockType, x, currentLineIndex, false));
        }
        ground.putLine(new GroundLine(blocks, currentLineIndex));
    }
}
