package org.ift3913.tp1;

import java.nio.file.Path;

/**
 * Données obtenues après une analyse de commentaires auprès d'un fichier de code.
 *
 * @author Pierre Janier Dubry et Rui Jie Liu
 */
public record ResultatAnalyseFichier(String nomClasse, int lignesDeCode, int lignesCommentaires,
                                     Path cheminFichier, int nbPredicat) {
    public double densiteCommentaires() {
        return (double) lignesCommentaires / (double) lignesDeCode;
    }

    public String toString() {
        return "%s,%s,%s,%s,%s".formatted(cheminFichier(), nomClasse, lignesDeCode, lignesCommentaires, densiteCommentaires());
    }
}
