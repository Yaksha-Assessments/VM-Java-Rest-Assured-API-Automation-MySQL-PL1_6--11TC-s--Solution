package rest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;



public class ApiUtil {
	private static final Set<Integer> usedNumbers = new HashSet<>();
	private static final Random random = new Random();
	private static String BASE_URL;
	Properties prop;

	/**
	 * Retrieves the base URL from the configuration properties file.
	 *
	 * <p>
	 * This method loads the properties from the file located at
	 * <code>{user.dir}/src/main/resources/config.properties</code> and extracts the
	 * value associated with the key <code>base.url</code>. The value is stored in
	 * the static variable <code>BASE_URL</code> and returned.
	 *
	 * @return the base URL string if successfully read from the properties file;
	 *         {@code null} if an I/O error occurs while reading the file.
	 */
	public String getBaseUrl() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			BASE_URL = prop.getProperty("base.url");
			return BASE_URL;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retrieves the username from the configuration properties file.
	 *
	 * <p>
	 * This method reads the properties from the file located at
	 * <code>{user.dir}/src/main/resources/config.properties</code> and returns the
	 * value associated with the key <code>username</code>.
	 *
	 * @return the username as a {@code String} if found in the properties file;
	 *         {@code null} if an I/O error occurs while reading the file.
	 */
	public String getUsername() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			return prop.getProperty("username");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getPassword() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			return prop.getProperty("password");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retrieves the password from the configuration properties file.
	 *
	 * <p>
	 * This method loads the properties from the file located at
	 * <code>{user.dir}/src/main/resources/config.properties</code> and returns the
	 * value associated with the key <code>password</code>.
	 *
	 * @return the password as a {@code String} if found in the properties file;
	 *         {@code null} if an I/O error occurs while reading the file.
	 */
	public static String generateUniqueName(String base) {
		int uniqueNumber;
		do {
			uniqueNumber = 1000 + random.nextInt(9000);
		} while (usedNumbers.contains(uniqueNumber));

		usedNumbers.add(uniqueNumber);
		return base + uniqueNumber;
	}

	/**
	 * Sends a GET request to the specified localization endpoint using a session cookie and retrieves localization data.
	 *
	 * <p>This method targets the <code>/admin/localization</code> endpoint to fetch language and date format settings.
	 * It uses RestAssured to build the request, sets <code>Content-Type</code> to <code>application/json</code>,
	 * and includes the <code>orangehrm</code> session cookie for authentication.
	 *
	 * <p>The response is parsed to extract <code>data.language</code> and <code>data.dateFormat</code> values,
	 * which are then wrapped into lists and returned inside a {@link CustomResponse} object.
	 *
	 * @param endpoint     the relative URL to the localization API endpoint
	 * @param cookieValue  the value of the <code>orangehrm</code> session cookie for authentication
	 * @param body         a map for request body parameters (typically <code>null</code> for GET requests)
	 * @return a {@link CustomResponse} containing the response, status details, language, and date format
	 */

	public CustomResponse getLocalizationValid(String endpoint, String cookieValue, Map<String, String> body) {
		// write your code here

		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");
				
		Response response = request.get(BASE_URL + endpoint).then().extract().response();
		
		JsonPath jsonPath = response.jsonPath();
		
		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		String language = jsonPath.getString("data.language");
		String strDateFormat = jsonPath.getString("data.dateFormat");

		// Wrap them into lists
		List<Object> languages = Collections.singletonList(language);
		List<Object> dateFormat = Collections.singletonList(strDateFormat);
		
	    // Return wrapped CustomResponse with extracted fields
		return new CustomResponse(response, statusCode, status, languages, dateFormat);
	}


