package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import model.Quiz;
import model.QuizCreation;
import model.QuizSearcher;
import model.Users;

/**
 * Servlet implementation class Controller
 */
@MultipartConfig
@WebServlet(value = "/do/*") 
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Users users;
	private QuizCreation qCreate;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public void init() {
        users = new Users(); // Initialise users object
        // TODO Auto-generated constructor stub
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	//forward request - Action will determine where the dispatcher forwards the traffic
        String action = request.getPathInfo();
        RequestDispatcher dispatcher = null;
        //Prepares a string for an XML response
        String xmlResponse = "";
        //First login to the system
        if (action.equals("/login")){
        	//Creates local variables for parameters
        	String username = request.getParameter("username");
        	String password = request.getParameter("password");
        	//method ran once to determine whether success or fail
        	int u = users.isValid(username, password);
        	if (u != -1) 
        	{
        		//Create session
        		HttpSession session=request.getSession();
        		//add UserName to session
                session.setAttribute("user", username);   
                //add UserId to session
                session.setAttribute("userID", u);   
                //Create a quiz list based on most popular quizzed
    			QuizSearcher quizSearch = new QuizSearcher();
    			//store the quizID in the session
            	session.setAttribute("quizList", quizSearch);
            	//dispatcher will forward to the next page
        		dispatcher = this.getServletContext().getRequestDispatcher("/home.jsp");
        		dispatcher.forward(request, response);
        	}
        	else {
        		dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
        		dispatcher.include(request, response);
        		response.setContentType("text/html");
        		//Sends a message beck to the Login page to update a div with error message details of a failed login
        		PrintWriter out = response.getWriter();
        		out.println("<script>document.getElementById('error').innerHTML='Username or Password do not match'; </script>");
        	}
        }
        //Registering a user
        else if (action.equals("/addUser"))
        	{
        	//Creates local variables for parameters
        	String username = request.getParameter("username");
        	String password = request.getParameter("password");
        	users.addUser(username, password);
        	dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
        	dispatcher.forward(request, response);
        	}
        else if (action.equals("/validateRegistration"))
        {
        	//return a response as xml
        	response.setContentType("application/xml;charset=UTF-8");
        	PrintWriter out = response.getWriter();
        	String username = request.getParameter("username");
        	//validates whether username exists or not
        	if(users.validateUsername(username))
        	{
        		//Set XML
        		xmlResponse = "<?xml version=\"1.0\"?><data><username>true</username></data>";
        		//print XMLResponse
        		out.println(xmlResponse);
        		//do not forward
        		return;
        	}
        	else {
        		//Set XML
        		xmlResponse = "<?xml version=\"1.0\"?><data><username>false</username></data>";
        		//print XMLResponse
        		out.println(xmlResponse);
        		//do not forward
        		return;
        	}        	
        }
        else {
        	// Checks if user has a current session, creates one if there is not one already
        	HttpSession session = request.getSession(false);
        	//if session is null, or lesson attribute is not current in the session
        	if(session == null) 
        	{
        		//not logged in, valid session requires a lesson selection, redirects to login view
        		dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
        		dispatcher.forward(request, response);
        	}
        	else 
        	{
        		//if user searches for a quiz
        		if(action.equals("/searchQuiz"))
        		{
        			dispatcher = this.getServletContext().getRequestDispatcher("/QuizSearch.jsp");	  
        			dispatcher.forward(request, response);
        		}  		
        		else if(action.equals("/searchQuizzes"))
        		{
        			String searchQuery = request.getParameter("quizSearch");
        			QuizSearcher quizSearch = new QuizSearcher(searchQuery);
        			
        			//store the quizID in the session
	            	session.setAttribute("quizList", quizSearch);  
        			
        			dispatcher = this.getServletContext().getRequestDispatcher("/QuizSearch.jsp");
            		dispatcher.include(request, response);
            		response.setContentType("text/html");
            		//Sends a message beck to the Login page to update a div with error message details of a failed login
            		PrintWriter out = response.getWriter();
            		out.println("<script>document.getElementById('searchValue').hidden=false; </script>");
        		}
        		else if(action.equals("/selectQuiz"))
        		{
        			int quizID = Integer.valueOf(request.getParameter("quizID"));
        			//Create Quiz Bean from the quiz created
    				Quiz createdQuiz = new Quiz(quizID);
    				session.setAttribute("quiz", createdQuiz);
    				dispatcher = this.getServletContext().getRequestDispatcher("/quizSelected.jsp");	  
        			dispatcher.forward(request, response);	
        		}
        		//User has selected a lesson
        		else if(action.equals("/createQuiz"))
        		{
        			dispatcher = this.getServletContext().getRequestDispatcher("/createQuiz.jsp");	  
        			dispatcher.forward(request, response);
        			
        		}	
	        	else if(action.equals("/quizTitle"))
	        	{	
	        		if(null == session.getAttribute("quizID"))
	        		{
		        		//Creates a QuizCreation Bean
		        		this.qCreate = new QuizCreation(request.getParameter("quizTitle"));
		        		//If a file has been submitted as a quiz Image
		        		if(!request.getPart("quizImage").getSubmittedFileName().equals(""))
		        		{
			        		//Sets the image inserted to html as a part        		  		
			        		Part filePart = request.getPart("quizImage");
			        		//validate whether the file uploaded is an accepted image type    		
			        		String fileExtension = filePart.getSubmittedFileName().substring(filePart.getSubmittedFileName().length() -3);
			        		if(!fileExtension.toUpperCase().equals("PNG") && !fileExtension.toUpperCase().equals("JPG") && !fileExtension.toUpperCase().equals("GIF"))
			        		{
			        			dispatcher = this.getServletContext().getRequestDispatcher("/createQuiz.jsp");
			            		dispatcher.include(request, response);
			            		response.setContentType("text/html");
			            		//Sends a message beck to the Quiz Creation page to say file type must be an acceptable type
			            		PrintWriter out = response.getWriter();
			            		out.println("<script>document.getElementById('error').innerHTML='Please insert a file of type png, jpg, or gif only<br>'; </script>");
			            		return;
			        		}
			        		//obtain input stream for db insertion
			        		InputStream inputStream = filePart.getInputStream();
			        		//adds bean to the session
			        		this.getServletContext().setAttribute("quizCreate", qCreate);
			        	 	//Creates local variables for parameters
			            	int quizID = qCreate.createQuiz((int)session.getAttribute("userID"), request.getParameter("quizDesc"), 
			            			request.getParameter("quizDiff"),
			            			Integer.parseInt(request.getParameter("quizAmount")),
			            			inputStream);
			            	//store the quizID in the session
			            	session.setAttribute("quizID", quizID);  
			            	//Specify the amount of questions and a counter
			            	session.setAttribute("questionAmount", Integer.parseInt(request.getParameter("quizAmount")));  
			            	session.setAttribute("questionCounter", 1);
			            	dispatcher = this.getServletContext().getRequestDispatcher("/createQuestion.jsp");	  
		        			dispatcher.forward(request, response);
		        		}
		        		else
		        		{
		        			//adds bean to the session
			        		this.getServletContext().setAttribute("quizCreate", qCreate);
			        	 	//Creates local variables for parameters
			        		int quizID = qCreate.createQuiz((int)session.getAttribute("userID"), request.getParameter("quizDesc"), 
			            			request.getParameter("quizDiff"),
			            			Integer.parseInt(request.getParameter("quizAmount")));
			            	//store the quizID in the session
			            	session.setAttribute("quizID", quizID);  
			            	//Specify the amount of questions and a counter
			            	session.setAttribute("questionAmount", Integer.parseInt(request.getParameter("quizAmount")));  
			            	session.setAttribute("questionCounter", 1);
			            	dispatcher = this.getServletContext().getRequestDispatcher("/createQuestion.jsp");	  
		        			dispatcher.forward(request, response);
		        		}
	        		}
	        		else
	        		{
	        			dispatcher = this.getServletContext().getRequestDispatcher("/createQuestion.jsp");	  
	        			dispatcher.forward(request, response);
	        		}
	        	}
	        	else if(action.equals("/createTrueFalse"))
	        	{
	        		if(!(null == session.getAttribute("questionAmount")))
	        		{
		        		//Will keep redirecting user to the create question page until the amount of questions specified
		        		//prior is met
		        		int qAmount = (int)session.getAttribute("questionAmount");
		        		int qCount = (int)session.getAttribute("questionCounter");
		        		//if the Question count is less than question Amount
		        		if(qCount < qAmount)
		        		{
		        			//establish QuizCreation object from the one stored in the prior page 
		        			//QuizCreation qCreate = (QuizCreation) session.getAttribute("quizCreate");
		        			String tfQuestiontext = request.getParameter("tFQuestionText");
		        			//Insert the question to the database
		        			int quizID = (int) session.getAttribute("quizID");
		        			int questionID = qCreate.QuestionCreate(quizID,tfQuestiontext, "true/false");
		        			if (questionID != -1)
		        			{
		        				//insert the Answer if the question was succesfully inserted
		        				String tfValue = request.getParameter("TFValue");
		        				qCreate.AnswerCreate(questionID, tfValue);
		        				//increase the counter in the session
		        				qCount++;
		        				session.setAttribute("questionCounter", qCount);
		        				dispatcher = this.getServletContext().getRequestDispatcher("/createQuestion.jsp");	  
		            			dispatcher.forward(request, response);			
		        			}
		        			else 
		        			{
		                		dispatcher = this.getServletContext().getRequestDispatcher("/createQuestion.jsp");
		                		dispatcher.include(request, response);
		                		response.setContentType("text/html");
		                		//Sends a message beck to the Login page to update a div with error message details of a failed login
		                		PrintWriter out = response.getWriter();
		                		out.println("<script>document.getElementById('error').innerHTML='Failed to create Question'; </script>");
		                		return;
		                	}
		        			
		        		}
		        		else 
		        		{
		        			//Same Code, but with a different redirect
		        			//establish QuizCreation object from the one stored in the prior page 
		        			//QuizCreation qCreate = (QuizCreation) session.getAttribute("quizCreate");
		        			String tfQuestiontext = request.getParameter("tFQuestionText");
		        			//Insert the question to the database
		        			int quizID = (int) session.getAttribute("quizID");
		        			int questionID = qCreate.QuestionCreate(quizID,tfQuestiontext, "true/false");
		        			if (questionID != -1)
		        			{
		        				//insert the Answer if the question was succesfully inserted
		        				String tfValue = request.getParameter("TFValue");
		        				qCreate.AnswerCreate(questionID, tfValue);
		        				//remove the question Count and Amount in the session
		        				qCount=0;
		        				session.removeAttribute("questionCounter");
		        				session.removeAttribute("questionAmount");
		        				//Create Quiz Bean from the quiz created
		        				Quiz createdQuiz = new Quiz(quizID);
		        				//add object to session
		        				session.setAttribute("quiz", createdQuiz);
		        				//this.getServletContext().setAttribute("quiz", createdQuiz);
		        				dispatcher = this.getServletContext().getRequestDispatcher("/quizSelected.jsp");	  
		            			dispatcher.forward(request, response);			
		        			}
		        			else 
		        			{
		                		dispatcher = this.getServletContext().getRequestDispatcher("/createQuestion.jsp");
		                		dispatcher.include(request, response);
		                		response.setContentType("text/html");
		                		//Sends a message beck to the Login page to update a div with error message details of a failed login
		                		PrintWriter out = response.getWriter();
		                		out.println("<script>document.getElementById('error').innerHTML='Failed to create Question'; </script>");
		                		return;
		                	}
		        			
		        		}
	        		}
		        	else
		        	{
		        		dispatcher = this.getServletContext().getRequestDispatcher("/quizSelected.jsp");	  
	            		dispatcher.forward(request, response);
		        	}
	        	}
	        	else if(action.equals("/createMultiChoice"))
	        	{
	        		if(!(null == session.getAttribute("questionAmount")))
	        		{
		        		//Will keep redirecting user to the create question page until the amount of questions specified
		        		//prior is met
		        		int qAmount = (int)session.getAttribute("questionAmount");
		        		int qCount = (int)session.getAttribute("questionCounter");
		        		//if the Question count is less than question Amount
		        		if(qCount < qAmount)
		        		{
		        			//establish QuizCreation object from the one stored in the prior page 
		        			//QuizCreation qCreate = (QuizCreation) session.getAttribute("quizCreate");
		        			String tfQuestiontext = request.getParameter("mCQuestionText");
		        			//Insert the question to the database
		        			int quizID = (int) session.getAttribute("quizID");
		        			int questionID = qCreate.QuestionCreate(quizID,tfQuestiontext, "multiple choice");
		        			if (questionID != -1)
		        			{
		        				//Create an array holding the answers for the question
		        				String[] answers = new String[3];
		        				answers[0] = request.getParameter("mCOption1");
		        				answers[1] = request.getParameter("mCOption2");
		        				answers[2] = request.getParameter("mCOption3");
		        				//Establish which answer is the correct one
		        				int correct = Integer.parseInt(request.getParameter("mCAnswer"));
		        				//This loop will add in the answers one by one
		        				int i = 1;
		        				while(i <= 3)
		        				{
		        					//if the option is not the correct option
		        					if (i != correct)
		        					{
		        						//Add the answer as wrong
		        						qCreate.AnswerCreate(questionID, answers[i-1], false);	        						
		        						i++;
	
		        					}
		        					//else it is
		        					else 
		        					{
		        						//Add the answer as correct
		        						qCreate.AnswerCreate(questionID, answers[i-1], true);
		        						i++;
		        					}
		        				}
		        				//increase the counter in the session
		        				qCount++;
		        				session.setAttribute("questionCounter", qCount);
		        				dispatcher = this.getServletContext().getRequestDispatcher("/createQuestion.jsp");	  
		            			dispatcher.forward(request, response);	
		        			}
		        			else 
		        			{
		                		dispatcher = this.getServletContext().getRequestDispatcher("/createQuestion.jsp");
		                		dispatcher.include(request, response);
		                		response.setContentType("text/html");
		                		//Sends a message beck to the Login page to update a div with error message details of a failed login
		                		PrintWriter out = response.getWriter();
		                		out.println("<script>document.getElementById('error').innerHTML='Failed to create Question'; </script>");
		                		return;
		                	}
		        			
		        		}
		        		else 
		        		{
		        			//Same Code, but with a different redirect
		        			//establish QuizCreation object from the one stored in the prior page 
		        			//QuizCreation qCreate = (QuizCreation) session.getAttribute("quizCreate");
		        			String tfQuestiontext = request.getParameter("mCQuestionText");
		        			//Insert the question to the database
		        			int quizID = (int) session.getAttribute("quizID");
		        			int questionID = qCreate.QuestionCreate(quizID,tfQuestiontext, "multiple choice");
		        			if (questionID != -1)
		        			{
		        				//Create an array holding the answers for the question
		        				String[] answers = new String[3];
		        				answers[0] = request.getParameter("mCOption1");
		        				answers[1] = request.getParameter("mCOption2");
		        				answers[2] = request.getParameter("mCOption3");
		        				//Establish which answer is the correct one
		        				int correct = Integer.parseInt(request.getParameter("mCAnswer"));
		        				//This loop will add in the answers one by one
		        				int i = 1;
		        				while(i <= 3)
		        				{
		        					//if the option is not the correct option
		        					if (i != correct)
		        					{
		        						//Add the answer as wrong
		        						qCreate.AnswerCreate(questionID, answers[i-1], false);	        						
		        						i++;
	
		        					}
		        					//else it is
		        					else 
		        					{
		        						//Add the answer as correct
		        						qCreate.AnswerCreate(questionID, answers[i-1], true);
		        						i++;
		        					}
		        				}
		        				//remove the question Count and Amount in the session
		        				qCount=0;
		        				session.removeAttribute("questionCounter");
		        				session.removeAttribute("questionAmount");
		        				//Create Quiz Bean from the quiz created
		        				Quiz createdQuiz = new Quiz(quizID);
		        				//add object to session
		        				session.setAttribute("quiz", createdQuiz);
		        				//this.getServletContext().setAttribute("quiz", createdQuiz);
		        				dispatcher = this.getServletContext().getRequestDispatcher("/quizSelected.jsp");	  
		            			dispatcher.forward(request, response);	
		        			}
		        			else 
		        			{
		                		dispatcher = this.getServletContext().getRequestDispatcher("/createQuestion.jsp");
		                		dispatcher.include(request, response);
		                		response.setContentType("text/html");
		                		//Sends a message beck to the Login page to update a div with error message details of a failed login
		                		PrintWriter out = response.getWriter();
		                		out.println("<script>document.getElementById('error').innerHTML='Failed to create Question'; </script>");
		                		return;
		                	}
		        		}
	        		}
	        		else
				    {
				        dispatcher = this.getServletContext().getRequestDispatcher("/quizSelected.jsp");	  
			            dispatcher.forward(request, response);
				    }
	        	}
	        	else if(action.equals("/takeQuiz"))
	        	{
		        	//If the session has no quiz present
		        	if(null == session.getAttribute("quiz"))
		        	{
		        		//dispatcher will forward to the home page
		            	dispatcher = this.getServletContext().getRequestDispatcher("/home.jsp");
		            	dispatcher.forward(request, response);
		        	}
		        	else
		        	{
		        		//adds questions to the quiz object and updates the session
		        		Quiz q = (Quiz) session.getAttribute("quiz");
		        		q.loadQuestions();
		        		session.setAttribute("quiz", q);
		        		//set up submit flag (stops user from submitting a score multiple times on page reset
			        	session.setAttribute("submitFlag", 1);
		        		//forwards to the quiz game page
		            	dispatcher = this.getServletContext().getRequestDispatcher("/playQuiz.jsp");
		            	dispatcher.forward(request, response);
		        	}
	        	}
        		//Quiz completed and answers being submitted
	        	else if(action.equals("/submitAnswers"))
	        	{	
	        		//Stops user submitting a score twice on refresh
	        		if(1 == (int) session.getAttribute("submitFlag"))
	        		{
			        	//update the submit flag
			        	session.setAttribute("submitFlag", 2);
			        	//gets quiz options
		        		Quiz q = (Quiz) session.getAttribute("quiz");
		        		//gets paramaters issues by page
		        		int score = Integer.valueOf(request.getParameter("sScore"));
		        		String anon = request.getParameter("sAnon");
		        		int uId = (int) session.getAttribute("userID"); 
		        		//uses quiz method submit scores to submit to the scores db table
		        		q.submitScores(score, anon, uId);
		        		session.setAttribute("quiz", q);       		
		        		dispatcher = this.getServletContext().getRequestDispatcher("/quizSelected.jsp");	  
				        dispatcher.forward(request, response);
	        		}
	        		//user will have to play quiz again to submit another score
	        		else
	        		{
	        			dispatcher = this.getServletContext().getRequestDispatcher("/quizSelected.jsp");	  
				        dispatcher.forward(request, response);
	        		}
	        	}
        		//Clear objects from the session and send back to login page
	        	else if(action.equals("/logout"))
	        	{
	        		session.invalidate();
	        		dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");	  
		            dispatcher.forward(request, response);
	        	}
        		//removes session items and returns user to the home page
	        	else if(action.equals("/home"))
	        	{
	        		//questionCounter stored in session in prior section
	        		if(!(null == session.getAttribute("questionAmount")))
	        		{
	        			session.removeAttribute("questionAmount");
	        		}
	        		//questionCounter stored in session in prior section
	        		if(!(null == session.getAttribute("questionCounter")))
	        		{
	        			session.removeAttribute("questionCounter");
	        		}
	        		//QuizId stored in session in prior section
	        		if(!(null == session.getAttribute("quizID")))
	        		{
	        			session.removeAttribute("quizID");
	        		}
	        		//Quiz stored in session in prior section
	        		if(!(null == session.getAttribute("quiz")))
	        		{
	        			session.removeAttribute("quiz");
	        		}
	        		//Create a quiz list based on most popular quizzed
	    			QuizSearcher quizSearch = new QuizSearcher();
	    			//store the quizID in the session
	            	session.setAttribute("quizList", quizSearch);
	            	//dispatcher will forward to the next page
	        		dispatcher = this.getServletContext().getRequestDispatcher("/home.jsp");	  
		            dispatcher.forward(request, response);
	        	}
        	}
        }
    }

    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		processRequest(request, response);
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		processRequest(request, response);
	}

}
