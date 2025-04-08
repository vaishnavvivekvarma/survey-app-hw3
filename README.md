
# 📝 Survey Application – SWE 645 Assignment 3__

A complete full-stack student survey application built using **Spring Boot**, **Docker**, **Kubernetes (EKS)**, and **Amazon RDS**, with a CI/CD pipeline managed by **Jenkins**. This project demonstrates real-world deployment automation and container orchestration.

---

## 📁 Project Structure & Purpose

```
survey-app-hw3/
│
├── src/                    
│   └── main/java/...       # Contains all Java source code for the Spring Boot backend.
│
├── k8s/
│   ├── deployment.yaml     # Kubernetes Deployment configuration: defines how the app is deployed (replicas, container image, ports).
│   └── service.yaml        # Kubernetes Service: exposes the deployment using NodePort so it's accessible via browser.
│
├── Dockerfile              # Multi-stage Docker build file that compiles the app and packages it as a container.
├── Jenkinsfile             # Declarative Jenkins pipeline that automates build → Docker image creation → push to Docker Hub → deploy to Kubernetes.
├── pom.xml                 # Maven config for dependencies, project metadata, and build instructions.
├── .gitignore              # Ignores IDE files, target folder, etc.
└── README.md               # You're reading it 😊
```

---

## 🚀 How the App Works

### ✅ Features:
- Users can submit a survey form with personal info, preferences, and raffle entries.
- Survey submissions are stored in an Amazon RDS MySQL database.
- Admins can view records (if extended with a frontend or API).
- Raffle & interest fields processed and stored efficiently.

---

## 🧑‍💻 Tech Stack

| Layer             | Technology Used                                |
|------------------|--------------------------------------------------|
| Backend           | Spring Boot (REST API)                         |
| Build Tool        | Maven                                           |
| Database          | Amazon RDS (MySQL)                             |
| Containerization  | Docker & Docker Hub                            |
| CI/CD             | Jenkins (on EC2)                                |
| Deployment        | Kubernetes via AWS EKS                         |
| Source Control    | Git & GitHub                                    |

---

## 🔄 CI/CD Pipeline Flow (Fully Automated)

1. **Developer Pushes Code** → to GitHub `main` branch.
2. **Jenkins** detects change and:
   - Clones repo.
   - Builds Spring Boot project using Maven.
   - Builds Docker image.
   - Pushes image to Docker Hub:  
     ➤ `vaishnavvarma/survey-app:latest`
   - Applies Kubernetes deployment and service using `kubectl`.

---

## 🌐 Deployed App URLs

| Node Instance IP         | Access Link                                  |
|--------------------------|-----------------------------------------------|
| EC2 Node 1               | http://3.81.100.168:30081                     |
| EC2 Node 2               | http://3.83.205.144:30081                     |

📬 You can submit survey forms via either node IP!

---

## 🛢️ Amazon RDS - MySQL

- **Host**: `survey-db.csngcimmuxcv.us-east-1.rds.amazonaws.com`
- **User**: `admin`
- **Database**: `surveydb`

To check submitted forms:

```sql
SELECT * FROM surveys;
```

---

## 🔐 Jenkins Credentials & Docker

- **DockerHub Username**: `vaishnavvarma`
- **Credential ID in Jenkins**: `dockerhub-creds`
- Used inside `Jenkinsfile` to authenticate Docker push.

---

## 📄 Jenkinsfile Explanation

```groovy
pipeline {
    agent any

    environment {
        IMAGE = 'vaishnavvarma/survey-app:latest'
    }

    stages {
        stage('Clone Code') {
            steps {
                git 'https://github.com/vaishnavvivekvarma/survey-app-hw3.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE .'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        docker push $IMAGE
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh '''
                    kubectl apply -f k8s/deployment.yaml
                    kubectl apply -f k8s/service.yaml
                '''
            }
        }
    }
}
```

---

## 📌 Notes

- If you're testing locally, make sure:
  - Docker daemon is running.
  - Kubernetes config (kubectl) is pointing to EKS.
- Don’t forget to set the correct Jenkins credentials.
