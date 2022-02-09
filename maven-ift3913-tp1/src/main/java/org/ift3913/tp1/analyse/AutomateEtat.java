package org.ift3913.tp1.analyse;

/**
 * Représente un automate style Moore pour l'analyse du code Java.
 * Le statut d'acceptance est relié à chacun des
 * <br>
 * L'automate fonctionne en analysant caractère-par-caractère l'entièreté du code.
 * Un {@link java.io.BufferedReader} est recommandé pour la lecture.
 *
 * @implNote cette interface permet d'implémenter facilement des automates servant à identifier l'appartenance d'un caractère à une structure
 *
 * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html">Java Documentation - Lexical Structure</a>
 * @author Pierre Janier Dubry et Rui Jie Liu
 */
public interface AutomateEtat {
    /**
     * Retourner si l'état actuel de l'automate est un état valide dont cet automate est tâché d'identifier.
     * @return si l'état actuel représente un état valide.
     */
    boolean valide();

    /**
     * Obtenir le prochain état de l'automate en soumettant le caractère suivant.
     * @param prochainChar le caractère suivant le curseur actuel.
     * @return l'état suivant de l'automate.
     */
    AutomateEtat prochainEtat(char prochainChar);
}
