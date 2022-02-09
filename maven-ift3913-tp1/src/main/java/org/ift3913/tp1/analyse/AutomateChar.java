package org.ift3913.tp1.analyse;

/**
 * Représente un automate pour l'analyse des littéraux de caractères.
 * Note que cet automate ne vérifie pas la contrainte de longueur du littéral de caractère.
 * <br><br>
 * L'automate fonctionne en analysant caractère-par-caractère l'entièreté du code.
 * Un {@link java.io.BufferedReader} est recommandé pour la lecture.
 *
 * @author Pierre Janier Dubry et Rui Jie Liu
 */
public enum AutomateChar implements AutomateEtat {
    /**
     * Valeur : tout caractère
     * <br>
     * État initial, aucun littéral de caractère.
     */
    Initial {
        public boolean valide() { return false; }

        public AutomateChar prochainEtat(char prochainChar) {
            if (prochainChar == '\'') return Char;
            else return Initial;
        }
    },

    /**
     * Valeur : <b>{@code '}</b>...
     * <br>
     * À l'Intérieur d'un littéral de caractère.
     */
    Char {
        public boolean valide() { return true; }

        public AutomateChar prochainEtat(char prochainChar) {
            if (prochainChar == '\'' || prochainChar == '\n') return Initial;
            else return Char;
        }
    }
}
