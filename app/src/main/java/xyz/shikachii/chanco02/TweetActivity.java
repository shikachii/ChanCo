package xyz.shikachii.chanco02;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TweetActivity extends FragmentActivity {

    private EditText mInputText;
    private Twitter mTwitter;
    private int code;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        mTwitter = TwitterUtils.getTwitterInstance(this);

        mInputText = (EditText) findViewById(R.id.input_text);

        findViewById(R.id.action_tweet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tweet();
            }
        });
    }

    private void tweet(){
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
                    finish();
                }else{
                    showToast("ツイート失敗 code = "+ code);
                }
            }
        };
        task.execute(mInputText.getText().toString());
    }

    private void showToast(String text){
        Toast.makeText(this,text, Toast.LENGTH_SHORT).show();
    }

}
