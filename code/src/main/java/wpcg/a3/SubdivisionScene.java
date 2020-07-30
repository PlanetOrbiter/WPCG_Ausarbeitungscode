package wpcg.a3;

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
import wpcg.a3.halfedge.HalfEdgeTriangleMesh;
import wpcg.base.CameraController;
import wpcg.base.Scene;
import wpcg.base.mesh.ObjReader;
import wpcg.base.mesh.TriangleMesh;
import wpcg.base.mesh.TriangleMeshTools;

public class SubdivisionScene extends Scene
{
    /**
     * The asset manager is used to read content (e.g. triangle meshes or texture) from file to jMonkey.
     */
    private AssetManager assetManager;

    /**
     * This is the root node of the scene graph with all the scene content.
     */
    private Node rootNode;

    public SubdivisionScene()
    {
        rootNode = null;
        assetManager = null;
    }

    @Override
    public void init(AssetManager assetManager, Node rootNode, CameraController cameraController)
    {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        cameraController.setup(new Vector3f(0, 5, 5), new Vector3f(0, 2.5f, 0), new Vector3f(0, 1, 0));

        ObjReader reader = new ObjReader();
        TriangleMesh tMesh = reader.read("Models/cube.obj");
//        TriangleMesh tMesh = reader.read("Models/subdiv_object.obj");
//        TriangleMesh tMesh = reader.read("Models/suzanne.obj");

        HalfEdgeTriangleMesh hMesh = HalfEdgeTriangleMesh.from(tMesh);
        System.out.println(hMesh.getNumberOfVertices());
        Subdivision.subdivide(hMesh ,1);
        System.out.println(hMesh.getNumberOfVertices());

        Mesh mesh = TriangleMeshTools.convertTriangleMeshToMesh(hMesh.toMesh());
        Geometry geom = new Geometry("Test", mesh);
        geom.scale(2.5f, 2.5f, 2.5f);
        geom.move(0, 2.5f, 0);
        Material mat = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setColor("Diffuse", new ColorRGBA(1f, 1f, 1f, 0f));
        mat.setParam("UseMaterialColors", VarType.Boolean, true);
        geom.setMaterial(mat);
        geom.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        createGround();
        this.rootNode.attachChild(geom);
    }

    @Override
    public void update(float time)
    {

    }

    @Override
    public void render()
    {

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
}
