package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.LinkedList;

import cc.clv.BlueRobe.engine.Ground;
import cc.clv.BlueRobe.engine.GroundBlock;
import cc.clv.BlueRobe.engine.GroundLine;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by slightair on 15/02/22.
 */
public class Groundlayouter {

    private final Ground ground;
    private final LineLayouter lineLayouter = new LineLayouter();
    private final int capacity = GroundLine.NUM_BLOCKS * Ground.NUM_LINES;
    private int lineCount = 0;
    private int firstLineIndex = 0;

    @lombok.Getter
    private final LinkedList<ModelInstance> blockInstances = new LinkedList<ModelInstance>();

    public Groundlayouter(Ground ground) {
        ground.getNewLines().subscribe(lineLayouter);
        this.ground = ground;
    }

    public void layoutGround() {
        blockInstances.clear();

        Observable.from(ground.getLines()).subscribe(lineLayouter);
    }

    class LineLayouter implements Action1<GroundLine> {

        class ModelInstanceCreator implements Func1<GroundBlock, GroundBlockModelInstance> {

            @Override
            public GroundBlockModelInstance call(GroundBlock groundBlock) {
                return GroundBlockModelInstance.create(groundBlock);
            }
        }

        class BlockLayouter implements Action1<GroundBlockModelInstance> {

            @Override
            public void call(GroundBlockModelInstance modelInstance) {
                GroundBlock groundBlock = modelInstance.getGroundBlock();

                int lineIndex = groundBlock.getLineIndex() - firstLineIndex;
                float z = -(lineIndex - Ground.NUM_LINES / 2) * GroundBlockModel.SIZE;
                float y = -GroundBlockModel.HEIGHT / 2;
                float x = (groundBlock.getIndex() - GroundLine.NUM_BLOCKS / 2)
                        * GroundBlockModel.SIZE;

                modelInstance.transform.translate(x, y, z);
                blockInstances.addLast(modelInstance);

                if (blockInstances.size() > capacity) {
                    blockInstances.removeFirst();
                }
            }
        }

        @Override
        public void call(GroundLine groundLine) {
            lineCount++;
            if (lineCount > Ground.NUM_LINES) {
                firstLineIndex++;
            }

            Observable.from(groundLine.getBlocks())
                    .map(new ModelInstanceCreator())
                    .subscribe(new BlockLayouter());
        }
    }
}
