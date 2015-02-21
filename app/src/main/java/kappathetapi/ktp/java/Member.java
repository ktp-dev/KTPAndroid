package kappathetapi.ktp.java;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sjdallst on 2/21/2015.
 */
public class Member {

    Member() {
    }
    // personal info
    private String firstName;
    private String lastName;
    private String uniqname;
    private int year;     // graduation year
    private String major;
    private String gender;         // {M,F}
    private String hometown;
    private String biography;
    private String profPicUrl;

    // contact info
    private String email;          // default is umich email
    private String phoneNumber;

    // sites and links
    private String twitter;        // username
    private String facebook;       // username
    private String linkedin;       // username
    private String personalSite;  // full link

    // fraternity info
    private String pledgeClass;       // {Alpha,Beta,Gamma,Delta,Epsilon,Zeta,Eta}
    private String membershipStatus;  // {Active,Probation,Inactive,Eboard,Pledge}
    private String role;               // {Member,Pledge,President,Secretary,Director of Membership, ...}
    //private String[] committees: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Committee' }],


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUniqname() {
        return uniqname;
    }

    public void setUniqname(String uniqname) {
        this.uniqname = uniqname;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getProfPicUrl() {
        return profPicUrl;
    }

    public void setProfPicUrl(String profPicUrl) {
        this.profPicUrl = profPicUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getPersonalSite() {
        return personalSite;
    }

    public void setPersonalSite(String personal_site) {
        this.personalSite = personalSite;
    }

    public String getPledgeClass() {
        return pledgeClass;
    }

    public void setPledgeClass(String pledgeClass) {
        this.pledgeClass = pledgeClass;
    }

    public String getMembershipStatus() {
        return membershipStatus;
    }

    public void setMembershipStatus(String membershipStatus) {
        this.membershipStatus = membershipStatus;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static Member createInstance(JSONObject obj) {
        Member member = new Member();
        try {
            member.setFirstName(obj.getString("first_name"));
            member.setLastName(obj.getString("last_name"));
            member.setUniqname(obj.getString("uniqname"));
            member.setYear(obj.getInt("year"));
            member.setMajor(obj.getString("major"));
            member.setGender(obj.getString("gender"));
            member.setHometown(obj.getString("hometown"));
            member.setBiography(obj.getString("biography"));
            member.setProfPicUrl(obj.getString("prof_pic_url"));
            member.setEmail(obj.getString("email"));
            member.setPhoneNumber(obj.getString("phone_number"));
            member.setTwitter(obj.getString("twitter"));
            member.setFacebook(obj.getString("facebook"));
            member.setLinkedin(obj.getString("linkedin"));
            member.setPersonalSite(obj.getString("personal_site"));
            member.setPledgeClass(obj.getString("pledge_class"));
            member.setMembershipStatus(obj.getString("membership_status"));
            member.setRole(obj.getString("role"));
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return member;
    }
}
