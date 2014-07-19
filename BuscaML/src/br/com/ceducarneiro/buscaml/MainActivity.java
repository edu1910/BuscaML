package br.com.ceducarneiro.buscaml;

import android.R;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.widget.SearchView;
import java.util.List;

public class MainActivity extends SherlockListActivity implements MLParser.MLParserCallback {

    private MLParser parser;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        requestWindowFeature(Window.FEATURE_PROGRESS);

        parser = new MLParser(MainActivity.this);

        getSupportActionBar().show();
        setSupportProgressBarIndeterminate(true);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        MLParser.MLProduct product = (MLParser.MLProduct) l.getAdapter().getItem(position);

        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(product.link));
        startActivity(it);
    }

    @Override
    public void onSuccess(List<MLParser.MLProduct> products) {
        setSupportProgressBarIndeterminateVisibility(false);
        ProductAdapter adapter = new ProductAdapter(products);
        setListAdapter(adapter);
    }

    @Override
    public void onError() {
        setSupportProgressBarIndeterminateVisibility(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final SearchView searchView = new SearchView(getSupportActionBar().getThemedContext());
        searchView.setQueryHint("Search");

        menu.add(Menu.NONE, Menu.NONE, 1,"Search")
                .setIcon(R.drawable.ic_menu_search)
                .setActionView(searchView)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                setListAdapter(null);
                setSupportProgressBarIndeterminateVisibility(true);
                parser.getProducts(query);

                return false;
            }
        });

        return true;
    }
}