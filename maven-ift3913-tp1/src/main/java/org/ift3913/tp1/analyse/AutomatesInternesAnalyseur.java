package org.ift3913.tp1.analyse;

import org.ift3913.tp1.Utils;

interface AutomateEtatMots {
    boolean valide();
    AutomateEtatMots prochainEtat(String prochainMot);
}

/**
 * <b>Automate à l'usage interne à la classe AnalyseurJava. Ne pas référencier.</b>
 * <br>
 * Sert à analyser la signature d'une classe Java.
 */
enum AutomateClasses implements AutomateEtatMots {
    Initial {
        public boolean valide() {
            return false;
        }

        public AutomateClasses prochainEtat(String prochainMot) {
            if (Utils.identifiantsClasses.contains(prochainMot)) return NomClasse;
            else return Initial;
        }
    },

    NomClasse {
        public boolean valide() {
            return false;
        }

        public AutomateClasses prochainEtat(String prochainMot) {
            if (prochainMot.equals("{")) return DebutClasse;
            else return Implementation;
        }
    },

    Implementation {
        public boolean valide() {
            return false;
        }

        public AutomateClasses prochainEtat(String prochainMot) {
            if (prochainMot.equals("{")) return DebutClasse;
            else return Implementation;
        }
    },

    DebutClasse {
        public boolean valide() {
            return true;
        }

        public AutomateClasses prochainEtat(String prochainMot) {
            if (Utils.identifiantsClasses.contains(prochainMot)) return NomClasse;
            else return Initial;
        }
    }
}

/**
 * <b>Automate à l'usage interne à la classe AnalyseurJava. Ne pas référencier.</b>
 * <br>
 * Sert à analyser la signature d'une méthode Java.
 */
enum AutomateMethodes implements AutomateEtatMots {
    Initial {
        public boolean valide() {
            return false;
        }

        public AutomateMethodes prochainEtat(String prochainMot) {
            if (Utils.identifiantsMethodes.contains(prochainMot)) return Modificateurs;
            else if (prochainMot.equals("void")) return NomMethode;
            else return Initial;
        }
    },

    Modificateurs {
        public boolean valide() {
            return false;
        }

        public AutomateMethodes prochainEtat(String prochainMot) {
            return null;
        }
    },

    Type {
        public boolean valide() {
            return false;
        }

        public AutomateMethodes prochainEtat(String prochainMot) {
            return null;
        }
    },

    NomMethode {
        public boolean valide() {
            return false;
        }

        public AutomateMethodes prochainEtat(String prochainMot) {
            return null;
        }
    },

    Arguments {
        public boolean valide() {
            return false;
        }

        public AutomateMethodes prochainEtat(String prochainMot) {
            return null;
        }
    },

    DebutMethode {
        public boolean valide() {
            return true;
        }

        public AutomateMethodes prochainEtat(String prochainMot) {
            return null;
        }
    }
}