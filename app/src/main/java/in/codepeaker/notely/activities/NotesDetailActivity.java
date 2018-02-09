package in.codepeaker.notely.activities;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.codepeaker.notely.NotesWidgetProvider;
import in.codepeaker.notely.R;
import in.codepeaker.notely.data.NotesData;
import in.codepeaker.notely.service.WidgetUpdateService;
import in.codepeaker.notely.utils.Constant;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NotesDetailActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_FOR_EDIT_SCREEN = 105;

    @BindView(R.id.notes_detail_title)
    TextView notesTitle;

    @BindView(R.id.notes_detail_desc)
    TextView notesDesc;

    @BindView(R.id.notes_detail_last_updated)
    TextView notesLastUpdated;

    @BindView(R.id.add_widget)
    TextView notesAddWidget;

    @BindView(R.id.edit_textview)
    TextView notesEditTextView;

    @BindView(R.id.back_button)
    ImageView backButton;

    NotesData notesData;
    private int notesPosition;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_notes_detail);

        ButterKnife.bind(this);

        initAction();
    }

    private void initAction() {
        if (getIntent().getExtras() == null)
            return;
        notesData = (NotesData) getIntent().getExtras().get(Constant.notesData);
        notesPosition = getIntent().getExtras().getInt(Constant.notesPosition);

        if (notesData == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notesTitle.setTransitionName(notesPosition + "");
        }
        notesTitle.setText(notesData.getNotesTitle());
        notesDesc.setText(notesData.getNotesDesc());

        notesLastUpdated.setText(String.format("Last updated: %s", notesData.getNotesLastUpdate()));

        notesEditTextView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotesDetailActivity.this, NotesEditActivity.class);
                intent.putExtra(Constant.notesData, notesData);
                intent.putExtra(Constant.isEditNote, true);
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        NotesDetailActivity.this,
                        notesTitle,
                        "simple_title_animation"
                );
                startActivityForResult(intent, REQUEST_CODE_FOR_EDIT_SCREEN, activityOptionsCompat.toBundle());
            }
        });

        notesAddWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(NotesDetailActivity.this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(
                        NotesDetailActivity.this, NotesWidgetProvider.class
                ));

                if (appWidgetIds.length == 0) {
                    Toast.makeText(NotesDetailActivity.this, "please make a widget first", Toast.LENGTH_SHORT).show();
                } else {

                    WidgetUpdateService.startActionNotesWidget(NotesDetailActivity.this,
                            notesData, notesPosition);

                    Toast.makeText(NotesDetailActivity.this, "Your Note has been added", Toast.LENGTH_SHORT).show();
                }
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
        if (requestCode == REQUEST_CODE_FOR_EDIT_SCREEN && resultCode == RESULT_OK) {
            notesData = (NotesData) data.getExtras().get(Constant.notesData);

            notesTitle.setText(notesData.getNotesTitle());
            notesDesc.setText(notesData.getNotesDesc());
            notesLastUpdated.setText(String.format("Last updated: %s", notesData.getNotesLastUpdate()));

        } else {

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
