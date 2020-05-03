package com.apl.bookshelf.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.apl.bookshelf.pojos.Book;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * https://developer.android.com/training/volley/request
 */
public class GoogleBooksService {
    private static GoogleBooksService instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static Context ctx;

    private GoogleBooksService(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    private static synchronized GoogleBooksService getInstance(Context context) {
        if (instance == null) {
            instance = new GoogleBooksService(context);
        }
        return instance;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    private <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    private ImageLoader getImageLoader() {
        return imageLoader;
    }

    public static void searchBooks(Context context, String query, final IBookDataResponseHandler bookDataResponseHandler) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<Book> books = new ArrayList<>();
                        try {
                            JSONArray jsonArrayBooks = response.getJSONArray("items");
                            int arrayLength = jsonArrayBooks.length();
                            for (int i = 0; i < arrayLength; ++i) {
                                JSONObject jsonBook = jsonArrayBooks.getJSONObject(i);
                                JSONObject jsonVolumeInfo = jsonBook.getJSONObject("volumeInfo");

                                String title = jsonVolumeInfo.getString("title");

                                JSONArray jsonArrayAuthors = jsonVolumeInfo.getJSONArray("authors");
                                String author = jsonArrayAuthors.getString(0);

                                String imageUrl = null;
                                if (jsonVolumeInfo.has("imageLinks")) {
                                    imageUrl = jsonVolumeInfo.getJSONObject("imageLinks")
                                            .getString("thumbnail")
                                            .replace("&edge=curl", "");
                                }

                                Book book = new Book(null, imageUrl, author, title, false);
                                books.add(book);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        bookDataResponseHandler.OnCompleted(books);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        // Access the RequestQueue through your singleton class.
        getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void getImage(Context context, String imageUrl, final IBookImageResponseHandler bookImageResponseHandler) {
        ImageRequest imageRequest = new ImageRequest(imageUrl,
                new Response.Listener<Bitmap>() {

                    @Override
                    public void onResponse(Bitmap response) {
                        bookImageResponseHandler.onCompleted(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        getInstance(context).addToRequestQueue(imageRequest);
    }
}
