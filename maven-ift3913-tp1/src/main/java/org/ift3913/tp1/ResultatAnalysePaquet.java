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
                                    String nomPaquet, String chemin) {

    /**
     * Obtenir la statistique de lignes de code totales à travers tous les fichiers du paquet.
     *
     * @return le nombre de lignes de code en total
     */
    public int ligneCodesPaquet() {
        return resultatsFichiers.stream().mapToInt(ResultatAnalyseFichier::lignesDeCode).sum();
    }

    /**
     * Obtenir la statistique de lignes de commentaires totales à travers tous les fichiers du paquet.
     *
     * @return le nombre de lignes de code en total
     */
    public int lignesCommentairesPaquet() {
        return resultatsFichiers.stream().mapToInt(ResultatAnalyseFichier::lignesCommentaires).sum();
    }

    public double DensiteCommentaires() {
        return (double) lignesCommentairesPaquet() / (double) ligneCodesPaquet();
    }
}
