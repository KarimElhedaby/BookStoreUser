package com.hamza.alif.bookstoreuser.Util;

import android.content.Context;
import android.content.Intent;

import com.hamza.alif.bookstoreuser.Book;
import com.hamza.alif.bookstoreuser.Ui.BookDetail;
import com.hamza.alif.bookstoreuser.Ui.PDFViewerActivity;


/**
 * Created by karim pc on 9/29/2017.
 */

public final class ActivityLancher {
    public static final String BOOK_KEY = "book";
    public static final String publisher_KEY = "publisher";





    public static void openPDFViewerActivity(Context context, Book book){
        Intent i = new Intent(context, PDFViewerActivity.class);
        i.putExtra(BOOK_KEY, book);
        context.startActivity(i);
    }
    public static void openBookDetails(Context context, Book book){
        Intent i = new Intent(context, BookDetail.class);
        i.putExtra(BOOK_KEY, book);
        context.startActivity(i);
    }


}
