package xyz.shikachii.chanco02;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.UserStreamAdapter;

public class TweetListFragment extends ListFragment {

    private onListClickListener mCallback;
    public interface onListClickListener{
        void onListSelected(Status item);
    }

    private TweetAdapter mAdapter;
    private TweetAdapter sAdapter;
    private Twitter mTwitter;
    private TwitterStream sTwitter;
    private static android.os.Handler mHandler = new android.os.Handler();
    private int getNumber = 0,size = 0,code;
    private AsyncTask<Void, Void, List<Status>> task;
    public twitter4j.Paging p = new Paging();
    private ListView listView;
    private ProgressDialog progressDialog;
    private static int isLive = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new TweetAdapter(getActivity());
        setListAdapter(mAdapter);
        mTwitter = TwitterUtils.getTwitterInstance(getActivity());

        sAdapter = new TweetAdapter(getActivity());
        setListAdapter(sAdapter);
        sTwitter = TwitterUtilsStream.getTwitterInstance(getActivity());

        if(TwitterUtils.hasAccessToken(getActivity())) {
            showToast("UserStream開始");
            sTwitter.addListener(new UserStreamAdapter() {
                @Override
                public void onStatus(final Status status) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            sAdapter.insert(status, getNumber);
                            getNumber++;
                            listView = getListView();
                            int pos = listView.getFirstVisiblePosition();
                            View v = listView.getChildAt(0);
                            int top = (v == null) ? 0 : v.getTop();

                            listView.deferNotifyDataSetChanged();

                            listView.setSelectionFromTop(pos + 1, top);

                            if (pos == 0) { // && top == 0) { ← 表示位置が1番上のときという意味
                                listView.smoothScrollToPositionFromTop(pos, 0);
                            }
                        }
                    });
                }
            });

            reloadTimeLine();
        }
    }

    @Override
    public void onListItemClick(ListView listView, View v, int position,long id) {
        super.onListItemClick(listView, v, position, id);
        Status item = (Status) listView.getItemAtPosition(sAdapter.getsCount() - position - 1);
        //System.out.println(mCallback);
        //System.out.println("print : " + id + ":" + listView);
        mCallback.onListSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);

        listView = getListView();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean isLastItemVisible = totalItemCount == firstVisibleItem + visibleItemCount;
                if (totalItemCount > 20 && isLastItemVisible) {
                    reloadTimeLine();
                }
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();

        System.out.println("onStart");

        p.setCount(21);
        listView.setSelection(0);

        sTwitter.user();
    }

    public void set(){
        this.getListView().setSelection(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy().fragment");
        sTwitter.cleanUp();
    }

    @Override
    public void onStop(){
        super.onStop();
        System.out.println("onStop().fragment");
        //sTwitter.cleanUp();
    }

    @Override
    public void onPause(){
        super.onPause();
        System.out.println("onPause().fragment");
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        //sTwitter.user();
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume().fragment");
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof onListClickListener){
            mCallback = (onListClickListener) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_list, null);
        return v;
    }

    private void reloadTimeLine() {
        if(task != null && task.getStatus() == AsyncTask.Status.RUNNING){
            System.out.println(isLoading());
            return;
        }

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("読込中");
        progressDialog.setCancelable(true);
        progressDialog.show();

        task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {
            @Override
            protected List<twitter4j.Status> doInBackground(Void... params) {
                try {
                    return mTwitter.getHomeTimeline(p);
                } catch (TwitterException e) {
                    code = e.getErrorCode();
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(List<twitter4j.Status> result) {
                if (result != null){
                    listView.setSelection(0);
                    for (twitter4j.Status status : result) {
                        sAdapter.insert(status,0);
                        getNumber++;
                    }
                    size = result.size();
                    twitter4j.Status s = result.get(size-1);
                    p.setMaxId(s.getId());

                    sAdapter.remove(result.get(0));
                    getNumber--;
                } else {
                    showToast("タイムライン取得失敗 ErrorCode => "+code);
                }
                progressDialog.dismiss();
            }
        };
        task.execute();
    }

    private boolean isLoading(){
        System.out.println(task.getStatus());
        return (task != null && task.getStatus() == AsyncTask.Status.RUNNING);
    }

    private void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

}