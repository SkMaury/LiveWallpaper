package android.skmaury.com.livewallpaper.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.skmaury.com.livewallpaper.Common.Common;
import android.skmaury.com.livewallpaper.Interface.ItemClickListener;
import android.skmaury.com.livewallpaper.ListWallpaper;
import android.skmaury.com.livewallpaper.Model.CategoryItem;
import android.skmaury.com.livewallpaper.ViewHolder.CategoryViewHolder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.skmaury.com.livewallpaper.R;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class CategoryFragment extends Fragment {

    /* Views */
    RecyclerView recyclerView;

    /* FireBase related variables*/
    FirebaseDatabase database;
    DatabaseReference categoryBackground;

    /* FireBase UI Adapter */
    FirebaseRecyclerOptions<CategoryItem> options;
    FirebaseRecyclerAdapter<CategoryItem, CategoryViewHolder> adapter;

    private  static CategoryFragment INSTANCE = null;

    public CategoryFragment() {
        database = FirebaseDatabase.getInstance();
        categoryBackground = database.getReference(Common.STR_CATEGORY_BACKGROUND);

        options = new FirebaseRecyclerOptions.Builder<CategoryItem>()
                .setQuery(categoryBackground, CategoryItem.class) /* This query will select all in CategoryBackground */
                .build();

                adapter = new FirebaseRecyclerAdapter<CategoryItem, CategoryViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final CategoryViewHolder holder, int position, @NonNull final CategoryItem model) {
                        Picasso.with(getActivity())
                                .load(model.getImageLink())
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .into(holder.background_image, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Picasso.with(getActivity())
                                                .load(model.getImageLink())
                                                .error(R.drawable.ic_terrain_black_24dp)
                                                .into(holder.background_image, new Callback() {
                                                    @Override
                                                    public void onSuccess() {

                                                    }

                                                    @Override
                                                    public void onError() {
                                                        Log.e("Error SK", "Couldn't fetch image");
                                                    }
                                                });
                                    }
                                });

                        holder.category_name.setText(model.getName());

                        holder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                Common.CATEGORY_ID_SELECTED = adapter.getRef(position).getKey();
                                Log.v("Category Selected : ", Common.CATEGORY_ID_SELECTED);
                                Common.CATEGORY_SELECTED = model.getName();
                                Log.v("Category Model : ", Common.CATEGORY_ID_SELECTED);
                                startActivity(new Intent(getActivity(), ListWallpaper.class));
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View itemView = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.layout_category_item, parent, false);
                        return new CategoryViewHolder(itemView);
                    }
                };
    }

    public static CategoryFragment getInstance() {
        if(INSTANCE == null)
            INSTANCE = new CategoryFragment();

        return INSTANCE;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = view.findViewById(R.id.recycler_category);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        setCategory();

        return view;
    }

    private void setCategory() {
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter != null){
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        if(adapter != null)
            adapter.stopListening();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.startListening();
    }
}
