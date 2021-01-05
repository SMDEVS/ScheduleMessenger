package com.example.schedulemessenger.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedulemessenger.Model.MenuModel;
import com.example.schedulemessenger.databinding.MenuCardBinding;

import java.util.ArrayList;

public class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.viewHolder> {

    ArrayList<MenuModel> menuModelList = new ArrayList<>();
    Context mContext;

    public DashBoardAdapter(ArrayList<MenuModel> list, Context context) {
        menuModelList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MenuCardBinding binding = MenuCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {
        holder.binding.image.setImageResource(menuModelList.get(position).getIcon());;
        holder.binding.text.setText(menuModelList.get(position).getName());
        holder.binding.text.setTextColor(mContext.getResources().getColor(menuModelList.get(position).getTextColor()));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(menuModelList.get(position).getNavId());
            }
        });
    }


    @Override
    public int getItemCount() {
        return menuModelList.size();
    }


    class viewHolder extends RecyclerView.ViewHolder {
        MenuCardBinding binding;

        public viewHolder(MenuCardBinding cardBinding) {
            super(cardBinding.getRoot());
            binding = cardBinding;

        }
    }

}

