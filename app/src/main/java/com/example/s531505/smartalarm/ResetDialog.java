package com.example.s531505.smartalarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ResetDialog extends DialogFragment {

    public interface ResetDialogInterface {
        public void reset();
    }
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Do you want to reset?")
                .setMessage("You will lose your progress")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ResetDialogInterface conf = (ResetDialogInterface) getActivity();
                        conf.reset();
                        Toast.makeText(getActivity(), "Pressed on YES to reset",
                                Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "You clicked on NO button", Toast.LENGTH_SHORT).show(); }
                });
        return builder.create();


    }
}