	/**
	 * Sends a GET request to the given endpoint to retrieve a list of active languages.
	 *
	 * <p>This method uses RestAssured to construct the request with the required <code>orangehrm</code> session cookie 
	 * and <code>Content-Type: application/json</code> header. Although GET requests typically do not include a body, 
	 * an optional request body may be attached if provided.
	 *
	 * <p>The response is parsed to extract <code>id</code>, <code>name</code>, and <code>code</code> from each language entry 
	 * in the <code>data</code> array. These fields are wrapped into lists and returned in a {@link CustomResponse} object.
	 *
	 * @param endpoint     the relative URL for the active languages API endpoint
	 * @param cookieValue  the session cookie value used for authentication
	 * @param body         an optional map of request body parameters (can be <code>null</code>)
	 * @return a {@link CustomResponse} containing the response object and extracted fields
	 */

	public CustomResponse getActiveLanguages(String endpoint, String cookieValue, Map<String, String> body) {
		
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");
		
		// Only add the body if it's not null
		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();
	
		JsonPath jsonPath = response.jsonPath();
		
		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		String strlanguageId = jsonPath.getString("data.id");
		String strlanguageName = jsonPath.getString("data.name");
		String strlanguageCode = jsonPath.getString("data.code");

		// Wrap them into lists
		List<Object> languageId = Collections.singletonList(strlanguageId);
		List<Object> languageName = Collections.singletonList(strlanguageName);
		List<Object> languageCode = Collections.singletonList(strlanguageCode);
		
	    // Return wrapped CustomResponse with extracted fields
		return new CustomResponse(response, statusCode, status, languageId, languageName , languageCode);

	}

	/**
	 * Sends a GET request to the specified dashboard shortcuts endpoint using a session cookie.
	 *
	 * <p>This method constructs a GET request using RestAssured, sets the <code>Content-Type</code> 
	 * to <code>application/json</code>, and includes a session cookie named <code>orangehrm</code>. 
	 * Although not standard, an optional request body can be attached if provided.
	 *
	 * <p>The method extracts boolean values from keys such as <code>leave.assign_leave</code>, 
	 * <code>leave.leave_list</code>, <code>leave.apply_leave</code>, <code>leave.my_leave</code>,
	 * <code>time.employee_timesheet</code>, and <code>time.my_timesheet</code> from the <code>data</code> object 
	 * in the response using bracket notation due to the presence of dots in key names.
	 *
	 * <p>These values are wrapped in lists and returned via a {@link CustomResponse} object for validation and assertion.
	 *
	 * @param endpoint     the relative endpoint for dashboard shortcuts, appended to the base URL
	 * @param cookieValue  the value of the <code>orangehrm</code> session cookie for authentication
	 * @param body         optional request body parameters (may be <code>null</code>)
	 * @return a {@link CustomResponse} containing the response and extracted shortcut data fields
	 */
	public CustomResponse getDashboardShortcuts(String endpoint, String cookieValue, Map<String, String> body) {
		
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");
	
		// Only add the body if it's not null
		if (body != null) {
			request.body(body);
		}
		
		Response response = request.get(BASE_URL + endpoint).then().extract().response();
		
		
		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();
		
		JsonPath jsonPath = response.jsonPath();

		// Access fields with dots in key names using bracket notation
		Boolean assignLeave = jsonPath.getBoolean("data['leave.assign_leave']");
		Boolean leaveList = jsonPath.getBoolean("data['leave.leave_list']");
		Boolean applyLeave = jsonPath.getBoolean("data['leave.apply_leave']");
		Boolean myLeave = jsonPath.getBoolean("data['leave.my_leave']");
		Boolean empTimesheet = jsonPath.getBoolean("data['time.employee_timesheet']");
		Boolean myTimesheet = jsonPath.getBoolean("data['time.my_timesheet']");

		// Wrap into lists
		List<Object> leaveAssignLeave = Collections.singletonList(assignLeave);
		List<Object> leaveLeaveList = Collections.singletonList(leaveList);
		List<Object> leaveApplyLeave = Collections.singletonList(applyLeave);
		List<Object> leaveMyLeave = Collections.singletonList(myLeave);
		List<Object> timeEmployeeTimesheet = Collections.singletonList(empTimesheet);
		List<Object> timeMyTimesheet = Collections.singletonList(myTimesheet);

	    // Return wrapped CustomResponse with extracted fields
		return new CustomResponse(response, statusCode, status,
		    leaveAssignLeave, leaveLeaveList, leaveApplyLeave,
		    leaveMyLeave, timeEmployeeTimesheet, timeMyTimesheet);
	   
		
	}

