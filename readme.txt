IFT3913 - Qualité du logiciel et métriques

Auteurs :

- Rui Jie Liu (20174496)
- Pierre Janier Dubry (20143843)

Version de Java utilisé : 

java 16.0.2 "16.0.1" 2021-04-20
Java(TM) SE Runtime Environment (build 16.0.1+9-24)
Java HotSpot(TM) 64-Bit Server VM (build 16.0.1+9-24, mixed mode, sharing)

Date : 2022/02/11

================================ Abstrait ================================

Ce programme permet d'analyser et de mesurer la structure d'un projet ou 
fichier java. Il analyse la syntaxe et détermine s'il s'agit d'un commentaire
ou si c'est du code. Il calcule ainsi, la densité de commentaire d'une part et
le nombre de classes, boucles et méthodes qui composent chaque fichier contenu
dans le projet. En suite, en mettant ces données en resultat, pour chaque 
fichiers et chaque paquets, le programme calcul la métrique WMC et WCP. Une fois
les données collectées et analysées, le programme retourne à la racine 2 
fichiers csv éditables relatif aux classes et aux paquets du projet.

Nous avons effectué plusieurs tests avec ce programme et nous avons remarqué 
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

================================ Compilation ================================

Compilation en JAR à partir de Maven.
1) importer le projet Maven dans un IDE désiré
2) à la ligne de commande du IDE, entrer: mvn package
Le fichier JAR se retrouvera dans le dossier .../maven-ift3913-tp1/target/

Le point d'entrée du programme se trouve dans la classe TP1.main()

================================ Usage ================================

exemple de commande à écrire dans le shell pour exécuter le fichier jar : 
java -jar AnalyseurCommentaires.jar "C:\Users\user\Documents\Model"

Le programme produira deux fichiers en sortie dans le même dossier:
classes.csv et paquets.csv

================================ Résultats ================================

Voici une synthèse des résultats obtenus en mesurant les codes de jfreechart (version 2022-02-09)
et de libgdx (version 2022-02-10).

-------------------------------------------------------
Analyser un projet à partir de: "D:\Tests\jfreechart\src"
Analyse terminée, voici l'aperçu des résultats pour tous les paquets:
	Lignes de code:         	256412
	Lignes de commentaires: 	124416
	Densité de commentaires:	0.4852191005101165
	Complexité cyclomatique:	18711
	Tendance globale de niveau de commentaires:	2.593229119288742E-5

Les trois pires classes en terme de niveau de commentaire: 
D:\Tests\jfreechart\src\main\java\org\jfree\chart\plot\XYPlot.java,XYPlot,4981,2259,0.4535233888777354,599,7.571342051381225E-4
D:\Tests\jfreechart\src\main\java\org\jfree\chart\plot\CategoryPlot.java,CategoryPlot,4523,2110,0.4665045323900066,550,8.481900588909211E-4
D:\Tests\jfreechart\src\main\java\org\jfree\data\general\DatasetUtils.java,DatasetUtils,2326,872,0.3748925193465176,305,0.0012291558011361234

Les trois pires paquets en terme de niveau de commentaire: 
D:\Tests\jfreechart\src\main\java\org\jfree\chart\plot,main.java.org.jfree.chart.plot,25028,12156,0.48569602045708804,2540,1.9121890569176694E-4
D:\Tests\jfreechart\src\main\java\org\jfree\chart\axis,main.java.org.jfree.chart.axis,17608,8353,0.4743866424352567,1552,3.056614964144695E-4
D:\Tests\jfreechart\src\main\java\org\jfree\chart\renderer\xy,main.java.org.jfree.chart.renderer.xy,18269,8646,0.47326071487218785,1503,3.148773884711829E-4
-------------------------------------------------------

-------------------------------------------------------
Analyser un projet à partir de: "D:\Tests\libgdx\src"
Analyse terminée, voici l'aperçu des résultats pour tous les paquets:
    Lignes de code:             117193
    Lignes de commentaires:     28464
    Densité de commentaires:    0.24288140076625736
    Complexité cyclomatique:    19720
    Tendance globale de niveau de commentaires: 1.2316501053055648E-5

Les trois pires classes en terme de niveau de commentaire:
D:\Tests\libgdx\src\com\badlogic\gdx\AbstractGraphics.java,AbstractGraphics,16,0,0.0,5,0.0
D:\Tests\libgdx\src\com\badlogic\gdx\AbstractInput.java,AbstractInput,61,0,0.0,15,0.0
D:\Tests\libgdx\src\com\badlogic\gdx\scenes\scene2d\utils\UIUtils.java,UIUtils,55,0,0.0,15,0.0

Les trois pires paquets en terme de niveau de commentaire:
D:\Tests\libgdx\src\com\badlogic\gdx\scenes\scene2d\ui,src.com.badlogic.gdx.scenes.scene2d.ui,12446,1731,0.13908082918206652,2960,4.698676661556301E-5
D:\Tests\libgdx\src\com\badlogic\gdx\utils,src.com.badlogic.gdx.utils,23659,5640,0.2383870831396086,5067,4.7046987002093665E-5
D:\Tests\libgdx\src\com\badlogic\gdx\graphics\profiling,src.com.badlogic.gdx.graphics.profiling,2903,115,0.03961419221495005,450,8.803153825544455E-5
-------------------------------------------------------