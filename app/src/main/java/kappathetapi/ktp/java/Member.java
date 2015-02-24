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

    public void setFirstName(JSONObject obj) {
        try {
            this.firstName = obj.getString("first_name");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLastName(JSONObject obj) {
        try {
            this.lastName = obj.getString("last_name");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUniqname() {
        return uniqname;
    }

    public void setUniqname(String uniqname) {
        this.uniqname = uniqname;
    }

    public void setUniqname(JSONObject obj) {
        try {
            this.uniqname = obj.getString("uniqname");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setYear(JSONObject obj) {
        try {
            this.year = obj.getInt("year");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setMajor(JSONObject obj) {
        try {
            this.major = obj.getString("major");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setGender(JSONObject obj) {
        try {
            this.gender = obj.getString("gender");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public void setHometown(JSONObject obj) {
        try {
            this.hometown = obj.getString("hometown");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setBiography(JSONObject obj) {
        try {
            this.biography = obj.getString("biography");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public String getProfPicUrl() {
        return profPicUrl;
    }

    public void setProfPicUrl(String profPicUrl) {
        this.profPicUrl = profPicUrl;
    }

    public void setProfPicUrl(JSONObject obj) {
        try {
            this.profPicUrl = obj.getString("prof_pic_url");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmail(JSONObject obj) {
        try {
            this.email = obj.getString("email");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPhoneNumber(JSONObject obj) {
        try {
            this.phoneNumber = obj.getString("phone_number");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public void setTwitter(JSONObject obj) {
        try {
            this.twitter = obj.getString("twitter");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setFacebook(JSONObject obj) {
        try {
            this.facebook = obj.getString("facebook");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public void setLinkedin(JSONObject obj) {
        try {
            this.linkedin = obj.getString("linkedin");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public String getPersonalSite() {
        return personalSite;
    }

    public void setPersonalSite(String personal_site) {
        this.personalSite = personalSite;
    }

    public void setPersonalSite(JSONObject obj) {
        try {
            this.personalSite = obj.getString("personal_site");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public String getPledgeClass() {
        return pledgeClass;
    }

    public void setPledgeClass(String pledgeClass) {
        this.pledgeClass = pledgeClass;
    }

    public void setPledgeClass(JSONObject obj) {
        try {
            this.uniqname = obj.getString("pledge_class");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public String getMembershipStatus() {
        return membershipStatus;
    }

    public void setMembershipStatus(String membershipStatus) {
        this.membershipStatus = membershipStatus;
    }

    public void setMembershipStatus(JSONObject obj) {
        try {
            this.membershipStatus = obj.getString("membership_status");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setRole(JSONObject obj) {
        try {
            this.role = obj.getString("role");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public static Member createInstance(JSONObject obj) {
        Member member = new Member();
        member.setFirstName(obj);
        member.setLastName(obj);
        member.setUniqname(obj);
        member.setYear(obj);
        member.setMajor(obj);
        member.setGender(obj);
        member.setHometown(obj);
        member.setBiography(obj);
        member.setProfPicUrl(obj);
        member.setEmail(obj);
        member.setPhoneNumber(obj);
        member.setTwitter(obj);
        member.setFacebook(obj);
        member.setLinkedin(obj);
        member.setPersonalSite(obj);
        member.setPledgeClass(obj);
        member.setMembershipStatus(obj);
        member.setRole(obj);

        return member;
    }
}
