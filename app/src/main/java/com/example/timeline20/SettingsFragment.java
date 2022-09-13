package com.example.timeline20;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;


public class SettingsFragment extends Fragment {

    Button language_button, support_button, positive_button, negative_button;

    public NotificationManager notificationManager;
    private  static final int NOTIFY_ID = 1;
    private static final String CHANNEL_ID = "CHANNEL_ID";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.fragment_settings, container, false);

        support_button = (Button) inflatedView.findViewById(R.id.support_us_button);
        language_button = (Button) inflatedView.findViewById(R.id.language_button);

        support_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://youtu.be/CAZ8kTQ49c8"));
                SettingsFragment.this.startActivity(intent);
            }
        });

        language_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language_dialog();
            }
        });

        return inflatedView;
    }

    @Override
    public void onStart() {
        super.onStart();

        MainActivity mainActivity = (MainActivity) getActivity();
        Objects.requireNonNull(mainActivity).change_bar_icon_color(3, true);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void language_dialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setBackground(getResources().getDrawable(R.drawable.dialog_shape));
        ConstraintLayout cl = (ConstraintLayout) getLayoutInflater().inflate(R.layout.dialog_language, null);

        builder.setCancelable(false);
        builder.setView(cl);
        AlertDialog dialog = builder.show();

        positive_button = (Button) cl.findViewById(R.id.positive_button);
        negative_button = (Button) cl.findViewById(R.id.netagive_button);

        positive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Вы согласились продать душу", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        negative_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                notification();
            }
        });
    }

    private void notification() {
        notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(getContext(), SettingsFragment.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_pencil)
                        .setAutoCancel(true)
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(pendingIntent)
                        .setContentTitle("Z + V")
                        .setContentText("Удаляйте приложение, оно для русских!")
                        .setPriority(NotificationCompat.PRIORITY_MAX);
        createIfN(notificationManager);
        notificationManager.notify(NOTIFY_ID, notificationBuilder.build());
    }

    public static void createIfN(NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        MainActivity mainActivity = (MainActivity) getActivity();
        Objects.requireNonNull(mainActivity).change_bar_icon_color(3, false);
    }

}