package net.minheur.potoflux.modGen.data;

public class ModData {
    private String modName;
    private String modLicence;
    private String modInitialVersion;
    private String modAuthor;
    private String modCredits;
    private String modDesc;
    private String issueURL;
    private String updateJSONURL;
    private String displayURL;

    public void setModName(String modName) {
        this.modName = modName;
    }
    public void setModLicence(String modLicence) {
        this.modLicence = modLicence;
    }
    public void setModInitialVersion(String modInitialVersion) {
        this.modInitialVersion = modInitialVersion;
    }
    public void setModAuthor(String modAuthor) {
        this.modAuthor = modAuthor;
    }
    public void setModCredits(String modCredits) {
        this.modCredits = modCredits;
    }
    public void setModDesc(String modDesc) {
        this.modDesc = modDesc;
    }
    public void setIssueURL(String issueURL) {
        this.issueURL = issueURL;
    }
    public void setUpdateJSONURL(String updateJSONURL) {
        this.updateJSONURL = updateJSONURL;
    }
    public void setDisplayURL(String displayURL) {
        this.displayURL = displayURL;
    }

    public String getModName() {
        return modName;
    }
    public String getModLicence() {
        return modLicence;
    }
    public String getModInitialVersion() {
        return modInitialVersion;
    }
    public String getModAuthor() {
        return modAuthor;
    }
    public String getModCredits() {
        return modCredits;
    }
    public String getModDesc() {
        return modDesc;
    }
    public String getIssueURL() {
        return issueURL;
    }
    public String getUpdateJSONURL() {
        return updateJSONURL;
    }
    public String getDisplayURL() {
        return displayURL;
    }
}
