package kappathetapi.ktp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import kappathetapi.ktp.java.Member;


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

    private OnFragmentInteractionListener mListener;

    public static MemberProfileFragment newInstance(JSONObject memberJSON) {
        MemberProfileFragment fragment = new MemberProfileFragment();
        fragment.setMember(memberJSON);
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
        View view = inflater.inflate(R.layout.fragment_member_profile, container, false);
        PhotoRequest request = new PhotoRequest();
        try {
            ((ImageView) view.findViewById(R.id.profile_pic_view)).setImageBitmap(request.execute(member.getProfPicUrl()).get());
        }catch(Throwable e) {
            e.printStackTrace();
        }
        ((TextView)(view.findViewById(R.id.profile_name))).setText(member.getFirstName() + " " + member.getLastName());
        return view;
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
    }

    private class PhotoRequest extends AsyncTask<String, String, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... arg) {
            Bitmap bmp = null;
            try {
                URL url = new URL(arg[0]);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bmp;
            } catch(MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bmp;
        }
        @Override
        protected void onPostExecute(Bitmap bmp) {

        }
    }
}
