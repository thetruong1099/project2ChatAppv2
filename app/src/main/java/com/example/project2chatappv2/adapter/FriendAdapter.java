package com.example.project2chatappv2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project2chatappv2.R;
import com.example.project2chatappv2.model.UserModel;
import com.example.project2chatappv2.view.activity.MessageActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private Context context;
    private List<UserModel> listUser;

    public FriendAdapter(Context context, List<UserModel> listUser) {
        this.context = context;
        this.listUser = listUser;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imageProfile;
        private TextView tvUsername;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            tvUsername = itemView.findViewById(R.id.tvUsername);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_item, parent, false);
        return new FriendAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UserModel userModel = listUser.get(position);
        holder.tvUsername.setText(userModel.getUsername());
        if(userModel.getImageURL().equals("default")){
            holder.imageProfile.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(userModel.getImageURL()).into(holder.imageProfile);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", userModel.getId());
                intent.putExtra("username", userModel.getUsername());
                intent.putExtra("image_profile", userModel.getImageURL());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }


}
