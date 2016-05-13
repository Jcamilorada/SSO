#Single sign on (SSO) system synchronized with Lightweight Directory Access Protocol (LDAP).

##Modules
1. ldap_server. Standalone ldap server for testing purpose.
2. security_library. Base library with security utils and classes.
3. security_rest. Rest service application to perform security operations (login, logout, validate tokens)
4. security_web. Common user interface for authentication.
5. webapp1, webapp2. Single page application using angular integrated in the sso system.

## How to run project.

1. Compile project using

````
    gradle ./gradlew webapp1:npm_setup webapp2:npm_setup build
