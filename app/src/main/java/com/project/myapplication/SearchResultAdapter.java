package com.project.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {
    private List<SearchResult> searchResults;

    public SearchResultAdapter(List<SearchResult> searchResults) {
        this.searchResults = searchResults;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        SearchResult result = searchResults.get(position);
        holder.resultName.setText(result.getName());
        holder.contactProfileImage.setImageResource(result.getProfileImageResource());
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public class SearchResultViewHolder extends RecyclerView.ViewHolder {
        ImageView contactProfileImage;
        TextView resultName;

        public SearchResultViewHolder(View itemView) {
            super(itemView);
            contactProfileImage = itemView.findViewById(R.id.contactProfileImage);
            resultName = itemView.findViewById(R.id.resultName);
        }
    }
}


