package org.ift3913.tp1.automates;

/**
 * Représente un automate style Mealy pour l'analyse du code Java.
 * <br>
 * L'automate fonctionne en analysant caractère-par-caractère l'entièreté du code.
 * Un {@link java.io.BufferedReader} est recommandé pour la lecture.
 *
 * @implNote cette interface permet d'implémenter facilement des automates servant à identifier l'existence d'une structure.
 *
 * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html">Java Documentation - Lexical Structure</a>
 * @author Pierre Janier Dubry et Rui Jie Liu
 */
public interface AutomateTransition {
    /**
     * Obtenir le prochain état de l'automate en soumettant le caractère suivant.
     * @param prochainChar le caractère suivant le curseur actuel.
     * @return l'état suivant de l'automate, ainsi que l'acceptation de la transition.
     */
    ResultatTransitionAutomate prochainEtat(char prochainChar);
}

