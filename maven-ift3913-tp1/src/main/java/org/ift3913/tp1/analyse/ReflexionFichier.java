package org.ift3913.tp1.analyse;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;

/**
 * Classe utilitaire pour performer la réflexion sur les fichiers de code source Java.
 *
 * @see <a href="https://stackoverflow.com/questions/24490860/further-analysis-of-java-source-using-java-and-reflection">Further analysis of Java source using Java (and reflection)</a>
 */
public class ReflexionFichier {
    private void compilerSource(File javaFile) {
        // Obtenir un compilateur pourrant être utilisé sur un fichier sur le disque.
        JavaCompiler compilateur = ToolProvider.getSystemJavaCompiler();
        int statutRetour = compilateur.run(null, null, null, javaFile.getAbsolutePath());

        if (statutRetour != 0) {
            throw new RuntimeException("Une erreur s'est produite lors de la compilation du fichier Java pour l'analyse. Statut=" + statutRetour);
        }
    }
}
