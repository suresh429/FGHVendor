package com.ambitious.fghvendor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ambitious.fghvendor.Activities.LabListActivity;
import com.ambitious.fghvendor.Model.Lab;
import com.ambitious.fghvendor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

public class LabListAdapter extends RecyclerView.Adapter<LabListAdapter.MyViewHolder> {

    public Context context;
    private ArrayList<Lab> labArrayList;

    public LabListAdapter(Context context, ArrayList<Lab> labArrayList) {
        this.context = context;
        this.labArrayList = labArrayList;
        Collections.reverse(this.labArrayList);
    }

    @NonNull
    @Override
    public LabListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicalshop, parent, false);

        return new LabListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabListAdapter.MyViewHolder holder, int position) {

        Lab lab = labArrayList.get(position);

        if (lab.getImage().contains("png") || lab.getImage().contains("jpg")) {
            Glide.with(context).load(lab.getImage()).into(holder.iv_Image);
        } else {
            holder.iv_Image.setImageResource(R.drawable.profile_new);
        }

        holder.tv_Name.setText(lab.getName());
        holder.tv_Address.setText(lab.getAddress());
        holder.tv_Rate.setText(lab.getRating());
        holder.bar_Rating.setRating(Float.parseFloat(lab.getRating()));

        if (lab.getAvailable().equalsIgnoreCase("1")) {
            holder.tv_Available.setVisibility(View.VISIBLE);
            holder.tv_Notavailable.setVisibility(View.GONE);
        } else {
            holder.tv_Available.setVisibility(View.GONE);
            holder.tv_Notavailable.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return labArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_Image;
        private TextView tv_Name, tv_Address, tv_Rate, tv_Available, tv_Notavailable;
        private RatingBar bar_Rating;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_Image = itemView.findViewById(R.id.iv_Image);
            tv_Name = itemView.findViewById(R.id.tv_Name);
            tv_Address = itemView.findViewById(R.id.tv_Address);
            tv_Rate = itemView.findViewById(R.id.tv_Rate);
            bar_Rating = itemView.findViewById(R.id.bar_Rating);
            tv_Available = itemView.findViewById(R.id.tv_Available);
            tv_Notavailable = itemView.findViewById(R.id.tv_Notavailable);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*context.startActivity(new Intent(context, MedicalShopProfileActivity.class)
                            .putExtra("wallet", ((LabListActivity) context).wallet)
                            .putExtra("donated", ((LabListActivity) context).donated)
                            .putExtra("obj", "" + labArrayList.get(getAdapterPosition()).getObj()));
                    Animatoo.animateCard(context);*/
                }
            });
        }
    }

    public void updateList(ArrayList<Lab> list) {
        labArrayList = list;
        notifyDataSetChanged();
    }
}
