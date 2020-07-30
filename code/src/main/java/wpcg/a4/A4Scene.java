/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.a4;

import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.PointLightShadowFilter;
import com.jme3.shadow.PointLightShadowRenderer;
import com.jme3.texture.Texture;
import wpcg.a6.AnimationControllerSmoothPath;
import wpcg.base.mesh.ObjReader;
import wpcg.base.mesh.TriangleMesh;
import wpcg.a4.level.Cell;
import wpcg.a4.level.Level;
import wpcg.a4.level.Direction;
import wpcg.base.animatedmesh.AnimatedMesh;
import wpcg.base.animatedmesh.AnimationControllerPath;
import wpcg.base.CameraController;
import wpcg.base.Scene;
import wpcg.base.mesh.TriangleMeshTools;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Base scene for the exercise 4.
 */
public class A4Scene extends Scene {

    /**
     * Level object
     */
    private Level level;

    /**
     * Torch ligh source
     */
    PointLight torchLight;

    /**
     * Knight node
     */
    private Node knightNode;

    /**
     * The animation node of the knight.
     */
    protected AnimatedMesh animatedMesh;

    /**
     * Root node of the scene graph.
     */
    protected Node rootNode;

    /**
     * Camera controller.
     */
    protected CameraController cameraController;
    private Geometry sphereGeom;
    private DirectionalLight sun;

    public A4Scene() {
        this.level = new Level();
        this.cameraController = null;
        this.animatedMesh = null;
        this.level.readLevelFromFile("src/main/resources/levels/level01.json");
    }

    protected List<Vector3f> getWayPoints() {
        // Set path for the knight (list of cell centers)
        return Arrays.asList(
                to3D(level.getCell(0, 1).getCenter()),
                to3D(level.getCell(1, 0).getCenter()),
                to3D(level.getCell(2, 0).getCenter()),
                to3D(level.getCell(3, 0).getCenter()),
                to3D(level.getCell(3, 1).getCenter()),
                to3D(level.getCell(2, 1).getCenter()),
                to3D(level.getCell(2, 2).getCenter()),
                to3D(level.getCell(3, 2).getCenter()),
                to3D(level.getCell(2, 3).getCenter()),
                to3D(level.getCell(1, 3).getCenter()),
                to3D(level.getCell(0, 3).getCenter()),
                to3D(level.getCell(0, 2).getCenter())
        );
    }

    @Override
    public void init(AssetManager assetManager, Node rootNode, CameraController cameraController) {
        this.rootNode = rootNode;
        this.cameraController = cameraController;
        Vector3f levelCenter = level.getCenter();
        cameraController.setup(new Vector3f(10, 10, 10),
                levelCenter, new Vector3f(0, 1, 0));


        // Setup animated knight mesh.
        knightNode = loadCharacter(assetManager, rootNode, "Models/knight.gltf");
        knightNode.setShadowMode(RenderQueue.ShadowMode.Cast);
        knightNode.setLocalScale(0.003f);
//        AnimationControllerPath knightAnimationController = new AnimationControllerPath(getWayPoints(), 0.5f);
        AnimationControllerSmoothPath knightAnimationController = new AnimationControllerSmoothPath(getWayPoints(), 0.035f);
        animatedMesh = new AnimatedMesh(knightNode, knightAnimationController);


        // Render cells
        for (Iterator<Cell> it = level.getCellIterator(); it.hasNext(); ) {
            Cell cell = it.next();
            addCellGeometry(assetManager, "Textures/stone.png",
                    "Textures/stone_normalmap.png", cell);

            // Render walls
            for (Direction dir : Direction.values()) {
                if (cell.isWall(dir)) {
                    if (cell.getNeighborCell(dir) != null
                            && (dir == Direction.UHR_0 || dir == Direction.UHR_2 || dir == Direction.UHR_4)) {
                        // Create wall only once.
                        continue;
                    }
                    addWallGeometry(assetManager, "Textures/stone.png",
                            "Textures/stone_normalmap.png",
                            cell, dir);
                }
            }
        }
    }

    @Override
    public void update(float time)
    {
        animatedMesh.update(time);
        Vector3f forward;
        forward = knightNode.getWorldRotation().getRotationColumn(2).normalize().mult(0.25f);
        sun.setDirection(forward.negate().add(0, -0.2f, 0)); // trick 17; von oben nach unten gucken

        sphereGeom.setLocalTranslation(knightNode.getLocalTranslation().add(0, 0.75f, 0f).add(forward));
        torchLight.setPosition(sphereGeom.getLocalTranslation());
    }

    @Override
    public void render() {
    }

