package cc.clv.BlueRobe.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;

import cc.clv.BlueRobe.engine.GroundBlock;

public class GroundBlockModel extends Model {

    public static final float UNIT = 16f;
    public static final float WIDTH = UNIT * 11;
    public static final float HEIGHT = UNIT * 2;
    public static final float DEPTH = UNIT * 10;

    private final GroundBlock.Type type;

    public GroundBlockModel(GroundBlock.Type type) {
        this.type = type;

        switch (type) {
            case DebugWhite:
                createDebugBlock(Color.WHITE);
                break;
            case DebugGray:
                createDebugBlock(Color.LIGHT_GRAY);
                break;
            case Grass:
                createGrassBlock();
                break;
            default:
                createDebugBlock(Color.DARK_GRAY);
        }
    }

    private void createDebugBlock(Color color) {
        Material material = new Material(ColorAttribute.createDiffuse(color));

        MeshBuilder meshBuilder = new MeshBuilder();
        meshBuilder.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        Node node = new Node();
        node.id = "block";
        node.parts.add(new NodePart(meshBuilder.part("base", GL20.GL_TRIANGLES), material));

        meshBuilder.box(WIDTH, HEIGHT, DEPTH);

        meshBuilder.end();

        nodes.add(node);
    }

    private void createGrassBlock() {
        Color color1 = new Color(0x99ff70ff);
        Color color2 = new Color(0xaaff80ff);

        Material material1 = new Material(ColorAttribute.createDiffuse(color1));
        Material material2 = new Material(ColorAttribute.createDiffuse(color2));

        MeshBuilder meshBuilder = new MeshBuilder();
        float zBase = -DEPTH / 2 + UNIT / 2;
        for (int i = 0; i < 10; i++) {
            Material material = i % 2 == 0 ? material1 : material2;

            meshBuilder.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

            Node node = new Node();
            node.id = "line-" + i;
            node.parts.add(new NodePart(meshBuilder.part("base", GL20.GL_TRIANGLES), material));

            meshBuilder.box(0, 0, zBase + UNIT * i, WIDTH, HEIGHT, UNIT);

            meshBuilder.end();

            nodes.add(node);
        }
    }
}
