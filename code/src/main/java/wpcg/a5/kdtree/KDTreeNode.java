/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.a5.kdtree;

import com.jme3.math.Vector2f;

/**
 * A node in the tree consists of a root data point and can have two children.
 */
public class KDTreeNode<T> {

    /**
     * This is the data point which created the node and which later is used to split it further.
     */
    private KDTreeData<T> data;

    /**
     * A node can have up to two children. Both can be unassigned (null). If both are null, the array is also null.
     */
    private KDTreeNode[] children;

    /**
     * Split direction of the node: x or y (alternating between levels).
     */
    private SplitDirection splitDirection;

    /**
     * Bounding box lower left corner of the node.
     */
    private Vector2f ll;

    /**
     * Bounding box upper right corner of the node.
     */
    private Vector2f ur;

    /**
     * This enumeration encodes the split direction.: x or y
     */
    public enum SplitDirection {
        X, Y;

        public SplitDirection next() {
            if (this == X) {
                return Y;
            } else {
                return X;
            }
        }
    }

    public KDTreeNode(KDTreeData<T> data, SplitDirection splitDirection, Vector2f ll, Vector2f ur) {
        this.data = data;
        this.splitDirection = splitDirection;
        this.children = null;
        this.ll = ll;
        this.ur = ur;
    }

    // +++ GETTER/SETTER +++++++++++++++++++++++++++++

    /**
     * Sets the child node in the negative direction (smaller than split value).
     */
    public void setNeg(KDTreeNode<T> kdTree) {
        set(kdTree, 0);
    }

    /**
     * Sets the child node in the positive direction (larger than split value).
     */
    public void setPos(KDTreeNode<T> kdTree) {
        set(kdTree, 1);
    }

    /**
     * Internal helper method for setNeg() and setPos(): Set the child, generate array if required.
     */
    private void set(KDTreeNode<T> kdTree, int index) {
        if (kdTree == null) {
            return;
        }

        if (children == null) {
            children = new KDTreeNode[2];
        }
        children[index] = kdTree;
    }

    public SplitDirection getSplittingDirection() {
        return splitDirection;
    }

    public KDTreeData<T> getData() {
        return data;
    }

    public KDTreeNode<T> getNegChild() {

        return children == null ? null : children[0];
    }

    public KDTreeNode<T> getPosChild() {
        return children == null ? null : children[1];
    }

    public void setSplitDirection(SplitDirection splitDirection) {
        this.splitDirection = splitDirection;
    }

    public Vector2f getLL() {
        return ll;
    }

    public Vector2f getUR() {
        return ur;
    }
}
