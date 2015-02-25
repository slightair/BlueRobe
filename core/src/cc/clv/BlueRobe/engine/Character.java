package cc.clv.BlueRobe.engine;

import cc.clv.BlueRobe.BlueRobe;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by slightair on 15/02/27.
 */
public class Character {

    public enum Action {
        MOVE_LEFT,
        MOVE_RIGHT,
        PREPARE_JUMP,
        JUMP,
        CANCEL_JUMP,
    }

    private final Observable<BlueRobe.Action> actionObservable;

    public Character(Observable<BlueRobe.Action> actionObservable) {
        this.actionObservable = actionObservable;
    }

    public Observable<Action> getActions() {
        return actionObservable.map(new Func1<BlueRobe.Action, Action>() {
            @Override
            public Action call(BlueRobe.Action action) {
                Action result = null;
                switch (action) {
                    case CHARACTER_MOVE_LEFT:
                        result = Action.MOVE_LEFT;
                        break;
                    case CHARACTER_MOVE_RIGHT:
                        result = Action.MOVE_RIGHT;
                        break;
                    case CHARACTER_PREPARE_JUMP:
                        result = Action.PREPARE_JUMP;
                        break;
                    case CHARACTER_JUMP:
                        result = Action.JUMP;
                        break;
                    case CHARACTER_CANCEL_JUMP:
                        result = Action.CANCEL_JUMP;
                        break;
                }
                return result;
            }
        });
    }
}
