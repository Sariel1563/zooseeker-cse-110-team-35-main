package edu.ucsd.cse110.zooseeker_team35.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exhibitStatuses")
public class ExhibitStatus {

    @PrimaryKey
    @NonNull String id;

    boolean isAdded, isVisited;

    public ExhibitStatus(@NonNull String id, boolean isAdded, boolean isVisited) {
        this.id = id;
        this.isAdded = isAdded;
        this.isVisited = isVisited;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public boolean getIsAdded() {
        return isAdded;
    }

    public boolean getIsVisited() { return isVisited; }

    public void setIsAdded(boolean isAdded) {
        this.isAdded = isAdded;
    }

    public void setIsVisited(boolean isVisited) { this.isVisited = isVisited; }
}
