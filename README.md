

# Expense Tracker – Spring Boot Application

A full-stack **Expense Tracker Web Application** built using **Spring Boot**, **Thymeleaf**, and **PostgreSQL**.

This project is designed to demonstrate **real-world backend development** concepts such as authentication, role-based access, CRUD operations, reporting, and secure password reset using Email OTP.

---

## 📌 Project Overview

The Expense Tracker helps users manage their daily expenses in a structured way.

It supports:

* Secure user authentication
* Expense and category management
* Expense reports with filters
* Password reset using OTP
* Admin dashboard to monitor users

---

## ✨ Features

### 🔐 Authentication

* User registration
* User login
* Encrypted passwords (Spring Security)
* Forgot password using **Email OTP**
* Change password from profile

---

### 👤 User Features

#### Dashboard

* Total expenses (all time)
* This month’s expenses
* Total categories created

#### Categories

* Add categories
* Categories are user-specific

#### Expenses

* Add expense
* View expense list
* Filter expenses by:

  * Date range
  * Category
* Delete expenses

#### Reports

* View total spending for selected date range
* Analyze spending between dates

#### Profile

* View account details
* Change password securely
* Logout

---

### 🛡️ Admin Features

#### Admin Login

* Admin logs in using admin credentials
* Access restricted to ADMIN role only

#### Admin Dashboard

* View total registered users
* Quick access to user list

#### User Management (Read-Only)

* View list of registered users
* View user name, email, and role
* No delete or block actions (intentionally kept simple)

#### Admin Profile

* View admin account details
* Change admin password securely
* Logout

---

## 🛠️ Tech Stack

### Backend

* Java
* Spring Boot
* Spring MVC
* Spring Data JPA
* Spring Security (password encryption)
* Java Mail Sender (OTP email)

### Frontend

* Thymeleaf
* HTML
* CSS
* Bootstrap
* JavaScript

### Database

* PostgreSQL

---

## ⚙️ Configuration

### `application.properties`

Sensitive values are **not committed**.
Create your own `application.properties` file using the example below.

```properties
spring.application.name=expense-tracker

# PostgreSQL Database
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.open-in-view=false

# Email Configuration (OTP)
spring.mail.host=
spring.mail.port=
spring.mail.username=
spring.mail.password=
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

---

## 🔐 Email OTP Setup (Forgot Password)

This project uses **Gmail SMTP** for sending OTP emails.

Steps:

1. Enable **2-Step Verification** in your Google account
2. Generate a **Gmail App Password**
3. Use the App Password in `spring.mail.password`

---

## ▶️ How to Run the Project

### Prerequisites

* Java 17+
* PostgreSQL
* Maven
* IntelliJ IDEA (recommended)

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

## 🧭 How to Use the Application

### 👤 User Flow

1. Register a new account
2. Login with email and password
3. If password is forgotten → use **Forgot Password (OTP)**
4. After login:

   * View dashboard (total expenses, monthly expenses, categories)
   * Add categories
   * Add expenses
   * View and filter expense list
   * Generate expense report for date range
5. Go to Profile → change password
6. Logout

---

### 🛡️ Admin Flow

1. Login using admin credentials
2. Access Admin Dashboard
3. View total registered users
4. View user list (read-only)
5. Go to Admin Profile → change password
6. Logout

---

## 🚀 Future Improvements

* Expense charts and analytics
* Admin actions (block / delete users)
* Cloud deployment
* Docker support
* React frontend version

---

## 👤 Author

**Maan Bahadur Khadka**
Spring Boot Backend Developer (Fresher)

---

## 📄 License

This project is created for **learning and portfolio purposes**.

