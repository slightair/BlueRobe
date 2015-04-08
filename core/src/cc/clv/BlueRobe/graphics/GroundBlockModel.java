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

    public static final float WIDTH = 176f;
    public static final float HEIGHT = 32f;
    public static final float DEPTH = 160f;

    private final GroundBlock.Type type;

    public GroundBlockModel(GroundBlock.Type type) {
        this.type = type;

        construct();
    }

    private void construct() {
        Color color;
        switch (type) {
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

        meshBuilder.box(WIDTH, HEIGHT, DEPTH);

        meshBuilder.end();

        nodes.add(node);
    }
}
