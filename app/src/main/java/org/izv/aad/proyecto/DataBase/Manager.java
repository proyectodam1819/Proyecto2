package org.izv.aad.proyecto.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.Objects.Book;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Manager {

    private AuthorHelper authorHelper;
    private BookHelper bookHelper;
    private SQLiteDatabase bdLibro, bdAutor;

    public Manager(Context context){
        this.authorHelper = new AuthorHelper(context);
        this.bookHelper = new BookHelper(context);
        bdAutor = authorHelper.getWritableDatabase();
        bdLibro = bookHelper.getWritableDatabase();
    }

    public void close(){
        bdAutor.close();
        bdLibro.close();
    }

    /*************************************************
     ********************   INSERT  ******************
     *************************************************/

    public long insertAutor(Author author){
        return bdAutor.insert(Contract.AuthorTable.TABLE_NAME, null, Contract.contentValuesAuthor(author));
    }

    public long insertLibro(Book book){
        return bdLibro.insert(Contract.BookTable.TABLE_NAME, null, Contract.contentValuesBook(book));
    }

    /*************************************************
     ********************   DELETE  ******************
     *************************************************/

    public int deleteLibro(Book book){
        return deleteLibro(book.getId());
    }

    public int deleteLibro(long id){
        String condicion = Contract.BookTable._ID + " = ?";
        String[] argumentos = { id + "" };
        return bdLibro.delete(Contract.BookTable.TABLE_NAME, condicion, argumentos);
    }

    public int deleteAutor(Author author){
        return deleteAutor(author.getId());
    }

    public int deleteAutor(long id){
        String condicion = Contract.AuthorTable._ID + " = ?";
        String[] argumentos = { id + "" };
        return bdAutor.delete(Contract.AuthorTable.TABLE_NAME, condicion, argumentos);
    }

    /*************************************************
     ********************   UPDATE  ******************
     *************************************************/

    public int updateLibro(Book book){
        String condicion = Contract.BookTable._ID + " = ?";
        String[] argumentos = { book.getId() + "" };
        return bdLibro.update(Contract.BookTable.TABLE_NAME, Contract.contentValuesBook(book), condicion, argumentos);
    }

    public int updateAuthor(Author author){
        String condicion = Contract.AuthorTable._ID + " = ?";
        String[] argumentos = { author.getId() + "" };
        return bdAutor.update(Contract.AuthorTable.TABLE_NAME, Contract.contentValuesAuthor(author), condicion, argumentos);
    }

    /*************************************************
     ********************   CURSOR  ******************
     *************************************************/

    private Cursor getCursorAuthor(){
        return getCursorAuthor(null, null);
    }

    private Cursor getCursorAuthor(String condicion, String[] argumentos){
        return bdAutor.query(
                Contract.AuthorTable.TABLE_NAME,
                null,
                condicion,
                argumentos,
                null,
                null,
                Contract.AuthorTable.COLUMN_NAME + " desc",
                null
        );
    }

    private Cursor getCursorBook(){
        return getCursorBook(null, null);
    }

    private Cursor getCursorBook(String condicion, String[] argumentos){
        return bdAutor.query(
                Contract.BookTable.TABLE_NAME,
                null,
                condicion,
                argumentos,
                null,
                null,
                Contract.BookTable.COLUMN_ID_AUTHOR + " desc",
                null
        );
    }

    /*************************************************
     *******************   GET_ROW  ******************
     *************************************************/

    private Book getRowBook(Cursor cursor){
        Book book = new Book();

        int posId = cursor.getColumnIndex(Contract.BookTable._ID);
        int posIdAuthor = cursor.getColumnIndex(Contract.BookTable.COLUMN_ID_AUTHOR);
        int posTitle = cursor.getColumnIndex(Contract.BookTable.COLUMN_TITLE);
        int posUrlPhoto = cursor.getColumnIndex(Contract.BookTable.COLUMN_URL_PHOTO);
        int posResume = cursor.getColumnIndex(Contract.BookTable.COLUMN_RESUME);
        int posAssessment = cursor.getColumnIndex(Contract.BookTable.COLUMN_ASSESSMENT);
        int posFavorite = cursor.getColumnIndex(Contract.BookTable.COLUMN_FAVORITE);
        int posStartDate = cursor.getColumnIndex(Contract.BookTable.COLUMN_START_DATE);
        int posEndDate = cursor.getColumnIndex(Contract.BookTable.COLUMN_END_DATE);
        int posKey = cursor.getColumnIndex(Contract.BookTable.COLUMN_KEY);

        book.setTitle(cursor.getString(posTitle));
        book.setUrlPhoto(cursor.getString(posUrlPhoto));
        book.setResume(cursor.getString(posResume));
        book.setId(cursor.getLong(posId));
        book.setIdAuthor(cursor.getLong(posIdAuthor));
        book.setAssessment(cursor.getInt(posAssessment));
        book.setKey(cursor.getString(posKey));
        if(cursor.getInt(posFavorite) > 0){
            book.setFavorite(true);
        }else{
            book.setFavorite(false);
        }
        book.setStartDate(Date.valueOf(cursor.getString(posStartDate)));
        book.setEndDate(Date.valueOf(cursor.getString(posEndDate)));

        return book;
    }

    private Author getRowAutor(Cursor cursor){
        Author author = new Author();
        int posId = cursor.getColumnIndex(Contract.AuthorTable._ID);
        int posName = cursor.getColumnIndex(Contract.AuthorTable.COLUMN_NAME);
        int posKey = cursor.getColumnIndex(Contract.AuthorTable.COLUMN_KEY);

        author.setId(cursor.getLong(posId));
        author.setName(cursor.getString(posName));
        author.setKey(cursor.getString(posKey));

        return author;
    }

    /*************************************************
     ********************   SELECT  ******************
     *************************************************/

    public List<Book> getAllBooks(String condicion){
        List<Book> books = new ArrayList<>();
        Cursor cursor = getCursorBook(condicion, null);
        if(condicion == null){
            cursor = getCursorBook();
        }
        Book book;

        while(cursor.moveToNext()){
            book = getRowBook(cursor);
            books.add(book);
        }
        return books;
    }

    public List<Author> getAllAuthor(String condicion){
        List<Author> authors = new ArrayList<>();
        Cursor cursor = getCursorAuthor(condicion, null);
        if(condicion == null){
            cursor = getCursorAuthor();
        }
        Author author;

        while(cursor.moveToNext()){
            author = getRowAutor(cursor);
            authors.add(author);
        }
        return authors;
    }
}
