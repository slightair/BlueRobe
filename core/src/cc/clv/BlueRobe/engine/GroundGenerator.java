package cc.clv.BlueRobe.engine;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.ArrayList;
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
                .valueOf(stageMap.getProperties().get("baseGroundType", String.class));

        TiledMapTileLayer itemsLayer = (TiledMapTileLayer) stageMap.getLayers().get("items");

        ArrayList<Item> items = new ArrayList<Item>();
        int firstLine = currentBlockIndex * GroundBlock.NUM_VERTICAL_CELLS % itemsLayer.getHeight();
        for (int y = 0; y < GroundBlock.NUM_VERTICAL_CELLS; y++) {
            for (int x = 0; x < itemsLayer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = itemsLayer.getCell(x, firstLine + y);

                if (cell != null) {
                    String itemName = cell.getTile().getProperties().get("name", String.class);
                    Item item = new Item(itemName, x, y);
                    items.add(item);
                }
            }
        }

        GroundBlock block = GroundBlock.builder()
                .index(currentBlockIndex)
                .type(type)
                .items(items)
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
