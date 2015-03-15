package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.LinkedList;

import cc.clv.BlueRobe.engine.GameMaster;
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

    public void update(float deltaTime) {
        Observable.from(blockInstances).subscribe(new BlockForwarder(deltaTime));
    }

    private class BlockForwarder implements Action1<ModelInstance> {

        private final float deltaTime;

        public BlockForwarder(float deltaTime) {
            this.deltaTime = deltaTime;
        }

        @Override
        public void call(ModelInstance modelInstance) {
            modelInstance.transform.translate(0, 0,
                    (deltaTime / GameMaster.GROUND_LINE_SPAWN_INTERVAL) * GroundBlockModel.SIZE);
        }
    }

    private class LineLayouter implements Action1<GroundLine> {

        private class ModelInstanceCreator implements Func1<GroundBlock, GroundBlockModelInstance> {

            @Override
            public GroundBlockModelInstance call(GroundBlock groundBlock) {
                return GroundBlockModelInstance.create(groundBlock);
            }
        }

        private class BlockLayouter implements Action1<GroundBlockModelInstance> {

            @Override
            public void call(GroundBlockModelInstance modelInstance) {
                GroundBlock groundBlock = modelInstance.getGroundBlock();

                float z = groundBlock.isInitial() ?
                        ((Ground.NUM_LINES / 2) - groundBlock.getLineIndex())
                                * GroundBlockModel.SIZE
                        : -(Ground.NUM_LINES / 2 - 1) * GroundBlockModel.SIZE;
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
            Observable.from(groundLine.getBlocks())
                    .map(new ModelInstanceCreator())
                    .subscribe(new BlockLayouter());
        }
    }
}
