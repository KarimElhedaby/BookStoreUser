package com.hamza.alif.bookstoreuser.Ui;

import android.app.SearchManager;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hamza.alif.bookstoreuser.Util.ActivityLancher;
import com.hamza.alif.bookstoreuser.Book;
import com.hamza.alif.bookstoreuser.BookAdapter;
import com.hamza.alif.bookstoreuser.Util.Constants;
import com.hamza.alif.bookstoreuser.R;
import com.hamza.alif.bookstoreuser.Util.Utilities;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements BookAdapter.OnBookEventListener {

    private RecyclerView recyclerMyBooks;
    private BookAdapter adapter;
    private ArrayList<Book> books;
    private String lastBookKey = "";
    private GridLayoutManager gridLayoutManager;
    private boolean isLoadMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        books = new ArrayList<>();
        recyclerMyBooks = findViewById(R.id.recycler_books);
        adapter = new BookAdapter(this, books);
        gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerMyBooks.setLayoutManager(gridLayoutManager);
        recyclerMyBooks.setAdapter(adapter);
        showBooksinfo();

        recyclerMyBooks.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) { //select that scroll to y axis
                    //GET LAST ITEM  visible position
                    int lastVisibleItemPosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();


                    //لو انته اصل ب تلود دلوقتى وعملت لود تانى لا مش ه لود هستنى لما الريسلت تيجى
                    //بقوله انته تلود امتا
                    if (!isLoadMore && books != null && books.size() > 0
                            && lastVisibleItemPosition == books.size() - 1) {
                        isLoadMore = true;
                        lastBookKey = books.get(books.size() - 1).getId();
                        getBooks();
                    }
                }

            }
        });


        getBooks();
    }

    private void showBooksinfo() {
        if (Utilities.isNetworkAvailable(this)) {
//            if( adapter.getItemCount()!=0)
            Utilities.showLoadingDialog(this, Color.WHITE);

            getBooks();
        }
        else {
            Snackbar.make(recyclerMyBooks, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showBooksinfo();
                        }
                    })
                    .setActionTextColor(Color.WHITE)
                    .show();
        }
    }


    private void getBooks() {
//       Utilities.showLoadingDialog(this, Color.RED);
        FirebaseDatabase
                .getInstance()
                .getReference(Constants.REF_BOOK)
                .orderByKey()
                .startAt(lastBookKey)
                .limitToFirst(9)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Utilities.dismissLoadingDialog();

                        if (dataSnapshot.getChildrenCount() == 9) {
                            isLoadMore = false;
                        }

                        int i = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Book book = snapshot.getValue(Book.class);
                            if (book != null) {

                                if (!lastBookKey.isEmpty() && i == 0) {
                                    i++;
                                    continue;
                                }

                                adapter.addBook(book);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onBookClick(Book book) {
        ActivityLancher.openBookDetails(this, book);

    }

    @Override
    public void editBook(Book book) {

    }


    private MenuItem searchMenuItem;
    SearchView searchView;
    private SimpleCursorAdapter cursorAdapter;
    private MatrixCursor matrixCursor;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_books, menu);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.toLowerCase().trim().isEmpty()) {
                    matrixCursor = new MatrixCursor(new String[]{"_id", "title", "book"});//اسامى ال columns ال ف ال row
                    searchByBookTitle(newText);
                }
                return false;
            }
        });

        return true;
    }

    private void searchByBookTitle(final String title) {
        FirebaseDatabase
                .getInstance()
                .getReference(Constants.REF_BOOK)
                .orderByChild(Constants.BOOK_TITLE)
                .startAt(title)
                .endAt(title + "\uf8ff")
                .limitToFirst(5)//يجيب 5 ب 5
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        Book book;
                        int i = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            book = snapshot.getValue(Book.class);
                            if (book != null) {
                                matrixCursor.addRow(new Object[]{++i, book.getTitle(),
                                        book});//الكتاب معايا ف ال cursor

                                Log.d("SearchBOOK", book.getTitle());

                            }
                        }

                        cursorAdapter = new SimpleCursorAdapter(MainActivity.this, R.layout.item_suggestion
                                , matrixCursor, new String[]{"title"}, new int[]{R.id.tv_title},
                                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                        searchView.setSuggestionsAdapter(cursorAdapter);

                        //Todo: Implement on Suggest book selected
                        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                            @Override
                            public boolean onSuggestionSelect(int position) {

                                return false;
                            }

                            @Override
                            public boolean onSuggestionClick(final int position) {

                                FirebaseDatabase
                                        .getInstance()
                                        .getReference(Constants.REF_BOOK)
                                        .orderByChild(Constants.BOOK_TITLE).addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Book book;
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            book = snapshot.getValue(Book.class);
                                            if (book != null) {
                                                if (book.getTitle().equals(matrixCursor.getString(1)))
                                                    onBookClick(book);
                                                Log.d("SearchBOOKRESULT", book.getTitle());

                                            }
                                        }
                                    }


                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                                return true;
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("SearchBOOK", databaseError.getMessage());
                    }
                });
    }
}
