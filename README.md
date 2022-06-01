# Java Spring + JWT + TOTP With MongoDB

## Initiate project

### First you may need to install
<ul> 
<li>docker</li>
<li>docker-compose</li>
<li>maven cli</li>
<li>jdk 1.8</li>  
</ul>

### Run project 

```console
mvn clean install
docker-compose up -d //start mongodb server
mvn spring-boot:run
```
	  or just use intellij/eclipse
	  server run on port 8080

## Using 

 All requests are post request 

#### Signup

path
```
 /api/auth/signup
```
body
```json
{
  "username": "dom",
  "password": "123456"
}
```
#### Signin

path
```
  /api/auth/signin
```
body
```json
{
  "username": "dom",
  "password": "123456",
  "TOTP": "324321" // code from authenticator app, is optional, only require when 2fa is enabled
}
```

#### Check 2FA Status 

path
```
 /api/auth/check-2fa-status
```
 * Require Bearer token

#### Request Secret Key 

path
```
 /api/auth/request-secretkey
```
 * Require Bearer token


#### Request enable 2-factor authentication

 * First register secret key in authenticator app

path
```
 /api/auth/enable-totp
```
body
```json
{
  "TOTP": "123123"  // code from authenticator app 
}
```
 * Require Bearer token

#### Request disable 2-factor authentication

path
```
 /api/auth/disable-totp
```
body
```json
{
  "TOTP": "123123"  // code from authenticator app 
}
```
 * Require Bearer token



