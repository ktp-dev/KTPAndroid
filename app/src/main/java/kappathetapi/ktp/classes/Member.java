package kappathetapi.ktp.classes;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.bson.types.ObjectId;

import java.util.Comparator;

import kappathetapi.ktp.R;
import kappathetapi.ktp.tasks.MembersRequest;

/**
 * Created by sjdallst on 2/21/2015.
 */
public class Member {

    public Member() {
    }
    // personal info
    private String firstName = "";
    private String lastName = "";
    private String uniqname = "";
    private int year = -1;     // graduation year
    private String major = "";
    private String gender = "";         // {M,F}
    private String hometown = "";
    private String biography = "";
    private String profPicUrl = "";
    private String account = "";
    private String id = "";
    private String v = "";

    // contact info
    private String email = "";          // default is umich email
    private String phoneNumber = "";

    // sites and links
    private String twitter = "";        // username
    private String facebook = "";       // username
    private String linkedin = "";       // username
    private String personalSite = "";  // full link

    // fraternity info
    private String pledgeClass = "";       // {Alpha,Beta,Gamma,Delta,Epsilon,Zeta,Eta}
    private String membershipStatus = "";  // {Active,Probation,Inactive,Eboard,Pledge}
    private String role = "";               // {Member,Pledge,President,Secretary,Director of Membership, ...}
    private JSONArray committees = null; //committees: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Committee' }],

    // membership requirements
    private double serviceHours = -1;
    private int proDevEvents = -1;

