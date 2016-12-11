package org.cheeseandbacon.bitbeast;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class BitBeastDialogFragment extends DialogFragment {

    public interface DialogViewCallback {
        void onViewLoaded(int dialogLayout, String dialogMessage, View view);
    }

    private static final String ARG_DIALOG_TYPE = "dialogType";
    private static final String ARG_DIALOG_LAYOUT = "dialogLayout";
    private static final String ARG_DIALOG_MESSAGE = "dialogMessage";
    private static final String ARG_DIALOG_CALLBACK = "dialogCallback";

    public static final int DIALOG_TYPE_PROGRESS = 0;
    public static final int DIALOG_TYPE_ALERT = 1;

    int dialogType;
    int dialogLayout;
    String dialogMessage;
    DialogViewCallback callback;

    public BitBeastDialogFragment () {
        // Empty constructor required for dialog fragments
    }

    public static BitBeastDialogFragment newInstance (int dialogType, int dialogLayout, String dialogMessage, DialogViewCallback callback) {
        BitBeastDialogFragment dialogFragment = new BitBeastDialogFragment();

        /**Bundle arguments = new Bundle();
        arguments.putInt(ARG_DIALOG_TYPE, dialogType);
        arguments.putInt(ARG_DIALOG_LAYOUT, dialogLayout);
        arguments.putString(ARG_DIALOG_MESSAGE, dialogMessage);

        dialogFragment.setArguments(arguments);*/

        dialogFragment.dialogType = dialogType;
        dialogFragment.dialogLayout = dialogLayout;
        dialogFragment.dialogMessage = dialogMessage;
        dialogFragment.callback = callback;

        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        /**dialogType = getArguments().getInt(ARG_DIALOG_TYPE);
        dialogLayout = getArguments().getInt(ARG_DIALOG_LAYOUT);
        dialogMessage = getArguments().getString(ARG_DIALOG_MESSAGE);*/

        if (dialogType == DIALOG_TYPE_PROGRESS) {
            ProgressDialog progressDialog = new ProgressDialog(getActivity());

            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(dialogMessage);
            progressDialog.setCancelable(false);

            return progressDialog;
        } else if (dialogType == DIALOG_TYPE_ALERT) {
            View view = LayoutInflater.from(getActivity()).inflate(dialogLayout, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(view).setCancelable(true);

            callback.onViewLoaded(dialogLayout, dialogMessage, view);

            return builder.create();
        } else {
            return new Dialog(getActivity());
        }
    }
}
