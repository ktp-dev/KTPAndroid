package kappathetapi.ktp.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import kappathetapi.ktp.R;
import kappathetapi.ktp.activities.HomePageActivity;
import kappathetapi.ktp.classes.Member;
import kappathetapi.ktp.classes.gifhelpers.ImageViewGIF;
import kappathetapi.ktp.tasks.PhotoRequest;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MemberProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MemberProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberProfileFragment extends Fragment {
    private Member member;
    private Bitmap bmp;
    private View myView;
    private OnFragmentInteractionListener mListener;
    private boolean isMemberSet = false, showSaveButton = false;
    double oldServiceHours = 0;
    int oldProDevEvents = 0;

    public static MemberProfileFragment newInstance(JSONObject memberJSON) {
        MemberProfileFragment fragment = new MemberProfileFragment();
        fragment.setMember(memberJSON);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static MemberProfileFragment newInstance(Member member) {
        MemberProfileFragment fragment = new MemberProfileFragment();
        fragment.setMember(member);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MemberProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(!isMemberSet) {
            loadMember();
        }
        myView = inflater.inflate(R.layout.fragment_member_profile, container, false);

        setNameText();
        setYearText();
        setMajorText();
        setRoleText();
        setPledgeClassText();
        setHometownText();
        setBioText();
        setMembershipText();
        setServiceHoursText();
        setProDevEventsText();
        setPersonalSiteText();

        setDeleteButton();
        setSaveButton();

        return myView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Gets the photo from the member's URL then sets it in the view.
        //Must be in onStart because it needs access to the parent activity's runOnUIThread method
        PhotoRequest request = new PhotoRequest();
        request.getPicModifyView(getString(R.string.server_address) + member.getProfPicUrl(),
                getActivity(), (ImageViewGIF) (myView.findViewById(R.id.profile_pic_view)));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onDeletePressed();
        public void onSavePressed(Member member);
    }

    public void setMember(JSONObject json) {
        member = Member.createInstance(json);
        isMemberSet = true;
    }

    public void setMember(Member member) {
        this.member = member;
        this.isMemberSet = true;
    }

    private void setNameText() {
        ((TextView)(myView.findViewById(R.id.profile_name))).setText(member.getFirstName() + " " + member.getLastName());
    }

    private void setYearText() {
        ((TextView)(myView.findViewById(R.id.profile_year))).setText(String.valueOf(member.getYear()));
    }

    private void setMajorText() {
        ((TextView)(myView.findViewById(R.id.profile_major))).setText(member.getMajor());
    }

    private void setRoleText() {
        ((TextView)(myView.findViewById(R.id.profile_role))).setText(member.getRole());
    }

    private void setPledgeClassText() {
        ((TextView)(myView.findViewById(R.id.profile_pledge_class))).setText("Pledge Class: " +
                member.getPledgeClass());
    }

    private void setHometownText() {
        ((TextView)(myView.findViewById(R.id.profile_hometown))).setText("Hometown: " +
                member.getHometown());
    }

    private void setBioText() {
        if(member.getBiography() == null || member.getBiography().compareTo("") == 0) {
            ((TextView)(myView.findViewById(R.id.profile_bio))).setText("");
        } else {
            ((TextView) (myView.findViewById(R.id.profile_bio))).setText("Bio: " + member.getBiography());
        }
    }

    //Only visible to self and EBoard
    private void setMembershipText() {
        if(member.getUniqname().equals(((HomePageActivity)getActivity()).currentMember.getUniqname())
                || ((HomePageActivity)getActivity()).currentMember.getMembershipStatus().equals("Eboard")) {
            ((TextView) (myView.findViewById(R.id.profile_membership_status))).setText("Membership Status: " +
                    member.getMembershipStatus());
        } else {
            ((TextView) (myView.findViewById(R.id.profile_membership_status))).setVisibility(View.GONE);
        }
    }

    //Only visible to self and EBoard
    private void setServiceHoursText() {
        if(member.getUniqname().equals(((HomePageActivity)getActivity()).currentMember.getUniqname())
                || ((HomePageActivity)getActivity()).currentMember.getMembershipStatus().equals("Eboard")) {
            ((TextView) (myView.findViewById(R.id.profile_service_hours))).setText("Service Hours: ");
            EditText editText = ((EditText)(myView.findViewById(R.id.profile_service_hours_edit)));
            editText.setText(Double.toString(member.getServiceHours()));
        } else {
            ((TextView) (myView.findViewById(R.id.profile_service_hours))).setVisibility(View.GONE);
            myView.findViewById(R.id.profile_service_hours_edit).setVisibility(View.GONE);
        }
    }

    //Only visible to self and EBoard
    private void setProDevEventsText() {
        if(member.getUniqname().equals(((HomePageActivity)getActivity()).currentMember.getUniqname())
                ||((HomePageActivity)getActivity()).currentMember.getMembershipStatus().equals("Eboard")) {
            ((TextView) (myView.findViewById(R.id.profile_pro_dev_events))).setText("ProDev Events: ");
            EditText editText = (EditText)(myView.findViewById(R.id.profile_pro_dev_events_edit));
            editText.setText(Integer.toString(member.getProDevEvents()));
        } else {
            ((TextView) (myView.findViewById(R.id.profile_pro_dev_events))).setVisibility(View.GONE);
            myView.findViewById(R.id.profile_pro_dev_events_edit).setVisibility(View.GONE);
        }
    }

    private void setPersonalSiteText() {
        ((TextView)(myView.findViewById(R.id.profile_personal_site))).setText("Personal Site: " +
                member.getPersonalSite());
    }

    private void setDeleteButton() {
        if(((HomePageActivity)getActivity()).currentMember.getMembershipStatus().equals("Eboard")) {
            ((Button)(myView.findViewById(R.id.prof_delete_button))).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onDeletePressed();
                }
            });
        } else {
            (myView.findViewById(R.id.prof_delete_button)).setVisibility(View.GONE);
        }
    }

    private void setSaveButton() {
        if(((HomePageActivity)getActivity()).currentMember.getMembershipStatus().equals("Eboard")) {
            ((myView.findViewById(R.id.prof_save_button))).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(hasChanges()) {
                        boolean success = member.update(getActivity());
                        if (success) {
                            mListener.onSavePressed(member);
                        } else {
                            member.setProDevEvents(oldProDevEvents);
                            member.setServiceHours(oldServiceHours);
                        }
                    }
                }
            });
        } else {
            (myView.findViewById(R.id.prof_save_button)).setVisibility(View.GONE);
        }
    }

    private boolean hasChanges() {
        boolean changeMade = false;
        try {
            Double serviceHours = Double.valueOf(((EditText)(myView.findViewById(R.id.profile_service_hours_edit))).getText().toString());
            if(serviceHours != member.getServiceHours()) {
                oldServiceHours = member.getServiceHours();
                member.setServiceHours(serviceHours);
                changeMade = true;
            }
        } catch(NumberFormatException e) {
            e.printStackTrace();
        }
        try {
            Integer proDevEvents = Integer.valueOf(((EditText)(myView.findViewById(R.id.profile_pro_dev_events_edit))).getText().toString());
            if(proDevEvents != member.getProDevEvents()) {
                oldProDevEvents = member.getProDevEvents();
                member.setProDevEvents(proDevEvents);
                changeMade = true;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return changeMade;
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
}
