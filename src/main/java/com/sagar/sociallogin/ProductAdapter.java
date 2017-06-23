package com.sagar.sociallogin;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

/**
 * Created by sm185435 on 6/14/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Products> productsList;

    public ProductAdapter(List<Products> productList) {
        this.productsList = productList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, price, discount;
        private ImageView productImg;

        private MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);
            discount = (TextView) view.findViewById(R.id.discount);

            productImg = (ImageView) view.findViewById(R.id.imageView);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 1) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_product, parent, false);
            return new MyViewHolder(itemView);
        } else if (viewType == 2) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_loading, parent, false);
            return new FooterViewHolder(itemView);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {

          /*  if (position == productsList.size()) {
                position = position - 1;
            }*/
            Products products = productsList.get(position);
            ((MyViewHolder) holder).name.setText(products.getName());
            ((MyViewHolder) holder).price.setText(products.getPrice());
            ((MyViewHolder) holder).discount.setText(products.getDiscount());

            int[] photos={R.drawable.img1, R.drawable.img2,R.drawable.img3,R.drawable.img4,R.drawable.img5,R.drawable.img6,
                    R.drawable.img7};


            Random ran=new Random();
            int i=ran.nextInt(photos.length);
            ((MyViewHolder) holder).productImg.setImageResource(photos[i]);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position)) {
            return 2;
        }
        return 1;
    }

    private boolean isPositionFooter(int position) {
        return position == productsList.size();
    }

    @Override
    public int getItemCount() {
        return productsList.size() + 1;
    }
}