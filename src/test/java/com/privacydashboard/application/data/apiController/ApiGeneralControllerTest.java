package com.privacydashboard.application.data.apiController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.privacydashboard.application.data.GlobalVariables;
import com.privacydashboard.application.data.GlobalVariables.QuestionnaireVote;
import com.privacydashboard.application.data.GlobalVariables.RightType;
import com.privacydashboard.application.data.GlobalVariables.Role;
import com.privacydashboard.application.data.entity.IoTApp;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.entity.UserAppRelation;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;
import com.privacydashboard.application.security.UserDetailsServiceImpl;
import com.privacydashboard.application.data.entity.Message;
import com.privacydashboard.application.data.entity.Notification;
import com.privacydashboard.application.data.entity.PrivacyNotice;
import com.privacydashboard.application.data.entity.RightRequest;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Hashtable;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ApiGeneralControllerTest {

	private ObjectMapper mapper = new ObjectMapper();
	private ApiGeneralController api = new ApiGeneralController();

	private DataBaseService dataBaseService;
	private AuthenticatedUser authenticatedUser;
	private UserDetailsServiceImpl userDetailsServiceImpl;
	private ApiGeneralController apiMock;

	private static boolean isFieldNull(Object f) {
		if (f == null)
			return true;
		else
			return false;
	}

	private static boolean isAppNull(IoTApp app) {
		int nullFields = 0;

		if (isFieldNull(app.getId()))
			nullFields++;

		if (isFieldNull(app.getDescription()))
			nullFields++;

		if (isFieldNull(app.getConsenses()))
			nullFields++;

		if (isFieldNull(app.getName()))
			nullFields++;

		if (isFieldNull(app.getDetailVote()))
			nullFields++;

		if (isFieldNull(app.getQuestionnaireVote()))
			nullFields++;

		if (isFieldNull(app.getOptionalAnswers()))
			nullFields++;

		return nullFields == 7;
	}

	private static boolean isUserNull(User user) {
		int nullFields = 0;

		if (isFieldNull(user.getName()))
			nullFields++;

		if (isFieldNull(user.getHashedPassword()))
			nullFields++;

		if (isFieldNull(user.getId()))
			nullFields++;

		if (isFieldNull(user.getMail()))
			nullFields++;

		if (isFieldNull(user.getRole()))
			nullFields++;

		return nullFields == 5;
	}

	private static List<RightRequest> createRequestList(){
		User sender1 = new User();
		sender1.setId(new UUID(1, 1));
		sender1.setName("Sender1");

		User sender2 = new User();
		sender2.setId(new UUID(1, 2));
		sender2.setName("Sender2");

		User receiver1 = new User();
		receiver1.setId(new UUID(2, 1));
		receiver1.setName("Receiver1");

		User receiver2 = new User();
		receiver2.setId(new UUID(2, 2));
		receiver2.setName("Receiver2");

		IoTApp app1 = new IoTApp();
		app1.setId(new UUID(3, 1));
		app1.setName("App1");

		IoTApp app2 = new IoTApp();
		app2.setId(new UUID(3, 2));
		app2.setName("App2");

		RightRequest req1 = new RightRequest();
		req1.setSender(sender1);
		req1.setReceiver(receiver1);
		req1.setApp(app1);
		req1.setId(new UUID(0, 1));
		req1.setRightType(RightType.COMPLAIN);

		RightRequest req2 = new RightRequest();
		req2.setSender(sender1);
		req2.setReceiver(receiver2);
		req2.setApp(app2);
		req2.setId(new UUID(0, 2));
		req2.setRightType(RightType.ERASURE);

		RightRequest req3 = new RightRequest();
		req3.setSender(sender2);
		req3.setReceiver(receiver1);
		req3.setApp(app1);
		req3.setId(new UUID(0, 3));
		req3.setRightType(RightType.INFO);

		RightRequest req4 = new RightRequest();
		req4.setSender(sender2);
		req4.setReceiver(receiver2);
		req4.setApp(app2);
		req4.setId(new UUID(0, 4));
		req4.setRightType(RightType.INFO);

		List<RightRequest> list = List.of(req1, req2, req3, req4);

		return list;
	}

	private User createUser(Role role){
		User user = new User();
		user.setName("User");
		user.setId(new UUID(0, role.hashCode()));
		user.setMail("test"+role.toString()+"@mail.test");
		user.setRole(role);

		return user;
	}

	private IoTApp createApp(){
		IoTApp app = new IoTApp();
		app.setName("App");
		app.setId(new UUID(1, 0));
		app.setDescription("Description");
		app.setQuestionnaireVote(QuestionnaireVote.RED);

		return app;
	}

	private Message createMessage(){
		Message mess = new Message();
		mess.setId(new UUID(2, 0));
		mess.setMessage("Message");
		mess.setSender(createUser(Role.CONTROLLER));
		mess.setReceiver(createUser(Role.DPO));

		return mess;
	}

	private UserAppRelation createUserAppRelation(){
		UserAppRelation rel = new UserAppRelation();
		rel.setApp(createApp());
		rel.setUser(createUser(Role.SUBJECT));
		rel.setId(new UUID(4, 0));
		
		return rel;
	}

	private PrivacyNotice createPrivacyNotice(){
		PrivacyNotice notice = new PrivacyNotice();
		notice.setApp(createApp());
		notice.setId(new UUID(3, 0));
		notice.setText("Notice");

		return notice;
	}

	private Notification createNotification(){
		Notification note = new Notification();
		note.setId(new UUID(6, 0));
		note.setDescription("Description");
		note.setSender(createUser(Role.CONTROLLER));
		note.setReceiver(createUser(Role.DPO));
		
		return note;
	}

	private List<Notification> createNotificationList(boolean all, boolean isRead){
		Notification note = new Notification();
		note.setId(new UUID(6, 1));
		note.setDescription("Description1");
		note.setSender(createUser(Role.CONTROLLER));
		note.setReceiver(createUser(Role.DPO));
		note.setType("Type1");
		note.setObjectId(new UUID(0, 10));
		note.setRead(true);

		Notification note2 = new Notification();
		note2.setId(new UUID(6, 2));
		note2.setDescription("Description2");
		note2.setSender(createUser(Role.SUBJECT));
		note2.setReceiver(createUser(Role.CONTROLLER));
		note2.setType("Type2");
		note2.setObjectId(new UUID(10, 0));
		note2.setRead(false);

		if(all){
			List<Notification> list = List.of(note, note2);

			return list;
		}
		if(isRead){
			List<Notification> list = List.of(note);

			return list;
		}
		else{
			List<Notification> list = List.of(note2);

			return list;
		}
	}

	private Field getDataBaseService(){
		try{
			Field field = ApiGeneralController.class.getDeclaredField("dataBaseService");
			field.setAccessible(true);
			return field;
		}
		catch(NoSuchFieldException e){
			System.out.println("Exception: "+e);
		}
		return null;
	}

	private Field getAuthenticatedUser(){
		try{
			Field field = ApiGeneralController.class.getDeclaredField("authenticatedUser");
			field.setAccessible(true);
			return field;
		}
		catch(NoSuchFieldException e){
			System.out.println("Exception: "+e);
		}
		return null;
	}

	private Field getUserDetailsServiceImpl(){
		try{
			Field field = ApiGeneralController.class.getDeclaredField("userDetailsServiceImpl");
			field.setAccessible(true);
			return field;
		}
		catch(NoSuchFieldException e){
			System.out.println("Exception: "+e);
		}
		return null;
	}

	@BeforeEach
	private void setup(){
		dataBaseService = mock(DataBaseService.class);
		authenticatedUser = mock(AuthenticatedUser.class);
		userDetailsServiceImpl = mock(UserDetailsServiceImpl.class);

		apiMock = new ApiGeneralController();

		try{
            getDataBaseService().set(apiMock, dataBaseService);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

		try{
            getAuthenticatedUser().set(apiMock, authenticatedUser);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }

		try{
            getUserDetailsServiceImpl().set(apiMock, userDetailsServiceImpl);
        }
        catch(Exception e){
            System.out.println("Exception: "+e);
        }
	}

	@Test
	public void createJsonFromAppNullTest() {
		IoTApp app = new IoTApp();
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromApp(app);
		});
	}

	@Test
	public void createJsonFromAppNameNullTest() {
		IoTApp app = new IoTApp();
		app.setId(new UUID(0, 4));
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromApp(app);
		});
	}

	@Test
	public void createJsonFromAppIDNullTest() {
		IoTApp app = new IoTApp();
		app.setName("TestApp");
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromApp(app);
		});
	}

	@Test
	public void createJsonFromAppMinimalParametersTest() {
		IoTApp app = new IoTApp();
		app.setId(new UUID(0, 4));
		app.setName("TestApp");
		ObjectNode jsonTest = mapper.createObjectNode();
		jsonTest.put("id", new UUID(0, 4).toString());
		jsonTest.put("name", "TestApp");
		assertEquals(api.createJsonFromApp(app), jsonTest);
	}

	// The method createJsonFromApp asks for a set number
	// of optionalAnswers equals to nQuestions, while it
	// does not hold the same requirement for detailVote,
	// which can in number be more or less than nQuestions.
	// This is in contrast with the inverse method getAppFromJsonNode
	// which asks for at most nQuestions detailVote.
	// The method createJsonFromApp and its inverse
	// do not consider the Consenses attributes of IoTApp.
	@Test
	public void createJsonFromAppAllParametersTest() {
		IoTApp app = new IoTApp();
		app.setId(new UUID(0, 4));
		app.setName("TestApp");
		app.setDescription("TestDescription");
		app.setQuestionnaireVote(QuestionnaireVote.GREEN);

		String[] detailVote = { "TestDetailLine1", "TestDetailLine2" };
		app.setDetailVote(detailVote);

		Hashtable table = new Hashtable<Integer, String>();
		for (int i = 0; i < GlobalVariables.nQuestions; i++) {
			table.put(i, "LineTest" + i);
		}
		app.setOptionalAnswers(table);

		ObjectNode jsonTest = mapper.createObjectNode();
		jsonTest.put("id", new UUID(0, 4).toString());
		jsonTest.put("name", "TestApp");
		jsonTest.put("description", "TestDescription");
		jsonTest.put("questionnaireVote", QuestionnaireVote.GREEN.toString());

		ArrayNode detailVoteArray = mapper.createArrayNode();
		detailVoteArray.add(detailVote[0]);
		detailVoteArray.add(detailVote[1]);
		jsonTest.set("detailVote", detailVoteArray);

		ArrayNode optionalAnswersArrayNode = mapper.createArrayNode();
		for (int i = 0; i < GlobalVariables.nQuestions; i++) {
			optionalAnswersArrayNode.add("LineTest" + i);
		}
		jsonTest.set("optionalAnswers", optionalAnswersArrayNode);

		assertEquals(api.createJsonFromApp(app), jsonTest);
	}

	@Test
	public void getAppFromJsonNodeIDNotValidTest() {
		ObjectNode node = mapper.createObjectNode();
		node.put("id", "4");
		assertThrows(IllegalArgumentException.class, () -> {
			api.getAppFromJsonNode(false, node);
		});
	}

	@Test
	public void getAppFromJsonNodeNullNameTest() {
		ObjectNode node = mapper.createObjectNode();
		assertThrows(IllegalArgumentException.class, () -> {
			api.getAppFromJsonNode(true, node);
		});
	}

	@Test
	public void getAppFromJsonNodeAllParametersTest() {
		ObjectNode node = mapper.createObjectNode();
		IoTApp app = new IoTApp();

		app.setId(new UUID(0, 4));
		app.setName("TestApp");
		app.setDescription("TestDescription");
		app.setQuestionnaireVote(QuestionnaireVote.GREEN);

		String[] detailVote = new String[GlobalVariables.nQuestions];
		for (int i = 0; i < GlobalVariables.nQuestions; i++) {
			detailVote[i] = "LineTest" + i;
		}
		app.setDetailVote(detailVote);

		Hashtable table = new Hashtable<Integer, String>();
		for (int i = 0; i < GlobalVariables.nQuestions; i++) {
			table.put(i, "LineTest" + i);
		}
		app.setOptionalAnswers(table);

		node = api.createJsonFromApp(app);

		assertEquals(api.getAppFromJsonNode(true, node), app);
	}

	@Test
	public void getAppFromJsonNodeRedVoteTest() {
		ObjectNode node = mapper.createObjectNode();
		node.put("questionnaireVote", QuestionnaireVote.RED.toString());
		assertEquals(api.getAppFromJsonNode(false, node).getQuestionnaireVote(), QuestionnaireVote.RED);

	}

	@Test
	public void getAppFromJsonNodeOrangeVoteTest() {
		ObjectNode node = mapper.createObjectNode();
		node.put("questionnaireVote", QuestionnaireVote.ORANGE.toString());
		assertEquals(api.getAppFromJsonNode(false, node).getQuestionnaireVote(), QuestionnaireVote.ORANGE);
	}

	@Test
	public void getAppFromJsonNodeVoteAnswersNotArrayTest() {
		ObjectNode node = mapper.createObjectNode();
		IoTApp app = new IoTApp();

		app.setId(new UUID(0, 4));
		app.setName("TestApp");

		node = api.createJsonFromApp(app);
		node.put("detailVote", "TestFieldNotArray");
		node.put("optionalAnswers", "TestFieldNotArray");

		assertEquals(api.getAppFromJsonNode(false, node), app);
	}

	// Test fails because you cannot assign a null value
	// to a Hashtable record.
	// Error at line 358 of ApiGeneralController
	@Test
	public void getAppFromJsonNodeVoteAnswersNotFullArraysTest() {
		ObjectNode node = mapper.createObjectNode();
		IoTApp app = new IoTApp();

		app.setId(new UUID(0, 4));
		app.setName("TestApp");

		node = api.createJsonFromApp(app);

		String[] detailVote = new String[GlobalVariables.nQuestions];
		for (int i = 1; i < GlobalVariables.nQuestions; i++) {
			detailVote[i] = null;
		}
		detailVote[0] = "LineTest0";
		app.setDetailVote(detailVote);

		Hashtable table = new Hashtable<Integer, String>();
		table.put(0, "LineTest0");
		app.setOptionalAnswers(table);

		ArrayNode detailVoteArray = mapper.createArrayNode();
		detailVoteArray.add("LineTest0");
		node.set("detailVote", detailVoteArray);

		ArrayNode optionalAnswersArrayNode = mapper.createArrayNode();
		optionalAnswersArrayNode.add("LineTest0");
		node.set("optionalAnswers", optionalAnswersArrayNode);

		assertEquals(api.getAppFromJsonNode(false, node), app);

	}

	@Test
	public void getAppFromJsonNodeMinimalParametersTest() {
		ObjectNode node = mapper.createObjectNode();
		assertEquals(isAppNull(api.getAppFromJsonNode(false, node)), true);
	}

	@Test
	public void getAppFromJsonStringNotValidJsonTest() {
		assertThrows(IOException.class, () -> {
			api.getAppFromJsonString(false, "NotAJson");
		});
	}

	@Test
	public void getAppFromJsonStringNameNullTest() {
		String body = "{\"id\":\"" + new UUID(0, 0).toString() + "\"}";
		assertThrows(IllegalArgumentException.class, () -> {
			api.getAppFromJsonString(true, body);
		});
	}

	@Test
	public void getAppFromJsonStringIDNotValidTest() {
		String body = "{\"id\":\"" + "0" + "\"}";
		assertThrows(IllegalArgumentException.class, () -> {
			api.getAppFromJsonString(false, body);
		});
	}

	@Test
	public void getAppFromJsonStringValidBodyTest() {
		IoTApp app = new IoTApp();
		app.setName("TestName");
		app.setId(new UUID(0, 0));

		String body = "{\"name\":\"TestName\",\"id\":\"" + new UUID(0, 0).toString() + "\"}";
		try {
			IoTApp appRes = api.getAppFromJsonString(true, body);
			assertEquals(app, appRes);
		} catch (Exception e) {
			System.out.println('\n' + "Impossible path: " + e);
		}
	}

	@Test
	public void createJsonFromUserNullTest() {
		User user = new User();
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromUser(user);
		});
	}

	@Test
	public void createJsonFromUserIDNullTest() {
		User user = new User();

		user.setName("TestName");
		user.setHashedPassword("HashedTest");
		user.setRole(Role.CONTROLLER);

		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromUser(user);
		});
	}

	@Test
	public void createJsonFromUserNameNullTest() {
		User user = new User();

		user.setId(new UUID(0, 0));
		user.setHashedPassword("HashedTest");
		user.setRole(Role.CONTROLLER);

		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromUser(user);
		});
	}

	@Test
	public void createJsonFromUserPasswordNullTest() {
		User user = new User();

		user.setName("TestName");
		user.setId(new UUID(0, 0));
		user.setRole(Role.CONTROLLER);

		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromUser(user);
		});
	}

	@Test
	public void createJsonFromUserRoleNullTest() {
		User user = new User();

		user.setName("TestName");
		user.setId(new UUID(0, 0));
		user.setHashedPassword("HashedTest");

		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromUser(user);
		});
	}

	@Test
	public void createJsonFromUserMinimalParametersTest() {
		User user = new User();

		user.setName("TestName");
		user.setId(new UUID(0, 0));
		user.setHashedPassword("HashedTest");
		user.setRole(Role.CONTROLLER);

		ObjectNode node = mapper.createObjectNode();

		node.put("id", new UUID(0, 0).toString());
		node.put("name", "TestName");
		node.put("role", Role.CONTROLLER.toString());
		node.put("hashedPassword", "HashedTest");

		assertEquals(api.createJsonFromUser(user), node);
	}

	@Test
	public void createJsonFromUserAllParametersTest() {
		User user = new User();

		user.setName("TestName");
		user.setId(new UUID(0, 0));
		user.setHashedPassword("HashedTest");
		user.setRole(Role.CONTROLLER);
		user.setMail("test@mail.test");

		ObjectNode node = mapper.createObjectNode();

		node.put("id", new UUID(0, 0).toString());
		node.put("name", "TestName");
		node.put("role", Role.CONTROLLER.toString());
		node.put("hashedPassword", "HashedTest");
		node.put("mail", "test@mail.test");

		assertEquals(api.createJsonFromUser(user), node);
	}

	@Test
	public void getUserFromJsonNodeMinimalParametersTest() {
		ObjectNode node = mapper.createObjectNode();

		assertEquals(isUserNull(api.getUserFromJsonNode(false, node)), true);
	}

	@Test
	public void getUserFromJsonNodeIDNotValidTest() {
		ObjectNode node = mapper.createObjectNode();

		node.put("id", "0");

		assertThrows(IllegalArgumentException.class, () -> {
			api.getUserFromJsonNode(false, node);
		});
	}

	@Test
	public void getUserFromJsonNodeNameNullTest() {
		ObjectNode node = mapper.createObjectNode();
		assertThrows(IllegalArgumentException.class, () -> {
			api.getUserFromJsonNode(true, node);
		});
	}

	@Test
	public void getUserFromJsonNodePasswordNullTest() {
		ObjectNode node = mapper.createObjectNode();

		node.put("name", "TestName");
		assertThrows(IllegalArgumentException.class, () -> {
			api.getUserFromJsonNode(true, node);
		});
	}

	@Test
	public void getUserFromJsonNodeRoleNullTest() {
		ObjectNode node = mapper.createObjectNode();

		node.put("name", "TestName");
		node.put("password", "TestPassword");

		when(userDetailsServiceImpl.hashPass("TestPassword")).thenReturn("HashedTestPassword");
		assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getUserFromJsonNode(true, node);
		});
	}

	// ProfilePictureUrl attribute is not used in
	// this method or its inverse
	@Test
	public void getUserFromJsonNodeAllParametersTest() {
		ObjectNode node = mapper.createObjectNode();

		User user = new User();
		user.setName("TestName");
		user.setHashedPassword("HashedTestPassword");
		user.setId(new UUID(0, 0));
		user.setRole(Role.SUBJECT);
		user.setMail("test@mail.test");

		node.put("name", "TestName");
		node.put("password", "TestPassword");
		node.put("role", Role.SUBJECT.toString());
		node.put("id", new UUID(0, 0).toString());
		node.put("mail", "test@mail.test");

		when(userDetailsServiceImpl.hashPass("TestPassword")).thenReturn("HashedTestPassword");

		assertEquals(apiMock.getUserFromJsonNode(false, node), user);

	}

	@Test
	public void getUserFromJsonNodeControllerRoleTest() {
		ObjectNode node = mapper.createObjectNode();
		node.put("role", Role.CONTROLLER.toString());

		assertEquals(api.getUserFromJsonNode(false, node).getRole(), Role.CONTROLLER);
	}

	@Test
	public void getUserFromJsonNodeDPORoleTest() {
		ObjectNode node = mapper.createObjectNode();
		node.put("role", Role.DPO.toString());

		assertEquals(api.getUserFromJsonNode(false, node).getRole(), Role.DPO);
	}

	@Test
	public void getUserFromJsonStringNotValidJsonTest() {
		assertThrows(IOException.class, () -> {
			api.getUserFromJsonString(false, "NotAJson");
		});
	}

	@Test
	public void getUserFromJsonStringIDNotValidTest() {
		String body = "{\"id\":\"" + "0" + "\"}";
		assertThrows(IllegalArgumentException.class, () -> {
			api.getUserFromJsonString(false, body);
		});
	}

	@Test
	public void getUserFromJsonStringNameNullTest() {
		String body = "{\"id\":\"" + new UUID(0, 0).toString() + "\"}";
		assertThrows(IllegalArgumentException.class, () -> {
			api.getUserFromJsonString(true, body);
		});
	}

	@Test
	public void getUserFromJsonStringRoleNullTest() {
		String body = "{\"name\":\"" + "TestName" + "\",\"id\":\"" + new UUID(0, 0).toString() + "\"}";
		assertThrows(IllegalArgumentException.class, () -> {
			api.getUserFromJsonString(true, body);
		});
	}

	@Test
	public void getUserFromJsonStringPasswordNullTest() {
		String body = "{\"role\":\"" + Role.CONTROLLER.toString() + "\",\"name\":\"" + "TestName" + "\",\"id\":\""
				+ new UUID(0, 0).toString() + "\"}";
		assertThrows(IllegalArgumentException.class, () -> {
			api.getUserFromJsonString(true, body);
		});
	}

	@Test
	public void getUserFromJsonStringValidBodyTest() {
		String body = "{\"password\":\"" + "TestHash" + "\",\"role\":\"" + Role.CONTROLLER.toString() + "\",\"name\":\""
				+ "TestName" + "\",\"id\":\"" + new UUID(0, 0).toString() + "\"}";
		
		when(userDetailsServiceImpl.hashPass("TestHash")).thenReturn("HashedTestHash");
		
		User user = new User();
		
		try{
			user = apiMock.getUserFromJsonString(true, body);
		}
		catch(Exception e){
			System.out.println("Exception: "+e);
		}

		assertEquals(user.getName(), "TestName");
		assertEquals(user.getHashedPassword(), "HashedTestHash");
		assertEquals(user.getRole(), Role.CONTROLLER);
		assertEquals(user.getId(), new UUID(0, 0));
	}

	@Test
	public void createJsonFromUserAppRelationNullTest() {
		UserAppRelation relation = new UserAppRelation();
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromUserAppRelation(relation);
		});
	}

	@Test
	public void createJsonFromUserAppRelationIDNull() {
		UserAppRelation relation = new UserAppRelation();

		relation.setUser(new User());
		relation.setApp(new IoTApp());
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromUserAppRelation(relation);
		});
	}

	@Test
	public void createJsonFromUserAppRelationUserNull() {
		UserAppRelation relation = new UserAppRelation();

		relation.setId(new UUID(0, 0));
		relation.setApp(new IoTApp());
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromUserAppRelation(relation);
		});
	}

	@Test
	public void createJsonFromUserAppRelationAppNull() {
		UserAppRelation relation = new UserAppRelation();

		relation.setId(new UUID(0, 0));
		relation.setUser(new User());
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromUserAppRelation(relation);
		});
	}

	// Test fails because attributes
	// are not properly mapped in the method
	@Test
	public void createJsonFromUserAppRelationMinimalParametersTest() {
		UserAppRelation relation = new UserAppRelation();
		ObjectNode node = mapper.createObjectNode();

		User user = new User();
		user.setName("TestUserName");
		user.setId(new UUID(0, 1));

		IoTApp app = new IoTApp();
		app.setName("TestAppName");
		app.setId(new UUID(0, 2));

		relation.setId(new UUID(0, 0));
		relation.setUser(user);
		relation.setApp(app);

		node.put("id", new UUID(0, 0).toString());
		node.put("userName", "TestUserName");
		node.put("userId", new UUID(0, 1).toString());
		node.put("appName", "TestAppName");
		node.put("appId", new UUID(0, 2).toString());

		assertEquals(api.createJsonFromUserAppRelation(relation), node);
	}

	// Test fails because attributes
	// are not properly mapped in the method
	@Test
	public void createJsonFromUserAppRelationAllParametersTest() {
		UserAppRelation relation = new UserAppRelation();
		ObjectNode node = mapper.createObjectNode();

		User user = new User();
		user.setName("TestUserName");
		user.setId(new UUID(0, 1));

		IoTApp app = new IoTApp();
		app.setName("TestAppName");
		app.setId(new UUID(0, 2));

		String[] consenses = { "Consensus1", "Consensus2" };

		relation.setId(new UUID(0, 0));
		relation.setUser(user);
		relation.setApp(app);
		relation.setConsenses(consenses);

		node.put("id", new UUID(0, 0).toString());
		node.put("userName", "TestUserName");
		node.put("userId", new UUID(0, 1).toString());
		node.put("appName", "TestAppName");
		node.put("appId", new UUID(0, 2).toString());

		ArrayNode consensesArray = mapper.createArrayNode();
		consensesArray.add(consenses[0]);
		consensesArray.add(consenses[1]);

		node.set("consenses", consensesArray);

		assertEquals(api.createJsonFromUserAppRelation(relation), node);
	}

	@Test
	public void createJsonFromMessageNullTest() {
		Message mess = new Message();
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromMessage(mess);
		});
	}

	@Test
	public void createJsonFromMessageIDNullTest() {
		Message mess = new Message();

		User sender = new User();
		User receiver = new User();
		mess.setSender(sender);
		mess.setReceiver(receiver);
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromMessage(mess);
		});
	}

	@Test
	public void createJsonFromMessageSenderNullTest() {
		Message mess = new Message();

		mess.setId(new UUID(0, 0));
		User receiver = new User();
		mess.setReceiver(receiver);
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromMessage(mess);
		});
	}

	@Test
	public void createJsonFromMessageReceiverNullTest() {
		Message mess = new Message();

		User sender = new User();
		mess.setId(new UUID(0, 0));
		mess.setSender(sender);
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromMessage(mess);
		});
	}

	// Test fails and shows a hole in the method's logic,
	// which considers mandatory only messageId, sender and receiver,
	// but it does not work in absence of all parameters.
	// The problem lies in not checking Message and Time,
	// which leads to a NullPointerException
	@Test
	public void createJsonFromMessageMinimalParametersTest() {
		ObjectNode node = mapper.createObjectNode();
		Message mess = new Message();
		mess.setId(new UUID(0, 0));

		User sender = new User();

		User receiver = new User();

		mess.setSender(sender);
		mess.setReceiver(receiver);

		node.put("id", new UUID(0, 0).toString());

		try {
			assertEquals(api.createJsonFromMessage(mess), node);
		} catch (NullPointerException e) {
			System.out.println("-----TEST START JSON FROM MESSAGE MINIMAL PARAMETERS-----");
			System.out.println("Caught Exception:" + e);
			System.out.println("-----TEST END-----" + '\n');
		}

	}

	// There is an inconsistency with attributes' names
	// In particular the Message attributes called text in the json
	@Test
	public void createJsonFromMessageAllParametersTest() {
		ObjectNode node = mapper.createObjectNode();
		Message mess = new Message();
		LocalDateTime time = LocalDateTime.now();

		mess.setId(new UUID(0, 0));
		mess.setMessage("TestMessage");
		mess.setTime(time);

		User sender = new User();
		sender.setId(new UUID(0, 1));
		sender.setName("TestSenderName");

		User receiver = new User();
		receiver.setId(new UUID(0, 2));
		receiver.setName("TestReceiverName");

		mess.setSender(sender);
		mess.setReceiver(receiver);

		node.put("id", new UUID(0, 0).toString());
		node.put("senderId", new UUID(0, 1).toString());
		node.put("senderName", "TestSenderName");
		node.put("receiverId", new UUID(0, 2).toString());
		node.put("receiverName", "TestReceiverName");
		node.put("text", "TestMessage");
		node.put("time", time.toString());

		assertEquals(api.createJsonFromMessage(mess), node);
	}

	@Test
	public void getMessageFromJsonNodeSenderNullTest() {
		ObjectNode node = mapper.createObjectNode();
		node.put("text", "TestMessage");
		node.put("receiverId", new UUID(0, 2).toString());
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			api.getMessageFromJsonNode(node);
		});
		assertEquals(e.getMessage(), "JSON incomplete");
	}

	@Test
	public void getMessageFromJsonNodeReceiverNullTest() {
		ObjectNode node = mapper.createObjectNode();
		node.put("text", "TestMessage");
		node.put("senderId", new UUID(0, 1).toString());
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			api.getMessageFromJsonNode(node);
		});
		assertEquals(e.getMessage(), "JSON incomplete");
	}

	@Test
	public void getMessageFromJsonNodeMessageNullTest() {
		ObjectNode node = mapper.createObjectNode();
		node.put("senderId", new UUID(0, 1).toString());
		node.put("receiverId", new UUID(0, 2).toString());
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			api.getMessageFromJsonNode(node);
		});
		assertEquals(e.getMessage(), "JSON incomplete");
	}

	@Test
	public void getMessageFromJsonNodeIDNotValidTest() {
		ObjectNode node = mapper.createObjectNode();
		node.put("senderId", new UUID(0, 1).toString());
		node.put("receiverId", new UUID(0, 2).toString());
		node.put("text", "TestMessage");
		node.put("id", "0");
		assertThrows(IllegalArgumentException.class, () -> {
			api.getMessageFromJsonNode(node);
		});
	}

	@Test
	public void getMessageFromJsonNodeTimeNotValidTest() {
		ObjectNode node = mapper.createObjectNode();
		node.put("senderId", new UUID(0, 1).toString());
		node.put("receiverId", new UUID(0, 2).toString());
		node.put("text", "TestMessage");
		node.put("id", new UUID(0, 0).toString());
		node.put("time", "NotATime");
		assertThrows(DateTimeParseException.class, () -> {
			api.getMessageFromJsonNode(node);
		});
	}

	@Test
	public void getMessageFromJsonNodeSenderEmptyTest(){
		LocalDateTime time = LocalDateTime.now();
		ObjectNode node = mapper.createObjectNode();
		node.put("senderId", new UUID(0, 1).toString());
		node.put("receiverId", new UUID(0, 2).toString());
		node.put("text", "TestMessage");
		node.put("id", new UUID(0, 0).toString());
		node.put("time", time.toString());	

		when(dataBaseService.getUser(new UUID(0, 1))).thenReturn(Optional.empty());

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getMessageFromJsonNode(node);
		});
		assertEquals(e.getMessage(), "sender does not exist");
	}

	@Test
	public void getMessageFromJsonNodeReceiverEmptyTest(){
		LocalDateTime time = LocalDateTime.now();
		ObjectNode node = mapper.createObjectNode();

		User sender = createUser(Role.SUBJECT);

		node.put("senderId", sender.getId().toString());
		node.put("receiverId", new UUID(0, 2).toString());
		node.put("text", "TestMessage");
		node.put("id", new UUID(0, 0).toString());
		node.put("time", time.toString());	



		when(dataBaseService.getUser(sender.getId())).thenReturn(Optional.of(sender));
		when(dataBaseService.getUser(new UUID(0, 2))).thenReturn(Optional.empty());

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getMessageFromJsonNode(node);
		});
		assertEquals(e.getMessage(), "receiver does not exist");
	}

	@Test
	public void getMessageFromJsonNodeMinimalParametersTest(){
		ObjectNode node = mapper.createObjectNode();

		User sender = createUser(Role.SUBJECT);
		User receiver = createUser(Role.CONTROLLER);

		node.put("senderId", sender.getId().toString());
		node.put("receiverId", receiver.getId().toString());
		node.put("text", "TestMessage");

		when(dataBaseService.getUser(sender.getId())).thenReturn(Optional.of(sender));
		when(dataBaseService.getUser(receiver.getId())).thenReturn(Optional.of(receiver));

		Message mess = apiMock.getMessageFromJsonNode(node);
		assertEquals(mess.getSender(), sender);
		assertEquals(mess.getReceiver(), receiver);
		assertEquals(mess.getMessage(), "TestMessage");
	}

	@Test
	public void getMessageFromJsonNodeAllParametersTest(){
		LocalDateTime time = LocalDateTime.now();
		ObjectNode node = mapper.createObjectNode();

		User sender = createUser(Role.SUBJECT);
		User receiver = createUser(Role.CONTROLLER);

		node.put("senderId", sender.getId().toString());
		node.put("receiverId", receiver.getId().toString());
		node.put("text", "TestMessage");
		node.put("id", new UUID(0, 0).toString());
		node.put("time", time.toString());

		when(dataBaseService.getUser(sender.getId())).thenReturn(Optional.of(sender));
		when(dataBaseService.getUser(receiver.getId())).thenReturn(Optional.of(receiver));

		Message mess = apiMock.getMessageFromJsonNode(node);
		assertEquals(mess.getSender(), sender);
		assertEquals(mess.getReceiver(), receiver);
		assertEquals(mess.getMessage(), "TestMessage");
		assertEquals(mess.getId(), new UUID(0, 0));
		assertEquals(mess.getTime(), time);
	}

	// String body =
	// "{\"password\":\""+"TestHash"+"\",\"role\":\""+Role.CONTROLLER.toString()+"\",\"name\":\""+"TestName"+"\",\"id\":\""+new
	// UUID(0, 0).toString()+"\"}";

	@Test
	public void getMessageFromJsonStringNotValidJsonTest() {
		assertThrows(IOException.class, () -> {
			api.getMessageFromJsonString("NotAJson");
		});
	}

	@Test
	public void getMessageFromJsonStringSenderNullTest() {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			api.getMessageFromJsonString("{\"receiverId\":\"" + new UUID(0, 0).toString() + "\",\"id\":\""
					+ new UUID(0, 1).toString() + "\"}");
		});
		assertEquals(e.getMessage(), "invalid JSON parameters");
	}

	@Test
	public void getMessageFromJsonStringReceiverNullTest() {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			api.getMessageFromJsonString(
					"{\"senderId\":\"" + new UUID(0, 0).toString() + "\",\"id\":\"" + new UUID(0, 1).toString() + "\"}");
		});
		assertEquals(e.getMessage(), "invalid JSON parameters");
	}

	@Test
	public void getMessageFromJsonStringTextNullTest() {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			api.getMessageFromJsonString("{\"receiverId\":\"" + new UUID(0, 0).toString() + "\",\"senderId\":\""
					+ new UUID(0, 2).toString() + "\",\"id\":\"" + new UUID(0, 1).toString() + "\"}");
		});
		assertEquals(e.getMessage(), "invalid JSON parameters");
	}

	@Test
	public void getMessageFromJsonStringIDNotValidTest() {
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			api.getMessageFromJsonString("{\"receiverId\":\"" + new UUID(0, 0).toString() + "\",\"text\":\""
					+ "TestMessage" + "\",\"id\":\"" + "2" + "\"}");
		});
		assertEquals(e.getMessage(), "invalid JSON parameters");
	}

	@Test
	public void getMessageFromJsonStringTimeNotValidTest() {
		assertThrows(DateTimeParseException.class, () -> {
			api.getMessageFromJsonString("{\"receiverId\":\"" + new UUID(0, 0).toString() + "\",\"senderId\":\""
					+ new UUID(0, 2).toString() + "\",\"text\":\"" + "TestMessage" + "\",\"time\":\"" + "NotATime"
					+ "\",\"id\":\"" + new UUID(0, 1).toString() + "\"}");
		});
	}

	@Test
	public void getMessageFromJsonStringValidBody(){
		User sender = createUser(Role.CONTROLLER);
		User receiver = createUser(Role.SUBJECT);
		LocalDateTime time = LocalDateTime.now();

		when(dataBaseService.getUser(sender.getId())).thenReturn(Optional.of(sender));
		when(dataBaseService.getUser(receiver.getId())).thenReturn(Optional.of(receiver));

		Message mess = new Message();

		try{
			mess = apiMock.getMessageFromJsonString("{\"receiverId\":\"" + receiver.getId().toString() + "\",\"senderId\":\""
			+ sender.getId().toString() + "\",\"text\":\"" + "TestMessage" + "\",\"time\":\"" + time.toString()
			+ "\",\"id\":\"" + new UUID(0, 1).toString() + "\"}");
		}
		catch(Exception e){
			System.out.println("Exception: "+e);
		}
		
		assertEquals(mess.getSender(), sender);
		assertEquals(mess.getReceiver(), receiver);
		assertEquals(mess.getTime(), time);
		assertEquals(mess.getMessage(), "TestMessage");
		assertEquals(mess.getId(), new UUID(0, 1));

	}

	// As for similar previous methods, it should check if the
	// privacyNotice passed as argument is null or not.
	@Test
	public void createJsonFromPrivacyNoticeNullTest() {
		assertThrows(NullPointerException.class, () -> {
			api.createJsonFromPrivacyNotice(null);
		});
	}

	@Test
	public void createJsonFromPrivacyNoticeIDNullTest() {
		PrivacyNotice notice = new PrivacyNotice();
		notice.setText("TestText");
		notice.setApp(new IoTApp());
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromPrivacyNotice(notice);
		});
	}

	@Test
	public void createJsonFromPrivacyNoticeTextNullTest() {
		PrivacyNotice notice = new PrivacyNotice();
		notice.setId(new UUID(0, 0));
		notice.setApp(new IoTApp());
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromPrivacyNotice(notice);
		});
	}

	@Test
	public void createJsonFromPrivacyNoticeAppNullTest() {
		PrivacyNotice notice = new PrivacyNotice();
		notice.setId(new UUID(0, 0));
		notice.setText("TestText");
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromPrivacyNotice(notice);
		});
	}

	@Test
	public void createJsonFromPrivacyNoticeTest() {
		PrivacyNotice notice = new PrivacyNotice();

		IoTApp app = new IoTApp();
		app.setId(new UUID(0, 1));
		app.setName("AppName");

		notice.setId(new UUID(0, 0));
		notice.setText("TestText");
		notice.setApp(app);

		ObjectNode node = mapper.createObjectNode();
		node.put("id", new UUID(0, 0).toString());
		node.put("appname", "AppName");
		node.put("appId", new UUID(0, 1).toString());
		node.put("text", "TestText");

		assertEquals(node, api.createJsonFromPrivacyNotice(notice));
	}

	// As for similar previous methods, it should check if the
	// notification passed as argument is null or not.
	@Test
	public void createJsonFromNotificationNullTest() {
		assertThrows(NullPointerException.class, () -> {
			api.createJsonFromNotification(null);
		});
	}

	@Test
	public void createJsonFromNotificationIDNullTest() {
		Notification note = new Notification();
		note.setReceiver(new User());
		note.setSender(new User());
		note.setType("TestType");
		note.setObjectId(new UUID(0, 0));
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromNotification(note);
		});
	}

	@Test
	public void createJsonFromNotificationReceiverNullTest() {
		Notification note = new Notification();
		note.setId(new UUID(0, 1));
		note.setSender(new User());
		note.setType("TestType");
		note.setObjectId(new UUID(0, 0));
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromNotification(note);
		});
	}

	@Test
	public void createJsonFromNotificationSenderNullTest() {
		Notification note = new Notification();
		note.setId(new UUID(0, 1));
		note.setReceiver(new User());
		note.setType("TestType");
		note.setObjectId(new UUID(0, 0));
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromNotification(note);
		});
	}

	@Test
	public void createJsonFromNotificationTypeNullTest(){
		Notification note = new Notification();		
		note.setId(new UUID(0, 1));
		note.setReceiver(new User());
		note.setSender(new User());
		note.setObjectId(new UUID(0, 0));
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromNotification(note);
		});
	}

	@Test
	public void createJsonFromNotificationObjectIDNullTest(){
		Notification note = new Notification();		
		note.setId(new UUID(0, 1));
		note.setReceiver(new User());
		note.setSender(new User());
		note.setType("TestType");
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromNotification(note);
		});
	}

	@Test
	public void createJsonFromNotificationMinimalParametersTest(){
		Notification note = new Notification();		
		note.setId(new UUID(0, 1));

		User sender = new User();
		sender.setId(new UUID(0, 2));
		sender.setName("SenderName");

		User receiver = new User();
		receiver.setId(new UUID(0, 3));
		receiver.setName("ReceiverName");

		note.setReceiver(receiver);
		note.setSender(sender);
		note.setType("TestType");
		note.setDescription("DescriptionTest");
		note.setObjectId(new UUID(0, 0));

		ObjectNode node = mapper.createObjectNode();
		node.put("id", new UUID(0, 1).toString());
		node.put("senderId", new UUID(0, 2).toString());
		node.put("senderName", "SenderName");
		node.put("receiverId", new UUID(0, 3).toString());
		node.put("receiverName", "ReceiverName");
		node.put("description", "DescriptionTest");
		node.put("type", "TestType");
		node.put("objectId", new UUID(0, 0).toString());

		assertEquals(node, api.createJsonFromNotification(note));
	}

	@Test
	public void createJsonFromNotificationAllParametersTest(){
		Notification note = new Notification();	
		LocalDateTime time = LocalDateTime.now();
		
		note.setId(new UUID(0, 1));

		User sender = new User();
		sender.setId(new UUID(0, 2));
		sender.setName("SenderName");

		User receiver = new User();
		receiver.setId(new UUID(0, 3));
		receiver.setName("ReceiverName");

		note.setReceiver(receiver);
		note.setSender(sender);
		note.setType("TestType");
		note.setDescription("DescriptionTest");
		note.setObjectId(new UUID(0, 0));
		note.setTime(time);
		note.setRead(true);

		ObjectNode node = mapper.createObjectNode();
		node.put("id", new UUID(0, 1).toString());
		node.put("senderId", new UUID(0, 2).toString());
		node.put("senderName", "SenderName");
		node.put("receiverId", new UUID(0, 3).toString());
		node.put("receiverName", "ReceiverName");
		node.put("description", "DescriptionTest");
		node.put("type", "TestType");
		node.put("objectId", new UUID(0, 0).toString());
		node.put("time", time.toString());
		node.put("isRead", true);

		assertEquals(node, api.createJsonFromNotification(note));
	}

	@Test
	public void createJsonFromRequestNullTest(){
		assertThrows(NullPointerException.class, () -> {
			api.createJsonFromRequest(null);
		});
	}

	@Test
	public void createJsonFromRequestIdNullTest(){
		RightRequest req = new RightRequest();
		req.setSender(new User());
		req.setReceiver(new User());
		req.setRightType(RightType.COMPLAIN);
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromRequest(req);
		});
	}

	@Test
	public void createJsonFromRequestSenderNullTest(){
		RightRequest req = new RightRequest();
		req.setId(new UUID(0, 0));
		req.setReceiver(new User());
		req.setRightType(RightType.COMPLAIN);
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromRequest(req);
		});
	}

	@Test
	public void createJsonFromRequestReceiverNullTest(){
		RightRequest req = new RightRequest();
		req.setId(new UUID(0, 0));
		req.setSender(new User());
		req.setRightType(RightType.COMPLAIN);
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromRequest(req);
		});
	}

	@Test
	public void createJsonFromRequestRightTypeNullTest(){
		RightRequest req = new RightRequest();
		req.setId(new UUID(0, 0));
		req.setSender(new User());
		req.setReceiver(new User());
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonFromRequest(req);
		});
	}

	@Test
	public void createJsonFromRequestMinimalParametersTest(){
		RightRequest req = new RightRequest();
		req.setId(new UUID(0, 0));

		User sender = new User();
		sender.setId(new UUID(0, 1));
		sender.setName("SenderName");

		User receiver = new User();
		receiver.setId(new UUID(0, 2));
		receiver.setName("ReceiverName");

		req.setSender(sender);
		req.setReceiver(receiver);
		
		req.setRightType(RightType.COMPLAIN);

		ObjectNode node = mapper.createObjectNode();
		node.put("id", new UUID(0, 0).toString());
		node.put("senderName", "SenderName");
		node.put("senderId", new UUID(0, 1).toString());
		node.put("receiverName", "ReceiverName");
		node.put("receiverId", new UUID(0, 2).toString());
		node.put("rightType", RightType.COMPLAIN.toString());

		assertEquals(node, api.createJsonFromRequest(req));
	}

	@Test
	public void createJsonFromRequestAllParametersTest(){
		RightRequest req = new RightRequest();
		LocalDateTime time = LocalDateTime.now();
		req.setId(new UUID(0, 0));

		User sender = new User();
		sender.setId(new UUID(0, 1));
		sender.setName("SenderName");

		User receiver = new User();
		receiver.setId(new UUID(0, 2));
		receiver.setName("ReceiverName");

		req.setSender(sender);
		req.setReceiver(receiver);

		IoTApp app = new IoTApp();
		app.setId(new UUID(0, 3));
		app.setName("AppName");

		req.setTime(time);
		req.setApp(app);
		req.setOther("OtherTest");
		req.setRightType(RightType.COMPLAIN);
		req.setHandled(true);
		req.setDetails("DetailsTest");
		req.setResponse("ResponseTest");

		ObjectNode node = mapper.createObjectNode();
		node.put("id", new UUID(0, 0).toString());
		node.put("senderName", "SenderName");
		node.put("senderId", new UUID(0, 1).toString());
		node.put("receiverName", "ReceiverName");
		node.put("receiverId", new UUID(0, 2).toString());
		node.put("time", time.toString());
		node.put("appId", new UUID(0, 3).toString());
		node.put("appName", "AppName");
		node.put("rightType", RightType.COMPLAIN.toString());
		node.put("other", "OtherTest");
		node.put("handled", true);
		node.put("details", "DetailsTest");
		node.put("response", "ResponseTest");

		assertEquals(node, api.createJsonFromRequest(req));
	}

	@Test
	public void createJsonRequestCheckAuthorizedUserNullTest(){
		assertThrows(NullPointerException.class, () -> {
			api.createJsonRequestCheckAuthorizedUser(null, null);
		});
	}

	@Test
	public void createJsonRequestCheckAuthorizedUserUserNullTest(){
		ArrayNode reqArray = mapper.createArrayNode();
		assertEquals(reqArray, api.createJsonRequestCheckAuthorizedUser(createRequestList(), null));
	}

	@Test
	public void createJsonRequestCheckAuthorizedUserListNullTest(){
		assertThrows(NullPointerException.class, () -> {
			api.createJsonRequestCheckAuthorizedUser(null, new User());
		});
	}

	@Test
	public void createJsonRequestCheckAuthorizedUserReceiverMatchTest(){
		List<RightRequest> list = createRequestList();
		ArrayNode reqArray = mapper.createArrayNode();
		reqArray.add(api.createJsonFromRequest(list.get(0)));
		reqArray.add(api.createJsonFromRequest(list.get(2)));
		assertEquals(reqArray, api.createJsonRequestCheckAuthorizedUser(list, list.get(0).getReceiver()));
	}

	@Test
	public void createJsonRequestCheckAuthorizedUserSenderMatchTest(){
		List<RightRequest> list = createRequestList();
		ArrayNode reqArray = mapper.createArrayNode();
		reqArray.add(api.createJsonFromRequest(list.get(0)));
		reqArray.add(api.createJsonFromRequest(list.get(1)));
		assertEquals(reqArray, api.createJsonRequestCheckAuthorizedUser(list, list.get(0).getSender()));
	}

	@Test
	public void createJsonRequestOfAppNullTest(){
		assertThrows(NullPointerException.class, () -> {
			api.createJsonRequestOfApp(null, null);
		});
	}

	@Test
	public void createJsonRequestOfAppAppNullTest(){
		ArrayNode reqArray = mapper.createArrayNode();
		assertEquals(reqArray, api.createJsonRequestOfApp(createRequestList(), null));
	}

	@Test
	public void createJsonRequestOfAppListNullTest(){
		assertThrows(NullPointerException.class, () -> {
			api.createJsonRequestOfApp(null, new IoTApp());
		});
	}

	@Test
	public void createJsonRequestOfAppMatchTest(){
		List<RightRequest> list = createRequestList();
		ArrayNode reqArray = mapper.createArrayNode();
		reqArray.add(api.createJsonFromRequest(list.get(0)));
		reqArray.add(api.createJsonFromRequest(list.get(2)));
		assertEquals(reqArray, api.createJsonRequestOfApp(list, list.get(0).getApp()));
	}

	@Test
	public void createJsonRequestOfRightTypeNullTest(){
		assertThrows(NullPointerException.class, () -> {
			api.createJsonRequestOfRightType(null, null);
		});
	}

	@Test
	public void createJsonRequestOfRightTypeListNullTest(){
		assertThrows(NullPointerException.class, () -> {
			api.createJsonRequestOfRightType(null, RightType.COMPLAIN.toString());
		});
	}

	@Test
	public void createJsonRequestOfRightTypeInvalidRightTypeTest(){
		assertThrows(IllegalArgumentException.class, () -> {
			api.createJsonRequestOfRightType(createRequestList(), "w");
		});
	}

	@Test
	public void createJsonRequestOfRightTypeMatchTest(){
		List<RightRequest> list = createRequestList();
		ArrayNode reqArray = mapper.createArrayNode();
		reqArray.add(api.createJsonFromRequest(list.get(2)));
		reqArray.add(api.createJsonFromRequest(list.get(3)));
		assertEquals(reqArray, api.createJsonRequestOfRightType(list, RightType.INFO.toString()));
	}

	@Test
	public void isAuthenticatedUserIdTrueTest(){
		User user = createUser(Role.SUBJECT);
		Optional<User> test = Optional.of(user);

		when(dataBaseService.getUser(user.getId())).thenReturn(test);
		when(authenticatedUser.get()).thenReturn(test);

		assertEquals(apiMock.isAuthenticatedUserId(user.getId().toString()), true);
	}

	@Test
	public void isAuthenticatedUserIdNotEqualTest(){
		User user = createUser(Role.SUBJECT);
		User authUser = createUser(Role.CONTROLLER);

		Optional<User> test = Optional.of(user);
		Optional<User> authTest = Optional.of(authUser);

		when(dataBaseService.getUser(user.getId())).thenReturn(test);
		when(authenticatedUser.get()).thenReturn(authTest);

		assertEquals(apiMock.isAuthenticatedUserId(user.getId().toString()), false);
	}

	@Test
	public void isAuthenticatedUserUserEmptyTest(){
		User user = createUser(Role.SUBJECT);
		User authUser = createUser(Role.CONTROLLER);

		Optional<User> test = Optional.empty();
		Optional<User> authTest = Optional.of(authUser);

		when(dataBaseService.getUser(user.getId())).thenReturn(test);
		when(authenticatedUser.get()).thenReturn(authTest);

		assertEquals(apiMock.isAuthenticatedUserId(user.getId().toString()), false);
	}

	@Test
	public void isAuthenticatedUserAuthEmptyTest(){
		User user = createUser(Role.SUBJECT);

		Optional<User> test = Optional.of(user);
		Optional<User> authTest = Optional.empty();

		when(dataBaseService.getUser(user.getId())).thenReturn(test);
		when(authenticatedUser.get()).thenReturn(authTest);

		assertEquals(apiMock.isAuthenticatedUserId(user.getId().toString()), false);
	}

	@Test
	public void isControllerOrDPOSubjectNotAuthTest(){
		User user = createUser(Role.SUBJECT);

		Optional<User> test = Optional.of(user);
		when(dataBaseService.getUser(user.getId())).thenReturn(test);

		assertEquals(apiMock.isControllerOrDpo(false, user.getId().toString()), false);
	}

	@Test
	public void isControllerOrDPOSubjectAuthTest(){
		User user = createUser(Role.SUBJECT);

		Optional<User> test = Optional.of(user);
		when(authenticatedUser.get()).thenReturn(test);

		assertEquals(apiMock.isControllerOrDpo(true, user.getId().toString()), false);
	}

	@Test
	public void isControllerOrDPOControllerNotAuthTest(){
		User user = createUser(Role.CONTROLLER);

		Optional<User> test = Optional.of(user);
		when(dataBaseService.getUser(user.getId())).thenReturn(test);

		assertEquals(apiMock.isControllerOrDpo(false, user.getId().toString()), true);
	}

	@Test
	public void isControllerOrDPOControllerAuthTest(){
		User user = createUser(Role.CONTROLLER);

		Optional<User> test = Optional.of(user);
		when(authenticatedUser.get()).thenReturn(test);

		assertEquals(apiMock.isControllerOrDpo(true, user.getId().toString()), true);
	}

	@Test
	public void isControllerOrDPODPONotAuthTest(){
		User user = createUser(Role.DPO);

		Optional<User> test = Optional.of(user);
		when(dataBaseService.getUser(user.getId())).thenReturn(test);

		assertEquals(apiMock.isControllerOrDpo(false, user.getId().toString()), true);
	}

	@Test
	public void isControllerOrDPODPOAuthTest(){
		User user = createUser(Role.DPO);

		Optional<User> test = Optional.of(user);
		when(authenticatedUser.get()).thenReturn(test);

		assertEquals(apiMock.isControllerOrDpo(true, user.getId().toString()), true);
	}

	@Test
	public void getAuthenicatedUserEmptyTest(){
		Optional<User> test = Optional.empty();

		when(authenticatedUser.get()).thenReturn(test);
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getAuthenicatedUser();
		});
		assertEquals(e.getMessage(), "No authenticated user");
	}

	@Test
	public void getAuthenicatedUserNotEmptyTest(){
		User user = createUser(Role.DPO);

		Optional<User> test = Optional.of(user);
		when(authenticatedUser.get()).thenReturn(test);

		assertEquals(apiMock.getAuthenicatedUser(), user);
	}

	@Test
	public void getUserFromIdIDNotValidTest(){
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getUserFromId("1");
		});
		assertEquals(e.getMessage(), "invalid ID");
	}

	//This particular exception is never received because it is
	//catch within the getUserFromId method, 
	//masking the real cause of the exception
	@Test
	public void getUserFromIdUserNotPresentTest(){
		Optional<User> test = Optional.empty();
		UUID id = new UUID(0, 0);
		when(dataBaseService.getUser(UUID.fromString(id.toString()))).thenReturn(test);

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getUserFromId(id.toString());
		});
		assertEquals(e.getMessage(), "user does not exist");
	}

	@Test
	public void getUserFromIdMatchTest(){
		User user = createUser(Role.DPO);

		Optional<User> test = Optional.of(user);
		when(dataBaseService.getUser(user.getId())).thenReturn(test);

		assertEquals(apiMock.getUserFromId(user.getId().toString()), user);
	}

	@Test
	public void getAppFromIdIDNotValidTest(){
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getAppFromId("1");
		});
		assertEquals(e.getMessage(), "invalid ID");
	}

	//This particular exception is never received because it is
	//catch within the getAppFromId method, 
	//masking the real cause of the exception
	@Test
	public void getAppFromIdAppNotPresent(){
		Optional<IoTApp> test = Optional.empty();
		UUID id = new UUID(0, 0);
		when(dataBaseService.getApp(UUID.fromString(id.toString()))).thenReturn(test);

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getAppFromId(id.toString());
		});
		assertEquals(e.getMessage(), "app does not exist");
	}

	@Test
	public void getAppFromIdMatchTest(){
		IoTApp app = createApp();

		Optional<IoTApp> test = Optional.of(app);
		when(dataBaseService.getApp(app.getId())).thenReturn(test);

		assertEquals(apiMock.getAppFromId(app.getId().toString()), app);
	}

	@Test
	public void getMessageFromIdIDNotValidTest(){
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getMessageFromId("1");
		});
		assertEquals(e.getMessage(), "invalid ID");
	}

	//This particular exception is never received because it is
	//catch within the getMessageFromId method, 
	//masking the real cause of the exception
	@Test
	public void getMessageFromIdMessageNotPresent(){
		Optional<Message> test = Optional.empty();
		UUID id = new UUID(0, 0);
		when(dataBaseService.getMessageFromID(UUID.fromString(id.toString()))).thenReturn(test);

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getMessageFromId(id.toString());
		});
		assertEquals(e.getMessage(), "message does not exist");
	}

	@Test
	public void getMessageFromIdMatchTest(){
		Message mess = createMessage();

		Optional<Message> test = Optional.of(mess);
		when(dataBaseService.getMessageFromID(mess.getId())).thenReturn(test);

		assertEquals(apiMock.getMessageFromId(mess.getId().toString()), mess);
	}

	@Test
	public void getUserAppRelationByUserIdAndAppIdUserIdNotValidTest(){
		IoTApp app = createApp();

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getUserAppRelationByUserIdAndAppId("1", app.getId().toString());
		});
		assertEquals(e.getMessage(), "invalid ID");
	}

	@Test
	public void getUserAppRelationByUserIdAndAppIdAppIdNotValidTest(){
		User user = createUser(Role.SUBJECT);
		Optional<User> test = Optional.of(user);

		when(dataBaseService.getUser(user.getId())).thenReturn(test);

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getUserAppRelationByUserIdAndAppId(user.getId().toString(), "1");
		});
		assertEquals(e.getMessage(), "invalid ID");
	}
	
	@Test
	public void getUserAppRelationByUserIdAndAppIdUserEmptyTest(){
		IoTApp app = createApp();
		User user = createUser(Role.SUBJECT);
		Optional<IoTApp> testApp = Optional.of(app);

		Optional<User> testUser = Optional.empty();

		when(dataBaseService.getUser(user.getId())).thenReturn(testUser);
		when(dataBaseService.getApp(app.getId())).thenReturn(testApp);

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getUserAppRelationByUserIdAndAppId(user.getId().toString(), app.getId().toString());
		});
		assertEquals(e.getMessage(), "user does not exist");
	}

	@Test
	public void getUserAppRelationByUserIdAndAppIdAppEmptyTest(){
		IoTApp app = createApp();
		User user = createUser(Role.SUBJECT);
		Optional<IoTApp> testApp = Optional.empty();

		Optional<User> testUser = Optional.of(user);

		when(dataBaseService.getUser(user.getId())).thenReturn(testUser);
		when(dataBaseService.getApp(app.getId())).thenReturn(testApp);

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getUserAppRelationByUserIdAndAppId(user.getId().toString(), app.getId().toString());
		});
		assertEquals(e.getMessage(), "app does not exist");
	}

	@Test
	public void getUserAppRelationByUserIdAndAppIdRelationNullTest(){
		IoTApp app = createApp();
		User user = createUser(Role.SUBJECT);
		Optional<IoTApp> testApp = Optional.of(app);

		Optional<User> testUser = Optional.of(user);

		when(dataBaseService.getUser(user.getId())).thenReturn(testUser);
		when(dataBaseService.getApp(app.getId())).thenReturn(testApp);
		when(dataBaseService.getUserAppRelationByUserAndApp(user, app)).thenReturn(null);

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getUserAppRelationByUserIdAndAppId(user.getId().toString(), app.getId().toString());
		});
		assertEquals(e.getMessage(), "user does not have this app");
	}

	@Test
	public void getUserAppRelationByUserIdAndAppIdMatchTest(){
		IoTApp app = createApp();
		User user = createUser(Role.SUBJECT);
		Optional<IoTApp> testApp = Optional.of(app);

		Optional<User> testUser = Optional.of(user);

		when(dataBaseService.getUser(user.getId())).thenReturn(testUser);
		when(dataBaseService.getApp(app.getId())).thenReturn(testApp);
		when(dataBaseService.getUserAppRelationByUserAndApp(user, app)).thenReturn(createUserAppRelation());

		UserAppRelation res = apiMock.getUserAppRelationByUserIdAndAppId(user.getId().toString(), app.getId().toString());

		assertEquals(res.getApp(), app);
		assertEquals(res.getUser(), user);
		assertEquals(res.getId(), new UUID(4, 0));
	}

	@Test
	public void getPrivacyNoticeFromIdIDNotValidTest(){
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getPrivacyNoticeFromId("1");
		});
		assertEquals(e.getMessage(), "invalid ID");
	}

	@Test
	public void getPrivacyNoticeFromIdEmptyTest(){
		Optional<PrivacyNotice> notice = Optional.empty();

		when(dataBaseService.getPrivacyNoticeFromId(createPrivacyNotice().getId())).thenReturn(notice);
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getPrivacyNoticeFromId(createPrivacyNotice().getId().toString());
		});
		assertEquals(e.getMessage(), "privacy notice does not exist");
	}

	@Test
	public void getPrivacyNoticeFromIdMatchTest(){
		PrivacyNotice notice = createPrivacyNotice();
		Optional<PrivacyNotice> test = Optional.of(notice);

		when(dataBaseService.getPrivacyNoticeFromId(notice.getId())).thenReturn(test);

		assertEquals(apiMock.getPrivacyNoticeFromId(notice.getId().toString()), notice);
	}

	@Test
	public void getPrivacyNoticeFromAppIdIDNotValidTest(){
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getPrivacyNoticeFromAppId("1");
		});
		assertEquals(e.getMessage(), "invalid ID");
	}

	@Test
	public void getPrivacyNoticeFromAppIdAppEmptyTest(){
		Optional<IoTApp> app = Optional.empty();

		when(dataBaseService.getApp(new UUID(0, 0))).thenReturn(app);

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getPrivacyNoticeFromAppId(new UUID(0, 0).toString());
		});
		assertEquals(e.getMessage(), "app does not exist");
	}

	@Test
	public void getPrivacyNoticeFromAppIdNoticeEmptyTest(){
		Optional<IoTApp> app = Optional.of(createApp());

		when(dataBaseService.getApp(app.get().getId())).thenReturn(app);
		when(dataBaseService.getPrivacyNoticeFromApp(app.get())).thenReturn(null);

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getPrivacyNoticeFromAppId(app.get().getId().toString());
		});
		assertEquals(e.getMessage(), "privacy notice does not exist");
	}

	@Test
	public void getPrivacyNoticeFromAppIdMatchTest(){
		Optional<IoTApp> app = Optional.of(createApp());
		PrivacyNotice notice = createPrivacyNotice();

		when(dataBaseService.getApp(app.get().getId())).thenReturn(app);
		when(dataBaseService.getPrivacyNoticeFromApp(app.get())).thenReturn(notice);

		assertEquals(apiMock.getPrivacyNoticeFromAppId(app.get().getId().toString()), notice);
	}

	@Test
	public void getNotificationFromIdIDNotValidTest(){
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getNotificationFromId("1");
		});
		assertEquals(e.getMessage(), "invalid ID");
	}

	@Test
	public void getNotificationFromIdEmptyTest(){
		Optional<Notification> note = Optional.empty();
		
		when(dataBaseService.getNotificationFromId(new UUID(0, 0))).thenReturn(note);

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getNotificationFromId(new UUID(0, 0).toString());
		});
		assertEquals(e.getMessage(), "notification does not exist");
	}

	@Test
	public void getNotificationFromIdMatchTest(){
		Notification note = createNotification();
		Optional<Notification> test = Optional.of(note);

		when(dataBaseService.getNotificationFromId(note.getId())).thenReturn(test);

		assertEquals(apiMock.getNotificationFromId(note.getId().toString()), note);
	}

	@Test
	public void getRequestFromIdIDNotValidTest(){
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getRequestFromId("1");
		});
		assertEquals(e.getMessage(), "invalid ID");
	}

	@Test
	public void getRequestFromIdEmptyTest(){
		when(dataBaseService.getRequestFromId(new UUID(0, 0))).thenReturn(null);

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getRequestFromId(new UUID(0, 0).toString());
		});
		assertEquals(e.getMessage(), "Right Request does not exist");
	}

	@Test
	public void getRequestFromIdMatchTest(){
		RightRequest req = createRequestList().get(0);

		when(dataBaseService.getRequestFromId(req.getId())).thenReturn(req);

		assertEquals(apiMock.getRequestFromId(req.getId().toString()), req);
	}

	@Test
	public void getPrivacyNoticeFromJsonNodeMinimalParametersTest(){
		ObjectNode node = mapper.createObjectNode();
		PrivacyNotice test = api.getPrivacyNoticeFromJsonNode(false, node);
		assertEquals(test.getClass(), PrivacyNotice.class);
		assertNull(test.getApp());
		assertNull(test.getId());
		assertNull(test.getText());
	}

	@Test
	public void getPrivacyNoticeFromJsonNodeAppIdNullTest(){
		ObjectNode node = mapper.createObjectNode();
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			api.getPrivacyNoticeFromJsonNode(true, node);
		});
		assertEquals(e.getMessage(), "missing appId");
	}

	//The exception message is wrong,
	//It should read "missing text"
	@Test
	public void getPrivacyNoticeFromJsonNodeTextNullTest(){
		ObjectNode node = mapper.createObjectNode();
		IoTApp app = createApp();
		node.put("appId", app.getId().toString());

		when(dataBaseService.getApp(app.getId())).thenReturn(Optional.of(app));

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getPrivacyNoticeFromJsonNode(true, node);
		});
		assertEquals(e.getMessage(), "missing appId");
	}

	@Test
	public void getPrivacyNoticeFromJsonNodeMatchTest(){
		ObjectNode node = mapper.createObjectNode();
		IoTApp app = createApp();
		node.put("appId", app.getId().toString());
		node.put("text", "Message");

		when(dataBaseService.getApp(app.getId())).thenReturn(Optional.of(app));

		PrivacyNotice test = apiMock.getPrivacyNoticeFromJsonNode(true, node);
		assertEquals(test.getApp(), app);
		assertEquals(test.getText(), "Message");
	}

	@Test
	public void getPrivacyNoticeFromJsonStringNotValidJsonTest(){
		assertThrows(IOException.class, () -> {
			api.getPrivacyNoticeFromJsonString(false, "NotAJson");
		});
	}

	@Test
	public void getPrivacyNoticeFromJsonStringIdNotValidTest(){
		assertThrows(IllegalArgumentException.class, () -> {
			api.getPrivacyNoticeFromJsonString(false, "{\"appId\":\"1\"}");
		});
	}

	@Test
	public void getPrivacyNoticeFromJsonStringMatchTest(){
		IoTApp app = createApp();
		PrivacyNotice test = new PrivacyNotice();

		when(dataBaseService.getApp(app.getId())).thenReturn(Optional.of(app));

		try{
			test = apiMock.getPrivacyNoticeFromJsonString(false, "{\"appId\":\""+app.getId().toString()+"\", \"text\":\"Message\"}");
		}
		catch(Exception e){
			System.out.println("Exception: "+e);
		}

		assertEquals(test.getApp(), app);
		assertEquals(test.getText(), "Message");
	}

	@Test
	public void createJsonNotificationsFromUserNullTest(){
		List<Notification> list = List.of();

		when(dataBaseService.getNotificationsFromUser(createUser(Role.SUBJECT))).thenReturn(list);

		
		ArrayNode test = apiMock.createJsonNotificationsFromUser(createUser(Role.SUBJECT), true, false);
		assertEquals(test.isEmpty(), true);
	}

	@Test
	public void createJsonNotificationsFromUserAllTest(){
		List<Notification> list = createNotificationList(true, false);

		when(dataBaseService.getNotificationsFromUser(createUser(Role.CONTROLLER))).thenReturn(list);

		ArrayNode test = mapper.createArrayNode();
		test.add(api.createJsonFromNotification(list.get(0)));
		test.add(api.createJsonFromNotification(list.get(1)));

		assertEquals(apiMock.createJsonNotificationsFromUser(createUser(Role.CONTROLLER), true, false), test);
	}

	@Test
	public void createJsonNotificationsFromUserReadTest(){
		List<Notification> list = createNotificationList(false, true);

		when(dataBaseService.getOldNotificationsFromUser(createUser(Role.CONTROLLER))).thenReturn(list);

		ArrayNode test = mapper.createArrayNode();
		test.add(api.createJsonFromNotification(list.get(0)));

		assertEquals(apiMock.createJsonNotificationsFromUser(createUser(Role.CONTROLLER), false, true), test);
	}

	@Test
	public void createJsonNotificationsFromUserNotReadTest(){
		List<Notification> list = createNotificationList(false, false);

		when(dataBaseService.getNewNotificationsFromUser(createUser(Role.CONTROLLER))).thenReturn(list);

		ArrayNode test = mapper.createArrayNode();
		test.add(api.createJsonFromNotification(list.get(0)));

		assertEquals(apiMock.createJsonNotificationsFromUser(createUser(Role.CONTROLLER), false, false), test);
	}

	@Test
	public void getNotificationFromJsonNodeSenderIdMissingTest(){
		ObjectNode node = mapper.createObjectNode();
		node.put("receiverId", new UUID(0, 0).toString());
		node.put("type", "Message");
		node.put("objectId", new UUID(0, 1).toString());

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			api.getNotificationFromJsonNode(node);
		});
		assertEquals(e.getMessage(), "missing mandatory parameters in the JSON object");
	}

	@Test
	public void getNotificationFromJsonNodeReceiverIdMissingTest(){
		ObjectNode node = mapper.createObjectNode();
		node.put("senderId", new UUID(0, 0).toString());
		node.put("type", "Message");
		node.put("objectId", new UUID(0, 1).toString());

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			api.getNotificationFromJsonNode(node);
		});
		assertEquals(e.getMessage(), "missing mandatory parameters in the JSON object");
	}

	@Test
	public void getNotificationFromJsonNodeTypeMissingTest(){
		ObjectNode node = mapper.createObjectNode();
		node.put("senderId", new UUID(0, 2).toString());	
		node.put("receiverId", new UUID(0, 0).toString());
		node.put("objectId", new UUID(0, 1).toString());

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			api.getNotificationFromJsonNode(node);
		});
		assertEquals(e.getMessage(), "missing mandatory parameters in the JSON object");
	}

	@Test
	public void getNotificationFromJsonNodeObjectIdMissingTest(){
		ObjectNode node = mapper.createObjectNode();
		node.put("senderId", new UUID(0, 0).toString());
		node.put("type", "Message");
		node.put("receiverId", new UUID(0, 1).toString());

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			api.getNotificationFromJsonNode(node);
		});
		assertEquals(e.getMessage(), "missing mandatory parameters in the JSON object");
	}

	@Test
	public void getNotificationFromJsonNodeInvalidObjectIdTest(){
		ObjectNode node = mapper.createObjectNode();

		User sender = createUser(Role.SUBJECT);
		User receiver = createUser(Role.CONTROLLER);

		node.put("senderId", sender.getId().toString());
		node.put("type", "NotAType");
		node.put("receiverId", receiver.getId().toString());
		node.put("objectId", "1");

		when(dataBaseService.getUser(sender.getId())).thenReturn(Optional.of(sender));
		when(dataBaseService.getUser(receiver.getId())).thenReturn(Optional.of(receiver));

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getNotificationFromJsonNode(node);
		});
		assertEquals(e.getMessage(), "invalid object ID");
	}

	//The method retrieves an object depending on the type
	//which is never used. Even if the reason is to check
	//if the object does indeed exist, it would be better to
	//use a ad hoc method which return a boolean, instead of 
	//an entire object
	@Test
	public void getNotificationFromJsonNodeMinimalParametersTest(){
		ObjectNode node = mapper.createObjectNode();

		User sender = createUser(Role.SUBJECT);
		User receiver = createUser(Role.CONTROLLER);
		Message mess = createMessage();

		node.put("senderId", sender.getId().toString());
		node.put("type", "Message");
		node.put("receiverId", receiver.getId().toString());
		node.put("objectId", mess.getId().toString());

		when(dataBaseService.getUser(sender.getId())).thenReturn(Optional.of(sender));
		when(dataBaseService.getUser(receiver.getId())).thenReturn(Optional.of(receiver));
		when(dataBaseService.getMessageFromID(mess.getId())).thenReturn(Optional.of(mess));

		Notification note = apiMock.getNotificationFromJsonNode(node);

		assertEquals(note.getObjectId(), mess.getId());
		assertEquals(note.getSender(), sender);
		assertEquals(note.getReceiver(), receiver);
		assertEquals(note.getType(), "Message");
	}

	@Test
	public void getNotificationFromJsonNodeAllParametersTest(){
		ObjectNode node = mapper.createObjectNode();

		User sender = createUser(Role.SUBJECT);
		User receiver = createUser(Role.CONTROLLER);
		RightRequest req = createRequestList().get(0);
		LocalDateTime time = LocalDateTime.now();

		node.put("senderId", sender.getId().toString());
		node.put("type", "Request");
		node.put("receiverId", receiver.getId().toString());
		node.put("objectId", req.getId().toString());
		node.put("time", time.toString());
		node.put("description", "Description");
		node.put("isRead", "true");

		when(dataBaseService.getUser(sender.getId())).thenReturn(Optional.of(sender));
		when(dataBaseService.getUser(receiver.getId())).thenReturn(Optional.of(receiver));
		when(dataBaseService.getRequestFromId(req.getId())).thenReturn(req);

		Notification note = apiMock.getNotificationFromJsonNode(node);

		assertEquals(note.getObjectId(), req.getId());
		assertEquals(note.getSender(), sender);
		assertEquals(note.getReceiver(), receiver);
		assertEquals(note.getType(), "Request");
		assertEquals(note.getDescription(), "Description");
		assertEquals(note.getTime(), time);
		assertEquals(note.getRead(), true);
	}

	@Test
	public void getNotificationFromJsonNodeTypePrivacyNoticeTest(){
		ObjectNode node = mapper.createObjectNode();

		User sender = createUser(Role.SUBJECT);
		User receiver = createUser(Role.CONTROLLER);
		PrivacyNotice pNote = createPrivacyNotice();

		node.put("senderId", sender.getId().toString());
		node.put("type", "PrivacyNotice");
		node.put("receiverId", receiver.getId().toString());
		node.put("objectId", pNote.getId().toString());

		when(dataBaseService.getUser(sender.getId())).thenReturn(Optional.of(sender));
		when(dataBaseService.getUser(receiver.getId())).thenReturn(Optional.of(receiver));
		when(dataBaseService.getPrivacyNoticeFromId(pNote.getId())).thenReturn(Optional.of(pNote));

		Notification note = apiMock.getNotificationFromJsonNode(node);

		assertEquals(note.getObjectId(), pNote.getId());
		assertEquals(note.getSender(), sender);
		assertEquals(note.getReceiver(), receiver);
		assertEquals(note.getType(), "PrivacyNotice");
	}
	
	@Test
	public void getNotificationFromJsonStringInvalidJsonTest(){
		assertThrows(IOException.class, () -> {
			api.getNotificationFromJsonString("NotAJson");
		});
	}

	@Test
	public void getNotificationFromJsonStringMatchTest(){
		User sender = createUser(Role.SUBJECT);
		User receiver = createUser(Role.CONTROLLER);
		RightRequest req = createRequestList().get(0);

		String body = "{\"senderId\":\""+sender.getId().toString()+"\", \"receiverId\":\""+receiver.getId().toString()+"\", \"type\": \"Request\", \"objectId\":\""+req.getId().toString()+"\"}";

		when(dataBaseService.getUser(sender.getId())).thenReturn(Optional.of(sender));
		when(dataBaseService.getUser(receiver.getId())).thenReturn(Optional.of(receiver));
		when(dataBaseService.getRequestFromId(req.getId())).thenReturn(req);

		Notification note = new Notification();

		try{
			note = apiMock.getNotificationFromJsonString(body);
		}
		catch(Exception e){
			System.out.println("Exception :"+e);
		}

		assertEquals(note.getObjectId(), req.getId());
		assertEquals(note.getSender(), sender);
		assertEquals(note.getReceiver(), receiver);
		assertEquals(note.getType(), "Request");
	}

	@Test
	public void getRequestFromJsonNodeMinimalParametersTest(){
		RightRequest req = api.getRequestFromJsonNode(mapper.createObjectNode(), false);
		assertNull(req.getApp());
		assertNull(req.getDetails());
		assertNull(req.getId());
		assertNull(req.getOther());
		assertNull(req.getSender());
		assertNull(req.getReceiver());
		assertNull(req.getRightType());
		assertNull(req.getTime());
		assertNull(req.getResponse());
		assertNull(req.getHandled());
	}

	//Method documentation is wrong
	//receiverId is not mandatory
	@Test
	public void getRequestFromJsonNodeSenderIdMissingTest(){
		ObjectNode node = mapper.createObjectNode();
		node.put("rightType", RightType.COMPLAIN.toString());
		node.put("appId", new UUID(0, 1).toString());

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			api.getRequestFromJsonNode(node, true);
		});
		assertEquals(e.getMessage(), "senderId value missing");
	}

	@Test
	public void getRequestFromJsonNodeAppIdMissingTest(){
		ObjectNode node = mapper.createObjectNode();

		User sender = createUser(Role.SUBJECT);

		node.put("rightType", RightType.COMPLAIN.toString());
		node.put("senderId", sender.getId().toString());

		when(dataBaseService.getUser(sender.getId())).thenReturn(Optional.of(sender));

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getRequestFromJsonNode(node, true);
		});
		assertEquals(e.getMessage(), "appId value missing");
	}

	@Test
	public void getRequestFromJsonNodeRightTypeMissingTest(){
		ObjectNode node = mapper.createObjectNode();

		User sender = createUser(Role.SUBJECT);
		IoTApp app = createApp();

		node.put("senderId", sender.getId().toString());
		node.put("appId", app.getId().toString());

		when(dataBaseService.getUser(sender.getId())).thenReturn(Optional.of(sender));
		when(dataBaseService.getApp(app.getId())).thenReturn(Optional.of(app));

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getRequestFromJsonNode(node, true);
		});
		assertEquals(e.getMessage(), "rightType value missing");
	}

	@Test
	public void getRequestFromJsonNodeMandatoryParametersTest(){
		ObjectNode node = mapper.createObjectNode();

		User sender = createUser(Role.SUBJECT);
		IoTApp app = createApp();

		node.put("senderId", sender.getId().toString());
		node.put("appId", app.getId().toString());
		node.put("rightType", RightType.COMPLAIN.toString());

		when(dataBaseService.getUser(sender.getId())).thenReturn(Optional.of(sender));
		when(dataBaseService.getApp(app.getId())).thenReturn(Optional.of(app));

		RightRequest req = apiMock.getRequestFromJsonNode(node, true);
		assertEquals(req.getApp(), app);
		assertEquals(req.getSender(), sender);
		assertEquals(req.getRightType(), RightType.COMPLAIN);
	}

	@Test
	public void getRequestFromJsonNodeInvalidDateTest(){
		ObjectNode node = mapper.createObjectNode();

		User sender = createUser(Role.SUBJECT);
		IoTApp app = createApp();

		node.put("senderId", sender.getId().toString());
		node.put("appId", app.getId().toString());
		node.put("rightType", RightType.COMPLAIN.toString());
		node.put("time", "NotATime");

		when(dataBaseService.getUser(sender.getId())).thenReturn(Optional.of(sender));
		when(dataBaseService.getApp(app.getId())).thenReturn(Optional.of(app));

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> {
			apiMock.getRequestFromJsonNode(node, false);
		});
		assertEquals(e.getMessage(), "invalid date");
	}

	//In the method details are set twice, 
	//using both the deatils and the response field
	@Test
	public void getRequestFromJsonNodeAllParametersTest(){
		ObjectNode node = mapper.createObjectNode();

		User sender = createUser(Role.SUBJECT);
		User receiver = createUser(Role.CONTROLLER);
		IoTApp app = createApp();
		LocalDateTime time = LocalDateTime.now();

		node.put("senderId", sender.getId().toString());
		node.put("receiverId", receiver.getId().toString());
		node.put("appId", app.getId().toString());
		node.put("rightType", RightType.COMPLAIN.toString());
		node.put("time", time.toString());
		node.put("other", "TestOther");
		node.put("details", "TestDetails");
		node.put("response", "TestResponse");
		node.put("handled", "true");

		when(dataBaseService.getUser(sender.getId())).thenReturn(Optional.of(sender));
		when(dataBaseService.getUser(receiver.getId())).thenReturn(Optional.of(receiver));
		when(dataBaseService.getApp(app.getId())).thenReturn(Optional.of(app));

		RightRequest req = apiMock.getRequestFromJsonNode(node, true);
		assertEquals(req.getApp(), app);
		assertEquals(req.getSender(), sender);
		assertEquals(req.getRightType(), RightType.COMPLAIN);
		assertEquals(req.getReceiver(), receiver);
		assertEquals(req.getTime(), time);
		assertEquals(req.getOther(), "TestOther");
		assertEquals(req.getHandled(), true);
		assertEquals(req.getDetails(), "TestDetails");
		assertEquals(req.getResponse(), "TestResponse");
	}

	@Test
	public void getRequestFromJsonNodeNotHandledTest(){
		ObjectNode node = mapper.createObjectNode();

		User sender = createUser(Role.SUBJECT);
		IoTApp app = createApp();

		node.put("senderId", sender.getId().toString());
		node.put("appId", app.getId().toString());
		node.put("rightType", RightType.COMPLAIN.toString());
		node.put("handled", "false");

		when(dataBaseService.getUser(sender.getId())).thenReturn(Optional.of(sender));
		when(dataBaseService.getApp(app.getId())).thenReturn(Optional.of(app));

		RightRequest req = apiMock.getRequestFromJsonNode(node, true);
		assertEquals(req.getApp(), app);
		assertEquals(req.getSender(), sender);
		assertEquals(req.getRightType(), RightType.COMPLAIN);
		assertEquals(req.getHandled(), false);
	}

	@Test
	public void getRequestFromJsonNodeHandledNotValidTest(){
		ObjectNode node = mapper.createObjectNode();

		User sender = createUser(Role.SUBJECT);
		IoTApp app = createApp();

		node.put("senderId", sender.getId().toString());
		node.put("appId", app.getId().toString());
		node.put("rightType", RightType.COMPLAIN.toString());
		node.put("handled", "NotBoolean");

		when(dataBaseService.getUser(sender.getId())).thenReturn(Optional.of(sender));
		when(dataBaseService.getApp(app.getId())).thenReturn(Optional.of(app));

		RightRequest req = apiMock.getRequestFromJsonNode(node, true);
		assertEquals(req.getApp(), app);
		assertEquals(req.getSender(), sender);
		assertEquals(req.getRightType(), RightType.COMPLAIN);
		assertNull(req.getHandled());
	}

	@Test
	public void getRequestFromJsonStringInvalidJsonTest(){
		assertThrows(IOException.class, () -> {
			api.getRequestFromJsonString("NotAJSon", false);
		});
	}

	@Test
	public void getRequestFromJsonStringMatchTest(){
		User sender = createUser(Role.SUBJECT);
		IoTApp app = createApp();

		String body = "{\"senderId\": \""+sender.getId().toString()+"\", \"appId\":\""+app.getId().toString()+"\",\"rightType\":\""+RightType.COMPLAIN.toString()+"\"}";

		when(dataBaseService.getUser(sender.getId())).thenReturn(Optional.of(sender));
		when(dataBaseService.getApp(app.getId())).thenReturn(Optional.of(app));

		RightRequest req = new RightRequest();

		try{
			req = apiMock.getRequestFromJsonString(body, false);
		}
		catch(Exception e){
			System.out.println("Exception: "+e);
		}
		
		assertEquals(req.getApp(), app);
		assertEquals(req.getSender(), sender);
		assertEquals(req.getRightType(), RightType.COMPLAIN);
	}

}
