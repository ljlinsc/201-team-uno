<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
		<title>201 UNO</title>
		<link href="https://fonts.googleapis.com/css2?family=Varta:wght@300;400;600&display=swap" rel="stylesheet">
    	<link rel="stylesheet" type="text/css" href="CSS/main.css">
  	</head>
  	<body>
  		<div class="newuser">
	  		<div class="header">
		    	<p class="sign-in-header">Create a new account</p>
		    </div>
		    <form name="signinform" method="POST" action="CreateUserServlet">
		    	<p class="invalid-user">${message}</p>
		    	<input id="userpass" type="text" placeholder="username" name="username" />
		        <input id="userpass" type="text" placeholder="password" name="password" />
		        <input id="userpass" type="text" placeholder="nickname" name="nickname" />
		        <input id="sign-in-submit" type="submit" name="signinsubmit" value="Enter" />
		    </form>
		</div>
	</body>
</html>