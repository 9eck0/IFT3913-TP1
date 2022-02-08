package org.ift3913.tp1.automates;

/**
 * Enum interne de l'{@linkplain AutomateIdentifiant}. Ne pas directement référencer.
 * @author Pierre Janier Dubry et Rui Jie Liu
 */
enum AutomateIdentifiantInterne implements AutomateTransition {
    /**
     * Valeur: tout caractère non-identifiant
     * <br>
     * État initial, aucun identifiant.
     */
    Initial {
        public ResultatTransitionAutomate prochainEtat(char prochainChar) {
            if (Character.isJavaIdentifierStart(prochainChar)) return new ResultatTransitionAutomate(DebutIdentifiant, false);
            else if (Character.isJavaIdentifierPart(prochainChar)) return new ResultatTransitionAutomate(Autres, false);
            else return new ResultatTransitionAutomate(Initial, false);
        }
    },

    /**
     * Valeur : tout caractère de début d'identifiant
     * <br>
     * Début d'un identifiant Java.
     */
    DebutIdentifiant {
        public ResultatTransitionAutomate prochainEtat(char prochainChar) {
            if (Character.isJavaIdentifierPart(prochainChar)) return new ResultatTransitionAutomate(Identifiant, false);
            else return new ResultatTransitionAutomate(Initial, true);
        }
    },

    /**
     * Valeur : tout caractère d'identifiant
     * <br>
     * Un identifiant Java.
     */
    Identifiant {
        public ResultatTransitionAutomate prochainEtat(char prochainChar) {
            if (Character.isJavaIdentifierPart(prochainChar)) return new ResultatTransitionAutomate(Identifiant, false);
            else return new ResultatTransitionAutomate(Initial, true);
        }
    },

    /**
     * Valeur : tout caractère identifiant Java
     * <br>
     * Pas un identifiant valide.
     */
    Autres {
        public ResultatTransitionAutomate prochainEtat(char prochainChar) {
            if (Character.isJavaIdentifierPart(prochainChar)) return new ResultatTransitionAutomate(Autres, false);
            else return new ResultatTransitionAutomate(Initial, false);
        }
    }
}

/**
 * Représente un automate pour l'identification des identifiants Java à l'intérieur du code Java.
 * Un identifiant ainsi trouvé n'est valide que lorsqu'il est un identifiant indépendant,
 * c'est-à-dire qu'il n'est pas entouré par des chiffres et des "lettres Java" (un caractère
 * Unicode accepté par la fonction {@link Character#isJavaIdentifierPart(char)}).
 * <br><br>
 * L'automate fonctionne en analysant caractère-par-caractère l'entièreté du code.
 * Un {@link java.io.BufferedReader} est recommandé pour la lecture.
 * <br><br>
 * Note que cet automate ne prend pas en compte de la validité de l'identifiant trouvé
 * (e.g. s'il fait partie d'un string)
 * Pour assurer la validité, il faut combiner le résultat des automates d'analyse de
 * commentaires et de littéraux de strings.
 *
 * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html">Java Documentation - Lexical Structure</a>
 * @author Pierre Janier Dubry et Rui Jie Liu
 */
public class AutomateIdentifiant {
    private StringBuilder identifiant = new StringBuilder();
    private AutomateIdentifiantInterne etatActuel = AutomateIdentifiantInterne.Initial;

    /**
     * Faire avancer l'automate d'un caractère dans le code, en fournissant le prochain caractère
     * à cette fonction.
     * <br>
     * Si un identifiant valide a été identifié, prochainCaractere retournera l'identifiant sous
     * forme d'une {@linkplain String chaîne de caractères}. Sinon, cette méthode retournera
     * {@code null}.
     * @param prochainChar le prochain caractère du code
     * @return l'identifiant trouvé, ou {@code null} si aucun identifiant n'a été trouvé encore.
     */
    public String prochainCaractere(char prochainChar) {

        ResultatTransitionAutomate resultat = etatActuel.prochainEtat(prochainChar);
        identifiant.append(prochainChar);

        if (resultat.valide()) {
            String identifiantValide = identifiant.toString();
            // Réinitialiser le StringBuilder de l'identifiant
            identifiant.setLength(0);
            return identifiantValide;
        } else {
            return null;
        }
    }
}
