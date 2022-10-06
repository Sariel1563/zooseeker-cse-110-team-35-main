package edu.ucsd.cse110.zooseeker_team35.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import edu.ucsd.cse110.zooseeker_team35.R;

public class DirectionsAdapter extends RecyclerView.Adapter<DirectionsAdapter.ViewHolder> {
    private List<String> directions = Collections.emptyList();

    public void setExhibits(List<String> directions) {
        this.directions.clear();
        this.directions = directions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DirectionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.single_direction_item, parent, false);

        return new DirectionsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setExhibits(directions.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return directions.size();
    }

    public String getId(int position) { return directions.get(position); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private String singleDirection;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.single_direction_txt);
        }

        public String getSingleDirection() {
            return singleDirection;
        }

        public void setExhibits(String direction) {
            this.singleDirection = direction;
            this.textView.setText(singleDirection);
        }
    }

}
