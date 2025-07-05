# JWT Authentication with Spring Boot

## Overview

This project demonstrates how to implement JWT (JSON Web Token) authentication in a Spring Boot application with Spring Security using **HTTP-only cookies** for enhanced security. It includes user registration, login, and JWT token generation, along with password hashing using BCrypt. Additionally, the system sends a welcome email to users upon successful registration.

## Features

- **Secure JWT authentication** using HTTP-only cookies (XSS protection)
- BCrypt password encryption
- Email notification system for new user registration
- Spring Boot-based backend with Spring Security integration
- Role-based access control (USER/ADMIN roles)
- Easy-to-understand and extendable codebase

## Security Improvements

This implementation addresses security concerns by:
- **Using HTTP-only cookies** instead of localStorage/sessionStorage
- **Preventing XSS attacks** by making tokens inaccessible to JavaScript
- **Automatic token transmission** with every request
- **Configurable cookie security settings** (Secure, SameSite, HttpOnly)

## Technologies Used

- **Spring Boot**: Framework for building the application
- **Spring Security**: For securing the APIs
- **JWT (JSON Web Token)**: For user authentication and token-based session management
- **BCrypt**: For secure password hashing
- **Java Mail**: For sending emails to users
- **HTTP-only Cookies**: For secure token storage

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java 8 or higher.
- Maven (for dependency management and build automation).
- Spring Boot 3.x.
- Access to an SMTP server for email functionality.

## Installation

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/AjayKumbham/jwt-auth-api.git
   cd jwt-auth-api
   ```

2. **Configure Application Properties**:

   Edit `src/main/resources/application.properties` to include your SMTP server configuration and JWT secret key.

   ```properties
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=your-email@example.com
   spring.mail.password=your-email-password
   jwt.secret=your-jwt-secret
   
   # JWT Cookie Configuration (for enhanced security)
   jwt.cookie.name=jwt-token
   jwt.cookie.max-age=1800
   jwt.cookie.secure=true
   jwt.cookie.http-only=true
   jwt.cookie.same-site=Strict
   ```

   Replace `your-email@example.com`, `your-email-password` and `your-jwt-secret` with your actual details.

3. **Database Configuration**

   To configure the MySQL database, update the following properties in `src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
   spring.datasource.username=your_database_username
   spring.datasource.password=your_database_password
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
   ```

   Replace `your_database_name`, `your_database_username`, and `your_database_password` with your actual MySQL details.

4. **Build and Run the Application**:
   
   First, clean and install the dependencies by running:

   ```bash
   mvn clean install
   ```
   Using Maven, run the following command to build and start the application:

   ```bash
   mvn spring-boot:run
   ```

## API Endpoints

The following are the available API endpoints in the application:

### 1. **Welcome Endpoint** (Non-Secure)

- **Method**: `GET`
- **Endpoint**: `http://localhost:8080/auth/welcome`
- **Description**: A simple, non-secure endpoint to welcome users.

- **Response**:

   ```plaintext
   Welcome, this endpoint is not secure.

### 2. **Register User**

- **Method**: `POST`
- **Endpoint**: `http://localhost:8080/auth/register`
- **Description**: Registers a new user. A welcome email is sent upon successful registration.
- **Request Body**:

   ```json
   {
     "username": "user123",
     "password": "password123",
     "email": "user@example.com",
     "roles":"ROLE_USER"
   }
   ```

- **Response**:

   ```plaintext
   User Added Successfully. A welcome email has been sent.
   ```

### 3. **Login**

- **Method**: `POST`
- **Endpoint**: `http://localhost:8080/auth/login`
- **Description**: Logs in the user and sets JWT token as HTTP-only cookie.
- **Request Body**:

   ```json
   {
     "username": "user123",
     "password": "password123"
   }
   ```

- **Response**:

   ```plaintext
   Login successful. JWT token set as HTTP-only cookie.
   ```

