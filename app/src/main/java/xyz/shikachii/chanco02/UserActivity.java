package xyz.shikachii.chanco02;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;
import com.squareup.picasso.Picasso;

import twitter4j.Status;

public class UserActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        TextView name = (TextView)findViewById(R.id.name_);
        TextView screen_name = (TextView)findViewById(R.id.screen_name_);
        SmartImageView icon = (SmartImageView)findViewById(R.id.image_icon);

        TextView bio = (TextView)findViewById(R.id.bio);

        TextView tweet_count = (TextView)findViewById(R.id.tweet_counts);
        TextView follows = (TextView)findViewById(R.id.follows);
        TextView followers = (TextView)findViewById(R.id.followers);
        TextView ff = (TextView)findViewById(R.id.ff);
        TextView fav_counts = (TextView)findViewById(R.id.fav_counts);


        Button button = (Button)findViewById(R.id.close_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        Status status = (Status) intent.getSerializableExtra("status");

        name.setText(status.getUser().getName());
        screen_name.setText("@"+status.getUser().getScreenName());

        Picasso.with(getApplicationContext())
                .load(status.getUser().getOriginalProfileImageURLHttps())
                .into(icon);

        bio.setText(status.getUser().getDescription());

        tweet_count.setText("ツイート数 : " + status.getUser().getStatusesCount());
        follows.setText("フォロー : " + status.getUser().getFriendsCount());
        followers.setText("フォロワー : " + status.getUser().getFollowersCount());
        ff.setText("ff比 : " + (double)status.getUser().getFollowersCount() / (double)status.getUser().getFriendsCount());
        fav_counts.setText("fav数 : " + status.getUser().getFavouritesCount());

    }

    @Override
    protected void onStop(){
        super.onStop();
        System.out.println("onStop()");
    }

    @Override
    protected void onPause(){
        super.onPause();
        System.out.println("onPause()");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        System.out.println("onDestroy()");
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
