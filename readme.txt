Auteur : 

- Rui Jie Liu (20174496)
- Pierre Janier Dubry (20143843)

Date : 2022/02/11

Ce programme permet d'analyser et de mesurer la structure d'un projet ou 
fichier java. Il analyse la syntaxe et détermine s'il s'agit d'un commentaire
ou si c'est du code. Il calcule ainsi, la densité de commentaire d'une part et
le nombre de classes, boucles et méthodes qui composent chaque fichier contenu
dans le projet. En suite, en mettant ces données en resultat, pour chaque 
fichiers et chaque paquets, le programme calcul la métrique WMC et WCP. Une fois
les données collectées et analysées, le programme retourne à la racine 2 
fichiers csv éditables relatif aux classes et aux paquets du projet.

Nous avons effectuer plusieurs tests avec ce programme et nous avons remarqué 
que le projet jfreechart n'était que partiellement ou faiblement commenté.

Pour le bonus, nous avons chercher un projet Java open-source sur Github et nous 
avons trouvé le projet libgdx (https://github.com/libgdx/libgdx). Nous avons 
cloner le main et nous avons remarqué que le code était extrêment mal commenté.
Pour ce rendre compte de cela, nous avons remarqué que la notation du WMC ou WCP
des classes ou paquets du fichier texte était écrite en écriture exponentiel!
On pouvait constater des valeurs par exemple : 3.148773884711829E-4

Cela nous a donné l'idée d'analyser la moyenne de tous les WMC et WCP pour les 
paquets et les classes respectivement, ainsi il serait plus rapide d'interpréter
les résultats en sortie du programme. Comme WMC et WCP sont tous les deux des 
ratios alors il est possible de calculer leur moyenne.
Comme cette fonction n'était pas demandé par l'énoncé du TP1, nous avons seulement
écrit dans le command prompt la moyenne.

Concernant le Pull-Request, nous avons envoyé .... (des fichiers commentés ou 
juste les résultats?)