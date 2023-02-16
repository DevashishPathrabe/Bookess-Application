Bookess Application
===================  

Bookess is an application written using Spring boot.  

Before running the application please enter your mySQL username and password in application.properties file.  

Below are the REST API endpoints available:  

```
POST /register
Body:
{
	"name": <String>,
	"username": <String>,
	"password": <String>,
	"isAdmin": <boolean>
}
```
Note: username should be unique for a user and it can't be changed.  
If ('isAdmin': false) then you will be registered as a user and if ('isAdmin': true) then you will be registered as an admin.  
Accessible to: Admin and User  

```
POST /login
Body:
{
	"username": <String>,
	"password": <String>
}
```
To login user. User should be registered before accessing this endpoint.  
User will be provided a JWT token once login is successful. This JWT token should be provided as a Bearer token in the Authorization header while accessing below endpoints.  
e.g. `Authorization: Bearer <JWT token>`  
Accessible to: Admin and User  

```
GET /signout
```
To logout user.
Accessible to: Admin and User  
Authorization header required to access this endpoint.  

```
GET /books
```
To get the list of available books.  
Accessible to: Admin and User  

```
GET /books/id
```
To get the book details of a given Book ID.  
Accessible to: Admin  
Authorization header required to access this endpoint.  

```
POST /books
Body:
{
	"name": <String>,
	"author": <String>,
	"price": <float>
}
```
To create a new book.  
Accessible to: Admin  
Authorization header required to access this endpoint.  

```
PUT /books/id
Body:
{
	"name": <String>,
	"author": <String>,
	"price": <float>
}
```
To update book details of a given Book ID.  
Book attributes that can be updated are 'name', 'author' and 'price'.  
Accessible to: Admin  
Authorization header required to access this endpoint.  

```
PATCH /books/id
Body:
{
	"name": <String>
}
```
To update book details of a given Book ID.  
Book attributes that can be updated are 'name' and/or 'author'.  
Accessible to: Admin  
Authorization header required to access this endpoint.  

```
DELETE /books/id
```
To delete the book with a given Book ID.  
Accessible to: Admin  
Authorization header required to access this endpoint.  

```
DELETE /books
```
To delete all the books.  
Accessible to: Admin   
Authorization header required to access this endpoint.  

```
GET /users
```
To get the list of available users.  
Accessible to: Admin  
Authorization header required to access this endpoint.  

```
GET /users/id
```
To get the user details of a given User ID.  
Accessible to: Admin  
Authorization header required to access this endpoint.  

```
POST /users
Body:
{
	"name": <String>,
	"username": <String>,
	"password": <String>,
	"isAdmin": <boolean>
}
```
To create a new user.
Accessible to: Admin  
Authorization header required to access this endpoint.  

```
PUT /users/id
Body:
{
	"name": <String>,
	"isAdmin": <boolean>
}
```
To update user details of a given User ID.  
User attributes that can be updated are 'name' and 'isAdmin'.  
Accessible to: Admin  
Authorization header required to access this endpoint.  

```
PATCH /users/id
Body:
{
	"name": <String>
}
```
To update user details of a given User ID.  
User attribute that can be updated is 'name'.  
Accessible to: Admin  
Authorization header required to access this endpoint.  

```
DELETE /users/id
```
To delete the user with a given User ID.  
Accessible to: Admin  
Authorization header required to access this endpoint.  

```
DELETE /users
```
To delete all the users.  
Accessible to: Admin  
Authorization header required to access this endpoint.  

```
GET /users/liked-books
```
To get the list of user's liked books.  
Accessible to: User  
Authorization header required to access this endpoint.  

```
POST /users/liked-books
Body:
<Integer>
```
To add book with a given Book ID to the list of user's liked books.  
Accessible to: User  
Authorization header required to access this endpoint.  

```
GET /users/read-lated-books
```
To get the list of user's read later books.  
Accessible to: User  
Authorization header required to access this endpoint.  

```
POST /users/read-later-books
Body:
<Integer>
```
To add book with a given Book ID to the list of user's read later books.  
Accessible to: User  
Authorization header required to access this endpoint.  
