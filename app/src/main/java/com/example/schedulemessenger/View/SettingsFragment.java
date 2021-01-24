package com.example.schedulemessenger.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.schedulemessenger.R;
import com.example.schedulemessenger.databinding.FragmentSettingsBinding;
import com.example.schedulemessenger.databinding.FragmentSmsScheduleBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding settingsBinding;
    public static String gmailId = "";
    public static String password = "";

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment, using ViewBinding
        settingsBinding = FragmentSettingsBinding.inflate(inflater, container, false);
        return settingsBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settingsBinding.setDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailId = settingsBinding.emailIdEditText.getText().toString().trim();
                String password = settingsBinding.passwordEditText.getText().toString().trim();

                if(emailId.isEmpty()) {
                    Toast.makeText(getContext(), "Enter a valid Gmail id",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if(!emailId.endsWith("gmail.com")) {
                    Toast.makeText(getContext(), "Please enter only a Gmail id",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if(password.isEmpty()) {
                    Toast.makeText(getContext(), "Password is empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Context context = getActivity();
                SharedPreferences sharedPref = context.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("EMAIL_ID", emailId);
                editor.putString("PASSWORD", password);
                editor.apply();

                Toast.makeText(context, "Gmail id set", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
