/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.base;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import wpcg.base.animatedmesh.AnimationController;

import java.util.List;

/**
 * Represents a path for an entity
 */
public class Path {
    /**
     * Points on the path
     */
    private List<Vector3f> waypoints;

    /**
     * Current parameter on path.
     */
    private float t;

    public Path(List<Vector3f> waypoints) {
        this.waypoints = waypoints;
        t = 0;
    }

    /**
     * Move along the path
     */
    public void moveOnPath(float distance) {
        t += distance;
        float pathLength = getPathLength();
        if (t > pathLength) {
            t -= pathLength;
        }
    }

    /**
     * Get the current position on the path.
     */
    public AnimationController.Pose getCurrentPose() {
        float t = this.t;
        int segmentIndex = getSegmentIndex();
        float segmentLength = getSegmentLength(segmentIndex);
        for (int i = 0; i < segmentIndex; i++) {
            t -= getSegmentLength(i);
        }
        float alpha = t / segmentLength;
        Vector3f pos = waypoints.get(segmentIndex).mult((1 - alpha)).add(
                waypoints.get((segmentIndex + 1) % waypoints.size()).mult(alpha));
        Vector3f dir = waypoints.get((segmentIndex + 1) % waypoints.size()).subtract(waypoints.get(segmentIndex));
        return new AnimationController.Pose(pos, makeRotation(dir));
    }

    /**
     * Create a rotation matrix from a direction.
     */
    private Matrix3f makeRotation(Vector3f z) {
        z = z.normalize();
        Vector3f y = new Vector3f(0, 1, 0);
        Vector3f x = y.cross(z).normalize();
        //z = x.cross(y).normalize();
        Matrix3f rot = new Matrix3f();
        rot.fromAxes(x, y, z);
        return rot;
    }

    /**
     * Return the length of the segment from waypoint i to i+1
     */
    private float getSegmentLength(int i) {
        return waypoints.get((i + 1) % waypoints.size()).distance(waypoints.get(i % waypoints.size()));
    }

    /**
     * Return the length of the path.
     */
    private float getPathLength() {
        float length = 0;
        for (int i = 0; i < waypoints.size(); i++) {
            length += getSegmentLength(i);
        }
        return length;
    }

    /**
     * Compute and return the direction along the current path segment.
     */
    public Vector3f getDirection() {
        int segmentIndex = getSegmentIndex();
        return waypoints.get((segmentIndex + 1) % waypoints.size()).subtract(waypoints.get(segmentIndex));
    }

    /**
     * Return the index of the current segment in the path.
     */
    private int getSegmentIndex() {
        float t = this.t;
        for (int i = 0; i < waypoints.size(); i++) {
            float segmentLength = getSegmentLength(i);
            if (t > segmentLength) {
                // not this segment
                t -= segmentLength;
            } else {
                return i;
            }
        }
        // Fallback - should not happen.
        return 0;
    }
}
