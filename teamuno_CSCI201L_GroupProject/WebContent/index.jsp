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
  		<div class="main-wrapper">
		  	<div class="sign-in">
		  		<div class="header">
			    	<p class="sign-in-header">Sign In</p>
			    	<p class="invalid-user">${message}</p>
			    </div>
			    <form name="signinform" method="POST" action="SignInServlet">
			    	<input id="userpass" type="text" placeholder="username" name="username" />
			        <input id="userpass" type="password" placeholder="password" name="password" />
			        <input id="sign-in-submit" type="submit" name="signinsubmit" value="Enter" />
			    </form>
		    </div>
		    <div class="divider"></div>
		    <div class="other-options">
		    	<form name="createuserform" method="POST" action="newuser.jsp">
		    		<input id="yellow-button" type="submit" name="createusersubmit" value="Create a new account">
		    	</form>
		    	<form name="guestform" method="POST" action="GuestServlet">
		    		<input id="yellow-button" type="submit" name="guestsubmit" value="Enter as a guest">
		    	</form>
		    </div>
		</div>
	</body>
</html>