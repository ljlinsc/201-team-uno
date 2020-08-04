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
		<div class="sign-in-wrapper">
			<div class="sign-in">
				<div class="sign-in-header">
					<p class="header">Sign In</p>
					<p class="invalid">${message}</p>
				</div>
				<form name="signinform" method="POST" action="SignInServlet">
					<input class="gray-text-input" type="text" placeholder="username" name="username" /> 
					<input class="gray-text-input" type="password" placeholder="password" name="password" /> 
					<input class="submit" type="submit" name="signinsubmit" value="Enter" />
				</form>
			</div>
			<div class="divider"></div>
			<div class="other-options">
				<form name="createuserform" method="POST" action="newuser.jsp">
					<input class="yellow-button" type="submit" name="createusersubmit" value="Create a new account">
				</form>
				<form name="guestform" method="POST" action="GuestServlet">
					<input class="yellow-button" type="submit" name="guestsubmit" value="Enter as a guest">
				</form>
			</div>
		</div>
	</body>
</html>