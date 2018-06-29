package com.zc.pickuplearn.ui.category;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 问题分类的父列表bean
 * 类描述 ： 作者 ： Jonsen 联系方式 ：chenbin252@163.COM 创建时间：2016-11-16 下午2:05:49 版本 ：
 * V1.0
 * 
 */
public class QusetionTypeBean implements Parcelable{
	// "CODE": "5684",
	// "NAME": "电网假设",
	// "PARENTID": "2322"
	private String CODE;
	/**
	 * ICOPATH :
	 */

	private String ICOPATH;

	public QusetionTypeBean(Parcel in) {
		CODE = in.readString();
		NAME = in.readString();
		PARENTID = in.readString();
	}

	public static final Creator<QusetionTypeBean> CREATOR = new Creator<QusetionTypeBean>() {
		@Override
		public QusetionTypeBean createFromParcel(Parcel in) {
			return new QusetionTypeBean(in);
		}

		@Override
		public QusetionTypeBean[] newArray(int size) {
			return new QusetionTypeBean[size];
		}
	};

	public QusetionTypeBean() {

	}

	public String getCODE() {
		return CODE;
	}
	public void setCODE(String cODE) {
		CODE = cODE;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getPARENTID() {
		return PARENTID;
	}
	public void setPARENTID(String pARENTID) {
		PARENTID = pARENTID;
	}
	private String NAME;
	private String PARENTID;


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(CODE);
		dest.writeString(NAME);
		dest.writeString(PARENTID);
	}

	public String getICOPATH() {
		return ICOPATH;
	}

	public void setICOPATH(String ICOPATH) {
		this.ICOPATH = ICOPATH;
	}
}
