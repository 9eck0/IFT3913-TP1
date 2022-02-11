package org.ift3913.tp1.analyse;

import org.ift3913.tp1.ResultatAnalyseFichier;
import org.ift3913.tp1.Utils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Un analyseur de codes Java pour un seul fichier de code.
 * <br>
 * Cet analyseur est agnostique sur le langage de programmation à analyser.
 * Cependant, il faut fournir à l'analyseur l'automate correspondant au code
 * à analyser pour produire des résultats sensibles.
 *
 * @author Pierre Janier Dubry et Rui Jie Liu
 */
public class AnalyseurJava {

    /**
     * <b>[WIP, non-implémenté] détection des classes imbriquées</b>
     * <br>
     * Définit le type de l'imbrication l'analyseur est actuellement à l'intérieur de.
     */
    enum TypeImbrication { Classe, Methode, StructureControle, Autre }

    //region ================================ CHAMPS ================================

    /**
     * Le chemin fourni en entrée par l'utilisateur, pointant vers un fichier valide.
     * <br>
     * Note : ce chemin ne garantit pas un fichier .java existe en destination.
     */
    private final File fichier;

    private BufferedReader fileStream;

    private AutomateEtat etatAutomateCommentaires;
    private AutomateEtat etatAutomateStrings;
    private AutomateEtat etatAutomateChar;
    private AutomateIdentifiant automateIdentifiant;
    private AutomateEtatMots etatAutomateClasses;
    private AutomateEtatMots etatAutomateMethodes;

    // Statistiques à analyser
    private int lignesDeCode = 0;
    private int lignesCommentaires = 0;
    private int noeud = 1; // pour la complexité cyclomatique de McCabe

    //endregion CHAMPS

    //region ================================ CONSTRUCTEUR ================================

    public AnalyseurJava(File fichier) throws FileNotFoundException {
        if (!fichier.exists())
            throw new FileNotFoundException("Le chemin fourni ne correspond pas à un fichier valide!");
        this.fichier = fichier;

    }

    //endregion CONSTRUCTEUR

    //region ================================ MÉTHODES UTILITAIRES ================================

    // Lire l'entièreté du fichier
    private String lireToutFichier(File fichier) throws FileNotFoundException {
        fileStream = new BufferedReader(new FileReader(fichier));
        return fileStream.lines().collect(Collectors.joining());
    }

    // Décomposer une ligne brute en plusieurs lignes à partir des caractères de séparation de codes (';', '{', '}')
    private List<String> decomposerLigne(String ligne) {
        ligne = ligne.replace(";", ";\n");
        ligne = ligne.replace("{", "{\n");
        ligne = ligne.replace("}", "}\n");
        return Arrays.stream(ligne.split("\n")).toList();
    }

    //endregion MÉTHODES UTILITAIRES

    //region ================================ MÉTHODES D'ANALYSE ================================

    /**
     * Cette méthode permet de remettre à zéro les états des automates lorsqu'on recommence une
     * nouvelle ligne alors on réinitialise tous les automates concernés par l'analyse
     */
    private void initialiser() {
        this.etatAutomateCommentaires = AutomateCommentaires.Initial;
        this.etatAutomateStrings = AutomateStrings.Initial;
        this.etatAutomateChar = AutomateChar.Initial;
        this.automateIdentifiant = new AutomateIdentifiant();
        this.etatAutomateClasses = AutomateClasses.Initial;
        this.etatAutomateMethodes = AutomateMethodes.Initial;

        // Réinitialiser les valeurs d'objectif
        this.lignesDeCode = 0;
        this.lignesCommentaires = 0;
        this.noeud = 1;
    }

