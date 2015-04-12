package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cc.clv.BlueRobe.engine.GameMaster;
import cc.clv.BlueRobe.engine.Ground;
import cc.clv.BlueRobe.engine.GroundBlock;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class GroundLayouter {

    private final PhysicsMaster physicsMaster;
    private final LinkedList<GroundDivision> divisions = new LinkedList<GroundDivision>();

    public GroundLayouter(PhysicsMaster physicsMaster) {
        this.physicsMaster = physicsMaster;
    }

    public void layoutGround(Ground ground) {
        divisions.clear();

        ground.getNewBlocks()
                .map(new GroundDivisionCreator())
                .subscribe(new GroundDivisionLayouter());

        Observable.from(ground.getBlocks())
                .map(new GroundDivisionCreator())
                .subscribe(new GroundDivisionLayouter());
    }

    public void update(final float deltaTime) {
        float distance = (deltaTime / GameMaster.GROUND_BLOCK_SPAWN_INTERVAL)
                * GroundBlockModel.DEPTH;
        Observable.from(divisions).subscribe(new GroundDivisionForwarder(distance));

        float expectFirstDivisionPosZ = GroundBlockModel.DEPTH / 2 + GroundBlockModel.DEPTH;
        float firstDivisionPosZ = divisions.getFirst().getBlockModelInstance()
                .transform.getTranslation(new Vector3()).z;
        float gap = firstDivisionPosZ - expectFirstDivisionPosZ;

        if (gap > 0) {
            Observable.from(divisions).subscribe(new GroundDivisionForwarder(-gap));
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

    private class GroundDivisionCreator implements Func1<GroundBlock, GroundDivision> {

        @Override
        public GroundDivision call(GroundBlock groundBlock) {
            return new GroundDivision(groundBlock);
        }
    }

    private class GroundDivisionLayouter implements Action1<GroundDivision> {

        @Override
        public void call(GroundDivision division) {
            layout(division);

            divisions.addLast(division);
            if (divisions.size() > Ground.NUM_BLOCKS) {
                divisions.removeFirst();
            }
        }

        private void layout(GroundDivision division) {
            GroundBlockModelInstance blockModelInstance = division.getBlockModelInstance();
            if (blockModelInstance == null) {
                return;
            }

            GroundBlock groundBlock = blockModelInstance.getGroundBlock();
            int index = groundBlock.getIndex();

            float z;
            if (index == 0) {
                z = GroundBlockModel.DEPTH / 2;
            } else {
                GroundBlockModelInstance prevBlock = divisions.getLast().getBlockModelInstance();
                z = prevBlock.transform.getTranslation(new Vector3()).z - GroundBlockModel.DEPTH;
            }
            float y = -GroundBlockModel.HEIGHT / 2;
            float x = 0;

            blockModelInstance.transform.translate(x, y, z);

            for (ItemModelInstance itemModelInstance : division.getItemModelInstances()) {
                Vector3 position = itemModelInstance.transform.getTranslation(new Vector3())
                        .add(x, y, z);
                itemModelInstance.transform.translate(position);
            }
        }
    }

    private class GroundDivisionForwarder implements Action1<GroundDivision> {

        private final float distanceZ;

        public GroundDivisionForwarder(float distanceZ) {
            this.distanceZ = distanceZ;
        }

        @Override
        public void call(GroundDivision division) {
            for (ModelInstance instance : division.getModelInstances()) {
                instance.transform.translate(0, 0, distanceZ);
            }
        }
    }
}
