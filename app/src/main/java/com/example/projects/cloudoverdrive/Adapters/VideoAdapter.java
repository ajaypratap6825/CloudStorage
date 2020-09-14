package com.example.projects.cloudoverdrive.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projects.cloudoverdrive.R;
import com.example.projects.cloudoverdrive.Models.Videos;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private Context context;
    private List<Videos> uploads;
    private VideoAdapter.OnItemClickListener mListener;

    public VideoAdapter(Context context, List<Videos> uploads) {
        this.context = context;
        this.uploads = uploads;
    }

    @NonNull
    @Override
    public VideoAdapter.VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.videos ,parent,false);
        return new VideoAdapter.VideoViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, final int position) {

        Videos uploadsCurrent = uploads.get(position);
        holder.video.setVideoURI(Uri.parse(uploadsCurrent.getUrl()));
        holder.video.seekTo(1);
        final String url = uploadsCurrent.getUrl();
        final String uri= uploadsCurrent.getUrl();
        holder.text.setText(uploadsCurrent.getFilename());
        holder.video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                    // Video files
                    intent.setDataAndType(Uri.parse(uri), "video/*");
                } else {
                    intent.setDataAndType(Uri.parse(uri), "*/*");
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, view);
                popup.inflate(R.menu.more);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete: mListener.onDelete(position);
                                return true;
                            case R.id.download: mListener.onDownload(position);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public class VideoViewHolder extends  RecyclerView.ViewHolder{

        public VideoView video;
        TextView text,options;
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            video = itemView.findViewById(R.id.post);
            text = itemView.findViewById(R.id.text);
            options = itemView.findViewById(R.id.options);
        }
    }
    public interface OnItemClickListener {
        void onDownload(int position );
        void onDelete(int position);
    }
    public void setOnItemClickListener(VideoAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
}
