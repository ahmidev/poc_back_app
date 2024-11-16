# POC Chat Application - Backend

Ce projet représente le backend du **POC Chat Application**, développé dans le cadre du projet **Your Car Your Way App**. Il gère la communication en temps réel entre les utilisateurs (Client et Support), l'enregistrement des messages dans une base de données, ainsi que la récupération des messages non lus. Le backend repose sur **Spring Boot**, utilise une base de données relationnelle (MySQL) et expose des fonctionnalités WebSocket pour une communication instantanée.

---

## **Contexte d'Entreprise**

### **À propos de Your Car Your Way**

**Your Car Your Way** est une entreprise de location de voitures opérant depuis plus de 20 ans. Elle a débuté en Angleterre avant de s'étendre en Europe, puis aux États-Unis. Avec cette croissance, l'entreprise souhaite centraliser ses systèmes d'information pour offrir à ses clients une expérience unifiée via une plateforme unique.

Le **POC Chat Application** fait partie intégrante de cette nouvelle plateforme, visant à améliorer la communication entre les clients et l'équipe support.

Le backend fournit les services nécessaires pour :

- La gestion des messages en temps réel (via WebSockets et STOMP).
- La persistance des messages dans une base de données relationnelle.
- La récupération et le marquage des messages non lus.

---

## **Prérequis**

Avant de démarrer ce projet, assurez-vous d'avoir :

1. **Java 17** ou une version supérieure installée.
2. **Maven** (outil de gestion des dépendances).
3. **MySQL** configuré et en cours d'exécution.
4. Un IDE pour Java (par exemple : IntelliJ IDEA, Eclipse ou VS Code).

---

## **Installation**

### Étape 1 : Cloner le Dépôt

Clonez le projet depuis le dépôt Git :

```bash
git clone <repository-url>
cd poc_back_app
```

### Étape 2 : Configurer la Base de Données

1. Créez une base de données MySQL pour l'application, par exemple `poc_chat_db` :
   ```sql
   CREATE DATABASE poc_chat_db;
   ```
2. Mettez à jour les paramètres de connexion à la base de données dans le fichier **`application.properties`** (situé dans `src/main/resources`) :

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/poc_chat_db
   spring.datasource.username=<votre_nom_utilisateur>
   spring.datasource.password=<votre_mot_de_passe>
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```

### Étape 3 : Construire et Lancer le Projet

1. Compilez le projet avec Maven :
   ```bash
   mvn clean install
   ```
2. Lancez l'application Spring Boot :
   ```bash
   mvn spring-boot:run
   ```

L'application sera accessible à l'adresse [http://localhost:8081](http://localhost:8081).

---

## **Fonctionnalités**

### **WebSocket et Gestion des Messages**

- **WebSocket** : Le backend utilise Spring WebSocket pour établir une connexion bidirectionnelle en temps réel entre le client et le serveur.
- **Protocole STOMP** : Les messages sont échangés via STOMP, qui ajoute une couche d'abstraction à WebSocket.
- Les messages envoyés par un utilisateur sont **persistés en base de données** et simultanément transmis en temps réel aux destinataires connectés.

### **Gestion des Messages Non Lus**

- Les messages non lus sont marqués comme **`isRead = false`** lorsqu'ils sont reçus.
- Lorsqu'un utilisateur ouvre le chat, les messages non lus sont marqués comme lus, et un compteur est mis à jour.

### **Endpoints REST**

Le backend expose également des endpoints REST pour gérer les messages :

- **Récupération des messages non lus** :
  ```http
  GET /messages/unread?receiver=<receiver>
  ```
  Renvoie la liste des messages non lus pour un utilisateur donné.

- **Marquage des messages comme lus** :
  ```http
  POST /messages/mark-as-read?receiver=<receiver>
  ```
  Met à jour l'état des messages non lus d'un utilisateur en les marquant comme lus.

---

## **Structure du Projet**

### Arborescence des Fichiers

```plaintext
src/
├── main/
│   ├── java/com/poc_chat/
│   │   ├── config/
│   │   │   ├── WebSocketConfig.java         # Configuration WebSocket et STOMP
│   │   ├── controller/
│   │   │   ├── ChatController.java          # Gestion des messages WebSocket
│   │   │   ├── ChatRestController.java      # Endpoints REST pour les messages
│   │   ├── model/
│   │   │   ├── ChatMessage.java             # Modèle pour les messages
│   │   ├── repository/
│   │   │   ├── ChatMessageRepository.java   # Requêtes pour manipuler les messages
│   │   ├── service/
│   │   │   ├── ActiveUserService.java       # Gestion des utilisateurs actifs
│   │   ├── listener/
│   │   │   ├── WebSocketEventListener.java  # Gestion des événements WebSocket (connexion/déconnexion)
│   └── resources/
│       ├── application.properties           # Configuration (Base de données, logs, etc.)
└── test/
```

---

## **Principales Dépendances**

Voici les principales dépendances utilisées dans ce projet :

1. **`spring-boot-starter-websocket`** :
    - Fournit un support complet pour les WebSockets et le protocole STOMP.

2. **`spring-boot-starter-data-jpa`** :
    - Permet d'utiliser JPA (Java Persistence API) pour gérer les entités et effectuer des opérations sur la base de données.

3. **`mysql-connector-j`** :
    - Connecteur JDBC pour MySQL.

4. **`spring-boot-starter-web`** :
    - Fournit les fonctionnalités nécessaires pour exposer des endpoints REST.

5. **`lombok`** :
    - Simplifie le code en générant automatiquement les getters, setters, constructeurs, etc.

---

## **Scripts Maven Utiles**

- **`mvn clean install`** : Compile et génère un package exécutable.
- **`mvn spring-boot:run`** : Démarre l'application Spring Boot.
- **`mvn test`** : Exécute les tests unitaires.

---

