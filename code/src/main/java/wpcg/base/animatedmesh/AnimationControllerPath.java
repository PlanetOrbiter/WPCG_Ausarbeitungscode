/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 * <p>
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 * <p>
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */
package wpcg.base.animatedmesh;

import com.jme3.math.Vector3f;
import wpcg.base.Path;

import java.util.List;

/**
 * An entity has a location and orientation in the scene.
 */
public class AnimationControllerPath implements AnimationController {
    /**
     * Path for the entity to move on.
     */
    private Path path;

    /**
     * Movement spee.
     */
    private float speed;

    public AnimationControllerPath(List<Vector3f> waypoints, float speed) {
        this.path = new Path(waypoints);
        this.speed = speed;
    }

    @Override
    public Pose getPose() {
        return path.getCurrentPose();
    }

    @Override
    public Vector3f getDirection() {
        return path.getDirection();
    }

    /**
     * Update pose.
     */
    public void move(float time) {
        path.moveOnPath(time * speed);
    }
}
