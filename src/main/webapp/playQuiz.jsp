<%@ page contentType="text/html;charset=utf8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
	
    <!-- any content can be specified here, e.g.: -->
	<link href="../quizStyles.css" rel="stylesheet" type="text/css"/>
	
     <script type="text/javascript">
     
			// Create a question type global variable
			var questionCount = 0;
			var questionList;
			var answeredCorrectly;
			var score = 0;
			
     		function createQuestions()
     		{
				var qCounter = 0;
				var aCounter = 0;
				var questions = [];
				//For each entry in questions
				<c:forEach var="entry" items="${quiz.questions}">
					//create question and answer objects
					question = {questionText: '', qType: '', answers: []};
					//Adds Questions to the variable	
					questions[qCounter] = question;
					questions[qCounter].questionText = '${entry.value.questionText}'
					questions[qCounter].qType = '${entry.value.QType}'
					//Adds answers to the variable
					<c:forEach var="answer" items="${entry.value.answers}">
						if(questions[qCounter].qType === 'true/false')
						{
							//create answer object
							answer =  {isCorrect: '', answerString: ''};
							questions[qCounter].answers[aCounter] = answer;
							questions[qCounter].answers[aCounter].isCorrect = '${answer.value.isCorrect}'
						}
						else
						{
							//create answer object
							answer =  {isCorrect: '', answerString: ''};
							questions[qCounter].answers[aCounter] = answer;
							questions[qCounter].answers[aCounter].answerString = '${answer.value.answerString}'
							questions[qCounter].answers[aCounter].isCorrect = '${answer.value.isCorrect}'
						}
						// Increase answer counter
						aCounter++;
					</c:forEach>;
					//Reset answer Counter and increase question counter
					aCounter = 0;
					qCounter++;
				</c:forEach>;	
				//hide start quiz button
				document.getElementById("startBtn").hidden = "hidden";
				//Load the question list to a global variable
				questionList = questions;
				//Load the first question and update HTMl
				loadQuestion();
     		}

     	//function called by buttons to load a question and its div onto the page
     	function loadQuestion()
     	{
     		//load document elements
     		var trueFalseC = document.getElementById("trueFalseC");
     		var multiChoiceC = document.getElementById("multiChoiceC");
     		var submitFDiv = document.getElementById("submitFDiv")
     		//If there are still more questions
     		if(questionList.length != questionCount)
     		{	
     			//if the question type is true/false
	     		if(questionList[questionCount].qType === 'true/false')
	     		{
	     			//show question values
	     			multiChoiceC.hidden = "hidden";
	     			trueFalseC.hidden = false;
	     			document.getElementById("startBtn").hidden = "hidden";
	     			document.getElementById("questionCountTf").innerHTML = "Question " + (questionCount+1)+"/"+ '${quiz.qAmount}';
	     			document.getElementById("questionNameTf").innerHTML = questionList[questionCount].questionText;
	     			//change colour of buttons
	     			document.head.insertAdjacentHTML("beforeend", '<style>.trueBtn{background-color:black}</style>')
	     			document.head.insertAdjacentHTML("beforeend", '<style>.falseBtn{background-color:black}</style>')
	     			document.getElementById("trueBtn").disabled = false;
	         		document.getElementById("falseBtn").disabled = false;
	     		}
	     		//else its a multiple choice
	     		else
	     		{
	     			//show question values
	     			multiChoiceC.hidden = false;
	     			trueFalseC.hidden = "hidden";
	     			document.getElementById("questionCountMc").innerHTML = "Question " + (questionCount+1)+"/"+ '${quiz.qAmount}';
	     			document.getElementById("questionNameMc").innerHTML = questionList[questionCount].questionText;
	     			//get the buttons
	     			mCBtn1 = document.getElementById("mCBtn1");
	     			mCBtn2 = document.getElementById("mCBtn2");
	     			mCBtn3 = document.getElementById("mCBtn3");
	     			//Change the colours
	     			//change button colours, and enable button for the next question
         			document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn1{background-color:black}</style>')
         			document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn2{background-color:black}</style>')
     				document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn3{background-color:black}</style>')
	     			//enable them 
	     			mCBtn1.disabled = false;
	     			mCBtn2.disabled = false;
	     			mCBtn3.disabled = false;
	     			//Display answer options
	     			mCBtn1.innerHTML = questionList[questionCount].answers[0].answerString;
	     			mCBtn2.innerHTML = questionList[questionCount].answers[1].answerString;
	     			mCBtn3.innerHTML = questionList[questionCount].answers[2].answerString;
	     			//Add hidden values (if answer is correct)
	     			mCBtn1.value = questionList[questionCount].answers[0].isCorrect;
	     			mCBtn2.value = questionList[questionCount].answers[1].isCorrect;
	     			mCBtn3.value = questionList[questionCount].answers[2].isCorrect;
	     		}
     		}
     		else
     		{
     			//show question values
     			multiChoiceC.hidden = "hidden";
     			trueFalseC.hidden = "hidden";
     			submitFDiv.hidden = false;
     			document.getElementById("submitScore").innerHTML = "You Scored </br>" + score + " Points!";	
     		}
     	}
     	
     	//Selecting a true false answer
     	function trueFalseSelect(truefalse, button)
     	{
     		//load document elements
     		var trueBtn = document.getElementById("trueBtn");
     		var falseBtn = document.getElementById("falseBtn");
     		var nextBtn = document.getElementById("nextBtnTf");
     		var btnSelected = button;
     		//if the answer is correct (false matches false, true matches true)
     		if(questionList[questionCount].answers[0].isCorrect === truefalse)
     		{
     			//if the button selected was the true button
     			if(button.id === "trueBtn")
     			{
	     			//change button colours, and enable button for the next question
	     			document.head.insertAdjacentHTML("beforeend", '<style>.trueBtn{background-color:rgb(19,134,7)}</style>')
	     			document.head.insertAdjacentHTML("beforeend", '<style>.falseBtn{background-color:rgb(216,14,21)}</style>')
	     			nextBtn.hidden = false;
	     			answeredCorrectly = true;
     			}
     			else
     			{
     				//change button colours, and enable button for the next question
	     			document.head.insertAdjacentHTML("beforeend", '<style>.trueBtn{background-color:rgb(216,14,21)}</style>')
     				document.head.insertAdjacentHTML("beforeend", '<style>.falseBtn{background-color:rgb(19,134,7)}</style>')
	     			nextBtn.hidden = false;
	     			answeredCorrectly = true;
     			}
     		}		
     		else
     		{
     			if(button.id === "falseBtn")
     			{
	     			//change button colours, and enable button for the next question
	     			document.head.insertAdjacentHTML("beforeend", '<style>.trueBtn{background-color:rgb(19,134,7)}</style>')
	     			document.head.insertAdjacentHTML("beforeend", '<style>.falseBtn{background-color:rgb(216,14,21)}</style>')
	     			nextBtn.hidden = false;
	     			answeredCorrectly = false;
	     		}
     			else
     			{
     				//change button colours, and enable button for the next question
	     			document.head.insertAdjacentHTML("beforeend", '<style>.trueBtn{background-color:rgb(216,14,21)}</style>')
     				document.head.insertAdjacentHTML("beforeend", '<style>.falseBtn{background-color:rgb(19,134,7)}</style>')
	     			nextBtn.hidden = false;
	     			answeredCorrectly = true;
     			}
     		}
     		//disable buttons
     		falseBtn.disabled = true;
     		trueBtn.disabled = true;
     	} 	
     	
     	//Submit answer will submit the answer which the user has selected, add points to a score
     	//and modify the page to present the next question
     	function submitAnswer(questionType)
     	{
     		//if Question type is True False
     		if(questionType === 'TF')
     		{
	     		if(answeredCorrectly == true)
	     		{
	     			questionCount++;
	     			score = score + 10;
	     			document.getElementById("nextBtnTf").hidden = true;
	     			loadQuestion();	
	     		}
	     		else
	     		{
	     			questionCount++;
	     			document.getElementById("nextBtnTf").hidden = true;
	     			loadQuestion();
	     		}     		
     		}
     		// if question type is MC
     		else if(questionType === 'MC')
     		{
     			if(answeredCorrectly == true)
	     		{
	     			questionCount++;
	     			score = score + 10;
	     			document.getElementById("nextBtnMc").hidden = true;
	     			loadQuestion();	
	     		}
	     		else
	     		{
	     			questionCount++;
	     			document.getElementById("nextBtnMc").hidden = true;
	     			loadQuestion();
	     		} 
     		}
     	}
     	
     	function multiChoiceSelect(button)
     	{
     		//load document elements
     		var mCBtn1 = document.getElementById("mCBtn1");
     		var	mCBtn2 = document.getElementById("mCBtn2");
     		var mCBtn3 = document.getElementById("mCBtn3");
     		var nextBtn = document.getElementById("nextBtnMc");
     		//if the answer is correct (true false value was hidden in the button on push)
     		if(button.value === 'true')
     		{
     			//matches the button ID to correctly change the colour
     			if(button.id === "mCBtn1")
     			{
     				//change button colours, and enable button for the next question
         			document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn1{background-color:rgb(19,134,7)}</style>')
         			document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn2{background-color:rgb(216,14,21)}</style>')
     				document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn3{background-color:rgb(216,14,21)}</style>')
     				nextBtn.hidden = false;
         			answeredCorrectly = true;
     			}
     			//matches the button ID to correctly change the colour
     			else if(button.id === "mCBtn2")
     			{
     				//change button colours, and enable button for the next question
         			document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn1{background-color:rgb(216,14,21)}</style>')
         			document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn2{background-color:rgb(19,134,7)}</style>')
     				document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn3{background-color:rgb(216,14,21)}</style>')
     				nextBtn.hidden = false;
         			answeredCorrectly = true;
     			}
     			else if(button.id === "mCBtn3")
     			{
     				//change button colours, and enable button for the next question
         			document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn1{background-color:rgb(216,14,21)}</style>')
         			document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn2{background-color:rgb(216,14,21)}</style>')
     				document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn3{background-color:rgb(19,134,7)}</style>')
     				nextBtn.hidden = false;
         			answeredCorrectly = true;
     			}
     		}
     		//button value is false (incorrect answer)
     		else
     		{
     			//matches the button ID to correctly change the colour
     			if(button.id === "mCBtn1")
     			{	
     				//change button colours, and enable button for the next question
         			document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn1{background-color:rgb(216,14,21)}</style>')
         			//if statements used to determine which answer was correct and ultimately
     				//which button to change the colour of
     				if(mCBtn2.value === 'true')
     				{
         				document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn2{background-color:rgb(19,134,7)}</style>')
     					document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn3{background-color:rgb(216,14,21)}</style>')
     				}
   					//3rd button is correct answer
     				else
     				{
     					document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn2{background-color:rgb(216,14,21)}</style>')
     					document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn3{background-color:rgb(19,134,7)}</style>')
     				}
     				nextBtn.hidden = false;
         			answeredCorrectly = false;
     			}
     			//matches the button ID to correctly change the colour
     			else if(button.id === "mCBtn2")
     			{
     				//change button colours, and enable button for the next question, we know this is already incorrect
         			document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn2{background-color:rgb(216,14,21)}</style>')
         			//if statements used to determine which answer was correct and ultimately
     				//which button to change the colour of
     				if(mCBtn1.value === 'true')
     				{
         				document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn1{background-color:rgb(19,134,7)}</style>')
     					document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn3{background-color:rgb(216,14,21)}</style>')
     				}
     				else
     				{
     					document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn1{background-color:rgb(216,14,21)}</style>')
     					document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn3{background-color:rgb(19,134,7)}</style>')
     				}
     				nextBtn.hidden = false;
         			answeredCorrectly = false;
     			}
     			else if(button.id === "mCBtn3")
     			{
     				//change button colours, and enable button for the next question, we know this is already incorrect
         			document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn3{background-color:rgb(216,14,21)}</style>')
         			//if statements used to determine which answer was correct and ultimately
     				//which button to change the colour of
     				if(mCBtn2.value === 'true')
     				{
         				document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn2{background-color:rgb(19,134,7)}</style>')
     					document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn1{background-color:rgb(216,14,21)}</style>')
     				}
     				else
     				{
     					document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn2{background-color:rgb(216,14,21)}</style>')
     					document.head.insertAdjacentHTML("beforeend", '<style>.mCBtn1{background-color:rgb(19,134,7)}</style>')
     				}
     				nextBtn.hidden = false;
         			answeredCorrectly = false;
     			}
     		}
     		//disable buttons
     		mCBtn1.disabled = true;
     		mCBtn2.disabled = true;
     		mCBtn3.disabled = true;
     	}
     	
     	//function updates the hidden values of the form to be posted to the servlet
     	function sButtonSelect(anonymous)
     	{
     		//load document elements
     		var sAnon = document.getElementById("sAnon");
     		var	sNormal = document.getElementById("sNormal");
     		var sScoreH = document.getElementById("sScoreH");
     		var sAnonH = document.getElementById("sAnonH");
     		var sSubmit = document.getElementById("sSubmit");
     		//if Score submitted anonymously
     		if(anonymous === 'anon')
     		{
     			//change colour of button to green
     			document.head.insertAdjacentHTML("beforeend", '<style>.sAnon{background-color:rgb(19,134,7)}</style>')
     			//change colour of other to white
     			document.head.insertAdjacentHTML("beforeend", '<style>.sNormal{background-color:black}</style>')
     			//Add values to the hidden inputs
     			sScoreH.value = score;
     			sAnonH.value = "anon";
     			//form can now be submitted
     			sSubmit.disabled = false;
     		}  		
     		//else its a normal submit
     		else 
     		{
     			//change colour of button to green
     			document.head.insertAdjacentHTML("beforeend", '<style>.sNormal{background-color:rgb(19,134,7)}</style>')
     			//change colour of other to white
     			document.head.insertAdjacentHTML("beforeend", '<style>.sAnon{background-color:black}</style>')
     			//Add values to the hidden inputs
     			sScoreH.value = score;
     			sAnonH.value = "normal";
     			//form can now be submitted
     			sSubmit.disabled = false;
     		}
     	}
     		
    	</script>
    
	<jsp:element name="html">
        <jsp:attribute name="lang">EN</jsp:attribute>
        <jsp:body>
        <body>
        	<jsp:include page="navigation.jspx" />
        	<h1><c:out value="${quiz.quizName}"/></h1>
        	<div class="topContainer">
        		<div id="trueFalseC" class="trueFalseC" hidden="hidden">
        			<h3 class="questionCount" id="questionCountTf"></h3>
        			<h2 class="questionName" id="questionNameTf"></h2>
        			<div class="trueFalseBtns">
        				<button id="trueBtn" class="trueBtn tFBtns" onclick="trueFalseSelect('true', this)">True</button>
        				<button id="falseBtn" class="falseBtn tFBtns" onclick="trueFalseSelect('false', this)">False</button>
        			</div>
        			<button id="nextBtnTf" class="nextBtn tFBtns" onclick="submitAnswer('TF')" hidden="hidden">Next</button>
        		</div>
        		<div id="multiChoiceC" class="multiChoiceC" hidden="hidden">
        			<h3 class="questionCount" id="questionCountMc"></h3>
        			<h2 class="questionName" id="questionNameMc"></h2>
        			<div class="MultipleChoiceBtns">
        				<button id="mCBtn1" class="mCBtn1 mCBtns" onclick="multiChoiceSelect(this)"></button>
        				<button id="mCBtn2" class="mCBtn2 mCBtns" onclick="multiChoiceSelect(this)"></button>
        				<button id="mCBtn3" class="mCBtn3 mCBtns" onclick="multiChoiceSelect(this)"></button>
        			</div>
        			<button id="nextBtnMc" class="nextBtn mcBtns" onclick="submitAnswer('MC')" hidden="hidden">Next</button>
        		</div>
        		<div class="controls">
        			<button id="startBtn" class="start-btn btn" onclick="createQuestions()">Start Quiz</button>
        		</div>
        		<div id="submitFDiv" class="submitFDiv" hidden="hidden">
        			<h2 class="score" id="submitScore"></h2>
        			<div class="submitBtns">
        				<button id="sNormal" class="sNormal sBtns" onclick="sButtonSelect('normal')">Submit with Username</button>
        				<button id="sAnon" class="sAnon sBtns" onclick="sButtonSelect('anon')">Submit Anonymously</button>
        			</div>
        			<form id="submitForm" method="POST" action="submitAnswers">
        				<input id="sScoreH" name="sScore" type="hidden" value=""/>
        				<input id="sAnonH" name="sAnon" type="hidden" value=""/>
        				<input type="submit" id="sSubmit" class="sSubmit" value="Submit Score" disabled="disabled"/>
        			</form>
        		</div>
        	</div>
        </body>
		</jsp:body>
	</jsp:element>