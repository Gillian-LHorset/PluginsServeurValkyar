# ServeurValkyar

Repository du code des plugins du serveur minecraft de Valkyar

## Structure

La manipulation des données se fait en deux temps et avec un intermédiaire.

#### Mémoire

Le stockage en mémoire sert à pouvoir accéder à des données de manière immédiate sans avoir à faire de call DB à chaque modification ou appel de valeur. Au lancement du serveur le plugin DatabaseCommunicationAPI qui centralise tous les models, variables, utilities et autres crée plusieurs HashMap static contenant des valeurs ou objets qui peuvent être accédés directement depuis le plugin.

#### DB

La DB stock toutes les informations importantes du serveur. Elle peut être initialisée avec le fichier DB/database_creator.sql qui contient la structure des données et quelques valeurs de base.  
Les données critiques qui sont stockées dans les différents objets sont modifiées en parallèle dans la DB à chaque altération. Cela permet qu'en cas de crash ou extinction du serveur, ces données soient préservées.

#### Managers

Les managers ont un rôle essentiel, appliquer les altérations effectuées sur les objets et reporter les données dans la DB.  
Lorsqu'une donnée critique doit être modifiée, au lieu de passer par l'objet et à coté de faire un appel à la DB, lorsqu'on a une valeur critique à manipuler (ex : ajouter de l'argent à sa ville), on va appeler le Manager associé qui va lui s'occuper de mettre à jour les données dans l'objet, puis faire un report de ces valeurs dans la DB de manière asynchrone de façon à être non bloquant.  
Le but du manager est d'éviter la duplication du code, éviter de se perdre et éviter d'avoir à faire plein d'appels de différents objets puis de différents appels DB et d'en oublier. En somme, cela permet de gagner du temps et d'avoir un code bien structuré.

### DatabaseCommunicationAPI

Cette API permet à tous les plugins de pouvoir interagir avec la DB. L'utilisation à la db doit se faire le moins possible afin de ne pas la surcharger et de garantir la maintenabilité et la scalabilité du code.  
Toutes les classes qui sont directement lié à la DB ou les classe qui en dépende devrons se trouver dans l'API. Cela permet de pouvoir facilité l'utilisation des classes dans d'autres contexte et créer un code propre.

## Normes

### Messages dans la console

Les messages print dans la console, doivent être écrit selon la norme suivante :\
**[NomDuPlugin] : Message**
