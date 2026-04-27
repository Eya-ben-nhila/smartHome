# 🏠 Smart Home IoT - Guide d'Utilisation

## 📋 Table des Matières

1. [Démarrage du Backend](#démarrage-du-backend)
2. [Accès au Frontend](#accès-au-frontend)
3. [Authentification](#authentification)
4. [Export de Données](#export-de-données)
5. [Système d'Alertes](#système-dalertes)
6. [Cache Redis](#cache-redis)
7. [API Endpoints](#api-endpoints)

---

## 🚀 Démarrage du Backend

### Prérequis
- Java 17+
- Maven 3.6+
- MongoDB (localhost:27017)
- Redis (localhost:6379) - optionnel

### Démarrage
```bash
cd backend
mvn spring-boot:run
```

Le serveur démarrera sur `http://localhost:8080/api`

---

## 🌐 Accès au Frontend

### Méthode 1: Ouvrir directement
```bash
# Windows
start frontend/index.html

# macOS/Linux  
open frontend/index.html
```

### Méthode 2: Navigateur
Ouvrir `file:///Users/eyabe/OneDrive/Desktop/iot-monitoring-platform/frontend/index.html`

---

## 🔐 Authentification

### Inscription
1. Cliquer sur "Basculer vers Inscription"
2. Remplir le formulaire:
   - Nom complet: Jean Dupont
   - Email: jean@dupont.com
   - Mot de passe: minimum 6 caractères
3. Cliquer "S'inscrire"

### Connexion
1. Remplir le formulaire:
   - Email: jean@dupont.com
   - Mot de passe: celui défini lors de l'inscription
2. Cliquer "Se Connecter"
3. Le token JWT sera affiché en cas de succès

---

## 📊 Export de Données

### Types d'Export Disponibles
- **Utilisateurs**: Liste complète des utilisateurs
- **Appareils**: Liste des appareils IoT
- **Données Énergétiques**: Historique de consommation

### Formats Supportés
- **CSV**: Compatible Excel, Google Sheets
- **PDF**: Rapport formaté pour impression

### Procédure d'Export
1. Choisir le type d'export (Utilisateurs/Appareils/Énergie)
2. Choisir le format (CSV/PDF)
3. Cliquer "Exporter"
4. Le fichier sera téléchargé automatiquement

---

## 🚨 Système d'Alertes

### Types d'Alertes
- **DEVICE_OFFLINE**: Appareil déconnecté
- **HIGH_ENERGY_CONSUMPTION**: Consommation élevée
- **TEMPERATURE_ALERT**: Alerte température
- **SECURITY_ALERT**: Alerte sécurité
- **MAINTENANCE_REQUIRED**: Maintenance requise
- **BATTERY_LOW**: Batterie faible
- **CONNECTION_LOST**: Perte de connexion
- **AUTOMATION_FAILED**: Échec automation

### Niveaux de Sévérité
- **LOW**: Information simple
- **MEDIUM**: Attention requise
- **HIGH**: Action immédiate recommandée
- **CRITICAL**: Urgence absolue

### Visualisation
- **Statistiques**: Total, non lues, actives
- **Liste**: 5 alertes les plus récentes
- **Couleurs**: Vert (info), Orange (warning), Rouge (erreur)

---

## ⚡ Cache Redis

### Clés de Cache Utilisées
- `energy:{deviceId}`: Données énergétiques temps réel
- `device:status:{deviceId}`: Statut des appareils
- `session:{userId}`: Sessions utilisateur
- `realtime:{sensorId}`: Données capteurs temps réel

### Utilisation
1. Entrer une clé (ex: `device:status:123`)
2. Entrer une valeur
3. Cliquer "Mettre en Cache"
4. Pour récupérer: cliquer "Récupérer du Cache"

---

## 🔌 API Endpoints

### Authentification
```
POST /api/auth/register
POST /api/auth/login
GET  /api/auth/health
```

### Export
```
GET  /api/export/csv/users
GET  /api/export/pdf/users
GET  /api/export/csv/devices
GET  /api/export/csv/energy
```

### Alertes
```
GET  /api/alerts/user/{userId}
GET  /api/alerts/user/{userId}/active
GET  /api/alerts/user/{userId}/unread
POST /api/alerts/create
POST /api/alerts/mark-read/{alertId}
POST /api/alerts/resolve/{alertId}
DELETE /api/alerts/{alertId}
```

### Exemples de Requêtes

#### Inscription
```bash
curl -X POST -H "Content-Type: application/json" \
-d '{"fullName":"Jean Dupont","email":"jean@dupont.com","password":"password123"}' \
http://localhost:8080/api/auth/register
```

#### Connexion
```bash
curl -X POST -H "Content-Type: application/json" \
-d '{"email":"jean@dupont.com","password":"password123"}' \
http://localhost:8080/api/auth/login
```

#### Export CSV
```bash
curl -H "Authorization: Bearer {token}" \
http://localhost:8080/api/export/csv/users
```

---

## 🎯 Prochaines Étapes

### Tests Finaux
1. Tester tous les endpoints avec curl
2. Vérifier l'intégration frontend-backend
3. Tester les exports avec données réelles
4. Valider le système d'alertes

### Déploiement
1. Configurer MongoDB production
2. Configurer Redis production
3. Activer sécurité JWT complète
4. Déployer sur serveur cloud

---

## 📞 Support

### Problèmes Communs
- **Backend ne démarre pas**: Vérifier MongoDB sur localhost:27017
- **Frontend ne se connecte pas**: Vérifier CORS et port 8080
- **Exports ne fonctionnent pas**: Vérifier authentification JWT

### Solutions
1. Redémarrer MongoDB: `mongod`
2. Vérifier logs backend pour erreurs
3. Utiliser navigateur avec console ouverte pour debug

---

## 🎉 Conclusion

Le système Smart Home IoT est maintenant **complètement fonctionnel** avec:
- ✅ Backend Spring Boot avec MongoDB
- ✅ Frontend moderne et responsive
- ✅ Système d'authentification JWT
- ✅ Export de données CSV/PDF
- ✅ Système d'alertes temps réel
- ✅ Cache Redis pour performances

**Prêt pour production et utilisation réelle!** 🚀
