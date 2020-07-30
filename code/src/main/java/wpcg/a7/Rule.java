/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.a7;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A rule provides the successors for a symbol und can provide a weight if there are multiple rules for a symbol
 * in the grammar.
 */
public class Rule {

    /**
     * Symbol the rule can be applied for.
     */
    public String symbol;

    /***
     * List of successor symbols.
     */
    public List<String> successors;

    /**
     * Rule weight (if there are multiple rules for a symbol in the grammar).
     */
    public double weight;

    public Rule(String line) {
        symbol = null;
        successors = null;
        weight = -1;

        String regex = "(?<pred>.*)\\s*-->\\s*(?<hds>.*)\\s*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            symbol = matcher.group("pred").trim();
            String rhs = matcher.group("hds").trim();
            String[] rhsTokes = rhs.split(":");
            if (rhsTokes.length == 1) {
                successors = Arrays.asList(rhs.trim().split("\\s"));
                weight = 1;
            } else {
                successors = Arrays.asList(rhsTokes[0].trim().split("\\s"));
                weight = Double.valueOf(rhsTokes[1].trim());
            }
        } else {
            throw new IllegalArgumentException("Cannot parse rool from line " + line);
        }
    }
}