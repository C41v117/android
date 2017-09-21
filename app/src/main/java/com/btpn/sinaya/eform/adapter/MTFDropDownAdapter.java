package com.btpn.sinaya.eform.adapter;


import java.util.List;

import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.model.MTFMasterDataFormContentModel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MTFDropDownAdapter extends ArrayAdapter<MTFMasterDataFormContentModel>{

	int resourceId;
	private Context context;
	
	public MTFDropDownAdapter(Context context, int resourceId,
			List<MTFMasterDataFormContentModel> masterDataModel) {
		super(context, resourceId, masterDataModel);
		this.context = context;
		this.resourceId = resourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);

        ((TextView) v).setTextSize(14);
        ((TextView) v).setText(getItem(position).getTitle());
        ((TextView) v).setTextColor(context.getResources().getColorStateList(R.color.black));
        ((TextView) v).setWidth(LayoutParams.MATCH_PARENT);
        ((TextView) v).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_drop_down, 0);
		return v;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View v =super.getDropDownView(position, convertView, parent);

        ((TextView) v).setTextSize(14);
        ((TextView) v).setText(getItem(position).getTitle());
        ((TextView) v).setTextColor(context.getResources().getColorStateList(R.color.black));
        ((TextView) v).setWidth(LayoutParams.MATCH_PARENT);

		return v;
	}
	
	

}
