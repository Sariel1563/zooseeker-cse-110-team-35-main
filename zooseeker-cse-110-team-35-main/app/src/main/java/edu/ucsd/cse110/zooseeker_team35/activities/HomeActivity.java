package edu.ucsd.cse110.zooseeker_team35.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import edu.ucsd.cse110.zooseeker_team35.adapters.ExhibitsAdapter;
import edu.ucsd.cse110.zooseeker_team35.R;
import edu.ucsd.cse110.zooseeker_team35.location_tracking.LocationPermissionChecker;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatus;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatusDao;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatusDatabase;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<ZooData.VertexInfo> exhibits;
    private ExhibitsAdapter adapter;
    private TextView noExhibitsTextView;
    private TextView exhibitsCountTextView;
    private final LocationPermissionChecker permissionChecker = new LocationPermissionChecker(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        exhibits = ZooInfoProvider.getSelectedExhibits(getApplicationContext());

        adapter = new ExhibitsAdapter();
        adapter.setHasStableIds(true);
        recyclerView = findViewById(R.id.added_exhibits_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        noExhibitsTextView = (TextView) findViewById(R.id.no_exhibit);
        exhibitsCountTextView = (TextView) findViewById(R.id.exhibit_count);

        updateDisplay();

        if(getIntent().getBooleanExtra("reset", false)) {
            onResetButtonClicked(this.findViewById(R.id.resetBtn));
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        /* Permission Setup */
        if (this.ensurePermissions()) return;
    }

    //functionality when the plan button is clicked
    public void onPlanButtonClicked(View view) {
        if (exhibits.size() > 0){
            Intent intent = new Intent(this, PlanResultsActivity.class);
            startActivity(intent);
        }
    }

    //functionality when the search button is clicked
    public void onSearchButtonClicked(View view) {
        Intent intent = new Intent(this, SearchResultsActivity.class);

        //pass in the searchTerm as an extra to the SearchResultsActivity
        TextView searchTermView = (TextView)findViewById(R.id.search_text);
        String searchTerm = searchTermView.getText().toString();
        intent.putExtra("searchTerm", searchTerm);

        startActivity(intent);
        searchTermView.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        exhibits = ZooInfoProvider.getSelectedExhibits(getApplicationContext());
        updateDisplay();
    }

    public void updateDisplay() {
        if (exhibits.isEmpty()) {
            noExhibitsTextView.setVisibility(View.VISIBLE);
            exhibitsCountTextView.setVisibility(View.INVISIBLE);
        }
        else {
            noExhibitsTextView.setVisibility(View.INVISIBLE);
            exhibitsCountTextView.setVisibility(View.VISIBLE);
            exhibitsCountTextView.setText(Integer.toString(exhibits.size()));
        }
        adapter.setExhibits(exhibits);
    }

    private boolean ensurePermissions() {
        return permissionChecker.ensurePermissions();
    }

    public void onResetButtonClicked(View view) {
        ExhibitStatusDao dao = ExhibitStatusDatabase.getSingleton(this).exhibitStatusDao();
        List<ExhibitStatus> databaseExhibits = dao.getAll();
        SharedPreferences preferences = getSharedPreferences("shared", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        for(ExhibitStatus e : databaseExhibits) {
            e.setIsAdded(false);
            e.setIsVisited(false);
            dao.update(e);
        }

        exhibits = ZooInfoProvider.getSelectedExhibits(this);
        updateDisplay();
    }
}