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

/**
 * Created by slightair on 15/02/22.
 */
public class GroundBlockModel extends Model {

    public static final float SIZE = 16f;
    public static final float HEIGHT = 32f;
    private final GroundBlock.Type blockType;

    public GroundBlockModel(GroundBlock.Type blockType) {
        this.blockType = blockType;

        createBlockModel();
    }

    private void createBlockModel() {
        createDebugBlockModel();
    }

    private void createDebugBlockModel() {
        Color color;
        switch (blockType) {
            case DebugWhite:
                color = Color.WHITE;
                break;
            case DebugGray:
                color = Color.LIGHT_GRAY;
                break;
            default:
                color = Color.DARK_GRAY;
        }

        Material material = new Material(ColorAttribute.createDiffuse(color));

        MeshBuilder meshBuilder = new MeshBuilder();
        meshBuilder.begin(VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        Node node = new Node();
        node.id = "block";
        node.parts.add(new NodePart(meshBuilder.part("base", GL20.GL_TRIANGLES), material));

        meshBuilder.box(SIZE, HEIGHT, SIZE);

        meshBuilder.end();

        nodes.add(node);
    }
}
