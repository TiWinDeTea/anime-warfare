# Règles

## Le tour de jeu

1. Phase de recrutement du staff.

        a. +1 par mascotte.
        b. +2 par studio contrôlé.
        c. +1 par studio indépendant.
        d. +1 par mascotte ennemie capturée (rendues dans la réserve de la faction d'origine).
        e. Augmenter jusqu'à la moitié du score de la faction au plus haut pouvoir (arrondi au supérieur).

2. Phase de détermination du premier joueur.

        a. Si c'est le premier tour, aléatoire.
        b. Sinon, celui avec le plus de staff.
        c. En cas d'égalité, c'est le premier joueur du tour précédent qui départage.
        d. Le joueur qui commence choisi aussi le sens de rotation pour l'ordre du tour de table.

3. Phase de marketing.

        a. +1K de fans par studio contrôlé. (!!! pas en partie longue !!!)
        b. Chaque joueur organiser une convention pour obtenir
            - +1K de fans par studio contrôlé
            - +1 droit de campagne publicitaire par Héros
        c. Évènements spéciaux.
        d. Vérifier les conditions de victoire et de défaite.

4. La phase d'action.


On revient à la phase 1 après avoir effectué la phase 4,
sauf si la partie est gagnée.

/!\\ lors du premier tour de jeu, pas de phase 3.


## Progression sur l'échelle de popularité

Il y a une échelle numérotée de 0 à FANS_FIN.
Chaque joueur a sa propre jauge de fans (= sa position sur l'échelle).


## Conventions

En débutant par le premier joueur, chacun choisi s'il souhaite effectuer une convention.
Il n'est possible de lancer *qu'une et une seule convention*.

Comment l'effectuer :

    1. Il faut affecter le nombre de staff correspondant à la position où se trouve le curseur sur la piste marketing.
    2. Le curseur avance d'un cran sur la piste marketing.
    3. Le joueur avance son pion sur l'échelle de popularité d'un espace pour chaque studio qu'il contrôle et gagne un droit de campagne publicitaire pour chacun de ses Héros en jeu.

### Crise économique du secteur

Si le curseur de la piste marketing arrive à la fin, on détermine un gagnant éventuel immédiatement et le jeu se termine.

### Campagne publicitaires

Lorsque l'on gagne un droit de campagne publicitaire, on le pioche dans la réserve **sans le révéler aux autres joueurs** et on le place à proximité du plateau de sa faction (info visible par les autres joueurs).
La valeur (entre 1 et 3) de la campagne n'est connue que du joueur.

### Évènements spéciaux

Certaines capacités et productions prennent effet durant la phase marketing.
Sauf cas exceptionnel, les effets surviennent après les conventions et avant de déterminer la victoire.
En cas de conflit, c'est dans l'ordre du tour de table que les effets sont appliqués.


## Phase d'action

Lors de la phase d'action chaque joueur dans l'ordre défini peut effectuer des actions.

La phase d'action continue tant qu'il reste un joueur avec au moins un employé disponible.
Si un joueur n'a plus aucun employé disponible, il ne peut plus effectuer d'actions (même gratuite) jusqu'à la fin de la phase.
Un joueur peut passer son tour, cela fait immédiatement prendre congé à son staff.

Il y a trois types d'actions :

    - *Actions communes*, une maximum par round d'action.
    - *Actions uniques*, une maximum par round, dépend des factions.
    - *Actions illimitées*, aucune limitation pendant le round du joueur.

        - Gratuit — Prendre le contrôle d'un studio. Il s'agit de placer une mascotte sur le studio.
        - Gratuit – Abandonner un studio. La mascotte quitte le studio.

Lors d'un round, il **faut** faire soit une action commune, soit une action unique (pas les deux !).
Les actions illimités peuvent être effectuées avant et/ou après.

### Actions communes

- -3 membres du staff – ouvrir un studio.
    Il est nécessaire de disposer d'une mascotte dans une *zone sans stadio* où vous voulez ouvrir votre studio.

- -N membre(s) du staff – Déplacer N unités d'une case.

- -1 membre du staff – Engager un affrontement.
    *Devient illimité lorsque les six mangas sont collectés*, cependant le round peut s'arrêter si le joueur le souhaite et il n'est **pas possible de lancer plus d'un affrontement par zone** lors de son round round.

- -1 membre du staff – Capturer une mascotte.
    Hiérarchie entre les unités : Héros > Personnage > Mascotte.
    Pour capturer un adorateur, il faut une unité supérieur en rang à tous les adversaires de la faction concernée de la zone.
    Attention, capturer ≠ combat. L'adorateur ne peut pas se défendre même s'il a des points de combat.

- -1 membre du staff – Recruter une mascotte.
    Il ne peuvent apparaître que dans une zone où il y a déjà une unité du joueur ou
    n'importe où si le joueur n'a plus d'unité.

- -? membre(s) du staff – Invoquer un personnage.
    Un personnage doit apparaître sur un studio contrôlé par le joueur.

- -? membre(s) du staff – Invoquer un Héros.
    cf spécificités de chaque Héros.

- Tout les membres du staff – passer son tour.


## Affrontement publicitaire

1. Pré-affrontement.

        L'attaquant puis le défenseur appliquent les capacités de préaffrontement de leur choix.
        Les capacités permanentes et de préaffrontement des factions tierces s'appliquent ensuite.

2. Affrontement.

        Les deux opposants jettent simultanément leurs dés en fonction de leur score de combat (un dé par point).
        Un jet de 6 humilie, un jet de 4 ou 5 blesse leur égo, le reste rate.
        Les unités humiliatiées sont retirées du jeu et remis en réserve. C'est le joueur qui perd les unités qui décide quelles unités sont perdues.

3. Post-affrontement.

        Les blessures et les capacités post-affrontement sont appliquées (de la même façon que les capacités pré-affrontement).
        Une unité blessée doit se replier sur une zone adjacente où il n'y a pas d'unité de l'autre faction.
        Sinon, l'unité meurt.
        L'attaquant effectue la retraite en premier.


## Les productions

Chaque production (manga, light novel, …) a un effet unique.
Lorsque l'on créé une production, elle ne sera jamais perdue, même si la condition de création n'est plus remplie.


## Réserves

- 6 mascottes max sur la carte pour chaque faction.
- Pour les autres personnages, voir au cas par cas dans les factions.


## Détermination victoire et défaite

La partie se termine soit quand un joueur a plus de FANS_FIN fans soit par une « Crise économique du secteur ».
Si le jeu se termine lors d'une phase de marketing, les autres joueurs peuvent toujours lancer une dernière convention (si pas déjà fait).
Dans n'importe quel cas, il est possible de révéler ses campagnes publicitaires.

    - Le vainqueur est celui qui a le plus de fans parmi ceux qui possèdent 6 productions.
    - En cas d'égalité, les joueurs concernés se partagent la victoire.
    - Si personne n'a les 6 productions, tout le monde perd, et les studios d'animation du monde entier font faillite.

