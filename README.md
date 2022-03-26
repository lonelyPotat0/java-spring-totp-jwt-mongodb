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

#### Signup
```
 /auth/sigup
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
 /auth/sigin
```
body
```json
{
  "username": "dom",
  "password": "123456",
  "TOTP": "324321" // TOTP is optional
}
```
#### Request Enable 
```
 /auth/sigup
```
body
```json
{
  "username": "dom",
  "password": "123456"
}
```



