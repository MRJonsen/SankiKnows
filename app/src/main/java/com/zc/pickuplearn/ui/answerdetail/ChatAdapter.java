package com.zc.pickuplearn.ui.answerdetail;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zc.pickuplearn.R;
import com.zc.pickuplearn.http.ImageLoaderUtil;
import com.zc.pickuplearn.ui.previewphoto.PreViewImageActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 比原来的多了getItemViewType和getViewTypeCount这两个方法，
 */
public class ChatAdapter extends BaseAdapter {

    public static final String KEY = "key";
    public static final String VALUE = "value";

    public static final int VALUE_LEFT_TEXT = 0;
    public static final int VALUE_LEFT_IMAGE = 1;
    public static final int VALUE_RIGHT_TEXT = 2;
    public static final int VALUE_RIGHT_IMAGE = 3;

    private LayoutInflater mInflater;
    private Context mContext;
    private List<AnswerDetailBean> myList;

    public ChatAdapter(Context context, List<AnswerDetailBean> mdata) {
        this.myList = mdata;
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return myList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        final AnswerDetailBean msg = myList.get(position);
        int type = getItemViewType(position);

        ViewHolderRightText holderRightText = null;
        ViewHolderRightImg holderRightImg = null;

        ViewHolderLeftText holderLeftText = null;
        ViewHolderLeftImg holderLeftImg = null;


        if (convertView == null) {
            switch (type) {
                // 左边
                case VALUE_LEFT_TEXT:
                    holderLeftText = new ViewHolderLeftText();
                    convertView = mInflater.inflate(R.layout.list_item_left_text,
                            null);
                    holderLeftText.ivLeftIcon = (CircleImageView) convertView
                            .findViewById(R.id.iv_icon);
                    holderLeftText.btnLeftText = (TextView) convertView
                            .findViewById(R.id.btn_left_text);
                    holderLeftText.btnLeftText.setText(msg.getEXPLAIN());
                    holderLeftText.tvTimeTip = (TextView) convertView
                            .findViewById(R.id.tv_time_tip);
                    holderLeftText.tvTimeTip.setText(msg.getSYSCREATEDATE());
                    if (!TextUtils.isEmpty(msg.getHAEDIMAG())) {
                        setImage(msg.getHAEDIMAG(), holderLeftText.ivLeftIcon);
                    } else {
                        setDefaitIcon(holderLeftText.ivLeftIcon);
                    }
                    convertView.setTag(holderLeftText);
                    break;

                case VALUE_LEFT_IMAGE:
                    holderLeftImg = new ViewHolderLeftImg();
                    convertView = mInflater.inflate(R.layout.list_item_left_iamge,
                            null);
                    holderLeftImg.ivLeftIcon = (CircleImageView) convertView
                            .findViewById(R.id.iv_icon);
                    holderLeftImg.ivLeftImage = (ImageView) convertView
                            .findViewById(R.id.iv_left_image);
                    setImage(msg.getFILEURL(), holderLeftImg.ivLeftImage);
                    holderLeftImg.ivLeftImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PreViewImageActivity.startPreView(mContext, msg.getFILEURL());
                        }
                    });
                    holderLeftImg.tvTimeTip = (TextView) convertView
                            .findViewById(R.id.tv_time_tip);
                    holderLeftImg.tvTimeTip.setText(msg.getSYSCREATEDATE());
                    if (!TextUtils.isEmpty(msg.getHAEDIMAG())) {
                        setImage(msg.getHAEDIMAG(), holderLeftImg.ivLeftIcon);
                    } else {
                        setDefaitIcon(holderLeftImg.ivLeftIcon);
//                        holderLeftImg.ivLeftIcon.setImageResource(R.mipmap.default_user_circle_icon);
                    }
                    convertView.setTag(holderLeftImg);
                    break;


                // 右边
                case VALUE_RIGHT_TEXT:
                    holderRightText = new ViewHolderRightText();
                    convertView = mInflater.inflate(R.layout.list_item_right_text,
                            null);
                    holderRightText.ivRightIcon = (CircleImageView) convertView
                            .findViewById(R.id.iv_icon);
                    holderRightText.btnRightText = (TextView) convertView
                            .findViewById(R.id.btn_right_text);
                    holderRightText.btnRightText.setText(msg.getEXPLAIN());
                    holderRightText.tvTimeTip = (TextView) convertView
                            .findViewById(R.id.tv_time_tip);
                    holderRightText.tvTimeTip.setText(msg.getSYSCREATEDATE());
                    if (!TextUtils.isEmpty(msg.getHAEDIMAG())) {
                        setImage(msg.getHAEDIMAG(), holderRightText.ivRightIcon);
                    } else {
                        setDefaitIcon(holderRightText.ivRightIcon);
//                        holderRightText.ivRightIcon.setImageResource(R.mipmap.default_user_circle_icon);
                    }
                    convertView.setTag(holderRightText);
                    break;

