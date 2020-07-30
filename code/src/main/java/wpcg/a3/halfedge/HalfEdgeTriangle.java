/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.a3.halfedge;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/**
 * A facet (here triangle) has a reference to one of its half edges. This datastructure
 * represents a general mesh (triangle, quad, ...). However, we only use
 * triangle meshes here.
 */
public class HalfEdgeTriangle {

    /**
     * One of the half edges around the facet.
     */
    private HalfEdge halfEdge;

    /**
     * Normal of the facet.
     */
    private Vector3f normal;

    /**
     * Color of the facet.
     */
    private ColorRGBA color;

    public HalfEdgeTriangle() {
        halfEdge = null;
        normal = new Vector3f(0, 1, 0);
        color = ColorRGBA.Gray;
    }

    public HalfEdgeTriangle(HalfEdgeTriangle triangle) {
        HalfEdgeTriangle t = new HalfEdgeTriangle();
        t.halfEdge = halfEdge;
    }

    @Override
    public String toString() {
        return "HalfEdgeTriangle";
    }

    /**
     * Compute the area of the facet. Area of the facet.
     *
     * @return Area of the triangle.
     */
    public double getArea() {
        Vector3f v0 = halfEdge.getStartVertex().getPosition();
        Vector3f v1 = halfEdge.getNext().getStartVertex().getPosition();
        Vector3f v2 = halfEdge.getNext().getNext().getStartVertex().getPosition();
        return v1.subtract(v0).cross(v2.subtract(v0)).length() / 2.0;
    }

    /**
     * Compute the centroid (center of mass) of the triangle.
     *
     * @return Centroid of the triangle.
     */
    public Vector3f getCentroid() {
        Vector3f v0 = halfEdge.getStartVertex().getPosition();
        Vector3f v1 = halfEdge.getNext().getStartVertex().getPosition();
        Vector3f v2 = halfEdge.getNext().getNext().getStartVertex().getPosition();
        return (v0.add(v1).add(v2)).mult(1.0f / 3.0f);
    }

    // +++ GETTER/SETTER +++++++++++++++++++++++++++

    public void setNormal(Vector3f normal) {
        this.normal = normal;
    }

    public void setColor(ColorRGBA color) {
        this.color = color;
    }

    public ColorRGBA getColor() {
        return color;
    }

    public HalfEdge getHalfEdge() {
        return halfEdge;
    }

    public void setHalfEdge(HalfEdge halfEdge) {
        this.halfEdge = halfEdge;
    }
}
