package com.chuck.android.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chuck.android.bakingapp.R;
import com.chuck.android.bakingapp.RecipeListStepActivity;
import com.chuck.android.bakingapp.RecipeStepDetailActivity;
import com.chuck.android.bakingapp.RecipeStepDetailFragment;
import com.chuck.android.bakingapp.models.Step;

import java.util.List;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {

        private final RecipeListStepActivity mParentActivity;
        private final List<Step> mValues;
        private final boolean mTwoPane;


        public RecipeStepsAdapter(RecipeListStepActivity parent,
                                      List<Step> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(Integer.toString(mValues.get(position).getId()));
            holder.mContentView.setText(mValues.get(position).getShortDescription());
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView =  view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
                view.setOnClickListener(this);

            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();

                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(RecipeStepDetailFragment.ARG_ITEM_ID, Integer.toString(mValues.get(position).getId()));
                    arguments.putString(RecipeStepDetailFragment.ARG_ITEM_DESCRIPTION, mValues.get(position).getShortDescription());
                    arguments.putString(RecipeStepDetailFragment.ARG_ITEM_LONG_DESCRIPTION, mValues.get(position).getDescription());
                    arguments.putString(RecipeStepDetailFragment.ARG_ITEM_VIDEO_URL, mValues.get(position).getVideoURL());
                    RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RecipeStepDetailActivity.class);
                    intent.putExtra(RecipeStepDetailFragment.ARG_ITEM_ID, Integer.toString(mValues.get(position).getId()));
                    intent.putExtra(RecipeStepDetailFragment.ARG_ITEM_DESCRIPTION, mValues.get(position).getShortDescription());
                    intent.putExtra(RecipeStepDetailFragment.ARG_ITEM_LONG_DESCRIPTION, mValues.get(position).getDescription());
                    intent.putExtra(RecipeStepDetailFragment.ARG_ITEM_VIDEO_URL, mValues.get(position).getVideoURL());


                    context.startActivity(intent);
                }

            }
        }
    }

