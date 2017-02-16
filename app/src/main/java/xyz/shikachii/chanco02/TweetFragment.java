package xyz.shikachii.chanco02;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TweetFragment extends Fragment {

    private onMenuClickListener mCallback;
    private onImageMenuClickListener mImageCallback;
    public interface onMenuClickListener{
        void onMenuSelected();
    }
    public interface onImageMenuClickListener{
        void onImageMenuClicked(Status status,int index);
    }

    private EditText mInputText;
    private Twitter mTwitter;
    private LinearLayout mUtil,mDetail;
    private int code;
    private Status sta;
    private TextView image_text,detail;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.activity_tweet,container,false);
        mTwitter = TwitterUtils.getTwitterInstance(getActivity());
        mInputText = (EditText)v. findViewById(R.id.input_text);
        image_text = (TextView) v.findViewById(R.id.image_text);
        mUtil = (LinearLayout)v.findViewById(R.id.tweet_view);
        mDetail = (LinearLayout)v.findViewById(R.id.tweet_detail);
        detail = (TextView)v.findViewById(R.id.tweet_status);

        mUtil.setVisibility(View.GONE);
        mDetail.setVisibility(View.GONE);

        v.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUtil.setVisibility(View.GONE);
                mDetail.setVisibility(View.GONE);
            }
        });

        v.findViewById(R.id.fav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onMenuSelected();
                setFav(sta);
            }
        });

        v.findViewById(R.id.rt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onMenuSelected();
                setRT(sta);
            }
        });

        v.findViewById(R.id.utility).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCallback.onMenuSelected();
                viewMenu(sta);
            }
        });



        mDetail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){}
        });

        v.findViewById(R.id.action_tweet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tweet(v);
            }
        });

        return v;
    }

    //ツイートメソッド
    private void tweet(final View v){
        AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    mTwitter.updateStatus(params[0]);
                    return true;
                }catch (TwitterException e) {
                    code = e.getErrorCode();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result){
                if(result){
                    showToast("ツイート完了");
                    mInputText.getEditableText().clear();
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }else{
                    showToast("ツイート失敗 code = "+ code);
                }
            }
        };
        task.execute(mInputText.getText().toString());
    }

    //ファボメソッド
    public void setFav(final Status status){
        AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                try{
                    mTwitter.createFavorite(status.getId());
                    return true;
                }catch (TwitterException e){
                    code = e.getErrorCode();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result){
                if (result){
                    showToast("お気に入り完了");
                }else{
                    showToast("お気に入り失敗 code = "+ code);
                }
            }
        };
        task.execute("True");
    }

    //リツイートメソッド
    public void setRT(final Status status){
        AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>(){
            @Override
            protected Boolean doInBackground(String... params){
                try{
                    mTwitter.retweetStatus(status.getId());
                    return true;
                } catch (TwitterException e) {
                    code = e.getErrorCode();
                    return false;
                }
            }

            @Override
            protected  void onPostExecute(Boolean result){
                if(result){
                    showToast("RT完了");
                }else{
                    showToast("RT失敗しました code = "+code);
                }
            }
        };
        task.execute("True");
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof onMenuClickListener){
            mCallback = (onMenuClickListener) activity;
        }

        if(activity instanceof  onImageMenuClickListener){
            mImageCallback = (onImageMenuClickListener) activity;
        }
    }

    public void viewBox(Status tweet){
        mUtil.setVisibility(View.VISIBLE);
        mDetail.setVisibility(View.VISIBLE);

        if(tweet.isRetweet()) {
            detail.setText("@" + tweet.getRetweetedStatus().getUser().getScreenName()
                    + "\n" + tweet.getRetweetedStatus().getText());
        }else{
            detail.setText("@" + tweet.getUser().getScreenName()
                    + "\n" + tweet.getText());
        }

        sta = tweet;
    }

    private void viewMenu(Status tweet) {
        final MediaEntity[] mediaEntities;
        final Status status = tweet;
        final ArrayList<String> media = new ArrayList<String>();

        if (tweet.isRetweet()) {
            mediaEntities = tweet.getRetweetedStatus().getExtendedMediaEntities();
        } else {
            mediaEntities = tweet.getExtendedMediaEntities();
        }

        media.add("@" + status.getUser().getScreenName());

        System.out.println(mediaEntities.length);
        image_text.setText(" " + mediaEntities.length);

        if (mediaEntities.length > 0){
            //String[] media = new String[4];
            for(MediaEntity me : mediaEntities){
                media.add(me.getMediaURLHttps());
            }
            /*
            for ( int i = 1; i < mediaEntities.length; i++){
                media.add(mediaEntities[i].getMediaURLHttps());
            }
            */

            //mImageCallback.onImageMenuClicked(tweet);
        }

        new AlertDialog.Builder(getActivity())
                .setItems(media.toArray(new String[media.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0){
                            showToast(status.getUser().getScreenName());
                        }else {
                            mImageCallback.onImageMenuClicked(status, i-1);
                            //showToast(mediaEntities[i].getMediaURL());
                        }
                    }
                })
                .show();
    }



    private void showToast(String text){
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

}
