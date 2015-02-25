//package cc.clv.BlueRobe.engine;
//
//import java.util.ArrayList;
//
//import rx.Observable;
//import rx.Observer;
//
///**
// * Created by slightair on 15/02/22.
// */
//public class GroundGenerator {
//
//    public final int numBlockHorizontal = 9;
//    public final int numBlockVertical = 24;
//
//    public enum Event {
//        UPDATED
//    }
//
//    public Observer<Event> eventObserver;
//    @lombok.Getter
//    private ArrayList<ArrayList<GroundBlock>> lines;
//
//    public GroundGenerator() {
//        createGround();
//    }
//
//    private void createGround() {
//        ArrayList lines = new ArrayList();
//        for (int y = 0; y < numBlockVertical; y++) {
//            ArrayList line = new ArrayList();
//            for (int x = 0; x < numBlockHorizontal; x++) {
//                GroundBlock.Type type = (x + y % 2) % 2 == 0 ?
//                        GroundBlock.Type.DebugWhite : GroundBlock.Type.DebugGray;
//
//                GroundBlock block = new GroundBlock(type);
//                line.add(block);
//            }
//            lines.add(line);
//        }
//        this.lines = lines;
//    }
//
//    private void updateGround() {
//        Observable.just(Event.UPDATED).subscribe(eventObserver);
//    }
//}
