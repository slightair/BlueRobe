package cc.clv.BlueRobe.engine;

import com.badlogic.gdx.maps.MapLayers;
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

        MapLayers layers = stageMap.getLayers();
        TiledMapTileLayer objectsLayer = (TiledMapTileLayer) layers.get("objects");
        TiledMapTileLayer itemsLayer = (TiledMapTileLayer) layers.get("items");
        TiledMapTileLayer obstaclesLayer = (TiledMapTileLayer) layers.get("obstacles");

        ArrayList<Object> objects = new ArrayList<Object>();
        ArrayList<Object> items = new ArrayList<Object>();
        ArrayList<Object> obstacles = new ArrayList<Object>();
        int firstLine = currentBlockIndex * GroundBlock.NUM_VERTICAL_CELLS % itemsLayer.getHeight();
        for (int y = 0; y < GroundBlock.NUM_VERTICAL_CELLS; y++) {
            for (int x = 0; x < itemsLayer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell;

                cell = objectsLayer.getCell(x, firstLine + y);
                if (cell != null) {
                    String itemName = cell.getTile().getProperties().get("name", String.class);
                    Object item = new Object(itemName, x, y);
                    objects.add(item);
                }

                cell = itemsLayer.getCell(x, firstLine + y);
                if (cell != null) {
                    String itemName = cell.getTile().getProperties().get("name", String.class);
                    Object item = new Object(itemName, x, y);
                    items.add(item);
                }

                cell = obstaclesLayer.getCell(x, firstLine + y);
                if (cell != null) {
                    String itemName = cell.getTile().getProperties().get("name", String.class);
                    Object item = new Object(itemName, x, y);
                    obstacles.add(item);
                }
            }
        }

        GroundBlock block = GroundBlock.builder()
                .index(currentBlockIndex)
                .type(type)
                .objects(objects)
                .items(items)
                .obstacles(obstacles)
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
