<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
  <head>
    <title>Sign In</title>
  </head>
  <body>
    <h1>Sign In</h1>
    <form name="signinform" method="GET" action="SignInServlet">
      <table>
        <tr>
          <td><input type="text" placeholder="username" name="username" /></td>
        </tr>
        <tr>
          <td><input type="password" placeholder="password" name="password" /></td>
        </tr>
        <tr>
          <td colspan="2">
            <input type="submit" name="submit" value="Submit Form" />
          </td>          
        </tr>
      </table>
    </form>
  </body>
</html>