# Documentation - Système de Gestion Universitaire avec Rôles

## L'Architecture Utilisée

### MVVM avec Authentification et Gestion des Rôles

J'ai implémenté une architecture MVVM complète avec un système d'authentification et de gestion des rôles (étudiants/enseignants). Voici le flux global :

```
Authentification → Navigation Dynamique → Entity → DAO → Repository → ViewModel → UI
```

**En résumé :** L'utilisateur se connecte, le système détermine son rôle, puis la navigation et les fonctionnalités sont adaptées automatiquement selon qu'il soit étudiant ou enseignant.

### Les Couches de l'Architecture

#### 1. **Entity** (Les modèles de données)

- **UserEntity** : Stocke les identifiants de connexion (username, password) et le rôle (STUDENT ou TEACHER). Lié à StudentEntity ou TeacherEntity selon le rôle.
- **StudentEntity** : Un étudiant avec nom, prénom, date de naissance, niveau d'étude, email, genre.
- **TeacherEntity** : Un enseignant avec nom, prénom, email, département, date de naissance, genre.
- **CourseEntity** : Un cours avec nom, crédits ECTS, niveau, description, et l'ID de l'enseignant.
- **SubscribeEntity** : Une inscription qui lie un étudiant à un cours avec une note préliminaire.
- **GradeEntity** : Une note donnée par un enseignant à un étudiant pour un cours spécifique, avec la date.

#### 2. **DAO** (Les accès à la base de données)

- **UserDao** : Gère les utilisateurs pour l'authentification (recherche par username, CRUD).
- **StudentDao** : Opérations CRUD sur les étudiants.
- **TeacherDao** : Opérations CRUD sur les enseignants.
- **CourseDao** : Opérations CRUD sur les cours, avec filtrage par niveau et par enseignant.
- **SubscribeDao** : Gère les inscriptions étudiants-cours.
- **GradeDao** : Gère les notes avec filtrage par étudiant, cours ou enseignant.

#### 3. **Repository** (Le chef d'orchestre)

- **AuthRepository** : Gère l'authentification, la connexion, l'inscription et le suivi de l'utilisateur connecté. Maintient l'état d'authentification dans des StateFlow.
- **SCRUDRepository** : Centralise toutes les opérations CRUD sur les données. Contient aussi la logique métier pour calculer les moyennes pondérées par ECTS.

#### 4. **ViewModel** (Le cerveau de chaque écran)

- **AuthViewModel** : Gère la connexion et l'inscription, expose l'état d'authentification et le rôle.
- **StudentViewModel** : Gère les informations de l'étudiant connecté.
- **TeacherViewModel** : Gère les informations de l'enseignant connecté.
- **StudentListViewModel** : Gère la liste des étudiants (administration).
- **CourseListViewModel** : Gère la liste des cours avec filtrage.
- **SubscribeListViewModel** : Gère les inscriptions.
- **StudentGradesViewModel** : Charge les notes de l'étudiant connecté.
- **StudentSummaryViewModel** : Calcule la moyenne pondérée par ECTS.
- **TeacherGradesViewModel** : Gère la saisie des notes par l'enseignant.

#### 5. **UI** (Ce qu'on voit)

**Authentification :**

- **LoginScreen** : Écran de connexion avec redirection automatique selon le rôle.
- **RegisterScreen** : Inscription avec sélection du rôle (étudiant ou enseignant).

**Étudiant :**

- **StudentHomeScreen** : Accueil avec accès aux fonctionnalités (cours, inscriptions, notes, résumé).
- **StudentCoursesScreen** : Liste des cours disponibles pour son niveau.
- **StudentSubscriptionsScreen** : Liste de ses inscriptions.
- **StudentGradesScreen** : Affichage de toutes ses notes par cours.
- **StudentSummaryScreen** : Affichage de la moyenne pondérée par ECTS.

**Enseignant :**

- **TeacherHomeScreen** : Accueil avec accès aux fonctionnalités (cours, notes, étudiants).
- **TeacherCoursesScreen** : Liste des cours qu'il enseigne.
- **TeacherGradesScreen** : Saisie des notes pour les étudiants inscrits à ses cours.
- **TeacherStudentsScreen** : Liste des étudiants inscrits à ses cours.

**Administration (ancien système) :**

- **StudentListScreen**, **CourseListScreen**, **SubscribeListScreen** : Listes complètes avec CRUD.

## Flux de Données

### Flux d'Authentification

```
1. Inscription : UI → AuthViewModel → AuthRepository → UserDao + StudentDao/TeacherDao → Base
2. Connexion : UI → AuthViewModel → AuthRepository → UserDao → StateFlow → UI (redirection)
3. Navigation : AuthViewModel.userRole → NavGraph → Écran approprié (student_home ou teacher_home)
```

