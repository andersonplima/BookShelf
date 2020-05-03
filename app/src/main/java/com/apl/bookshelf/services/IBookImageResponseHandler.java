package com.apl.bookshelf.services;

import android.graphics.Bitmap;

public interface IBookImageResponseHandler {
    void onCompleted(Bitmap bitmap);
}
