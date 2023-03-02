<%@ page contentType="text/html;charset=utf8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
	
    <!-- any content can be specified here, e.g.: -->
    
     <script type="text/javascript">
			function validateSearch(){
				//Creates a username for the user	
				var searchString = document.getElementById("quizSearch");
				//If the value is more than 7 characters long
				if(searchString.value.length > 0){
					//enable login button
					document.getElementById("searchSubmit").disabled = false;
				}
				else{
					document.getElementById("searchSubmit").disabled = true;
				}
			}		
    
    	var idCounter = 1;
    	</script>
    	
    <link href="../quizStyles.css" rel="stylesheet" type="text/css"/>
    
	<jsp:element name="html">
        <jsp:attribute name="lang">EN</jsp:attribute>
        <jsp:body>
        	<jsp:include page="navigation.jspx" />
        	<h1>Search for a Quiz!</h1>
        	<div class="searchForm">
	        	<form id="form" method="POST" action="searchQuizzes">
	        		<input type="text" id="quizSearch" class="searchQName" name="quizSearch" onchange="validateSearch()" value="" placeholder="Quiz String"/>   
			        <input type="submit" id="searchSubmit" class="searchBtn" disabled="disabled" name="submit" value="Click to search" />
	        	</form>
	        </div>	
        	<div id="searchValue" hidden="hidden">     		
        		<c:forEach var="entry" items="${quizList.quizSearch}">
        			<div class="sContainer">
	        			<table>
					        <div id="quiz" class="sQuiz">
								<h1 class="searchT"><c:out value="${entry.value.quizName}"/>!</h1>
								<div id="image">
									<script>
										var image = document.getElementById("image");
										var formdiv = document.getElementById("quiz");
										image.insertAdjacentHTML("afterbegin", "<img id=\"quizImage" + idCounter + "\"  class=\"image\" width=\"400\" height=\"400\"/>");
										image.id = "image"+idCounter;
										formdiv.id= "quiz"+idCounter;
									</script>
								</div>
								<script>
									document.getElementById("quizImage" + idCounter).src = "data:image/png;base64," + '<c:out value="${entry.value.image}"/>';
								</script>
								<p class="qDetails">
									<div class="quizDescC">
										<p class="sFormT">Description:</p>
										<p class="sFormE"><c:out value="${entry.value.quizDesc}"/></p>
									</div>
									<div class="quizDescC">
										<p class="sFormT">Difficulty:</p> 
										<p class="sFormE"><c:out value="${entry.value.quizDiff}"/></p>
									</div>
									<div class="quizDescC">
										<p class="sFormT">Question Count:</p> 
										<p class="sFormE"><c:out value="${entry.value.qAmount}"/></p>
									</div>
									<div class="quizDescC">
										<p class="sFormT">Created by:</p>
										<p class="sFormE"><c:out value="${entry.value.owner}"/></p>
									</div>
									<div class="quizDescC">
										<p class="sFormT">Date Created:</p>
										<p class="sFormE"><c:out value="${entry.value.creationDate}"/></p>		
									</div>
								</p>
								<form method="POST" action="selectQuiz">
									<input type="hidden" id="quizID" name="quizID" value="${entry.value.quizID}"/>
									<input type="submit" class="takeQBtn" value="View Quiz"/>
								</form>
							</div>
							<script>
								idCounter++;
							</script>
						</table>
					</div>
				</c:forEach>
			</div> 
		</jsp:body>
	</jsp:element>