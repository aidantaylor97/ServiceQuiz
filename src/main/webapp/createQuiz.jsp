<%@ page contentType="text/html;charset=utf8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
	
    <!-- any content can be specified here, e.g.: -->
    <link href="../quizStyles.css" rel="stylesheet" type="text/css"/>
    
    
    <jsp:element name="javascript">
	<script>
		function validateQuiz(){
			//Get login form ID
			var form = document.getElementById("createQuizTitle");
			//Creates a username for the user	
			var quizTitle = document.getElementById("quizTitle");
			var quizAmount = document.getElementById("quizAmount");
			var quizDesc = document.getElementById("quizDesc");
			var quizDiff = document.getElementById("quizDiff");
			//If the value is more than 7 characters long
			if((quizTitle.value.length > 0) && (quizAmount.value > 0 && quizAmount.value < 10) 
			&& (quizDesc.value.length > 5) && (quizDiff.value.length > 0))
			{
				//enable login button
				document.getElementById("quizTitleSubmit").disabled = false;
			}
			else
			{
				document.getElementById("quizTitleSubmit").disabled = true;
			}
		}	
	</script>
	</jsp:element>
	<jsp:element name="html">
        <jsp:attribute name="lang">EN</jsp:attribute>
        <jsp:body>
        	<jsp:include page="navigation.jspx" />
            <h1>Create your Quiz!</h1>
            <div class="topContainer">  
            	<div class="createQForm">
					<form id="createQuizTitle" method="POST" action="/QuizService/do/quizTitle" enctype="multipart/form-data">
		      			<div class="qTitleC">
		                	<p>Quiz Title</p>
		                	<input type="text" id="quizTitle" class="qTitleE fText" name="quizTitle" onchange="validateQuiz()" value="" />
		                </div>
		                <div class="qTitleC">
		                	<p>Quiz Description</p>
		                	<input type="text" id="quizDesc" class="qDescE fText" name="quizDesc" onchange="validateQuiz()" value="" />
		                </div>
		                <div class="formFlex">
			                <div class="qAmountC">
			                	<p>Question Amount</p>
			                	<input type="number" id="quizAmount" class="qAmountE fText" name="quizAmount" onchange="validateQuiz()" value="" />
			                </div>
		                	<div class="qDiffC">
				                <p>Quiz Difficulty</p>
				                <select name="quizDiff" class="qDiffE fText" id="quizDiff" onchange="validateQuiz()">
				                	<option value="Low">Low</option>
									<option value="Medium">Medium</option>
									<option value="High">High</option>
								</select>
							</div>
						</div>
						<div class="qTitleC">
							<p>Optionally, insert an image for your quiz<p>
							<input type="file" id="quizImage" class="fileE" name="quizImage" accept="image/*" />	
						</div>
						<div id="error" style="color:red;"></div>
				        <input type="submit" id="quizTitleSubmit" class="createQBtn1" disabled="disabled" name="submit" value="Create quiz questions" />
				  	</form>	
			  	</div>
		  	</div>  	
        </jsp:body>
    </jsp:element>

