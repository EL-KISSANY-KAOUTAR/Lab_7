package com.example.starsgallery.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.starsgallery.R;
import com.example.starsgallery.adapter.StarAdapter;
import com.example.starsgallery.service.StarService;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewerr;
    private StarAdapter  adapterrr;
    private TextView     tvvvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Une seule toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recyclerViewerr = findViewById(R.id.recycle_view);
        tvvvEmpty      = findViewById(R.id.tvEmpty);
        StarService service = StarService.getInstance();

        recyclerViewerr.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewerr.setHasFixedSize(true);

        adapterrr = new StarAdapter(service.findAll());
        recyclerViewerr.setAdapter(adapterrr);

        checkEmptyState();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        if (searchItem != null) {
            SearchView searchView = (SearchView) searchItem.getActionView();
            if (searchView != null) {
                searchView.setQueryHint("Recherchez votre célébrité préféré(e)");

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        searchView.clearFocus();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newTextt) {
                        if (adapterrr != null) {
                            adapterrr.getFilter().filter(newTextt);
                        }
                        return true;
                    }
                });
            }

            searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(@NonNull MenuItem item) {
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(@NonNull MenuItem item) {
                    if (adapterrr != null) {
                        adapterrr.getFilter().filter("");
                    }
                    return true;
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem ittem) {
        if (ittem.getItemId() == R.id.share) {
            partagerApplication();
            return true;
        }
        return super.onOptionsItemSelected(ittem);
    }

    private void partagerApplication() {
        new ShareCompat.IntentBuilder(this)
                .setType("text/plain")
                .setChooserTitle("Partager Stars Gallery via...")
                .setText("Découvrez Stars Gallery !")
                .startChooser();
    }

    private void checkEmptyState() {
        if (adapterrr != null) {
            if (adapterrr.getItemCount() == 0) {
                recyclerViewerr.setVisibility(View.GONE);
                tvvvEmpty.setVisibility(View.VISIBLE);
            } else {
                recyclerViewerr.setVisibility(View.VISIBLE);
                tvvvEmpty.setVisibility(View.GONE);
            }
        }
    }
}
