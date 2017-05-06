package com.mesor.test.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mesor.test.R;
import com.mesor.test.framework.BaseFragment;

import butterknife.BindView;

/**
 * Created by Limeng on 2017/5/5.
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.toolBar)
    Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_home);
    }

    @Override
    public void initViewProperty() {
        getAppCompatActivity().setSupportActionBar(toolbar);
        toolbar.setTitle("项目名称");
    }

    @Override
    public void initData() {

    }
}
