package com.nextzy.library.core;

import android.support.v4.app.DialogFragment;

/**
 * Created by Akexorcist on 5/1/2017 AD.
 */

public abstract class DialogHelperActivity extends LocalizationHelperActivity {
    private static final String TAG_DEFAULT_DIALOG = "default_dialog";

    private DialogFragment dialogFragment;

    public void showDialog(DialogFragment dialogFragment) {
        showDialog(dialogFragment, TAG_DEFAULT_DIALOG);
    }

    public void showDialog(DialogFragment dialogFragment, String tag) {
        dismissDialog(tag);
        this.dialogFragment = dialogFragment;
        dialogFragment.show(getSupportFragmentManager(), tag);
    }

    public boolean isDialogShowing() {
        return isDialogShowing(TAG_DEFAULT_DIALOG);
    }

    public boolean isDialogShowing(String tag) {
        return dialogFragment != null && getSupportFragmentManager().findFragmentByTag(tag) != null;
    }

    public boolean dismissDialog() {
        return dismissDialog(TAG_DEFAULT_DIALOG);
    }

    public boolean dismissDialog(String tag) {
        if (dialogFragment != null && getSupportFragmentManager().findFragmentByTag(tag) != null) {
            dialogFragment.dismiss();
            return true;
        } else {
            return false;
        }
    }
}
