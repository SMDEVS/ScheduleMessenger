package com.example.schedulemessenger;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.schedulemessenger.databinding.FragmentDashBoardBinding;

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
        View rootView = fragmentDashBoardBinding.getRoot();
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
        //Whenever user clicks SMS from the dashboard (i.e., home page of app)
        fragmentDashBoardBinding.smsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //To go to SmsScheduleFragment from DashBoardFragment
                Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_smsScheduleFragment);
                
            }
        });

        //Whenever user clicks email from the dashboard
        fragmentDashBoardBinding.emailLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //To go to EmailScheduleFragment from DashBoardFragment
                Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_emailScheduleFragment);

            }
        });

        //Whenever user clicks WhatsApp from the dashboard
        fragmentDashBoardBinding.whatsappLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //To go to WhatsappScheduleFragment from DashBoardFragment
                Navigation.findNavController(v).navigate(R.id.action_dashBoardFragment_to_whatsappScheduleFragment);

            }
        });
         **/ 

    }

    //To avoid memory leaks
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentDashBoardBinding = null;
    }

}