    @Override
    public void setupLights(AssetManager assetManager, Node rootNode, ViewPort viewPort) {
        super.setupLights(assetManager, rootNode, viewPort);

        torchLight = new PointLight();
        torchLight.setColor(ColorRGBA.Green);
        torchLight.setRadius(4f);
        rootNode.addLight(torchLight);


        // Unnötige Spielerei
        Sphere sphere = new Sphere(16, 16, .075f);
        sphereGeom = new Geometry("mageLight", sphere);
        Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
//        material.setColor("Diffuse", new ColorRGBA(0, 0.7f, 0.8f, 0.3f));
        material.setColor("Diffuse", new ColorRGBA(0, 0.8f, 0, .5f));
        material.setBoolean("UseMaterialColors",true);
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        sphereGeom.setQueueBucket(RenderQueue.Bucket.Transparent);
        sphereGeom.setMaterial(material);
        sphereGeom.move(0, 2, 0);
        rootNode.attachChild(sphereGeom);


        sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(viewPort.getCamera().getDirection());

        final int SHADOWMAP_SIZE=1024;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
//        PointLightShadowRenderer dlsr = new PointLightShadowRenderer(assetManager, SHADOWMAP_SIZE); // Test mit Point light shadow zeug
        dlsr.setLight(sun);
        viewPort.addProcessor(dlsr);

        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
//        PointLightShadowFilter dlsf = new PointLightShadowFilter(assetManager, SHADOWMAP_SIZE);
        dlsf.setLight(sun);
        dlsf.setEnabled(true);
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(dlsf);
        viewPort.addProcessor(fpp);
    }

    /**
     * Convert a 2D vector to a 3D vector.
     */
    public Vector3f to3D(Vector2f p) {
        return new Vector3f(p.x, 0, p.y);
    }

    /**
     * Create and add the geometry for a hexagon cell ground.
     */
    protected void addCellGeometry(AssetManager assetManager, String textureFilename,
                                   String normalMapFilename, Cell cell) {
        TriangleMesh mesh = new ObjReader().read("Models/hexagon.obj");
        Mesh cellMesh = TriangleMeshTools.convertTriangleMeshToMesh(mesh);
        Geometry geom = new Geometry("Cell", cellMesh);

        Vector3f cellCenter = to3D(cell.getCenter());

//        Spatial safeMesh = assetManager.loadModel("Models/hexagon.obj");

        Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        Texture texture = assetManager.loadTexture(textureFilename);
        Texture textureNormal = assetManager.loadTexture(normalMapFilename);
        texture.setMagFilter(Texture.MagFilter.Bilinear);
        texture.setMinFilter(Texture.MinFilter.BilinearNearestMipMap);
        material.setTexture("DiffuseMap", texture);
        material.setTexture("NormalMap", textureNormal);

//        safeMesh.setMaterial(material);
//        safeMesh.setLocalTranslation(cellCenter);
//        safeMesh.setShadowMode(RenderQueue.ShadowMode.Receive);

        geom.move(cellCenter);
        geom.setMaterial(material);
        geom.setShadowMode(RenderQueue.ShadowMode.Receive);

        rootNode.attachChild(geom);

        // TODO: Create jmonkey triangle mesh, set texture and normal map texture, move to cell center,
        //  add to root node
        //  Roger!
    }

    /**
     * Create and add the geometry for a cell wall in a given direction.
     */
    protected void addWallGeometry(AssetManager assetManager, String textureFilename, String normalMapFilename,
                                   Cell cell, Direction dir) {
        Vector2f orientation = dir.getOrientation();
        Vector2f wallCenter = cell.getCenter().add(orientation.mult(Cell.getZellenhoehe()));
        TriangleMesh mesh = new ObjReader().read("Models/hex_wall.obj");

        Mesh wallMesh = TriangleMeshTools.convertTriangleMeshToMesh(mesh);
        Geometry geom = new Geometry("Wall", wallMesh);


//        Spatial safeMesh = assetManager.loadModel("Models/hex_wall.obj");

        Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        Texture texture = assetManager.loadTexture(textureFilename);
        Texture textureNormal = assetManager.loadTexture(normalMapFilename);
        texture.setMagFilter(Texture.MagFilter.Bilinear);
        texture.setMinFilter(Texture.MinFilter.BilinearNearestMipMap);
        material.setTexture("DiffuseMap", texture);
        material.setTexture("NormalMap", textureNormal);

//        safeMesh.setMaterial(material);
//        safeMesh.setLocalTranslation(to3D(wallCenter));
        float angle = -Vector3f.UNIT_Z.angleBetween(to3D(orientation).normalize());

        if(dir == Direction.UHR_4 || dir == Direction.UHR_2) // TODO warum ist das so?
        {
            angle *= -1;
        }

//        safeMesh.rotate(0, angle, 0);
//        safeMesh.setShadowMode(RenderQueue.ShadowMode.Receive);

        geom.move(to3D(wallCenter));
        geom.setMaterial(material);
        geom.rotate(0, angle, 0);
        geom.setShadowMode(RenderQueue.ShadowMode.Receive);

        rootNode.attachChild(geom);

        // TODO: Create jmonkey triangle mesh, set texture and normal map texture, move to wall center, rotate wall,
        //  add to root node
    }
}
