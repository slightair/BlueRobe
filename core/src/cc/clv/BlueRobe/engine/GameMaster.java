package cc.clv.BlueRobe.engine;

import cc.clv.BlueRobe.BlueRobe;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by slightair on 15/02/27.
 */
public class GameMaster {

    private Observable<BlueRobe.Action> actionObservable;

    @lombok.Getter
    private final Character character;

    public GameMaster(Observable<BlueRobe.Action> actionObservable) {
        actionObservable.subscribe(new Action1<BlueRobe.Action>() {
            @Override
            public void call(BlueRobe.Action action) {
            }
        });
        this.actionObservable = actionObservable;
        this.character = new Character(actionObservable);
    }
}
