/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.a4.level;

/**
 * Ein Beobachter registriert sich als solcher bei einem
 */
public interface Observer {

    /**
     * Diese Methode wird aufgerufen, wenn der Beobachter über eine Änderung informiert wird.
     *
     * @param payload Es können Informationen zu der Änderungen mitgegeben werden. Wenn keine
     *                Information mitgegeben wird, dann ist payload null.
     */
    void update(Object payload);
}
