/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.base.mesh;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

/**
 * Represents a vertex in 3-space with position and normal.
 */
public class Vertex {

    /**
     * Vertex position in 3-space.
     */
    protected Vector3f position;

    /**
     * Vertex normal in 3-space.
     */
    protected Vector3f normal;

    /**
     * Color in RBGA format.
     */
    protected ColorRGBA color;

    public Vertex(Vector3f position, Vector3f normal, ColorRGBA color) {
        this.position = new Vector3f(position);
        this.normal = new Vector3f(normal);
        this.color = new ColorRGBA(color);
    }

    public Vertex(Vector3f position, Vector3f normal) {
        this(position, normal, new ColorRGBA(0.5f, 0.5f, 0.5f, 1));
    }

    public Vertex(Vector3f position) {
        this(position, new Vector3f(0, 1, 0), new ColorRGBA(0.5f, 0.5f, 0.5f, 1));
    }

    public Vertex(Vertex vertex) {
        this(vertex.position, vertex.normal, new ColorRGBA(vertex.color));
    }

    @Override
    public String toString() {
        return position.toString();
    }

    // +++ GETTER/SETTER ++++++++++++++++++++++++++

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getNormal() {
        return normal;
    }

    public void setNormal(Vector3f normal) {
        this.normal = normal;
    }

    public ColorRGBA getColor() {
        return color;
    }

    public void setColor(ColorRGBA color) {
        this.color = color;
    }
}
