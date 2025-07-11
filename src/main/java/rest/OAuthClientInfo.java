package rest;

import java.util.List;

public class OAuthClientInfo {
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
    private List<Object> middleNames;
    private List<Object> employeeIds;

    

    public OAuthClientInfo(List<Object> ids, List<Object> names,
                           List<Object> clientAdminIds, List<Object> redirectUris,
                           List<Object> enabledFlags, List<Object> confidentialFlags) {
        this.ids = ids;
        this.names = names;
        this.clientAdminIds = clientAdminIds;
        System.out.println("Cleint Id is : " + this.clientAdminIds);
        this.redirectUris = redirectUris;
        this.enabledFlags = enabledFlags;
        this.confidentialFlags = confidentialFlags;
    };
    
    
    

    
    
    public List<Object> getIds() {
        return ids;
    }

    public List<Object> getNames() {
        return names;
    }

    public List<Object> getClientIds() {
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
    
    // Test Case 6 Medthods
    
    

}
