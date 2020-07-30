/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.a4.level;

import java.util.HashSet;
import java.util.Set;

/**
 * Ein Beobachteter kann von beliebig vielen Beobachtern beobachtet werden. Bei einer Änderung werden die Beobachter
 * informiert.
 */
public abstract class Observable {

    /**
     * Menge der Beobachter
     */
    private Set<Observer> beobachter;

    public Observable() {
        beobachter = new HashSet<>();
    }

    /**
     * Ein weiterer Beobachter registriert sich.
     */
    public void hinzufuegen(Observer beobachter) {
        this.beobachter.add(beobachter);
    }

    /**
     * Alle registrierten werden über eine Veränderung mit Zusatzinformation informiert.
     */
    public void melden(Object payload) {
        beobachter.forEach(b -> b.update(payload));
    }

    /**
     * Alle registrierten werden über eine Veränderung ohne Zusatzinformation informiert.
     */
    public void melden() {
        melden(null);
    }
}
