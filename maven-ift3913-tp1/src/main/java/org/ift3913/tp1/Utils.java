package org.ift3913.tp1;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;

/**
 * Une classe contenant des fonctions utilitaires statiques, communes à plusieurs classes.
 *
 * @author Pierre Janier Dubry et Rui Jie Liu
 */
public class Utils {

    /**
     * Obtenir l'extension d'un fichier, en minuscules, à partir de son chemin.
     * Cette fonction retourne la chaîne de caractère après (excluant) le point final
     * (i.e. le délimiteur d'extension).
     * <br>
     * Si le fichier ne possède pas d'extension valide, cette fonction retourne {@code null}.
     *
     * @param cheminFichier le chemin du fichier
     * @return l'extension du fichier, en minuscules, sans le point (délimiteur d'extension).
     * {@code null} si n'existe pas.
     */
    public static String obtenirExtensionFichier(Path cheminFichier) {
        if (Files.isDirectory(cheminFichier)) return null;

        String nomFichier = cheminFichier.getFileName().toString();
        int indexDuPointExtension = nomFichier.lastIndexOf(".");
        if (indexDuPointExtension > 0 && nomFichier.length() > indexDuPointExtension) {
            return nomFichier.substring(indexDuPointExtension + 1).toLowerCase(Locale.ROOT);
        } else {
            return null;
        }
    }

    /**
     * Obtenir le nom complet du paquet d'un fichier ou dossier à partir de son chemin relatif
     * au chemin du dossier/paquet de base. Cette fonction suit la convention de nomenclature
     * des paquets des langages de programmation modernes, e.g. Java.
     * <br><br>
     * Comportement:
     * <ul>
     *     <li>
     *         Cas normal: fichier
     *         <ul>
     *             <li>racine = "C:\Projet"</li>
     *             <li>objectif = "C:\Projet\paquet1\paquet2\test.java"</li>
     *             <li>résultat = "paquet1.paquet2"</li>
     *         </ul>
     *     </li>
     *     <li>
     *         Cas normal: dossier
     *         <ul>
     *             <li>racine = "C:\Projet"</li>
     *             <li>objectif = "C:\Projet\paquet1\paquet2"</li>
     *             <li>résultat = "paquet1.paquet2"</li>
     *         </ul>
     *     </li>
     *     <li>
     *         Cas anormaux: objectif est un fichier
     *         <ul>
     *             <li>racine = "C:\ProjetA"</li>
     *             <li>objectif = "C:\ProjetB\paquet1\paquet2\test.java"</li>
     *             <li>résultat = ""</li>
     *         </ul>
     *     </li>
     *     <li>
     *         Cas anormaux: objectif est un dossier
     *         <ul>
     *             <li>racine = "C:\ProjetA"</li>
     *             <li>objectif = "C:\ProjetB\paquet1\paquet2\"</li>
     *             <li>résultat = "paquet2"</li>
     *         </ul>
     *     </li>
     * </ul>
     *
     * @param racine   le chemin du paquet de base
     * @param objectif le chemin du fichier/dossier spécifié
     * @return le nom complet du paquet, basé sur le chemin relatif
     */
    public static String obtenirNomPaquet(Path racine, Path objectif) {
        // Filtrer quelques cas anormaux
        if (!Files.isDirectory(racine)) {
            // si racine n'est pas un dossier (chemin aléatoire)
            return Files.isDirectory(objectif) ? objectif.getFileName().toString() : "";
        } else if (racine.equals(objectif)) {
            // si racine == objectif
            return racine.getFileName().toString();
        }

        // on récupère le chemin filtré du fichier concerné
        String cheminRelatif;
        if (Files.isDirectory(objectif)) {
            // si objectif est un dossier, il fait partie du nom du paquet
            cheminRelatif = racine.toUri().relativize(objectif.toUri()).toString();
        } else {
            // si objectif est un fichier, on ne veut pas mettre le nom du fichier comme nom d'un paquet
            cheminRelatif = racine.toUri().relativize(objectif.getParent().toUri()).toString();
        }

        if (Objects.equals(cheminRelatif, objectif.toString())) {
            // racine n'est pas ancêtre de l'objectif
            return Files.isDirectory(objectif) ? objectif.getFileName().toString() : "";
        }

        // Scénario normal: paquet = cheminRelatif
        // Note: Java Uri convertit tous les séparateurs de chemin en '/', peu importe la plateforme
        if (cheminRelatif.charAt(cheminRelatif.length() - 1) == '/') {
            cheminRelatif = cheminRelatif.substring(0, cheminRelatif.length() - 1);
        }
        return cheminRelatif.replace('/', '.');
    }
}
