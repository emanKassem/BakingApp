package com.example.android.bakingapp.View;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder>{

    ArrayList<Step> steps;
    public interface OnItemClickListener {

        void onItemClick(Step step);
    }
    private final StepAdapter.OnItemClickListener listener;

    public StepAdapter(ArrayList<Step> steps, OnItemClickListener listener){
        this.steps = steps;
        this.listener = listener;
    }
    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {

        holder.stepDescripion.setText(steps.get(position).getShortDescription());
        holder.bind(steps.get(position), listener);

    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.step_short_description)
        TextView stepDescripion;
        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Step step, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(step);
                }
            });
        }
    }

}
