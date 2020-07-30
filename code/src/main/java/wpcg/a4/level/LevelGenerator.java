/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.a4.level;

import java.awt.Point;

/**
 * Helper class to generate a level.
 */
public class LevelGenerator {

    /**
     * Generates a rectangular level grid.
     */
    public static void generiereLevel(Level level, int breite, int hoehe) {

        level.clear();

        Cell[][] zellenCache = new Cell[breite][hoehe];

        for (int i = 0; i < breite; i++) {
            for (int j = 0; j < hoehe; j++) {
                Cell zelle = new Cell(new Point(i, j));
                level.zelleHinzufuegen(zelle);
                zellenCache[i][j] = zelle;
            }
        }

        for (int i = 0; i < breite; i++) {
            for (int j = 0; j < hoehe; j++) {
                if (i < breite - 1) {
                    if (i % 2 == 0) {
                        if (j > 0) {
                            zellenCache[i][j].setNeighborCell(Direction.UHR_2,
                                    zellenCache[i + 1][j - 1]);
                        }
                        zellenCache[i][j].setNeighborCell(Direction.UHR_4,
                                zellenCache[i + 1][j]);

                    } else {
                        zellenCache[i][j].setNeighborCell(Direction.UHR_2,
                                zellenCache[i + 1][j]);
                        if (j < hoehe - 1) {
                            zellenCache[i][j].setNeighborCell(Direction.UHR_4,
                                    zellenCache[i + 1][j + 1]);
                        }
                    }
                }
                if (j < hoehe - 1) {
                    zellenCache[i][j].setNeighborCell(Direction.UHR_6,
                            zellenCache[i][j + 1]);
                }
            }
        }
    }

}
