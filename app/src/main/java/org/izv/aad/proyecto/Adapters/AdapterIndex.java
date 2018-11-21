package org.izv.aad.proyecto.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.izv.aad.proyecto.Activities.Index;
import org.izv.aad.proyecto.Interfaces.OnItemClickListener;
import org.izv.aad.proyecto.Objects.Book;

import java.util.List;

public class AdapterIndex extends RecyclerView.Adapter <AdapterIndex.MyViewHolder> {

    private List<Book> books;
    private OnItemClickListener listener;

    public AdapterIndex(Index index, List<Book> books, OnItemClickListener listener) {
        this.books = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Inflar el layout
        View itemView = null;
        //View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card, viewGroup,false);
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
        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

        }

        public void bind(final Book book, final OnItemClickListener listener) {

            /***************************************************************************** *
            * AQUÍ ES DONDE SE LE DAN LOS VALORES A LOS ELEMENTOS DE LA CLASE MYVIEWHOLDER *
            * ******************************************************************************/

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onBookClickListener(book);
                }
            });
        }

    }
}
