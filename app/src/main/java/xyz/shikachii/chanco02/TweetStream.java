package xyz.shikachii.chanco02;


import android.content.Context;
import android.widget.Toast;

import twitter4j.Status;
import twitter4j.UserStreamAdapter;

public final class TweetStream extends UserStreamAdapter{
    private Context mContext;

    public TweetStream(Context context){
        mContext = context;
    }

    @Override
    public void onStatus(Status status){
        System.out.println(status.getText());
        Toast.makeText(mContext, status.getText(), Toast.LENGTH_SHORT).show();
    }
}
