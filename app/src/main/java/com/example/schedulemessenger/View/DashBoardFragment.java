package com.example.schedulemessenger.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.schedulemessenger.Model.MenuModel;
import com.example.schedulemessenger.R;
import com.example.schedulemessenger.View.Adapter.DashBoardAdapter;
import com.example.schedulemessenger.databinding.FragmentDashBoardBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class DashBoardFragment extends Fragment {

    private FragmentDashBoardBinding fragmentDashBoardBinding;

    public DashBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment, using ViewBinding
        fragmentDashBoardBinding = FragmentDashBoardBinding.inflate(inflater, container, false);
        return fragmentDashBoardBinding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentDashBoardBinding.menuList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        fragmentDashBoardBinding.menuList.setAdapter(new DashBoardAdapter(getMenu(), getActivity()));
    }


    private ArrayList<MenuModel> getMenu() {
        return new ArrayList<MenuModel>(Arrays.asList(
                new MenuModel("SMS", R.drawable.ic_sms, R.color.SMSText, R.id.action_dashBoardFragment_to_smsScheduleFragment),
                new MenuModel("Email", R.drawable.ic_email, R.color.EmailText, R.id.action_dashBoardFragment_to_emailScheduleFragment),
                new MenuModel("WhatsApp", R.drawable.ic_whatsapp, R.color.WAText,R.id.action_dashBoardFragment_to_whatsappScheduleFragment),
                new MenuModel("Instagram", R.drawable.ic_instagram, R.color.IGText,R.id.action_dashBoardFragment_to_instagramScheduleFragment)
        ));
    }

    //To avoid memory leaks
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentDashBoardBinding = null;
    }

}