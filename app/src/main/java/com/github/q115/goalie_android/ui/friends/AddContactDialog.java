package com.github.q115.goalie_android.ui.friends;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.q115.goalie_android.Constants;
import com.github.q115.goalie_android.R;
import com.github.q115.goalie_android.https.RESTGetUserInfo;
import com.github.q115.goalie_android.utils.UserHelper;

/**
 * Created by Qi on 11/16/2016.
 */

public class AddContactDialog extends DialogFragment {
    private String mUsername;

    /// <summary>
    /// default constructor. Needed so rotation doesn't crash
    /// </summary>
    public AddContactDialog() {
        super();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        if (savedInstanceState != null) {
            mUsername = savedInstanceState.getString("username", mUsername);
        } else
            mUsername = "";

        builder.setView(inflater.inflate(R.layout.dialog_add_contact, null))
                .setTitle(R.string.add_title)
                .setPositiveButton(R.string.add, null)
                .setNegativeButton(R.string.cancel, null);

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mUsername = ((EditText) getDialog().findViewById(R.id.add_username)).getText().toString().trim();

        outState.putString("username", mUsername);
        super.onSaveInstanceState(outState);
    }

    /// <summary>
    /// Set click events and set max length of editview.
    /// </summary>
    @Override
    public void onStart() {
        super.onStart();

        EditText addUsernameInput = getDialog().findViewById(R.id.add_username);
        addUsernameInput.setOnEditorActionListener(handleEditorAction());
        addUsernameInput.setText(mUsername);

        (getDialog().findViewById(R.id.add_friend_status)).setVisibility(View.INVISIBLE);

        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    add();
                }
            });
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDialog().dismiss();
                }
            });
        }
    }

    private EditText.OnEditorActionListener handleEditorAction() {
        return new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    add();
                    return true;
                }
                return false;
            }
        };
    }

    public void add() {
        final String username = ((EditText) getDialog().findViewById(R.id.add_username)).getText().toString().trim();

        //check if username is valid before pinging server
        if (!UserHelper.isUsernameValid(username)) {
            TextView updatestatus = getDialog().findViewById(R.id.add_friend_status);
            updatestatus.setVisibility(View.VISIBLE);
            updatestatus.setText(getString(R.string.username_error));
        } else if (UserHelper.getInstance().getAllContacts().containsKey(username)) {
            TextView updatestatus = getDialog().findViewById(R.id.add_friend_status);
            updatestatus.setVisibility(View.VISIBLE);
            updatestatus.setText(getString(R.string.already_friends));
        } else if (username.equals(UserHelper.getInstance().getOwnerProfile().username)) {
            TextView updatestatus = getDialog().findViewById(R.id.add_friend_status);
            updatestatus.setVisibility(View.VISIBLE);
            updatestatus.setText(getString(R.string.no_self));
        } else {
            //hide keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            // verify if the soft keyboard is open
            if (imm != null && getDialog().getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getDialog().getCurrentFocus().getWindowToken(), 0);
            }

            final ProgressDialog progress = new ProgressDialog(getActivity());
            progress.setMessage(getString(R.string.adding));
            progress.show();

            RESTGetUserInfo sm = new RESTGetUserInfo(username);
            sm.setListener(new RESTGetUserInfo.Listener() {
                @Override
                public void onSuccess() {
                    progress.cancel();
                    ((FriendsActivity) getActivity()).onActivityResult(Constants.RESULT_FRIENDS_ADD, Activity.RESULT_OK, new Intent(username));
                    dismiss();
                }

                @Override
                public void onFailure(String errMsg) {
                    progress.cancel();
                    TextView updatestatus = getDialog().findViewById(R.id.add_friend_status);
                    updatestatus.setVisibility(View.VISIBLE);
                    updatestatus.setText(errMsg);
                }
            });
            sm.execute();
        }
    }
}