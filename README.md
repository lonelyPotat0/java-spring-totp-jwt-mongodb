# Java Spring JWT + Authenticator APP With MongoDB

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
	docker-compose up -d
	mvn spring-boot:run
```
	  or just use intellij

### Using 

 All requests are post request 

#### Signup
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
#### Signpin
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
#### Request Authkey 
```
 /totp/request-authkey
```
 * Require Bearer token


#### Request enable 2-factor authentication

register secret key (authKey) in authenticator app

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

register secret key (authKey) in authenticator app

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



