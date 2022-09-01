# iRembo User Account Management API

A Restful Backend Service which provide the ability to save, retrieve and verify User Information.
It contains CRUD Operations (CREATE, READ, UPDATE & DELETE).

Validation has been implemented on the User Creation and Update which is:
- [x] Email checked for Uniqueness.

Integration Testing has been done, For Test to be effective UserController & LoginController 
has been tested using Postman.

For compiling the application in Production run the IremboBackBash.sh
file, After successful packaging you can now run the MavenBash in
the IremboDeployment Project.

The iRembo User Account Software has been Dockerized both the Database & App.