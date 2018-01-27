package in.codepeaker.notely.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import in.codepeaker.notely.R;
import in.codepeaker.notely.activities.NotesEditActivity;
import in.codepeaker.notely.activities.NotesListActivity;
import in.codepeaker.notely.adapter.SwipeableWithButtonExampleAdapter;
import in.codepeaker.notely.contentprovider.NotesContract;
import in.codepeaker.notely.data.NotesData;

import static android.app.Activity.RESULT_OK;
import static in.codepeaker.notely.activities.NotesListActivity.NOTES_LOADER_ID;
import static in.codepeaker.notely.contentprovider.NotesContract.NotesEntry.CONTENT_URI;

/**
 * Created by github.com/codepeaker on 24/1/18.
 */
public class SwipeableWithButtonExampleFragment extends Fragment {

    public static final int REQUEST_ADD_CODE = 124;
    public SwipeableWithButtonExampleAdapter myItemAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;

    public SwipeableWithButtonExampleFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_list_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //noinspection ConstantConditions
        mRecyclerView = getView().findViewById(R.id.notes_recyclerview);
        mLayoutManager = new LinearLayoutManager(getContext());

        // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
        mRecyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        mRecyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        mRecyclerViewTouchActionGuardManager.setEnabled(true);

        // swipe manager
        mRecyclerViewSwipeManager = new RecyclerViewSwipeManager();


        Cursor cursor = getActivity().getContentResolver().query(CONTENT_URI, null, null, null, NotesContract.NotesEntry.COLUMN_LAST_UPDATED + " DESC");

        if (cursor == null)
            return;
        boolean[] pinnedArray = new boolean[cursor.getCount()];
        //adapter
        myItemAdapter = new SwipeableWithButtonExampleAdapter(getActivity(), pinnedArray);
        myItemAdapter.swapCursor(cursor);
        myItemAdapter.setEventListener(new SwipeableWithButtonExampleAdapter.EventListener() {
            @Override
            public void onItemPinned(int position) {
                ((NotesListActivity) getActivity()).onItemPinned(position);
            }

            @Override
            public void onItemViewClicked(View v) {
                handleOnItemViewClicked(v);
            }

            @Override
            public void onUnderSwipeableViewButtonClicked(View v) {
                handleOnUnderSwipeableViewButtonClicked(v);
            }
        });

        mAdapter = myItemAdapter;

        mWrappedAdapter = mRecyclerViewSwipeManager.createWrappedAdapter(myItemAdapter);      // wrap for swiping

        final GeneralItemAnimator animator = new SwipeDismissItemAnimator();

        // Change animations are enabled by default since support-v7-recyclerview v22.
        // Disable the change animation in order to make turning back animation of swiped item works properly.
        animator.setSupportsChangeAnimations(false);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);


        mRecyclerViewTouchActionGuardManager.attachRecyclerView(mRecyclerView);
        mRecyclerViewSwipeManager.attachRecyclerView(mRecyclerView);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (getActivity() != null)
            getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (getActivity() == null) {
            return false;
        }
        switch (item.getItemId()) {
            case R.id.action_add:

                startActivityForResult(new Intent(getActivity(), NotesEditActivity.class), REQUEST_ADD_CODE);

                break;
            case R.id.action_filter:
                if (!((NotesListActivity) getActivity()).drawer.isDrawerOpen(GravityCompat.END)) {
                    ((NotesListActivity) getActivity()).drawer.openDrawer(GravityCompat.END);
                }

                break;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_ADD_CODE:
                if (resultCode == RESULT_OK) {

                    Cursor cursor = getActivity().getContentResolver().query(CONTENT_URI, null, null, null, NotesContract.NotesEntry.COLUMN_LAST_UPDATED + " DESC");

                    if (cursor == null)
                        return;
                    myItemAdapter.swapCursor(cursor);
                    myItemAdapter.notifyDataSetChanged();

                }
                break;
        }

    }

    @Override
    public void onDestroyView() {
        if (mRecyclerViewSwipeManager != null) {
            mRecyclerViewSwipeManager.release();
            mRecyclerViewSwipeManager = null;
        }

        if (mRecyclerViewTouchActionGuardManager != null) {
            mRecyclerViewTouchActionGuardManager.release();
            mRecyclerViewTouchActionGuardManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mAdapter = null;
        mLayoutManager = null;

        super.onDestroyView();
    }

    private void handleOnItemViewClicked(View v) {
        int position = mRecyclerView.getChildAdapterPosition(v);
        if (position != RecyclerView.NO_POSITION) {

            ((NotesListActivity) getActivity()).onItemClicked(myItemAdapter.getNotesData(position));
        }
    }

    private void handleOnUnderSwipeableViewButtonClicked(View v) {
        int position = mRecyclerView.getChildAdapterPosition(v);
        if (position != RecyclerView.NO_POSITION) {
            NotesData notesData = myItemAdapter.getNotesData(position);

            ((NotesListActivity) getActivity()).onItemButtonClicked(notesData);

            if (notesData == null) {
                Toast.makeText(getActivity(), "try again later", Toast.LENGTH_SHORT).show();
            } else {
                long id = notesData.getId();
                Uri uri = CONTENT_URI.buildUpon().appendPath(id + "").build();
                int affectedRows = getActivity().getContentResolver().delete(uri, null, null);

                getActivity().getSupportLoaderManager().restartLoader(NOTES_LOADER_ID, null, ((NotesListActivity)getActivity()));
                Toast.makeText(getActivity(), affectedRows + " row deleted!", Toast.LENGTH_SHORT).show();
            }

            mAdapter.notifyItemRemoved(position);

        }
    }

    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }


    public void notifyItemChanged(int position) {
        mAdapter.notifyItemChanged(position);
    }

    public void notifyItemInserted(int position) {
        mAdapter.notifyItemInserted(position);
        mRecyclerView.scrollToPosition(position);
    }
}
