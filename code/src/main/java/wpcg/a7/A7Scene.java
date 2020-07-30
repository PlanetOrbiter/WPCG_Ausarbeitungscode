package wpcg.a7;

import com.jme3.asset.AssetManager;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.shader.VarType;
import wpcg.base.CameraController;
import wpcg.base.Scene;
import wpcg.base.mesh.ObjReader;
import wpcg.base.mesh.TriangleMesh;
import wpcg.base.mesh.TriangleMeshTools;

import java.util.ArrayList;
import java.util.List;

public class A7Scene extends Scene
{
    private Node rootNode;
    private AssetManager assetManager;
    private CameraController cameraController;
    Grammar grammar;
    private float extend = 50;


    @Override
    public void init(AssetManager assetManager, Node rootNode, CameraController cameraController)
    {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.cameraController = cameraController;

        grammar = new Grammar(222);

        PointLight sun = new PointLight(new Vector3f(0, 7.5f, 0));
        sun.setRadius(10);


        for (int i = -(int)extend / 2; i <= (int)extend / 2; i += 2)
        {
            buildHouse(3, i, false);
            buildHouse(3, i, true);
        }

        createGround();
        createStreet();
        placeSign((-extend*2.5f), 8, true);
        placeSign((extend*2.5f), -8, true);
        placeSign((extend*2.5f), 8, true);
        placeSign((-extend*2.5f), -8, true);
    }

    @Override
    public void update(float time)
    {

    }

    @Override
    public void render()
    {

    }

    private void buildHouse(float x, float z, boolean flip)
    {
        List<String> result = grammar.derive("Building");

        List<BuildingBlocks> blocks = new ArrayList<>();
        result.forEach(s -> blocks.add(BuildingBlocks.valueOf(s)));

        ObjReader reader = new ObjReader();
        float y = 0;
        for (int i = 0; i < result.size(); i++)
        {
            String cur = result.get(i).toLowerCase();
            TriangleMesh tMesh = reader.read("Models/pcg/" + cur + ".obj");
            TriangleMeshTools.translate(tMesh, x, y, z);
            y += tMesh.getBoundingBox().getYExtent();

            Mesh mesh = TriangleMeshTools.convertTriangleMeshToMesh(tMesh);
            Geometry geom = new Geometry(cur, mesh);
            if(flip)
                geom.rotate(0, (float)Math.toRadians(180), 0);
            Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setBoolean("VertexColor", true);
            geom.setMaterial(mat);

            rootNode.attachChild(geom);
            geom.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        }
    }

    private void createGround()
    {
        Box box = new Box(new Vector3f(-extend / 6, -0.05f, -extend / 2),
                new Vector3f(extend / 6, 0, extend / 2));
        Geometry quadGeometry = new Geometry("Ground", box);
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setColor("Diffuse", ColorRGBA.Green);
        mat.setParam("UseMaterialColors", VarType.Boolean, true);
        quadGeometry.setMaterial(mat);
        rootNode.attachChild(quadGeometry);
        quadGeometry.setShadowMode(RenderQueue.ShadowMode.Receive);
    }

    private void createStreet()
    {
        Box box = new Box(new Vector3f(-1.5f, 0f, -extend / 2),
                new Vector3f(1.5f, 0.001f, extend / 2));
        Geometry quadGeometry = new Geometry("Ground", box);
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setColor("Diffuse", ColorRGBA.Gray);
        mat.setParam("UseMaterialColors", VarType.Boolean, true);
        quadGeometry.setMaterial(mat);
        rootNode.attachChild(quadGeometry);
        quadGeometry.setShadowMode(RenderQueue.ShadowMode.Receive);

        for (float i = -extend / 2; i < extend / 2; i += 2)
        {
            Box white = new Box(new Vector3f(-0.1f, 0f, i),
                    new Vector3f(0.1f, 0.002f, i + 0.8f));
            Geometry whiteGeometry = new Geometry("Ground", white);
            Material whiteMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            whiteMat.setColor("Color", ColorRGBA.White);
            whiteGeometry.setMaterial(whiteMat);
            rootNode.attachChild(whiteGeometry);
            whiteGeometry.setShadowMode(RenderQueue.ShadowMode.Receive);
        }
    }

    private void placeSign(float x, float z, boolean flip)
    {
        ObjReader reader = new ObjReader();

        TriangleMesh tMesh = reader.read("Models/pcg/ortsschildA7.obj");
        TriangleMeshTools.translate(tMesh, x, 0, z);

        Mesh mesh = TriangleMeshTools.convertTriangleMeshToMesh(tMesh);
        Geometry geom = new Geometry("ortsschildA7", mesh);
        geom.setLocalScale(0.2f);
        if(flip)
            geom.rotate(0, (float)Math.toRadians(90), 0);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setBoolean("VertexColor", true);
        geom.setMaterial(mat);

        rootNode.attachChild(geom);
        geom.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

    }
}
