package kappathetapi.ktp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import kappathetapi.ktp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChangePasswordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    TextView errorText;

    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();

        return fragment;
    }

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        Button changePasswordButton = (Button)(view.findViewById(R.id.cp_change_password_button));
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int passwordChanged = mListener.performPasswordChange(
                        ((EditText) (((View)(v.getParent())).findViewById(R.id.cp_username_edit_text))).getText().toString().trim(),
                        ((EditText) (((View)(v.getParent())).findViewById(R.id.cp_old_password_edit_text))).getText().toString().trim(),
                        ((EditText) (((View)(v.getParent())).findViewById(R.id.cp_new_password_edit_text))).getText().toString().trim(),
                        ((EditText) (((View)(v.getParent())).findViewById(R.id.cp_new_password_edit_text1))).getText().toString().trim());
                informUser(((View)(v.getParent())), passwordChanged);
            }
        });

        Button backToLoginButton = (Button)(view.findViewById(R.id.cp_back_to_login_button));
        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.backToLogin();
            }
        });
        return view;
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

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.cp_change_password_button:

                break;
            case R.id.cp_back_to_login_button:

                break;
        }
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

        public int performPasswordChange(String username, String oldPassword,
                                         String newPassword, String newPassword1);

        public void backToLogin();
    }

    private void informUser(View view, int passwordChanged) {
        errorText = (TextView)view.findViewById(R.id.cp_error_text);
        switch(passwordChanged) {
            case 0:
                Toast.makeText(getActivity().getApplicationContext(),"success!", Toast.LENGTH_LONG).show();
                break;
            case 1:
                errorText.setText("Account not found. You in the right place, bruh?");
                break;
            case 2:
                errorText.setText("Invalid password. What time is it? Bad O'Clock!");
            case 4:
                errorText.setText("How hard is it to type the same thing twice?");
            default:
                errorText.setText("Idk what's going on...\nContact sjdallst@umich.edu");
        }
    }

}
