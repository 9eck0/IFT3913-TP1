package org.ift3913.tp1.analyse;

/**
 * Cette Enum définit le comportement d'un automate fini servant à analyser caractère-par-caractère
 * les commentaires dans du code Java.
 * <br><br>
 * Java supporte trois types de commentaires :
 * <ul>
 *     <li>Monoligne: <b>&#47&#47</b>...</li>
 *     <li>Bloc multi-lignes : <b>&#47*</b>...<b>*&#47</b></li>
 *     <li>Javadoc: <b>&#47**</b>...<b>*&#47</b></li>
 * </ul>
 * Il est à noter que ces trois types de commentaires n'entrent pas en conflict entre eux,
 * sauf pour la clause fermante des commentaires multilignes et Javadocs (<b>*&#47</b>),
 * qui ferme tous les deux types de commentaires multilignes déjà ouverte.
 * <br><br>
 * Le moyen de détection des commentaires dans les fichiers de code se font à l'aide
 * d'un automate à états finis. Cet automate recense trois modes différents,
 * permettant de détecter la présence des trois types de commentaires Java :
 * <ul>
 *     <li>Monoligne: si <b>&#47&#47</b> détecté, transitionner vers un état séparé
 *     qui attend un retour de ligne ("\n").</li>
 *     <li>Bloc multilignes: si <b>&#47*</b> détecté, transitionner vers un état séparé
 *     qui attend la clause de fermeture (<b>*&#47</b>). Fermer tous les commentaires multilignes et Javadocs ouverts.</li>
 *     <li>Javadocs: si <b>&#47**</b> détecté, transitionner vers un état séparé
 *     qui attend la clause de fermeture (<b>*&#47</b>). Fermer tous les commentaires multilignes et Javadocs ouverts.</li>
 * </ul>
 * <p>
 * L'implémentation concrète est effectuée en groupant les blocs multilignes et les Javadocs
 * en un seul mode, pour réduire la complexité tout en restant à l'intérieur de la
 * portée de ce projet.
 * <br><br>
 * Pour simplifier la spécification, <b>cet automate n'accepte que les caractères {@code \n} comme retour de ligne.</b>
 * Ainsi, c'est au rôle de l'utilisateur de cet automate de fournir ce retour de ligne ({@code \n}) à chaque fois
 * qu'une fin de ligne est rendue.
 *
 * @author Pierre Janier Dubry et Rui Jie Liu
 */
public enum AutomateCommentaires implements AutomateEtat {

    /**
     * Valeur : tout caractère
     * <br>
     * État initial, aucun commentaire.
     */
    Initial {
        public boolean valide() {
            return false;
        }

        @Override
        public AutomateCommentaires prochainEtat(char prochainChar) {
            if (prochainChar == '/') {
                return DebutCommentaire;
            } else {
                return Initial;
            }
        }
    },

    /**
     * Valeur: <b>&#47</b>
     * <br>
     * Début potentiel d'un commentaire.
     */
    DebutCommentaire {
        public boolean valide() {
            return false;
        }

        @Override
        public AutomateCommentaires prochainEtat(char prochainChar) {
            if (prochainChar == '/') {
                return Monoligne;
            } else if (prochainChar == '*') {
                return Bloc;
            } else {
                return Initial;
            }
        }
    },

    /**
     * Valeur: <b>&#47&#47</b> ...
     * <br>
     * Commentaire monoligne détecté.
     */
    Monoligne {
        public boolean valide() {
            return true;
        }

        @Override
        public AutomateCommentaires prochainEtat(char prochainChar) {
            if (prochainChar == '\n') {
                return Initial;
            } else if (prochainChar == '/') {
                return DebutMonoligneEtBloc;
            } else {
                return Monoligne;
            }
        }
    },

    /**
     * Valeur: <b>&#47*</b> ...
     * <br>
     * Commentaire multiligne (bloc ou Javadoc) détecté.
     */
    Bloc {
        public boolean valide() {
            return true;
        }

        @Override
        public AutomateCommentaires prochainEtat(char prochainChar) {
            if (prochainChar == '/') {
                return DebutBlocEtMonoligne;
            } else if (prochainChar == '*') {
                return FinBloc;
            } else {
                return Bloc;
            }
        }
    },

