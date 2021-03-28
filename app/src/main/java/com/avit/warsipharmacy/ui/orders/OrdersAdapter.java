package com.avit.warsipharmacy.ui.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avit.warsipharmacy.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderItemViewHolder>{

    public interface OrdersInterface{
        void openDetailsTab(OrderItem orderItem);
    }

    private Context context;
    private List<OrderItem> orderItemList;
    private OrdersInterface ordersInterface;

    public OrdersAdapter(Context context,OrdersInterface ordersInterface){
        this.context = context;
        orderItemList = new ArrayList<>();
        this.ordersInterface = ordersInterface;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item,parent,false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        final OrderItem currItem = orderItemList.get(position);

        holder.orderIdView.setText(currItem.getOrder_id());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        holder.orderDateView.setText(simpleDateFormat.format(currItem.getOrder_date()));
        holder.orderQuantityView.setText(currItem.getOrderItems().size()+"");

        int total_amount = OrderItem.getTotal(currItem.getOrderItems());
        holder.orderTotalView.setText("₹" + total_amount);

        holder.openDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordersInterface.openDetailsTab(currItem);
            }
        });

        holder.orderStatusView.setText(OrderItem.getOrderStatus(currItem.getStatus()));
        switch (currItem.getStatus()){
            case 0:
                holder.orderStatusView.setTextColor(context.getResources().getColor(R.color.warningColor));
                break;
            case 1:
                holder.orderStatusView.setTextColor(context.getResources().getColor(R.color.warningColor));
                break;
            case 2:
                holder.orderStatusView.setTextColor(context.getResources().getColor(R.color.successColor));
                break;

            case -1:
                holder.orderStatusView.setTextColor(context.getResources().getColor(R.color.cancelledColor));
                break;
        }

    }


    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
        notifyDataSetChanged();
    }

    public class OrderItemViewHolder extends RecyclerView.ViewHolder{

        public TextView orderIdView,orderDateView,orderQuantityView,orderTotalView,orderStatusView;
        public Button openDetailsButton;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);

            orderIdView = itemView.findViewById(R.id.order_id);
            orderDateView = itemView.findViewById(R.id.order_date);
            orderQuantityView = itemView.findViewById(R.id.quantity);
            orderTotalView = itemView.findViewById(R.id.total_price);
            orderStatusView = itemView.findViewById(R.id.order_status);

            openDetailsButton = itemView.findViewById(R.id.details);

        }
    }
}
