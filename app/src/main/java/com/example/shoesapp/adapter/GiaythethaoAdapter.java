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

public class GiaythethaoAdapter extends BaseAdapter {
    Context context;
    ArrayList <Sanpham> arraygiaythethao;

    public GiaythethaoAdapter(Context context, ArrayList<Sanpham> arraygiaythethao) {
        this.context = context;
        this.arraygiaythethao = arraygiaythethao;
    }

    @Override
    public int getCount() {
        return arraygiaythethao.size();
    }

    @Override
    public Object getItem(int i) {
        return arraygiaythethao.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder{
        public TextView txttengiaythethao,txtgiagiaythethao,txtmotagiaythethao;
        public ImageView imggiaythethao;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_giaythethao,null);
            viewHolder.txttengiaythethao = (TextView) view.findViewById(R.id.textviewgiaythethao);
            viewHolder.txtgiagiaythethao = (TextView) view.findViewById(R.id.textviewgiagiaythethao);
            viewHolder.txtmotagiaythethao = (TextView) view.findViewById(R.id.textviewmotagiaythethao);
            viewHolder.imggiaythethao = (ImageView) view.findViewById(R.id.imageviewgiaythethao);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();

        }
        Sanpham sanpham = (Sanpham) getItem(i);
        viewHolder.txttengiaythethao.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtgiagiaythethao.setText("Giá : " + decimalFormat.format(sanpham.getGiasanpham()) + " Đ");
        viewHolder.txtmotagiaythethao.setMaxLines(2);
        viewHolder.txtmotagiaythethao.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtmotagiaythethao.setText(sanpham.getMotasanpham());
        Picasso.with(context).load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.error)
                .into(viewHolder.imggiaythethao);

        return view;
    }
}
