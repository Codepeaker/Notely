package in.codepeaker.notely.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.codepeaker.notely.R;
import in.codepeaker.notely.data.NotesData;
import in.codepeaker.notely.utils.Constant;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NotesDetailActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_FOR_EDIT_SCREEN = 105;

    @BindView(R.id.notes_detail_title)
    TextView notesTitle;

    @BindView(R.id.notes_detail_desc)
    TextView notesDesc;

    @BindView(R.id.notes_detail_last_updated)
    TextView notesLastUpdated;

    @BindView(R.id.edit_textview)
    TextView notesEditTextView;

    @BindView(R.id.back_button)
    ImageView backButton;

    NotesData notesData;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_detail);

        ButterKnife.bind(this);
        if (getIntent().getExtras() == null)
            return;
     notesData = (NotesData) getIntent().getExtras().get(Constant.notesData);

        if (notesData == null)
            return;


        notesTitle.setText(notesData.getNotesTitle());
        notesDesc.setText(notesData.getNotesDesc());
        notesLastUpdated.setText(String.format("Last updated: %s", notesData.getNotesLastUpdate()));

        notesEditTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(NotesDetailActivity.this, NotesEditActivity.class)
                        .putExtra(Constant.notesData, notesData)
                        .putExtra(Constant.isEditNote, true), REQUEST_CODE_FOR_EDIT_SCREEN);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_FOR_EDIT_SCREEN && resultCode == RESULT_OK){
            notesData = (NotesData) data.getExtras().get(Constant.notesData);

            notesTitle.setText(notesData.getNotesTitle());
            notesDesc.setText(notesData.getNotesDesc());
            notesLastUpdated.setText(String.format("Last updated: %s", notesData.getNotesLastUpdate()));

        }else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }
}
