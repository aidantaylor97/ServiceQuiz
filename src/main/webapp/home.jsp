<%@ page contentType="text/html;charset=utf8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

	<link href="../quizStyles.css" rel="stylesheet" type="text/css"/> 
    <!-- any content can be specified here, e.g.: -->
    <script type="text/javascript">
    		var idCounter = 1;
	</script>
        
    <jsp:element name="html">
        <jsp:attribute name="lang">EN</jsp:attribute>
        <jsp:body>
        	<jsp:include page="navigation.jspx" />
            <h1>Service Quiz Home!</h1>
            <h2 class="popE">Most Popular</h2>
            <div id="searchValue">  
            <div class="sContainer">
            	<c:forEach var="entry" items="${quizList.quizSearch}">
	        			<table>
	            			<div id="quiz" class="sQuiz1">	
	            				<h1 class="searchT1"><c:out value="${entry.value.quizName}"/>!</h1>		
								<div id="image">
									<script>
										var image = document.getElementById("image");
										var formdiv = document.getElementById("quiz");
										image.insertAdjacentHTML("afterbegin", "<img id=\"quizImage" + idCounter + "\"  class=\"image1\" width=\"400\" height=\"400\"/>");
										image.id = "image"+idCounter;
										formdiv.id= "quiz"+idCounter;
									</script>
								</div>
								<script>
									document.getElementById("quizImage" + idCounter).src = "data:image/png;base64," + '<c:out value="${entry.value.image}"/>';
								</script>
								<p class="qDetails">
									<div class="quizDescC">
										<p class="sFormT1">Description:</p>
										<p class="sFormE1"><c:out value="${entry.value.quizDesc}"/></p>
									</div>
									<div class="quizDescC">
										<p class="sFormT1">Difficulty:</p> 
										<p class="sFormE1"><c:out value="${entry.value.quizDiff}"/></p>
									</div>
									<div class="quizDescC">
										<p class="sFormT1">Question Count:</p> 
										<p class="sFormE1"><c:out value="${entry.value.qAmount}"/></p>
									</div>
									<div class="quizDescC">
										<p class="sFormT1">Created by:</p>
										<p class="sFormE1"><c:out value="${entry.value.owner}"/></p>
									</div>
									<div class="quizDescC">
										<p class="sFormT1">Date Created:</p>
										<p class="sFormE1"><c:out value="${entry.value.creationDate}"/></p>		
									</div>
								</p>
								<form method="POST" action="selectQuiz">
									<input type="hidden" id="quizID" name="quizID" value="${entry.value.quizID}"/>
									<input type="submit" class="takeQBtn" value="View Quiz"/>
								</form>
								<script>
										idCounter++;
								</script>
							</div>
						</table>
					</c:forEach>
				</div>
			</div>
        </jsp:body>
    </jsp:element>

