package edu.ucsd.cse110.zooseeker_team35.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.zooseeker_team35.location_tracking.DirectionTracker;
import edu.ucsd.cse110.zooseeker_team35.path_finding.IdentifiedWeightedEdge;
import edu.ucsd.cse110.zooseeker_team35.R;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooPathFinder;


public class PlanResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_results);

        Graph<String, IdentifiedWeightedEdge> zooGraph = ZooData.loadZooGraphJSON(this.getApplicationContext(), ZooInfoProvider.zooGraphJSON);
        ZooPathFinder zooPathFinder = new ZooPathFinder(zooGraph);
        List<String> targetExhibits = ZooInfoProvider.getSelectedExhibits(this.getApplicationContext()).stream().map(vertex -> vertex.id).collect(Collectors.toList());
        List<GraphPath<String, IdentifiedWeightedEdge>> pathList = zooPathFinder.calculatePath("entrance_exit_gate", "entrance_exit_gate", targetExhibits);
        DirectionTracker.initialize(zooGraph, pathList);

        List<String> planSummary = calculatePlanSummary(pathList);
        ListView listView = findViewById(R.id.plan_summary);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                planSummary);
        listView.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.getBoolean("isMidPlan") == true) {
            SharedPreferences preferences = getSharedPreferences("shared", MODE_PRIVATE);
            DirectionTracker.setCurrentExhibit(preferences.getInt("currentExhibit", -1));
            Intent intent = new Intent(this, DirectionsActivity.class);
            intent.putExtra("use_location_updated", true);
            startActivity(intent);
        }
    }

    public List<String> calculatePlanSummary(List<GraphPath<String, IdentifiedWeightedEdge>> pathList){

        List<String> planSummary = new ArrayList<>();
        planSummary.add(ZooInfoProvider.getVertexWithId(pathList.get(0).getStartVertex()).name + "\n\n");
        double distance = 0;
        for (GraphPath<String, IdentifiedWeightedEdge> path : pathList){
            distance += path.getWeight();
            planSummary.add( ZooInfoProvider.getVertexWithId(path.getEndVertex()).name + " - " + distance + " feet away \n\n");
        }

        return planSummary;
    }

    //
    public void onDirectionsButtonClicked(View view) {
        /*
        TextView mockRouteTv = this.findViewById(R.id.mockRouteTv);
        if(!mockRouteTv.getText().toString().equals("")) {
            Intent intent = new Intent(this, DirectionsActivity.class);
            intent.putExtra("mockRoute", mockRouteTv.getText().toString());
            intent.putExtra("use_location_updated", false);

            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, DirectionsActivity.class);
            intent.putExtra("use_location_updated", true);
            startActivity(intent);
        }
        */
        Intent intent = new Intent(this, DirectionsActivity.class);
        startActivity(intent);
    }

    public void onBackButtonClicked(View view) {
        finish();
    }
}