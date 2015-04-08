package cc.clv.BlueRobe.engine;

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
        for (int i = 0; i < Ground.NUM_LINES; i++) {
            lines.add(createNewLine());
        }

        return new Ground(lines);
    }

    private GroundLine createNewLine() {
        GroundLine.Type type = currentLineIndex % 2 == 0 ?
                GroundLine.Type.DebugWhite : GroundLine.Type.DebugGray;
        GroundLine line = new GroundLine(type, currentLineIndex);
        currentLineIndex++;

        return line;
    }

    public void next() {
        ground.putLine(createNewLine());
    }
}
