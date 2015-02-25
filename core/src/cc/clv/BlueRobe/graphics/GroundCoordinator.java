//package cc.clv.BlueRobe.graphics;
//
//import com.badlogic.gdx.graphics.g3d.Model;
//import com.badlogic.gdx.graphics.g3d.ModelInstance;
//
//import cc.clv.BlueRobe.engine.GroundBlock;
//import cc.clv.BlueRobe.engine.GroundGenerator;
//import rx.Observer;
//
///**
// * Created by slightair on 15/02/22.
// */
//public class GroundCoordinator {
//
//    private GroundGenerator generator;
//
//    public GroundCoordinator(GroundGenerator generator) {
//        generator.eventObserver = new Observer<GroundGenerator.Event>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(GroundGenerator.Event event) {
//                switch (event) {
//
//                    case UPDATED:
//                        layoutGround();
//                        break;
//                }
//            }
//        }
//        this.generator = generator;
//    }
//
//    private void layoutGround() {
//        Model whiteGround = new GroundBlockModel(new GroundBlock(GroundBlock.Type.DebugWhite));
//        Model grayGround = new GroundBlockModel(new GroundBlock(GroundBlock.Type.DebugGray));
//
//        int numTileVerticalHalf = numTileVertical / 2;
//        int numTileHorizontalHalf = numTileHorizontal / 2;
//        for (int z = -numTileVerticalHalf; z <= numTileVerticalHalf; z++) {
//            for (int x = -numTileHorizontalHalf; x <= numTileHorizontalHalf; x++) {
//                Model model = (x + z % 2) % 2 == 0 ? whiteGround : grayGround;
//                ModelInstance instance = new ModelInstance(model);
//                instance.transform
//                        .translate(x * GroundBlockModel.SIZE, -32 / 2, z * GroundBlockModel.SIZE);
//                instances.add(instance);
//            }
//        }
//    }
//}
