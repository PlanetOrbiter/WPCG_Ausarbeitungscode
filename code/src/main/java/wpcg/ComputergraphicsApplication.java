/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.TouchInput;
import com.jme3.input.controls.*;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import wpcg.a1.IntroScene;
import wpcg.a4.A4Scene;
import wpcg.a5.A5Scene;
import wpcg.a7.A7Scene;
import wpcg.beziersurface.sandbox;
import wpcg.base.CameraController;
import wpcg.base.Scene;
import wpcg.a3.SubdivisionScene;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the main application which is used for all exercises. Only adjust the scene in the main method.
 */
public class ComputergraphicsApplication extends SimpleApplication {

    /**
     * Camera controller.
     */
    private CameraController cameraController;

    /**
     * Scene providing the content.
     */
    private final Scene scene;

    public ComputergraphicsApplication(Scene scene) {
        this.scene = scene;
        this.mouseLeftPressed = false;
        this.mouseRightPressed = false;
        this.mouseMiddlePressed = false;
    }

    @Override
    public void simpleInitApp() {
        // Input
        setupInput();

        // Debug
        setDisplayFps(true);
        setDisplayStatView(true);

        scene.setupLights(assetManager, rootNode, viewPort);
        scene.init(assetManager, rootNode, cameraController);
        viewPort.setBackgroundColor(ColorRGBA.Black); // TODO
    }

    public static void main(String[] args) {
        Logger.getLogger("com.jme3").setLevel(Level.SEVERE);

        // Set the current scene here
//        Scene scene = new IntroScene();
//        Scene scene = new SubdivisionScene();
//        Scene scene = new A4Scene();
        Scene scene = new sandbox();
        ComputergraphicsApplication app = new ComputergraphicsApplication(scene);

        app.setShowSettings(false);
        AppSettings settings = new AppSettings(true);
        settings.setTitle("WP Computergraphics");
        settings.setWidth(2304);
        settings.setHeight(1296);
//        settings.setWidth(1024);
//        settings.setHeight(768);
        app.setSettings(settings);
        app.start();
    }

    /**
     * Register the handlers for the mous and key input (also used for the camera controller).
     */
    private void setupInput() {
        cam.setLocation(new Vector3f(-5, 5, 5));
        stateManager.detach(stateManager.getState(FlyCamAppState.class));
        cameraController = new CameraController(cam);
        cameraController.lookAt(new Vector3f(0, 1, 0), new Vector3f(0, 1, 0));
        viewPort.setBackgroundColor(ColorRGBA.LightGray);
        inputManager.addMapping(MOUSE_MOVED_LEFT, new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addMapping(MOUSE_MOVED_RIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping(MOUSE_MOVED_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addMapping(MOUSE_MOVED_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping(MOUSE_WHEEL_UP, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addMapping(MOUSE_WHEEL_DOWN, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));

        inputManager.addMapping(TRIGGER_MOUSE_LEFT_CLICK,
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping(TRIGGER_MOUSE_RIGHT_CLICK,
                new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping(TRIGGER_MOUSE_MIDDLE_CLICK,
                new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));

        inputManager.addMapping("tap", new TouchTrigger(TouchInput.ALL));

        inputManager.addListener(actionListener, TRIGGER_MOUSE_LEFT_CLICK);
        inputManager.addListener(actionListener, TRIGGER_MOUSE_RIGHT_CLICK);
        inputManager.addListener(actionListener, TRIGGER_MOUSE_MIDDLE_CLICK);
        inputManager.addListener(analogListener, MOUSE_MOVED_LEFT);
        inputManager.addListener(analogListener, MOUSE_MOVED_RIGHT, MOUSE_MOVED_UP, MOUSE_MOVED_DOWN);
        inputManager.addListener(analogListener, MOUSE_WHEEL_UP);
        inputManager.addListener(analogListener, MOUSE_WHEEL_DOWN);

        initKeys();
    }

    /**
     * Custom Keybinding: Map named actions to inputs.
     */
    private void initKeys() {
        inputManager.addMapping("Walk", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(actionListener, "Walk");
    }

    /**
     * Store the mouse button state if pressed/released.
     */
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals(TRIGGER_MOUSE_LEFT_CLICK)) {
                mouseLeftPressed = !mouseLeftPressed;
            } else if (name.equals(TRIGGER_MOUSE_RIGHT_CLICK)) {
                mouseRightPressed = !mouseRightPressed;
            } else if (name.equals(TRIGGER_MOUSE_MIDDLE_CLICK)) {
                mouseMiddlePressed = !mouseMiddlePressed;
            }
        }
    };

    /**
     * Used for input which is continuously used.
     */
    private final AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
            if (mouseLeftPressed && name.equals(MOUSE_MOVED_LEFT)) {
                cameraController.rotateAroundUp(-2);
            } else if (mouseLeftPressed && name.equals(MOUSE_MOVED_RIGHT)) {
                cameraController.rotateAroundUp(2);
            } else if (mouseLeftPressed && name.equals(MOUSE_MOVED_UP)) {
                cameraController.rotateAroundLeft(-2);
            } else if (mouseLeftPressed && name.equals(MOUSE_MOVED_DOWN)) {
                cameraController.rotateAroundLeft(2);
            } else if (name.equals(MOUSE_WHEEL_UP)) {
                cameraController.zoomIn();
            } else if (name.equals(MOUSE_WHEEL_DOWN)) {
                cameraController.zoomOut();
            }
        }
    };

    @Override
    public void simpleUpdate(float tpf) {
        scene.update(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        scene.render();
    }

    // +++  Mouse and key input +++++++++++++++++++++

    /**
     * Store the current mouse state.
     */
    private boolean mouseLeftPressed, mouseRightPressed, mouseMiddlePressed;

    private static final String TRIGGER_MOUSE_LEFT_CLICK = "MOUSE_LEFT_CLICK_DOWN_RELEASE";
    private static final String TRIGGER_MOUSE_RIGHT_CLICK = "MOUSE_RIGHT_CLICK_DOWN_RELEASE";
    private static final String TRIGGER_MOUSE_MIDDLE_CLICK = "MOUSE_MIDDLE_CLICK_DOWN_RELEASE";
    private static final String MOUSE_MOVED_LEFT = "MOUSE_MOVED_LEFT";
    private static final String MOUSE_MOVED_RIGHT = "MOUSE_MOVED_RIGHT";
    private static final String MOUSE_MOVED_UP = "MOUSE_MOVED_UP";
    private static final String MOUSE_MOVED_DOWN = "MOUSE_MOVED_DOWN";
    private static final String MOUSE_WHEEL_UP = "MOUSE_WHEEL_UP";
    private static final String MOUSE_WHEEL_DOWN = "MOUSE_WHEEL_DOWN";
}