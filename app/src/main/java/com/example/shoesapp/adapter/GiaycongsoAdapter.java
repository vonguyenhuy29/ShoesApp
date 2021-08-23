package com.example.shoesapp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shoesapp.R;
import com.example.shoesapp.model.Sanpham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GiaycongsoAdapter extends BaseAdapter {
    Context context;
    ArrayList<Sanpham> arraygiaycongso;

    public GiaycongsoAdapter(Context context, ArrayList<Sanpham> arraygiaycongso) {
        this.context = context;
        this.arraygiaycongso = arraygiaycongso;
    }

    @Override
    public int getCount() { //get về số lượng dòng
        return arraygiaycongso.size();
    }

    @Override
    public Object getItem(int i) {
        return arraygiaycongso.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder{
        public TextView txttengiaycongso,txtgiagiaycongso,txtmotagiaycongso;
        public ImageView imggiaycongso;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        GiaycongsoAdapter.ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new GiaycongsoAdapter.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_giaycongso,null);
            viewHolder.txttengiaycongso = (TextView) view.findViewById(R.id.textviewgiaycongso);
            viewHolder.txtgiagiaycongso = (TextView) view.findViewById(R.id.textviewgiagiaycongso);
            viewHolder.txtmotagiaycongso = (TextView) view.findViewById(R.id.textviewmotagiaycongso);
            viewHolder.imggiaycongso = (ImageView) view.findViewById(R.id.imageviewgiaycongso);
            view.setTag(viewHolder);
        }else{
            viewHolder = (GiaycongsoAdapter.ViewHolder) view.getTag();

        }
        Sanpham sanpham = (Sanpham) getItem(i);
        viewHolder.txttengiaycongso.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###"); //Định dạng lại sô 000.000.000
        viewHolder.txtgiagiaycongso.setText("Giá : " + decimalFormat.format(sanpham.getGiasanpham()) + " Đ");
        viewHolder.txtmotagiaycongso.setMaxLines(2); //set xuống dòng(2 dòng)
        viewHolder.txtmotagiaycongso.setEllipsize(TextUtils.TruncateAt.END); //thêm dấu ... nếu dòng dài quá
        viewHolder.txtmotagiaycongso.setText(sanpham.getMotasanpham());
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.noimage) // hình ảnh đợi load
                .error(R.drawable.error) //hình load ko thành công
                .into(viewHolder.imggiaycongso);

        return view;
    }
}
