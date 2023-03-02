<%@ page contentType="text/html;charset=utf8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
	
	<link href="../quizStyles.css" rel="stylesheet" type="text/css"/>
    <!-- any content can be specified here, e.g.: -->
    
    <jsp:element name="javascript">
	<script>
		function questionTypeSelectTF()
		{
			//If true/false is selected
			if(document.getElementById('trueFalse').checked){
				document.getElementById('multiChoice').checked = false;
				document.getElementById('trueFalseDiv').hidden = false;
				document.getElementById('multiChoiceDiv').hidden = true;
			}
			
		}
		
		function questionTypeSelectMC()
		{	
			// If Multi Choice is selected
			if(document.getElementById('multiChoice').checked){
				document.getElementById('trueFalse').checked = false;
				document.getElementById('trueFalseDiv').hidden = true;
				document.getElementById('multiChoiceDiv').hidden = false;
			}
		}

		function tFQuestionValidify(trueFalse){
			//If true/false is selected
			trueCheck = document.getElementById('true')
			falseCheck = document.getElementById('false')
			tFQuestionText = document.getElementById('tFQuestionText')
			//Set hidden value to true
			if(trueCheck.checked){
				if(trueFalse == true)
				{
					falseCheck.checked = false;
					document.getElementById('TFValue').value = "True"
				}
				else
				{
					trueCheck.checked = false;
					return tFQuestionValidify(trueFalse);
				}
			}
			//Set hidden value to false
			else if(falseCheck.checked)
			{
				if(trueFalse == false)
				{
					trueCheck.checked = false;
					document.getElementById('TFValue').value = "False"
				}
				else 
				{
					falseCheck = false;
					return tFQuestionValidify(trueFalse);
				}				
			}
			//If the value is more than 5 characters long
			if((tFQuestionText.value.length > 2) && (document.getElementById('TFValue').value.length > 0)){
				//enable submit button
				document.getElementById("tFQuestionSubmit").disabled = false;
			}
			else{
				document.getElementById("tFQuestionSubmit").disabled = true;
			}	
		}
		
		function mCQuestionValidify(option){
			//If true/false is selected
			o1 = document.getElementById('o1')
			o2 = document.getElementById('o2')
			o3 = document.getElementById('o3')
			mCQuestionText = document.getElementById('mCQuestionText')
			mCOption1 = document.getElementById('mCOption1')
			mCOption2 = document.getElementById('mCOption2')
			mCOption3 = document.getElementById('mCOption3')
			//Set hidden value to o1
			if(o1.checked)
			{	
				if(option === "o1")
				{
					o2.checked = false;
					o3.checked = false;
					document.getElementById('mCAnswer').value = "1"
				}
				else
				{
					o1.checked = false;
					return mCQuestionValidify(option);
				}
			}
			//Set hidden value to o2
			else if(o2.checked)
			{
				if(option === "o2")
				{
					o1.checked = false;
					o3.checked = false;
					document.getElementById('mCAnswer').value = "2"
				}
				else
				{
					o2.checked = false;
					return mCQuestionValidify(option);
				}
			}
			//Set hidden value to o3
			else if(o3.checked)
			{
				if(option === "o3")
				{
					o1.checked = false;
					o2.checked = false;
					document.getElementById('mCAnswer').value = "3"
				}
				else
				{
					o3.checked = false;
					return mCQuestionValidify(option);
				}
			}
			//If the value is more than 5 characters long
			if(((mCQuestionText.value.length > 0) && (mCOption1.value.length > 0) 
				&& (mCOption2.value.length > 0) && (mCOption3.value.length > 0)) 
				&& (document.getElementById('mCAnswer').value.length > 0)){
				//enable submit button
				document.getElementById("mCQuestionSubmit").disabled = false;
			}
			else{
				document.getElementById("mCQuestionSubmit").disabled = true;
			}	
		}
		
	</script>
	</jsp:element>
	<jsp:element name="html">
        <jsp:attribute name="lang">EN</jsp:attribute>
        <jsp:body>
        	<jsp:include page="navigation.jspx" />
            <h1><c:out value="${quizCreate.name}"/> !</h1>
            <div class="topContainer">
            	<div class="questionInput">  
					<h2>Question <c:out value="${questionCounter}"/></h2>
					<form id="createQuizQuestion">
			               <p>Which question type would you like to create</p>
			               <div class="qQuestionChooser">
				                <div class="tFC">
					                <input type="radio" id="trueFalse" name="trueFalse" onchange="questionTypeSelectTF()" /> 
					                <label for="trueFalse">True/False</label>
					            </div>
					            <div class="tFC">
									<input type="radio" id="multiChoice" name="multiChoice" onchange="questionTypeSelectMC()" /> 
									<label for="multiChoice">Multiple Choice</label>
								</div>
							</div> 
					</form>			
					<div id="trueFalseDiv" hidden="hidden">
						<form id="trueFalseForm" method="POST" action="/QuizService/do/createTrueFalse">
						<div class="qTitleC">
							<p>Enter your question</p> 
							<input type="text" id="tFQuestionText" class="qTitleE fText" name="tFQuestionText" onchange="tFQuestionValidify()"><br>
						</div>
						<div class="qTitleC">
							<div class="qQuestionChooser1">
								<div class="tFC">
									<input type="radio" id="true" name="true" onchange="tFQuestionValidify(true)"/> <label for="true">True</label>
								</div>
								<div class="tFC">	
									<input type="radio" id="false" name="false" onchange="tFQuestionValidify(false)"/> <label for="false">False</label>
								</div>
							</div>
						</div>
						<input type="hidden" name="TFValue" id="TFValue" value=""/>
						<input type="submit" id="tFQuestionSubmit" class="createQBtn1" disabled="disabled" name="tFQuestionSubmit" value="Create question" />
						</form>
					</div>
					
					<div id="multiChoiceDiv" hidden="hidden">
						<form id="multiChoiceForm" method="POST" action="/QuizService/do/createMultiChoice">
							<div class="qTitleC">
								<p>Enter your question</p> 
								<input type="text" id="mCQuestionText" class="qTitleE fText" name="mCQuestionText" onchange="mCQuestionValidify()"><br>
							</div>
							<div class="qOptionC">
								<div class="qOptionE">
									<p>Option 1</p> 
									<input type="text" id="mCOption1" class="qTitleE fText" name="mCOption1" onchange="mCQuestionValidify()"><br>
								</div>
								<div class="qOptionE">
									<p>Option 2</p> 
									<input type="text" id="mCOption2" class="qTitleE fText" name="mCOption2" onchange="mCQuestionValidify()"><br>
								</div>
								<div class="qOptionE">	
									<p>Option 3</p>
									<input type="text" id="mCOption3" class="qTitleE fText" name="mCOption3" onchange="mCQuestionValidify()"><br>
								</div>
							</div>
							<div class="qTitleC">
								<p>Which is the correct answer?</p>
								<div class="qQuestionChooser">
									<div class="tFC">
										<input type="radio" id="o1" name="o1" onchange="mCQuestionValidify('o1')"/> <label for="o1">Option 1</label>
									</div>
									<div class="tFC">
										<input type="radio" id="o2" name="o2" onchange="mCQuestionValidify('o2')"/> <label for="o2">Option 2</label>
									</div>
									<div class="tFC">
										<input type="radio" id="o3" name="o3" onchange="mCQuestionValidify('o3')"/> <label for="o3">Option 3</label><br>							
									</div>
								</div>
							</div>
							<input type="hidden" name="mCAnswer" id="mCAnswer" value=""/>
							<input type="submit" id="mCQuestionSubmit" class="createQBtn1" disabled="disabled" name="mCQustionSubmit" value="Create question" />
						</form>	
					</div>
						<div id="error" style="color:red;"></div>   
				</div>
			</div>  	   
        </jsp:body>
    </jsp:element>

