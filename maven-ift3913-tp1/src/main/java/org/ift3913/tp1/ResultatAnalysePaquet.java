package org.ift3913.tp1;

import java.util.Collection;

/**
 * Données obtenues après une analyse de commentaires auprès d'un seul paquet (dossier)
 * contenant possiblement plusieurs fichiers de code.
 * Ce conteneur de données est supposé de contenir uniquement les fichiers appartenant
 * au paquet spécifié, et non aux sous-paquets.
 *
 * @param resultatsFichiers un dictionnaire couplant les noms des paquets et
 * @author Pierre Janier Dubry et Rui Jie Liu
 */
public record ResultatAnalysePaquet(Collection<ResultatAnalyseFichier> resultatsFichiers,
                                    String nomPaquet, String chemin) implements Comparable<ResultatAnalysePaquet> {

    /**
     * Obtenir la statistique de lignes de code totales à travers tous les fichiers du paquet.
     * <br>
     * @return le nombre de lignes de code en total
     */
    public int ligneCodesPaquet() {
        return resultatsFichiers.stream().mapToInt(ResultatAnalyseFichier::lignesDeCode).sum();
    }

    /**
     * Obtenir la statistique de lignes de commentaires totales à travers tous les fichiers du paquet.
     * <br>
     * @return le nombre de lignes de code en total
     */
    public int lignesCommentairesPaquet() {
        return resultatsFichiers.stream().mapToInt(ResultatAnalyseFichier::lignesCommentaires).sum();
    }

    /**
     * Obtenir la densité de commentaires de tout le paquet.
     * @return une valeur entre 0 et 1
     */
    public double densiteCommentaires() {
        return (double) lignesCommentairesPaquet() / (double) ligneCodesPaquet();
    }

    /**
     * Obtenir la complexité cyclomatique du paquet.
     * @return un entier positif ou nul
     */
    public int complexiteCyclomatiquePaquet() {
        return resultatsFichiers.stream().mapToInt(ResultatAnalyseFichier::complexiteCyclomatique).sum();
    }

    /**
     * Cette statistique représente le degré selon lequel le paquet est bien commentée.
     * Elle est obtenue avec la formule suivante:
     * <br>
     * densité_commentaires / complexité_cyclomatique
     * @return une valeur entre 0 et 1
     */
    public double paquetBC() {
        return densiteCommentaires() / complexiteCyclomatiquePaquet();
    }

    /**
     * Pour comparer les resultats en fonction de la valeur de leur WCP pour le tri
     * de la collection.
     * <br>
     * @param o un résultat à comparer
     * @return retourne le paquet comparé
     */
    @Override
    public int compareTo(ResultatAnalysePaquet o) {
        return Double.compare(paquetBC(), o.paquetBC());
    }

    /**
     * Affiche de manière formattée style CSV les résultats du paquet analysé.
     * @return une chaîne de caractères pouvant être écrit dans un fichier CSV
     */
    @Override
    public String toString() {
        return "%s,%s,%s,%s,%s,%s,%s".formatted(chemin, nomPaquet,
                ligneCodesPaquet(), lignesCommentairesPaquet(),
                densiteCommentaires(), complexiteCyclomatiquePaquet(), paquetBC());
    }
}
