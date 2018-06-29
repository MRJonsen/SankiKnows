package com.zc.pickuplearn.ui.district_manager.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hss01248.dialog.StyledDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.xframe.common.XActivityStack;
import com.youth.xframe.widget.loadingview.XLoadingView;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.beans.CourseWareBean;
import com.zc.pickuplearn.beans.CourseWareCommentBean;
import com.zc.pickuplearn.http.API;
import com.zc.pickuplearn.http.CommonCallBack;
import com.zc.pickuplearn.ui.base.BaseFragment;
import com.zc.pickuplearn.ui.district_manager.adapter.CourseWareCommentAdapter;
import com.zc.pickuplearn.ui.view.KeyboardLayout;
import com.zc.pickuplearn.utils.KeyBoardUtils;
import com.zc.pickuplearn.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 评论
 */
public class CourseCommentFragment extends BaseFragment {

    @BindView(R.id.rc_content)
    RecyclerView rcContent;
    @BindView(R.id.x_loading)
    XLoadingView xLoading;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.et_comment)
    TextView mEditComment;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private CourseWareBean.DatasBean mParam1;
    private String mParam2;
    List<CourseWareCommentBean> mData = new ArrayList<>();
    boolean IS_REFRESH = true;
    private int mCurrent = 0;
    private CourseWareCommentAdapter courseWareCommentAdapter;
    private Dialog dialog;

    public CourseCommentFragment() {
        // Required empty public constructor
    }

    public static CourseCommentFragment newInstance(CourseWareBean.DatasBean param1) {
        CourseCommentFragment fragment = new CourseCommentFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_course_comment;
    }

    @Override
    public void initView() {
        if (getArguments() != null) {
            mParam1 = (CourseWareBean.DatasBean) getArguments().getSerializable(ARG_PARAM1);
        }
        initXloading();
        initRecycleView();
        initRefresh();
        mEditComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePayWayshowDialog();
            }
        });
//        mEditComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (v.getId() == R.id.et_comment && EditorInfo.IME_ACTION_SEND == actionId) {
//                    String searchtext = mEditComment.getText().toString();
//                    KeyBoardUtils.closeKeybord(mEditComment, getContext());
//                    if (TextUtils.isEmpty(searchtext)){
//                        ToastUtils.showToast(getmCtx(),"请输入评论");
//                        return true;
//                    }
//                    mEditComment.setText("");
//                    API.getInstance().courseComment(mParam1, searchtext, new CommonCallBack<String>() {
//                        @Override
//                        public void onSuccess(String s) {
//                            if (refreshLayout!=null)
//                                refreshLayout.autoRefresh();
//                        }
//
//                        @Override
//                        public void onFailure() {
//
//                        }
//                    });
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    private void initXloading() {
        xLoading.setEmptyViewWarning("暂无评论");
    }

    private void initRecycleView() {
        rcContent.setHasFixedSize(true);
        rcContent.setLayoutManager(new LinearLayoutManager(getmCtx()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rcContent.setItemAnimator(new DefaultItemAnimator()); //设置Item增加、移除动画
        courseWareCommentAdapter = new CourseWareCommentAdapter(getmCtx(), mData);
        rcContent.setAdapter(courseWareCommentAdapter);
    }

    private void initRefresh() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (refreshlayout.isLoadmoreFinished())
                    refreshlayout.setLoadmoreFinished(false);
                IS_REFRESH = true;
                mCurrent = 0;
                mData.clear();
                getData();
            }
        });
        refreshLayout.setEnableLoadmore(true);
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                IS_REFRESH = false;
                getData();
            }
        });

        refreshLayout.autoRefresh();
    }

    public void getData() {
        API.getInstance().courseWareCommentData(mParam1, mCurrent, new CommonCallBack<List<CourseWareCommentBean>>() {
            @Override
            public void onSuccess(List<CourseWareCommentBean> courseWareCommentBeen) {
                if (courseWareCommentAdapter != null) {
                    if (IS_REFRESH) {
                        mData.clear();
                    }

                    mData.addAll(courseWareCommentBeen);
                    mCurrent = mData.size();
                    courseWareCommentAdapter.notifyDataSetChanged();
                }
                finishRefresh();
            }

            @Override
            public void onFailure() {
                finishRefresh();
            }
        });
    }

    private void finishRefresh() {
        if (refreshLayout != null) {
            if (IS_REFRESH) {
                refreshLayout.finishRefresh();
            } else {
                refreshLayout.finishLoadmore();
            }
        }
        if (xLoading != null) {
            if (mCurrent == 0) {
                xLoading.showEmpty();
            } else {
                xLoading.showContent();
            }
        }
    }

    private void choosePayWayshowDialog() {
        if (dialog == null) {
            // final BottomSheetDialog dialog = new BottomSheetDialog(this);
            View contentView = LayoutInflater.from(getmCtx()).inflate(R.layout.input_dialog, null);
            final EditText input = (EditText) contentView.findViewById(R.id.et_comment);
            final Button send = (Button) contentView.findViewById(R.id.btn_send);
            input.setText(mEditComment.getText().toString());

            input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mEditComment.setText(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String searchtext = mEditComment.getText().toString();
                    KeyBoardUtils.closeKeybord(mEditComment, getContext());
                    if (TextUtils.isEmpty(searchtext)) {
                        ToastUtils.showToast(getmCtx(), "请输入评论");
                        return;
                    }
                    send.setClickable(false);
                    API.getInstance().courseComment(mParam1, searchtext, new CommonCallBack<String>() {
                        @Override
                        public void onSuccess(String s) {
                            send.setClickable(true);
                            mEditComment.setText("");
                            input.setText("");
                            if (refreshLayout != null)
                                refreshLayout.autoRefresh();
                            if (dialog != null) {
                                KeyBoardUtils.closeKeybord(input, getContext());
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure() {
                            send.setClickable(true);
                            if (dialog != null)
                                dialog.dismiss();
                        }
                    });
                }
            });
            input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (v.getId() == R.id.et_comment && EditorInfo.IME_ACTION_SEND == actionId) {
                        String searchtext = mEditComment.getText().toString();
                        KeyBoardUtils.closeKeybord(mEditComment, getContext());
                        if (TextUtils.isEmpty(searchtext)) {
                            ToastUtils.showToast(getmCtx(), "请输入评论");
                            return true;
                        }
                        mEditComment.setText("");
                        input.setText("");
                        API.getInstance().courseComment(mParam1, searchtext, new CommonCallBack<String>() {
                            @Override
                            public void onSuccess(String s) {
                                if (refreshLayout != null)
                                    refreshLayout.autoRefresh();

                                if (dialog != null)
                                    dialog.dismiss();
                            }

                            @Override
                            public void onFailure() {
                                if (dialog != null)
                                    dialog.dismiss();
                            }
                        });
                        return true;
                    }
                    return false;
                }
            });
            //不好建立回调
            dialog = StyledDialog.buildCustomBottomSheet(contentView).setActivity(XActivityStack.getInstance().topActivity()).setCancelable(true, true).show();
            KeyBoardUtils.openKeybord(input, getmCtx());
        } else {
            dialog.show();
            KeyBoardUtils.openKeybord(mEditComment, getmCtx());
        }
    }
}
