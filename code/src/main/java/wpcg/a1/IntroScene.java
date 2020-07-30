/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.a1;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.shader.VarType;
import wpcg.base.Scene;
import wpcg.base.*;
import wpcg.base.animatedmesh.AnimatedMesh;
import wpcg.base.animatedmesh.AnimationControllerFollow;
import wpcg.base.animatedmesh.AnimationControllerPath;
import wpcg.base.mesh.ObjReader;
import wpcg.base.mesh.TriangleMesh;
import wpcg.base.mesh.TriangleMeshTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This introduction scene contains the content of the first exercise: a knight that walks on a squared plane.
 */
public class IntroScene extends Scene {

    /**
     * The asset manager is used to read content (e.g. triangle meshes or texture) from file to jMonkey.
     */
    private AssetManager assetManager;

    /**
     * This is the root node of the scene graph with all the scene content.
     */
    private Node rootNode;

    /**
     * This node contains the knight.
     */
    private Node knightNode;

    /**
     * This node contains the house.
     */
    private Node houseNode;

    /**
     * This node contains the knight.
     */
    private Node stalkerKnightNode;

    /**
     * A list of all animated entities
     */
    protected List<AnimatedMesh> animatedMeshList;

    public IntroScene() {
        assetManager = null;
        rootNode = null;
        knightNode = null;
        stalkerKnightNode = null;
        houseNode = null;
        animatedMeshList = new ArrayList<>();
    }

    @Override
    public void init(AssetManager assetManager, Node rootNode, CameraController cameraController) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        cameraController.setup(new Vector3f(-10, 10, -10),
                new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));

        // Load animated knight
        knightNode = loadCharacter(assetManager, rootNode, "Models/knight.gltf");
        knightNode.setLocalScale(0.01f);

        AnimationControllerPath knightAnimationController = new AnimationControllerPath(Arrays.asList(
                new Vector3f(6, 0, -6),
                new Vector3f(6, 0, -3.5f),
                new Vector3f(-3.5f, 0, 6),
                new Vector3f(-6f, 0, 6f),
                new Vector3f(-6f, 0, -6f)), 1.2f);
        animatedMeshList.add(new AnimatedMesh(knightNode, knightAnimationController));

        //Load Stalker knight
        stalkerKnightNode = loadCharacter(assetManager, rootNode, "Models/knight.gltf");
        stalkerKnightNode.setLocalScale(0.01f);

        AnimationControllerFollow stalkerKnightAnimationController = new AnimationControllerFollow(knightNode);
        animatedMeshList.add(new AnimatedMesh(stalkerKnightNode, stalkerKnightAnimationController));

        // Add a square to the scene graph
        createGround();
        createHouse();
    }

    @Override
    public void update(float time) {
        // Update position
        for (AnimatedMesh animatedMesh : animatedMeshList) {
            animatedMesh.update(time);
        }
    }

    @Override
    public void render() {
    }

    /**
     * Generate a ground mesh.
     */
    private void createGround() {
        float extend = 15;
        Box box = new Box(new Vector3f(-extend / 2, -0.05f, -extend / 2),
                new Vector3f(extend / 2, 0, extend / 2));
        Geometry quadGeometry = new Geometry("Ground", box);
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setColor("Diffuse", new ColorRGBA(0.3f, 0.15f, 0.05f, 1));
        mat.setParam("UseMaterialColors", VarType.Boolean, true);
        quadGeometry.setMaterial(mat);
        rootNode.attachChild(quadGeometry);
        quadGeometry.setShadowMode(RenderQueue.ShadowMode.Receive);
    }

    private void createHouse()
    {
        ObjReader reader = new ObjReader();
        TriangleMesh tMesh = reader.read("Models/medieval_house.obj");
        Mesh mesh = TriangleMeshTools.convertTriangleMeshToMesh(tMesh);
        Geometry geom = new Geometry("medieval_house", mesh);
        geom.move(4, 0, 4);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setBoolean("VertexColor", true);
        geom.setMaterial(mat);

        rootNode.attachChild(geom);
        geom.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
    }

    protected Node getKnightNode() {
        return knightNode;
    }
}
