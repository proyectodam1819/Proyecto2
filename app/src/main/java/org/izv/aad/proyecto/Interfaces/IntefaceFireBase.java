package org.izv.aad.proyecto.Interfaces;

import com.google.firebase.auth.FirebaseUser;

import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.Objects.Book;

public interface IntefaceFireBase {

    void isCorrectlyLogUp(boolean isSuccessful, String error);

    Book getBook(Book book);

    Author getAuthor(Author author);

    FirebaseUser getUserLogin(FirebaseUser user);

}
