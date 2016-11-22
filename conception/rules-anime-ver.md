# Règles

## Le tour de jeu

1. Phase de recrutement du staff.

        a. +1 par mascotte.
        b. +2 par studio contrôlé.
        c. +1 par studio abandonné.
        d. +1 par mascotte ennemie capturée (rendues dans la réserve de la faction d'origine).
        e. Augmenter jusqu'à la moitié du score de la faction au plus haut pouvoir (arrondi au supérieur).

2. Phase de détermination du premier joueur.

        a. Si c'est le premier tour, aléatoire.
        b. Sinon, celui avec le plus de staff.
        c. En cas d'égalité, c'est le premier joueur du tour précédent qui départage.
        d. Le joueur qui commence choisi aussi le sens de rotation pour l'ordre du tour de table.

3. Phase de marketing.

        a. +1K de fans par studio contrôlé. (!!! pas en partie longue !!!)
        b. Chaque joueur peut lancer un rituel et obtenir
            - +1K de fans par studio contrôlé
            - +1 campagne publicitaire par Héros
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
    3. Le joueur avance son pion sur l'échelle de popularité d'un espace pour chaque studio qu'il contrôle et gagne une campagne publicitaire pour chacun de ses Héros en jeu.

### Mort subite

Si le curseur de la piste marketing arrive à la fin, on détermine la victoire immédiatement et le jeu se termine.

### Campagne publicitaires

Lorsque l'on gagne une campagne publicitaire, on le pioche dans la réserve **sans le révéler aux autres joueurs** et on le place à proximité du plateau de sa faction (info visible par les autres joueurs).
La valeur (entre 1 et 3) de la campagne n'est connue que du joueur.

### Évènements spéciaux

Certains mangas et capacités prennent effet durant la phase marketing.
Sauf cas exceptionnel, les effets surviennent après les conventions et avant de déterminer la victoire.
En cas de conflit, c'est dans l'ordre du tour de table que les effets sont appliqués.


## Phase d'action

Lors de la phase d'action chaque joueur dans l'ordre défini peut effectuer des actions.

La phase d'action continue tant qu'il reste un joueur avec du staff disponible.
Si un joueur tombe à court de staff, il ne peut plus effectuer d'actions (même gratuite) jusqu'à la fin de la phase.
Un joueur peut passer son tour, cela fait immédiatement prendre congé à son staff.

Il y a trois types d'actions :

    - *Actions communes*, une maximum par round d'action.
    - *Actions uniques*, une maximum par round, dépend des factions.
    - *Actions illimitées*, aucune limitation pendant le round du joueur.

        - 0 staff — Contrôler d'un studio. Il s'agit de placer une mascotte sur le studio.
        - 0 staff – Abandonner un studio. La mascotte quitte le studio.

Lors d'un round, il **faut** faire soit une action commune, soit une action unique (pas les deux !).
Les actions illimités peuvent être effectuées avant et/ou après.

### Actions communes

- -3 staffs – ouvrir un studio.
    Il est nécessaire de disposer d'une mascotte dans une *zone sans stadio* où vous voulez ouvrir votre studio.

- -N staffs – Déplacer N unités d'une case.

- -1 staff – Engager une bataille.
    *Devient illimité lorsque les six mangas sont collectés*, cependant le round peut s'arrêter si le joueur le souhaite et il n'est **pas possible de lancer plus d'une bataille par zone** lors du round.

- -1 staff – Capturer une mascotte.
    Hiérarchie entre les unités : Héros > Personnage > Mascotte.
    Pour capturer un adorateur, il faut une unité supérieur en rang à tous les adversaires de la faction concernée de la zone.
    Attention, capturer ≠ combat. L'adorateur ne peut pas se défendre même s'il a des points de combat.

- -1 staff – Recruter une mascotte.
    Il ne peuvent apparaître que dans une zone où il y a déjà une unité du joueur ou
    n'importe où si le joueur n'a plus d'unité.

- -? staffs – Invoquer un personnage.
    Un personnage doit apparaître sur un studio contrôlé par le joueur.

- -? staffs – Invoquer un Héros.
    cf spécificités de chaque Héros.

- Tout le staff – passer son tour.


## Bataille

1. Prébataille.

        L'attaquant puis le défenseur appliquent les capacités de prébataille de leur choix.
        Les capacités permanentes et de prébataille des factions tierces s'appliquent ensuite.

2. Bataille.

        Les deux opposants jettent simultanément leurs dés en fonction de leur score de combat (un dé par point).
        Un jet de 6 donne un coup mortel, un jet de 4 ou 5 donne une blessure, le reste rate.
        Les coups mortels sont appliqués. C'est le joueur qui perd les unités qui décide quelles unités sont perdues.

3. Postbataille.

        Les blessures et les capacités postbataille sont appliquées (de la même façon que les capacités prébatailles).
        Une unité blessée doit se replier sur une zone adjacente où il n'y a pas d'unité de l'autre faction.
        Sinon, l'unité meurt.
        L'attaquant effectue la retraite en premier.


## Les manga

Chaque manga a un effet unique.
Lorsque l'on obtient un manga, il ne sera jamais perdu même si la condition d'obtention n'est plus remplie.


## Réserves

- 6 mascottes max sur la carte pour chaque faction.
- Pour les personnages, voir au cas par cas dans les factions.


## Détermination victoire et défaite

La partie se termine soit quand un joueur a plus de FANS_FIN fans soit par une « mort subite ».
Si phase d'apocalypse, les autres joueurs peuvent toujours lancer une dernière convention (si pas déjà fait).
Dans n'importe quel cas, il est possible de révéler ses campagnes publicitaires.

    - Le vainqueur est celui qui a le plus de fans parmi ceux qui possèdent les 6 mangas.
    - En cas d'égalité, les joueurs concernés se partagent la victoire.
    - Si personne n'a les 6 mangas, personne ne gagne et l'humanité est sauvée.

