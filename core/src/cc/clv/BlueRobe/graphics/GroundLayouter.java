package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cc.clv.BlueRobe.engine.GameMaster;
import cc.clv.BlueRobe.engine.Ground;
import cc.clv.BlueRobe.engine.GroundLine;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by slightair on 15/02/22.
 */
public class GroundLayouter {

    private final Ground ground;
    private final PhysicsMaster physicsMaster;
    private final LinkedList<GroundDivision> divisions = new LinkedList<GroundDivision>();

    public GroundLayouter(Ground ground, PhysicsMaster physicsMaster) {
        this.ground = ground;
        this.physicsMaster = physicsMaster;

        ground.getNewLines()
                .map(new GroundDivisionCreator())
                .subscribe(new GroundDivisionLayouter());
    }

    public void layoutGround() {
        divisions.clear();

        Observable.from(ground.getLines())
                .map(new GroundDivisionCreator())
                .subscribe(new GroundDivisionLayouter());
    }

    public void update(final float deltaTime) {
        float distance = (deltaTime / GameMaster.GROUND_LINE_SPAWN_INTERVAL)
                * GroundLineModel.DEPTH;
        Observable.from(divisions).subscribe(new GroundDivisionForwarder(distance));

        float expectFirstDivisionPosZ = GroundLineModel.DEPTH / 2 + GroundLineModel.DEPTH;
        float firstDivisionPosZ = divisions.getFirst().getLineModelInstance()
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

    private class GroundDivisionCreator implements Func1<GroundLine, GroundDivision> {

        @Override
        public GroundDivision call(GroundLine groundLine) {
            return new GroundDivision(groundLine);
        }
    }

    private class GroundDivisionLayouter implements Action1<GroundDivision> {

        @Override
        public void call(GroundDivision division) {
            layout(division);

            divisions.addLast(division);
            if (divisions.size() > Ground.NUM_LINES) {
                divisions.removeFirst();
            }
        }

        private void layout(GroundDivision division) {
            GroundLineModelInstance lineModelInstance = division.getLineModelInstance();
            if (lineModelInstance == null) {
                return;
            }

            GroundLine groundLine = lineModelInstance.getGroundLine();
            int index = groundLine.getIndex();

            float z;
            if (index == 0) {
                z = GroundLineModel.DEPTH / 2;
            } else {
                GroundLineModelInstance prevLine = divisions.getLast().getLineModelInstance();
                z = prevLine.transform.getTranslation(new Vector3()).z - GroundLineModel.DEPTH;
            }
            float y = -GroundLineModel.HEIGHT / 2;
            float x = 0;

            lineModelInstance.transform.translate(x, y, z);

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
