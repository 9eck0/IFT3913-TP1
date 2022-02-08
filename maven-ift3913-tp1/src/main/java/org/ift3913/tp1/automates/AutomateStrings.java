package org.ift3913.tp1.automates;

/**
 * Représente un automate pour l'analyse des littéraux de chaînes de caractères à l'intérieur du code Java.
 * <br><br>
 * L'automate fonctionne en analysant caractère-par-caractère l'entièreté du code.
 * Un {@link java.io.BufferedReader} est recommandé pour la lecture.
 *
 * @author Pierre Janier Dubry et Rui Jie Liu
 */
public enum AutomateStrings implements AutomateEtat {
    /**
     * Valeur : tout caractère
     * <br>
     * État initial, aucun string.
     */
    Initial {
        public boolean valide() {
            return false;
        }

        @Override
        public AutomateStrings prochainEtat(char prochainChar) {
            return null;
        }
    }
    // TODO: construire l'automate string
}
