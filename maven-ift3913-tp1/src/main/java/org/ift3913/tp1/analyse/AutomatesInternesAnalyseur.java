package org.ift3913.tp1.analyse;

import org.ift3913.tp1.Utils;

/**
 * <b>Interface automate à l'usage interne à la classe AnalyseurJava. Ne pas référencier.</b>
 * <br>
 * Définit un automate style Moore opérant sur les mots (au lieu des caractères).
 *
 * @author Pierre Janier Dubry et Rui Jie Liu
 */
interface AutomateEtatMots {
    /**
     * @return si l'état actuel représente un état valide du système en cours d'analyse
     */
    boolean valide();

    /**
     * Passer à l'état prochain de l'automate.
     * @param prochainMot le prochain mot de la suite du système
     * @return le prochain état, dépendamment du mot fourni
     */
    AutomateEtatMots prochainEtat(String prochainMot);
}

/**
 * <b>Automate à l'usage interne à la classe AnalyseurJava. Ne pas référencier.</b>
 * <br>
 * Sert à analyser la signature d'une classe Java.
 *
 * @author Pierre Janier Dubry et Rui Jie Liu
 */
enum AutomateClasses implements AutomateEtatMots {
    Initial {
        public boolean valide() {
            return false;
        }

        public AutomateClasses prochainEtat(String prochainMot) {
            if (prochainMot.equals("record")) return DeclarationRecord;
            else if (Utils.identifiantsDeclarationClasses.contains(prochainMot)) return DeclarationClasse;
            else return Initial;
        }
    },

    DeclarationClasse {
        public boolean valide() {
            return false;
        }

        public AutomateClasses prochainEtat(String prochainMot) {
            if (Utils.estMotCleJava(prochainMot) || prochainMot.equals("{")) return Initial;
            else return NomClasse;
        }
    },

    NomClasse {
        public boolean valide() {
            return false;
        }

        public AutomateClasses prochainEtat(String prochainMot) {
            if (prochainMot.equals("{")) return DebutClasse;
            else if (Utils.identifiantsImplementationClasses.contains(prochainMot)) return Implementation;
            else return Initial;
        }
    },

    DeclarationRecord {
        public boolean valide() {
            return false;
        }

        public AutomateClasses prochainEtat(String prochainMot) {
            if (Utils.estMotCleJava(prochainMot) || prochainMot.equals("{")) return Initial;
            else return NomRecord;
        }
    },

    NomRecord {
        public boolean valide() {
            return false;
        }

        public AutomateClasses prochainEtat(String prochainMot) {
            if (prochainMot.equals("(")) return ArgumentsRecord;
            else return Initial;
        }
    },

    ArgumentsRecord {
        public boolean valide() {
            return false;
        }

        public AutomateClasses prochainEtat(String prochainMot) {
            if (prochainMot.equals(")")) return FinArgumentsRecord;
            else return ArgumentsRecord;
        }
    },

    FinArgumentsRecord {
        public boolean valide() {
            return false;
        }

        public AutomateClasses prochainEtat(String prochainMot) {
            if (prochainMot.equals("{")) return DebutClasse;
            else return Initial;
        }
    },

    Implementation {
        public boolean valide() {
            return false;
        }

        public AutomateClasses prochainEtat(String prochainMot) {
            if (prochainMot.equals("{")) return DebutClasse;
            else if (Utils.identifiantsAutres.contains(prochainMot)) return Initial;
            else return Implementation;
        }
    },

    DebutClasse {
        public boolean valide() {
            return true;
        }

        public AutomateClasses prochainEtat(String prochainMot) {
            if (Utils.identifiantsDeclarationClasses.contains(prochainMot)) return NomClasse;
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
            if (Utils.identifiantsModificateursMethodes.contains(prochainMot)) return Modificateurs;
            else if (!Utils.estMotCleJava(prochainMot)) return NomMethode;
            else return Initial;
        }
    },

    Modificateurs {
        public boolean valide() {
            return false;
        }

        public AutomateMethodes prochainEtat(String prochainMot) {
            if (Utils.identifiantsModificateursMethodes.contains(prochainMot)) return Modificateurs;
            else return Type;
        }
    },

    Type {
        public boolean valide() {
            return false;
        }

        int nestedTypes = 0;
        public AutomateMethodes prochainEtat(String prochainMot) {
            if (Utils.identifiantsDeclarationClasses.contains(prochainMot)) return Initial;
            else if (prochainMot.equals("<")) {
                nestedTypes++;
                return this;
            }
            else if (prochainMot.equals(">") && nestedTypes > 0) {
                nestedTypes--;
            }
            if (nestedTypes > 0) return this;
            else if (Utils.identifiantValide(prochainMot)) return NomMethode;
            else return Initial;
        }
    },

    NomMethode {
        public boolean valide() {
            return false;
        }

        public AutomateMethodes prochainEtat(String prochainMot) {
            if (prochainMot.equals("(")) return Arguments;
            else return Initial;
        }
    },

    Arguments {
        public boolean valide() {
            return false;
        }

        public AutomateMethodes prochainEtat(String prochainMot) {
            if (prochainMot.equals(")")) return FinArguments;
            else if (prochainMot.equals("{")) return Initial;
            else return Arguments;
        }
    },

    FinArguments {
        public boolean valide() {
            return false;
        }

        public AutomateMethodes prochainEtat(String prochainMot) {
            if (prochainMot.equals("{")) return DebutMethode;
            else return Initial;
        }
    },

    DebutMethode {
        public boolean valide() {
            return true;
        }

        public AutomateMethodes prochainEtat(String prochainMot) {
            return Initial;
        }
    }
}