    // pledge/active meetings
    private JSONArray meetings = null; // meetings: [{ type: mongoose.Schema.Types.ObjectId, ref: 'PledgeMeeting' }]

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
           //e.printStackTrace();
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
            //e.printStackTrace();
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
            //e.printStackTrace();
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
            //e.printStackTrace();
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
            //e.printStackTrace();
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
            //e.printStackTrace();
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
            //e.printStackTrace();
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
            //e.printStackTrace();
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
            //e.printStackTrace();
        }
    }

    public String getAccount() {
        return account;
    }

    private void setAccount(String account) {
        this.account = account;
    }

    public void setAccount(JSONObject obj) {
        try {
            this.account = obj.getString("account");
        } catch (JSONException e) {

        }
    }

    public String getId() { return id; }

    private void setId(String id) {
        this.id = id;
    }

    public void setId(JSONObject obj) {
        try {
            id = obj.getString("_id");
        } catch (JSONException e) {

        }
    }

    public String getV() { return v; }

    private void setV(String v) {
        this.v = v;
    }

    public void setV(JSONObject obj) {
        try {
            v = obj.getString("__v");
        } catch(JSONException e) {

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
            //e.printStackTrace();
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
            //e.printStackTrace();
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
            //e.printStackTrace();
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
            //e.printStackTrace();
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
            //e.printStackTrace();
        }
    }

    public String getPersonalSite() {
        return personalSite;
    }

    public void setPersonalSite(String personalSite) {
        this.personalSite = personalSite;
    }

    public void setPersonalSite(JSONObject obj) {
        try {
            this.personalSite = obj.getString("personal_site");
        } catch(JSONException e) {
            //e.printStackTrace();
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
            this.pledgeClass = obj.getString("pledge_class");
        } catch(JSONException e) {
            //e.printStackTrace();
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
            //e.printStackTrace();
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
            //e.printStackTrace();
        }
    }

    public JSONArray getCommittees() {
        return committees;
    }

    public void setCommittees(JSONArray committees) {
        this.committees = committees;
    }

    public void setCommittees(JSONObject obj) {
        try {
            this.committees = (JSONArray)(obj.get("committees"));
        } catch (JSONException e) {
            //e.printStackTrace();
        }
    }

    public double getServiceHours() {
        return serviceHours;
    }

    public void setServiceHours(double hours) {
        serviceHours = hours;
    }

    public void setServiceHours(JSONObject json) {
        try {
            serviceHours = json.getDouble("service_hours");
        } catch (JSONException e) {
            //e.printStackTrace();
        }
    }

    public int getProDevEvents() {
        return proDevEvents;
    }

    public void setProDevEvents(int eventsNum) {
        proDevEvents = eventsNum;
    }

    public void setProDevEvents(JSONObject json) {
        try {
            proDevEvents = json.getInt("pro_dev_events");
        } catch (JSONException e) {
            //e.printStackTrace();
        }
    }

    public JSONArray getMeetings() {
        return meetings;
    }

    public void setMeetings(JSONArray meetings) {
        this.meetings = meetings;
    }

    public void setMeetings(JSONObject obj) {
        try {
            meetings = (JSONArray)(obj.get("meetings"));
        } catch(JSONException e) {

        }
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("first_name", getFirstName());
            json.put("last_name", getLastName());
            json.put("uniqname", getUniqname());
            json.put("year", getYear());
            json.put("major", getMajor());
            json.put("gender", getMajor());
            json.put("hometown", getHometown());
            json.put("biography", getBiography());
            json.put("prof_pic_url", getProfPicUrl());
            json.put("email", getEmail());
            json.put("phone_number", getPhoneNumber());
            json.put("twitter", getTwitter());
            json.put("facebook", getFacebook());
            json.put("linkedin", getLinkedin());
            json.put("personal_site", getPersonalSite());
            json.put("pledge_class", getPledgeClass());
            json.put("membership_status", getMembershipStatus());
            json.put("role", getRole());
            json.put("service_hours", getServiceHours());
            json.put("pro_dev_events", getProDevEvents());
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static Member createInstance(JSONObject obj) {
        Member member = new Member();
        member.setFields(obj);
        return member;
    }

    public void setFields(JSONObject obj) {
        setFirstName(obj);
        setLastName(obj);
        setUniqname(obj);
        setYear(obj);
        setMajor(obj);
        setGender(obj);
        setHometown(obj);
        setBiography(obj);
        setProfPicUrl(obj);
        setAccount(obj);
        setId(obj);
        setV(obj);
        setEmail(obj);
        setPhoneNumber(obj);
        setTwitter(obj);
        setFacebook(obj);
        setLinkedin(obj);
        setPersonalSite(obj);
        setPledgeClass(obj);
        setMembershipStatus(obj);
        setRole(obj);
        setCommittees(obj);
        setServiceHours(obj);
        setProDevEvents(obj);
        setMeetings(obj);
    }

    public boolean update(Activity currActivity) {
        MembersRequest membersRequest = new MembersRequest();
        Log.d("JSON", toJSON().toString());
        String response = membersRequest.getResponse(currActivity.getString(R.string.server_address),
                MembersRequest.RequestPath.MEMBERS, MembersRequest.RequestType.PUT, toJSON(), getId());
        try {
            JSONObject jsonObject = new JSONObject(response);
            setFields(jsonObject);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    public boolean delete(Activity currActivity) {
        MembersRequest membersRequest = new MembersRequest();
        membersRequest.getResponse(currActivity.getString(R.string.server_address),
                MembersRequest.RequestPath.MEMBERS, MembersRequest.RequestType.DELETE, toJSON(), getId());
        return true;
    }

    public void copy(Member member) {
        setFirstName(member.getFirstName());
        setLastName(member.getLastName());
        setUniqname(member.getUniqname());
        setYear(member.getYear());
        setMajor(member.getMajor());
        setGender(member.getGender());
        setHometown(member.getHometown());
        setBiography(member.getBiography());
        setProfPicUrl(member.getProfPicUrl());
        setAccount(member.getAccount());
        setId(member.getId());
        setV(member.getV());
        setEmail(member.getEmail());
        setPhoneNumber(member.getPhoneNumber());
        setTwitter(member.getTwitter());
        setFacebook(member.getFacebook());
        setLinkedin(member.getLinkedin());
        setPersonalSite(member.getPersonalSite());
        setPledgeClass(member.getPledgeClass());
        setMembershipStatus(member.getMembershipStatus());
        setRole(member.getRole());
        setCommittees(member.getCommittees());
        setServiceHours(member.getServiceHours());
        setProDevEvents(member.getProDevEvents());
        setMeetings(member.getMeetings());
    }

    //Comparators for the sortings :DDDDDD
    public static class MemberNameComparator implements Comparator<Member> {

        @Override
        public int compare(Member lhs, Member rhs) {
            if(lhs.getUniqname() == null || lhs.getFirstName() == null || lhs.getLastName() == null) {
                return -1;
            }
            if(rhs.getUniqname() == null || rhs.getFirstName() == null || rhs.getLastName() == null) {
                return 1;
            }
            return (lhs.getLastName() + lhs.getFirstName() + lhs.getUniqname()).compareToIgnoreCase(
                    (rhs.getLastName() + rhs.getFirstName() + rhs.getUniqname()));
        }
    }

    public static class MemberNameComparatorR implements Comparator<Member> {

        @Override
        public int compare(Member lhs, Member rhs) {
            if(lhs.getUniqname() == null || lhs.getFirstName() == null || lhs.getLastName() == null) {
                return -1;
            }
            if(rhs.getUniqname() == null || rhs.getFirstName() == null || rhs.getLastName() == null) {
                return 1;
            }
            return (rhs.getLastName() + rhs.getFirstName() + rhs.getUniqname()).compareToIgnoreCase(
                    (lhs.getLastName() + lhs.getFirstName() + lhs.getUniqname()));
        }
    }
}
