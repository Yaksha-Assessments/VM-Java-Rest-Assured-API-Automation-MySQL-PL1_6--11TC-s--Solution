package testcases;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rest.CustomResponse;

public class TestCodeValidator {

	// Method to validate if specific keywords are used in the method's source code
	public static boolean validateTestMethodFromFile(String filePath, String methodName, List<String> keywords)
			throws IOException {
		// Read the content of the test class file
		String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));

		// Extract the method body for the specified method using regex
		String methodRegex = "(public\\s+CustomResponse\\s+" + methodName + "\\s*\\(.*?\\)\\s*\\{)([\\s\\S]*?)}";
		Pattern methodPattern = Pattern.compile(methodRegex);
		Matcher methodMatcher = methodPattern.matcher(fileContent);

		if (methodMatcher.find()) {

			String methodBody = fetchBody(filePath, methodName);

			// Now we validate the method body for the required keywords
			boolean allKeywordsPresent = true;

			// Loop over the provided keywords and check if each one is present in the
			// method body
			for (String keyword : keywords) {
				Pattern keywordPattern = Pattern.compile("\\b" + keyword + "\\s*\\(");
				if (!keywordPattern.matcher(methodBody).find()) {
					System.out.println("'" + keyword + "()' is missing in the method.");
					allKeywordsPresent = false;
				}
			}

			return allKeywordsPresent;

		} else {
			System.out.println("Method " + methodName + " not found in the file.");
			return false;
		}
	}

	// This method takes the method name as an argument and returns its body as a
	// String.
	public static String fetchBody(String filePath, String methodName) {
		StringBuilder methodBody = new StringBuilder();
		boolean methodFound = false;
		boolean inMethodBody = false;
		int openBracesCount = 0;

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				// Check if the method is found by matching method signature
				if (line.contains("public CustomResponse " + methodName + "(")
						|| line.contains("public String " + methodName + "(")) {
					methodFound = true;
				}

				// Once the method is found, start capturing lines
				if (methodFound) {
					if (line.contains("{")) {
						inMethodBody = true;
						openBracesCount++;
					}

					// Capture the method body
					if (inMethodBody) {
						methodBody.append(line).append("\n");
					}

					// Check for closing braces to identify the end of the method
					if (line.contains("}")) {
						openBracesCount--;
						if (openBracesCount == 0) {
							break; // End of method body
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return methodBody.toString();
	}
	
	public static boolean validateResponseFields(String methodName, CustomResponse customResponse) {
		boolean isValid = true;

		switch (methodName)
		{
		case "getLocalizationValid":
			// Define the expected fields within the 'data' section of the response
		    List<String> expectedLocalFields = List.of("language", "dateFormat"); // ✅ fixed typo

		    // Extract the 'data' map from the customResponse JSON
		    Map<String, Object> localizationData = customResponse.getResponse().jsonPath().getMap("data");

		 // Check if the 'data' section is missing or empty
		    if (localizationData == null || localizationData.isEmpty()) {
		        isValid = false;
		        System.out.println("'data' section is missing or empty in the response.");
		        break;
		    }

		    // Loop through each expected field to verify presence and non-null values
		    for (String field : expectedLocalFields) {
		    	
		    	// If the field is missing in the 'data' section
		        if (!localizationData.containsKey(field)) {
		            isValid = false;
		            System.out.println("Missing field in 'data': " + field);
		        } else {
		            Object value = localizationData.get(field);
		            if (value == null || value.toString().trim().isEmpty()) {
		                isValid = false;
		                System.out.println("Field '" + field + "' is null or empty.");
		            }
		        }
		    }

		    break;
		
		    
		case "getActiveLanguages":
			// Define the expected fields within the 'data' section of the response
		    List<String> expectedLanguageFields = List.of("id", "name", "code");

		    // Extract the 'data' map from the customResponse JSON
		    List<Map<String, Object>> languageData = customResponse.getResponse().jsonPath().getList("data");

		 // Check if the 'data' section is missing or empty
		    if (languageData == null || languageData.isEmpty()) {
		        isValid = false;
		        System.out.println("'data' array is missing or empty in the response.");
		        break;
		    }

		    // Loop through each expected field to verify presence and non-null values
		    for (int i = 0; i < languageData.size(); i++) {
		        Map<String, Object> langEntry = languageData.get(i);

		        for (String field : expectedLanguageFields) {
		        	
		        	// If the field is missing in the 'data' section
		            if (!langEntry.containsKey(field)) {
		                isValid = false;
		                System.out.println("Missing field '" + field + "' in entry at index " + i);
		            } else {
		                Object value = langEntry.get(field);
		                if (value == null || value.toString().trim().isEmpty()) {
		                    isValid = false;
		                    System.out.println("Field '" + field + "' is null or empty at index " + i);
		                }
		            }
		        }
		    }

		    break;
			    
		    
			case "getDashboardShortcuts":

				// Define the expected fields within the 'data' section of the response
	            List<String> expectedDashboardFields = List.of(
	            "leave.assign_leave",
	                "leave.leave_list",
	                "leave.apply_leave",
	                "leave.my_leave",
	                "time.employee_timesheet",
	                "time.my_timesheet"
	            );

	            // Get the data section from response
	            Map<String, Object> dataMap = customResponse.getResponse().jsonPath().getMap("data");

	            // Check if the 'data' section is missing or empty
	            if (dataMap == null || dataMap.isEmpty()) {
	                System.out.println("❌ 'data' section is missing or empty.");
	                return false;
	            }

			    // Loop through each expected field to verify presence and non-null values
	            for (String field : expectedDashboardFields) {
	            	
	            	// If the field is missing in the 'data' section
	                if (!dataMap.containsKey(field)) {
	                    System.out.println("❌ Missing expected field: " + field);
	                    isValid = false;
	                } else if (dataMap.get(field) == null) {
	                    System.out.println("❌ Field '" + field + "' is present but null.");
	                    isValid = false;
	                }
	            }

	            break;
	            
			case "getAuthProviders":

			    // Define expected fields inside each item of the "data" array
			    List<String> expectedProviderFields = List.of(
			        "id",
			        "providerName",
			        "providerUrl",
			        "status",
			        "clientId"
			    );

			    // Extract the 'data' map from the customResponse JSON
			    List<Map<String, Object>> providersList = customResponse.getResponse().jsonPath().getList("data");

			    // Check if the 'data' section is missing or empty
			    if (providersList == null || providersList.isEmpty()) {
			        System.out.println("❌ 'data' array is missing or empty.");
			        return false;
			    }

			    // Loop through each expected field to verify presence and non-null values
			    for (int i = 0; i < providersList.size(); i++) {
			        Map<String, Object> provider = providersList.get(i);
			        for (String field : expectedProviderFields) {
			        	
			        	// If the field is missing in the 'data' section
			            if (!provider.containsKey(field)) {
			                System.out.println("❌ Missing field '" + field + "' in provider at index " + i);
			                isValid = false;
			            } else if (provider.get(field) == null) {
			                System.out.println("❌ Field '" + field + "' is null in provider at index " + i);
			                isValid = false;
			            }
			        }
			    }

			    break;
			    
			case "getAdminOAuthList":
				// Define the expected fields within the 'data' section of the response
			    List<String> expectedClientFields = List.of(
			        "id",
			        "name",
			        "clientId",
			        "redirectUri",
			        "enabled",
			        "confidential"
			    );

			    // Extract the 'data' map from the customResponse JSON
			    List<Map<String, Object>> clients = customResponse.getResponse().jsonPath().getList("data");

			    // Check if the 'data' section is missing or empty
			    if (clients == null || clients.isEmpty()) {
			        System.out.println("❌ 'data' array is missing or empty.");
			        return false;
			    }

			    // Loop through each expected field to verify presence and non-null values
			    for (int i = 0; i < clients.size(); i++) {
			        Map<String, Object> client = clients.get(i);
			        for (String field : expectedClientFields) {
			        	
			        	// If the field is missing in the 'data' section
			            if (!client.containsKey(field)) {
			                System.out.println("❌ Missing field '" + field + "' in client at index " + i);
			                isValid = false;
			            } else if (client.get(field) == null) {
			                System.out.println("❌ Field '" + field + "' is null in client at index " + i);
			                isValid = false;
			            }
			        }
			    }

			    break;
			  
			case "getEmployeeList":
				
				// Define the expected fields within the 'data' section of the response
			    List<String> expectedEmployeeFields = List.of(
			            "empNumber",
			            "firstName",
			            "lastName",
			            "middleName"
			        );
			    // Extract the 'data' map from the customResponse JSON
		        List<Map<String, Object>> employees = customResponse.getResponse().jsonPath().getList("data");
	
			    // Check if the 'data' section is missing or empty
		        if (employees == null || employees.isEmpty()) {
		            System.out.println("❌ 'data' array is missing or empty.");
		            return false;
		        }

			    // Loop through each expected field to verify presence and non-null values
		        for (int i = 0; i < employees.size(); i++) {
		            Map<String, Object> emp = employees.get(i);
		            for (String field : expectedEmployeeFields) {
		            	
		            	// If the field is missing in the 'data' section
		                if (!emp.containsKey(field)) {
		                    System.out.println("❌ Missing field '" + field + "' in employee at index " + i);
		                    isValid = false;
		                } else if (emp.get(field) == null) {
		                    System.out.println("❌ Field '" + field + "' is null in employee at index " + i);
		                    isValid = false;
		                }
		            }
		        }

		        break;
			        
			case "putLanguagePackage":
				
				// Define the expected fields within the 'data' section of the response
			    List<String> expectedAdminLanguageFields = List.of("id", "name", "code");

			    // Extract the 'data' map from the customResponse JSON
			    Map<String, Object> adminLangEntry = customResponse.getResponse().jsonPath().getMap("data");

			    // Check if the 'data' section is missing or empty
			    if (adminLangEntry == null || adminLangEntry.isEmpty()) {
			        isValid = false;
			        System.out.println("❌ 'data' object is missing or empty in the response.");
			        break;
			    }

			    // Loop through each expected field to verify presence and non-null values
			    for (String field : expectedAdminLanguageFields) {
			    	
			    	// If the field is missing in the 'data' section
			        if (!adminLangEntry.containsKey(field)) {
			            isValid = false;
			            System.out.println("❌ Missing field '" + field + "' in the 'data' object.");
			        } else {
			            Object value = adminLangEntry.get(field);
			            if (value == null || value.toString().trim().isEmpty()) {
			                isValid = false;
			                System.out.println("❌ Field '" + field + "' is null or empty in the 'data' object.");
			            }
			        }
			    }

			    break;
			    
			case "deleteLangById":
				// Define the expected fields within the 'data' section of the response
			    List<Object> deletedIds = customResponse.getResponse().jsonPath().getList("data");

			    // Check if the 'data' section is missing or empty
			    if (deletedIds == null || deletedIds.isEmpty()) {
			        System.out.println("❌ 'data' array is missing or empty.");
			        isValid = false;
			        break;
			    }

			    // Loop through each expected field to verify presence and non-null values
			    for (int i = 0; i < deletedIds.size(); i++) {
			        Object idVal = deletedIds.get(i);
			        
			     // If the field is missing in the 'data' section
			        if (idVal == null || idVal.toString().trim().isEmpty()) {
			            System.out.println("❌ Deleted ID at index " + i + " is null or empty.");
			            isValid = false;
			        }
			    }
			    
			    break;
			    
			case "putModulesSettings":
				// Define the expected fields within the 'data' section of the response
			    Map<String, Object> settings = customResponse.getResponse().jsonPath().getMap("data");

			    // Check if the 'data' section is missing or empty
			    if (settings == null || settings.isEmpty()) {
			        System.out.println("❌ 'data' map is missing or empty.");
			        isValid = false;
			        break;
			    }

			    List<String> expectedBooleanModules = List.of(
			        "admin", "buzz", "claim", "directory", "leave",
			        "maintenance", "mobile", "performance", "pim", "recruitment", "time"
			    );

			    // Loop through each expected field to verify presence and non-null values
			    for (String module : expectedBooleanModules) {
			    	
			    	// If the field is missing in the 'data' section
			        if (!settings.containsKey(module)) {
			            System.out.println("❌ Missing module: " + module);
			            isValid = false;
			        } else if (!(settings.get(module) instanceof Boolean)) {
			            System.out.println("❌ Module '" + module + "' should be a boolean value.");
			            isValid = false;
			        }
			    }
			    break;
			    
			    
			case "postOpenIdProvide":
				// Define the expected fields within the 'data' section of the response
			    List<String> expectedFields = List.of("providerName", "providerUrl", "clientId");

			    // Extract the 'data' map from the customResponse JSON
			    Map<String, Object> openIdData = customResponse.getResponse().jsonPath().getMap("data");

			    // Check if the 'data' section is missing or empty
			    if (openIdData == null || openIdData.isEmpty()) {
			        isValid = false;
			        System.out.println("❌ 'data' object is missing or empty.");
			        break;
			    }
			    
			    // Loop through each expected field to verify presence and non-null values
			    for (String field : expectedFields) {
			    	
			    	// If the field is missing in the 'data' section
			        if (!openIdData.containsKey(field)) {
			            System.out.println("❌ Missing field: " + field);
			            isValid = false;
			        } else {
			            Object value = openIdData.get(field);
			            if (value == null || value.toString().trim().isEmpty()) {
			                System.out.println("❌ Field '" + field + "' is null or empty.");
			                isValid = false;
			            }
			        }
			    }
			    
			    break;

		default:
			System.out.println("Method " + methodName + " is not recognized for validation.");
			isValid = false;
		}
		return isValid;
	}
	
}