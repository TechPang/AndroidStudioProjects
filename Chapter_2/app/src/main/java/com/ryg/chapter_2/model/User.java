package com.ryg.chapter_2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ryg.chapter_2.aidl.Book;

import java.io.Serializable;

public class User implements Serializable,Parcelable {

    private static final long serialVersionUID = 1L;
    public int userId;
    public String userName;
    public boolean isMale;
    public Book book;

    public User(int useId, String userName, boolean isMale) {
        this.userId = useId;
        this.userName = userName;
        this.isMale = isMale;
    }

    private User(Parcel in) {
        userId = in.readInt();
        userName = in.readString();
        isMale = in.readInt() == 1;
        book = in.readParcelable(Thread.currentThread().getContextClassLoader());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(userId);
        out.writeString(userName);
        out.writeInt(isMale ? 1 : 0);
        out.writeParcelable(book, 0);
    }
}
