package org.izv.aad.proyecto.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseUser;

import org.izv.aad.proyecto.Adapters.AdapterIndex;
import org.izv.aad.proyecto.DataBase.Manager;
import org.izv.aad.proyecto.FireBase.FirebaseCustom;
import org.izv.aad.proyecto.Interfaces.InterfaceFireBase;
import org.izv.aad.proyecto.Interfaces.OnItemClickListener;
import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.Objects.Book;
import org.izv.aad.proyecto.R;
import org.izv.aad.proyecto.Utils.Gravatar;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class Index extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private RecyclerView recyclerBooks;
    private List<Book> books;
    private Manager manager;
    private TextView msg_error_index;
    private InterfaceFireBase interfaceFireBase;
    private ImageView index_imageUser;
    private AdapterIndex adapterIndex;

    public static int CODE_RESULT_MANAGEBOOKS_CREATE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        getEmailSharedPreferences();
    }

    /**********************************************************
     * ******************** INIT METHODS ******************** *
     **********************************************************/

    private void init(){

        getInterfacesMethorFirebase();
        updateBooksFromFirebase();
        initFloatingButton();
        initNavigarionDrawer();
        msg_error_index = findViewById(R.id.msg_error_index);
        manager = new Manager(this);
        books = new ArrayList<>();
        getBooks();
    }

    private void initFloatingButton(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manageBooks = new Intent(Index.this,ManageBooks.class);
                startActivityForResult(manageBooks, CODE_RESULT_MANAGEBOOKS_CREATE);
            }
        });
    }

    private void initNavigarionDrawer(){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        index_imageUser = headerView.findViewById(R.id.index_imageUser);
    }

    private void initRecycler(){
        recyclerBooks = findViewById(R.id.recyclerBooks);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerBooks.setLayoutManager(mLayoutManager);
        adapterIndex = new AdapterIndex(this, manager, books, new OnItemClickListener() {
            @Override
            public void onBookClickListener(Book book) {
                //Cuando se haga click hará algo aquí
                Intent manageBooks = new Intent(Index.this, ShowBook.class);
                manageBooks.putExtra("book", book);
                startActivity(manageBooks);
            }
        });
        recyclerBooks.setAdapter(adapterIndex);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //Close navigation
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        //Close app
        else {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**********************************************************
     * ***************** OPTIONS NAVIGATION ***************** *
     **********************************************************/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.sign_out) {
            removeSharedPreferences();
            removeBD();
            finish();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**********************************************************
     * ******************** UTILS METHODS ******************** *
     **********************************************************/

    private void getEmailSharedPreferences(){
        SharedPreferences pref = getSharedPreferences(Login.EMAIL, MODE_PRIVATE);
        String email = pref.getString("email", null);
        String imageUrl = null;
        if(email != null) {
            imageUrl = Gravatar.codeGravatarImage(email);
        }
        setGravatar(imageUrl);

    }

    private void setGravatar(String imageUrl){
        Glide.with(this)
            .load(imageUrl)
            .apply(
                new RequestOptions()
                    .placeholder(R.mipmap.ic_launcher)
                    .fitCenter()
            )
            .into(index_imageUser);
    }

    private void removeSharedPreferences(){
        SharedPreferences pref = getSharedPreferences(Login.EMAIL, MODE_PRIVATE);
        pref.edit().clear().commit();
        pref = getSharedPreferences(Login.SAVE_SHARED_PREFERENCES, MODE_PRIVATE);
        pref.edit().clear().commit();
    }

    private void getBooks(){
        ThreadGetBooks hilo = new ThreadGetBooks();
        hilo.execute();
    }

    private void checkRecyclerBooks(){
        if(books.size() <= 0){
            msg_error_index.setText(getString(R.string.no_books));
            msg_error_index.setVisibility(View.VISIBLE);
        }else{
            msg_error_index.setVisibility(View.GONE);
            initRecycler();
        }
    }

    private void removeBD(){
        manager.dropTables();
    }

    private void updateBooksFromFirebase(){
        FirebaseCustom.getAllBooks(interfaceFireBase);
    }

    private void getInterfacesMethorFirebase(){
        interfaceFireBase = new InterfaceFireBase() {
            @Override
            public void isCorrectlyLogUp(boolean isSuccessful, String error) {

            }

            @Override
            public Book getBook(Book book) {
                return null;
            }

            @Override
            public Author getAuthor(Author author) {
                return null;
            }

            @Override
            public void getUserLogin(FirebaseUser user, String error) {

            }

            @Override
            public List<Book> getAllBooks(List<Book> books) {
                for(Book book : books){
                    //saveBooks(book);
                }
                return books;
            }

            @Override
            public String sendRoutePhoto(String string) {
                return null;
            }

            @Override
            public String getRoutePhoto(String string) {
                return null;
            }
        };
    }

    private void saveBooks(Book book){
        Long id = manager.insertLibro(book);
        book.setId(id);
        book = FirebaseCustom.saveBook(book);
        manager.updateLibro(book);
    }

    /**********************************************************
     * ******************** ANOTHER CLASS ******************** *
     **********************************************************/


    private class ThreadGetBooks extends AsyncTask <Integer, String, List<Book>> {

        public ThreadGetBooks(){}

        @Override
        protected List<Book> doInBackground(Integer... integers) {
            List<Book> books = manager.getAllBooks();

            if(books.size() <=0 ){
                books = new ArrayList<>();
            }
            return books;
        }

        @Override
        protected void onPostExecute(List<Book> booksThread) {
            super.onPostExecute(books);
            books = booksThread;
            checkRecyclerBooks();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == CODE_RESULT_MANAGEBOOKS_CREATE){
            Book book = data.getParcelableExtra("book");
            books.add(book);
            saveBooks(book);
            if(adapterIndex == null) {
                initRecycler();
            }
            adapterIndex.notifyDataSetChanged();
            checkRecyclerBooks();
        }
    }
}
