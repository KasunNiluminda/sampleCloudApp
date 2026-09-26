# sampleCloudApp
# ☁️ SecureCloudFX — Cloud File Storage & Role-Based Access Control

[![Java Version](https://img.shields.io/badge/Java-11%2B-orange?logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-13-blue?logo=java&logoColor=white)](https://openjfx.io/)
[![Database](https://img.shields.io/badge/Database-SQLite3-003B57?logo=sqlite&logoColor=white)](https://www.sqlite.org/)
[![Build](https://img.shields.io/badge/Build-Maven-C71A36?logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![Security](https://img.shields.io/badge/Security-PBKDF2_Salted_Hashing-green)](#security-architecture)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

> **SecureCloudFX** is a standalone JavaFX enterprise desktop application designed for secure cloud file management, user administration, and granular file permission sharing. It provides salted cryptographic password hashing, role-based user authentication, and dynamic SQLite database interactions.

---

## 📸 Key Features

- 🔐 **Cryptographic Security:** Passwords are never stored in plaintext; salted and hashed using `PBKDF2WithHmacSHA1` with a dedicated secure `.salt` engine.
- 👥 **Role-Based Access Control (RBAC):**
  - **Administrator Console:** Manage user registrations, update user details, revoke privileges, and upload administrative system files.
  - **User Dashboard:** Upload personal files, manage storage items, view assigned permissions, and inspect file metadata.
- 🗂️ **Granular Permission Sharing:** Authors can selectively grant or restrict access levels (`Read`, `Write`, `None`) on specific files to other system users.
- 💾 **Embedded Database:** Powered by `sqlite-jdbc` with persistent relational tables for users, uploaded files, and permission matrices.
- 🎨 **Modern JavaFX FXML Interface:** Clean, component-based graphical interface with interactive controllers and real-time state feedback.

---

## 🏛️ System Architecture

```mermaid
flowchart TD
    UI[JavaFX FXML Interface] -->|Action Events| Controllers[Controller Layer]
    Controllers --> App[App Router / Scene Navigator]
    
    subgraph Security Layer
        Auth[PBKDF2 Password Hasher] <--> Salt[Unique .salt Provider]
    end
    
    subgraph Data & Persistence Layer
        Controllers --> DB[Database Manager DB.java]
        DB --> Auth
        DB <--> SQLite[(comp20081.db SQLite)]
    end

