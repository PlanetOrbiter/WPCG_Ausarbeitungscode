/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.a5.kdtree;

import com.jme3.math.Vector2f;

import java.util.Comparator;

import static wpcg.a5.kdtree.KDTreeNode.SplitDirection.X;

/**
 * This class represents a data point in the data structure. It has a 2D location and an arbitrary data
 * payload.
 */
public class KDTreeData<T> {

    /**
     * This position is used to build the kd tree.
     */
    private Vector2f p;

    /**
     * This payload is associated to each data point and can contain any data.
     */
    private T data;

    public KDTreeData(Vector2f p, T data) {
        this.p = p;
        this.data = data;
    }

    /**
     * Creates a comparator based on the current split direction (x or y). Can be helpful the the tree builder.
     */
    public static <T> Comparator<KDTreeData<T>> getComparator(KDTreeNode.SplitDirection splitDirection) {
        if (splitDirection == X) {
            return (d1, d2) -> {
                float d = (d1.p.x - d2.p.x);
                return d < 0 ? -1 : (d > 0 ? 1 : 0);
            };
        } else {
            return (d1, d2) -> {
                float d = (d1.p.y - d2.p.y);
                return d < 0 ? -1 : (d > 0 ? 1 : 0);
            };
        }
    }

    @Override
    public String toString() {
        return data.toString() + "(" + p.toString() + ")";
    }

    // +++ Getter/Setter +++++++++++++++++++++++


    public T getData() {
        return data;
    }

    public Vector2f getP() {
        return p;
    }
}
