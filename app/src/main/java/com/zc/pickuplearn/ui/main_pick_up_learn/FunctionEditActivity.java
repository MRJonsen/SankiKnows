package com.zc.pickuplearn.ui.main_pick_up_learn;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.FunctionBean;
import com.zc.pickuplearn.event.FunctionEditEvent;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.base.BaseActivity;
import com.zc.pickuplearn.ui.main_pick_up_learn.adapter.MenuFuntionAdapter;
import com.zc.pickuplearn.ui.main_pick_up_learn.adapter.MyFunctionAdapter;
import com.zc.pickuplearn.ui.view.drag.DragCallback;
import com.zc.pickuplearn.ui.view.drag.DragForScrollView;
import com.zc.pickuplearn.ui.view.drag.DragGridView;
import com.zc.pickuplearn.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class FunctionEditActivity extends BaseActivity {
    private static final String ARG_PARAM1 = "param1";
    private DragGridView dragGridView;
    private MyFunctionAdapter adapterSelect; //我的功能适配器
    private TextView tv_set;
    private ArrayList<FunctionBean> menuList = new ArrayList<FunctionBean>();
    private DragGridView dragGridView2;
    private MenuFuntionAdapter menuAdapter;//其他功能适配器
    private LinearLayout ll_top_back;
    private LinearLayout ll_top_sure;
    private TextView tv_top_title;
    private TextView tv_top_sure;
    private TextView tv_drag_tip;
    private DragForScrollView sv_index;
    private List<FunctionBean> indexSelect = new ArrayList<FunctionBean>();
    private LinearLayout ll_root;

    public static void startView(Context context, List<FunctionBean> functionBeanList) {
        Intent intent = new Intent(context, FunctionEditActivity.class);
        intent.putExtra(ARG_PARAM1, (ArrayList) functionBeanList);
        context.startActivity(intent);
    }

    @Override
    public int setLayout() {
        return R.layout.activity_function_edit;
    }

    @Override
    public void initView() {
        findView();
        initListener();
        //处理状态栏及间距
        StatusBarUtil.darkMode(this);
        StatusBarUtil.setPaddingSmart(this, ll_root);
    }

    private void initListener() {
        ll_top_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        ll_top_sure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {//编辑按钮监听
                if (tv_top_sure.getText().toString().equals("管理")) {
                    tv_top_sure.setText("完成");
                    adapterSelect.setEdit();
                    if (menuAdapter != null) {
                        menuAdapter.setEdit();
                    }
                    tv_drag_tip.setVisibility(View.VISIBLE);
                } else {
                    showProgress();
                    API.editFunctionEditSave(adapterSelect.getDatas(), new CommonCallBack<String>() {

                        @Override
                        public void onSuccess(String s) {
                            tv_top_sure.setText("管理");
                            tv_drag_tip.setVisibility(View.GONE);
                            adapterSelect.endEdit();
                            if (menuAdapter != null) {
                                menuAdapter.endEdit();
                            }
                            EventBus.getDefault().post(new FunctionEditEvent(adapterSelect.getDatas()));
                            hideProgress();
                        }

                        @Override
                        public void onFailure() {
                            hideProgress();
                        }
                    });

                }
            }
        });

        dragGridView.setDragCallback(new DragCallback() {
            @Override
            public void startDrag(int position) {
                sv_index.startDrag(position);
            }

            @Override
            public void endDrag(int position) {
                sv_index.endDrag(position);
            }
        });
//        dragGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (!adapterSelect.getEditStatue()) {
//
//                }
//            }
//        });
        dragGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (tv_top_sure.getText().toString().equals("管理")) {
                    tv_top_sure.setText("完成");
                    adapterSelect.setEdit();
                    if (menuAdapter != null)
                        menuAdapter.setEdit();
                    tv_drag_tip.setVisibility(View.VISIBLE);
                }
                dragGridView.startDrag(position);
                return false;
            }
        });
    }

    /**
     * 找控件
     */
    private void findView() {
        dragGridView = (DragGridView) findViewById(R.id.gridview);
        dragGridView2 = (DragGridView) findViewById(R.id.gridview2);
        sv_index = (DragForScrollView) findViewById(R.id.sv_index);
        ll_top_back = (LinearLayout) findViewById(R.id.ll_top_back);
        ll_root = (LinearLayout) findViewById(R.id.root);
        ll_top_sure = (LinearLayout) findViewById(R.id.ll_top_sure);
        tv_top_title = (TextView) findViewById(R.id.tv_top_title);
        tv_top_sure = (TextView) findViewById(R.id.tv_top_sure);
        tv_top_title.setText("全部应用");
        tv_top_sure.setText("管理");
        tv_top_sure.setVisibility(View.VISIBLE);
        tv_drag_tip = (TextView) findViewById(R.id.tv_drag_tip);

    }

    @Override
    protected void initData() {
        myfunctionAdapter();
        editfunctionAdater();
        API.getMoreFunctionList(new CommonCallBack<List<FunctionBean>>() {
            @Override
            public void onSuccess(List<FunctionBean> functionBeanList) {
                menuList.addAll(functionBeanList);
                menuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {

            }
        });
    }


    private void editfunctionAdater() {
        menuAdapter = new MenuFuntionAdapter(this, menuList);
        dragGridView2.setAdapter(menuAdapter);
    }

    private void myfunctionAdapter() {
        List<FunctionBean> indexDataList = (List<FunctionBean>) getIntent().getSerializableExtra(ARG_PARAM1);
        indexDataList.remove(indexDataList.size() - 1);
        indexSelect.clear();
        indexSelect.addAll(indexDataList);
        adapterSelect = new MyFunctionAdapter(this, indexSelect);
        dragGridView.setAdapter(adapterSelect);
    }

    public void DelMeun(FunctionBean indexData, int position) {
        menuList.add(indexData);
        indexSelect.remove(indexData);
        if (menuAdapter != null)
            menuAdapter.notifyDataSetChanged();
        if (adapterSelect != null)
            adapterSelect.notifyDataSetChanged();
    }

    public void AddMenu(FunctionBean menuEntity) {
        menuList.remove(menuEntity);
        indexSelect.add(menuEntity);
        if (menuAdapter != null)
            menuAdapter.notifyDataSetChanged();
        if (adapterSelect != null)
            adapterSelect.notifyDataSetChanged();
    }
}
