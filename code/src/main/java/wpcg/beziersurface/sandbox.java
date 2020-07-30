package wpcg.beziersurface;

import com.jme3.asset.AssetManager;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.shader.VarType;
import wpcg.a3.halfedge.HalfEdgeTriangleMesh;
import wpcg.a7.BuildingBlocks;
import wpcg.a7.Grammar;
import wpcg.base.CameraController;
import wpcg.base.Scene;
import wpcg.base.mesh.ObjReader;
import wpcg.base.mesh.Triangle;
import wpcg.base.mesh.TriangleMesh;
import wpcg.base.mesh.TriangleMeshTools;

import java.util.ArrayList;
import java.util.List;

public class sandbox extends Scene
{
    private BezierSurface surface;
    private Vector3f p00;
    private Vector3f p10;
    private Vector3f p20;
    private Vector3f p30;

    private Vector3f p01;
    private Vector3f p11;
    private Vector3f p21;
    private Vector3f p31;

    private Vector3f p02;
    private Vector3f p12;
    private Vector3f p22;
    private Vector3f p32;

    private Vector3f p03;
    private Vector3f p13;
    private Vector3f p23;
    private Vector3f p33;

    private Node rootNode;
    private AssetManager assetManager;
    private CameraController cameraController;
    private float extend = 50;

    private Geometry sphereGeom;

    public sandbox()
    {
        //Initializing the control points of the bezier surface
        this.p00 = new Vector3f(0.0f, 0.0f, 0.0f);
        this.p10 = new Vector3f(0.33f, 0.0f, 0.0f);
        this.p20 = new Vector3f(0.66f, 0.0f, 0.0f);
        this.p30 = new Vector3f(1.0f, 0.0f, 0.0f);

        this.p01 = new Vector3f(0.0f, 0.0f, 0.33f);
        this.p11 = new Vector3f(0.33f, 1.0f, 0.33f);
        this.p21 = new Vector3f(0.66f, -1.0f, 0.33f);
        this.p31 = new Vector3f(1.0f, 0.0f, 0.33f);

        this.p02 = new Vector3f(0.0f, 0.0f, 0.66f);
        this.p12 = new Vector3f(0.33f, 1.0f, 0.66f);
        this.p22 = new Vector3f(0.66f, -1.0f, 0.66f);
        this.p32 = new Vector3f(1.0f, 0.0f, 0.66f);

        this.p03 = new Vector3f(0.0f, 0.0f, 1.0f);
        this.p13 = new Vector3f(0.33f, 0.0f, 1.0f);
        this.p23 = new Vector3f(0.66f, 0.0f, 1.0f);
        this.p33 = new Vector3f(1.0f, 0.0f, 1.0f);

        surface = new BezierSurface(p00, p10, p20, p30, p01, p11, p21, p31, p02, p12, p22, p32, p03, p13, p23, p33);
    }




    @Override
    public void init(AssetManager assetManager, Node rootNode, CameraController cameraController)
    {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.cameraController = cameraController;


        PointLight sun = new PointLight(new Vector3f(0, 7.5f, 0));
        sun.setRadius(10);


        drawBezierSurfaceWithDots();
        drawControlPoints();
        //createGround();
    }

    @Override
    public void update(float time)
    {

    }

    @Override
    public void render()
    {

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


    private void placeSphere(Vector3f pos)
    {
        Sphere sphere = new Sphere(16, 16, .05f);
        Geometry sphereGeom = new Geometry("mageLight", sphere);
        Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        material.setColor("Diffuse", new ColorRGBA(1, 1, 1, 0.6f));
        material.setBoolean("UseMaterialColors",true);
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        sphereGeom.setQueueBucket(RenderQueue.Bucket.Transparent);
        sphereGeom.setMaterial(material);
        sphereGeom.move(pos);
        rootNode.attachChild(sphereGeom);
    }

    private void placeSphereBlue(Vector3f pos)
    {
        Sphere sphere = new Sphere(16, 16, .05f);
        Geometry sphereGeom = new Geometry("mageLight", sphere);
        Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        material.setColor("Diffuse", new ColorRGBA(0, 0, 1, 1));
        material.setBoolean("UseMaterialColors",true);
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        sphereGeom.setQueueBucket(RenderQueue.Bucket.Transparent);
        sphereGeom.setMaterial(material);
        sphereGeom.move(pos);
        rootNode.attachChild(sphereGeom);
    }

    private void drawBezierSurfaceWithDots()
    {
        List<Vector3f> newPositions = new ArrayList<>();
        HalfEdgeTriangleMesh hmesh = new HalfEdgeTriangleMesh();
        for(int i = 0; i < 11; i++)
        {
            for (int j = 0; j < 11; j++)
            {
                Vector3f resultVec = surface.getCoordinates((float)i / 10, (float)j / 10);
                placeSphere(resultVec); // Showing the individual calculated positions.
                hmesh.addVertex(resultVec);
            }
        }
        hmesh.computeTriangleNormals();
        Mesh mesh = TriangleMeshTools.convertTriangleMeshToMesh(hmesh.toMesh());
        Geometry geom = new Geometry("Test", mesh);
        geom.scale(2.5f, 2.5f, 2.5f);
        geom.move(0, 2.5f, 0);
        Material mat = new Material(this.assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setColor("Diffuse", new ColorRGBA(1f, 1f, 1f, 1f));
        mat.setParam("UseMaterialColors", VarType.Boolean, true);
        geom.setMaterial(mat);
        geom.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        rootNode.attachChild(geom);
    }

    private void drawControlPoints()
    {
        Vector3f[][] cpoints = surface.getControlPoints();
        for(int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                placeSphereBlue(cpoints[i][j]);
            }
        }
    }

}
