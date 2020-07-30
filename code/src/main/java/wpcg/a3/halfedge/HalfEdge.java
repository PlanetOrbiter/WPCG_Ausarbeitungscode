/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.a3.halfedge;

/**
 * A half edge has references to the next edge within the current facet, the
 * opposite edge, its start vertex and the facet it belongs to.
 */
public class HalfEdge {

    /**
     * Reference to the next edge in the mesh.
     */
    private HalfEdge next;

    /**
     * Reference to the opposite edge in the mesh.
     */
    private HalfEdge opposite;

    /**
     * Start vertex of the half edge.
     */
    private HalfEdgeVertex startVertex;

    /**
     * The half edge belongs to this facet.
     */
    private HalfEdgeTriangle facet;

    public HalfEdge() {
        this.next = null;
        this.opposite = null;
        this.startVertex = null;
        this.facet = null;
    }

    public HalfEdge(HalfEdgeVertex v) {
        this.next = null;
        this.opposite = null;
        this.startVertex = v;
        this.facet = null;
    }

    @Override
    public String toString() {
        return "Half Edge";
    }

    /**
     * Return the end vertex of a half edge (not directly saved.
     *
     * @return End index of the half edge, null on error.
     */
    public HalfEdgeVertex getEndVertex() {
        if (getNext() == null) {
            throw new IllegalArgumentException();
        }
        return getNext().getStartVertex();
    }

    /**
     * Returns true if the edge is at the boundary (has no opposite half edge).
     */
    public boolean isBoundary() {
        return opposite == null;
    }

    // +++ GETTER/SETTER +++++++++++++++++++++++++++++++++++++

    public HalfEdge getNext() {
        return next;
    }

    public void setNext(HalfEdge next) {
        this.next = next;
    }

    public HalfEdge getOpposite() {
        return opposite;
    }

    public void setOpposite(HalfEdge opposite) {
        this.opposite = opposite;
    }

    public HalfEdgeVertex getStartVertex() {
        return startVertex;
    }

    public void setStartVertex(HalfEdgeVertex startVertex) {
        this.startVertex = startVertex;
    }

    public HalfEdgeTriangle getFacet() {
        return facet;
    }

    public void setFacet(HalfEdgeTriangle facet) {
        this.facet = facet;
    }
}
