package com.example.project2chatappv2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project2chatappv2.R;
import com.example.project2chatappv2.model.UserModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private Context context;
    private List<UserModel> listUser;

    public MemberAdapter(Context context, List<UserModel> listUser) {
        this.context = context;
        this.listUser = listUser;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView image_profile;
        private TextView tvUsername;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_profile = itemView.findViewById(R.id.image_profile);
            tvUsername = itemView.findViewById(R.id.tvUsername);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.member_item, parent, false);
        return new MemberAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UserModel user= listUser.get(position);
        holder.tvUsername.setText(user.getUsername());
        if(user.getImageURL().equals("default")){
            holder.image_profile.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(user.getImageURL()).into(holder.image_profile);
        }
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }


}
