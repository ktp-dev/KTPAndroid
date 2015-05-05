package kappathetapi.ktp.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private boolean isMemberSet = false;

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

        return myView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Gets the photo from the member's URL then sets it in the view.
        //Must be in onStart because it needs access to the parent activity's runOnUIThread method
        PhotoRequest request = new PhotoRequest();
        request.execute(getString(R.string.server_address) + member.getProfPicUrl());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void setMember(JSONObject json) {
        member = Member.createInstance(json);
        isMemberSet = true;
    }

    public void setMember(Member member) {
        this.member = member;
        this.isMemberSet = true;
    }

    private class PhotoRequest extends AsyncTask<String, String, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... arg) {
            bmp = null;
            if(!(arg[0].equals(getString(R.string.server_address)))) {
                try {
                    URL url = new URL(arg[0]);
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            ((ImageView) myView.findViewById(R.id.profile_pic_view)).setImageBitmap(bmp);
                        }
                    });

                    return bmp;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_user);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        ((ImageView) myView.findViewById(R.id.profile_pic_view)).setImageBitmap(bmp);
                    }
                });
            }

            return bmp;
        }
        @Override
        protected void onPostExecute(Bitmap bmp) {

        }
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
            ((TextView) (myView.findViewById(R.id.profile_bio))).setText("Bio:\n" + member.getBiography());
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
            ((TextView) (myView.findViewById(R.id.profile_service_hours))).setText("Service Hours: " +
                    member.getServiceHours());
        } else {
            ((TextView) (myView.findViewById(R.id.profile_service_hours))).setVisibility(View.GONE);
        }
    }

    //Only visible to self and EBoard
    private void setProDevEventsText() {
        if(member.getUniqname().equals(((HomePageActivity)getActivity()).currentMember.getUniqname())
                ||((HomePageActivity)getActivity()).currentMember.getMembershipStatus().equals("Eboard")) {
            ((TextView) (myView.findViewById(R.id.profile_pro_dev_events))).setText("ProDev Events: " +
                    member.getProDevEvents());
        } else {
            ((TextView) (myView.findViewById(R.id.profile_pro_dev_events))).setVisibility(View.GONE);
        }
    }

    private void setPersonalSiteText() {
        ((TextView)(myView.findViewById(R.id.profile_personal_site))).setText("Personal Site: " +
                member.getPersonalSite());
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
