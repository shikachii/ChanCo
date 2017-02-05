package xyz.shikachii.chanco02;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import twitter4j.Status;

public class TweetAdapter extends ArrayAdapter<Status> {

        public ArrayList<Status> tl = new ArrayList<Status>();
        private LayoutInflater mInflater;
        private  int mStatusLayoutResource;
        private static final int resource = R.layout.list_item_tweet;
        int count;

        public TweetAdapter(Context context) {
            super(context,resource);

            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mStatusLayoutResource = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            StatusLayout view;

            if (convertView == null) {
                view = (StatusLayout) mInflater.inflate(mStatusLayoutResource,parent,false);
            }else{
                view = (StatusLayout) convertView;
            }
            Status item;
            item = getItem(getCount() - position - 1);

            view.bindView(item);

            return view;
        }

    public ArrayList<Status> getStatusList(){
        int size = getCount();
        ArrayList<Status> statusList = new ArrayList<Status>(size);
        for (int index = 0; index < size; index++){
            statusList.add(getItem(index));
        }
        return statusList;
    }

    public int getsCount(){
        count = getCount();
        return count;
    }
/*
    public void addAll(ArrayList<Status> parcelableArrayList){
        @SuppressWarnings("unchecked")
        ArrayList<Status> statusList = (ArrayAdapter<Status>) parcelableArrayList;
        super.addAll(statusList);
    }
*/

}
