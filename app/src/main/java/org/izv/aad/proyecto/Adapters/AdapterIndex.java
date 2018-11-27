package org.izv.aad.proyecto.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.izv.aad.proyecto.Activities.Index;
import org.izv.aad.proyecto.DataBase.Manager;
import org.izv.aad.proyecto.Interfaces.OnItemClickListener;
import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.Objects.Book;
import org.izv.aad.proyecto.R;

import java.util.List;

public class AdapterIndex extends RecyclerView.Adapter <AdapterIndex.MyViewHolder> {

    private List<Book> books;
    private OnItemClickListener listener;
    private Manager manager;

    public AdapterIndex(Manager manager, List<Book> books, OnItemClickListener listener) {
        this.manager = manager;
        this.books = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Inflar el layout
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recyclerbook, viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterIndex.MyViewHolder myViewHolder, int i) {


        myViewHolder.bind(books.get(i), listener);
    }


    @Override
    public int getItemCount() {
        return books.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView item_title, item_author;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.item_title = itemView.findViewById(R.id.item_title);
            this.item_author = itemView.findViewById(R.id.item_author);
        }

        public void bind(final Book book, final OnItemClickListener listener) {

            /***************************************************************************** *
            * AQUÍ ES DONDE SE LE DAN LOS VALORES A LOS ELEMENTOS DE LA CLASE MYVIEWHOLDER *
            * ******************************************************************************/

            Author author = manager.getAuthor(book.getIdAuthor());

            String nameAuthor = "";
            if(author != null) {
                nameAuthor = author.getName();
            }

            Log.v("ZZTY", book.getTitle().length()+ "");

            item_title.setText(book.getTitle());
            item_author.setText(nameAuthor);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onBookClickListener(book);
                }
            });
        }

    }
}