	/**
	 * Sends a GET request to fetch authentication provider details from the specified endpoint.
	 *
	 * <p>
	 * This method uses RestAssured to construct a GET request, sets the 
	 * <code>Content-Type</code> header to <code>application/json</code>, and includes 
	 * an <code>orangehrm</code> session cookie for authentication. If a request body is provided, 
	 * it will be added to the request, although including a body in a GET request is not standard practice.
	 *
	 * <p>
	 * The response is parsed to extract the following fields from the <code>data</code> array
	 *
	 * @param endpoint     the relative path of the endpoint to which the request is sent
	 * @param cookieValue  the value of the <code>orangehrm</code> session cookie
	 * @param body         a map representing the request body, or <code>null</code> if not needed
	 * @return a {@link CustomResponse} containing status details and extracted response data
	 */
	public CustomResponse getAuthProviders(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		// Only add the body if it's not null
		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();
	
		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();

		// Extract lists from "data" array
		List<Object> providerIds = jsonPath.getList("data.id");
		List<Object> providerNames = jsonPath.getList("data.providerName");
		List<Object> providerUrls = jsonPath.getList("data.providerUrl");
		List<Object> providerStatuses = jsonPath.getList("data.status");
		List<Object> clientIds = jsonPath.getList("data.clientId");

		// Return wrapped CustomResponse with extracted fields
		return new CustomResponse(response, statusCode, status,
		        providerIds, providerNames, providerUrls,
		        providerStatuses, clientIds);
		
	};

	/**
	 * Sends a GET request to fetch a list of OAuth clients from the admin endpoint.
	 *
	 * <p>
	 * This method constructs a GET request using RestAssured, sets the 
	 * <code>Content-Type</code> header to <code>application/json</code>, and includes 
	 * an <code>orangehrm</code> session cookie for authorization. If a request body is 
	 * provided, it is attached to the request. Note that sending a body with a GET request 
	 * is non-standard and may be ignored or rejected by some servers.
	 *
	 * <p>
	 * The response is parsed to extract OAuth client attributes.
	 * These are wrapped into an {@link OAuthClientInfo} object and returned inside a {@link CustomResponse}.
	 *
	 * @param endpoint     the API endpoint for retrieving OAuth client data
	 * @param cookieValue  the session cookie value for authentication
	 * @param body         the optional request body, or <code>null</code> if not needed
	 * @return a {@link CustomResponse} containing status info and an {@link OAuthClientInfo} payload
	 */


	public CustomResponse getAdminOAuthList(String endpoint, String cookieValue, Map<String, String> body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		// Only add the body if it's not null
		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();
		
		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();

		List<Object> ids = jsonPath.getList("data.id");
	    List<Object> names = jsonPath.getList("data.name");
	    List<Object> clientIds = jsonPath.getList("data.clientId");
	    System.out.println("Cleint Id is : " + clientIds);
	    List<Object> redirectUris = jsonPath.getList("data.redirectUri");
	    List<Object> enabledFlags = jsonPath.getList("data.enabled");
	    List<Object> confidentialFlags = jsonPath.getList("data.confidential");

	    // There Are Multiple CustomResponse Become error prone so creating other constructor for holding all Variables Types : - OAuthClientInfo
	    OAuthClientInfo clientInfo = new OAuthClientInfo(ids, names, clientIds, redirectUris, enabledFlags, confidentialFlags);
	    // Return wrapped CustomResponse with extracted fields
	    return new CustomResponse(response, statusCode, status, clientInfo);

	}
	
