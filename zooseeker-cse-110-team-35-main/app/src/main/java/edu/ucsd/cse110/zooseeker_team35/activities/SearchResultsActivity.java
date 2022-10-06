package edu.ucsd.cse110.zooseeker_team35.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.zooseeker_team35.R;
import edu.ucsd.cse110.zooseeker_team35.adapters.SearchListAdapter;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;

public class SearchResultsActivity extends AppCompatActivity {
    private List<ZooData.VertexInfo> exhibits;
    private List<ZooData.VertexInfo> exhibitResults;
    private Button searchBtn2;
    private EditText searchBar;
    private TextView backButton;
    public RecyclerView recyclerView;
    public SearchListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        //initialize buttons/search bar
        this.searchBtn2 = this.findViewById(R.id.search_btn_2);
        searchBtn2.setOnClickListener(this::onSearchButton2Clicked);
        this.searchBar = this.findViewById(R.id.search_bar_2);
        this.backButton = this.findViewById(R.id.back_btn);
        backButton.setOnClickListener(this::onBackButtonClicked);

        //initialize recycler/adapter
        this.adapter = new SearchListAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.search_item_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //Initializes ArrayList which will hold the searched exhibits that are to be displayed
        this.exhibitResults = new ArrayList<>();

        //Grabs the search term, and normalizes it to all lower case
        Bundle extra = getIntent().getExtras();
        String searchTerm = extra.getString("searchTerm").toLowerCase();


        this.exhibits = ZooInfoProvider.getExhibits();

        displaySearchResult(searchTerm);
    }

    void onSearchButton2Clicked(View view){

        //sets no result message to invisible in case the search result pulls something
        TextView noResultsFound = this.findViewById(R.id.no_results_msg);
        noResultsFound.setVisibility(View.INVISIBLE);

        //Gets the new search results
        String searchTerm = searchBar.getText().toString();

        /*
        Uses some code from
        https://stackoverflow.com/questions/1397361/how-to-restart-activity-in-android
        Concerning how to "restart" an activity without screen flickering
         */
        getIntent().putExtra("searchTerm", searchTerm);
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    void onBackButtonClicked(View view){
        finish();
    }

    void displaySearchResult(String searchTerm) {
        //remove other search results first
        exhibitResults.clear();

        System.out.println("Exhibits: ");
        for (ZooData.VertexInfo search : exhibits){
            System.out.println(search.name);
        }

        //Adds all exhibits which contain the search term in either their name or tags
        for(ZooData.VertexInfo exhibit : exhibits) {
            if (exhibit.name.toLowerCase().contains(searchTerm)) {
                exhibitResults.add(exhibit);
            }
            else
                for(String tag : exhibit.tags) {
                    if(tag.toLowerCase().contains(searchTerm)) {
                        exhibitResults.add(exhibit);
                        break;
                    }
                }
        }

        System.out.println("Search Results: ");
        for (ZooData.VertexInfo search : exhibitResults){
            System.out.println(search.name);
        }

        if(exhibitResults.isEmpty()) {
            searchFail();
        }
        else {
            adapter.setSearchItems(exhibitResults);
            searchBar.setText(searchTerm);
        }
    }

    //RecyclerView is updated with an empty list and show no results message
    void searchFail(){
        exhibitResults.clear();
        adapter.notifyDataSetChanged();
        TextView no_result = (TextView)findViewById(R.id.no_results_msg);
        no_result.setVisibility(View.VISIBLE);
    }
}