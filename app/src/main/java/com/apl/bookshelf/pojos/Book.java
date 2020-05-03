package com.apl.bookshelf.pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private String category;
    private String imageUrl;
    private String author;
    private String title;
    private boolean read;

    public Book(String category, String imageUrl, String author, String title, boolean read) {
        this.category = category;
        this.imageUrl = imageUrl;
        this.author = author;
        this.title = title;
        this.read = read;
    }

    protected Book(Parcel in) {
        category = in.readString();
        imageUrl = in.readString();
        author = in.readString();
        title = in.readString();
        read = in.readByte() != 0;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeString(imageUrl);
        dest.writeString(author);
        dest.writeString(title);
        dest.writeByte(read ? (byte)1 : 0);
    }
}
