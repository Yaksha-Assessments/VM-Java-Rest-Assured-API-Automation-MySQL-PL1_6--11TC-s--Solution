package testcases;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import coreUtilities.utils.FileOperations;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import rest.ApiUtil;
import rest.CustomResponse;

@SuppressWarnings("unused")
public class RestAssured_TestCases {

	private static String baseUrl;
	private static String username;
	private static String password;
	private static String cookieValue = null;
	private ApiUtil apiUtil;
	private int employeeStatus;
	private TestCodeValidator testCodeValidator;
	private String apiUtilPath = System.getProperty("user.dir") + "\\src\\main\\java\\rest\\ApiUtil.java";
	private String excelPath = System.getProperty("user.dir") + "\\src\\main\\resources\\TestData.xlsx";

	@Test(priority = 0, groups = {
			"PL1" }, description = "Login to the application using Selenium and retrieve the cookie.")
	public void loginWithSeleniumAndGetCookie() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();

		apiUtil = new ApiUtil();
		baseUrl = apiUtil.getBaseUrl();
		username = apiUtil.getUsername();
		password = apiUtil.getPassword();

		driver.get(baseUrl + "/web/index.php/auth/login");
		Thread.sleep(3000); // Wait for page load

		// Login to the app
		driver.findElement(By.name("username")).sendKeys(username);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button[type='submit']")).click();
		Thread.sleep(9000); // Wait for login

