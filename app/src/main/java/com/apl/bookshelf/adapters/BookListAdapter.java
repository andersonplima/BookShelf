package com.apl.bookshelf.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.apl.bookshelf.R;
import com.apl.bookshelf.pojos.Book;
import com.apl.bookshelf.services.GoogleBooksService;
import com.apl.bookshelf.services.IBookImageResponseHandler;

import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {
    private List<Book> bookArrayList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class BookViewHolder extends RecyclerView.ViewHolder {
        private static final float SWIPE_LIMIT = 80;
        // each data item is just a string in this case
        private View view;

        public BookViewHolder(View view) {
            super(view);
            this.view = view;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BookListAdapter(List<Book> bookArrayList) {
        this.bookArrayList = bookArrayList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BookListAdapter.BookViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);

        BookViewHolder vh = new BookViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final BookViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Book book = bookArrayList.get(position);

        ((TextView) holder.view.findViewById(R.id.textViewCategory)).setText(book.getCategory());
        ((TextView) holder.view.findViewById(R.id.textViewAuthor)).setText(book.getAuthor());
        ((TextView) holder.view.findViewById(R.id.textViewTitle)).setText(book.getTitle());
        holder.view.findViewById(R.id.imageViewRead).setVisibility(book.isRead() ? View.VISIBLE : View.INVISIBLE);

        if (book.getImageUrl() != null)
            GoogleBooksService.getImage(holder.view.getContext(), book.getImageUrl(), new IBookImageResponseHandler() {
                @Override
                public void onCompleted(Bitmap bitmap) {
                    ((ImageView) holder.view.findViewById(R.id.imageViewBookImage)).setImageBitmap(bitmap);
                }
            });
    }

    public Book getItem(int position) {
        return this.bookArrayList.get(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }
}
