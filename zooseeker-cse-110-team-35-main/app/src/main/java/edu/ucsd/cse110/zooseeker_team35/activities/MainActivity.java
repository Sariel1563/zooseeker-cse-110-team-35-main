package edu.ucsd.cse110.zooseeker_team35.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Map;

import edu.ucsd.cse110.zooseeker_team35.R;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatusDao;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatusDatabase;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Map<String, ZooData.VertexInfo> vertexInfo = ZooData.loadVertexInfoJSON(this.getApplicationContext(), ZooInfoProvider.nodeInfoJSON);
        Map<String, ZooData.EdgeInfo> edgeInfo = ZooData.loadEdgeInfoJSON(this.getApplicationContext(), ZooInfoProvider.edgeInfoJSON);
        ZooInfoProvider.setIdVertexMap(vertexInfo);
        ZooInfoProvider.setIdEdgeMap(edgeInfo);
        SharedPreferences preferences = getSharedPreferences("shared", MODE_PRIVATE);
/*
        ExhibitStatusDao dao = ExhibitStatusDatabase.getSingleton(this).exhibitStatusDao();
        if(!dao.getVisited(true).isEmpty()) {
            Intent intent = new Intent(this, PlanResultsActivity.class);
            intent.putExtra("isMidPlan", true);
            startActivity(intent);
        }
        */
        if(preferences.getInt("currentExhibit", -1) != -1) {
            Intent intent = new Intent(this, PlanResultsActivity.class);
            intent.putExtra("isMidPlan", true);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }
}