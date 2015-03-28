package cc.clv.BlueRobe.engine;

import java.util.LinkedList;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by slightair on 15/03/11.
 */
public class Ground {

    public static final int NUM_LINES = 24;

    private final PublishSubject<GroundLine> subject = PublishSubject.create();

    @lombok.Getter
    private final LinkedList<GroundLine> lines;

    public Ground(LinkedList<GroundLine> lines) {
        this.lines = lines;
    }

    public Observable<GroundLine> getNewLines() {
        return subject;
    }

    public void putLine(GroundLine line) {
        lines.addLast(line);
        if (lines.size() > NUM_LINES) {
            lines.removeFirst();
        }
        subject.onNext(line);
    }
}