	/**
	 * Sends a GET request to retrieve a detailed list of employees from the specified endpoint.
	 *
	 * <p>
	 * This method builds a GET request using RestAssured, sets the 
	 * <code>Content-Type</code> header to <code>application/json</code>, and attaches a 
	 * session cookie named <code>orangehrm</code> for authentication. If a request body 
	 * is provided, it is included in the request (though not typical for GET requests).
	 *
	 * <p>
	 * The method extracts employee-related fields from the JSON response.
	 * These fields are wrapped into a {@link CustomResponse} object and returned.
	 *
	 * @param endpoint     the API endpoint to send the request to
	 * @param cookieValue  the value of the <code>orangehrm</code> session cookie
	 * @param body         the request body to include, or <code>null</code> if not needed
	 * @return a {@link CustomResponse} containing the response data and extracted employee fields
	 */

	public CustomResponse getEmployeeList(String endpoint, String cookieValue, Object body) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		// Only add the body if it's not null
		if (body != null) {
			request.body(body);
		}
		
		Response response = request.get(BASE_URL + endpoint).then().extract().response();
		
		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();

		List<Object> empNumber = jsonPath.getList("data.empNumber");
	    List<Object> lastName = jsonPath.getList("data.lastName");
	    List<Object> firstName = jsonPath.getList("data.firstName");
	    System.out.println("Emp first is : " + firstName);
	    List<Object> employeeId = jsonPath.getList("data.employeeId");
	    
	    // Return wrapped CustomResponse with extracted fields
	    return new CustomResponse(response, statusCode, status , empNumber , lastName , firstName , employeeId);   
	    
	    
	}

	/**
	 * Sends a PUT request to the specified endpoint with a session cookie and an optional request body.
	 *
	 * <p>
	 * This method constructs a PUT request using RestAssured, sets the 
	 * <code>Content-Type</code> header to <code>application/json</code>, and includes 
	 * a cookie named <code>orangehrm</code> for session or authentication purposes. 
	 * If a request body is provided, it is attached to the request.
	 *
	 * <p>
	 * The response is parsed to extract language package fields.
	 * These values are wrapped and returned inside a {@link CustomResponse} object.
	 *
	 * @param endpoint     the API endpoint to which the PUT request is sent
	 * @param cookieValue  the session cookie value for authentication
	 * @param body         the request body to be sent with the PUT request, or <code>null</code>
	 * @return a {@link CustomResponse} containing the response and extracted language data
	 */
	public CustomResponse putLanguagePackage(String endpoint, String cookieValue , Object body) {

		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue)
				.header("Content-Type", "application/json");

		// Only add the body if it's not null
		if (body != null) {
			request.body(body);
		}
		
		Response response = request.put(BASE_URL + endpoint).then().extract().response();
		
		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();

