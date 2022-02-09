package org.ift3913.tp1;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Programme d'analyse statistique pour mesurer la complexité de la sous-documentation d'un projet
 * Java.
 *
 * @author Pierre Janier Dubry et Rui Jie Liu
 */
public class TP1 {

    //region ================================ MAIN ================================

    public static void main(String[] args) {

        // Analyse et traitement des arguments en entrée
        File cheminBase = null;

        // TODO retirer la boucle for et récupérer uniquement le premier argument
        for (String arg : args) {
            // Vérification de la validité du chemin donné. Aucune analyse du type de fichier ici.
            // Il n'est pas nécessaire de vérifier qu'il s'agisse bien du dossier racine.
            // S'il y a un seul argument alors la sortie par défaut est le même que celui du premier argument
            File potentialPath = new File(arg);
            if (potentialPath.exists()) {
                cheminBase = potentialPath;
            }
            // TODO: refaire les entrées des arguments d'appels pour tenir en compte des "pointeurs" aux arguments
        }

        if (cheminBase == null) {
            System.out.println("Veuillez fournir un chemin valide à un paquet ou fichier Java!");
            System.exit(3);     // Cohérent avec code erreur Win32: 3=ERROR_PATH_NOT_FOUND
        }

        // Lancer la partie logique du programme

        if (cheminBase.isDirectory()) {
            // Analyse paquet : parcours du contenu du dossier
            Collection<ResultatAnalysePaquet> resultats = analyserPaquet(cheminBase);

            // on crée le fichier csv à partir des résultats analysés et on crée le fichier de sortie
            String cheminFichierCsvClasses = dossierActuel + File.separator + NOM_CSVCLASSES;
            String cheminFichierCsvPaquets = dossierActuel + File.separator + NOM_CSVPAQUETS;
            creerCsvClasses(resultats, new File(cheminFichierCsvClasses));
            creerCsvPaquets(resultats, new File(cheminFichierCsvPaquets));
        } else {
            // Analyse fichier simple : pas d'architecture à parcourir
            ResultatAnalyseFichier resultat = analyserFichier(cheminBase);

            if (resultat == null) {
                // Référer à analyserFichier()
                System.out.println("Une erreur s'est produite lors de l'analyse du fichier" +
                        " et le programme ne peut continuer.\nProblème: 0x3 ERROR_FILE_NOT_FOUND");
                System.exit(2);     // Cohérent avec code erreur Win32: 3=ERROR_FILE_NOT_FOUND
            }

            String cheminFichierCsv = dossierActuel + File.separator + NOM_CSVCLASSES;
            creerCsvClasses(resultat, Path.of(cheminFichierCsv));
        }

        System.out.println("fin");

    }

    //endregion MAIN

    //region ================================ CHAMPS ================================

    /**
     * Ensemble de toutes les extensions ce programme supporterait l'analyse de commentaires sur.
     * Veuillez écrire les extensions à supporter en minuscules.
     */
    static final String[] EXTENSIONS_FICHIERS = {"java"};

    /**
     * Le dossier dans lequel le programme fut lancé
     */
    static final String dossierActuel = System.getProperty("user.dir");
    /**
     * Nom par défaut du fichier CSV de classes à générer
     */
    static final String NOM_CSVCLASSES = "classes.csv";
    /**
     * Nom par défaut du fichier CSV de paquets à générer
     */
    static final String NOM_CSVPAQUETS = "paquets.csv";
    // En-têtes des fichiers CSV
    static final String ENTETE_CSVCLASSES = "chemin,class,classe_LOC,classe_CLOC,classe_DC,complexiteCyclomatiqueClasse";
    static final String ENTETE_CSVPAQUETS = "chemin,paquet,paquet_LOC,paquet_CLOC,paquet_DC,complexiteCyclomatiquePaquet";

    //endregion CHAMPS

    //region ================================ FONCTIONS ================================