### Flux de Lecture des Données

```
Base SQLite → Room → DAO → Repository → ViewModel.StateFlow → collectAsState() → UI
```

### Flux d'Écriture des Données

```
UI (action) → ViewModel → Repository → DAO → Room → Base SQLite → Flow → StateFlow → UI (mise à jour)
```

### Flux de Calcul de Moyenne

```
StudentSummaryScreen → StudentSummaryViewModel → SCRUDRepository.calculateStudentAverage()
→ GradeDao.getGradesByStudent() → CourseDao.getCourseById() → Calcul pondéré par ECTS → UI
```

## Navigation Dynamique selon le Rôle

### Gestion de la Navigation

L'application utilise **Jetpack Navigation Compose** avec une navigation dynamique basée sur le rôle :

1. **Point d'entrée** : `LoginScreen` (route `login`)
2. **Après connexion** :
   - Si `UserRole.STUDENT` → Navigation vers `student_home`
   - Si `UserRole.TEACHER` → Navigation vers `teacher_home`
3. **Sous-graphes de navigation** :
   - **Étudiant** : `student_home` → `student_courses`, `student_subscriptions`, `student_grades`, `student_summary`
   - **Enseignant** : `teacher_home` → `teacher_courses`, `teacher_grades`, `teacher_students`

### Implémentation

La navigation est gérée dans `NavGraphs.kt` avec :

- Un objet `Routes` centralisant toutes les routes
- Des `composable` pour chaque écran avec gestion des paramètres
- Redirection automatique via `LaunchedEffect` dans `LoginScreen` qui observe `isAuthenticated` et `userRole`

## Fonctionnalités Implémentées

### Module Authentification

- **Inscription** : Choix du rôle (étudiant ou enseignant), validation des champs, vérification de l'unicité du username
- **Connexion** : Vérification des identifiants, gestion des erreurs, redirection automatique selon le rôle
- **Déconnexion** : Réinitialisation de l'état d'authentification, retour à l'écran de connexion

### Module Étudiant

- **Inscription** : Spécification du niveau d'étude (P1, P2, B1, etc.)
- **Cours disponibles** : Affichage des cours correspondant au niveau de l'étudiant
- **Inscriptions** : Possibilité de s'inscrire aux cours de son niveau
- **Notes** : Consultation de toutes les notes reçues avec le nom du cours et la date
- **Résumé final** : Calcul et affichage de la moyenne pondérée par ECTS pour le niveau de l'étudiant

### Module Enseignant

- **Inscription** : Création d'un compte avec département
- **Déclaration de cours** : Création de cours avec spécification du niveau, ECTS, description
- **Saisie des notes** :
  - Sélection d'un cours enseigné
  - Affichage des étudiants inscrits à ce cours
  - Saisie/modification des notes (sur 20) avec validation
- **Consultation des étudiants** : Liste des étudiants inscrits aux cours de l'enseignant

### Module Notes et Moyennes

- **Gestion des notes** : Stockage des notes avec date, lien étudiant-cours-enseignant
- **Calcul de moyenne pondérée** :
  - Formule : `Σ(note × ECTS) / Σ(ECTS)` pour tous les cours du niveau
  - Filtrage automatique par niveau de l'étudiant
  - Gestion du cas où aucune note n'est disponible

## Technologies Utilisées

### Backend

- **Room** : Base de données SQLite locale avec migration automatique
- **Hilt** : Injection de dépendances pour simplifier l'injection des DAO, Repository et ViewModel
- **Coroutines** : Gestion asynchrone des opérations de base de données
- **Flow/StateFlow** : Flux de données réactifs pour mettre à jour l'UI automatiquement
- **TypeConverters** : Conversion automatique des enums (Gender, LevelCourse, UserRole) et Date en types primitifs

### Frontend

- **Jetpack Compose** : Interface moderne déclarative d'Android
- **Material Design 3** : Design system de Google avec thème cohérent
- **Navigation Compose** : Navigation déclarative et type-safe entre les écrans
- **LazyColumn** : Listes performantes avec recyclage automatique
- **State Management** : `remember`, `mutableStateOf`, `collectAsState()` pour gérer l'état local

### Architecture

- **MVVM** : Séparation claire entre données, logique métier et interface utilisateur
- **Repository Pattern** : Abstraction de la source de données
- **Dependency Injection** : Hilt pour l'injection automatique des dépendances

## Difficultés Rencontrées et Solutions

### 1. **Gestion de l'État d'Authentification**

