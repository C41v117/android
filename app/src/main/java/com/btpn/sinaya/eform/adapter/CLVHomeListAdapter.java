package com.btpn.sinaya.eform.adapter;

/**
 * Created by vaniaw on 10/9/2017.
 */


import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.btpn.sinaya.eform.R;
import com.btpn.sinaya.eform.database.MTFDatabaseHelper;
import com.btpn.sinaya.eform.model.CLVUserModel;
import com.btpn.sinaya.eform.model.MTFUserModel;

public class CLVHomeListAdapter extends ArrayAdapter<CLVUserModel>{

    private int resourceId;
    private List<CLVUserModel> customerModels;
    private MTFUserModel userModel;

    public CLVHomeListAdapter(Context context, int resourceId, List<CLVUserModel> customerModels) {
        super(context, resourceId, customerModels);
        this.resourceId = resourceId;
        this.customerModels = customerModels;
        MTFDatabaseHelper database = MTFDatabaseHelper.getInstance(context);
        this.userModel = database.getActiveSession();
    }

    public void updateList(List<CLVUserModel> model){
        customerModels.clear();
        customerModels.addAll(model);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final TextView name;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
        }
        final CLVUserModel customerModel = customerModels.get(position);
        name = (TextView)convertView.findViewById(R.id.customer_list_nama_textview);
        ImageView home_img = (ImageView)convertView.findViewById(R.id.home_list_img);
        ImageView profile_img = (ImageView)convertView.findViewById(R.id.profile_img);
        ImageView love_img = (ImageView)convertView.findViewById(R.id.love_iv);
        TextView love_count = (TextView)convertView.findViewById(R.id.customer_list_love_count);
        TextView home_location = (TextView)convertView.findViewById(R.id.home_location);
        TextView home_caption = (TextView)convertView.findViewById(R.id.home_caption);
        TextView home_date = (TextView)convertView.findViewById(R.id.home_date_caption);

        SimpleDateFormat  format = new SimpleDateFormat("dd-MM-yyyy");


