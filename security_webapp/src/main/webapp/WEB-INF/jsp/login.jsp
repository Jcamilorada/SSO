<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">

    <link rel="stylesheet" href="css/login.css">

    <meta charset="UTF-8">
    <title>Login form</title>
</head>
<body>
<div class="container">
    <div class="wrapper">
        <form action="login" method="post" name="Login_Form" class="form-signin">
            <h3 class="form-signin-heading">Please Sign In</h3>
            <hr class="colorgraph">
            <br>
            <input type="text" value="dcgonzalez" class="form-control" name="Username" placeholder="Username" required="true" autofocus="true"/>
            <input type="password" value="dcga123" class="form-control" name="Password" placeholder="Password" required="true"/>
            <input type="hidden" name="RedirectUrl" value="${RedirectUrl}" />
            <button type="submit" class="btn btn-lg btn-primary btn-block" name="Submit" value="Login">Login</button>

            <br>
            <div class="alert alert-danger" role="alert">
              <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
              <span class="sr-only">Error:</span>${error}
            </div>
        </form>
    </div>
</div>
</body>
</html>