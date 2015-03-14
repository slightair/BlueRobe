package cc.clv.BlueRobe.engine;

import java.util.ArrayList;

/**
 * Created by slightair on 15/02/22.
 */
public class GroundGenerator {

    @lombok.Getter
    private final Ground ground;

    public GroundGenerator() {
        this.ground = initialGround();
    }

    private Ground initialGround() {
        ArrayList<GroundLine> lines = new ArrayList();

        for (int y = 0; y < Ground.NUM_LINES; y++) {
            ArrayList<GroundBlock> blocks = new ArrayList();
            for (int x = 0; x < GroundLine.NUM_BLOCKS; x++) {
                GroundBlock.Type blockType = (x + y % 2) % 2 == 0 ?
                        GroundBlock.Type.DebugWhite : GroundBlock.Type.DebugGray;
                blocks.add(new GroundBlock(blockType, x, y));
            }
            lines.add(new GroundLine(blocks, y));
        }

        return new Ground(lines);
    }
}
