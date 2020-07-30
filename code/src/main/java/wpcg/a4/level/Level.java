/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.a4.level;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import org.json.JSONArray;
import org.json.JSONObject;
import wpcg.base.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

/**
 * A level consisting of a grid of hexagon cells which are connected via links. Each link can be a wall or not.
 */
public class Level extends Observable {

    /**
     * Set containing all cells.
     */
    private Set<Cell> cells;

    public Level() {
        cells = new HashSet<Cell>();
    }

    /**
     * Zelle in Level einfügen.
     */
    public void zelleHinzufuegen(Cell zelle) {
        cells.add(zelle);
    }

    /**
     * Return an iterator for the links
     */
    public Iterator<Link> getLinkIterator() {
        Set<Link> alleLinks = new HashSet<Link>();
        for (Cell zelle : cells) {
            for (Direction richtung : Direction.values()) {
                Link link = zelle.getLink(richtung);
                if (link != null) {
                    alleLinks.add(link);
                }
            }
        }
        return alleLinks.iterator();
    }

    /**
     * Resize the level (cell grid).
     */
    public void resize(int resX, int resY) {
        LevelGenerator.generiereLevel(this, resX, resY);
        melden();
    }

    /**
     * Clear the level (remove all cells).
     */
    public void clear() {
        cells.clear();
    }

    /**
     * Compute and return the center of the level.
     */
    public Vector3f getCenter() {
        Vector2f ll = new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);
        Vector2f ur = new Vector2f(Float.MIN_VALUE, Float.MIN_VALUE);
        for (Cell cell : cells) {
            for (int i = 0; i < 6; i++) {
                Vector2f p = cell.getCorner(i);
                if (p.x < ll.x) {
                    ll.x = p.x;
                }
                if (p.y < ll.y) {
                    ll.y = p.y;
                }
                if (p.x > ur.x) {
                    ur.x = p.x;
                }
                if (p.y > ur.y) {
                    ur.y = p.y;
                }
            }
        }
        Vector2f center2D = ll.add(ur).mult(0.5f);
        Vector3f center = new Vector3f(center2D.x, 0, center2D.y);
        return center;
    }

    /**
     * Get the cell with the given index.
     */
    public Cell getCell(int x, int y) {
        for (Cell cell : cells) {
            if (cell.getIndex().x == x && cell.getIndex().y == y) {
                return cell;
            }
        }
        return null;
    }

    // +++ GETTER/SETTER ++++++++++++++++++++

    public Iterator<Cell> getCellIterator() {
        return cells.iterator();
    }

    // ++++ JSON +++++++++++++++++++++++

    /**
     * JSON constant identifier.
     */
    private static final String ZELLEN = "zellen";

    /**
     * JSON constant identifier.
     */
    private static final String LINKS = "links";

    /**
     * Save the level to a file with the given filename in a json structure.
     */
    public void saveJsonToFile(String filename) {
        try (FileWriter writer = new FileWriter(new File(filename))) {
            JSONObject root = toJson(null);
            root.write(writer);
            System.out.println("JSON Dokument erfolgreich in Datei "
                    + filename + " gschrieben.");
        } catch (FileNotFoundException e) {
            System.out.println("Datei nicht gefunden: " + filename);
        } catch (IOException e) {
            System.out.println("Fehler beim Schreiben in die Datei "
                    + filename);
        }
    }

    /**
     * Make a JSON object from the level information.
     */
    private JSONObject toJson(Object metaInformation) {
        JSONObject levelObjekt = new JSONObject();

        // Links
        JSONArray linkListe = new JSONArray();
        Map<Link, Integer> linkMap = new HashMap<Link, Integer>();
        int linkIndex = 0;
        for (Iterator<Link> it = getLinkIterator(); it.hasNext(); ) {
            Link link = it.next();
            linkMap.put(link, linkIndex);
            linkListe.put(link.toJson(null));
            linkIndex++;
        }
        levelObjekt.put(LINKS, linkListe);

        // Cells
        JSONArray zellenListe = new JSONArray();
        for (Iterator<Cell> it = getCellIterator(); it.hasNext(); ) {
            Cell zelle = it.next();
            zellenListe.put(zelle.toJson(linkMap));
        }
        levelObjekt.put(ZELLEN, zellenListe);

        return levelObjekt;
    }

    /**
     * Read the level information from a file with JSON content.
     */
    public void readLevelFromFile(String filename) {
        StringBuilder text = new StringBuilder();
        try (Stream<String> stream =
                     Files.lines(Paths.get(filename), StandardCharsets.UTF_8)) {
            stream.forEach(s -> text.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(text.toString());
        fromJson(jsonObject);
    }

    /**
     * Set the level information from a JSON object.
     */
    private void fromJson(JSONObject jsonObjekt) {
        // Links
        JSONArray linksArray = (JSONArray) jsonObjekt.get(LINKS);
        List<Link> links = linksFromJson(linksArray);

        // Cells
        JSONArray zellenArray = (JSONArray) jsonObjekt.get(ZELLEN);
        List<Cell> zellen = cellsFromJson(zellenArray, links);
        for (Cell zelle : zellen) {
            zelleHinzufuegen(zelle);
        }

        Logger.getInstance().msg("Successfully loaded level with " + zellen.size() + " cells");
    }

    /**
     * Read links from JSON array.
     */
    private static List<Link> linksFromJson(JSONArray linksArray) {
        List<Link> links = new ArrayList<Link>();
        for (int i = 0; i < linksArray.length(); i++) {
            Object obj = linksArray.get(i);
            JSONObject linkObjekt = (JSONObject) obj;
            Link link = new Link();
            link.fromJson(linkObjekt, null);
            links.add(link);
        }
        return links;
    }

    /**
     * Read cells from JSON array.
     */
    private static List<Cell> cellsFromJson(JSONArray zellenArray, List<Link> links) {
        List<Cell> zellen = new ArrayList<Cell>();
        for (int i = 0; i < zellenArray.length(); i++) {
            Object obj = zellenArray.get(i);
            JSONObject zellenObjekt = (JSONObject) obj;
            Cell zelle = new Cell();
            zelle.fromJson(zellenObjekt, links);
            zellen.add(zelle);
        }
        return zellen;
    }
}
