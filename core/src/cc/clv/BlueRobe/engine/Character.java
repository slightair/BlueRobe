package cc.clv.BlueRobe.engine;

import rx.Observable;
import rx.functions.Func1;

public class Character {

    public enum Action {
        MOVE_LEFT,
        MOVE_RIGHT,
        PREPARE_JUMP,
        JUMP,
        CANCEL_JUMP,
        UNKNOWN,
    }

    private final Observable<GameMaster.Action> input;

    private class ActionConverter implements Func1<GameMaster.Action, Action> {

        @Override
        public Action call(GameMaster.Action action) {
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
                default:
                    result = Action.UNKNOWN;
            }
            return result;
        }
    }

    public Character(Observable<GameMaster.Action> input) {
        this.input = input;
    }

    public Observable<Action> getActions() {
        return input.map(new ActionConverter());
    }
}
