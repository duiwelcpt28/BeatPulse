package com.lukechenshui.beatpulse.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lukechenshui.beatpulse.Config;
import com.lukechenshui.beatpulse.DrawerInitializer;
import com.lukechenshui.beatpulse.R;
import com.lukechenshui.beatpulse.Utility;
import com.lukechenshui.beatpulse.models.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by luke on 12/10/16.
 */

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileHolder> {
    Context context;
    ArrayList<File> files = new ArrayList<>();

    public FileAdapter(ArrayList<File> files, Context context) {
        this.files = files;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(FileHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public FileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_list_item, parent, false);
        return new FileHolder(view);
    }

    @Override
    public void onBindViewHolder(FileHolder holder, int position) {
        File file = files.get(position);
        holder.bindData(file);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    protected class FileHolder extends RecyclerView.ViewHolder {
        TextView fileNameTextView;
        File currentFile;

        public FileHolder(View itemView) {
            super(itemView);
            fileNameTextView = (TextView) itemView.findViewById(R.id.fileNameTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentFile != null) {
                        if (currentFile.isDirectory()) {
                            Config.setLastFolderLocation(currentFile.getAbsolutePath(),
                                    context);
                            ArrayList<File> fileList = Utility.getListOfAudioFilesInDirectory(context);
                            files.clear();
                            files = fileList;
                            notifyDataSetChanged();
                        } else if (currentFile.getName().endsWith("mp3")) {
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();

                            Song song = new Song(currentFile.getName(), currentFile);

                            realm.copyToRealmOrUpdate(song);

                            Intent intent = new Intent(context, DrawerInitializer.getDrawerActivities().get(Config.NOW_PLAYING_DRAWER_ITEM_POS));

                            Config.getActiveDrawer().setSelection(Config.NOW_PLAYING_DRAWER_ITEM_POS, false);

                            Bundle bundle = new Bundle();
                            bundle.putParcelable("song", song);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtras(bundle);
                            realm.commitTransaction();
                            context.startActivity(intent);
                        }
                    }
                }
            });
        }

        public void bindData(File file) {
            if (file != null) {
                currentFile = file;
                File lastDirectory = new File(Config.getLastFolderLocation(context));
                File parent = lastDirectory.getParentFile();
                if (parent != null) {
                    if (file.getAbsolutePath().equals(parent.getAbsolutePath())) {
                        fileNameTextView.setText("..");
                    } else {
                        fileNameTextView.setText(file.getName());
                    }
                } else {
                    fileNameTextView.setText(file.getName());
                }
            }
        }
    }
}