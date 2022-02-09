package org.ift3913.tp1;

import org.ift3913.tp1.automates.*;

import java.io.*;
import java.util.*;

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

    //region ================================ CHAMPS ================================

    /**
     * Le chemin fourni en entrée par l'utilisateur, pointant vers un fichier valide.
     * <br>
     * Note : ce chemin ne garantit pas un fichier .java existe en destination.
     */
    private final File fichier;

    // TODO: utiliser ce BufferedReader pour lire le fichier à analyser
    private BufferedReader fileStream;

    private AutomateEtat etatAutomateCommentaires;
    //    private AutomateEtat etatAutomateStrings;
//    private AutomateTransition etatAutomateTernaire;
//    private AutomateIdentifiant automateIdentifiant;
    private final Set<String> identifiantsStructuresDeControle = new HashSet<>(
            Arrays.asList("if", "else", "while", "for", "switch"));
    // on sauvegarde les mots et leurs occurrences dans un hashmap qu'on videra pour compter
    // le nombre total de prédicat à la fin après l'analyse totale du fichier
    Map<String, Integer> occurenceIdentifiant = new HashMap<>();

    //endregion CHAMPS

    //region ================================ CONSTRUCTEUR ================================

    public AnalyseurJava(File fichier) throws FileNotFoundException {
        if (!fichier.exists())
            throw new FileNotFoundException("Le chemin fourni ne correspond pas à un fichier valide!");
        this.fichier = fichier;

    }

    //endregion CONSTRUCTEUR

    //region ================================ MÉTHODES ================================

    private void initialiser() {
        this.etatAutomateCommentaires = AutomateCommentaires.Initial;
//        this.etatAutomateStrings = AutomateStrings.Initial;
//        this.etatAutomateTernaire = AutomateTernaire.Initial;
//        this.automateIdentifiant = new AutomateIdentifiant();
    }

    public ResultatAnalyseFichier analyser() throws FileNotFoundException {
        initialiser();

        // Statistiques à analyser
        int lignesDeCode = 0;
        int lignesCommentaires = 0;
        int noeud = 0; // pour la complexité cyclomatique de McCabe

        try {
            fileStream = new BufferedReader(new FileReader(fichier));
            String currentLine;

            // lecture du contenu du fichier
            while ((currentLine = fileStream.readLine()) != null) {

                // réinitialisation : on remet les compteurs à zero pour compter la ligne suivante
                for (String id : identifiantsStructuresDeControle) {
                    occurenceIdentifiant.put(id, 0);
                }

                currentLine = currentLine.toLowerCase().strip();
                // Si la ligne est vide (e.g. seulement des espaces blancs), on ne va pas la compter
                if (currentLine.equals("")) continue;

                // Un verrou pour empêcher l'incrémentation de la lignesCommentaires
                // lorsqu'on est toujours sur la même ligne
                boolean isSameLine = false;

                // traiter la ligne caractère-par-caractère à l'intérieur de l'automate
                for (int i = 0; i < currentLine.length(); i++) {
                    char nextChar = currentLine.charAt(i);

                    // Obtenir le prochain état des automates
                    etatAutomateCommentaires = etatAutomateCommentaires.prochainEtat(nextChar);

                    if (!isSameLine && etatAutomateCommentaires.valide()) {
                        // cette ligne contient un commentaire
                        lignesCommentaires++;
                        isSameLine = true;
                    }

                }

                // on commence par analyser les identifiants spéciaux dans la phrase
                // si on trouve un mot spécial dans la phrase et qu'on n'est pas dans un commentaire
                // alors on ajoute son occurrence dans le hashmap et on incrémente son nombre
                for (String identifiant : identifiantsStructuresDeControle) {
                    // TODO l'id peut être présent plus d'une fois dans la ligne mais n'est compter qu'une seule fois avec contains(id)
                    if (currentLine.contains(identifiant) && !etatAutomateCommentaires.valide()) {
                        int nbMot = occurenceIdentifiant.get(identifiant);
                        occurenceIdentifiant.put(identifiant, nbMot + 1);
                    }
                }

                // récupère la somme des valeurs de toutes les clés.
                noeud += getSumValue(occurenceIdentifiant);

                // Une fois le traitement caractère-par-caractère pour la ligne est finie,
                // soumettre manuellement le caractère de retour de ligne à l'automate
                // Ce caractère est simplement un signal à l'automate qu'une fin de ligne est atteinte
                // et indépendant de la plateforme sur lequel le programme est exécuté.
                etatAutomateCommentaires = etatAutomateCommentaires.prochainEtat('\n');

                lignesDeCode++;
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Le fichier à analyser n'existe plus sur le disque!");
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

        // on récupère les chemins et on termine
        String extensionFichier = "." + Utils.obtenirExtensionFichier(fichier.toPath());
        String nomClasse = fichier.getName().replace(extensionFichier, "");
        return new ResultatAnalyseFichier(nomClasse, lignesDeCode, lignesCommentaires, fichier.toPath(), noeud + 1);
    }

    private int getSumValue(Map<String, Integer> map) {
        int n = 0;
        for (String id : map.keySet()) {
            n += map.get(id);
        }
        return n;
    }

    //endregion MÉTHODES
}
