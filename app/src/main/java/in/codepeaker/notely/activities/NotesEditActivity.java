package in.codepeaker.notely.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.werdpressed.partisan.rundo.RunDo;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.codepeaker.notely.BuildConfig;
import in.codepeaker.notely.R;
import in.codepeaker.notely.adapter.SwipeableWithButtonExampleAdapter;
import in.codepeaker.notely.contentprovider.NotesContract;
import in.codepeaker.notely.data.NotesData;
import in.codepeaker.notely.utils.Constant;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static in.codepeaker.notely.contentprovider.NotesContract.NotesEntry.COLUMN_DESC;
import static in.codepeaker.notely.contentprovider.NotesContract.NotesEntry.COLUMN_FAV;
import static in.codepeaker.notely.contentprovider.NotesContract.NotesEntry.COLUMN_LAST_UPDATED;
import static in.codepeaker.notely.contentprovider.NotesContract.NotesEntry.COLUMN_POEM;
import static in.codepeaker.notely.contentprovider.NotesContract.NotesEntry.COLUMN_STARRED;
import static in.codepeaker.notely.contentprovider.NotesContract.NotesEntry.COLUMN_STORY;
import static in.codepeaker.notely.contentprovider.NotesContract.NotesEntry.COLUMN_TITLE;
import static in.codepeaker.notely.contentprovider.NotesContract.NotesEntry.CONTENT_URI;

public class NotesEditActivity extends AppCompatActivity implements RunDo.TextLink {

    @BindView(R.id.notes_add_description)
    EditText notesDescEditText;

    @BindView(R.id.notes_add_title)
    EditText notesTitleEditText;

    boolean isEditNote = false;
    NotesData notesData;
    private RunDo runDo;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("");
        ButterKnife.bind(this);

        runDo = RunDo.Factory.getInstance(getSupportFragmentManager());
        runDo.setQueueSize(200);
        runDo.setTimerLength(200);


        if (getIntent().getExtras() == null)
            return;
        notesData = (NotesData) getIntent().getExtras().get(Constant.notesData);

        if (notesData != null) {
            notesTitleEditText.setText(notesData.getNotesTitle());
            notesDescEditText.setText(notesData.getNotesDesc());
        }

        if (getIntent().getExtras().getBoolean(Constant.isEditNote)) {
            isEditNote = true;
        }

        MobileAds.initialize(this, BuildConfig.ADDMOB_APP_ID_STRING);

        mInterstitialAd = new InterstitialAd(this);
//        ca-app-pub-2004734361017408/5451211810 original
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712"); //test unit
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add_notes, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.undo_note:
                if (notesTitleEditText.isFocused()) {
                    Toast.makeText(this, "Undo is not available for Title", Toast.LENGTH_SHORT).show();
                    break;
                }
                runDo.undo();
                break;
            case R.id.save_note:

                if (mInterstitialAd!=null&&mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                String title = notesTitleEditText.getText().toString();
                String desc = notesDescEditText.getText().toString(); //can be empty

                if (title.isEmpty()) {
                    Toast.makeText(this, "Title can not be empty", Toast.LENGTH_SHORT).show();
                    break;
                }

                ContentValues contentValues = new ContentValues();

                if (isEditNote) {
                    if (notesData != null) {
                        contentValues.put(COLUMN_TITLE, title);
                        contentValues.put(COLUMN_DESC, desc);
                        contentValues.put(COLUMN_LAST_UPDATED, new Date().getTime()); //TODO change to int
                        contentValues.put(COLUMN_FAV, notesData.getIsFav());
                        contentValues.put(COLUMN_STARRED, notesData.getIsStarred());
                        contentValues.put(COLUMN_STORY, notesData.getIsStory());
                        contentValues.put(COLUMN_POEM, notesData.getIsPoem());
                        contentValues.put(NotesContract.NotesEntry._ID, notesData.getId());

                        Uri uri = CONTENT_URI.buildUpon().appendPath(notesData.getId() + "").build();

                        int notesUpdated = getContentResolver().update(uri, contentValues, null, null);

                        if (notesUpdated > 0) {
                            notesData.setNotesTitle(title);
                            notesData.setNotesDesc(desc);
                            notesData.setNotesLastUpdate(SwipeableWithButtonExampleAdapter
                                    .getDateStringfromMilliseconds(new Date().getTime()));

                            setResult(RESULT_OK, new Intent().putExtra(Constant.notesData, notesData));

                            Toast.makeText(this, notesUpdated + " note is updated", Toast.LENGTH_SHORT).show();

                        } else {
                            setResult(RESULT_CANCELED);
                            Toast.makeText(this, "Unable to update note", Toast.LENGTH_SHORT).show();
                        }

                    }

                } else {

                    contentValues.put(COLUMN_TITLE, title);
                    contentValues.put(COLUMN_DESC, desc);

                    contentValues.put(COLUMN_LAST_UPDATED, new Date().getTime());
                    Uri uri = getContentResolver().insert(CONTENT_URI, contentValues);
                    if (uri != null)
                        Toast.makeText(this, R.string.saved_successfully, Toast.LENGTH_SHORT).show();

                    setResult(RESULT_OK);
                    finish();
                }

                break;

        }
        return true;
    }

    @Override
    public EditText getEditTextForRunDo() {
//      if (notesTitleEditText.isFocused()){
//          return notesTitleEditText;
//      }else {
        return notesDescEditText;
//      }

    }


}
