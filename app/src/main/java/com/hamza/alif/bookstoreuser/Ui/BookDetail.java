package com.hamza.alif.bookstoreuser.Ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;
import com.hamza.alif.bookstoreuser.Util.ActivityLancher;
import com.hamza.alif.bookstoreuser.Book;
import com.hamza.alif.bookstoreuser.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookDetail extends AppCompatActivity {
    StorageReference storageRef;
    private Book book;
    @BindView(R.id.bookIV)
    ImageView bookIV;
    @BindView(R.id.titleTV)
    TextView titleTV;
    @BindView(R.id.descriptionTV)
    TextView descriptionTV;
    @BindView(R.id.priceTV)
    TextView priceTV;
    @BindView(R.id.dateTV)
    TextView dateTV;
    @BindView(R.id.read_bookB)
    Button readbookB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        book = (Book) getIntent().getSerializableExtra(ActivityLancher.BOOK_KEY);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(book.getTitle());


        descriptionTV.setText(book.getDescription());
        titleTV.setText(book.getTitle());
        priceTV.setText(String.valueOf(book.getPrice()));
        dateTV.setText(book.getDate());
        Glide.with(this).load(book.getImageUrl()).into(bookIV);

    }

    @OnClick(R.id.read_bookB)
    public void readbook() {

        ActivityLancher.openPDFViewerActivity(this, book);
    }


}