    static ResultatAnalyseFichier analyserFichier(File fichier) {
        try {
            AnalyseurJava analyseur = new AnalyseurJava(fichier);
            return analyseur.analyser();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cette méthode analyse tous les fichiers à l'intérieur du dossier/paquet spécifié
     * et recherche les fichiers dont leurs extensions sont supportées.
     *
     * @param dossierPaquet le fichier du dossier.
     */
    static Collection<ResultatAnalysePaquet> analyserPaquet(File dossierPaquet) {
        // Conversion des extensions supportées en un ensemble permettant d'éviter les éléments en
        // double, de convertir tout en minuscule, et de faire appel à la méthode contains()
        Set<String> ExtensionsSupportees = new HashSet<>(Arrays.stream(EXTENSIONS_FICHIERS)
                .map(String::toLowerCase).toList());

        // Ce dictionnaire contiendra tous les résultats des analyses de commentaires sur
        // les fichiers individuellement, groupés par paquets
        Hashtable<String, List<ResultatAnalyseFichier>> resultatsTries = new Hashtable<>();

        // peupler la collection resultatsTries en analysant chaque fichier de code
        try {
            /*
            Utiliser la méthode du stream Java pour traverser tous les fichiers .java à partir
            du dossier racine. Dans un premier temps, on traverse l'architecture, puis on filtre
            les fichiers et enfin on l'analyse. Après l'analyse, on sauvegarde les résultats dans
            la collection resulats.
            */
            Files.walk(dossierPaquet.toPath())
                    .filter(cheminFichier -> ExtensionsSupportees.contains(Utils.obtenirExtensionFichier(cheminFichier)))
                    .forEach(cheminFichier -> {
                        // Pour chaque fichier à analyser, on veut le stocker dans le dictionnaire/hashtable sous le paquet correspondant
                        String nomPaquet = Utils.obtenirNomPaquet(dossierPaquet.toPath(), cheminFichier);

                        // Vérifier si le dictionnaire/hashtable contient le nom du paquet comme entrée
                        if (!resultatsTries.containsKey(nomPaquet)) {
                            // si non, initialiser une liste pour contenir les résultats d'analyse des classes du paquet
                            resultatsTries.put(nomPaquet, new LinkedList<>());
                        }

                        ResultatAnalyseFichier resultatAnalyseFichier = analyserFichier(cheminFichier.toFile());
                        if (resultatAnalyseFichier != null)
                            resultatsTries.get(nomPaquet).add(resultatAnalyseFichier);
                    });

        } catch (IOException e) {
            System.err.println("Une erreur s'est produite lors du parcours des dossiers à la recherche des fichiers Java: " + e.getMessage());
            e.printStackTrace();
        }

        Collection<ResultatAnalysePaquet> resultats = new HashSet<>();

        for (String nomPaquet : resultatsTries.keySet()) {
            List<ResultatAnalyseFichier> fichiersDuPaquet = resultatsTries.get(nomPaquet);

            String cheminPaquet = fichiersDuPaquet.get(0).cheminFichier().getParent().toString();
            ResultatAnalysePaquet resultatPaquet = new ResultatAnalysePaquet(fichiersDuPaquet, nomPaquet, cheminPaquet);
            resultats.add(resultatPaquet);
        }

        return resultats;
    }

    /**
     * Prend les résultats d'une analyse de paquets et extrait les informations pertinentes
     * aux statistiques de classes dans un fichier CSV de chemin spécifié.
     *
     * @param resultat   le résultat d'une analyse d'un seul fichier
     * @param fichierCsv le chemin complet, incluant le nom du fichier CSV, à générer
     */
    static void creerCsvClasses(ResultatAnalyseFichier resultat, Path fichierCsv) {
        try {
            BufferedWriter csvWriter = new BufferedWriter(new FileWriter(fichierCsv.toString()));

            csvWriter.write(ENTETE_CSVCLASSES);
            csvWriter.newLine();
            csvWriter.write(resultat.toString());

            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prend les résultats d'une analyse de paquets et extrait les informations pertinentes
     * aux statistiques de classes dans un fichier CSV de chemin spécifié.
     *
     * @param resultats  le résultat d'une analyse d'un paquet
     * @param fichierCsv le chemin complet, incluant le nom du fichier CSV, à générer
     */
    static void creerCsvClasses(Collection<ResultatAnalysePaquet> resultats, File fichierCsv) {
        try {
            BufferedWriter csvWriter = new BufferedWriter(new FileWriter(fichierCsv));

            // Écriture de la ligne d'en-tête
            csvWriter.write(ENTETE_CSVCLASSES);

            // Écriture des lignes de statistiques de classes
            for (ResultatAnalysePaquet paquet : resultats) {
                for (ResultatAnalyseFichier resultatFichier : paquet.resultatsFichiers()) {
                    // Pour chaque classe à l'intérieur du paquet :
                    csvWriter.newLine();
                    csvWriter.write(resultatFichier.toString());
                }
            }

            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void creerCsvPaquets(Collection<ResultatAnalysePaquet> resultats, File fichierCsv) {
        try {
            BufferedWriter csvWriter = new BufferedWriter(new FileWriter(fichierCsv));

            // Écriture de la ligne d'en-tête
            csvWriter.write(ENTETE_CSVPAQUETS);

            // Écriture des lignes de statistiques de classes
            for (ResultatAnalysePaquet paquet : resultats) {
                csvWriter.newLine();
                csvWriter.write("%s,%s,%s,%s,%s,%s".formatted(paquet.chemin(), paquet.nomPaquet(),
                        paquet.ligneCodesPaquet(), paquet.lignesCommentairesPaquet(),
                        paquet.densiteCommentaires(), paquet.complexiteCyclomatiquePaquet()));
            }

            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //endregion FONCTIONS
}
