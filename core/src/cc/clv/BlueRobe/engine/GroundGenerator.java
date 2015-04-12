package cc.clv.BlueRobe.engine;

import com.badlogic.gdx.maps.tiled.TiledMap;

import java.util.LinkedList;

import cc.clv.BlueRobe.assets.AssetMaster;

public class GroundGenerator {

    private final String stageName;

    private TiledMap stageMap;

    @lombok.Getter
    private Ground ground;

    private int currentBlockIndex = 0;

    public GroundGenerator(String stageName) {
        this.stageName = stageName;
    }

    private Ground initialGround() {
        LinkedList<GroundBlock> blocks = new LinkedList();
        for (int i = 0; i < Ground.NUM_BLOCKS; i++) {
            blocks.add(createNewBlock());
        }

        return new Ground(blocks);
    }

    public void start() {
        this.stageMap = AssetMaster.getStageLoader().get("debug");
        this.ground = initialGround();
    }

    private GroundBlock createNewBlock() {
        GroundBlock.Type type = GroundBlock.Type
                .valueOf(stageMap.getProperties().get("baseGroundType").toString());

        GroundBlock block = GroundBlock.builder()
                .index(currentBlockIndex)
                .type(type)
                .build();
        currentBlockIndex++;

        return block;
    }

    public void next() {
        GroundBlock nextBlock = createNewBlock();

        if (nextBlock != null) {
            ground.putBlock(nextBlock);
        }
    }
}
