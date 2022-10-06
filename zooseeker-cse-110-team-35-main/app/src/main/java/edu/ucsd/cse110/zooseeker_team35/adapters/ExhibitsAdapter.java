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
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;

public class ExhibitsAdapter extends RecyclerView.Adapter<ExhibitsAdapter.ViewHolder> {
    private List<ZooData.VertexInfo> exhibits = Collections.emptyList();

    /*public ExhibitsAdapter(List<ZooData.VertexInfo> exhibits) {
        this.exhibits = exhibits;
    }*/

    public void setExhibits(List<ZooData.VertexInfo> addedExhibits) {
        this.exhibits.clear();
        this.exhibits = addedExhibits;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.display_exhibit_items, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setExhibits(exhibits.get(position));
    }

    @Override
    public int getItemCount() {
        return exhibits.size();
    }

    public String getId(int position) { return exhibits.get(position).id; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        //ExhibitStatusDao dao;
        //private final Button xBox;
        private ZooData.VertexInfo vertex;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //this.xBox =itemView.findViewById(R.id.x_box);
            this.textView = itemView.findViewById(R.id.exhibit_item_text);
        }

        public ZooData.VertexInfo getVertex() {
            return vertex;
        }

        public void setExhibits(ZooData.VertexInfo vertex) {
            this.vertex = vertex;
            this.textView.setText(vertex.name);
        }
    }
}

