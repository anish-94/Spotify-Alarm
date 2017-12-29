package com.example.anish.spotifyalarm;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;

/**
 *  Adapter file to gather Spotify data using the API Wrapper
 *  Get the list of songs given a query and store it in a list
 *
 * Created by anish on 12/21/17.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.SearchResultViewHolder> {

    private ArrayList<Track> mSearchResultList;
    private OnSearchResultClickListener mSearchResultClickListener;

    public ResultAdapter(OnSearchResultClickListener clickListener){
        mSearchResultClickListener = clickListener;
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_result_item, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {
        holder.bind(mSearchResultList.get(position));
        holder.mSearchResultTV.setTextColor(Color.parseColor("#ffffff"));
    }

    public void updateSearchResults(ArrayList<Track> searchResultsList){
        mSearchResultList = searchResultsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mSearchResultList != null) {
            return mSearchResultList.size();
        }
        else {
            return 0;
        }

    }

    public interface OnSearchResultClickListener {
        void onSearchResultClick(Track searchResult);
    }

    class SearchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mSearchResultTV;

        public SearchResultViewHolder(View itemView) {
            super(itemView);
            mSearchResultTV = (TextView)itemView.findViewById(R.id.tv_search_result);
            itemView.setOnClickListener(this);
        }

        public void bind(Track track) {
            mSearchResultTV.setText(track.name + " - " + track.album);                // Store the track + album names in the specified list
        }

        public void onClick(View v){
            Track searchResult = mSearchResultList.get(getAdapterPosition());
            mSearchResultClickListener.onSearchResultClick(searchResult);
        }

    }
}
