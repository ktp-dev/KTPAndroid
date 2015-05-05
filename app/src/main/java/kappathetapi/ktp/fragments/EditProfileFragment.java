package kappathetapi.ktp.fragments;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import kappathetapi.ktp.R;
import kappathetapi.ktp.activities.HomePageActivity;
import kappathetapi.ktp.classes.Member;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {
    private Member member;
    private boolean isMemberSet = false;
    private View myView;
    private OnSaveListener mListener;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    public static EditProfileFragment createInstance(JSONObject memberJSON) {
        EditProfileFragment fragment = new EditProfileFragment();
        fragment.setMember(memberJSON);
        return fragment;
    }

    public static EditProfileFragment createInstance(Member member) {
        EditProfileFragment fragment = new EditProfileFragment();
        fragment.setMember(member);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(!isMemberSet) {
            loadMember();
        }

        myView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        Button saveChangesButton = (Button)(myView.findViewById(R.id._edit_profile_save_button));
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changesMade()) {
                    boolean success = updateMember();
                    if (success) {
                        mListener.updateThrough(member);
                    }
                }
            }
        });

        setHometown();
        setBio();
        setPhone();
        setEmail();
        setFacebook();
        setTwitter();

        return myView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSaveListener) activity;
            ((HomePageActivity) activity).onSectionAttached(getString(R.string.edit_profile));
        } catch(ClassCastException e) {
            Log.d("CLASSCAST", "editprofilefragment listener not implemented");
        }
    }

    public interface OnSaveListener {
        public void updateThrough(Member member);
    }

    public void setMember(JSONObject memberJSON) {
        member = Member.createInstance(memberJSON);
        isMemberSet = true;
    }

    public void setMember(Member member) {
        this.member = member;
        isMemberSet = true;
    }

    private void loadMember() {
        SharedPreferences prefs = getActivity().getSharedPreferences(getString(R.string.app_identifier), Activity.MODE_PRIVATE);
        String membString = prefs.getString(HomePageActivity.HOME_PAGE_LAST_CLICKED_MEMBER, "");
        try {
            JSONObject memberJSON = new JSONObject(membString);
            setMember(memberJSON);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean changesMade() {
        boolean changeMade = false;
        if(!((EditText)(myView.findViewById(R.id.edit_profile_hometown_input))).getText().toString().equals(member.getHometown())
                && !((EditText)(myView.findViewById(R.id.edit_profile_hometown_input))).getText().toString().equals("")) {
            member.setHometown(((EditText)(myView.findViewById(R.id.edit_profile_hometown_input))).getText().toString());
            changeMade = true;
        }
        if(!((EditText)(myView.findViewById(R.id.edit_profile_bio_input))).getText().toString().equals(member.getBiography())
                && !((EditText)(myView.findViewById(R.id.edit_profile_bio_input))).getText().toString().equals("")) {
            member.setBiography(((EditText) (myView.findViewById(R.id.edit_profile_bio_input))).getText().toString());
            changeMade = true;
        }
        if(!((EditText)(myView.findViewById(R.id.edit_profile_phone_input))).getText().toString().equals(member.getPhoneNumber())
                && !((EditText)(myView.findViewById(R.id.edit_profile_phone_input))).getText().toString().equals("")) {
            member.setPhoneNumber(((EditText) (myView.findViewById(R.id.edit_profile_phone_input))).getText().toString());
            changeMade = true;
        }
        if(!((EditText)(myView.findViewById(R.id.edit_profile_email_input))).getText().toString().equals(member.getEmail())
                && !((EditText)(myView.findViewById(R.id.edit_profile_email_input))).getText().toString().equals("")) {
            member.setEmail(((EditText) (myView.findViewById(R.id.edit_profile_email_input))).getText().toString());
            changeMade = true;
        }
        if(!((EditText)(myView.findViewById(R.id.edit_profile_facebook_input))).getText().toString().equals(member.getFacebook())
                && !((EditText)(myView.findViewById(R.id.edit_profile_facebook_input))).getText().toString().equals("")) {
            member.setFacebook(((EditText) (myView.findViewById(R.id.edit_profile_facebook_input))).getText().toString());
            changeMade = true;
        }
        if(!((EditText)(myView.findViewById(R.id.edit_profile_twitter_input))).getText().toString().equals(member.getTwitter())
                && !((EditText)(myView.findViewById(R.id.edit_profile_twitter_input))).getText().toString().equals("")) {
            member.setTwitter(((EditText) (myView.findViewById(R.id.edit_profile_twitter_input))).getText().toString());
            changeMade = true;
        }
        return changeMade;
    }

    private boolean updateMember() {
        return member.update(this.getActivity());
    }

    public void setHometown() {
        ((EditText)(myView.findViewById(R.id.edit_profile_hometown_input))).setText(member.getHometown());
    }

    public void setBio() {
        ((EditText)(myView.findViewById(R.id.edit_profile_bio_input))).setText(member.getBiography());
    }

    public void setPhone() {
        ((EditText)(myView.findViewById(R.id.edit_profile_phone_input))).setText(member.getPhoneNumber());
    }

    public void setEmail() {
        ((EditText)(myView.findViewById(R.id.edit_profile_email_input))).setText(member.getEmail());
    }

    public void setFacebook() {
        ((EditText)(myView.findViewById(R.id.edit_profile_facebook_input))).setText(member.getFacebook());
    }

    public void setTwitter() {
        ((EditText)(myView.findViewById(R.id.edit_profile_twitter_input))).setText(member.getTwitter());
    }

}