//		List<Object> adminLangId = jsonPath.getList("data.id");
		String adminLangId = jsonPath.getString("data.id");
		String adminLangName = jsonPath.getString("data.name");
		String adminLangCode = jsonPath.getString("data.code");
		
	    // Return wrapped CustomResponse with extracted fields
	    return new CustomResponse(response, statusCode, status , adminLangId , adminLangName, adminLangCode);   
	}
	
	
	/**
	 * Sends a DELETE request to the specified endpoint with a session cookie and a request body.
	 *
	 * <p>
	 * This method constructs a DELETE request using RestAssured, sets the 
	 * <code>Content-Type</code> header to <code>application/json</code>, and includes 
	 * a session cookie named <code>orangehrm</code> for authentication purposes.
	 * The request body is attached as a JSON string, which is supported in some API implementations
	 * for DELETE requests, especially when specifying resources to remove.
	 *
	 * <p>
	 * The response is parsed to extract a list of deleted IDs from the <code>data</code> field,
	 * which is wrapped into a {@link CustomResponse} for further validation.
	 *
	 * @param endpoint     the relative endpoint to which the DELETE request is sent,
	 *                     appended to the base URL
	 * @param cookieValue  the value of the <code>orangehrm</code> session cookie for authentication
	 * @param requestBody  the JSON string representing the request body to be sent with the DELETE request
	 * @return a {@link CustomResponse} containing the response and the list of deleted IDs
	 */
	public CustomResponse deleteLangById(String endpoint, String cookieValue,String requestBody) {
		
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue)
				.header("Content-Type", "application/json");
				
	    Response response = request.body(requestBody)
				.delete(BASE_URL + endpoint) // Sending PUT request to the specified endpoint
				.then().extract().response(); // Extracting the response
		
	    int statusCode = response.getStatusCode();
		String status = response.getStatusLine();
		
		JsonPath jsonPath = response.jsonPath();
		List<Object> deletedIds = jsonPath.getList("data");
		
	    // Return wrapped CustomResponse with extracted fields	
		return new CustomResponse(response, statusCode, status, deletedIds);
	
	}
	
	/**
	 * Sends a PUT request to the specified endpoint with a session cookie and a JSON request body.
	 *
	 * <p>
	 * This method constructs a PUT request using RestAssured. It sets the 
	 * <code>Content-Type</code> header to <code>application/json</code>, attaches the specified 
	 * request body, and includes a session cookie named <code>orangehrm</code> for authentication.
	 *
	 * <p>
	 * The response is expected to contain a <code>data</code> field representing updated module settings,
	 * which are parsed as a map and wrapped in a {@link CustomResponse}.
	 *
	 * @param endpoint     the relative endpoint to which the PUT request is sent, appended to the base URL
	 * @param cookieValue  the value of the <code>orangehrm</code> session cookie for authentication
	 * @param requestBody  the JSON string representing the request body to be sent with the PUT request
	 * @return a {@link CustomResponse} object containing the full response, status, and module settings map
	 */
	public CustomResponse putModulesSettings(String endpoint, String cookieValue, String requestBody) {
		
	    RequestSpecification request = RestAssured
	        .given()
	        .cookie("orangehrm", cookieValue)
	        .header("Content-Type", "application/json")
	        .body(requestBody);
	    
	    Response response = request.put(BASE_URL + endpoint)
		        .then()
		        .extract()
		        .response();
	        

	    int statusCode = response.getStatusCode();
	    String status = response.getStatusLine();

	    // You can optionally parse specific booleans if needed
	    JsonPath jsonPath = response.jsonPath();
	    Map<String, Object> moduleSettings = jsonPath.getMap("data"); // assuming response has a "data" map
        
	    // Return wrapped CustomResponse with extracted fields
	    return new CustomResponse(response, statusCode, status, moduleSettings);
	}

	/**
	 * Sends a POST request to the specified endpoint with a session cookie and a JSON request body.
	 *
	 * <p>
	 * This method uses RestAssured to send a POST request with the required headers and authentication cookie. 
	 * The <code>Content-Type</code> is set to <code>application/json</code>, and the request body is provided 
	 * as a JSON-formatted string.
	 *
	 * <p>
	 * The response is parsed to extract key OpenID provider fields (name, url, clientId, clientSecret),
	 * which are wrapped into a {@link CustomResponse} object for validation or further processing.
	 *
	 * @param endpoint     the API endpoint (relative to the base URL) for the POST request
	 * @param cookieValue  the value of the <code>orangehrm</code> session cookie for authentication
	 * @param requestBody  the JSON string representing the OpenID provider to be created
	 * @return a {@link CustomResponse} containing the raw response, status code, status line, and extracted fields
	 */

	public CustomResponse postOpenIdProvide(String endpoint, String cookieValue,String requestBody) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue)
				.header("Content-Type", "application/json")
				.body(requestBody)
		        .header("Content-Type", "application/json")
		        .body(requestBody);

	    Response response = request.post(BASE_URL + endpoint)
	        .then()
	        .extract()
	        .response();

	    int statusCode = response.getStatusCode();
	    String status = response.getStatusLine();
	    JsonPath jsonPath = response.jsonPath();

	    // Extract fields if needed (optional)
	    String name = jsonPath.getString("data.name");
	    String url = jsonPath.getString("data.url");
	    String clientId = jsonPath.getString("data.clientId");
	    String clientSecret = jsonPath.getString("data.clientSecret");

	    // Return wrapped CustomResponse with extracted fields
	    return new CustomResponse(response, statusCode, status, name, url, clientId, clientSecret);
	    
		}

}