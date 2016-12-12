# État actuel


             ╔══C══╗
        A────B═════D═════E═════F

        ─ : requête de connection
        ═ : connexion existante


<br/>
<br/>
<br/>

# Étapes de connexion.

A souhaite se connecter, sur le réseau, et a détecté la présence de B.
Or, seul B et E sont ouverts à la connexion. Il n'est pas possible de se connecter sur C, D, ou F.

- Liste des étapes vue depuis A :

    - Détection de la présence de B.
    - Connexion à B.
    - Récupération des couples (adresses IP des membres du réseau - distance depuis B)

      << (C, 1) - (D, 1) - (E, 2) - (F, 3) >>

    - Tentative de connexion aux autres membres.

       Les connexions à C, D, F échouent <br/>
       La connexion à E réussi. Récupération des couples << (D, 1) - (C, 2) - (B, 2) - (F, 1) >>

    - Pour C, D, puis F, on informe le pair connecté le plus proche que la tentative de connexion à échoué
    - On conserve la connaissance de l'existence de C, D, F, ainsi que les distances de connexion avec les autres membres.
    - Si on reçoit une tentative (réussie) de connexion de la part de C, D, ou F, on met à jour la distance.
    - Si (B ou E) nous envoie que la connexion avec (C, D, ou F) est impossible à faire directement, on enregistre le fait que pour communiquer avec (C, D, ou F), il est nécessaire de passer par (B ou E).

- Liste des étapes vue par B ou E :

    - Établissement d'une connection avec A
    - Envoi des couples << IP - distance >>
    - Enregistrement de l'existence de A et de sa distance avec nous
    - Si on reçoit un message d'échec de connexion de la part de A concernant (C, D, ou F) ; alors on informe (C, D, ou F) de l'existence de A et de son échec de connexion.
    - Si (C, D, ou F) nous informent à leur tour d'un échec de connexion, on passe en mode tunneling entre A et (C, D, ou F), en informant A.

- Liste des étapes vue par C, D, ou F

    - D'après (B ou E), A tente de se connecter. On enregistre l'existence de A.
    - On tente de se connecter à A, mais c'est un échec (sinon, on est comme E ou B).
    - On demande alors à (B ou E) de passer en mode tunneling, et on enregistre le fait que pour communiquer avec A, il est nécessaire de passer par (B ou E).

<br/>
<br/>
<br/>


# Étapes de déconnexion (brisage de liens)

(Tous points de vue confondus)

- Lors d'une déconnexion d'un pair D, on informe les pairs étant à une distance de 1 de sa deconnexion.
- Pour chaque pair recevant l'information, on déconnecte et on oublie l'ip. Si on perd l'accès a un pair P (avec qui D faisait du tunneling), alors :
    - On demande aux pairs avec accès directs (distance de 1) s'ils ont accès à P, si oui à quelle distance, et en passant par quel autre pair.
    - Si l'un des pairs à accès à P, alors on vérifie s'ils envoient le packet à nous pour le transmettre à P. On fait ensuite une demande de tunneling pour obtenir la distance la plus courte.
- On retransmet l'information concernant la déconnexion de tout les pairs avec qui la connexion est perdue. Cible de la transmission : les pairs qui communiquaient avec D en faisant du tunneling par soi.
