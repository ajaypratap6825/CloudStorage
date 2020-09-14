package com.example.projects.cloudoverdrive.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projects.cloudoverdrive.Models.Audios;
import com.example.projects.cloudoverdrive.R;

import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioViewHolder> {
    private Context context;
    private List<Audios> uploads;
    private AudioAdapter.OnItemClickListener mListener;

    public AudioAdapter(Context context, List<Audios> uploads) {
        this.context = context;
        this.uploads = uploads;
    }

    @NonNull
    @Override
    public AudioAdapter.AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.audios ,parent,false);
        return new AudioAdapter.AudioViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull AudioAdapter.AudioViewHolder holder, final int position) {

        Audios uploadsCurrent = uploads.get(position);
        final String url = uploadsCurrent.getmUrl();
        final String uri= uploadsCurrent.getmUrl();
        holder.audio.setText(uploadsCurrent.getFilename());
        holder.audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if(url.contains(".wav") || url.toString().contains(".mp3")) {
                    // WAV audio file
                    intent.setDataAndType(Uri.parse(uri), "audio/x-wav");
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

    public class AudioViewHolder extends  RecyclerView.ViewHolder{

        public TextView audio, options;
        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            audio = itemView.findViewById(R.id.text);
            options = itemView.findViewById(R.id.options);
        }
    }
    public interface OnItemClickListener {
        void onDownload(int position );
        void onDelete(int position);
    }
    public void setOnItemClickListener(AudioAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
}
