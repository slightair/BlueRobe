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
import cc.clv.BlueRobe.engine.Item;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by slightair on 15/02/22.
 */
public class GroundLayouter {

    private final Ground ground;
    private final PhysicsMaster physicsMaster;
    private final LineLayouter lineLayouter = new LineLayouter();
    private final int capacity = GroundLine.NUM_BLOCKS * Ground.NUM_LINES;

    private final LinkedList<GroundDivision> divisions = new LinkedList<GroundDivision>();

    public GroundLayouter(Ground ground, PhysicsMaster physicsMaster) {
        this.ground = ground;
        this.physicsMaster = physicsMaster;

        ground.getNewLines().subscribe(lineLayouter);
    }

    public void layoutGround() {
        divisions.clear();

        Observable.from(ground.getLines()).subscribe(lineLayouter);
    }

    public void update(final float deltaTime) {
        float distance = (deltaTime / GameMaster.GROUND_LINE_SPAWN_INTERVAL)
                * GroundBlockModel.SIZE;
        Observable.from(divisions).subscribe(new DivisionForwarder(distance));

        float gap = (Ground.NUM_LINES / 2 * GroundBlockModel.SIZE) - divisions
                .getFirst().getBlockModelInstance().transform.getTranslation(new Vector3()).z;
        if (Math.abs(gap) > GroundBlockModel.SIZE) {
            Observable.from(divisions)
                    .subscribe(new DivisionForwarder(gap % GroundBlockModel.SIZE));
        }

        Observable.from(divisions)
                .map(new Func1<GroundDivision, List<AnimatableModelInstance>>() {
                    @Override
                    public List<AnimatableModelInstance> call(GroundDivision division) {
                        return division.getAnimatableModelInstances();
                    }
                }).flatMap(
                new Func1<List<AnimatableModelInstance>, Observable<AnimatableModelInstance>>() {
                    @Override
                    public Observable<AnimatableModelInstance> call(
                            List<AnimatableModelInstance> animatableModelInstances) {
                        return Observable.from(animatableModelInstances);
                    }
                })
                .subscribe(new Action1<AnimatableModelInstance>() {
                    @Override
                    public void call(AnimatableModelInstance animatableModelInstance) {
                        animatableModelInstance.updateAnimation(deltaTime);
                    }
                });
    }

    public List<ModelInstance> getModelInstances() {
        return Observable.from(divisions)
                .map(new Func1<GroundDivision, ArrayList<ModelInstance>>() {
                    @Override
                    public ArrayList<ModelInstance> call(GroundDivision division) {
                        return division.getModelInstances();
                    }
                }).flatMap(new Func1<ArrayList<ModelInstance>, Observable<ModelInstance>>() {
                    @Override
                    public Observable<ModelInstance> call(ArrayList<ModelInstance> modelInstances) {
                        return Observable.from(modelInstances);
                    }
                }).toList().toBlocking().single();
    }

    private class DivisionForwarder implements Action1<GroundDivision> {

        private final float distanceZ;

        public DivisionForwarder(float distanceZ) {
            this.distanceZ = distanceZ;
        }

        @Override
        public void call(GroundDivision division) {
            GroundBlockModelInstance blockModelInstance = division.getBlockModelInstance();
            if (blockModelInstance != null) {
                blockModelInstance.transform.translate(0, 0, distanceZ);
            }

            ItemModelInstance itemModelInstance = division.getItemModelInstance();
            if (itemModelInstance != null) {
                itemModelInstance.transform.translate(0, 0, distanceZ);
            }
        }
    }

    private class LineLayouter implements Action1<GroundLine> {

        private class GroundDivisionCreator implements Func1<GroundBlock, GroundDivision> {

            @Override
            public GroundDivision call(GroundBlock groundBlock) {
                GroundBlockModelInstance blockModelInstance = GroundBlockModelInstance
                        .create(groundBlock);

                ItemModelInstance itemModelInstance = null;
                Item item = groundBlock.getItem();

                if (item != null) {
                    itemModelInstance = ItemModelInstance.create(item);
                }

                return new GroundDivision(blockModelInstance, itemModelInstance);
            }
        }

        private class GroundDivisionLayouter implements Action1<GroundDivision> {

            @Override
            public void call(GroundDivision division) {
                GroundBlockModelInstance blockModelInstance = division.getBlockModelInstance();

                if (blockModelInstance != null) {
                    layoutBlock(blockModelInstance);
                    layoutItem(division.getItemModelInstance(), blockModelInstance);
                }

                divisions.addLast(division);
                if (divisions.size() > capacity) {
                    divisions.removeFirst();
                }
            }

            private void layoutBlock(GroundBlockModelInstance instance) {
                if (instance == null) {
                    return;
                }

                GroundBlock groundBlock = instance.getGroundBlock();

                float z = ((Ground.NUM_LINES / 2) - groundBlock.getLineIndex())
                        * GroundBlockModel.SIZE;

                if (!groundBlock.isInitial()) {
                    GroundDivision lastLineDivision = divisions
                            .get((Ground.NUM_LINES - 1) * GroundLine.NUM_BLOCKS);

                    GroundBlockModelInstance prevBlock = lastLineDivision.getBlockModelInstance();
                    z = prevBlock.transform.getTranslation(new Vector3()).z - GroundBlockModel.SIZE;
                }

                float y = -GroundBlockModel.HEIGHT / 2;
                float x = (groundBlock.getIndex() - GroundLine.NUM_BLOCKS / 2)
                        * GroundBlockModel.SIZE;

                instance.transform.translate(x, y, z);
            }

            private void layoutItem(ItemModelInstance itemModelInstance,
                    GroundBlockModelInstance blockModelInstance) {

                if (itemModelInstance == null) {
                    return;
                }

                Vector3 position = blockModelInstance.transform.getTranslation(new Vector3());
                position.y = 0;

                itemModelInstance.transform.translate(position);
            }
        }

        @Override
        public void call(GroundLine groundLine) {
            Observable.from(groundLine.getBlocks())
                    .map(new GroundDivisionCreator())
                    .subscribe(new GroundDivisionLayouter());
        }
    }
}
