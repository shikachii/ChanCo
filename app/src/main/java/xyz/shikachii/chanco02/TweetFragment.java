package xyz.shikachii.chanco02;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

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
                new AlertDialog.Builder(getActivity())
                        .setTitle("確認").setMessage("RTしますか?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mCallback.onMenuSelected();
                                setRT(sta);
                            }
                        }).setNegativeButton("Cancel", null).show();
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
        final SparseArray<String> m = new SparseArray<String>();
        final ArrayList<String> media = new ArrayList<String>();
        final ArrayList<String> arrayList = new ArrayList<String>();

        ArrayList<String> re = new ArrayList<String>();
        ArrayList<String> id = new ArrayList<String>();

        if (tweet.isRetweet()) {
            mediaEntities = tweet.getRetweetedStatus().getExtendedMediaEntities();
            re.add("共 @" + tweet.getRetweetedStatus().getUser().getScreenName());
            m.put(0,"共 @" + tweet.getRetweetedStatus().getUser().getScreenName());
            arrayList.addAll(re);
        } else {
            mediaEntities = tweet.getExtendedMediaEntities();
        }

        id.add("人 @" + status.getUser().getScreenName());
        m.put(1,"人 @" + status.getUser().getScreenName());

        System.out.println(mediaEntities.length);

        if (mediaEntities.length > 0){
            int i = 2;
            for(MediaEntity me : mediaEntities){
                media.add(me.getMediaURLHttps());
                m.put(i,me.getMediaURLHttps());
                ++i;
            }
        }

        arrayList.addAll(id);
        arrayList.addAll(media);

        for(int i = 0; i < m.size(); i++){
            System.out.println(i + " : " + m.valueAt(i));
        }

        new AlertDialog.Builder(getActivity())
                .setItems(arrayList.toArray(new String[arrayList.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("keyAt : " + m.keyAt(i) + " i : " + i);
                        if(m.keyAt(i) == 0){
                            Intent intent = new Intent(getActivity(), UserActivity.class);
                            intent.putExtra("status", status.getRetweetedStatus());
                            startActivity(intent);
                        }else if(m.keyAt(i) == 1) {
                            Intent intent = new Intent(getActivity(), UserActivity.class);
                            intent.putExtra("status", status);
                            startActivity(intent);
                        }else if(m.keyAt(i) >= 2 && m.keyAt(i) <= 5){
                            mImageCallback.onImageMenuClicked(status, arrayList.size()-i-1);
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