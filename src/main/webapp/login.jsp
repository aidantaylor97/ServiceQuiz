<link href="quizStyles.css" rel="stylesheet" type="text/css"/> 
<link href="../quizStyles.css" rel="stylesheet" type="text/css"/> 
<html> 
    <head>
        <title>Login / signup page</title>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <script type="text/javascript">
			function validateLogin(){
				//Get login form ID
				var form = document.getElementById("loginForm");
				//Creates a username for the user	
				var username = document.getElementById("loginUsername");
				//If the value is more than 7 characters long
				if(username.value.length > 7){
					//enable login button
					document.getElementById("loginSubmit").disabled = false;
				}
				else{
					document.getElementById("loginSubmit").disabled = true;
				}
			}		
		</script>
        <script type="text/javascript">
			var request;
			//Gather dom element for message

			//Requests validation of a given username
			function requestValidation(username) {
				if(validateRegister(username) == true)
				{
					//Create new XMLHttpRequest
					request = new XMLHttpRequest();
					//Open the request forwarding to the controller
					request.open("POST", "http://localhost:9090/QuizService/do/validateRegistration", true);
					//Callback method call
					request.onreadystatechange = function(){
						processResponse();
					};
					request.setRequestHeader("Content-type", "application/x-www-form-urlencoded")
					//Send request with the username parameter
					request.send("username=" + username);
				}
				else
				{
					//Set display as a variable
					var display = document.getElementById("display");
					//Message to warn user username must be above 7 characters
					 display.innerHTML = "<p style=\"color:red\" id=\"message\">Username must be above 7 characters";
					 //Disables the form button incase it was enabled by a previous attempt
					 document.getElementById("registerSubmit").disabled = true;
				 } 
			}


			//If the response contains a false value, enable the disabled form button
			function processResponse(data) {
				//If response is recieved
				if (request.readyState == 4 && request.status == 200) {
					//Set display as a variable
					var display = document.getElementById("display");
					//Sets up a variable for the response
					var valid = request.responseXML.documentElement;
					var v = valid.childNodes[0].childNodes[0];
					console.log(v.data);
					//If false value returned
					if(v.data === "false"){
						//Remove message if one exists
						if(document.getElementById('message') !== null)
						{
							document.getElementById("message").remove();
						}
						//Enable submit button
						document.getElementById("registerSubmit").disabled = false;
					}
					else{
						//Message for user to try another username
						display.innerHTML = "<p style=\"color:red\" id=\"message\">This username already exists, please try another name</p>";
						//Disables the form button incase it was enabled by a previous attempt
						document.getElementById("registerSubmit").disabled = true;  
					}   
				}
			}
			//Returns true if username is more than 7 characters
			function validateRegister(username)
			{
				//If the value is more than 7 characters long
				if(username.length > 7){
					//return true
					return true;
				}
				else{
					return false;
				}
			}
		</script>
    </head>
    <body>
    	<h1>Welcome to Quiz Service!</h1>
    	<div class="topContainer">
    		<div class="loginC">
		        <h2>Please log in!</h2>
		           	<form id="loginForm" method="POST" action="/QuizService/do/login">
		                <input type="text" id="loginUsername" class="usernameE" name="username" onchange="validateLogin()" value="" placeholder="Username"/>
		                <input type="password" class="passwordE" name="password" value="" placeholder="Password"/>        
				        <input type="submit" id="loginSubmit" class="loginBtn" disabled="disabled" name="submit" value="Log in" />
				  	</form>
		      	<div id="error" class="error"></div>  	
	        </div>
	    </div>
	    <div class="topContainer">
	    	<div class="loginC">
		        <form id="registerForm" method="POST" action="/QuizService/do/addUser"">
		            <h2> Don't yet have an account? </h2>
			            <input type="text" name="username" class="usernameE" onchange="requestValidation(this.value)" value="" placeholder="Username"/>
			            <input type="password" class="passwordE" name="password" value="" placeholder="Password"/>      
			            <input type="submit" disabled="disabled" id="registerSubmit" class="loginBtn" value="Sign up"/>
		        </form>
		        <div id="display" class="error"></div>
		 	</div>
         </div>
    </body>
</html>
