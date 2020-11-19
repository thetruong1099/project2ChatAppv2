package com.example.project2chatappv2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project2chatappv2.R;
import com.example.project2chatappv2.model.UserModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChoseMemberAdapter extends RecyclerView.Adapter<ChoseMemberAdapter.ViewHolder> {

    private Context context;
    private List<UserModel> listUser;

    public ChoseMemberAdapter(Context context, List<UserModel> listUser) {
        this.context = context;
        this.listUser = listUser;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imageProfile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.member_chose_item, parent, false);
        return new ChoseMemberAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String imageURL = listUser.get(position).getImageURL();
        if(imageURL .equals("default")){
            holder.imageProfile.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(imageURL ).into(holder.imageProfile);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listUser.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listUser!=null){
            return listUser.size();
        }else return 0;

    }


}
