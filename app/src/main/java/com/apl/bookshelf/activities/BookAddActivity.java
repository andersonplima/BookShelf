package com.apl.bookshelf.activities;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.apl.bookshelf.R;
import com.apl.bookshelf.adapters.BookListAdapter;
import com.apl.bookshelf.pojos.Book;
import com.apl.bookshelf.services.GoogleBooksService;
import com.apl.bookshelf.services.IBookDataResponseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class BookAddActivity extends AppCompatActivity {
    private String[] possibleCategories;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_add);

        possibleCategories = getResources().getStringArray(R.array.book_categories);

        AutoCompleteTextView autoCompleteTextViewCategory = findViewById(R.id.editTextCategory);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, possibleCategories);
        autoCompleteTextViewCategory.setAdapter(categoryAdapter);

        final RecyclerView recyclerViewBooks = findViewById(R.id.recyclerViewResults);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerViewBooks.setLayoutManager(layoutManager);

        ImageButton imageButtonSearch = findViewById(R.id.imageButtonSearch);
        imageButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextSearchByName = findViewById(R.id.editTextSearchByName);
                recyclerViewBooks.setAdapter(null);
                editTextSearchByName.clearFocus();
                GoogleBooksService.searchBooks(getApplicationContext(), editTextSearchByName.getText().toString(), new IBookDataResponseHandler() {
                    @Override
                    public void OnCompleted(ArrayList<Book> books) {
                        BookListAdapter bookRepositoryAdapter = new BookListAdapter(books);
                        recyclerViewBooks.setAdapter(bookRepositoryAdapter);
                    }
                });
            }
        });

        FloatingActionButton floatingActionButtonSave = findViewById(R.id.floatingActionButtonSave);
        floatingActionButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookListAdapter adapter = (BookListAdapter) recyclerViewBooks.getAdapter();
                if (adapter != null) {
                    if (adapter.getItemCount() > 0) {
                        Book book = adapter.getItem(0);
                        AutoCompleteTextView autoCompleteTextViewCategory = findViewById(R.id.editTextCategory);
                        book.setCategory(autoCompleteTextViewCategory.getText().toString());

                        Intent data = new Intent();
                        data.putExtra("book", book);
                        setResult(RESULT_OK, data);
                    } else {
                        setResult(RESULT_CANCELED);
                    }
                } else
                    setResult(RESULT_CANCELED);

                finish();
            }
        });
    }
}
