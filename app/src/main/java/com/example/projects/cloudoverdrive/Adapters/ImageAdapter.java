package com.example.projects.cloudoverdrive.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projects.cloudoverdrive.Models.Images;
import com.example.projects.cloudoverdrive.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private List<Images> uploads;
    private ImageAdapter.OnItemClickListener mListener;

    public ImageAdapter(Context context, List<Images> uploads) {
        this.context = context;
        this.uploads = uploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.images ,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, final int position) {
        final Images uploadsCurrent = uploads.get(position);
        final String url = uploadsCurrent.getUrl();
        final String uri= uploadsCurrent.getUrl();
        Picasso.get().load(url).fit().centerCrop().into(holder.image);
        holder.text.setText(uploadsCurrent.getFilename());
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if(url.contains(".gif")) {
                    // GIF file
                    intent.setDataAndType(Uri.parse(uri), "image/gif");
                } else if(url.contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                    // JPG file
                    intent.setDataAndType(Uri.parse(uri), "image/*");
                }else {
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

    public class ImageViewHolder extends  RecyclerView.ViewHolder{

        public ImageView image;
        TextView text,options;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            image= itemView.findViewById(R.id.post);
            text = itemView.findViewById(R.id.text);
            options = itemView.findViewById(R.id.options);
        }
    }
    public interface OnItemClickListener {
        void onDownload(int position );
        void onDelete(int position);
    }
    public void setOnItemClickListener(ImageAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
}
