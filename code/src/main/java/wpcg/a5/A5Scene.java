/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.a5;

import com.jme3.asset.AssetManager;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.shader.VarType;
import wpcg.a5.kdtree.KDTreeBuilder;
import wpcg.a5.kdtree.KDTreeData;
import wpcg.a5.kdtree.KDTreeNode;
import wpcg.a5.kdtree.NearestNeighborSearch;
import wpcg.base.CameraController;
import wpcg.base.Scene;

import java.util.ArrayList;
import java.util.List;

/**
 * Base scene for the exercise 4.
 */
public class A5Scene extends Scene {

    /**
     * Root node of the scene graph.
     */
    protected Node rootNode;

    /**
     * The root node of the KD tree of the data points in the scene.
     */
    protected KDTreeNode<Geometry> kdTree;

    /**
     * Camera controller.
     */
    protected CameraController cameraController;

    /**
     * Referende to the asset maneger.
     */
    private AssetManager assetManager;

    /**
     * List of all data points in the scene.
     */
    protected List<KDTreeData<Geometry>> points;

    /**
     * Sample application: this point moved thru the scene and finds its current closest data point.
     */
    protected Geometry movingPoint;

    /**
     * The sample point movingPoint moves into an arbitrary direction encoded by this angle.
     */
    protected float movingPointAngle;

    /**
     * This cache object helps to avoid creating of new objects
     */
    private Quaternion cacheQuaternion;

    /**
     * The nearest neigbor search
     */
    protected NearestNeighborSearch<Geometry> nearestNeighborSearch;

    /**
     * Current closest data point to the movingPoint.
     */
    private Geometry closestNode;

    /**
     * Color preset: regular data point.
     */
    private static ColorRGBA DEFAULT_POINT_COLOR = new ColorRGBA(1f, 0f, 0f, 1);

    /**
     * Color preset: current closest data point.
     */
    private static ColorRGBA CLOSEST_POINT_COLOR = new ColorRGBA(0f, 1f, 0f, 1);

    /**
     * Color preset: moving point.
     */
    private static ColorRGBA MOVING_NODE_COLOR = new ColorRGBA(0f, 0f, 1f, 1);

    /**
     * Number of generated data points.
     */
//    private static int NUM_POINTS = 10000;
    private static int NUM_POINTS = 100;

    public A5Scene() {
        this.cameraController = null;
        this.rootNode = null;
        this.kdTree = null;
        this.assetManager = null;
        this.points = null;
        this.movingPoint = null;
        this.movingPointAngle = 0;
        this.cacheQuaternion = new Quaternion();
        this.closestNode = null;

    }

    @Override
    public void init(AssetManager assetManager, Node rootNode, CameraController cameraController) {
        cameraController.setup(new Vector3f(10f, 10f, 10f),
                new Vector3f(5f, 0, 5f), new Vector3f(0, 1, 0));
        this.rootNode = rootNode;
        this.cameraController = cameraController;
        this.assetManager = assetManager;

        // Generate test data
        points = makeRandomPoints(NUM_POINTS);

        // TODO: Build kd-tree
        kdTree = KDTreeBuilder.buildKDTree(points);

        // Setup nearest neighbor search
        this.nearestNeighborSearch = new NearestNeighborSearch(kdTree, points);


        //For testing
//        final int TEST_SIZE = 100000;
//        List<KDTreeData<Geometry>> find = makeRandomPoints(TEST_SIZE / 10);
//        KDTreeNode<Geometry> findKdTree = KDTreeBuilder.buildKDTree(find);
//        NearestNeighborSearch<Geometry> nns = new NearestNeighborSearch(findKdTree, find);
//        int inconsistentMatches = 0;
//        long naiveTime = 0;
//        long notNaiveTime = 0;
//        for (int i = 0; i < find.size(); i++)
//        {
//            long start = System.nanoTime();
//            KDTreeData<Geometry> naive = nns.getNearestNeighborNaive(find.get(i).getP());
//            long end = System.nanoTime();
//            naiveTime += (end - start);
//
//            start = System.nanoTime();
//            KDTreeData<Geometry> notNaive = nns.getNearestNeighbor(find.get(i).getP());
//            end = System.nanoTime();
//            notNaiveTime += (end - start);
//
//            if(!naive.getP().equals(notNaive.getP()))
//                inconsistentMatches++;
//        }
//        naiveTime /= find.size();
//        notNaiveTime /= find.size();
//        System.out.println("Test conditions:\n Number of Element: " + TEST_SIZE + " Number of tests: " + find.size());
//        System.out.println("Naive: " + naiveTime);
//        System.out.println("Not Naive: " + notNaiveTime);
//        System.out.println("Not Naive %: " + ((float)((float)notNaiveTime / (float)naiveTime)) * 100.0f);
//        System.out.println("Inconsistency: " + (inconsistentMatches / find.size()) * 100);

        // Draw hyperplanes (do not call with more than 100 data points)
        addHyperplanesToSceneGraph(kdTree);

        // Draw a point which moves through the scene
        addMovingPointToSceneGraph();

    }

