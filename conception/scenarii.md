# Scénarii « création de la partie »

## Scénario nominal

1. Affichage du menu de création de la partie
2. Lecture des paramètres de la partie (nom, nombre de joueurs, mot de passe, port)
3. Lancement du salon de jeu
4. Affichage de l'interface du salon
5. Attente des autres joueurs
6. Démarrage de la partie

## Scénario d'exception

3. Lancement du salon impossible (port déjà utilisé, …), retour à 1.

# Scénarii « rejoindre une partie »

## Scénario nominal

1. Affichage de la liste des parties
2. Lecture du nom de la partie à rejoindre
3. Connexion au salon
4. Affichage de l'interface du salon
5. Attente des autres joueurs
6. Démarrage de la partie

## Scénario alternatif "avec un mot de passe"

3. Mot de passe requis.
4. Lecture du mot de passe.
5. Connexion au salon
6. Affichage de l'interface du salon
7. Attente des autres joueurs
8. Démarrage de la partie

## Scénario d'exception "impossible de se connecter"

3. Connexion impossible, retour à 1.

