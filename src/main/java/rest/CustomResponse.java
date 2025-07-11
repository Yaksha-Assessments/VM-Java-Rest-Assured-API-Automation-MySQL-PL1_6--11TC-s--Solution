package rest;

import java.util.List;
import java.util.Map;

import io.restassured.response.Response;

public class CustomResponse {
	private Response response;
	private int statusCode;
	private String status;
	// Variable of Test Case 1
	private List<Object> languages;
	private List<Object> dateFormat;
	// Variable of Test Case 2
	private List<Object> languageId;
	private List<Object> languageName;
	private List<Object> languageCode;
	// Variable of Test Case 3
	private List<Object> leaveAssignLeave;
	private List<Object> leaveLeaveList;
	private List<Object> leaveApplyLeave;
	private List<Object> leaveMyLeave;
	private List<Object> timeEmployeeTimesheet;
	private List<Object> timeMyTimesheet;
	// Variables of Test Case 4
	private List<Object> providerIds;
    private List<Object> providerNames;
    private List<Object> providerUrls;
    private List<Object> providerStatuses;
    private List<Object> clientIds;
    // Variables for Test Case 5
    private List<Object> ids;
    private List<Object> names;
    private List<Object> clientAdminIds;
    private List<Object> redirectUris;
    private List<Object> enabledFlags;
    private List<Object> confidentialFlags;
    // Variables For Test Case 6
    private List<Object> empNumbers;
    private List<Object> firstNames;
    private List<Object> lastNames;
    // Variables For Test Case 7
    private String adminLangId;
    private String adminLangName;
    private String adminLangCode;
    //Variables For Test Case 8
    private List<Object> deletedIds;
    // Variables For Test Case 9
    private Map<String, Object> moduleSettings;
    // Variable For Test Case 10
    private String name;
    private String url;
    private String clientId;
    private String clientSecret;
	private List<Object> employeeIds;


	
	public CustomResponse(Response response, int statusCode, String status,List<Object> languages, List<Object> dateFormat) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
		this.languages = languages;
		this.dateFormat = dateFormat;
	}
	
	
	public CustomResponse(Response response, int statusCode, String status,List<Object> languageId, List<Object> languageName , List<Object> languageCode) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
		this.languageId = languageId;
		this.languageName = languageName;
		this.languageCode = languageCode;
	}
	
	public CustomResponse(Response response, int statusCode, String status,List<Object> leaveAssignLeave,List<Object> leaveLeaveList,
			List<Object> leaveApplyLeave,List<Object> leaveMyLeave,List<Object> timeEmployeeTimesheet,List<Object> timeMyTimesheet) {
		
	    this.response = response;
	    this.statusCode = statusCode;
	    this.status = status;
	    this.leaveAssignLeave = leaveAssignLeave;
	    this.leaveLeaveList = leaveLeaveList;
	    this.leaveApplyLeave = leaveApplyLeave;
	    this.leaveMyLeave = leaveMyLeave;
	    this.timeEmployeeTimesheet = timeEmployeeTimesheet;
	    this.timeMyTimesheet = timeMyTimesheet;
	}
	
	public CustomResponse(Response response, int statusCode, String status,
            List<Object> providerIds, List<Object> providerNames,
            List<Object> providerUrls, List<Object> providerStatuses,
            List<Object> clientIds) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
		this.providerIds = providerIds;
		this.providerNames = providerNames;
		this.providerUrls = providerUrls;
		this.providerStatuses = providerStatuses;
		this.clientIds = clientIds;
		}
	
	
	public CustomResponse(Response response, int statusCode, String status, OAuthClientInfo clientInfo) {
	    this.response = response;
	    this.statusCode = statusCode;
	    this.status = status;
	    this.ids = clientInfo.getIds();
	    this.names = clientInfo.getNames();
	    this.clientAdminIds = clientInfo.getClientIds();
	    System.out.println("Cleint Id is : " + clientAdminIds);
	    this.redirectUris = clientInfo.getRedirectUris();
	    this.enabledFlags = clientInfo.getEnabledFlags();
	    this.confidentialFlags = clientInfo.getConfidentialFlags();
	}

	public CustomResponse(Response response,
            int statusCode,
            String status,List<Object> empNumbers, List<Object> firstNames,
            List<Object> lastNames,
            List<Object> employeeIds) {
			this.response = response;
			this.statusCode = statusCode;
			this.status = status;
			this.empNumbers = empNumbers;
			this.firstNames = firstNames;
			this.lastNames = lastNames;
			this.employeeIds = employeeIds;
			}
	
	
	public CustomResponse(Response response, int statusCode, String status,
            String adminLangId, String adminLangName, String adminLangCode) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
		this.adminLangId = adminLangId;
		this.adminLangName = adminLangName;
		this.adminLangCode = adminLangCode;
	}
	
	public CustomResponse(Response response, int statusCode, String status, List<Object> deletedIds) {
	    this.response = response;
	    this.statusCode = statusCode;
	    this.status = status;
	    this.deletedIds = deletedIds;
	}
	
	public CustomResponse(Response response, int statusCode, String status, Map<String, Object> moduleSettings) {
	    this.response = response;
	    this.statusCode = statusCode;
	    this.status = status;
	    this.moduleSettings = moduleSettings;
	}
	
	public CustomResponse(Response response, int statusCode, String status,
            String name, String url, String clientId, String clientSecret) {
		this.response = response;
		this.statusCode = statusCode;
		this.status = status;
		this.name = name;
		this.url = url;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	// Getting Responses , StatusCode , Status Line
	public Response getResponse() {
		return response;
	}
	
	public int getStatusCode() {
		return statusCode;
	}

	public String getStatus() {
		return status;
	}
	
	// Get Methods For Test Case 2
	public List<Object> getLanguages() {
		return languages;
	}

	public List<Object> getDateFormat() {
		return dateFormat;
	}
	
	// Get Methods For Test Case 2
	public List<Object> getLanguageId() {
		return languageId;
	}
	public List<Object> getLanguageName() {
		return languageName;
	}
	public List<Object> getLanguageCode() {
		return languageCode;
	}
		
	// Get Methods For Test Case 3	
	public List<Object> leave_Assign_Level() {
	    return leaveAssignLeave;
	}
	public List<Object> leave_Leave_List() {
	    return leaveLeaveList;
	}
	public List<Object> leave_Apply_Leave() {
	    return leaveApplyLeave;
	}
	public List<Object> leave_My_Leave() {
	    return leaveMyLeave;
	}
	public List<Object> time_Employee_Timesheet() {
	    return timeEmployeeTimesheet;
	}
	public List<Object> time_My_Timesheet() {
	    return timeMyTimesheet;
	}
	
	
	// Get Methods For Test Case 4
    public List<Object> getProviderIds() {
        return providerIds;
    }

    public List<Object> getProviderNames() {
        return providerNames;
    }

    public List<Object> getProviderUrls() {
        return providerUrls;
    }

    public List<Object> getProviderStatuses() {
        return providerStatuses;
    }

    public List<Object> getClientIds() {
        return clientIds;
    }
	
    // Get Methods For Test Case 5
    public List<Object> getIds() {
        return ids;
    }

    public List<Object> getNames() {
        return names;
    }

    public List<Object> getAdminIds() {
        return clientAdminIds;
    }

    public List<Object> getRedirectUris() {
        return redirectUris;
    }

    public List<Object> getEnabledFlags() {
        return enabledFlags;
    }

    public List<Object> getConfidentialFlags() {
        return confidentialFlags;
    }
	
    // Get Methods For Test Case 6
    public List<Object> getEmpNumbers() {
        return empNumbers;
    }

    public List<Object> getFirstNames() {
        return firstNames;
    }

    public List<Object> getLastNames() {
        return lastNames;
    }
    
    // Get Methods For Test Case 7
 	public String getAdminLangId(){
 		return adminLangId;
 	}
 	public String getAdminLangName() {
 		return adminLangName;
 	}
 	public String getAdminLangCode() {
 		return adminLangCode;
 	}
 	
    // Get Method For Test Case 8
    public List<Object> getDeletedIds() {
        return deletedIds;
    }
    
    // GEt Methods For Test Case 9 
    public Map<String, Object> getModuleSettings() {
        return moduleSettings;
    }
    
    // Get Methods For Test Case 10
    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

	
	/*----------------------------------- Helper Function --------------------------------------------*/
 
	
}