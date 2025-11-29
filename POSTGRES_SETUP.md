# PostgreSQL Setup Guide for BlockFlow Pro

This guide will walk you through installing PostgreSQL on Windows, setting up the database, and configuring your Spring Boot application.

## 1. Installing PostgreSQL on Windows

1.  **Download the Installer:**
    *   Go to the official PostgreSQL download page: [https://www.enterprisedb.com/downloads/postgres-postgresql-downloads](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)
    *   Download the installer for **Windows x86-64** (latest version, e.g., 16.x or 15.x).

2.  **Run the Installer:**
    *   Run the downloaded `.exe` file.
    *   **Installation Directory:** Default is usually fine (`C:\Program Files\PostgreSQL\16`).
    *   **Select Components:** Ensure **PostgreSQL Server**, **pgAdmin 4**, and **Command Line Tools** are checked.
    *   **Data Directory:** Default is fine.
    *   **Password:** **IMPORTANT:** You will be asked to set a password for the superuser (`postgres`). **Remember this password!** (e.g., `postgres` or `admin`).
    *   **Port:** Default is `5432`. Keep this unless you have a conflict.
    *   **Locale:** Default is fine.
    *   Finish the installation.

## 2. Verifying Installation

1.  Open the **Start Menu** and search for **pgAdmin 4**. Open it.
2.  It may ask for a master password (for pgAdmin itself) and then the password for the `postgres` user you set during installation.
3.  Expand **Servers** -> **PostgreSQL 16**. If it connects, you are good to go.

## 3. Creating Database and User

You can do this via **pgAdmin 4** or the **SQL Shell (psql)**. We will use the SQL Shell for precision.

1.  Open **Start Menu** -> **SQL Shell (psql)**.
2.  Press **Enter** for Server, Database, Port, and Username to accept defaults.
3.  Enter the **Password** you set during installation (characters won't show).

Run the following SQL commands (copy and paste one by one):

```sql
-- 1. Create the database
CREATE DATABASE blockflow;

-- 2. Create a dedicated user for the application
-- Replace 'blockflow_password' with a secure password if you wish
CREATE USER blockflow_user WITH ENCRYPTED PASSWORD 'blockflow_password';

-- 3. Grant privileges to the user on the database
GRANT ALL PRIVILEGES ON DATABASE blockflow TO blockflow_user;

-- 4. (Optional but recommended) Grant schema privileges
\c blockflow
GRANT ALL ON SCHEMA public TO blockflow_user;
```

Type `\q` and press Enter to exit the shell.

## 4. Configuring Spring Boot

I have updated your `src/main/resources/application.properties` to match the credentials created above.

**Current Configuration:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/blockflow
spring.datasource.username=blockflow_user
spring.datasource.password=blockflow_password
```

## 5. Testing the Connection

1.  Open a terminal in your project folder (`blockflow-backend`).
2.  Run the application:
    ```powershell
    mvn spring-boot:run
    ```
3.  Watch the logs. You should see lines like:
    *   `HikariPool-1 - Starting...`
    *   `HikariPool-1 - Added connection org.postgresql.jdbc.PgConnection...`
    *   `Started BlockflowBackendApplication in ...`

If the application starts without error, the database connection is successful!

## 6. Troubleshooting

*   **Connection Refused:** Ensure PostgreSQL service is running (Task Manager -> Services -> postgresql-x64-16).
*   **Authentication Failed:** Double-check the password in `application.properties` matches what you set in Step 3.
*   **Database does not exist:** Ensure you ran `CREATE DATABASE blockflow;` successfully.
*   **Port Conflict:** If port 5432 is taken, you might need to change it in `postgresql.conf` and `application.properties`.
