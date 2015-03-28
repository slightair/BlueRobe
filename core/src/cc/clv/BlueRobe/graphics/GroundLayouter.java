package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
public class GroundLayouter {

    private final Ground ground;
    private final LineLayouter lineLayouter = new LineLayouter();
    private final int capacity = GroundLine.NUM_BLOCKS * Ground.NUM_LINES;

    private final LinkedList<ModelInstanceGroup> modelInstanceGroups
            = new LinkedList<ModelInstanceGroup>();

    public GroundLayouter(Ground ground) {
        ground.getNewLines().subscribe(lineLayouter);
        this.ground = ground;
    }

    public void layoutGround() {
        blockInstances.clear();

        Observable.from(ground.getLines()).subscribe(lineLayouter);
    }

    public void update(float deltaTime) {
        float distance = (deltaTime / GameMaster.GROUND_LINE_SPAWN_INTERVAL)
                * GroundBlockModel.SIZE;
        Observable.from(blockInstances).subscribe(new BlockForwarder(distance));

        float gap = (Ground.NUM_LINES / 2 * GroundBlockModel.SIZE) - blockInstances
                .getFirst().transform.getTranslation(new Vector3()).z;
        if (Math.abs(gap) > GroundBlockModel.SIZE) {
            Observable.from(blockInstances)
                    .subscribe(new BlockForwarder(gap % GroundBlockModel.SIZE));
        }
    }

    public List<ModelInstance> getModelInstances() {
        return Observable.from(modelInstanceGroups)
                .map(new Func1<ModelInstanceGroup, ArrayList<ModelInstance>>() {
                    @Override
                    public ArrayList<ModelInstance> call(ModelInstanceGroup instanceGroup) {
                        return instanceGroup.getInstances();
                    }
                }).flatMap(new Func1<ArrayList<ModelInstance>, Observable<ModelInstance>>() {
                    @Override
                    public Observable<ModelInstance> call(ArrayList<ModelInstance> modelInstances) {
                        return Observable.from(modelInstances);
                    }
                }).toList().toBlocking().single();
    }

    private class BlockForwarder implements Action1<ModelInstance> {

        private final float distanceZ;

        public BlockForwarder(float distanceZ) {
            this.distanceZ = distanceZ;
        }

        @Override
        public void call(ModelInstance modelInstance) {
            modelInstance.transform.translate(0, 0, distanceZ);
        }
    }

    private class LineLayouter implements Action1<GroundLine> {

        private class ModelInstanceGroupCreator implements Func1<GroundBlock, ModelInstanceGroup> {

            @Override
            public ModelInstanceGroup call(GroundBlock groundBlock) {
                ModelInstanceGroup instanceGroup = new ModelInstanceGroup();

                instanceGroup.add(GroundBlockModelInstance.create(groundBlock));

                return instanceGroup;
            }
        }

        private class InstanceGroupLayouter implements Action1<ModelInstanceGroup> {

            @Override
            public void call(ModelInstanceGroup instanceGroup) {

                for (ModelInstance instance : instanceGroup.getInstances()) {
                    if (instance.getClass() == GroundBlockModelInstance.class) {
                        GroundBlockModelInstance modelInstance
                                = (GroundBlockModelInstance) instance;
                        GroundBlock groundBlock = modelInstance.getGroundBlock();

                        float z = ((Ground.NUM_LINES / 2) - groundBlock.getLineIndex())
                                * GroundBlockModel.SIZE;
                        if (!groundBlock.isInitial()) {
                            GroundBlockModelInstance prevBlock = blockInstances
                                    .get((Ground.NUM_LINES - 1) * GroundLine.NUM_BLOCKS);
                            z = prevBlock.transform.getTranslation(new Vector3()).z
                                    - GroundBlockModel.SIZE;
                        }
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
            }
        }

        @Override
        public void call(GroundLine groundLine) {
            Observable.from(groundLine.getBlocks())
                    .map(new ModelInstanceGroupCreator())
                    .subscribe(new InstanceGroupLayouter());
        }
    }
}
