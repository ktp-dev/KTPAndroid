package kappathetapi.ktp.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import kappathetapi.ktp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    private TextView errorText;

    OnFragmentInteractionListener mListener;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Button loginButton = (Button)(view.findViewById(R.id.login_button));
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int loginSuccess = mListener.performLogin(
                        ((EditText) (((View)(v.getParent())).findViewById(R.id.username_text))).getText().toString().trim(),
                        ((EditText) (((View)(v.getParent())).findViewById(R.id.password_text))).getText().toString().trim());
                informUser(v, loginSuccess);
            }
        });

        errorText = (TextView)(view.findViewById(R.id.failed_login_text));

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener)activity;
        } catch (ClassCastException e) {
            Log.e("LISTENER: ", "activity must implement LoginFragment's listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {

        public int performLogin(String username, String password);

        public void startChangePasswordFragment();
    }

    public void informUser(View view, int loginSuccess) {
        switch(loginSuccess) {
            case 0:
               break;
            case 1:
                errorText.setText("Account not found. You in the right place, bruh?");
                break;
            case 2:
                errorText.setText("Invalid password. What time is it? Bad O'Clock!");
            default:
                errorText.setText("Idk what's going on...\nContact sjdallst@umich.edu");
        }
    }
}