- **Security**: The JWT token is automatically set as an HTTP-only cookie and will be sent with subsequent requests.

### 4. **Logout**

- **Method**: `POST`
- **Endpoint**: `http://localhost:8080/auth/logout`
- **Description**: Logs out the user by clearing the JWT cookie.
- **Request**: No body required (cookie is automatically sent)

- **Response**:

   ```plaintext
   Logout successful. JWT cookie cleared.
   ```

### 5. **Get User Profile**

- **Method**: `GET`
- **Endpoint**: `http://localhost:8080/auth/user/user-profile`
- **Description**: Fetches the user's profile information. Requires authentication (JWT cookie).
- **Request**: No headers required (cookie is automatically sent)

- **Response**:

   ```plaintext
   Welcome to User Profile.
   ```

### 6. **Get Admin Profile**

- **Method**: `GET`
- **Endpoint**: `http://localhost:8080/auth/admin/admin-profile`
- **Description**: Fetches the admin's profile information. Requires authentication (JWT cookie) and ADMIN role.
- **Request**: No headers required (cookie is automatically sent)

- **Response**:

   ```plaintext
   Welcome to Admin Profile.
   ```

## Testing with Postman

You can use Postman to test the endpoints. Here are the steps for each:

1. **Register User**: 

   - Set the HTTP method to `POST`.
   - Use the `http://localhost:8080/auth/register` endpoint.
   - Add the request body as shown above.
   - Press "Send" and check the response.

2. **Login**:

   - Set the HTTP method to `POST`.
   - Use the `http://localhost:8080/auth/login` endpoint.
   - Add the login credentials in the request body.
   - Press "Send" - the JWT token will be set as an HTTP-only cookie automatically.

3. **Get User Profile**:

   - Set the HTTP method to `GET`.
   - Use the `http://localhost:8080/auth/user/user-profile` endpoint.
   - Press "Send" to view the profile data (cookie is automatically sent).

4. **Get Admin Profile**:

   - Set the HTTP method to `GET`.
   - Use the `http://localhost:8080/auth/admin/admin-profile` endpoint.
   - Press "Send" to view the admin profile data (cookie is automatically sent).

5. **Logout**:

   - Set the HTTP method to `POST`.
   - Use the `http://localhost:8080/auth/logout` endpoint.
   - Press "Send" to clear the JWT cookie.

**Note**: With HTTP-only cookies, you don't need to manually manage tokens. The browser automatically sends the cookie with each request to the same domain.

### Screenshots

screenshots of the API testing in Postman:

### 1. **Welcome Endpoint** (Non-Secure)
**Screenshot**:
![Welcome Endpoint](screenshots/screenshot1.png)

---

### 2. **Register Endpoint**
**Screenshot**:
![Register Endpoint](screenshots/screenshot2.png)

---

### 3. **Login Endpoint**
**Screenshot**:
![Login Endpoint](screenshots/screenshot3.png)

---

### 4. **User Profile Endpoint**
**Screenshot**:
![User Profile Endpoint](screenshots/screenshot4.png)

---

### 5. **JWT Token Encoding and Decoding**
Here's how the JWT token is decoded using [JWT.io](https://jwt.io/).

**Screenshot**:
![JWT Token Decoded](screenshots/screenshot5.png)

---

### 6. **MySQL Database (UserInfo Table)**
After registering a user, you can verify the data in the MySQL `UserInfo` table.
  
**Screenshot**:
![User Info in MySQL](screenshots/screenshot6.png)


## Contributing

Contributions are welcome! If you have suggestions for improvements or bug fixes, please open an issue or submit a pull request.

### How to Contribute:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Make your changes.
4. Commit your changes (`git commit -am 'Add feature'`).
5. Push to the branch (`git push origin feature/your-feature`).
6. Create a new pull request.

## License

This project is open-source and available under the [MIT License](LICENSE).

---

