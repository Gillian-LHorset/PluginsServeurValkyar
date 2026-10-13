# ServeurValkyar

Repository du code des plugins du serveur minecraft de Valkyar

## Structure

### DatabaseCommunicationAPI

Cette API permet à tous les plugins de pouvoir interagire avec la DB. L'utilisation à la db doit se faire le moins possible afin de ne pas la surchager et de garantire la maintenabilité et la scalabilité du code.

Toutes les classes qui sont directement lié à la DB ou les classe qui en dépende devrons se trouver dans l'API. Cela permet de pouvoir facilité l'utilisation des classes dans d'autres contexte et créer un code propre.

## Normes

### Messages dans la console

Les messages print dans la console, doivent être écrit selon la norme suivante :\
**[NomDuPlugin] : Message**
