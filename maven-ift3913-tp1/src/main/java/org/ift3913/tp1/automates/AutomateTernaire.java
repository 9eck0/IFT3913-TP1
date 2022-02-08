package org.ift3913.tp1.automates;

/**
 * Représente un automate pour l'analyse des opérateurs ternaires à l'intérieur du code Java.
 * <br><br>
 * L'automate fonctionne en analysant caractère-par-caractère l'entièreté du code.
 * Un {@link java.io.BufferedReader} est recommandé pour la lecture.
 *
 * @author Pierre Janier Dubry et Rui Jie Liu
 */
public enum AutomateTernaire implements AutomateTransition {
    /**
     * Valeur : tout caractère
     * <br>
     * État initial, aucun opérateur ternaire.
     */
    Initial {
        public ResultatTransitionAutomate prochainEtat(char prochainChar) {
            return null;
        }
    }
    // TODO: construire l'automate ternaire
}
