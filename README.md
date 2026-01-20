Got it 👍
Here is a **clean, well-structured, DIRECT copy-paste README** — no extra talk, no confusion.
This is **resume + GitHub + recruiter friendly**.

---

## ✅ COPY THIS EXACT README.md

```md
# Expense Tracker – Spring Boot Application

A full-stack **Expense Tracker Web Application** built using **Spring Boot**, **Thymeleaf**, and **PostgreSQL**.  
This project focuses on real-world backend concepts such as authentication, CRUD operations, reporting, and OTP-based password reset.

---

## 📌 Project Overview

The Expense Tracker allows users to:
- Register and login securely
- Track daily expenses
- Categorize expenses
- Generate expense reports
- Reset password using Email OTP

> ⚠️ Current version supports **USER role only**  
> (No Admin module implemented yet)

---

## ✨ Features

### 🔐 Authentication
- User registration
- User login
- Password encryption
- Forgot password using **OTP via Email**

### 💰 Expense Management
- Add expense
- Edit expense
- Delete expense
- View expense list

### 🗂️ Categories
- Create categories
- Assign category to expenses

### 📊 Reports
- Filter expenses by date range
- View total expense
- Export report as **PDF**

### 📱 UI
- Responsive design
- Mobile-friendly sidebar toggle
- Clean Bootstrap UI

---

## 🛠️ Tech Stack

### Backend
- Java
- Spring Boot
- Spring MVC
- Spring Data JPA
- Spring Security (password encryption)
- Java Mail Sender (OTP email)

### Frontend
- Thymeleaf
- HTML
- CSS
- Bootstrap
- JavaScript

### Database
- PostgreSQL

---

## 🗃️ Project Structure

```

expense-tracker
│
├── src/main/java
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   └── config
│
├── src/main/resources
│   ├── templates
│   ├── static
│   └── application.properties
│
├── pom.xml
└── README.md

````

---

## ⚙️ Configuration (application.properties)

> ❗ Sensitive values are NOT included  
> Add your own values before running

```properties
spring.application.name=expense-tracker

# Database (PostgreSQL)
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.open-in-view=false

# Email Configuration (Gmail SMTP for OTP)
spring.mail.host=
spring.mail.port=
spring.mail.username=
spring.mail.password=
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
````

---

## 🔐 Email OTP Setup (Forgot Password)

* Uses **Gmail SMTP**
* Requires **2-Step Verification**
* Uses **Gmail App Password**

Steps:

1. Enable 2-Step Verification in Google Account
2. Generate App Password
3. Use App Password in `spring.mail.password`

---

## ▶️ How to Run the Project

### Prerequisites

* Java 17+
* PostgreSQL
* Maven
* IntelliJ IDEA

### Steps

```bash
git clone https://github.com/Maan516/expense-tracker.git
cd expense-tracker
mvn spring-boot:run
```

### Open in Browser

```
http://localhost:8080
```

---

## 🚀 Future Improvements

* Admin dashboard
* Role-based access control
* Charts & analytics
* Cloud deployment
* Docker support

---

## 👤 Author

**Maan Bahadur Khadka**
Spring Boot Backend Developer (Fresher)

---

## 📄 License

This project is created for learning and portfolio purposes.

