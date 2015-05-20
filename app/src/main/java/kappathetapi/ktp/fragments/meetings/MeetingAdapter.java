package kappathetapi.ktp.fragments.meetings;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import kappathetapi.ktp.R;
import kappathetapi.ktp.classes.Member;

/**
 * Created by sjdallst on 5/14/2015.
 */
public class MeetingAdapter<T> extends ArrayAdapter<T> {
    private Member user = null;
    private Fragment mFragment;
    public MeetingAdapter(Context context, int resource, int textViewResourceId, T[] objects, Fragment frag) {
        super(context, resource, textViewResourceId, objects);
        mFragment = frag;
    }

    @Override
    public View getView(int position, View viewConvert, ViewGroup parent) {
        if(viewConvert == null) {
            LayoutInflater vi = (LayoutInflater)(getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            viewConvert=vi.inflate(R.layout.list_item_meeting, null);
        }
        Member member = (Member)getItem(position);
        boolean hasMet = checkForMeeting(member);

        if(hasMet) {
            setImage(viewConvert, R.drawable.ic_green_check);
        } else {
            setImage(viewConvert, R.drawable.ic_red_x);
        }

        setName(member, viewConvert);

        return viewConvert;
    }

    private void setImage(View view, int photoId) {
        Bitmap bmp = BitmapFactory.decodeResource(mFragment.getResources(), photoId);
        System.out.println(view == null);
        System.out.println(view.findViewById(R.id.meeting_image) == null);
        ((ImageView)(view.findViewById(R.id.meeting_image))).setImageBitmap(bmp);
    }

    private void setName(Member member, View view) {
        ((TextView)(view.findViewById(R.id.meeting_text))).setText(member.getFirstName() + " " + member.getLastName());
    }

    public void setUser(Member user) {
        this.user = user;
    }

    //TODO: make actually return if people have met
    private boolean checkForMeeting(Member member) {

        boolean hasMet = false;

        return hasMet;
    }
}
