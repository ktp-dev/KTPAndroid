package kappathetapi.ktp.fragments.meetings;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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
    private Activity mActivity;
    public MeetingAdapter(Context context, int resource, int textViewResourceId, T[] objects, Activity activity) {
        super(context, resource, textViewResourceId, objects);
        mActivity = activity;
    }

    @Override
    public View getView(int position, View viewConvert, ViewGroup parent) {
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
        Bitmap bmp = BitmapFactory.decodeResource(mActivity.getResources(), photoId);
        ((ImageView)(view.findViewById(R.id.meeting_image))).setImageBitmap(bmp);
    }

    private void setName(Member member, View view) {
        ((TextView)(view.findViewById(R.id.meeting_text))).setText(member.getFirstName() + " " + member.getLastName());
    }

    public void setUser(Member user) {
        this.user = user;
    }

    private boolean checkForMeeting(Member member) {
        JSONArray meetingIds = member.getMeetings();
        int size = meetingIds.length();
        boolean hasMet = false;
        for(int i = 0; i < size && !hasMet; ++i) {
            try {
                Log.d("MEETING ID?", meetingIds.get(i).toString());
                if (meetingIds.get(i).toString().equals(user.getId())) {
                    hasMet = true;
                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return hasMet;
    }
}
