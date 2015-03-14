package cc.clv.BlueRobe.engine;

import java.util.ArrayList;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by slightair on 15/03/11.
 */
public class Ground {

    public static int NUM_LINES = 24;

    private final PublishSubject<GroundLine> subject = PublishSubject.create();

    @lombok.Getter
    private final ArrayList<GroundLine> lines;

    public Observable<GroundLine> getNewLines() {
        return subject;
    }

    public Ground(ArrayList<GroundLine> lines) {
        this.lines = lines;
    }
}
