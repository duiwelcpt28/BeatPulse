package com.lukechenshui.beatpulse.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by luke on 12/10/16.
 */
/*
TODO: Add a boolean to specify if the playlist originated from an album
TODO: If the boolean is true, the playlist will not show in the to-be-implemented list of playlists.

 */

public class Playlist extends RealmObject implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Playlist> CREATOR = new Parcelable.Creator<Playlist>() {
        @Override
        public Playlist createFromParcel(Parcel in) {
            return new Playlist(in);
        }

        @Override
        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };
    private RealmList<Song> songs = new RealmList<>();
    @PrimaryKey
    private String name;
    private int lastPlayedPosition = 0;

    public Playlist() {

    }

    public Playlist(RealmList<Song> songs, String name) {
        this.songs = songs;
        this.name = name;
    }

    public Playlist(ArrayList<Song> songs, String name) {
        if(this.songs != null){
            this.songs.clear();
        }
        else{
            this.songs = new RealmList<Song>();
        }
        this.songs.addAll(songs);
        this.name = name;
    }

    protected Playlist(Parcel in) {
        ArrayList<Song> songList = (ArrayList<Song>) in.readValue(RealmList.class.getClassLoader());
        if (songs == null) {
            songs = new RealmList<>();
        } else {
            songs.clear();
        }
        songs.addAll(songList);
        name = in.readString();
        lastPlayedPosition = in.readInt();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Playlist) {
            Playlist playlistObj = (Playlist) obj;
            return songs.equals(playlistObj.songs);
        } else {
            return false;
        }
    }

    public int getLastPlayedPosition() {
        return lastPlayedPosition;
    }

    public void setLastPlayedPosition(int lastPlayedPosition) {
        this.lastPlayedPosition = lastPlayedPosition;
    }

    public RealmList<Song> getSongs() {
        return songs;
    }

    public void setSongs(RealmList<Song> songs) {
        this.songs = songs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(songs);
        dest.writeString(name);
        dest.writeInt(lastPlayedPosition);
    }

    public void addSong(Song song){
        songs.add(song);
    }
}