    /**
     * Analyser le fichier spécifié lors de la construction de cet analyseur.
     * @return le résultat d'analyse du fichier
     * @throws FileNotFoundException si le fichier est inaccessible (e.g. n'existe plus)
     */
    public ResultatAnalyseFichier analyser() throws FileNotFoundException {
        // Réinitialiser toutes les variables d'analyse
        initialiser();

        try {
            fileStream = new BufferedReader(new FileReader(fichier));
            String ligneActuelle;

            // lecture du contenu du fichier
            while ((ligneActuelle = fileStream.readLine()) != null) {
                ligneActuelle = ligneActuelle.strip();

                // Si la ligne est vide (e.g. seulement des espaces blancs), on ne va pas la compter
                if (ligneActuelle.equals("")) continue;

                // Analyser caractère-par-caractère la ligne actuelle
                analyserParCaractere(ligneActuelle);

                /* Une fois le traitement caractère-par-caractère pour la ligne est finie,
                   soumettre manuellement le caractère de retour de ligne aux automates.
                   Ce caractère est simplement un signal à chaque automate qu'une fin de ligne est atteinte
                   et indépendant de la plateforme sur lequel le programme est exécuté. */
                etatAutomateCommentaires = etatAutomateCommentaires.prochainEtat('\n');
                etatAutomateStrings = etatAutomateStrings.prochainEtat('\n');
                etatAutomateChar = etatAutomateChar.prochainEtat('\n');
                automateIdentifiant.prochainCaractere('\n');

                lignesDeCode++;
            }
        } catch (IOException e) {
            System.out.println("Une erreur s'est produite lors de la lecture du fichier à analyser: " + e.getMessage());
            e.printStackTrace();
        }

        // fermeture du BufferedReader
        try {
            if (fileStream != null) fileStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // on récupère les résultats et on termine
        String extensionFichier = "." + Utils.obtenirExtensionFichier(fichier.toPath());
        String nomClasse = fichier.getName().replace(extensionFichier, "");
        return new ResultatAnalyseFichier(nomClasse, lignesDeCode, lignesCommentaires, fichier.toPath(), noeud);
    }

    // Analyser une ligne caractère-par-caractère
    private void analyserParCaractere(String ligneActuelle) {
        // Un verrou pour empêcher l'incrémentation de la statistique lignesCommentaires
        // lorsqu'on est toujours sur la même ligne
        boolean commentaireTrouve = false;

        // traiter la ligne caractère-par-caractère à l'intérieur de l'automate
        for (int i = 0; i < ligneActuelle.length(); i++) {
            char nextChar = ligneActuelle.charAt(i);

            // Obtenir le prochain état des automates Mealy
            /* les automates Mealy se réfèrent au caractère précédent pour leurs résultats,
               donc doivent être traités avant que les automates Moore passent au prochain caractère.*/
            String identifiant = automateIdentifiant.prochainCaractere(nextChar);

            // Tester les identifiants
            if (identifiant != null
                    && !etatAutomateCommentaires.valide()
                    && !etatAutomateStrings.valide()
                    && !etatAutomateChar.valide()) {

                etatAutomateClasses = etatAutomateClasses.prochainEtat(identifiant);
                etatAutomateMethodes = etatAutomateMethodes.prochainEtat(identifiant);

                // Structures de contrôle
                if (Utils.identifiantsStructuresDeControle.contains(identifiant)) noeud++;
            }

            // Obtenir le prochain état des automates Moore
            etatAutomateCommentaires = etatAutomateCommentaires.prochainEtat(nextChar);
            etatAutomateStrings = etatAutomateStrings.prochainEtat(nextChar);
            etatAutomateChar = etatAutomateChar.prochainEtat(nextChar);

            if (etatAutomateCommentaires.valide()) {
                if (!commentaireTrouve) {
                    // cette ligne contient un commentaire
                    lignesCommentaires++;
                    commentaireTrouve = true;
                }
            } else if (!etatAutomateStrings.valide() && !etatAutomateChar.valide()) {
                // code Java brut (pas un commentaire/String/char)

                if (nextChar == '?') {
                    // Identification opérateur ternaire
                    noeud++;
                }
                else if (Arrays.asList('{', '(', ')', '<', '>').contains(nextChar)) {
                    // Identification classes / méthodes
                    String nextCharStr = String.valueOf(nextChar);

                    etatAutomateClasses = etatAutomateClasses.prochainEtat(nextCharStr);
                    etatAutomateMethodes = etatAutomateMethodes.prochainEtat(nextCharStr);
                    if (etatAutomateClasses.valide()) {
                        // Classe valide trouvée
                        // Cette section sera utile lorsque la capabilité de traiter les classes imbriquées sera implémentée

                        //System.out.println("Classe valide trouvée @ " + ligneActuelle);
                    } else if (etatAutomateMethodes.valide()) {
                        // Méthode valide trouvée

                        //System.out.println("Méthode valide trouvée @ " + ligneActuelle);
                        noeud++;
                    }
                }
            }
        }
    }

    //endregion MÉTHODES D'ANALYSE
}
