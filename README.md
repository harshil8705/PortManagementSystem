# Port Management System – Backend

A robust and scalable **Port Management System** built with **Java, Spring Boot, Hibernate (JPA), MySQL**, and cutting‑edge AI integration. The backend automates port operations—order handling, container tracking, expense monitoring, and more—secured via **cookie‑based JWT authentication** and enriched with AI‑driven capabilities.

---

## Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Data JPA (Hibernate)**
- **Spring Security** (JWT + Cookies)
- **Spring AI** (Ollama / local LLM)
- **MySQL**
- **Python Flask Microservice** (AI recommendations with `scikit‑learn`)
- **Maven**
- **Jackson / JSON**  
  *(Frontend: React + Tailwind CSS coming soon)*

---

## Key Features

### 1. Personal AI Chatbot
- Built with **Spring AI** and local LLMs.
- Handles natural‑language **order tracking** and **cancellation**.
- Calls service‑layer methods and queries the database in real time.

### 2. AI‑Based Port Recommendation
- Separate **Python Flask** service using **scikit‑learn**.
- Suggests optimal ports from live MySQL data.
- Future‑proof for more advanced ML models.

### 3. Real‑Time Admin Analytics
- Dashboards for port authorities:
  - Shows the Current amount-filled status of Ports, Ships and Containers.
- Live data updates via backend APIs.

### 4. Expense Tracker
- Logs **payments** and **refunds** automatically.
- Supports auditing and financial oversight for users and admins.

### 5. Cookie‑Based JWT Authentication
- Secure signup/login with tokens stored in cookies.
- **Role‑based access**:  
  `USER` – place/cancel orders, track shipments  
  `ADMIN` – full analytics & management APIs

### 6. Order & Payment Management
- Place orders, simulate payments, cancel with refunds.
- Comprehensive validation and status tracking.

---

## 7. Feature Plan

- Building **Frontend** with React + Tailwind CSS 

---

## 8. Contact

- 🔗 [LinkedIn – Harshil Champaneri](https://www.linkedin.com/in/harshil-champaneri/)
- 🐙 [GitHub – @harshil8705](https://github.com/harshil8705)

Feel free to connect or reach out for queries, feedback, or collaboration!
