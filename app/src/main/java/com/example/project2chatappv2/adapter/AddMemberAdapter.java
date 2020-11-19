package com.example.project2chatappv2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project2chatappv2.R;
import com.example.project2chatappv2.model.UserModel;
import com.example.project2chatappv2.viewModel.MemberGroupChatViewModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddMemberAdapter extends RecyclerView.Adapter<AddMemberAdapter.ViewHolder> {

    private Context context;
    private List<UserModel> listUsers;

    private List<UserModel> userModelList;

    public AddMemberAdapter() {
    }

    public AddMemberAdapter(Context context, List<UserModel> listUsers) {
        this.context = context;
        this.listUsers = listUsers;
        userModelList = new ArrayList<>();
    }


    public List<UserModel> getUserModelList() {
        return userModelList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView image_profile;
        private TextView tvUsername;
        private CheckBox checkBoxButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            checkBoxButton = itemView.findViewById(R.id.checkBoxButton);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_member_item, parent, false);
        return new AddMemberAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final UserModel user= listUsers.get(position);
        holder.tvUsername.setText(user.getUsername());
        if(user.getImageURL().equals("default")){
            holder.image_profile.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(user.getImageURL()).into(holder.image_profile);
        }


        holder.checkBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBoxButton.isChecked() == true){
                    userModelList.add(user);
                }
                else userModelList.remove(user);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }
}
