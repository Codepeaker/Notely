package in.codepeaker.notely.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.codepeaker.notely.R;
import in.codepeaker.notely.data.NotesData;
import in.codepeaker.notely.utils.Constant;

public class NotesDetailActivity extends AppCompatActivity {

    @BindView(R.id.notes_detail_title)
    TextView notesTitle;

    @BindView(R.id.notes_detail_desc)
    TextView notesDesc;

    @BindView(R.id.notes_detail_last_updated)
    TextView notesLastUpdated;

    @BindView(R.id.edit_textview)
    TextView notesEditTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_detail);

        ButterKnife.bind(this);
        if (getIntent().getExtras() == null)
            return;
        final NotesData notesData = (NotesData) getIntent().getExtras().get(Constant.notesData);

        if (notesData == null)
            return;

        notesTitle.setText(notesData.getNotesTitle());
        notesDesc.setText(notesData.getNotesDesc());
        notesLastUpdated.setText(notesData.getNotesLastUpdate());

        notesEditTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotesDetailActivity.this, NotesEditActivity.class)
                        .putExtra(Constant.notesData, notesData)
                        .putExtra(Constant.isEditNote, true));
            }
        });

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
