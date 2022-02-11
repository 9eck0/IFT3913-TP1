package org.ift3913.tp1;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Une classe contenant des fonctions utilitaires statiques, communes à plusieurs classes.
 *
 * @author Pierre Janier Dubry et Rui Jie Liu
 */
public class Utils {

    /**
     * Identifiants servant à modifier l'accès d'une classe ou sous-classe.
     * Peuvent seulement apparaître avant le nom de la classe.
     */
    public static final Set<String> identifiantsModificateursClasses = new HashSet<>(
            Arrays.asList("abstract", "final", "non-sealed", "private", "public", "sealed", "static"));

    /**
     * Identifiants servant à dénoter les keywords permissibles pour la définition des classes (Java 16).
     * Ces identifiants doivent absolument se retrouver immédiatement avant le nom de la classe
     */
    public static final Set<String> identifiantsDeclarationClasses = new HashSet<>(
            Arrays.asList("class", "enum", "interface", "record"));

    /**
     * Identifiants servant à modifier la flexibilité d'hiérarchiser une classe ou sous-classe.
     * Peuvent seulement apparaître après le nom de la classe.
     */
    public static final Set<String> identifiantsImplementationClasses = new HashSet<>(
            Arrays.asList("extends", "implements", "permits"));

    /**
     * Mots-clés permissibles dans la déclaration de la signature d'une méthode.
     * Ces mots-clés viennent juste avant la déclaration de type de la méthode.
     */
    public static final Set<String> identifiantsModificateursMethodes = new HashSet<>(
            Arrays.asList("default", "final", "native", "private", "protected", "public", "static", "synchronized"));

    /**
     * Identifiants des types du langage Java
     */
    public static final Set<String> identifiantsTypesNatifs = new HashSet<>(
            Arrays.asList("boolean", "byte", "char", "double", "float", "int", "long", "short", "short", "void"));

    /**
     * Identifiants servant à dénoter le début d'un branchement
     */
    public static final Set<String> identifiantsStructuresDeControle = new HashSet<>(
            Arrays.asList("if", "while", "for", "switch"));

    public static final Set<String> identifiantsAutres = new HashSet<>(
            Arrays.asList("_", "assert", "break", "case", "catch", "const", "continue", "do", "else", "false",
                    "finally", "goto", "import", "instanceof", "module", "new", "null", "package", "return",
                    "strictfp", "super", "this", "throw", "transient", "true", "var", "volatile", "yield"));

    private static Set<String> identifiantsJava;

    /**
     * Valide si le mot passé en argument
     *
     * @param identifiant le mot à vérifier
     * @return retourne si le mot est en effet un mot réservé de Java
     */
    public static boolean estMotCleJava(String identifiant) {
        if (identifiantsJava == null) {
            identifiantsJava = new HashSet<>(identifiantsAutres);
            identifiantsJava.addAll(identifiantsModificateursClasses);
            identifiantsJava.addAll(identifiantsDeclarationClasses);
            identifiantsJava.addAll(identifiantsImplementationClasses);
            identifiantsJava.addAll(identifiantsModificateursMethodes);
            identifiantsJava.addAll(identifiantsTypesNatifs);
            identifiantsJava.addAll(identifiantsStructuresDeControle);
        }

        return identifiantsJava.contains(identifiant);
    }

    /**
     * Vérifie si le terme est un identifiant valide, c'est à dire si le mot passé
     * en paramètre est un identifiant java ou non
     *
     * @param terme le mot à analyser
     * @return si il s'agit d'un identifiant java ou non
     */
    public static boolean identifiantValide(String terme) {
        terme = terme.strip();
        if (!Character.isJavaIdentifierStart(terme.charAt(0))) return false;
        for (int i = 1; i < terme.length(); i++) {
            char chr = terme.charAt(i);
            if (!Character.isJavaIdentifierPart(chr)) return false;
        }
        return true;
    }

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
