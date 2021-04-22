# Execution

* Se placer au niveau du dossier [src/](./SPLReader/src)
* Compiler le code source dans [SPLReader/src/](./SPLReader/src) avec `javac package1/*.java`
* Se placer au niveau du dossier [bin/](./SPLReader/bin)
* Executer avec `java package1.Parser <input> <output>`, remplacer `<input>` par le path vers le dossier (analyse par répertoire) ou fichier (analyse unique), si aucun `<output>` n'est spécifié, un dossier  [`out/`](./SPLReader/out) est crée a la racine de [SPLReader/](./SPLReader/)
* Les `<output>` successifs ne sont pas écrasés. Le timestamp du run est utilisé pour les différenciés.

# Compréhension d'un fichier de sortie XML

Un fichier de sortie XML contient :

* Une balise racine *annotations*.
* Une balise annotations contient une balise *input* indiquant le fichier analysé puis une énumération d'*annotation*. 
* L'énumération est composé d'*annotation* **Simple** et **Composer**.

# Remarques

Application du pattern **Composite** sur le cas d'une Annotation.

* Une annotation simple symbolise une **feuille** de l'AST.
* Une annotation composer symbolise un **noeud** de l'AST.
