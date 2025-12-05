# 🔐 Spring Security JWT Authentication

Spring Boot 3.2.0 ile JWT tabanlı kimlik doğrulama sistemi. Register, Login ve korumalı endpoint'ler içerir.

---

## 📋 İçindekiler

- [Teknolojiler](#teknolojiler)
- [Özellikler](#özellikler)
- [Kurulum](#kurulum)
- [Proje Yapısı](#proje-yapısı)
- [API Kullanımı](#api-kullanımı)
- [Nasıl Çalışır](#nasıl-çalışır)
- [Güvenlik](#güvenlik)

---

## 🛠 Teknolojiler

- **Spring Boot** 3.2.0
- **Spring Security** 6.x
- **JWT (JSON Web Token)** - io.jsonwebtoken 0.11.5
- **MySQL** 8.x
- **JPA/Hibernate** - Veritabanı işlemleri
- **Lombok** - Boilerplate kod azaltma
- **Maven** - Dependency yönetimi

---

## ✨ Özellikler

- ✅ Kullanıcı kaydı (Register)
- ✅ Kullanıcı girişi (Login)
- ✅ JWT token üretimi
- ✅ Token tabanlı kimlik doğrulama
- ✅ Şifre şifreleme (BCrypt)
- ✅ Rol bazlı yetkilendirme (ROLE_USER, ROLE_ADMIN)
- ✅ Korumalı endpoint'ler
- ✅ MySQL veritabanı entegrasyonu

---

## 🚀 Kurulum

### 1. Gereksinimler

- Java 17 veya üzeri
- Maven 3.6+
- MySQL 8.x
- IntelliJ IDEA / Eclipse 

### 2. MySQL Database Oluştur

```sql
CREATE DATABASE security_db;
```

### 3. application.properties Ayarları

`src/main/resources/application.properties` dosyasını oluştur:

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/security_db

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT Configuration
jwt.secret=your_secret_key
jwt.expiration=86400000
```

> ⚠️ **Önemli**: `jwt.secret` değerini production'da mutlaka değiştirin!

### 4. Projeyi Çalıştır

```bash
mvn clean install
mvn spring-boot:run
```

Uygulama `http://localhost:8080` adresinde çalışacaktır.

---

## 📁 Proje Yapısı

```
src/main/java/com/example/security1/
├── config/
│   └── SecurityConfig.java          # Spring Security ayarları
├── controller/
│   ├── AuthController.java          # Login/Register endpoint'leri
│   └── TestController.java          # Korumalı test endpoint'leri
├── dto/
│   ├── LoginRequest.java            # Login request objesi
│   ├── RegisterRequest.java         # Register request objesi
│   └── AuthResponse.java            # JWT response objesi
├── entity/
│   └── User.java                    # User entity (veritabanı modeli)
├── filter/
│   └── JwtAuthenticationFilter.java # JWT doğrulama filter'ı
├── repository/
│   └── UserRepository.java          # User repository (JPA)
├── service/
│   └── CustomUserDetailsService.java # UserDetailsService implementasyonu
├── util/
│   └── JwtUtil.java                 # JWT utility sınıfı
└── Security1Application.java        # Ana uygulama sınıfı
```

---

## 🔌 API Kullanımı

### 1. Kullanıcı Kaydı (Register)

**Endpoint**: `POST /api/auth/register`

**Request Body**:
```json
{
  "username": "ahmet",
  "email": "ahmet@example.com",
  "password": "12345678",
  "roles": ["ROLE_USER"]
}
```

**Response**:
```json
{
  "message": "User registered successfully!"
}
```

---

### 2. Kullanıcı Girişi (Login)

**Endpoint**: `POST /api/auth/login`

**Request Body**:
```json
{
  "username": "ahmet",
  "password": "12345678"
}
```

**Response**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "ahmet"
}
```

---

### 3. Korumalı Endpoint Kullanımı

**Endpoint**: `GET /api/test/user`

**Headers**:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response**:
```
Hello User!
```

---

## 📖 Nasıl Çalışır

### 1. Register İşlemi

```
1. Kullanıcı username, email, password gönderir
   ↓
2. AuthController isteği alır
   ↓
3. Username ve email unique mi kontrol edilir
   ↓
4. Şifre BCrypt ile hashlenir
   ↓
5. User veritabanına kaydedilir
   ↓
6. Başarılı mesajı döner
```

### 2. Login İşlemi

```
1. Kullanıcı username ve password gönderir
   ↓
2. Spring Security AuthenticationManager devreye girer
   ↓
3. CustomUserDetailsService veritabanından user'ı bulur
   ↓
4. Şifre BCrypt ile karşılaştırılır
   ↓
5. Doğruysa JwtUtil ile token üretilir
   ↓
6. Token client'a döner
```

### 3. Korumalı Endpoint'e Erişim

```
1. Client token'ı Authorization header'ında gönderir
   ↓
2. JwtAuthenticationFilter her isteği yakalar
   ↓
3. Token'dan username çıkarılır
   ↓
4. Token signature kontrol edilir
   ↓
5. Token geçerliyse SecurityContext'e authentication set edilir
   ↓
6. İstek controller'a ulaşır
```

---

## 🔒 Güvenlik

### JWT Token Yapısı

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhaG1ldCIsImlhdCI6MTczNTkyMDAwMH0.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c

[HEADER].[PAYLOAD].[SIGNATURE]
```

- **Header**: Algoritma bilgisi (HS256)
- **Payload**: Kullanıcı bilgileri (username, expiration)
- **Signature**: Secret key ile imzalanmış hash

### Güvenlik Özellikleri

- ✅ Şifreler **hiçbir zaman plain text** olarak saklanmaz
- ✅ BCrypt ile **one-way hashing**
- ✅ JWT signature ile **token manipülasyonu engellenir**
- ✅ Token expiration ile **oturum süresi sınırlanır**
- ✅ Stateless authentication (sunucuda session yok)
- ✅ CSRF koruması devre dışı (stateless olduğu için gerekli değil)

### Production İçin Öneriler

1. **JWT Secret**: Güçlü ve uzun bir secret key kullanın (min 256-bit)
2. **HTTPS**: Mutlaka HTTPS kullanın (token şifreli değil!)
3. **Token Expiration**: Kısa süreli tokenlar kullanın (1-24 saat)
4. **Refresh Token**: Uzun oturumlar için refresh token mekanizması ekleyin
5. **Rate Limiting**: Brute force saldırılarına karşı rate limiting ekleyin
6. **Input Validation**: Tüm kullanıcı inputlarını validate edin

---

## 🧪 Test Etme (Postman)

### 1. Register
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "test",
  "email": "test@test.com",
  "password": "password123",
  "roles": ["ROLE_USER"]
}
```

### 2. Login
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "test",
  "password": "password123"
}
```

### 3. Protected Endpoint
```http
GET http://localhost:8080/api/test/user
Authorization: Bearer <your-token-here>
```



## 📝 Lisans

Bu proje eğitim amaçlıdır ve özgürce kullanılabilir.

---



**Happy Coding! 🚀**