    /**
     * Generate the moving point and add its geometry to the scene graph
     */
    private void addMovingPointToSceneGraph() {
        Sphere sphere = new Sphere(10, 10, getSphereRadius());
        movingPoint = new Geometry("Ground", sphere);
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setColor("Diffuse", MOVING_NODE_COLOR);
        mat.setParam("UseMaterialColors", VarType.Boolean, true);
        movingPoint.setMaterial(mat);
        movingPoint.move(5f, 0, 5f);
        rootNode.attachChild(movingPoint);
    }

    /**
     * Compute a fitting radius for the spheres based on their number
     */
    private float getSphereRadius() {
        return 1.25f / (float) Math.sqrt(NUM_POINTS);
    }

    /**
     * Generate a list of random data points in [0,10]^2
     */
    protected List<KDTreeData<Geometry>> makeRandomPoints(int n) {
        List<KDTreeData<Geometry>> points = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Vector3f pos = new Vector3f((float) Math.random() * 10.0f, 0, (float) Math.random() * 10.0f);
            Sphere sphere = new Sphere(10, 10, getSphereRadius());
            Geometry geometry = new Geometry("Ground", sphere);
            geometry.move(pos);
            Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            mat.setColor("Diffuse", DEFAULT_POINT_COLOR);
            mat.setParam("UseMaterialColors", VarType.Boolean, true);
            geometry.setMaterial(mat);
            rootNode.attachChild(geometry);
            points.add(new KDTreeData<Geometry>(new Vector2f(pos.x, pos.z), geometry));
        }
        return points;
    }

    /**
     * Generate a hyperplane for a kd-tree node (recursively) and add it to the scene graph (uses transparency)
     */
    protected void addHyperplanesToSceneGraph(KDTreeNode<Geometry> kdTree) {
        if (kdTree == null) {
            return;
        }
        Box box = null;
        float width = 0.02f;
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");

        mat.setParam("UseMaterialColors", VarType.Boolean, true);
        float alpha = 0.5f;
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        if (kdTree.getSplittingDirection() == KDTreeNode.SplitDirection.X) {
            float x = kdTree.getData().getP().x;
            box = new Box(new Vector3f(x - width, -1f, kdTree.getLL().y),
                    new Vector3f(x + width, 1f, kdTree.getUR().y));
            mat.setColor("Diffuse", new ColorRGBA(0f, 1f, 0f, alpha));
        } else {
            float y = kdTree.getData().getP().y;
            box = new Box(new Vector3f(kdTree.getLL().x, -1f, y - width),
                    new Vector3f(kdTree.getUR().x, 1f, y + width));
            mat.setColor("Diffuse", new ColorRGBA(0f, 0f, 1f, alpha));
        }
        Geometry quadGeometry = new Geometry("Ground", box);
        quadGeometry.setMaterial(mat);
        rootNode.attachChild(quadGeometry);

        // Recursive call
        addHyperplanesToSceneGraph(kdTree.getNegChild());
        addHyperplanesToSceneGraph(kdTree.getPosChild());
    }

    @Override
    public void update(float time) {
        // Update the moving point
        cacheQuaternion.fromAngleAxis(movingPointAngle, Vector3f.UNIT_Y);
        Vector3f dir = cacheQuaternion.toRotationMatrix().mult(Vector3f.UNIT_X);
        float speed = 10.0f / (float) Math.sqrt(NUM_POINTS);
        movingPoint.move(dir.mult(time * speed));
        float x = movingPoint.getLocalTranslation().x;
        float z = movingPoint.getLocalTranslation().z;
        if (x < 0 || z < 0 || x > 10 || z > 10) {
            movingPointAngle += (float) Math.PI;
            movingPoint.setLocalTranslation(Math.max(0, Math.min(10, x)), 0, Math.max(0, Math.min(10, z)));
        }

        // Update the nearest node
        Vector3f pos3D = movingPoint.getLocalTranslation();
//        KDTreeData<Geometry> n = nearestNeighborSearch.getNearestNeighborNaive(new Vector2f(pos3D.x, pos3D.z));
        KDTreeData<Geometry> n = nearestNeighborSearch.getNearestNeighbor(new Vector2f(pos3D.x, pos3D.z));
        if (closestNode != null) {
            closestNode.getMaterial().setColor("Diffuse", DEFAULT_POINT_COLOR);
        }
        if (n != null) {
            closestNode = n.getData();
            closestNode.getMaterial().setColor("Diffuse", CLOSEST_POINT_COLOR);
        }
    }

    @Override
    public void render() {
        movingPointAngle += (Math.random() - 0.5f) * 0.2f;
        cameraController.lookAt(movingPoint.getLocalTranslation(), Vector3f.UNIT_Y);
    }

    @Override
    public void setupLights(AssetManager assetManager, Node rootNode, ViewPort viewPort) {
        // Scene
        PointLight light1 = new PointLight(new Vector3f(10, 10, 10));
        light1.setColor(new ColorRGBA(1f, 1f, 1f, 1));
        light1.setRadius(40);
        rootNode.addLight(light1);
    }
}
