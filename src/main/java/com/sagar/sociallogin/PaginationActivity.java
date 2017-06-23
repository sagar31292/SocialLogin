package com.sagar.sociallogin;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by sm185435 on 6/14/2017.
 */

public class PaginationActivity extends AppCompatActivity {


    private List<Products> productsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    int i = 1;
    int cnt = 0;
    boolean check = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pagination);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_product);

        mAdapter = new ProductAdapter(productsList);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        final int min = 100;
        final int max = 1000;

        final int min1 = 1;
        final int max1 = 10;

        final Random r = new Random();


        productsList.add(new Products("Product" + i++, String.valueOf(r.nextInt(max - min + 1) + min) + "$", String.valueOf(r.nextInt(max1 - min1 + 1) + min1) + "% Off"));
        productsList.add(new Products("Product" + i++, String.valueOf(r.nextInt(max - min + 1) + min) + "$", String.valueOf(r.nextInt(max1 - min1 + 1) + min1) + "% Off"));
        productsList.add(new Products("Product" + i++, String.valueOf(r.nextInt(max - min + 1) + min) + "$", String.valueOf(r.nextInt(max1 - min1 + 1) + min1) + "% Off"));
        productsList.add(new Products("Product" + i++, String.valueOf(r.nextInt(max - min + 1) + min) + "$", String.valueOf(r.nextInt(max1 - min1 + 1) + min1) + "% Off"));
        productsList.add(new Products("Product" + i++, String.valueOf(r.nextInt(max - min + 1) + min) + "$", String.valueOf(r.nextInt(max1 - min1 + 1) + min1) + "% Off"));
        productsList.add(new Products("Product" + i++, String.valueOf(r.nextInt(max - min + 1) + min) + "$", String.valueOf(r.nextInt(max1 - min1 + 1) + min1) + "% Off"));
        productsList.add(new Products("Product" + i++, String.valueOf(r.nextInt(max - min + 1) + min) + "$", String.valueOf(r.nextInt(max1 - min1 + 1) + min1) + "% Off"));
        productsList.add(new Products("Product" + i++, String.valueOf(r.nextInt(max - min + 1) + min) + "$", String.valueOf(r.nextInt(max1 - min1 + 1) + min1) + "% Off"));
        productsList.add(new Products("Product" + i++, String.valueOf(r.nextInt(max - min + 1) + min) + "$", String.valueOf(r.nextInt(max1 - min1 + 1) + min1) + "% Off"));
        productsList.add(new Products("Product" + i++, String.valueOf(r.nextInt(max - min + 1) + min) + "$", String.valueOf(r.nextInt(max1 - min1 + 1) + min1) + "% Off"));


        mAdapter.notifyDataSetChanged();


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();


                int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                //if (!isLoading() && !isLastPage()) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0 && check) {


                    check = false;

                    //Toast.makeText(getApplicationContext(), "List End" + cnt++, Toast.LENGTH_SHORT).show();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            productsList.add(new Products("Product" + i++, String.valueOf(r.nextInt(max - min + 1) + min) + "$", String.valueOf(r.nextInt(max1 - min1 + 1) + min1) + "% Off"));
                            productsList.add(new Products("Product" + i++, String.valueOf(r.nextInt(max - min + 1) + min) + "$", String.valueOf(r.nextInt(max1 - min1 + 1) + min1) + "% Off"));
                            productsList.add(new Products("Product" + i++, String.valueOf(r.nextInt(max - min + 1) + min) + "$", String.valueOf(r.nextInt(max1 - min1 + 1) + min1) + "% Off"));
                            productsList.add(new Products("Product" + i++, String.valueOf(r.nextInt(max - min + 1) + min) + "$", String.valueOf(r.nextInt(max1 - min1 + 1) + min1) + "% Off"));
                            productsList.add(new Products("Product" + i++, String.valueOf(r.nextInt(max - min + 1) + min) + "$", String.valueOf(r.nextInt(max1 - min1 + 1) + min1) + "% Off"));
                            productsList.add(new Products("Product" + i++, String.valueOf(r.nextInt(max - min + 1) + min) + "$", String.valueOf(r.nextInt(max1 - min1 + 1) + min1) + "% Off"));
                            productsList.add(new Products("Product" + i++, String.valueOf(r.nextInt(max - min + 1) + min) + "$", String.valueOf(r.nextInt(max1 - min1 + 1) + min1) + "% Off"));
                            productsList.add(new Products("Product" + i++, String.valueOf(r.nextInt(max - min + 1) + min) + "$", String.valueOf(r.nextInt(max1 - min1 + 1) + min1) + "% Off"));
                            productsList.add(new Products("Product" + i++, String.valueOf(r.nextInt(max - min + 1) + min) + "$", String.valueOf(r.nextInt(max1 - min1 + 1) + min1) + "% Off"));
                            productsList.add(new Products("Product" + i++, String.valueOf(r.nextInt(max - min + 1) + min) + "$", String.valueOf(r.nextInt(max1 - min1 + 1) + min1) + "% Off"));

                            mAdapter.notifyDataSetChanged();
                            check = true;

                        }
                    }, 2000);
                }
            }
        });


    }

    void loadMoreItems() {


    }
}