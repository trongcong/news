package com.dev4u.ntc.generalnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dev4u.ntc.generalnews.R;
import com.dev4u.ntc.generalnews.model.Category;
import com.dev4u.ntc.generalnews.utils.ConnectionUtils;
import com.dev4u.ntc.generalnews.view.NewsByCategoryActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * IDE: Android Studio
 * Created by Nguyen Trong Cong  - 2DEV4U.COM
 * Name packge: com.dev4u.ntc.generalnews.adapter
 * Name project: GeneralNews
 * Date: 2/8/2017
 * Time: 23:52
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    boolean isInternetAvailable = false;
    LayoutInflater inflate;
    private ArrayList<Category> marrCategories;
    private Context context;

    public CategoryAdapter(Context context, ArrayList<Category> marrCategories) {
        this.context = context;
        this.marrCategories = marrCategories;
        inflate = LayoutInflater.from(context);
        // get Internet status
        isInternetAvailable = ConnectionUtils.isConnected(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflate.inflate(R.layout.custom_category_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvNameCategory.setText(marrCategories.get(position).getNameCategory());
        //check wifi/3g
        //if false( có )
        if (isInternetAvailable) {
            Glide.with(context).load("http://img.f9.giaitri.vnecdn.net/2017/01/03/01-HOANG-THUY-LINH-CHI-PU-3-1483422238_660x0.jpg")
                    .placeholder(R.drawable.bg_no_img)
                    .error(R.drawable.bg_no_img)
                    .override(300, 230)
                    .crossFade(3000)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.imgCategory);
        } else {
            //lấy ImageView ra để thiết lập hình ảnh cho đúng
            holder.imgCategory.setImageResource(R.drawable.bg_no_img);
        }
    }

    @Override
    public int getItemCount() {
        return marrCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgCategory)
        ImageView imgCategory;
        @BindView(R.id.tvNameCategory)
        TextView tvNameCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(context, NewsByCategoryActivity.class);
                    intent.putExtra("ca_list", marrCategories.get(position));
                    intent.putExtra("idCa", marrCategories.get(position).getId_ca() + "");
                    intent.putExtra("nameCa", marrCategories.get(position).getNameCategory());
                    context.startActivity(intent);
                }
            });
        }
    }
}
