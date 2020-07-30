/**
 * Diese Datei ist Teil der Vorgabe zur Lehrveranstaltung Einführung in die Computergrafik der Hochschule
 * für Angewandte Wissenschaften Hamburg von Prof. Philipp Jenke (Informatik)
 */

package wpcg.a7;

import java.io.*;
import java.util.*;

/**
 * Representation of the building grammar.
 */
public class Grammar {

    /**
     * Mapping between the symbols and the matching rules.
     */
    private Map<String, List<Rule>> rules;

    private final long seed; // 222

    private Random rnd;

    public Grammar(long seed) {
        this.seed = seed;
        rules = new HashMap<>();
        Locale.setDefault(Locale.US);
        parse("src/main/resources/grammars/building_pcg.grammar");
        rnd = new Random();
        if(seed != 0)
            rnd.setSeed(seed);
    }

    public List<String> derive(String symbol)
    {
        List<String> ret = new ArrayList<>();
        if (isTerminal(symbol))
        {
            ret.add(symbol);
            return ret;
        }
        if(isDeterministicSymbolRule(symbol))
        {
            List<String> sucs = rules.get(symbol).get(0).successors;
            for(String suc : sucs)
            {
                ret.addAll(derive(suc));
            }
            return ret;
        }
        else
        {
            List<Rule> symbolRules = rules.get(symbol);
            float p = rnd.nextFloat();
            float currentWeight = 0;
            for (Rule r : symbolRules)
            {
                currentWeight += r.weight;
                if(currentWeight > p)
                {
//                    System.out.println("P: " + p);
                    List<String> sucs = r.successors;
                    for(String suc : sucs)
                    {
                        ret.addAll(derive(suc));
                    }
                    return ret;
                }
            }
            //Falls es float Ungenauigkeiten gab
            List<String> sucs = symbolRules.get(symbolRules.size() - 1).successors;
            for(String suc : sucs)
            {
                ret.addAll(derive(suc));
            }
            return ret;
        }
    }

    /**
     * Returns true if the rule is deterministic (only one rule for the symbol).
     */
    protected boolean isDeterministicSymbolRule(String symbol) {
        return rules.get(symbol) != null && rules.get(symbol).size() == 1;
    }

    /**
     * Returns trie if the symbol is a terminal symbol (no rule available).
     */
    protected boolean isTerminal(String symbol) {
        return rules.get(symbol) == null;
    }

    /**
     * Parse the create file and create the corresponding rules.
     */
    public void parse(String grammarFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(grammarFile)));
            String line;
            while ((line = reader.readLine()) != null) {
                addRule(new Rule(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a rule to the grammar.
     */
    private void addRule(Rule rule) {
        if (rules.get(rule.symbol) == null) {
            List<Rule> rules4pred = new ArrayList<>();
            rules.put(rule.symbol, rules4pred);
        }
        rules.get(rule.symbol).add(rule);
    }

    // +++ GETTER/SETTER ++++++++++++++++++

    protected Map<String, List<Rule>> getRules() {
        return rules;
    }
}
