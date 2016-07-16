package com.sociallearn.app.utils;

/**
 * Created by Sys on 7/16/2016.
 */
        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;
        import com.sociallearn.app.R;
        import com.squareup.picasso.Picasso;

        import java.util.ArrayList;

public class StartupRecyclerViewAdapter extends RecyclerView
        .Adapter<StartupRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "StartupRecyclerViewAdapter";
    private ArrayList<StartupObject> mDataset;
    private static MyClickListener myClickListener;

    Context context;
    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label,status;
        TextView dateTime;
        ImageView icon;

        public DataObjectHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            label = (TextView) itemView.findViewById(R.id.textView);
            dateTime = (TextView) itemView.findViewById(R.id.textView2);
            status = (TextView) itemView.findViewById(R.id.status);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            myClickListener.onItemClick(getAdapterPosition(), v);
        }

    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public StartupRecyclerViewAdapter(Context context, ArrayList<StartupObject> myDataset) {
        this.context = context;
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Picasso.with(context).load(mDataset.get(position).getLogourl()).into(holder.icon);
        holder.label.setText(mDataset.get(position).getmText1());
        holder.dateTime.setText(mDataset.get(position).getmText2());
        holder.status.setText(mDataset.get(position).getStatus());
    }

    public void addItem(StartupObject dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
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
}