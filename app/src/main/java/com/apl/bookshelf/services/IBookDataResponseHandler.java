package com.apl.bookshelf.services;

import com.apl.bookshelf.pojos.Book;

import java.util.ArrayList;

public interface IBookDataResponseHandler {
    void OnCompleted(ArrayList<Book> books);
}
