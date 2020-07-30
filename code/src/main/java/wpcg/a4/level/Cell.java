/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.a4.level;

import com.jme3.math.Vector2f;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.Point;
import java.util.List;
import java.util.Map;

/**
 * A hexagon cell in a level.
 */
public class Cell {

    /**
     * Link to the neighboring cell (or null at boundry along the six directions/edges.
     */
    private Link[] neighbors = new Link[6];

    /**
     * 2D Index of the cell.
     */
    private Point index = null;

    public Cell() {
        this.index = null;
    }

    public Cell(Point index) {
        this.index = new Point(index);
    }

    /**
     * Return the neighboring in the given direction (or null at boundary).
     */
    public Cell getNeighborCell(Direction richtung) {
        if (neighbors[richtung.ordinal()] == null) {
            return null;
        }
        return neighbors[richtung.ordinal()].getOppositeCell(this);
    }

    /**
     * Setter for tzhe neighbor in the given direction.
     */
    public void setNeighborCell(Direction richtung, Cell nachbarZelle) {
        Link link = new Link(this, nachbarZelle);
        neighbors[richtung.ordinal()] = link;
        nachbarZelle.setLink(richtung.getGegenueber(), link);
    }

    /**
     * Set the link between cells, used internally.
     */
    private void setLink(Direction richtung, Link link) {
        neighbors[richtung.ordinal()] = link;
    }

    /**
     * Sets that a cell has a Wall at a link in a given direction.
     */
    public void setWall(Direction richtung, boolean hatWand) {
        Link link = neighbors[richtung.ordinal()];
        if (link != null) {
            link.setIsWall(hatWand);
        }
    }

    /**
     * Returns if the edge in a given direction of the cell is a wall.
     */
    public boolean isWall(Direction richtung) {
        return neighbors[richtung.ordinal()] == null
                || neighbors[richtung.ordinal()].isWall();
    }

    /**
     * Return the cell center (computed based on the index).
     */
    public Vector2f getCenter() {
        float x = index.x * 1.5f + 1;
        float y = index.y * getZellenhoehe() * 2.0f
                + ((index.x % 2 == 1) ? getZellenhoehe() : 0)
                + 1;
        return new Vector2f(x, y);
    }

    /**
     * Internal helping routing computing the height of a cell triangle.
     */
    public static float getZellenhoehe() {
        return (float) (Math.sqrt(3) / 2.0);
    }

    /**
     * Return the i'th corner of the cell.
     */
    public Vector2f getCorner(int index) {
        Vector2f cellCenter = getCenter();

        //cellCenter = cellCenter.add(new Vector2f(cell.getIndex().x * 5, cell.getIndex().y * 5));

        Vector2f p = null;
        switch (index) {
            case 0:
                p = new Vector2f(cellCenter.x - 0.5f, cellCenter.y - Cell.getZellenhoehe());
                break;
            case 1:
                p = new Vector2f(cellCenter.x + 0.5f, cellCenter.y - Cell.getZellenhoehe());
                break;
            case 2:
                p = new Vector2f(cellCenter.x + 1, cellCenter.y);
                break;
            case 3:
                p = new Vector2f(cellCenter.x + 0.5f, cellCenter.y + Cell.getZellenhoehe());
                break;
            case 4:
                p = new Vector2f(cellCenter.x - 0.5f, cellCenter.y + Cell.getZellenhoehe());
                break;
            case 5:
                p = new Vector2f(cellCenter.x - 1, cellCenter.y);
                break;
            default:
                throw new IllegalArgumentException("Should not happen.");
        }
        return p;
    }

    // ++++ GETTER/SETTER ++++++++++++++++++++

    public Point getIndex() {
        return index;
    }

    public Link getLink(Direction richtung) {
        return neighbors[richtung.ordinal()];
    }

    // +++ JSON +++++++++++++++++++++++

    /**
     * JSON constant identifier.
     */
    private static final String ZELLE_INDEX_I = "indexI";

    /**
     * JSON constant identifier.
     */
    private static final String ZELLE_INDEX_J = "indexJ";

    /**
     * JSON constant identifier.
     */
    private static final String NACHBARN = "nachbarn";

    /**
     * Creates a JSON object from the cell information.
     */
    public JSONObject toJson(Object metaInformation) {
        JSONObject zellenObjekt = new JSONObject();
        Map<Link, Integer> linkMap = (Map<Link, Integer>) metaInformation;
        JSONArray links = new JSONArray();
        for (int i = 0; i < 6; i++) {
            Link link = getLink(Direction.values()[i]);
            if (link == null) {
                links.put(-1);
            } else {
                links.put(linkMap.get(link));
            }
        }
        zellenObjekt.put(NACHBARN, links);
        zellenObjekt.put(ZELLE_INDEX_I, getIndex().x);
        zellenObjekt.put(ZELLE_INDEX_J, getIndex().y);
        return zellenObjekt;
    }

    /**
     * Fills the this cell with the information from the JSON object.
     */
    public void fromJson(JSONObject jsonObjekt, Object metaInformation) {
        List<Link> links = (List<Link>) metaInformation;
        index = new Point(jsonObjekt.getInt(ZELLE_INDEX_I),
                jsonObjekt.getInt(ZELLE_INDEX_J));
        JSONArray nachbarn = (JSONArray) jsonObjekt.get(NACHBARN);
        for (int i = 0; i < 6; i++) {
            int nachbarIndex = nachbarn.getInt(i);
            if (nachbarIndex < 0) {
                neighbors[i] = null;
            } else {
                Link link = links.get(nachbarIndex);
                neighbors[i] = link;
                link.setZelle(this);
            }
        }
    }
}