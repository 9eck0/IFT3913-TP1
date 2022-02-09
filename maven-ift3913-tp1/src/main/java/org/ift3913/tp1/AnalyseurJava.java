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
    private AutomateEtat etatAutomateStrings;
    private AutomateEtat etatAutomateChar;
    private AutomateIdentifiant automateIdentifiant;
    // Identifiants servant pour dénoter le début d'un branchement
    private final Set<String> identifiantsStructuresDeControle = new HashSet<>(
            Arrays.asList("if", "while", "for", "switch"));

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
        this.etatAutomateStrings = AutomateStrings.Initial;
        this.etatAutomateChar = AutomateChar.Initial;
        this.automateIdentifiant = new AutomateIdentifiant();
    }

    public ResultatAnalyseFichier analyser() throws FileNotFoundException {
        initialiser();

        // Statistiques à analyser
        int lignesDeCode = 0;
        int lignesCommentaires = 0;
        int noeud = 1; // pour la complexité cyclomatique de McCabe

        try {
            fileStream = new BufferedReader(new FileReader(fichier));
            String ligneActuelle;

            // lecture du contenu du fichier
            while ((ligneActuelle = fileStream.readLine()) != null) {
                ligneActuelle = ligneActuelle.toLowerCase().strip();

                // Si la ligne est vide (e.g. seulement des espaces blancs), on ne va pas la compter
                if (ligneActuelle.equals("")) continue;

                // Un verrou pour empêcher l'incrémentation de la lignesCommentaires
                // lorsqu'on est toujours sur la même ligne
                boolean commentaireTrouve = false;

                // traiter la ligne caractère-par-caractère à l'intérieur de l'automate
                for (int i = 0; i < ligneActuelle.length(); i++) {
                    char nextChar = ligneActuelle.charAt(i);

                    // Obtenir le prochain état des automates Mealy
                    /* les automates Mealy se réfèrent au caractère précédent pour leurs résultats,
                       donc doivent être traités avant que les automates Moore passent au prochain caractère.*/
                    String identifiant = automateIdentifiant.prochainCaractere(nextChar);

                    // Tester identifiant structure de contrôle
                    if (identifiant != null
                            && identifiantsStructuresDeControle.contains(identifiant)
                            && !etatAutomateCommentaires.valide()
                            && !etatAutomateStrings.valide()
                            && !etatAutomateChar.valide()) noeud++;

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

                        // Identification opérateur ternaire
                        if (nextChar == '?') noeud++;
                    }
                }

                /* Une fois le traitement caractère-par-caractère pour la ligne est finie,
                   soumettre manuellement le caractère de retour de ligne aux automates
                   Ce caractère est simplement un signal à chaque automate qu'une fin de ligne est atteinte
                   et indépendant de la plateforme sur lequel le programme est exécuté. */
                etatAutomateCommentaires = etatAutomateCommentaires.prochainEtat('\n');
                etatAutomateStrings = etatAutomateStrings.prochainEtat('\n');
                etatAutomateChar = etatAutomateChar.prochainEtat('\n');
                automateIdentifiant.prochainCaractere('\n');

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
        return new ResultatAnalyseFichier(nomClasse, lignesDeCode, lignesCommentaires, fichier.toPath(), noeud);
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