        if(customerModel.getUsername()!=null){
            name.setText(customerModel.getUsername());
        }else
            name.setText("elizavania");

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("bicon", R.drawable.brezyn_icons);
        home_img.setImageResource(map.get("bicon"));
        profile_img.setImageResource(map.get("bicon"));
        love_img.setImageResource(map.get("bicon"));
        love_count.setText(R.string.dummy_love);
        home_location.setText(R.string.dummy_location);
        home_caption.setText(R.string.dummy_caption);
        home_date.setText(R.string.dummy_date_caption);

//        if(customerModel.getProblemMessage()!=null &&
//                !customerModel.getProblemMessage().trim().equals("") && customerModel.getStatusRegistration()==MTFCustomerStatus.DITOLAK){
//            info.setVisibility(View.VISIBLE);
//            info.setText(customerModel.getProblemMessage());
//        }else{
//            info.setVisibility(View.GONE);
//            info.setText("");
//        }
//
//        if(customerModel.getProductType()!=null){
//            phoneNumber.setVisibility(View.VISIBLE);
//            if(customerModel.getHighRisk() == 1 && !(customerModel.getStatusRegistration()==MTFCustomerStatus.DISETUJUI && customerModel.getStatusFail() == 0)) {
//                phoneNumber.setText(customerModel.getProductType());
//            }else{
//                String text = customerModel.getProductType();
//                if(customerModel.getAccountNo()!=null && !customerModel.getAccountNo().equals("") && !customerModel.getAccountNo().equalsIgnoreCase("null"))
//                    text = text + " - " + customerModel.getAccountNo();
//                if(customerModel.getCifNo()!=null && !customerModel.getCifNo().equals("") && !customerModel.getCifNo().equalsIgnoreCase("null"))
//                    text = text + " - " + customerModel.getCifNo();
//                phoneNumber.setText(text);
//            }
//        }else{
//            phoneNumber.setVisibility(View.GONE);
//        }
//
//        if(customerModel.getStatusRegistration()==MTFCustomerStatus.DRAFT){
//            status.setText(MTFCustomerStatus.DRAFT.toString());
//            status.setTextColor(getContext().getResources().getColor(R.color.white));
//            syncDate.setTextColor(getContext().getResources().getColor(R.color.white));
//            statusLayout.setBackground(getContext().getResources().getDrawable(R.drawable.status_grey));
//            deleteBtn.setVisibility(View.VISIBLE);
//        }else if(customerModel.getStatusRegistration()==MTFCustomerStatus.READY_TO_SENT){
//            status.setText("READY TO SENT");
//            status.setTextColor(getContext().getResources().getColor(R.color.black));
//            syncDate.setTextColor(getContext().getResources().getColor(R.color.black));
//            statusLayout.setBackground(getContext().getResources().getDrawable(R.drawable.status_yellow));
//        }else if(customerModel.getStatusRegistration()==MTFCustomerStatus.VERIFIKASI){
//            status.setText(MTFCustomerStatus.VERIFIKASI.toString());
//            status.setTextColor(getContext().getResources().getColor(R.color.black));
//            syncDate.setTextColor(getContext().getResources().getColor(R.color.black));
//            statusLayout.setBackground(getContext().getResources().getDrawable(R.drawable.status_yellow));
//        }else if(customerModel.getStatusRegistration()==MTFCustomerStatus.APPROVAL){
//            status.setText(MTFCustomerStatus.APPROVAL.toString());
//            status.setTextColor(getContext().getResources().getColor(R.color.black));
//            syncDate.setTextColor(getContext().getResources().getColor(R.color.black));
//            statusLayout.setBackground(getContext().getResources().getDrawable(R.drawable.status_yellow));
//        }else if(customerModel.getStatusRegistration()==MTFCustomerStatus.EDD){
//            status.setText(MTFCustomerStatus.EDD.toString());
//            status.setTextColor(getContext().getResources().getColor(R.color.black));
//            syncDate.setTextColor(getContext().getResources().getColor(R.color.black));
//            statusLayout.setBackground(getContext().getResources().getDrawable(R.drawable.status_yellow));
//        }else if(customerModel.getStatusRegistration()==MTFCustomerStatus.DISETUJUI  && customerModel.getStatusFail() == 0){
//            status.setText(MTFCustomerStatus.DISETUJUI.toString() + " - SUKSES");
//            status.setTextColor(getContext().getResources().getColor(R.color.white));
//            syncDate.setTextColor(getContext().getResources().getColor(R.color.white));
//            statusLayout.setBackground(getContext().getResources().getDrawable(R.drawable.status_green));
//        }else if(customerModel.getStatusRegistration()==MTFCustomerStatus.DISETUJUI && customerModel.getStatusFail() == 1){
//            status.setText(MTFCustomerStatus.DISETUJUI.toString()+" - GAGAL");
//            status.setTextColor(getContext().getResources().getColor(R.color.black));
//            syncDate.setTextColor(getContext().getResources().getColor(R.color.black));
//            statusLayout.setBackground(getContext().getResources().getDrawable(R.drawable.status_yellow));
//        }else if(customerModel.getStatusRegistration()==MTFCustomerStatus.TERKIRIM){
//            status.setText(MTFCustomerStatus.TERKIRIM.toString());
//            status.setTextColor(getContext().getResources().getColor(R.color.white));
//            syncDate.setTextColor(getContext().getResources().getColor(R.color.white));
//            statusLayout.setBackground(getContext().getResources().getDrawable(R.drawable.status_green));
//        }else if(customerModel.getStatusRegistration()==MTFCustomerStatus.MASALAH){
//            if(userModel.getAgentType().equals("PB")) {
//                status.setText("RETURN TO PB");
//            } else if(userModel.getAgentType().equals("CS")) {
//                status.setText("RETURN TO CS");
//            }
//            status.setTextColor(getContext().getResources().getColor(R.color.white));
//            syncDate.setTextColor(getContext().getResources().getColor(R.color.white));
//            statusLayout.setBackground(getContext().getResources().getDrawable(R.drawable.status_red));
//        }else if(customerModel.getStatusRegistration()==MTFCustomerStatus.DITOLAK){
//            status.setText(MTFCustomerStatus.DITOLAK.toString());
//            status.setTextColor(getContext().getResources().getColor(R.color.white));
//            syncDate.setTextColor(getContext().getResources().getColor(R.color.white));
//            statusLayout.setBackground(getContext().getResources().getDrawable(R.drawable.status_red));
//        }else if(customerModel.getStatusRegistration()==MTFCustomerStatus.GAGAL_BARU || customerModel.getStatusRegistration()==MTFCustomerStatus.GAGAL_REPAIR){
//            status.setText("GAGAL");
//            status.setTextColor(getContext().getResources().getColor(R.color.white));
//            syncDate.setTextColor(getContext().getResources().getColor(R.color.white));
//            statusLayout.setBackground(getContext().getResources().getDrawable(R.drawable.status_red));
//        }else if(customerModel.getStatusRegistration()==MTFCustomerStatus.PENGIRIMAN_BARU || customerModel.getStatusRegistration()==MTFCustomerStatus.PENGIRIMAN_REPAIR){
//            status.setText("PENGIRIMAN");
//            status.setTextColor(getContext().getResources().getColor(R.color.black));
//            syncDate.setTextColor(getContext().getResources().getColor(R.color.black));
//            statusLayout.setBackground(getContext().getResources().getDrawable(R.drawable.status_yellow));
//        }else if(customerModel.getStatusRegistration()==MTFCustomerStatus.AKTIFASI){
//            status.setText(MTFCustomerStatus.AKTIFASI.toString());
//            status.setTextColor(getContext().getResources().getColor(R.color.white));
//            syncDate.setTextColor(getContext().getResources().getColor(R.color.white));
//            statusLayout.setBackground(getContext().getResources().getDrawable(R.drawable.status_red));
//        }else if(customerModel.getStatusRegistration()==MTFCustomerStatus.CALLBACK){
//            status.setText(MTFCustomerStatus.CALLBACK.toString());
//            status.setTextColor(getContext().getResources().getColor(R.color.black));
//            syncDate.setTextColor(getContext().getResources().getColor(R.color.black));
//            statusLayout.setBackground(getContext().getResources().getDrawable(R.drawable.status_yellow));
//        }else if(customerModel.getStatusRegistration()==MTFCustomerStatus.PRE_EDD){
//            status.setText(MTFCustomerStatus.PRE_EDD.toString().replace("_"," "));
//            status.setTextColor(getContext().getResources().getColor(R.color.black));
//            syncDate.setTextColor(getContext().getResources().getColor(R.color.black));
//            statusLayout.setBackground(getContext().getResources().getDrawable(R.drawable.status_yellow));
//        }
//
//        if(customerModel.getSyncronizeDate()==null){
//            syncDate.setText("");
//        }else{
//            syncDate.setText(format.format(customerModel.getSyncronizeDate()));
//        }
//        regDate.setText("-");
//        if(customerModel.getRegistrationDate() != null && !customerModel.getRegistrationDate().equals("")){
//            regDate.setText(format.format(customerModel.getRegistrationDate()));
//        }
//
//        deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showConfirmationDialog(customerModel, position);
//            }
//        });

        return convertView;
    }


}
