/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.base;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.control.CameraControl;

/**
 * Control the camera movement.
 */
public class CameraController extends CameraControl {

    /**
     * Reference to the jMonkey camera
     */
    private final Camera cam;

    /**
     * Rotation angle offset when rotating around the up-vector.
     */
    private final float ANGLE_ROUND_UP = 0.06f;

    /**
     * Reference point.
     */
    private Vector3f ref;

    public CameraController(Camera cam) {
        this.cam = cam;
        ref = null;
    }

    /**
     * Rotate the camera around the up vector.
     */
    public void rotateAroundUp(float sign) {
        Vector3f eye = cam.getLocation();
        Vector3f dir = ref.subtract(eye);
        Matrix3f m = new Matrix3f();
        m.fromAngleAxis(ANGLE_ROUND_UP * sign, cam.getUp());
        Vector3f newDir = m.mult(dir);
        Vector3f newEye = ref.subtract(newDir);
        cam.setLocation(newEye);
        cam.lookAt(ref, cam.getUp());
    }

    /**
     * Rotate the camera around the left vector.
     */
    public void rotateAroundLeft(float sign) {
        Vector3f eye = cam.getLocation();
        Vector3f dir = ref.subtract(eye);
        Matrix3f m = new Matrix3f();
        m.fromAngleAxis(ANGLE_ROUND_UP * sign, cam.getLeft());
        Vector3f newDir = m.mult(dir);
        Vector3f newEye = ref.subtract(newDir);
        cam.setLocation(newEye);
        cam.lookAt(ref, cam.getUp());
    }

    /**
     * Set a new look position (reference and up vector).
     */
    public void lookAt(Vector3f ref, Vector3f up) {
        this.ref = new Vector3f(ref);
        cam.lookAt(ref, up);
    }

    /**
     * Setup the camera.
     */
    public void setup(Vector3f eye, Vector3f ref, Vector3f up) {
        cam.setLocation(eye);
        this.ref = new Vector3f(ref);
        cam.lookAt(ref, up);
    }

    /**
     * Zoom closer to the reference point.
     */
    public void zoomIn() {
        Vector3f eye = cam.getLocation();
        Vector3f ref = this.ref;
        Vector3f dir = ref.subtract(eye);
        dir = dir.mult(0.02f);
        cam.setLocation(eye.add(dir));
    }

    /**
     * Zoom further away from the reference point.
     */
    public void zoomOut() {
        Vector3f eye = cam.getLocation();
        Vector3f ref = this.ref;
        Vector3f dir = ref.subtract(eye);
        dir = dir.mult(-0.02f);
        cam.setLocation(eye.add(dir));
    }
}
