# Projet Min Makespan
Programme réalisé en Java pour comparer les algorithmes de makespan (calcul de temps de projet).

Les algorithmes utilisés sont : 
* LSA (List Simulated Annealing)
* LPT (Largest Processing Time)
* Algorithme perso

## LSA
Principe: on affecte les taches une par une à la première machine qui se libère.

## LPT
Principe: réaliser la LSA sur la liste de taches triées par ordre décroissant.

## Notre algo
Principe: Appliquer des taches à la première machine jusqu'à atteindre la moyenne, puis appliquer les taches restantes sur la seconde avec le même principe, etc jusqu'à la dernière machine. Ensuite, appliquer une LPT classique avec les taches restantes.

# Le dossier
Le dossier est disponible sous le nom "Min Makespan.docx" à la racine du git.
