package in.codepeaker.notely.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.codepeaker.notely.R;
import in.codepeaker.notely.adapter.FilterAdapter;
import in.codepeaker.notely.adapter.SwipeableWithButtonExampleAdapter;
import in.codepeaker.notely.contentprovider.NotesContract;
import in.codepeaker.notely.data.NotesData;
import in.codepeaker.notely.fragments.SwipeableWithButtonExampleFragment;
import in.codepeaker.notely.utils.Constant;

import static in.codepeaker.notely.contentprovider.NotesContract.NotesEntry.COLUMN_FAV;
import static in.codepeaker.notely.contentprovider.NotesContract.NotesEntry.COLUMN_LAST_UPDATED;
import static in.codepeaker.notely.contentprovider.NotesContract.NotesEntry.COLUMN_STARRED;
import static in.codepeaker.notely.contentprovider.NotesContract.NotesEntry.CONTENT_URI;


public class NotesListActivity extends AppCompatActivity implements FilterAdapter.InteractionListener,
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {


    public static final String FRAGMENT_TAG_DATA_PROVIDER = "Data";
    public static final int NOTES_LOADER_ID = 10;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;
    @BindView(R.id.filter_apply_button)
    Button applyButton;
    HashMap<Integer, Boolean> hashMap = new HashMap<>();
    private String FRAGMENT_LIST_VIEW = "listview";
    private String TAG = NotesListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("");
        ButterKnife.bind(this);


        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sliding_pane_container, new SwipeableWithButtonExampleFragment(), FRAGMENT_LIST_VIEW)
                    .commit();
        }

        //Drawer view
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(NotesListActivity.this));
        String[] filterNames = {"Hearted", "Favourites", "Poems", "Stories"};
        FilterAdapter filterAdapter = new FilterAdapter(NotesListActivity.this, filterNames);
        recyclerView.setAdapter(filterAdapter);

        applyButton.setOnClickListener(this);

        getSupportLoaderManager().initLoader(NOTES_LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all notes
        getSupportLoaderManager().restartLoader(NOTES_LOADER_ID, null, this);
    }

    /**
     * This method will be called when a list item is pinned
     *
     * @param position The position of the item within data set
     */
    public void onItemPinned(int position) {
    }

    public void onItemClicked(NotesData notesData) {
        Intent intent = new Intent(NotesListActivity.this, NotesDetailActivity.class);
        intent.putExtra(Constant.notesData, notesData);
        startActivity(intent);

    }

    public void onItemButtonClicked(NotesData notesData) {

//
//        Snackbar snackbar = Snackbar.make(
//                findViewById(R.id.),
//                text,
//                Snackbar.LENGTH_SHORT);
//
//        snackbar.show();
    }

    @Override
    public void getDrawerListMap(HashMap<Integer, Boolean> hashMap) {
        this.hashMap = hashMap;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mCursor = null;

            @Override
            protected void onStartLoading() {
                if (mCursor != null)
                    deliverResult(mCursor);
                else
                    forceLoad();
            }

            @Override
            public Cursor loadInBackground() {

                try {
                    return getContentResolver().query(CONTENT_URI,
                            null,
                            null,
                            null,
                            NotesContract.NotesEntry.COLUMN_LAST_UPDATED + " DESC");

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void deliverResult(Cursor data) {
                mCursor = data;
                super.deliverResult(data);
            }

        };


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_VIEW);
        if (fragment instanceof SwipeableWithButtonExampleFragment) {
            ((SwipeableWithButtonExampleFragment) fragment).myItemAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_VIEW);
        if (fragment instanceof SwipeableWithButtonExampleFragment) {
            ((SwipeableWithButtonExampleFragment) fragment).myItemAdapter.swapCursor(null);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.filter_apply_button) {
            if (hashMap != null) {
                SwipeableWithButtonExampleAdapter swipeableWithButtonExampleAdapter = null;
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_VIEW);
                if (fragment instanceof SwipeableWithButtonExampleFragment) {
                    swipeableWithButtonExampleAdapter = ((SwipeableWithButtonExampleFragment) fragment).myItemAdapter;
                }

                if (swipeableWithButtonExampleAdapter == null)
                    return;


                if (hashMap.get(0) && hashMap.get(1)) {
                    Uri uri = CONTENT_URI.buildUpon().appendPath(NotesContract.FAV_AND_STAR).build();
                    Cursor cursor = getContentResolver().query(uri, null, COLUMN_FAV + "=? and " + COLUMN_STARRED + "=?", new String[]{"1", "1"}, COLUMN_LAST_UPDATED + " DESC");
                    if (cursor != null) {
                        swipeableWithButtonExampleAdapter.swapCursor(cursor);
                    }
                } else if (hashMap.get(0)) {
                    Uri uri = CONTENT_URI.buildUpon().appendPath(NotesContract.FAV).build();
                    Cursor cursor = getContentResolver().query(uri, null, COLUMN_FAV + "=?", new String[]{"1"}, COLUMN_LAST_UPDATED + " DESC");
                    if (cursor != null) {
                        swipeableWithButtonExampleAdapter.swapCursor(cursor);
                    }
                } else if (hashMap.get(1)) {
                    Uri uri = CONTENT_URI.buildUpon().appendPath(NotesContract.STAR).build();
                    Cursor cursor = getContentResolver().query(uri, null, COLUMN_STARRED + "=?", new String[]{"1"}, COLUMN_LAST_UPDATED + " DESC");
                    if (cursor != null) {
                        swipeableWithButtonExampleAdapter.swapCursor(cursor);
                    }
                }

            }

            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);

            }
        }
    }


//
//    // implements ItemPinnedMessageDialogFragment.EventListener
//    @Override
//    public void onNotifyItemPinnedDialogDismissed(int itemPosition, boolean ok) {
//        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_VIEW);
//
//        getDataProvider().getItem(itemPosition).setPinned(ok);
//        ((SwipeableWithButtonExampleFragment) fragment).notifyItemChanged(itemPosition);
//    }

}
