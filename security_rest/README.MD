# Security Rest Application

Contains security rest service.

## 1. Login
Rest service that enable creation of logging request. If success retrieve security token.

### URL

http://server_url:server_port/security-rest/rest/login

### Example payload
>{
    "user" : "jcrada",
    "password": "12314"
}

### Answer (Success), status code : 200 ok

>{
  "token": "MQ==.Z+b5Atrwi7qjPJgkO2P/ffUD8Pn7x1mLyopY+fZjCWc=",
  "valid": true
}

### Answer (Error), status code 401 Unauthorized

>{
  "message": "Invalid username or password"
}

## 2. Validate
Validates the given token, if is valid retrieve user information and role in the given application.

### URL

http://server_url:server_port/security-rest/rest/validate

### Example payload
>{
    "token" : "NA==.RsqbrYoBO8BCmIbFzOGKF109GxshPPTgiJc4kDT/U/w=",
    "application" : "webapp1"
}

### Answer (Success), status code : 200 ok

>{
   "name": "jrada",
   "username": "Juan Rada",
   "email": "jcamilorada@gmail.com",
   "roles": [
     "admin"
   ]
 }
### Answer (Error), status code 401 Unauthorized

>{
   "message": "Invalid security token"
}

## 3. Logout
Logout the user with the current security token.

### URL

http://server_url:server_port/security-rest/rest/logout

### Example payload
>{
    "token" : "NA==.RsqbrYoBO8BCmIbFzOGKF109GxshPPTgiJc4kDT/U/w="
}

### Answer (Success), status code : 200 ok

### Answer (Error), status code 401 Unauthorized

>{
   "message": "Invalid security token"
}
