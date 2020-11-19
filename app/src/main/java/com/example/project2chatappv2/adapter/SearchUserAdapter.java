package com.example.project2chatappv2.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project2chatappv2.R;
import com.example.project2chatappv2.model.UserModel;
import com.example.project2chatappv2.view.activity.MessageActivity;
import com.example.project2chatappv2.view.activity.UserGuestActivity;
import com.example.project2chatappv2.viewModel.AuthencationViewModel;
import com.example.project2chatappv2.viewModel.FriendsViewModel;
import com.example.project2chatappv2.viewModel.UserViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> {

    private Context context;
    private List<UserModel> listUsers;
    private Boolean isChat;

    public SearchUserAdapter(Context context, List<UserModel> listUsers, Boolean isChat) {
        this.context = context;
        this.listUsers = listUsers;
        this.isChat = isChat;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView image_profile;
        private TextView tvUsername;
        private ImageView btnAddFriends;
        private ImageView img_on;
        private ImageView img_off;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            btnAddFriends = itemView.findViewById(R.id.btnAddFriends);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.searh_user_item, parent, false);
        return new SearchUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UserModel user= listUsers.get(position);
        holder.tvUsername.setText(user.getUsername());
        if(user.getImageURL().equals("default")){
            holder.image_profile.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(user.getImageURL()).into(holder.image_profile);
        }
        if (isChat){
            if (user.getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            }else{
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        }else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }

        holder.btnAddFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserGuestActivity.class);
                intent.putExtra("userid", user.getId());
                intent.putExtra("username", user.getUsername());
                intent.putExtra("image_profile", user.getImageURL());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                intent.putExtra("username", user.getUsername());
                intent.putExtra("image_profile", user.getImageURL());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }


}
