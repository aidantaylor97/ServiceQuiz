<%@ page contentType="text/html;charset=utf8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
	
	<link href="../quizStyles.css" rel="stylesheet" type="text/css"/>
    <!-- any content can be specified here, e.g.: -->
    
	<jsp:element name="html">
        <jsp:attribute name="lang">EN</jsp:attribute>
        <jsp:body> 	
        	<jsp:include page="navigation.jspx" />
	        <h1><c:out value="${quiz.quizName}"/>!</h1>
	        	<div class="topContainer">      	
		        	<div id="take quiz" class="takeQ">	        		
							<img id="quizImage" src=""  class="image" width="300vw" height="300vh"/>		
							<script>
								document.getElementById("quizImage").src = "data:image/png;base64," + '<c:out value="${quiz.image}"/>';
							</script>
							<p class="qDetails1">
									<div class="quizDescC">
										<p class="seFormT">Description:</p>
										<p class="seFormE"><c:out value="${quiz.quizDesc}"/></p>
									</div>
									<div class="quizDescC">
										<p class="seFormT">Difficulty:</p> 
										<p class="seFormE"><c:out value="${quiz.quizDiff}"/></p>
									</div>
									<div class="quizDescC">
										<p class="seFormT">Question Count:</p> 
										<p class="seFormE"><c:out value="${quiz.qAmount}"/></p>
									</div>
									<div class="quizDescC">
										<p class="seFormT">Created by:</p>
										<p class="seFormE"><c:out value="${quiz.owner}"/></p>
									</div>
									<div class="quizDescC">
										<p class="seFormT">Date Created:</p>
										<p class="seFormE"><c:out value="${quiz.creationDate}"/></p>		
									</div>
								</p>
						<button class="takeQBtn" onclick="location.href='takeQuiz'">Play Quiz!</button>		
					</div>
					<div id="leaderboard" class="takeQ">
						<h2 class="leaderboardT">Leaderboard</h2>
						<div id="tableD" class="tableD">
							<table class="leaderT">
								<tr class="heading">
									<th class="cUser">Username</th>
									<th class="cScore">Score</th>
									<th class="cDate">Date</th>
								</tr>       		
								<c:forEach var="entry" items="${quiz.scores}">
									<tr class="row">
										<th class="cUser"><c:out value="${entry.username}"/></th>
										<th class="cScore"><c:out value="${entry.score}"/></th>
										<th class="cDate"><c:out value="${entry.date}"/></th>
									</tr>		
								</c:forEach>
							</table>
						</div>	 
					</div>
				</div>
		</jsp:body>
	</jsp:element>