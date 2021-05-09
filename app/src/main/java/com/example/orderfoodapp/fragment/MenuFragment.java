package com.example.orderfoodapp.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orderfoodapp.MenuActivity;
import com.example.orderfoodapp.R;
import com.example.orderfoodapp.interfacepackage.ItemClickListener;
import com.example.orderfoodapp.model.Category;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.time.Instant;

public class MenuFragment extends Fragment {
    RecyclerView recyclerViewMenu;
    RecyclerView.LayoutManager layoutManager;

    public MenuFragment() {
        // Required empty public constructor
    }
    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        recyclerViewMenu = view.findViewById(R.id.recycleview_menu);
        recyclerViewMenu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewMenu.setLayoutManager(layoutManager);
        loadMenu();
        return view;
    }

    private void loadMenu() {
        Query query = FirebaseDatabase.getInstance().getReference().child("Category").
                limitToLast(15);
        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(query, Category.class)
                        .build();
        FirebaseRecyclerAdapter<Category, MenuActivity.ViewHolder> adapter =
                new FirebaseRecyclerAdapter<Category, MenuActivity.ViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MenuActivity.ViewHolder menuViewHolder,
                                                    int position, @NonNull Category category) {
                        Picasso.with(getActivity().getBaseContext()).load(category.getImage())
                                .into(menuViewHolder.imgFood);
                        Category itemClicked = category;
                        menuViewHolder.tvFoodName.setText(category.getName());
                        menuViewHolder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {
                                Toast.makeText(getActivity().getBaseContext(), itemClicked.getName(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public MenuActivity.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                      int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.menu_item, parent, false);

                        return new MenuActivity.ViewHolder(view);
                    }
                };
        adapter.startListening();
        recyclerViewMenu.setAdapter(adapter);
    }


}