                case VALUE_RIGHT_IMAGE:
                    holderRightImg = new ViewHolderRightImg();
                    convertView = mInflater.inflate(R.layout.list_item_right_iamge,
                            null);
                    holderRightImg.ivRightIcon = (CircleImageView) convertView
                            .findViewById(R.id.iv_icon);
                    holderRightImg.ivRightImage = (ImageView) convertView
                            .findViewById(R.id.iv_right_image);
                    setImage(msg.getFILEURL(), holderRightImg.ivRightImage);
                    holderRightImg.tvTimeTip = (TextView) convertView
                            .findViewById(R.id.tv_time_tip);
                    holderRightImg.tvTimeTip.setText(msg.getSYSCREATEDATE());
                    if (!TextUtils.isEmpty(msg.getHAEDIMAG())) {
                        setImage(msg.getHAEDIMAG(), holderRightImg.ivRightIcon);
                    } else {
                        setDefaitIcon(holderRightImg.ivRightIcon);
//                        holderRightImg.ivRightIcon.setImageResource(R.mipmap.default_user_circle_icon);
                    }
                    holderRightImg.ivRightImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PreViewImageActivity.startPreView(mContext, msg.getFILEURL());
                        }
                    });
                    convertView.setTag(holderRightImg);
                    break;
                default:
                    break;
            }

        } else {
            switch (type) {
                case VALUE_LEFT_TEXT:
                    holderLeftText = (ViewHolderLeftText) convertView.getTag();
                    holderLeftText.btnLeftText.setText(msg.getEXPLAIN());
                    holderLeftText.tvTimeTip.setText(msg.getSYSCREATEDATE());
                    if (!TextUtils.isEmpty(msg.getHAEDIMAG())) {
                        setImage(msg.getHAEDIMAG(), holderLeftText.ivLeftIcon);
                    } else {
                        setDefaitIcon(holderLeftText.ivLeftIcon);
//                        holderLeftText.ivLeftIcon.setImageResource(R.mipmap.default_user_circle_icon);
                    }
                    break;
                case VALUE_LEFT_IMAGE:
                    holderLeftImg = (ViewHolderLeftImg) convertView.getTag();
                    setImage(msg.getFILEURL(), holderLeftImg.ivLeftImage);
                    holderLeftImg.ivLeftImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PreViewImageActivity.startPreView(mContext, msg.getFILEURL());
                        }
                    });
                    holderLeftImg.tvTimeTip.setText(msg.getSYSCREATEDATE());
                    if (!TextUtils.isEmpty(msg.getHAEDIMAG())) {
                        setImage(msg.getHAEDIMAG(), holderLeftImg.ivLeftIcon);
                    } else {
                        setDefaitIcon(holderLeftImg.ivLeftIcon);
//                        holderLeftImg.ivLeftIcon.setImageResource(R.mipmap.default_user_circle_icon);
                    }
                    break;

                case VALUE_RIGHT_TEXT:
                    holderRightText = (ViewHolderRightText) convertView.getTag();
                    holderRightText.btnRightText.setText(msg.getEXPLAIN());
                    holderRightText.tvTimeTip.setText(msg.getSYSCREATEDATE());
                    if (!TextUtils.isEmpty(msg.getHAEDIMAG())) {
                        setImage(msg.getHAEDIMAG(), holderRightText.ivRightIcon);
                    } else {
                        setDefaitIcon(holderRightText.ivRightIcon);
//                        holderRightText.ivRightIcon.setImageResource(R.mipmap.default_user_circle_icon);
                    }
                    break;
                case VALUE_RIGHT_IMAGE:
                    holderRightImg = (ViewHolderRightImg) convertView.getTag();
                    if (!TextUtils.isEmpty(msg.getHAEDIMAG())) {
                        setImage(msg.getHAEDIMAG(), holderRightImg.ivRightIcon);
                    } else {
                        setDefaitIcon( holderRightImg.ivRightIcon);
//                        holderRightImg.ivRightIcon.setImageResource(R.mipmap.default_user_circle_icon);
                    }
                    holderRightImg.ivRightImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PreViewImageActivity.startPreView(mContext, msg.getFILEURL());
                        }
                    });
                    setImage(msg.getFILEURL(), holderRightImg.ivRightImage);
                    holderRightImg.tvTimeTip.setText(msg.getSYSCREATEDATE());
                    break;

                default:
                    break;
            }

            //holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    private void setDefaitIcon(ImageView view) {
        Glide.with(mContext).load(R.mipmap.default_user_circle_icon).into(view);
    }

    /**
     * 根据数据源的position返回需要显示的的layout的type
     * <p>
     * type的值必须从0开始
     */
    @Override
    public int getItemViewType(int position) {

        AnswerDetailBean msg = myList.get(position);
        int type = msg.getTYPE();

        return type;
    }

    /**
     * 返回所有的layout的数量
     */
    @Override
    public int getViewTypeCount() {
        return 4;
    }

    public void setImage(String url, ImageView image) {
        if (image instanceof CircleImageView) {
            ImageLoaderUtil.displayCircleView(mContext, image, url, false);
        } else {
            ImageLoaderUtil.displayImageResource(mContext, image, url, false);
        }
    }


    class ViewHolderRightText {
        private TextView tvTimeTip;// 时间
        private CircleImageView ivRightIcon;// 右边的头像
        private TextView btnRightText;// 右边的文本
    }

    class ViewHolderRightImg {
        private TextView tvTimeTip;// 时间
        private CircleImageView ivRightIcon;// 右边的头像
        private ImageView ivRightImage;// 右边的图像
    }


    class ViewHolderLeftText {
        private TextView tvTimeTip;// 时间
        private CircleImageView ivLeftIcon;// 左边的头像
        private TextView btnLeftText;// 左边的文本
    }

    class ViewHolderLeftImg {
        private TextView tvTimeTip;// 时间
        private CircleImageView ivLeftIcon;// 左边的头像
        private ImageView ivLeftImage;// 左边的图像
    }


}
