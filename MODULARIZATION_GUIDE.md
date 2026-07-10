# 📦 Guide de Modularisation du Projet Java (Gradle)

## 🎯 Objectif
Transformer le projet **PotoFlux** en projet **modulaire** :
- Un ou plusieurs **modules de code** (libs, features, …)
- Des **modules de ressources** (`assets.jar`, `atlanta.jar` minimum)
- Configuration Gradle et Git adaptée.

---

## 📁 Structure de répertoires proposée
```
PotoFlux/
│   build.gradle          ← *racine* (déclencheur)
│   settings.gradle       ← déclaration des modules
│   .gitignore            ← mise à jour
│   gradlew / gradlew.bat
│   gradle/                ← wrapper
│
├─ modules/                ← **Tous les sous‑modules**
│   ├─ core/                ← module code principal (ex. `potoflux-core`)
│   │   src/main/java/…
│   │   build.gradle        ← dépendances du module
│   ├─ utils/               ← module auxiliaire (ex. utils, extensions)
│   │   src/main/java/…
│   │   build.gradle
│   ├─ assets/              ← module resources `assets.jar`
│   │   src/main/resources/…
│   │   build.gradle        ← `java-library` + `jar` task
│   └─ atlanta/             ← module resources `atlanta.jar`
│       src/main/resources/…
│       build.gradle
│
└─ src/ (optionnel)        ← **ancienne source** (dépréciée, à migrer)
```

> **Pourquoi `modules/` ?**
> - Centralise la découverte des sous‑projets.
> - Facilite l’ajout/suppression de modules sans toucher à la racine.
> - Gradle traite chaque dossier contenant un `build.gradle` comme sous‑projet.

---

## ⚙️ Modifications du **settings.gradle**
```groovy
rootProject.name = 'PotoFlux'

// Inclure les modules de code
include 'modules:core'
include 'modules:utils'

// Inclure les modules de ressources (jar)
include 'modules:assets'
include 'modules:atlanta'
```
> *Astuce* : si vous ajoutez d’autres modules, il suffit d’ajouter une ligne `include 'modules:<nom>'`.

---

## 🛠️ Configuration du **build.gradle** (racine)
```groovy
plugins {
    id 'java'               // pour le projet racine (facultatif)
    id 'idea'               // génération de fichiers IDEA
}

allprojects {
    group = 'fr.ordiscorp.potoflux'
    version = '1.0.0'
    repositories { mavenCentral() }
}

subprojects {
    apply plugin: 'java-library'
    // version Java cible, ajustez si besoin
    java { sourceCompatibility = JavaVersion.VERSION_17 }

    // Option : publier chaque jar dans le répertoire `libs/` du projet racine
    tasks.register('publishJar', Copy) {
        from tasks.named('jar')
        into "${rootProject.projectDir}/libs"
    }
}

// Exemple de dépendance entre modules
project(':modules:core') {
    dependencies {
        implementation project(':modules:utils')
        implementation project(':modules:assets')
        implementation project(':modules:atlanta')
    }
}
```
> **Points clés**
> - `java-library` donne `api` / `implementation` ; utilisez‑les pour exposer ou masquer des API entre modules.
> - La tâche `publishJar` copie chaque jar généré dans `libs/` afin que les modules de ressources puissent être référencés à l’exécution.

---

## 📄 Mise à jour du **.gitignore**
```gitignore
# IDEA folder – on ne veut pas le versionner
.idea/

# Gradle wrapper binaries (déjà présents, mais on les exclut pour éviter les conflits)
gradle/wrapper/gradle-wrapper.jar

# Build output
/build/
/**/*.class
/**/*.jar

# OS‑specific files
.DS_Store
Thumbs.db
```
> **Note** : Supprimez la ligne existante `/.idea` si elle diffère ; ajoutez la règle ci‑dessus.

---

## 🧩 Implémentation dans les modules **code**
### 1️⃣ Définir les *exports* et *requires* (module‑info)
Dans chaque sous‑module `src/main/java/module-info.java` :
```java
module fr.ordiscorp.potoflux.core {
    requires fr.ordiscorp.potoflux.utils; // dépendance interne
    requires fr.ordiscorp.potoflux.assets; // accès aux ressources
    // autres requires (java.sql, etc.)
    exports fr.ordiscorp.potoflux.core;
}
```
Répétez‑le pattern pour `utils`, `assets`, `atlanta` en ajustant les `exports`/`requires`.

### 2️⃣ Charger les JAR de ressources au runtime
```java
ClassLoader cl = Thread.currentThread().getContextClassLoader();
try (InputStream is = cl.getResourceAsStream("/assets/config.yml")) {
    // lecture du fichier présent dans assets.jar
}
```
> Le `ClassLoader` trouve automatiquement les ressources contenues dans les dépendances `module‑path`.

---

## 📦 Construction et exécution
```bash
# Dans le répertoire racine du projet
./gradlew clean build           # compile tous les modules & génère les JAR
./gradlew publishJar            # copie les JAR dans ./libs
```
Le jar exécutable principal se trouve généralement dans `modules/core/build/libs/` ; lancez‑le avec :
```bash
java -p libs -m fr.ordiscorp.potoflux.core/fr.ordiscorp.potoflux.core.Main
```
> `-p libs` indique le *module‑path* contenant `assets.jar` et `atlanta.jar`.

---

## ✅ Checklist rapide
- [ ] Créer dossier `modules/` et sous‑dossiers (`core`, `utils`, `assets`, `atlanta`).
- [ ] Déplacer chaque source existante dans le bon module (`src/main/java`, `src/main/resources`).
- [ ] Ajouter un `module-info.java` à chaque module.
- [ ] Copier le `build.gradle` de la racine dans chaque module (ou laisser l’héritage via `allprojects`).
- [ ] Mettre à jour `settings.gradle` avec les `include` ci‑dessus.
- [ ] Modifier le `build.gradle` racine comme indiqué.
- [ ] Actualiser `.gitignore` en excluant `.idea/` et les artefacts de build.
- [ ] Vérifier les dépendances inter‑modules (`implementation project(':modules:<module>')`).
- [ ] Exécuter `./gradlew clean build` et s’assurer que les JAR sont générés et accessibles.

---

## 📚 Ressources complémentaires
- [Guide officiel Gradle – Multi‑project builds](https://docs.gradle.org/current/userguide/multi_project_builds.html)
- [Java Platform Module System (JPMS) – Oracle docs](https://openjdk.org/jeps/261)
- [Best‑practice .gitignore for Java/Gradle](https://github.com/github/gitignore/blob/main/Java.gitignore)

---

*Ce guide vous fournit tout le nécessaire pour passer d’un projet monolithique à une architecture modulaire, prête à accueillir d’autres modules à l’avenir.*
