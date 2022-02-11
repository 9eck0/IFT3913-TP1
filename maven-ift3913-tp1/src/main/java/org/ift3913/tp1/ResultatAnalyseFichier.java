package org.ift3913.tp1;

import java.nio.file.Path;

/**
 * Données obtenues après une analyse de commentaires auprès d'un fichier de code.
 *
 * @author Pierre Janier Dubry et Rui Jie Liu
 */
public record ResultatAnalyseFichier(String nomClasse, int lignesDeCode, int lignesCommentaires,
                                     Path cheminFichier, int complexiteCyclomatique) implements Comparable<ResultatAnalyseFichier> {


    /**
     * Obtenir la densité de commentaires de tout le paquet.
     * @return une valeur entre 0 et 1
     */
    public double densiteCommentaires() {
        return (double) lignesCommentaires / (double) lignesDeCode;
    }

    /**
     * Cette statistique représente le degré selon lequel la classe est bien commentée.
     * Elle est obtenue avec la formule suivante:
     * <br>
     * densité_commentaires / complexité_cyclomatique
     * @return une valeur entre 0 et 1
     */
    public double classeBC() {
        return densiteCommentaires()/complexiteCyclomatique;
    }

    /**
     * Affiche de manière formattée style CSV les résultats de la classe analysée.
     * @return une chaîne de caractères pouvant être écrit dans un fichier CSV
     */
    public String toString() {
        return "%s,%s,%s,%s,%s,%s,%s".formatted(cheminFichier(), nomClasse,
                lignesDeCode, lignesCommentaires, densiteCommentaires(), complexiteCyclomatique,classeBC());
    }

    /**
     * Pour comparer les resultats en fonction de la valeur de leur WMC pour le tri
     * de la collection.
     * <br>
     * @param o un résultat à comparer
     * @return retourne le paquet comparé
     */
    @Override
    public int compareTo(ResultatAnalyseFichier o) {
        return Double.compare(classeBC(), o.classeBC());
    }
}
