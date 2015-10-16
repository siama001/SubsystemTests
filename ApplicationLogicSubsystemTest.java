package SubsystemTests;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import Storage.DBManager;
import Storage.Repository.DoubleRegistrationException;
import Storage.Repository.PanelistProfile;
import Storage.Repository.UserProfile;
import Storage.Repository.UserType;
import static org.mockito.Mockito.*;
import ApplicationLogic.*;
import Interface.AdministratorForms;
import Interface.EmployeeForms;
import Interface.PanelistForms;


/**
 * @author CEN4072
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DBManager.class})
public class ApplicationLogicSubsystemTest {
	private Controller controllerSpy;
	private StubResponse mockResponse;
	private StubRequest mockRequest;
	private PrintWriter mockPrintWriter;
	private UserProfile mockUser;
	private StubSession mockSession;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		controllerSpy = spy(new Controller());
		mockRequest = mock(StubRequest.class);
		mockResponse = mock(StubResponse.class);
		mockPrintWriter = mock( PrintWriter.class);
		mockUser = mock(UserProfile.class);
		mockSession = new StubSession();
		
		PowerMockito.mockStatic(DBManager.class);
		when(mockResponse.getWriter()).thenReturn(mockPrintWriter);
		when(mockResponse.getSession()).thenReturn(mockSession);
		when(mockRequest.getSession(true)).thenReturn(mockSession);

		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	/**
	 * Sub Systems Test 1a(SST1a)
	 * Verify that a login request to the facade with valid credentials (Panelist User type) 
	 * will log the current user in.
	 * Initial State: HttpSession(StubSession): blank session
	 * Input: HttpRequest, HttpResponse, InitParameter("action-type")="doLogin",
	 *  valid user credentials
	 * Expected Output: HttpSession(StubSession): Attributes:"User Profile"==mockUser,
	 * 	 	"User Form"== instanceof PanelistForms, 
	 * 		and Session URL == ("messagePage?messageCode=You are now logged in. Welcome.")
	 */
	public void SST1a() throws Exception{
		mockUser.Type = UserType.PANELIST;
		mockUser.UserName = "mrrobot";
		String pass = "123abc";
		
		when(mockRequest.getParameter("username")).thenReturn(mockUser.UserName);
		when(mockRequest.getParameter("password")).thenReturn(pass);
		when(DBManager.validateUsername(mockUser.UserName,  pass)).thenReturn(mockUser);
		doReturn("doLogin").when(controllerSpy).getInitParameter("action-type");
		doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
            	StubResponse mock = (StubResponse)invocation.getMock();
		    	String URL = (String) invocation.getArguments()[0];
		    	((StubSession) mock.getSession()).setURL(URL);
                return invocation.getArguments();
            }
        }).when(mockResponse).sendRedirect(anyString());
	
		/* Act */
		controllerSpy.doPost(mockRequest, mockResponse);	
		/* Assert */
		assertSame("the session should contain a valid UserProfile",
				mockSession.getAttribute("User Profile"),mockUser);
		assertTrue( "the session should contain a PanelistForms",
                mockSession.getAttribute( "User Form" ) instanceof PanelistForms );
		assertSame("the session url should be 'messagePage?messageCode=You are now logged in. "
				+ "Welcome.'",
				((StubSession) mockSession).getURL(),
				"messagePage?messageCode=You are now logged in. Welcome.");
		
	}
	@Test
	/**
	 * Sub Systems Test 1b(SST1b)
	 * Verify that a login request to the facade with valid credentials 
	 * will log the current user in.(Employee Usertype)
	 * Initial State: HttpSession(StubSession): blank session
	 * Input: HttpRequest, HttpResponse, InitParameter("action-type")="doLogin", 
	 * valid user credentials
	 * Expected Output: HttpSession(StubSession): Attributes:"User Profile"==mockUser,
	 * 	 	"User Form"== instanceof EmployeeForms, 
	 * 		and Session URL == ("messagePage?messageCode=You are now logged in. Welcome.")
	 */
	public void SST1b() throws Exception{
		mockUser.Type = UserType.EMPLOYEE;
		mockUser.UserName = "mrrobot";
		String pass = "123abc";
		
		when(mockRequest.getParameter("username")).thenReturn(mockUser.UserName);
		when(mockRequest.getParameter("password")).thenReturn(pass);
		when(DBManager.validateUsername(mockUser.UserName,  pass)).thenReturn(mockUser);
		doReturn("doLogin").when(controllerSpy).getInitParameter("action-type");
		doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
            	StubResponse mock = (StubResponse)invocation.getMock();
		    	String URL = (String) invocation.getArguments()[0];
		    	((StubSession) mock.getSession()).setURL(URL);
                return invocation.getArguments();
            }
        }).when(mockResponse).sendRedirect(anyString());
	
		/* Act */
		controllerSpy.doPost(mockRequest, mockResponse);	
		/* Assert */
		assertSame("the session should contain a valid UserProfile",
				mockSession.getAttribute("User Profile"),mockUser);
		assertTrue( "the session should contain a Employee",
                mockSession.getAttribute( "User Form" ) instanceof EmployeeForms );
		assertSame("the session url should be 'messagePage?messageCode=You are now logged in. "
				+ "Welcome.'",
				((StubSession) mockSession).getURL(),
				"messagePage?messageCode=You are now logged in. Welcome.");
		
	}
	@Test
	/**
	 * Sub Systems Test 1c(SST1c)
	 * Verify that a login request to the facade with valid credentials  
	 * will log the current user in. (Administrator user type)
	 * Initial State: HttpSession(StubSession): blank session
	 * Input: HttpRequest, HttpResponse, InitParameter("action-type")="doLogin",
	 *  valid user credentials
	 * Expected Output: HttpSession(StubSession): Attributes:"User Profile"==mockUser,
	 * 	 	"User Form"== instanceof AdministratorForms, 
	 * 		and Session URL == ("messagePage?messageCode=You are now logged in. Welcome.")
	 */
	public void SST1c() throws Exception{
		mockUser.Type = UserType.ADMINISTRATOR;
		mockUser.UserName = "mrrobot";
		String pass = "123abc";
		
		when(mockRequest.getParameter("username")).thenReturn(mockUser.UserName);
		when(mockRequest.getParameter("password")).thenReturn(pass);
		when(DBManager.validateUsername(mockUser.UserName,  pass)).thenReturn(mockUser);
		doReturn("doLogin").when(controllerSpy).getInitParameter("action-type");
		doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
            	StubResponse mock = (StubResponse)invocation.getMock();
		    	String URL = (String) invocation.getArguments()[0];
		    	((StubSession) mock.getSession()).setURL(URL);
                return invocation.getArguments();
            }
        }).when(mockResponse).sendRedirect(anyString());
	
		/* Act */
		controllerSpy.doPost(mockRequest, mockResponse);	
		/* Assert */
		assertSame("the session should contain a valid UserProfile",
				mockSession.getAttribute("User Profile"),mockUser);
		assertTrue( "the session should contain a AdminstratorForm",
                mockSession.getAttribute( "User Form" ) instanceof AdministratorForms );
		assertSame("the session url should be 'messagePage?messageCode=You are now logged in."
				+ " Welcome.'",
				((StubSession) mockSession).getURL(),
				"messagePage?messageCode=You are now logged in. Welcome.");
		
	}
	@Test
	/**
	 * Sub Systems Test 2(SST2)
	 * Verify that a login request to the facade with invalid credentials  
	 * will not log the user in.
	 * Initial State: HttpSession(StubSession): blank session
	 * Input: HttpRequest, HttpResponse, InitParameter("action-type")="doLogin"
	 * Expected Output: HttpSession(StubSession): Attributes:"User Profile"==null, 
	 * and Session URL == "login"
	 */
	public void SST2() throws Exception{
		mockUser.Type = UserType.PANELIST;
		mockUser.UserName = "mrrobot";
		String pass = "123abc";
	
		when(mockRequest.getParameter("username")).thenReturn(mockUser.UserName);
		when(mockRequest.getParameter("password")).thenReturn(pass);
		when(DBManager.validateUsername(mockUser.UserName,  pass)).thenReturn(null);
		doReturn("doLogin").when(controllerSpy).getInitParameter("action-type");
		
		/* Act */
		doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
            	StubResponse mock = (StubResponse)invocation.getMock();
		    	String URL = (String) invocation.getArguments()[0];
		    	((StubSession) mock.getSession()).setURL(URL);
                return invocation.getArguments();
            }
        }).when(mockResponse).sendRedirect(anyString());
		
		controllerSpy.doPost(mockRequest, mockResponse);
		
		/* Assert */
		assertNull("the session should contain a null UserProfile",
				mockSession.getAttribute("User Profile"));
		assertSame("the session url should be 'login'",
				((StubSession) mockSession).getURL(),"login");		
	}
	@Test
	/**
	 * Sub Systems Test 3(SST3)
	 * Verify that a createPanelistAccount request to the facade will create 
	 * a user account.
	 * Initial State: HttpSession(StubSession): blank session
	 * Input: HttpRequest(mockRequest), HttpResponse(mockResponse), 
	 * InitParameter("action-type")="createPanelistAccount",
	 * 			 valid new panelist info
	 * Expected Output: StubSession url == "messagePage?messageCode=Registration Successful"
	 */
	public void SST3() throws Exception{
		when(mockRequest.getParameter("pFName")).thenReturn("mr");
		when(mockRequest.getParameter("pLName")).thenReturn("robot");
		when(mockRequest.getParameter("username")).thenReturn("mrrobot");
		when(mockRequest.getParameter("password")).thenReturn("123abc");
		when(mockRequest.getParameter("pInstitution")).thenReturn("FIU");
		when(mockRequest.getParameter("pAddress")).thenReturn("123 road lane");
		when(mockRequest.getParameter("pCity")).thenReturn("Miami");
		when(mockRequest.getParameter("pState")).thenReturn("Florida");
		when(mockRequest.getParameter("pZip")).thenReturn("123456");
		when(mockRequest.getParameter("pTelephone")).thenReturn("5555555555");
		when(mockRequest.getParameter("pEmail")).thenReturn("mrrobot@fiu.edu");
		when(mockRequest.getParameter("pGender")).thenReturn("male");
		when(mockRequest.getParameter("pEthnicity")).thenReturn("robot");
		when(mockRequest.getParameter("pExpertise")).thenReturn("master");
		when(mockRequest.getParameter("pISCID")).thenReturn("33351");
		
		HashMap<String, String> newPanelist = new HashMap<String, String>();
        newPanelist.put("pFName",mockRequest.getParameter("pFName"));
        newPanelist.put("pLName",mockRequest.getParameter("pLName"));
        newPanelist.put("username",mockRequest.getParameter("username"));
        newPanelist.put("password",mockRequest.getParameter("password"));
        newPanelist.put("pInstitution",mockRequest.getParameter("pInstitution"));
        newPanelist.put("pAddress",mockRequest.getParameter("pAddress"));
        newPanelist.put("pCity",mockRequest.getParameter("pCity"));
        newPanelist.put("pState",mockRequest.getParameter("pState"));
        newPanelist.put("pZip",mockRequest.getParameter("pZip"));
        newPanelist.put("pTelephone",mockRequest.getParameter("pTelephone"));
        newPanelist.put("pEmail", mockRequest.getParameter("pEmail"));
        newPanelist.put("pGender",mockRequest.getParameter("pGender"));
        newPanelist.put("pEthnicity",mockRequest.getParameter("pEthnicity"));
        newPanelist.put("pExpertise",mockRequest.getParameter("pExpertise"));
        newPanelist.put("pISCID",mockRequest.getParameter("pISCID"));
        
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
            	StubResponse mock = (StubResponse)invocation.getMock();
		    	String URL = (String) invocation.getArguments()[0];
		    	((StubSession) mock.getSession()).setURL(URL);
                return invocation.getArguments();
            }
        }).when(mockResponse).sendRedirect(anyString());
        
		when(DBManager.createPanelist(newPanelist)).thenReturn(true);
		
		doReturn("createPanelistAccount").when(controllerSpy).getInitParameter("action-type");
		
		/* Act */
		controllerSpy.doPost(mockRequest, mockResponse);
		
		/* Assert */
		PowerMockito.verifyStatic();
		DBManager.createPanelist(newPanelist);
		assertSame("the session url should be 'messagePage?messageCode=Registration Successful'",
				((StubSession) mockSession).getURL(),
				"messagePage?messageCode=Registration Successful");
	}
	@Test
	/**
	 * Sub Systems Test 4(SST4)
	 * Verify that a invalid createPanelistAccount request to the facade will not create 
	 * a user account.
	 * Initial State: HttpSession(StubSession): blank session
	 * Input: HttpRequest(mockRequest), HttpResponse(mockResponse), 
	 * InitParameter("action-type")="createPanelistAccount", invalid panelist info
	 * Expected Output: StubSession.URL = "errorPage?errorCode=Registration failed. 
	 * Please try again."
	 */
	public void SST4() throws Exception{
		when(mockRequest.getParameter("pFName")).thenReturn("mr");
		when(mockRequest.getParameter("pLName")).thenReturn("robot");
		when(mockRequest.getParameter("username")).thenReturn("mrrobot");
		when(mockRequest.getParameter("password")).thenReturn("123abc");
		when(mockRequest.getParameter("pInstitution")).thenReturn("FIU");
		when(mockRequest.getParameter("pAddress")).thenReturn("123 road lane");
		when(mockRequest.getParameter("pCity")).thenReturn("Miami");
		when(mockRequest.getParameter("pState")).thenReturn("Florida");
		when(mockRequest.getParameter("pZip")).thenReturn("123456");
		when(mockRequest.getParameter("pTelephone")).thenReturn("5555555555");
		when(mockRequest.getParameter("pEmail")).thenReturn("mrrobot@fiu.edu");
		when(mockRequest.getParameter("pGender")).thenReturn("male");
		when(mockRequest.getParameter("pEthnicity")).thenReturn("robot");
		when(mockRequest.getParameter("pExpertise")).thenReturn("master");
		when(mockRequest.getParameter("pISCID")).thenReturn("33351");
		
		HashMap<String, String> newPanelist = new HashMap<String, String>();
        newPanelist.put("pFName",mockRequest.getParameter("pFName"));
        newPanelist.put("pLName",mockRequest.getParameter("pLName"));
        newPanelist.put("username",mockRequest.getParameter("username"));
        newPanelist.put("password",mockRequest.getParameter("password"));
        newPanelist.put("pInstitution",mockRequest.getParameter("pInstitution"));
        newPanelist.put("pAddress",mockRequest.getParameter("pAddress"));
        newPanelist.put("pCity",mockRequest.getParameter("pCity"));
        newPanelist.put("pState",mockRequest.getParameter("pState"));
        newPanelist.put("pZip",mockRequest.getParameter("pZip"));
        newPanelist.put("pTelephone",mockRequest.getParameter("pTelephone"));
        newPanelist.put("pEmail", mockRequest.getParameter("pEmail"));
        newPanelist.put("pGender",mockRequest.getParameter("pGender"));
        newPanelist.put("pEthnicity",mockRequest.getParameter("pEthnicity"));
        newPanelist.put("pExpertise",mockRequest.getParameter("pExpertise"));
        newPanelist.put("pISCID",mockRequest.getParameter("pISCID"));
        
		when(DBManager.createPanelist(newPanelist)).thenReturn(false);
		
		doReturn("createPanelistAccount").when(controllerSpy).getInitParameter("action-type");
		doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
            	StubResponse mock = (StubResponse)invocation.getMock();
		    	String URL = (String) invocation.getArguments()[0];
		    	((StubSession) mock.getSession()).setURL(URL);
                return invocation.getArguments();
            }
        }).when(mockResponse).sendRedirect(anyString());
        
		/* Act */
		controllerSpy.doPost(mockRequest, mockResponse);
		
		/* Assert */
		PowerMockito.verifyStatic();
		DBManager.createPanelist(newPanelist);
		assertSame("the session URL should be 'errorPage?errorCode=Registration failed."
				+ " Please try again.'",((StubSession) mockSession).getURL(),
				"errorPage?errorCode=Registration failed. Please try again.");
	}
	@Test
	/**
	 * Sub Systems Test 5(SST5)
	 * Verify that a invalid createPanelistAccount request to the facade 
	 * with an already existing ISCID
	 * will not create a user account
	 * Initial State: HttpSession(StubSession): blank session
	 * Input: HttpRequest(mockRequest), HttpResponse(mockResponse), 
	 * InitParameter("action-type")="createPanelistAccount", invalid panelist info
	 * Expected Output: StubSession.URL = "errorPage?errorCode=Double 
	 * Registration Exception: ISCID is already in use."
	 */
	public void SST5() throws Exception{
		when(mockRequest.getParameter("pFName")).thenReturn("mr");
		when(mockRequest.getParameter("pLName")).thenReturn("robot");
		when(mockRequest.getParameter("username")).thenReturn("mrrobot");
		when(mockRequest.getParameter("password")).thenReturn("123abc");
		when(mockRequest.getParameter("pInstitution")).thenReturn("FIU");
		when(mockRequest.getParameter("pAddress")).thenReturn("123 road lane");
		when(mockRequest.getParameter("pCity")).thenReturn("Miami");
		when(mockRequest.getParameter("pState")).thenReturn("Florida");
		when(mockRequest.getParameter("pZip")).thenReturn("123456");
		when(mockRequest.getParameter("pTelephone")).thenReturn("5555555555");
		when(mockRequest.getParameter("pEmail")).thenReturn("mrrobot@fiu.edu");
		when(mockRequest.getParameter("pGender")).thenReturn("male");
		when(mockRequest.getParameter("pEthnicity")).thenReturn("robot");
		when(mockRequest.getParameter("pExpertise")).thenReturn("master");
		when(mockRequest.getParameter("pISCID")).thenReturn("33351");
		
		HashMap<String, String> newPanelist = new HashMap<String, String>();

        newPanelist.put("pFName",mockRequest.getParameter("pFName"));
        newPanelist.put("pLName",mockRequest.getParameter("pLName"));
        newPanelist.put("username",mockRequest.getParameter("username"));
        newPanelist.put("password",mockRequest.getParameter("password"));
        newPanelist.put("pInstitution",mockRequest.getParameter("pInstitution"));
        newPanelist.put("pAddress",mockRequest.getParameter("pAddress"));
        newPanelist.put("pCity",mockRequest.getParameter("pCity"));
        newPanelist.put("pState",mockRequest.getParameter("pState"));
        newPanelist.put("pZip",mockRequest.getParameter("pZip"));
        newPanelist.put("pTelephone",mockRequest.getParameter("pTelephone"));
        newPanelist.put("pEmail", mockRequest.getParameter("pEmail"));
        newPanelist.put("pGender",mockRequest.getParameter("pGender"));
        newPanelist.put("pEthnicity",mockRequest.getParameter("pEthnicity"));
        newPanelist.put("pExpertise",mockRequest.getParameter("pExpertise"));
        newPanelist.put("pISCID",mockRequest.getParameter("pISCID"));
        
		when(DBManager.createPanelist(newPanelist)).thenThrow(new DoubleRegistrationException());
		
		doReturn("createPanelistAccount").when(controllerSpy).getInitParameter("action-type");
		doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
            	StubResponse mock = (StubResponse)invocation.getMock();
		    	String URL = (String) invocation.getArguments()[0];
		    	((StubSession) mock.getSession()).setURL(URL);
                return invocation.getArguments();
            }
        }).when(mockResponse).sendRedirect(anyString());
        
		/* Act */
		controllerSpy.doPost(mockRequest, mockResponse);
		
		/* Assert */
		PowerMockito.verifyStatic();
		DBManager.createPanelist(newPanelist);
		assertSame("the session URL should be 'errorPage?errorCode=Double"
				+ " Registration Exception: ISCID is already in use.'",
				((StubSession) mockSession).getURL(),
				"errorPage?errorCode=Double Registration Exception: ISCID is already in use.");
		//verify( mockResponse, atMost( 1 ) ).sendRedirect("errorPage?errorCode=Registration failed. Please try again.");
	}
	@Test
	/**
	 * Sub Systems Test 6(SST6)
	 * Verify that a addToPanel request to the facade with valid IDs
	 *  will add to the panel  
	 * Initial State: HttpSession(StubSession): blank session
	 * Input: HttpRequest, HttpResponse, InitParameter("action-type")="addToPanel",
	 *  PanelID, PanelistID
	 * Expected Output: HttpSession(StubSession): 
	 * 			Session URL == "messagePage?messageCode=Panelist has been successfully added."
	 */
	public void SST6() throws Exception{

		doReturn("addToPanel").when(controllerSpy).getInitParameter("action-type");
		when(mockRequest.getParameter("addToPanel")).thenReturn("1");
		when(mockRequest.getParameter("panelistID")).thenReturn("555");
		when(DBManager.addPanelistToPanel(555,1)).thenReturn(true);
		doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
            	StubResponse mock = (StubResponse)invocation.getMock();
		    	String URL = (String) invocation.getArguments()[0];
		    	((StubSession) mock.getSession()).setURL(URL);
                return invocation.getArguments();
            }
        }).when(mockResponse).sendRedirect(anyString());
			
		/* Act */
		controllerSpy.doPost(mockRequest, mockResponse);
		/* Assert */
		PowerMockito.verifyStatic();
		DBManager.addPanelistToPanel(555,1);
		assertSame("the session url is 'messagePage?messageCode=Panelist has been successfully "
				+ "added.'",
				((StubSession) mockSession).getURL(),
				"messagePage?messageCode=Panelist has been successfully added.");
		//verify( mockResponse, atMost( 1 ) ).sendRedirect( "login" );
		
	}
		@Test
	/**
	 * Sub Systems Test 7(SST7)
	 * Verify that a addToPanel request to the facade with invalid IDs 
	 * will not add to the panel  
	 * Initial State: HttpSession(StubSession): blank session
	 * Input: HttpRequest, HttpResponse, InitParameter("action-type")="addToPanel", 
	 * PanelID, PanelistID
	 * Expected Output: HttpSession(StubSession): 
	 * 			Session URL == "errorPage?errorCode=Error adding panelist. Please try again."
	 */
	public void SST7() throws Exception{		
		doReturn("addToPanel").when(controllerSpy).getInitParameter("action-type");
		when(mockRequest.getParameter("addToPanel")).thenReturn("1");
		when(mockRequest.getParameter("panelistID")).thenReturn("555");
		
		when(DBManager.addPanelistToPanel(555,1)).thenReturn(false);
		doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
            	StubResponse mock = (StubResponse)invocation.getMock();
		    	String URL = (String) invocation.getArguments()[0];
		    	((StubSession) mock.getSession()).setURL(URL);
                return invocation.getArguments();
            }
        }).when(mockResponse).sendRedirect(anyString());
			
		/* Act */
		controllerSpy.doPost(mockRequest, mockResponse);
		/* Assert */
		PowerMockito.verifyStatic();
		DBManager.addPanelistToPanel(555,1);
		assertSame("the session url is 'errorPage?errorCode=Error adding panelist. "
				+ "Please try again.'",
				((StubSession) mockSession).getURL(),"errorPage?errorCode=Error"
						+ " adding panelist. Please try again.");
		//verify( mockResponse, atMost( 1 ) ).sendRedirect( "login" );
		
	}
	@Test
	/**		 
	 * Sub Systems Test 8(SST8)
	 * Verify that a updatePanelStatus request to the facade with valid inputs will 
	 * update panel status
	 * and redirect user to the expected page
	 * Initial State: HttpSession(StubSession): blank session
	 * Input: HttpRequest, HttpResponse, InitParameter("action-type")="updatePanelStatus",
	 *  String status, Sting comments, int panelID
	 * Expected Output: HttpSession(StubSession): 
	 * Session URL == ("messagePage?messageCode=Panel status updated.")
	 */
	public void SST8() throws Exception{
		doReturn("updatePanelStatus").when(controllerSpy).getInitParameter("action-type");
		doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
            	StubResponse mock = (StubResponse)invocation.getMock();
		    	String URL = (String) invocation.getArguments()[0];
		    	((StubSession) mock.getSession()).setURL(URL);
                return invocation.getArguments();
            }
        }).when(mockResponse).sendRedirect(anyString());
		when(mockRequest.getParameter("panelStatus")).thenReturn("New Status");
		when(mockRequest.getParameter("statusComments")).thenReturn("Status Updated");
		when(mockRequest.getParameter("panelID")).thenReturn("1");
        when(DBManager.updatePanelStatus("New Status", "Status Updated", 1)).thenReturn(true);
        
		/* Act */
		controllerSpy.doPost(mockRequest, mockResponse);
		/* Assert */
		assertSame("the session url should be 'messagePage?messageCode=Panel status updated.'",
				((StubSession) mockSession).getURL(),
				"messagePage?messageCode=Panel status updated.");
		//verify( mockResponse, atMost( 1 ) ).sendRedirect( "login" );
		
	}
	@Test
	/**		 
	 * Sub Systems Test 9(SST9)
	 * Verify that a updatePanelStatus request to the facade with invalid inputs
	 *  will not update panel status
	 * and redirect user to the expected page
	 * Initial State: HttpSession(StubSession): blank session
	 * Input: HttpRequest, HttpResponse,
	 *  InitParameter("action-type")="updatePanelStatus",
	 *   String status, Sting comments, int panelID
	 * Expected Output: HttpSession(StubSession):
	 *  Session URL == ("errorPage?errorCode=Error updating panel. Please try again.")
	 */
	public void SST9() throws Exception{
		doReturn("updatePanelStatus").when(controllerSpy).getInitParameter("action-type");
		doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
            	StubResponse mock = (StubResponse)invocation.getMock();
		    	String URL = (String) invocation.getArguments()[0];
		    	((StubSession) mock.getSession()).setURL(URL);
                return invocation.getArguments();
            }
        }).when(mockResponse).sendRedirect(anyString());
		when(mockRequest.getParameter("panelStatus")).thenReturn("New Status");
		when(mockRequest.getParameter("statusComments")).thenReturn("Status Updated");
		when(mockRequest.getParameter("panelID")).thenReturn("1");
        when(DBManager.updatePanelStatus("New Status", "Status Updated", 1)).thenReturn(false);
        
		/* Act */
		controllerSpy.doPost(mockRequest, mockResponse);
		/* Assert */
		assertSame("the session url should be 'errorPage?errorCode=Error updating panel."
				+ " Please try again.'",
				((StubSession) mockSession).getURL(),"errorPage?errorCode=Error updating panel."
						+ " Please try again.");	
	}
	@Test
	/**		 
	 * Sub Systems Test 10(SST10)
	 * Verify that a createPanel request to the facade with valid inputs
	 *  will create a panel
	 * and redirect user to the expected page
	 * Initial State: HttpSession(StubSession): blank session
	 * Input: HttpRequest, HttpResponse, 
	 * InitParameter("action-type")="createPanel", String panelName, 
	 * String panelDescription, String employeeID, boolean result
	 * Expected Output: HttpSession(StubSession): 
	 * Session URL == ("messagePage?messageCode=Panel has been successfully created.")
	 */
	public void SST10() throws Exception{
		//EmployeeProfile emp = mock(EmployeeProfile.class);
		
		doReturn("createPanel").when(controllerSpy).getInitParameter("action-type");
		doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
            	StubResponse mock = (StubResponse)invocation.getMock();
		    	String URL = (String) invocation.getArguments()[0];
		    	((StubSession) mock.getSession()).setURL(URL);
                return invocation.getArguments();
            }
        }).when(mockResponse).sendRedirect(anyString());
		when(mockRequest.getParameter("panelName")).thenReturn("New Panel");
		when(mockRequest.getParameter("panelDescription")).thenReturn("Panel Created");
        when(DBManager.createPanel("New Panel", "Panel Created",0)).thenReturn(true);
        
		/* Act */
		controllerSpy.doPost(mockRequest, mockResponse);
		/* Assert */
		assertSame("the session url should be 'messagePage?messageCode=Panel has been successfully created.'",
				((StubSession) mockSession).getURL(),
				"messagePage?messageCode=Panel has been successfully created.");
		
	}	
	@Test
	/**		 
	 * Sub Systems Test 11(SST11)
	 * Verify that a createPanel request to the facade with invalid inputs
	 *  will not create a panel
	 * and redirect user to the expected page
	 * Initial State: HttpSession(StubSession): blank session
	 * Input: HttpRequest, HttpResponse, 
	 * InitParameter("action-type")="createPanel", String panelName, 
	 * String panelDescription, String employeeID, boolean result
	 * Expected Output: HttpSession(StubSession): 
	 * Session URL == ("errorPage?errorCode=There was an error creating the panel. 
	 * Please try again.")
	 */
	public void SST11() throws Exception{
		doReturn("createPanel").when(controllerSpy).getInitParameter("action-type");
		doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
            	StubResponse mock = (StubResponse)invocation.getMock();
		    	String URL = (String) invocation.getArguments()[0];
		    	((StubSession) mock.getSession()).setURL(URL);
                return invocation.getArguments();
            }
        }).when(mockResponse).sendRedirect(anyString());
		when(mockRequest.getParameter("panelName")).thenReturn("New Panel");
		when(mockRequest.getParameter("panelDescription")).thenReturn("Panel Created");
		//when(mockRequest.getParameter("employeeID")).thenReturn("1");
        when(DBManager.createPanel("New Panel", "Panel Created",0)).thenReturn(false);
        
		/* Act */
		controllerSpy.doPost(mockRequest, mockResponse);
		/* Assert */
		assertSame("the session url should be 'errorPage?errorCode=There was an error"
				+ " creating the panel. Please try again.",
				((StubSession) mockSession).getURL(),
				"errorPage?errorCode=There was an error creating the panel. Please try again.");
		
	}
	@Test
	/**
	 * Sub Systems Test 12(SST12)
	 * Verify that a valid searchPanelists request to the facade will search 
	 * a panelist.
	 * Initial State: HttpSession(StubSession): blank session
	 * Input: HttpRequest(mockRequest), HttpResponse(mockResponse), 
	 * InitParameter("action-type")="searchPanelists", invalid panelist info
	 * Expected Output: StubSession.URL = "displayPanelists.jsp"
	 */
	public void SST12() throws Exception{
		when(mockRequest.getParameter("pFName")).thenReturn("mr");
		when(mockRequest.getParameter("pLName")).thenReturn("robot");
		when(mockRequest.getParameter("pInstitution")).thenReturn("FIU");
		when(mockRequest.getParameter("pAddress")).thenReturn("123 road lane");
		when(mockRequest.getParameter("pCity")).thenReturn("Miami");
		when(mockRequest.getParameter("pState")).thenReturn("Florida");
		when(mockRequest.getParameter("pZipCode")).thenReturn("123456");
		when(mockRequest.getParameter("pTelephone")).thenReturn("5555555555");
		when(mockRequest.getParameter("pEmail")).thenReturn("mrrobot@fiu.edu");
		when(mockRequest.getParameter("pGender")).thenReturn("male");
		when(mockRequest.getParameter("pEtnicity")).thenReturn("robot");
		when(mockRequest.getParameter("pExpertise")).thenReturn("master");
		when(mockRequest.getParameter("pISCID")).thenReturn("33351");
		
		HashMap<String, String> newPanelist = new HashMap<String, String>();
        newPanelist.put("FirstName",mockRequest.getParameter("pFName"));
        newPanelist.put("LastName",mockRequest.getParameter("pLName"));
        newPanelist.put("Institution",mockRequest.getParameter("pInstitution"));
        newPanelist.put("Address",mockRequest.getParameter("pAddress"));
        newPanelist.put("City",mockRequest.getParameter("pCity"));
        newPanelist.put("State",mockRequest.getParameter("pState"));
        newPanelist.put("ZipCode",mockRequest.getParameter("pZip"));
        newPanelist.put("Telephone",mockRequest.getParameter("pTelephone"));
        newPanelist.put("Email", mockRequest.getParameter("pEmail"));
        newPanelist.put("Gender",mockRequest.getParameter("pGender"));
        newPanelist.put("Ethnicity",mockRequest.getParameter("pEthnicity"));
        newPanelist.put("Expertise",mockRequest.getParameter("pExpertise"));
        newPanelist.put("ISCID",mockRequest.getParameter("pISCID"));
        
        ArrayList<PanelistProfile> panArrayList = new ArrayList<PanelistProfile>();
        panArrayList.add(new PanelistProfile());
		when(DBManager.getPanelists(newPanelist)).thenReturn(panArrayList);
		
		doReturn("doSearch").when(controllerSpy).getInitParameter("action-type");
		doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
            	StubResponse mock = (StubResponse)invocation.getMock();
		    	String URL = (String) invocation.getArguments()[0];
		    	((StubSession) mock.getSession()).setURL(URL);
                return invocation.getArguments();
            }
        }).when(mockResponse).sendRedirect(anyString());
        
		/* Act */
		controllerSpy.doPost(mockRequest, mockResponse);
		
		/* Assert */
		assertTrue( "the session should contain a Panelist Arraylist",
                mockSession.getAttribute( "Panelists" ) instanceof ArrayList );
		assertSame("the session URL should be 'displayPanelists.jsp'",((StubSession) mockSession).getURL(),
				"displayPanelists.jsp");
	}
	@Test
	/**
	 * Sub Systems Test 13(SST13)
	 * Verify that a invalid searchPanelists request to the facade will search 
	 * a panelist.
	 * Initial State: HttpSession(StubSession): blank session
	 * Input: HttpRequest(mockRequest), HttpResponse(mockResponse), 
	 * InitParameter("action-type")="searchPanelists", invalid panelist info
	 * Expected Output: StubSession.URL = "displayPanelists.jsp"
	 */
	public void SST13() throws Exception{
		when(mockRequest.getParameter("pFName")).thenReturn("mr");
		when(mockRequest.getParameter("pLName")).thenReturn("robot");
		when(mockRequest.getParameter("pInstitution")).thenReturn("FIU");
		when(mockRequest.getParameter("pAddress")).thenReturn("123 road lane");
		when(mockRequest.getParameter("pCity")).thenReturn("Miami");
		when(mockRequest.getParameter("pState")).thenReturn("Florida");
		when(mockRequest.getParameter("pZipCode")).thenReturn("123456");
		when(mockRequest.getParameter("pTelephone")).thenReturn("5555555555");
		when(mockRequest.getParameter("pEmail")).thenReturn("mrrobot@fiu.edu");
		when(mockRequest.getParameter("pGender")).thenReturn("male");
		when(mockRequest.getParameter("pEtnicity")).thenReturn("robot");
		when(mockRequest.getParameter("pExpertise")).thenReturn("master");
		when(mockRequest.getParameter("pISCID")).thenReturn("33351");
		
		HashMap<String, String> newPanelist = new HashMap<String, String>();
        newPanelist.put("FirstName",mockRequest.getParameter("pFName"));
        newPanelist.put("LastName",mockRequest.getParameter("pLName"));
        newPanelist.put("Institution",mockRequest.getParameter("pInstitution"));
        newPanelist.put("Address",mockRequest.getParameter("pAddress"));
        newPanelist.put("City",mockRequest.getParameter("pCity"));
        newPanelist.put("State",mockRequest.getParameter("pState"));
        newPanelist.put("ZipCode",mockRequest.getParameter("pZip"));
        newPanelist.put("Telephone",mockRequest.getParameter("pTelephone"));
        newPanelist.put("Email", mockRequest.getParameter("pEmail"));
        newPanelist.put("Gender",mockRequest.getParameter("pGender"));
        newPanelist.put("Ethnicity",mockRequest.getParameter("pEthnicity"));
        newPanelist.put("Expertise",mockRequest.getParameter("pExpertise"));
        newPanelist.put("ISCID",mockRequest.getParameter("pISCID"));
        
		when(DBManager.getPanelists(newPanelist)).thenReturn(null);
		
		doReturn("doSearch").when(controllerSpy).getInitParameter("action-type");
		doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
            	StubResponse mock = (StubResponse)invocation.getMock();
		    	String URL = (String) invocation.getArguments()[0];
		    	((StubSession) mock.getSession()).setURL(URL);
                return invocation.getArguments();
            }
        }).when(mockResponse).sendRedirect(anyString());
        
		/* Act */
		controllerSpy.doPost(mockRequest, mockResponse);
		
		/* Assert */
		assertSame("the session URL should be 'messagePage?messageCode=No Panelists Found.'",
				((StubSession) mockSession).getURL(),
				"messagePage?messageCode=No Panelists Found.");
	}
	@Test
	/**
	 * Sub Systems Test 14(SST14)
	 * Verify that a log out request to the facade with valid credentials (Panelist User type) 
	 * will log the current user in.
	 * Initial State: HttpSession(StubSession): blank session
	 * Input: HttpRequest, HttpResponse, InitParameter("action-type")="doLogin",
	 *  valid user credentials
	 * Expected Output: HttpSession(StubSession): Attributes:"User Profile"==mockUser,
	 * 	 	"User Form"== instanceof PanelistForms, 
	 * 		and Session URL == ("messagePage?messageCode=You are now logged in. Welcome.")
	 */
	public void SST14() throws Exception{
		
		doReturn("doLogout").when(controllerSpy).getInitParameter("action-type");
		doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
            	StubResponse mock = (StubResponse)invocation.getMock();
		    	String URL = (String) invocation.getArguments()[0];
		    	((StubSession) mock.getSession()).setURL(URL);
                return invocation.getArguments();
            }
        }).when(mockResponse).sendRedirect(anyString());
	
		/* Act */
		controllerSpy.doGet(mockRequest, mockResponse);	
		/* Assert */
		assertTrue( "the session should be invalidated",
                mockSession.sessionInvalidated);
		assertSame("the session url should be 'index.jsp'",
				((StubSession) mockSession).getURL(),
				"index.jsp");
		
	}


}
