package sba301.java.opentalk.serverHrm.model;

public interface UserFromHRM {
    public String getFullName();
    public String getEmail();
    public String getUsername();
    public Boolean getIsEnabled();
    public Long getRole();
    public Long getCompanyBranch();
}
