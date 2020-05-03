package com.apl.bookshelf.activities;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.apl.bookshelf.R;
import com.apl.bookshelf.adapters.BookListAdapter;
import com.apl.bookshelf.pojos.Book;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ADD_BOOK = 1;
    private List<Book> bookRepository;
    private LinearLayoutManager layoutManager;
    private BookListAdapter bookRepositoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        bookRepository = new ArrayList<>();

        if (bookRepository.size() == 0)
            hideRecycler();
        else
            showRecycler();

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);

        RecyclerView recyclerViewBooks = findViewById(R.id.recyclerViewBooks);
        recyclerViewBooks.setLayoutManager(layoutManager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    if (position > -1 && position < bookRepository.size()) {
                        bookRepository.remove(position);
                        bookRepositoryAdapter.notifyItemRemoved(position);
                        if (bookRepository.size() == 0)
                            hideRecycler();
                    }
                } else {
                    Book book = bookRepository.get(position);
                    book.setRead(!book.isRead());
                    bookRepositoryAdapter.notifyItemChanged(position);
                }
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerViewBooks);

        // specify an adapter (see also next example)
        bookRepositoryAdapter = new BookListAdapter(bookRepository);
        recyclerViewBooks.setAdapter(bookRepositoryAdapter);

        FloatingActionButton floatingActionButtonAdd = findViewById(R.id.floatingActionButtonAdd);
        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookListActivity.this, BookAddActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_BOOK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_ADD_BOOK && resultCode == RESULT_OK) {
            assert data != null;

            if (bookRepository.size() == 0)
                showRecycler();

            Book book = data.getParcelableExtra("book");
            bookRepository.add(0, book);
            bookRepositoryAdapter.notifyItemInserted(0);
        }
    }

    private void hideRecycler() {
        TextView textViewNoContent = findViewById(R.id.textViewNoContent);
        RecyclerView recyclerViewBooks = findViewById(R.id.recyclerViewBooks);

        textViewNoContent.setVisibility(View.VISIBLE);
        recyclerViewBooks.setVisibility(View.INVISIBLE);
    }

    private void showRecycler() {
        TextView textViewNoContent = findViewById(R.id.textViewNoContent);
        RecyclerView recyclerViewBooks = findViewById(R.id.recyclerViewBooks);

        textViewNoContent.setVisibility(View.INVISIBLE);
        recyclerViewBooks.setVisibility(View.VISIBLE);
    }
}
