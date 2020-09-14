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

import com.example.projects.cloudoverdrive.Models.Files;
import com.example.projects.cloudoverdrive.R;

import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    private Context context;
    private List<Files> uploads;
    private OnItemClickListener mListener;

    public FileAdapter(Context context, List<Files> uploads) {
        this.context = context;
        this.uploads = uploads;
    }

    @NonNull
    @Override
    public FileAdapter.FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.files ,parent,false);
        return new FileAdapter.FileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final FileViewHolder holder, final int position) {

        Files uploadsCurrent = uploads.get(position);
        final String url = uploadsCurrent.getUrl();
        final String uri= uploadsCurrent.getUrl();
        holder.file.setText(uploadsCurrent.getFilename());
        holder.file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                    // Word document
                    intent.setDataAndType(Uri.parse(uri), "application/msword");
                } else if(url.toString().contains(".pdf")) {
                    // PDF file
                    intent.setDataAndType(Uri.parse(uri), "application/pdf");
                } else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                    // Powerpoint file
                    intent.setDataAndType(Uri.parse(uri), "application/vnd.ms-powerpoint");
                } else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                    // Excel file
                    intent.setDataAndType(Uri.parse(uri), "application/vnd.ms-excel");
                } else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
                    // WAV zip file
                    intent.setDataAndType(Uri.parse(uri), "application/x-wav");
                } else if(url.toString().contains(".txt")) {
                    // Text file
                    intent.setDataAndType(Uri.parse(uri), "text/plain");
                } else {
                    intent.setDataAndType(Uri.parse(uri), "*/*");
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
      //  final String a= uploadsCurrent.getFilename();
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

    public class FileViewHolder extends  RecyclerView.ViewHolder {

        public TextView file, options;
        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            file = itemView.findViewById(R.id.text);
            options = itemView.findViewById(R.id.options);
        }
    }

    public interface OnItemClickListener {
        void onDownload(int position );
        void onDelete(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
