package creativex.com.myapplication.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

import creativex.com.myapplication.DataModel.PlaceInfoDataModel;
import creativex.com.myapplication.R;

/**
 * Created by karthik on 4/1/16.
 */
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.DataObjectHolder> {
    private static String LOG_TAG = "PlaceAdapter";
//    private ArrayList<PlaceInfoDataModel> mDataset;


    private ArrayList<String> mDataset;

    private static MyClickListener myClickListener;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;
    Context mContext;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {
        TextView label;
        TextView desc;
        TextView dateTime;
        ImageView mImage;

        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.tv_title);
//            desc = (TextView) itemView.findViewById(R.id.tv_desc);
//            mImage = (ImageView) itemView.findViewById(R.id.iv_bg);
            Log.i(LOG_TAG, "Adding Listener");
//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

//    public PlaceAdapter(Context context, ArrayList<PlaceInfoDataModel> myDataset) {
//        mDataset = myDataset;
//        mContext = context;
//
//    }

    public PlaceAdapter(Context context, ArrayList<String> myDataset) {
        mDataset = myDataset;
        mContext = context;

    }


    View view;

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_recycleview_item, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mDataset.get(position));
//        holder.desc.setText(mDataset.get(position).getPlaceDesc());
//        holder.mImage.setBackgroundResource(mDataset.get(position).getImageSource());

//        setAnimation(view, position);
    }

    public void addItem(PlaceInfoDataModel dataObj, int index) {
//        mDataset.add(dataObj);
//        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
