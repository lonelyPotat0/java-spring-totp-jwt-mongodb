# Java Spring + JWT + TOTP With MongoDB

## Initiate project

### First you'll need to install
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
  "TOTP": "324321" // TOTP is optional, only require when 2fa is enabled
}
```

#### Check 2FA Status 

path
```
 /totp/check-2fa-status
```
 * Require Bearer token

#### Request Authkey 

path
```
 /totp/request-authkey
```
 * Require Bearer token


#### Request enable 2-factor authentication

 * First register secret key (authKey) in authenticator app

path
```
 /totp/enable
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
 /totp/disable
```
body
```json
{
  "TOTP": "123123"  // code from authenticator app 
}
```
 * Require Bearer token



