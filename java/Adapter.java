package com.digiapt.kritterapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.digiapt.kritteradinterface.model.RequestInfo;

import java.util.List;

import static com.digiapt.kritteradinterface.model.RequestInfo.RequestType.BANNER_AD;
import static com.digiapt.kritteradinterface.model.RequestInfo.RequestType.INTERSTITIAL_AD;
import static com.digiapt.kritteradinterface.model.RequestInfo.RequestType.NATIVE_CONTENT_STREAM_AD;
import static com.digiapt.kritteradinterface.model.RequestInfo.RequestType.VIDEO_AD;


public class Adapter extends RecyclerView.Adapter
{
    private List<String> adTypes;
    private Context context;

    public Adapter(Context context, List<String> adTypes)
    {
        this.adTypes = adTypes;
        this.context = context;
    }

    public OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(View view, int pos, RequestInfo.RequestType requestType);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter, parent, false);
        return new ViewHolderSub(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        final ViewHolderSub viewHolderSub = (ViewHolderSub) holder;

        String adType = adTypes.get(position);

        viewHolderSub.name.setTag(viewHolderSub.name.getId(), adTypes.get(position));
        viewHolderSub.name.setText(adTypes.get(position));

        viewHolderSub.itemView.setTag(viewHolderSub.itemView.getId(), adTypes.get(position));
        viewHolderSub.itemView.setOnClickListener(v -> {
            if (adType.equals("Banner Ad")){

                onItemClickListener.onItemClick(v,position,BANNER_AD);

            } else if (adType.equals("Video Ad")){

                onItemClickListener.onItemClick(v,position,VIDEO_AD);

            } else if (adType.equals("Interstitial Ad")){

                onItemClickListener.onItemClick(v,position,INTERSTITIAL_AD);

            } else if (adType.equals("Native Content Stream Ad")){

                onItemClickListener.onItemClick(v,position,NATIVE_CONTENT_STREAM_AD);

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return adTypes.size();
    }

    public class ViewHolderSub extends RecyclerView.ViewHolder
    {
        TextView name;

        public ViewHolderSub(View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.id_name);
        }
    }
}