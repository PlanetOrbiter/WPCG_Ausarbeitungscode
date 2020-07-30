/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.base.animatedmesh;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;

/**
 * An entity is used to define the pose of a thing in a 3D world. It can be changed over time by calling move().
 */
public interface AnimationController {

    /**
     * The pose encodes the current position an rotation of the thing
     */
    class Pose {
        /*
         * Position in 3-space.
         */
        public Vector3f pos;

        /**
         * Rotation matrix
         */
        public Matrix3f rot;

        public Pose(Vector3f pos, Matrix3f rot) {
            this.pos = pos;
            this.rot = rot;
        }
    }

    /**
     * Update the pose.
     */
    void move(float time);

    /**
     * Get the current pose.
     */
    Pose getPose();

    /**
     * Return the current Orientation.
     */
    Vector3f getDirection();
}
