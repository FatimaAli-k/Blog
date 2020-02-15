package com.example.blog.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blog.R;
import com.example.blog.model.Categories;

import java.util.ArrayList;


public class CategoriesFragment extends Fragment implements CategoriesRecyclerViewAdapter.ItemClickListener {

//    private CategoriesViewModel categoriesViewModel;

   CategoriesRecyclerViewAdapter adapter;
    RelativeLayout catRelativeLayout;
    ArrayList<Categories>catList=new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container,false);

//        categoriesViewModel =
//                ViewModelProviders.of(this).get(CategoriesViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_categories, container, false);
//        final TextView textView = root.findViewById(R.id.text_tools);
//        categoriesViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        Categories cat=new Categories(2,"test");

        for(int i=0;i<10;i++){
            cat=new Categories(i,"test"+i);
            catList.add(cat);
        }



        RecyclerView recyclerView = view.findViewById(R.id.categories_recycler_view);
        catRelativeLayout = view.findViewById(R.id.catRelativeLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter= new CategoriesRecyclerViewAdapter( getContext(),catList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


        return view;
    }
    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "pos: " + position+" id: "+ catList.get(position).getId(), Toast.LENGTH_SHORT).show();


        Bundle bundle = new Bundle();
        bundle.putInt("catId",catList.get(position).getId());
        Navigation.findNavController(view).navigate(R.id.action_nav_categories_to_nav_home,bundle);
//

    }
}