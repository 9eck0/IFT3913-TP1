package org.ift3913.tp1.analyse;

/**
 * Représente un résultat de la transition d'un {@link AutomateTransition} (style Mealy).
 *
 * @param prochainEtat le prochain état de l'automate
 * @param valide si la transition est valide ou non
 * @author Pierre Janier Dubry et Rui Jie Liu
 */
public record ResultatTransitionAutomate(AutomateTransition prochainEtat, boolean valide) {
}
