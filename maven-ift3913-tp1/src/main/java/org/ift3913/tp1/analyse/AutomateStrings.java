package org.ift3913.tp1.analyse;

/**
 * Représente un automate pour l'analyse des littéraux de chaînes de caractères à l'intérieur du code Java.
 * Ne garantit pas que le string est valide (e.g. à l'intérieur d'un commentaire, ou la séquence
 * d'échappement est invalide).
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
            if (prochainChar == '"') return StringLitteral;
            else return Initial;
        }
    },

    StringLitteral {
        public boolean valide() {
            return true;
        }

        @Override
        public AutomateStrings prochainEtat(char prochainChar) {
            if (prochainChar == '"' || prochainChar == '\n') return Initial;
            else if (prochainChar == '\\') return DebutEscapeSequence;
            else return StringLitteral;
        }
    },

    DebutEscapeSequence {
        public boolean valide() {
            return true;
        }

        @Override
        public AutomateStrings prochainEtat(char prochainChar) {
            if (prochainChar == '\n') return Initial;
            else return EscapedChar;
        }
    },

    EscapedChar {
        public boolean valide() {
            return true;
        }

        @Override
        public AutomateStrings prochainEtat(char prochainChar) {
            if (prochainChar == '\n') return Initial;
            else return StringLitteral;
        }
    }
    // TODO: construire l'automate string
}
