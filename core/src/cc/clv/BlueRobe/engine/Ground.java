package cc.clv.BlueRobe.engine;

import java.util.LinkedList;

import rx.Observable;
import rx.subjects.PublishSubject;

public class Ground {

    public static final int NUM_BLOCKS = 3;

    private final PublishSubject<GroundBlock> subject = PublishSubject.create();

    @lombok.Getter
    private final LinkedList<GroundBlock> blocks;

    public Ground(LinkedList<GroundBlock> blocks) {
        this.blocks = blocks;
    }

    public Observable<GroundBlock> getNewBlocks() {
        return subject;
    }

    public void putBlock(GroundBlock block) {
        blocks.addLast(block);
        if (blocks.size() > NUM_BLOCKS) {
            blocks.removeFirst();
        }
        subject.onNext(block);
    }
}