    /**
     * Valeur: <b>&#47*</b> ... <b>*</b>
     * <br>
     * Potentiellement la fin d'un bloc multilignes, ou le début d'un Javadoc.
     */
    FinBloc {
        public boolean valide() {
            return true;
        }

        @Override
        public AutomateCommentaires prochainEtat(char prochainChar) {
            if (prochainChar == '/') {
                return Initial;
            } else if (prochainChar == '*') {
                return FinBloc;
            } else {
                return Bloc;
            }
        }
    },

    /**
     * Valeur: <b>&#47&#47</b> ... <b>&#47</b>
     * <br>
     * Début potentiel d'un commentaire bloc/javadoc multilignes à l'intérieur d'un commentaire monoligne.
     */
    DebutMonoligneEtBloc {
        public boolean valide() {
            return true;
        }

        @Override
        public AutomateCommentaires prochainEtat(char prochainChar) {
            if (prochainChar == '\n') {
                return Initial;
            } else if (prochainChar == '*') {
                return MonoligneEtBloc;
            } else {
                return Monoligne;
            }
        }
    },

    /**
     * Valeur: <b>&#47&#47</b> ... <b>&#47*</b> ...
     * <br>
     * Un commentaire monoligne suivi d'un commentaire bloc/javadoc multilignes.
     */
    MonoligneEtBloc {
        public boolean valide() {
            return true;
        }

        @Override
        public AutomateCommentaires prochainEtat(char prochainChar) {
            if (prochainChar == '\n') {
                return Bloc;
            } else if (prochainChar == '*') {
                return FinMonoligneEtBloc;
            } else {
                return MonoligneEtBloc;
            }
        }
    },

    /**
     * Valeur: <b>&#47&#47</b> ... <b>&#47*</b> ... <b>*</b>
     * <br>
     * Fin potentiel d'un commentaire bloc/Javadoc multilignes à l'intérieur d'un commentaire monoligne.
     */
    FinMonoligneEtBloc {
        public boolean valide() {
            return true;
        }

        @Override
        public AutomateCommentaires prochainEtat(char prochainChar) {
            if (prochainChar == '\n') {
                return Bloc;
            } else if (prochainChar == '/') {
                return Monoligne;
            } else if (prochainChar == '*') {
                return FinMonoligneEtBloc;
            } else {
                return MonoligneEtBloc;
            }
        }
    },

    /**
     * Valeur: <b>&#47*</b> ... <b>&#47</b>
     * <br>
     * Début potentiel d'un commentaire monoligne à l'intérieur d'un commentaire bloc/javadoc multilignes.
     */
    DebutBlocEtMonoligne {
        public boolean valide() {
            return true;
        }

        @Override
        public AutomateCommentaires prochainEtat(char prochainChar) {
            if (prochainChar == '/') {
                return BlocEtMonoligne;
            } else {
                return Bloc;
            }
        }
    },

    /**
     * Valeur: <b>&#47*</b> ... <b>&#47&#47</b> ...
     * <br>
     * Un commentaire monoligne à l'intérieur d'un commentaire bloc/javadoc multilignes.
     */
    BlocEtMonoligne {
        public boolean valide() {
            return true;
        }

        @Override
        public AutomateCommentaires prochainEtat(char prochainChar) {
            if (prochainChar == '\n') {
                return Bloc;
            } else if (prochainChar == '*') {
                return FinBlocEtMonoligne;
            } else {
                return BlocEtMonoligne;
            }
        }
    },

    /**
     * Valeur: <b>&#47*</b> ... <b>&#47&#47</b> ... <b>*</b>
     * <br>
     * Fin potentiel d'un commentaire bloc/Javadoc multilignes à l'intérieur d'un commentaire monoligne.
     */
    FinBlocEtMonoligne {
        public boolean valide() {
            return true;
        }

        @Override
        public AutomateCommentaires prochainEtat(char prochainChar) {
            if (prochainChar == '\n') {
                return Bloc;
            } else if (prochainChar == '/') {
                return Monoligne;
            } else if (prochainChar == '*') {
                return FinBlocEtMonoligne;
            } else {
                return BlocEtMonoligne;
            }
        }
    }
}
