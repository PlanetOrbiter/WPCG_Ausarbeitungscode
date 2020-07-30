/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.base.mesh;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import wpcg.base.Logger;

/**
 * Representation of a triangle consisting of three indices. The indices
 * reference vertices in the vertex list in a triangle mesh.
 */
public class Triangle {

    /**
     * Indices of the vertices.
     */
    private int[] vertexIndices;

    /**
     * Triangle color in RGBA format.
     */
    protected ColorRGBA color;

    /**
     * Facet normal
     */
    protected Vector3f normal;

    /**
     * Indices of the texture coordinates.
     */
    protected int[] texCoordIndices;

    public Triangle() {
        this(-1, -1, -1, -1, -1, -1,
                new Vector3f(0, 1, 0), new ColorRGBA(0.5f, 0.5f, 0.5f, 1));
    }

    public Triangle(int a, int b, int c, int tA, int tB, int tC, Vector3f normal, ColorRGBA color) {
        vertexIndices = new int[3];
        texCoordIndices = new int[3];
        vertexIndices[0] = a;
        vertexIndices[1] = b;
        vertexIndices[2] = c;

        if (a == b || b == c || a == c) {
            Logger.getInstance().error("Invalid triangle generated.");
        }

        texCoordIndices[0] = tA;
        texCoordIndices[1] = tB;
        texCoordIndices[2] = tC;
        this.color = new ColorRGBA(color);
        this.normal = new Vector3f(normal);
    }

    public Triangle(int a, int b, int c) {
        this(a, b, c,
                -1, -1, -1,
                new Vector3f(1, 0, 0),
                new ColorRGBA(0.5f, 0.5f, 0.5f, 1));
    }

    public Triangle(int a, int b, int c, int tA, int tB, int tC) {
        this(a, b, c,
                tA, tB, tC,
                new Vector3f(1, 0, 0),
                new ColorRGBA(0.5f, 0.5f, 0.5f, 1));
    }


    public Triangle(Triangle triangle) {
        this(triangle.vertexIndices[0], triangle.vertexIndices[1], triangle.vertexIndices[2],
                triangle.texCoordIndices[0], triangle.texCoordIndices[1], triangle.texCoordIndices[2],
                triangle.normal, triangle.color);
    }

    /**
     * Compute and return the area of the triangle.
     */
    public static double getArea(Vector3f a, Vector3f b, Vector3f c) {
        return b.subtract(a).cross(c.subtract(a)).length();
    }

    /**
     * Add an offset to all texture coordinates (required when merging meshes).
     */
    public void addTexCoordOffset(int offset) {
        for (int i = 0; i < 3; i++) {
            texCoordIndices[i] += offset;
        }
    }

    /**
     * Add an offset to all vertex indices (required when merging meshes).
     */
    public void addVertexIndexOffset(int offset) {
        for (int i = 0; i < 3; i++) {
            vertexIndices[i] += offset;
        }
    }

    /**
     * Checks if the triangle has a duplicated vertex index.
     */
    public boolean isDegenerated() {
        return vertexIndices[0] == vertexIndices[1] ||
                vertexIndices[1] == vertexIndices[2] ||
                vertexIndices[2] == vertexIndices[0];
    }

    /**
     * Replace vertex index iReplace by iKeep.
     */
    public void replaceVertexIndex(int iKeep, int iReplace) {
        for (int i = 0; i < 3; i++) {
            if (vertexIndices[i] == iReplace) {
                vertexIndices[i] = iKeep;
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Triangle");
    }

    // +++ GETTER/SETTER +++++++++++++++++++++++++

    public ColorRGBA getColor() {
        return color;
    }

    /**
     * Color must be a 4D vector in RGBA format.
     */
    public void setColor(ColorRGBA color) {
        this.color = color;
    }

    public void setNormal(Vector3f normal) {
        this.normal = new Vector3f(normal);
    }

    public Vector3f getNormal() {
        return normal;
    }

    /**
     * Set the three texture coordinates at the three triangle corners.
     */
    public void setTextureCoordinates(int texCoordIndex1, int texCoordIndex2, int texCoordIndex3) {
        this.texCoordIndices[0] = texCoordIndex1;
        this.texCoordIndices[1] = texCoordIndex2;
        this.texCoordIndices[2] = texCoordIndex3;
    }

    public int getVertexIndex(int index) {
        return vertexIndices[index];
    }

    public int getTextureCoordinate(int vertexInTriangleIndex) {
        return texCoordIndices[vertexInTriangleIndex];
    }

    public int getA() {
        return vertexIndices[0];
    }

    public int getB() {
        return vertexIndices[1];
    }

    public int getC() {
        return vertexIndices[2];
    }
}