**Le problème :** Comment maintenir l'état d'authentification et le rôle de l'utilisateur à travers toute l'application ?

**La solution :** J'ai créé `AuthRepository` avec des `StateFlow` pour `isAuthenticated`, `currentUser` et `userRole`. Ces StateFlow sont observés par les ViewModel et l'UI se met à jour automatiquement.

### 2. **Navigation Dynamique selon le Rôle**

**Le problème :** Rediriger automatiquement l'utilisateur vers le bon écran après connexion selon son rôle.

**La solution :** Utilisation de `LaunchedEffect` dans `LoginScreen` qui observe `isAuthenticated` et `userRole`. Quand l'utilisateur se connecte, la navigation se fait automatiquement vers `student_home` ou `teacher_home`.

### 3. **Smart Cast avec Propriétés Déléguées**

**Le problème :** Kotlin ne peut pas faire de "smart cast" sur les propriétés déléguées (`val student by viewModel.currentStudent`).

**La solution :** Utilisation de `student?.let { currentStudent -> }` au lieu de `if (student != null)` pour créer une variable locale non-null.

### 4. **Migration de Base de Données**

**Le problème :** Erreur "A migration from 1 to 2 was required but not found" lors de l'ajout de nouvelles entités.

**La solution :** Ajout de `.fallbackToDestructiveMigration()` dans `AppModule.kt` pour le développement. En production, il faudrait créer une vraie migration.

### 5. **Calcul de Moyenne Pondérée**

**Le problème :** Calculer la moyenne en tenant compte des crédits ECTS et filtrer par niveau.

**La solution :** Implémentation dans `SCRUDRepository.calculateStudentAverage()` qui :

- Récupère toutes les notes de l'étudiant
- Pour chaque note, récupère le cours correspondant
- Filtre par niveau et calcule `Σ(note × ECTS) / Σ(ECTS)`

### 6. **Affichage des Noms au lieu des IDs**

**Le problème :** Dans les inscriptions, afficher les noms des étudiants et des cours au lieu des IDs.

**La solution :** Création de méthodes dans `SubscribeListViewModel` (`getStudentName()`, `getCourseName()`) qui recherchent dans les listes chargées.

### 7. **Gestion des Paramètres Manquants dans les Entités**

**Le problème :** Erreurs de compilation lors de l'ajout de nouveaux champs (`teacherId`, `description` dans `CourseEntity`, `level`, `email` dans `StudentEntity`).

**La solution :** Mise à jour systématique de tous les constructeurs et formulaires pour inclure les nouveaux champs.

### 8. **Gestion des Routes de Navigation Manquantes**

**Le problème :** Crash avec "Navigation destination that matches route student_courses cannot be found".

**La solution :** Ajout de toutes les routes manquantes dans `NavGraphs.kt` pour les écrans étudiants et enseignants.

## Justification des Bibliothèques Utilisées

### Room

- **Pourquoi :** Base de données locale simple et efficace pour stocker les données sans backend
- **Avantages :** Type-safe, génération automatique du code SQL, intégration avec Flow pour les mises à jour réactives

### Hilt

- **Pourquoi :** Simplifie grandement l'injection de dépendances
- **Avantages :** Moins de code boilerplate, injection automatique dans les ViewModel, gestion du cycle de vie

### Jetpack Compose

- **Pourquoi :** Interface moderne et déclarative
- **Avantages :** Code plus concis, recomposition automatique, performance améliorée

### Navigation Compose

- **Pourquoi :** Navigation type-safe et déclarative
- **Avantages :** Gestion automatique du back stack, support des paramètres typés, intégration native avec Compose

### Flow/StateFlow

- **Pourquoi :** Flux de données réactifs pour mettre à jour l'UI automatiquement
- **Avantages :** Réactivité automatique, pas besoin de gérer manuellement les callbacks, intégration avec Compose via `collectAsState()`

## Conclusion

J'ai réussi à créer une application complète avec :

- Système d'authentification fonctionnel avec gestion des rôles
- Navigation dynamique selon le rôle de l'utilisateur
- Fonctionnalités complètes pour étudiants (cours, inscriptions, notes, moyenne)
- Fonctionnalités complètes pour enseignants (cours, saisie notes, étudiants)
- Calcul de moyenne pondérée par ECTS
- Architecture MVVM propre et bien structurée
- Interface utilisateur moderne et réactive
- Code commenté et documenté

**Points forts :**

- L'application compile sans erreurs
- La navigation est fluide et adaptée au rôle
- L'UI se met à jour automatiquement grâce aux Flow
- Le code est bien organisé et maintenable
- La logique métier est centralisée dans les Repository

---
