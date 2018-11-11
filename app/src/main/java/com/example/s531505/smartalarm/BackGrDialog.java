package com.example.s531505.smartalarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BackGrDialog extends DialogFragment {

    ColorCallBack color;
    public interface ColorCallBack {
        void setBackgroundImage(int imageChoice);

    }
    public void onAttach(Activity act){
        super.onAttach(act);
        color = (ColorCallBack) act;
    }
    String[] images = {
            "fancy yellow","paper", "electric spectrum", "pink flowers"};
    @Override
    public Dialog onCreateDialog(Bundle sis){
        super.onCreateDialog(sis);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // Factory for an AlertDialog.

        builder.setTitle("Pick a background image")
                .setItems(images, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
// The 'which' argument contains the index position
// of the selected item
                        color.setBackgroundImage(which);
                    }
                });
        return builder.create();}
}
