package com.mob.mobapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mob.mobapp.R;
import com.mob.mobapp.pojos.Order;
import com.mob.mobapp.presenters.OrdersViewPresenter;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {
    private ArrayList<Order> orderArrayList;

    private final Context context;

    public OrdersAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        Order order = orderArrayList.get(position);

        // set description
        TextView textViewDescription = (TextView) holder.itemView.findViewById(R.id.textViewOrderDesc);
        textViewDescription.setText(order.getDesc());

        // set date-time and status
        TextView textViewTimeAndStatus = (TextView) holder.itemView.findViewById(R.id.timeAndStatus);
        textViewTimeAndStatus.setText(String.format("%s\n%s", order.getDateTime(), order.getStatus()));
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

    public void setOrderArrayList(ArrayList<Order> orderArrayList) {
        this.orderArrayList = orderArrayList;
        notifyDataSetChanged();
    }

    public class OrdersViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewDescription;
        public TextView textViewTimeAndStatus;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDescription = itemView.findViewById(R.id.textViewOrderDesc);
            textViewTimeAndStatus = itemView.findViewById(R.id.timeAndStatus);
        }
    }
}
