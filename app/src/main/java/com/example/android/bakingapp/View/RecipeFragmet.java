package com.example.android.bakingapp.View;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeFragmet extends Fragment{

    @BindView(R.id.ingredientsRecyclerView)
    RecyclerView ingredientsRecyclerView;
    @BindView(R.id.stepsRecyclerView)
    RecyclerView stepsRecyclerView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        ArrayList<Ingredient> ingredients = getActivity().getIntent().getExtras().getParcelableArrayList("ingredients");
        final ArrayList<Step> steps = getActivity().getIntent().getExtras().getParcelableArrayList("steps");
        ButterKnife.bind(this, view);
        RecyclerView.LayoutManager ingredientsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        ingredientsRecyclerView.setLayoutManager(ingredientsLayoutManager);
        IngredientAdapter ingredientAdapter = new IngredientAdapter(ingredients);
        ingredientsRecyclerView.setAdapter(ingredientAdapter);
        RecyclerView.LayoutManager StepsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        stepsRecyclerView.setLayoutManager(StepsLayoutManager);
        StepAdapter stepAdapter = new StepAdapter(steps, new StepAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Step step) {
                if(!(getResources().getBoolean(R.bool.twoPaneMode))) {
                    Fragment stepFragment = new StepFragment();
                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Step", step);
                    bundle.putParcelableArrayList("Steps", steps);
                    stepFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.frame, stepFragment, "tag");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else{
                    FrameLayout frameLayout = getActivity().findViewById(R.id.frame2);
                    frameLayout.setVisibility(View.VISIBLE);
                    Fragment stepFragment = new StepFragment();
                    FragmentManager fragmentManager = getActivity().getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Step", step);
                    bundle.putParcelableArrayList("Steps", steps);
                    stepFragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.frame2, stepFragment, "tag");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
        stepsRecyclerView.setAdapter(stepAdapter);
        return view;
    }

}
