package org.izv.aad.proyecto.Interfaces;

import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.Objects.Book;

public interface IntefaceFireBase {

    void isCorrectlyLogUp(boolean isSuccessful);

    Book getBook(Book book);

    Author getAuthor(Author author);

}
