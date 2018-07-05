package com.getmp3filefromstorage;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CustomHeader extends RelativeLayout  {
    public Context objContext;

    View view = null;



    @BindView(R.id.txtCenterTitle)
    public TextView txtCenterTitle;

    private InputMethodManager imm = null;

    public CustomHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        objContext = context;
        init();
    }

    public CustomHeader(final Context context) {
        super(context);
        objContext = context;

        init();

    }

    /*public void setHeaderTitle() {
        txtCenterTitle.setVisibility(VISIBLE);
    }*/



    public void init() {
        view = LayoutInflater.from(objContext).inflate(R.layout.cust_header, this, true);

        ButterKnife.bind(this, view);
        imm = (InputMethodManager) objContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    }




}
