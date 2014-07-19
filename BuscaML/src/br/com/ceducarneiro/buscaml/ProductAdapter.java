package br.com.ceducarneiro.buscaml;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends BaseAdapter {

    private List<MLParser.MLProduct> products;

    public ProductAdapter(List<MLParser.MLProduct> products) {
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MLParser.MLProduct product = (MLParser.MLProduct) getItem(position);

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product, parent, false);

            holder = new ViewHolder();
            holder.imgThumb = (URLImageView) convertView.findViewById(R.id.imgThumb);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTitle.setText(product.title);
        holder.txtPrice.setText(new DecimalFormat("R$#0.00").format(product.price));
        holder.imgThumb.setImageURL(product.thumbnail);

        return convertView;
    }

    static class ViewHolder {
        URLImageView imgThumb;
        TextView txtTitle;
        TextView txtPrice;
    }
}
