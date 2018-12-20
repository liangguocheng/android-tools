package com.lange.tools.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.lange.tools.R;

import io.reactivex.disposables.Disposable;

/**
 * Created by liangguocheng on 2018/12/19.
 */

public abstract class RetrofitLoadingObserver<T> extends RetrofitNormalObserver<T> implements DialogInterface.OnCancelListener {

    private ProgressDialog progressDialog;

    public RetrofitLoadingObserver(Context context) {
        this.progressDialog = new ProgressDialog(context);
        this.progressDialog.setTitle("");
        this.progressDialog.setMessage(context.getResources().getString(R.string.network_request_loading));
        this.progressDialog.setCancelable(false);
        this.progressDialog.setCanceledOnTouchOutside(false);
        this.progressDialog.setOnCancelListener(this);
    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        progressDialog.show();
    }


    @Override
    public void onComplete() {
        super.onComplete();
        progressDialog.dismiss();
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        disposable();
    }
}