		// Extract cookie named "orangehrm"
		Set<Cookie> cookies = driver.manage().getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("orangehrm")) {
				cookieValue = cookie.getValue();
				break;
			}
		}

		driver.quit();
		testCodeValidator = new TestCodeValidator();

		if (cookieValue == null) {
			throw new RuntimeException("orangehrm cookie not found after login");
		}
	}

	@Test(priority = 1, groups = {
			"PL1" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/localization' endpoint using a valid authentication cookie\n"
					+ "2. Verify that the response is parsed into a CustomResponse object with language and dateFormat fields extracted from the 'data' section\n"
					+ "3. Assert that both 'language' and 'dateFormat' fields are not null and not empty\n"
					+ "4. Validate that the test method implementation uses required RestAssured keywords using the TestCodeValidator\n"
					+ "5. Assert that the response status code is 200 (OK) and all required fields are present as per validation logic")

	public void getLocalizationValid() throws IOException {

		String endpoint = "/web/index.php/api/v2/admin/localization";

		// Send GET request
		CustomResponse customResponse = apiUtil.getLocalizationValid(endpoint, cookieValue, null);

		// Validate customResponse structure
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath,
				"getLocalizationValid", List.of("cookie", "get", "response"));
		System.out.println(isImplementationCorrect);

		// Validating customResponse Field
		Assert.assertTrue(TestCodeValidator.validateResponseFields("getLocalizationValid", customResponse),
				"Must have all required fields in the customResponse.");

		System.out.println("Status Code: " + customResponse.getStatusCode());
		System.out.println("customResponse Body: " + customResponse.getResponse().getBody().asString());

		// Retrieve objects for Checking
		List<Object> Languages = customResponse.getLanguages();
		List<Object> dateFormat = customResponse.getDateFormat();

		System.out.println("Language :" + Languages + "\n" + " DateFormat : " + dateFormat);

		Assert.assertFalse(Languages.isEmpty(), "ItemId list should not be empty.");
		Assert.assertFalse(dateFormat.isEmpty(), "ItemName list should not be empty.");

		// Checking all values of customResponses Data is not null
		for (int i = 0; i < Languages.size(); i++) {
			Assert.assertNotNull(Languages.get(i), "ItemId at index " + i + " should not be null.");
			Assert.assertNotNull(dateFormat.get(i), "ItemName at index " + i + " should not be null.");
		}
		// Checking For Implementation
		Assert.assertTrue(isImplementationCorrect,
				"getLocalizationValid must be implemented using the Rest Assured methods only!");
		assertEquals(customResponse.getStatusCode(), 200);

	}

	@Test(priority = 2, groups = {
			"PL1" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/i18n/languages?limit=50&offset=0&sortOrder=ASC&activeOnly=true' endpoint using a valid cookie\n"
					+ "2. Parse the response using a CustomResponse object, extracting 'id', 'name', and 'code' fields from each entry in the 'data' array\n"
					+ "3. Print the customResponse status code, body, and extracted language fields for debugging\n"
					+ "4. Assert that the extracted lists of IDs, names, and codes are not empty and contain no null entries\n"
					+ "5. Validate that the method is implemented using required RestAssured calls using TestCodeValidator\n"
					+ "6. Assert that the response contains all expected fields and the status code is 200 (OK)")

	public void getActiveLanguages() throws IOException {
		// Endpoint
		String endpoint = "/web/index.php/api/v2/admin/i18n/languages?limit=50&offset=0&sortOrder=ASC&activeOnly=true";

		// Send GET request
		CustomResponse customResponse = apiUtil.getActiveLanguages(endpoint, cookieValue, null);

		// Validate customResponse Structure
		System.out.println("Reading file from: " + apiUtilPath);

		System.out.println("Status Code: " + customResponse.getStatusCode());
		System.out.println("customResponse Body: " + customResponse.getResponse().getBody().asString());

		// Retrieve objects for Checking
		List<Object> languageId = customResponse.getLanguageId();
		List<Object> languageName = customResponse.getLanguageName();
		List<Object> languageCode = customResponse.getLanguageCode();

		System.out.println("Language Id :" + languageId + "\n" + "Language Name : " + languageName + "\n"
				+ "Language Code : " + languageCode);

		Assert.assertFalse(languageId.isEmpty(), "ItemId list should not be empty.");
		Assert.assertFalse(languageName.isEmpty(), "ItemName list should not be empty.");
		Assert.assertFalse(languageCode.isEmpty(), "ItemName list should not be empty.");

		// Checking all values of customResponses Data is not null
		for (int i = 0; i < languageId.size(); i++) {
			Assert.assertNotNull(languageId.get(i), "ItemId at index " + i + " should not be null.");
			Assert.assertNotNull(languageName.get(i), "ItemId at index " + i + " should not be null.");
			Assert.assertNotNull(languageCode.get(i), "ItemName at index " + i + " should not be null.");
		}

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath,
				"getActiveLanguages", List.of("given", "cookie", "get", "response"));
		System.out.println(isImplementationCorrect);

		// Validating customResponse Field
		Assert.assertTrue(TestCodeValidator.validateResponseFields("getActiveLanguages", customResponse),
				"Must have all required fields in the customResponse.");

		// Checking For Implementation
		Assert.assertTrue(isImplementationCorrect,
				"getActiveLanguages must be implementated using the Rest assured  methods only!");
		assertEquals(customResponse.getStatusCode(), 200);
	}

	@Test(priority = 3, groups = {
			"PL1" }, description = "1. Send a GET request to the '/web/index.php/api/v2/dashboard/shortcuts' endpoint with a valid cookie\n"
					+ "2. Parse the response using CustomResponse to extract relevant shortcut fields like leave.assign_leave, leave.leave_list, etc.\n"
					+ "3. Print the customResponse status code, response body, and extracted fields for verification\n"
					+ "4. Assert that all extracted lists are not empty and contain expected shortcut information\n"
					+ "5. Assert that the customResponse status code is 200 (OK)")

	public void getDashboardShortcuts() throws IOException {

		// Sending Endpoint
		CustomResponse customResponse = apiUtil.getDashboardShortcuts("/web/index.php/api/v2/dashboard/shortcuts",
				cookieValue, null);

		System.out.println("Status Code: " + customResponse.getStatusCode());
		System.out.println("customResponse Body: " + customResponse.getResponse().getBody().asString());

		List<Object> leave_assign_leave = customResponse.leave_Assign_Level();
		List<Object> leave_leave_list = customResponse.leave_Leave_List();
		List<Object> leave_apply_leave = customResponse.leave_Apply_Leave();
		List<Object> leave_my_leave = customResponse.leave_My_Leave();
		List<Object> time_employee_timesheet = customResponse.time_Employee_Timesheet();
		List<Object> time_my_timesheet = customResponse.time_My_Timesheet();

		// Print and assert
		System.out.println("leave.assign_leave: " + leave_assign_leave);
		System.out.println("leave.leave_list: " + leave_leave_list);

		// Assert lists are not empty
		Assert.assertFalse(leave_assign_leave.isEmpty(), "leave_assign_leave list should not be empty.");
		Assert.assertFalse(leave_leave_list.isEmpty(), "leave_leave_list list should not be empty.");
		Assert.assertFalse(leave_apply_leave.isEmpty(), "leave_apply_leave list should not be empty.");
		Assert.assertFalse(leave_my_leave.isEmpty(), "leave_my_leave list should not be empty.");
		Assert.assertFalse(time_employee_timesheet.isEmpty(), "time_employee_timesheet list should not be empty.");
		Assert.assertFalse(time_my_timesheet.isEmpty(), "time_my_timesheet list should not be empty.");

		// Assert none of the values are null
		for (int i = 0; i < leave_assign_leave.size(); i++) {
			Assert.assertNotNull(leave_assign_leave.get(i),
					"'leave_assign_leave' at index " + i + " should not be null.");
		}
		for (int i = 0; i < leave_leave_list.size(); i++) {
			Assert.assertNotNull(leave_leave_list.get(i), "'leave_leave_list' at index " + i + " should not be null.");
		}
		for (int i = 0; i < leave_apply_leave.size(); i++) {
			Assert.assertNotNull(leave_apply_leave.get(i),
					"'leave_apply_leave' at index " + i + " should not be null.");
		}
		for (int i = 0; i < leave_my_leave.size(); i++) {
			Assert.assertNotNull(leave_my_leave.get(i), "'leave_my_leave' at index " + i + " should not be null.");
		}
		for (int i = 0; i < time_employee_timesheet.size(); i++) {
			Assert.assertNotNull(time_employee_timesheet.get(i),
					"'time_employee_timesheet' at index " + i + " should not be null.");
		}
		for (int i = 0; i < time_my_timesheet.size(); i++) {
			Assert.assertNotNull(time_my_timesheet.get(i),
					"'time_my_timesheet' at index " + i + " should not be null.");
		}

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath,
				"getDashboardShortcuts", List.of("given", "cookie", "get", "response"));
		System.out.println(isImplementationCorrect);

		// Validating customResponse Field
		Assert.assertTrue(TestCodeValidator.validateResponseFields("getDashboardShortcuts", customResponse),
				"Must have all required fields in the customResponse.");

		Assert.assertTrue(isImplementationCorrect,
				"getDashboardShortcuts must be implementated using the Rest assured  methods only!");
		assertEquals(customResponse.getStatusCode(), 200);
	}

	@Test(priority = 4, groups = {
			"PL1" }, description = "1. Create a new OpenID provider using POST to ensure data exists for validation\n"
					+ "2. Send a GET request to the '/web/index.php/api/v2/auth/openid-providers?limit=50&offset=0' endpoint with a valid cookie\n"
					+ "3. Parse the response using CustomResponse to extract fields like id, providerName, providerUrl, status, and clientId\n"
					+ "4. Print the status code, response body, and all extracted fields for verification\n"
					+ "5. Assert that none of the extracted lists are empty or contain null values\n"
					+ "6. Assert that the customResponse status code is 200 (OK)")
	public void getAuthProviders() throws IOException {

		// Creating Post Response for Tackle Failure of GET Response returning Nothing
		createAuthProviderTest();

		// GET API Request
		CustomResponse customResponse = apiUtil
				.getAuthProviders("/web/index.php/api/v2/auth/openid-providers?limit=50&offset=0", cookieValue, null);

		System.out.println("Status Code: " + customResponse.getStatusCode());
		System.out.println("customResponse Body: " + customResponse.getResponse().getBody().asString());

		// Retrieve objects for Checking
		List<Object> providerIds = customResponse.getProviderIds(); // from "id"
		List<Object> providerNames = customResponse.getProviderNames(); // from "providerName"
		List<Object> providerUrls = customResponse.getProviderUrls(); // from "providerUrl"
		List<Object> providerStatuses = customResponse.getProviderStatuses(); // from "status"
		List<Object> clientIds = customResponse.getClientIds(); // from "clientId"

		System.out.println("Provider Ids       : " + providerIds);
		System.out.println("Provider Names     : " + providerNames);
		System.out.println("Provider URLs      : " + providerUrls);
		System.out.println("Provider Statuses  : " + providerStatuses);
		System.out.println("Client IDs         : " + clientIds);

		// Assert none of the lists are empty
		Assert.assertFalse(providerIds.isEmpty(), "Provider ID list should not be empty.");
		Assert.assertFalse(providerNames.isEmpty(), "Provider Name list should not be empty.");
		Assert.assertFalse(providerUrls.isEmpty(), "Provider URL list should not be empty.");
		Assert.assertFalse(providerStatuses.isEmpty(), "Provider Status list should not be empty.");
		Assert.assertFalse(clientIds.isEmpty(), "Client ID list should not be empty.");

		// Assert none of the individual values are null
		for (int i = 0; i < providerIds.size(); i++) {
			Assert.assertNotNull(providerIds.get(i), "Provider ID at index " + i + " should not be null.");
			Assert.assertNotNull(providerNames.get(i), "Provider Name at index " + i + " should not be null.");
			Assert.assertNotNull(providerUrls.get(i), "Provider URL at index " + i + " should not be null.");
			Assert.assertNotNull(providerStatuses.get(i), "Provider Status at index " + i + " should not be null.");
			Assert.assertNotNull(clientIds.get(i), "Client ID at index " + i + " should not be null.");
		}

		// Validate implementation correctness
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "getAuthProviders",
				List.of("given", "cookie", "get", "response"));
		System.out.println(isImplementationCorrect);

		// Validating customResponse Field
		Assert.assertTrue(TestCodeValidator.validateResponseFields("getAuthProviders", customResponse),
				"Must have all required fields in the customResponse.");

		// Checking For Implementation
		Assert.assertTrue(isImplementationCorrect,
				"getAuthProviders must be implementated using the Rest assured  methods only!");
		assertEquals(customResponse.getStatusCode(), 200);
	};

	@Test(priority = 5, groups = {
			"PL1" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/oauth-clients?limit=50&offset=0' endpoint with a valid cookie\n"
					+ "2. Parse the response using CustomResponse to extract id, name, adminId, redirectUri, enabled, and confidential fields\n"
					+ "3. Print the status code, response body, and extracted fields\n"
					+ "4. Assert that all extracted lists are non-empty and contain non-null values\n"
					+ "5. Assert that the response status code is 200 (OK)")
	public void getAdminOAuthList() throws IOException {

		CustomResponse customResponse = apiUtil
				.getAdminOAuthList("/web/index.php/api/v2/admin/oauth-clients?limit=50&offset=0", cookieValue, null);

		System.out.println("Status Code: " + customResponse.getStatusCode());
		System.out.println("customResponse Body: " + customResponse.getResponse().getBody().asString());

		// Retrieve objects for Checking
		List<Object> ids = customResponse.getIds();
		List<Object> names = customResponse.getNames();
		List<Object> clientAdminIds = customResponse.getAdminIds();
		List<Object> redirectUrls = customResponse.getRedirectUris();
		List<Object> enabledFlags = customResponse.getEnabledFlags();
		List<Object> confidentialFlags = customResponse.getConfidentialFlags();

		Assert.assertFalse(ids.isEmpty(), "IDs should not be empty.");
		Assert.assertFalse(names.isEmpty(), "Names should not be empty.");
		Assert.assertFalse(clientAdminIds.isEmpty(), "Client IDs should not be empty.");
		Assert.assertFalse(redirectUrls.isEmpty(), "Redirect URIs should not be empty.");
		Assert.assertFalse(enabledFlags.isEmpty(), "Enabled flags should not be empty.");
		Assert.assertFalse(confidentialFlags.isEmpty(), "Confidential flags should not be empty.");

		for (int i = 0; i < ids.size(); i++) {
			Assert.assertNotNull(ids.get(i), "ID at index " + i + " should not be null.");
			Assert.assertNotNull(names.get(i), "Name at index " + i + " should not be null.");
			Assert.assertNotNull(clientAdminIds.get(i), "Client ID at index " + i + " should not be null.");
			Assert.assertNotNull(redirectUrls.get(i), "Redirect URL at index " + i + " should not be null.");
			Assert.assertNotNull(enabledFlags.get(i), "Enabled flag at index " + i + " should not be null.");
			Assert.assertNotNull(confidentialFlags.get(i), "Confidential flag at index " + i + " should not be null.");
		}

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "getAdminOAuthList",
				List.of("given", "cookie", "get", "response"));
		System.out.println(isImplementationCorrect);

		// Checking For Implementation
		Assert.assertTrue(TestCodeValidator.validateResponseFields("getAdminOAuthList", customResponse),
				"Must have all required fields in the customResponse.");

		// Checking For Implementation
		Assert.assertTrue(isImplementationCorrect,
				"getAdminOAuthList must be implementated using the Rest assured  methods only!");
		assertEquals(customResponse.getStatusCode(), 200);
	}

	@Test(priority = 6, groups = {
			"PL1" }, description = "1. Send a GET request to '/web/index.php/api/v2/pim/employees?limit=50&offset=0&model=detailed&includeEmployees=onlyCurrent&sortField=employee.firstName&sortOrder=ASC' with a valid cookie\n"
					+ "2. Extract fields like empNumber, firstName, and lastName from the response\n"
					+ "3. Print status code, response body, and extracted employee details\n"
					+ "4. Assert that the lists are not empty and contain non-null values\n"
					+ "5. Assert that the response status code is 200 (OK)")
	public void getEmployeeList() throws IOException {

		CustomResponse customResponse = apiUtil.getEmployeeList(
				"/web/index.php/api/v2/pim/employees?limit=50&offset=0&model=detailed&includeEmployees=onlyCurrent&sortField=employee.firstName&sortOrder=ASC",
				cookieValue, null);

		System.out.println("Status Code: " + customResponse.getStatusCode());
		System.out.println("customResponse Body: " + customResponse.getResponse().getBody().asString());

		// Retrieve employee details for checking
		List<Object> empNumbers = customResponse.getEmpNumbers();
		List<Object> firstNames = customResponse.getFirstNames();
		List<Object> lastNames = customResponse.getLastNames();

		System.out.println("======= Employee Data =======");

		// Validate that none of the lists are empty
		Assert.assertFalse(empNumbers.isEmpty(), "empNumbers should not be empty.");
		Assert.assertFalse(firstNames.isEmpty(), "firstNames should not be empty.");
		Assert.assertFalse(lastNames.isEmpty(), "lastNames should not be empty.");

		// Validate that no value in any list is null
		for (int i = 0; i < empNumbers.size(); i++) {
			Assert.assertNotNull(empNumbers.get(i), "empNumber at index " + i + " should not be null.");
			Assert.assertNotNull(firstNames.get(i), "firstName at index " + i + " should not be null.");
			Assert.assertNotNull(lastNames.get(i), "lastName at index " + i + " should not be null.");
		}

		// Validate implementation correctness from file (if needed)
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "getEmployeeList",
				List.of("given", "cookie", "get", "response"));
		System.out.println(isImplementationCorrect);

		// Validate response fields
		Assert.assertTrue(TestCodeValidator.validateResponseFields("getEmployeeList", customResponse),
				"Must have all required employee fields in the customResponse.");

		// Checking For Implementation
		Assert.assertTrue(isImplementationCorrect,
				"getEmployeeList must be implementated using the Rest assured  methods only!");
		assertEquals(customResponse.getStatusCode(), 200);
	}

	@Test(priority = 7, groups = {
			"PL1" }, description = "1. Fetch a language package ID not present in the current list (less than 450)\n"
					+ "2. Send a PUT request to '/web/index.php/api/v2/admin/i18n/languages/{lang_package_id}' with valid cookie and body\n"
					+ "3. Print the language ID used, status code, and full response body\n"
					+ "4. Assert that the response status code is 200 (OK) and key fields (id, name, code) are non-empty")
	public void putLanguagePackage() throws IOException {

		int id = getMissingLanguageIdLessThan450();
		System.out.println("language id is " + id);

		CustomResponse customResponse = apiUtil.putLanguagePackage("/web/index.php/api/v2/admin/i18n/languages/" + id,
				cookieValue, null);

		// Retrieve employee details for checking
		String adminLangId = customResponse.getAdminLangId();
		String adminLangName = customResponse.getAdminLangName();
		String adminLangCode = customResponse.getAdminLangCode();

		// Validate that none of the lists are empty
		Assert.assertFalse(adminLangId.isEmpty(), "adminLangId should not be empty.");
		Assert.assertFalse(adminLangName.isEmpty(), "adminLangName should not be empty.");
		Assert.assertFalse(adminLangCode.isEmpty(), "adminLangCode should not be empty.");

		// Validate that no value in any list is null
		for (int i = 0; i < 3; i++) {
			Assert.assertNotNull(adminLangId, "adminLangId at index " + i + " should not be null.");
			Assert.assertNotNull(adminLangName, "adminLangName at index " + i + " should not be null.");
			Assert.assertNotNull(adminLangCode, "adminLangCode at index " + i + " should not be null.");
		}

		// Validate implementation correctness from file (if needed)
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath,
				"putLanguagePackage", List.of("given", "cookie", "put", "response"));
		System.out.println(isImplementationCorrect);

		// Validate response fields
		Assert.assertTrue(TestCodeValidator.validateResponseFields("putLanguagePackage", customResponse),
				"Must have all required employee fields in the customResponse.");
		// Checking For Implementation
		Assert.assertTrue(isImplementationCorrect,
				"putLanguagePackage must be implementated using the Rest assured  methods only!");
		assertEquals(customResponse.getStatusCode(), 200);
	}

	@Test(priority = 8, groups = {
			"PL1" }, description = "1. Fetch the first available language ID to delete and construct the request body\n"
					+ "2. Send a DELETE request to the '/web/index.php/api/v2/admin/i18n/languages' endpoint with valid cookie and request body\n"
					+ "3. Print the request body, status code, and response body for debugging\n"
					+ "4. Assert that the response status code is 200 (OK) and implementation is correct")
	public void deleteLangById() throws IOException {

		// Generating Random Ids For Deleting By API
		int id = getfirstlangid();
		String requestBody = "{ \"ids\": [" + id + "] }";

		CustomResponse customResponse = apiUtil.deleteLangById("/web/index.php/api/v2/admin/i18n/languages",
				cookieValue, requestBody);

		System.out.println("Request Body: " + requestBody);
		System.out.println("Status Code: " + customResponse.getStatusCode());

		// Validate request contains appropriate keywords
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "deleteLangById",
				List.of("given", "cookie", "delete", "response"));
		System.out.println(isImplementationCorrect);

		Assert.assertTrue(isImplementationCorrect,
				"deleteLangById must be implemented using proper RestAssured methods.");
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Expected status code 200 for successful deletion.");

		// Validate response fields
		Assert.assertTrue(TestCodeValidator.validateResponseFields("deleteLangById", customResponse),
				"Must have valid response structure with 'data' field.");

		// Checking For Implementation
		Assert.assertTrue(isImplementationCorrect,
				"deleteLangById must be implementated using the Rest assured  methods only!");
		assertEquals(customResponse.getStatusCode(), 200);

	}

	@Test(priority = 9, groups = {
			"PL1" }, description = "1. Create a request body with boolean values representing module states\n"
					+ "2. Send a PUT request to the '/web/index.php/api/v2/admin/modules' endpoint with a valid cookie and request body\n"
					+ "3. Print the request body, status code, and response body for verification\n"
					+ "4. Assert that the response status code is 200 (OK) and implementation is correct")
	public void putModulesSettings() throws Exception {

		Map<String, String> TestData = FileOperations.readExcelPOI(excelPath, "PutData9");

		String requestBody = "{" + "\"admin\": " + TestData.get("admin").toLowerCase() + "," + "\"buzz\": "
				+ TestData.get("buzz").toLowerCase() + "," + "\"claim\": " + TestData.get("claim").toLowerCase() + ","
				+ "\"directory\": " + TestData.get("directory").toLowerCase() + "," + "\"leave\": "
				+ TestData.get("leave").toLowerCase() + "," + "\"maintenance\": "
				+ TestData.get("maintenance").toLowerCase() + "," + "\"mobile\": "
				+ TestData.get("mobile").toLowerCase() + "," + "\"performance\": "
				+ TestData.get("performance").toLowerCase() + "," + "\"pim\": " + TestData.get("pim").toLowerCase()
				+ "," + "\"recruitment\": " + TestData.get("recruitment").toLowerCase() + "," + "\"time\": "
				+ TestData.get("time").toLowerCase() + "}";

		// Step 1: Call API
		CustomResponse customResponse = apiUtil.putModulesSettings("/web/index.php/api/v2/admin/modules", cookieValue,
				requestBody);

		// Step 2: Debug Print
		System.out.println("Request Body: " + requestBody);
		System.out.println("Status Code: " + customResponse.getStatusCode());
		System.out.println("customResponse Body: " + customResponse.getResponse().getBody().asString());

		// Step 3: Validate Implementation
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath,
				"putModulesSettings", List.of("given", "cookie", "put", "response"));
		Assert.assertTrue(isImplementationCorrect,
				"putModulesSettings must be implemented using the required RestAssured methods only!");

		// Step 4: Validate HTTP status
		assertEquals(customResponse.getStatusCode(), 200,
				"Expected status code 200 for successful module settings update.");

		// Step 5: Optional Response Field Validation
		Assert.assertTrue(TestCodeValidator.validateResponseFields("putModulesSettings", customResponse),
				"Response should contain all expected boolean module fields.");

		// Checking For Implementation
		Assert.assertTrue(isImplementationCorrect,
				"putModulesSettings must be implementated using the Rest assured  methods only!");
		assertEquals(customResponse.getStatusCode(), 200);
	}

	@Test(priority = 10, groups = {
			"PL1" }, description = "1. Create a request body with required fields: {name, url, clientId, clientSecret} and use a unique name\n"
					+ "2. Send a POST request to '/web/index.php/api/v2/auth/openid-providers' with a valid cookie and request body\n"
					+ "3. Print the request body, response status code, and body for debugging\n"
					+ "4. Assert that the status code is 200 and response contains expected fields")
	public void postOpenIdProvide() throws Exception {

		Map<String, String> TestData = FileOperations.readExcelPOI(excelPath, "PostData10");

		System.out.println("TestData : " + TestData);

		// Step 1: Prepare unique request body
		String uniqueName = TestData.get("name") + UUID.randomUUID().toString().substring(0, 8);
		String requestBody = "{" + "\"name\": \"" + uniqueName + "\"," + "\"url\": \"" + TestData.get("url") + "\","
				+ "\"clientId\": \"" + TestData.get("clientId") + "\"," + "\"clientSecret\": \""
				+ TestData.get("clientSecret") + "\"" + "}";

		// Step 2: Send request
		CustomResponse customResponse = apiUtil.postOpenIdProvide("/web/index.php/api/v2/auth/openid-providers",
				cookieValue, requestBody);

		// Step 3: Print response for debugging
		System.out.println("Request Body: " + requestBody);
		System.out.println("Status Code: " + customResponse.getStatusCode());
		System.out.println("customResponse Body: " + customResponse.getResponse().getBody().asString());

		// Step 4: Validate implementation from file
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "postOpenIdProvide",
				List.of("given", "cookie", "post", "response"));
		Assert.assertTrue(isImplementationCorrect,
				"postOpenIdProvide must be implemented using the required RestAssured methods only!");

		// Step 5: Validate response
		Assert.assertEquals(customResponse.getStatusCode(), 200, "Expected status code 200 for successful POST.");

		// Step 6: Optional: Validate expected fields
		Assert.assertTrue(TestCodeValidator.validateResponseFields("postOpenIdProvide", customResponse),
				"Response must contain expected OpenID fields.");

		// Checking For Implementation
		Assert.assertTrue(isImplementationCorrect,
				"postOpenIdProvide must be implementated using the Rest assured  methods only!");
		assertEquals(customResponse.getStatusCode(), 200);
	}

	/*------------Helper Methods------------*/

	public int getfirstlangid() {
		String url = "https://opensource-demo.orangehrmlive.com/web/index.php/api/v2/admin/i18n/languages?limit=50&offset=0&sortOrder=ASC&activeOnly=true";

		Response response = RestAssured.given().cookie("orangehrm", cookieValue)
				.header("Content-Type", "application/json").get(url).then().extract().response();

		// Extract the first language ID from the JSON customResponse
		int firstLangId = response.jsonPath().getInt("data[0].id");
		return firstLangId;
	}

	public int getMissingLanguageIdLessThan450() {
		String url = "https://opensource-demo.orangehrmlive.com/web/index.php/api/v2/admin/i18n/languages?limit=50&offset=0&sortOrder=ASC&activeOnly=true";

		Response customResponse = RestAssured.given().cookie("orangehrm", cookieValue)
				.header("Content-Type", "application/json").get(url).then().extract().response();

		List<Integer> existingIds = customResponse.jsonPath().getList("data.id");
//	    System.out.println("Existing IDs: " + existingIds);

		if (existingIds == null || existingIds.isEmpty()) {
			System.out.println("No IDs found in customResponse!");
			return -1;
		}

		Set<Integer> idSet = new HashSet<>(existingIds);

		for (int i = 1; i < 450; i++) {
			if (!idSet.contains(i)) {
				System.out.println("Missing ID Found: " + i);
				return i;
			}
		}

		System.out.println("No missing ID found under 450.");
		return -1;
	}

	public int getPayGradeid() {
		String endpoint = "/web/index.php/api/v2/admin/pay-grades?limit=50&offset=0";
		Response response = RestAssured.given().cookie("orangehrm", cookieValue).get(baseUrl + endpoint);

		if (response.statusCode() == 200) {
			int firstId = response.jsonPath().getInt("data[0].id");
			System.out.println("First Job Title ID: " + firstId);
			return firstId;
		} else {
			System.out.println("Failed to fetch job titles. Status code: " + response.statusCode());
			return -1;
		}
	}

	public int getemploymentstatusid() {
		String endpoint = "/web/index.php/api/v2/admin/employment-statuses?limit=50&offset=0";

		Response response = RestAssured.given().cookie("orangehrm", cookieValue).get(baseUrl + endpoint);

		if (response.statusCode() == 200) {
			int firstId = response.jsonPath().getInt("data[0].id");
			System.out.println("First Job Title ID: " + firstId);
			return firstId;
		} else {
			System.out.println("Failed to fetch job titles. Status code: " + response.statusCode());
			return -1;
		}

	}

	/*----------------------------------------------Helper Function----------------------------------------------------------*/

	public void createAuthProviderTest() throws IOException {

		String endpoint = "/web/index.php/api/v2/auth/openid-providers";

		Map<String, Object> body = new HashMap<>();
		body.put("name", "Test Provider " + System.currentTimeMillis());
		body.put("url", "https://openid.example.com");
		body.put("status", true);
		body.put("clientId", "client-id-" + UUID.randomUUID());
		body.put("clientSecret", "super-secret-value");

		// Prepare the request
		RequestSpecification request = RestAssured.given().baseUri(baseUrl).cookie("orangehrm", cookieValue)
				.header("Content-Type", "application/json");

		// Add body
		if (body != null) {
			request.body(body);
		}

		// Send POST request
		Response response = request.post(endpoint).then().extract().response();

		// Print for debug
		System.out.println("POST Status Code: " + response.getStatusCode());
		System.out.println("POST Response Body: " + response.getBody().asString());

		// Assert status code
		Assert.assertEquals(response.getStatusCode(), 200, "Expected 201 Created status.");
	}

}
