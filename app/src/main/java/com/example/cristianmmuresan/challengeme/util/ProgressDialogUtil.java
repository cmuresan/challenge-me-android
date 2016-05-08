package com.example.cristianmmuresan.challengeme.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Cristian M. Muresan on 5/8/2016.
 */
public class ProgressDialogUtil extends ProgressDialog {

    public ProgressDialogUtil(Context context) {
        super(context);
        this.setCancelable(true);
        this.setMessage("Loading ...");
    }
}
