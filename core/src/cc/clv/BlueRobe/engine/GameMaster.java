package cc.clv.BlueRobe.engine;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.subjects.PublishSubject;

public class GameMaster {

    public enum Action {
        CHARACTER_MOVE_LEFT,
        CHARACTER_MOVE_RIGHT,
        CHARACTER_PREPARE_JUMP,
        CHARACTER_JUMP,
        CHARACTER_CANCEL_JUMP,
        GROUND_SPAWN_NEW_BLOCK,
    }

    public static final float GROUND_BLOCK_SPAWN_INTERVAL = 1f;

    private final Observable<Action> input;

    @lombok.Getter
    private final Character character;

    @lombok.Getter
    private boolean isRunning;

    private final GroundGenerator groundGenerator = new GroundGenerator("debug");
    private final PublishSubject<Action> actionSubject = PublishSubject.create();
    private final PublishSubject<Float> updateSubject = PublishSubject.create();
    private final Observable<Float> timeline = updateSubject.scan(new Func2<Float, Float, Float>() {
        @Override
        public Float call(Float aFloat, Float aFloat2) {
            return aFloat + aFloat2;
        }
    });
    private final GroundBlockEmitter groundBlockEmitter =
            new GroundBlockEmitter(timeline, GROUND_BLOCK_SPAWN_INTERVAL);

    private class ActionProcessor implements Action1<Action> {

        @Override
        public void call(Action action) {
            if (action == Action.GROUND_SPAWN_NEW_BLOCK) {
                groundGenerator.next();
            }
        }
    }

    private class GroundBlockEmitter {

        private float recentTime = 0.0f;
        private final float threshold;
        private final Observable<Float> timeline;

        @lombok.Getter
        private final PublishSubject<Action> emits = PublishSubject.create();

        public GroundBlockEmitter(Observable<Float> timeline, final float threshold) {
            this.timeline = timeline;
            this.threshold = threshold;
            timeline.subscribe(new Action1<Float>() {

                @Override
                public void call(Float time) {
                    float interval = time - recentTime;
                    if (interval > threshold) {
                        int times = (int) Math.floor(interval / threshold);
                        for (int i = 0; i < times; i++) {
                            emits.onNext(Action.GROUND_SPAWN_NEW_BLOCK);
                        }
                        recentTime = time;
                    }
                }
            });
        }
    }

    public GameMaster(Observable<Action> input) {
        this.input = input;
        this.character = new Character(input);
    }

    public void start() {
        this.groundGenerator.start();

        actionSubject
                .mergeWith(input)
                .mergeWith(groundBlockEmitter.getEmits())
                .subscribe(new ActionProcessor());

        isRunning = true;
    }

    public Ground getGround() {
        return this.groundGenerator.getGround();
    }

    public void update(float deltaTime) {
        updateSubject.onNext(deltaTime);
    }
}
