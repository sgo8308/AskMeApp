//package com.example.firstapp;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//
//public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder> {
//        //아이템의 데이터를 담고 있는 클래스를 담아두는 리스트를 생성한다.
//        ArrayList<PaymentHistoryData> mItems;
//        //따로 정의한 OnTestItemClickListener라는 인터페이스의 변수를 만든다.
//        OnCancelPayClickListener listener;
//
//        @NonNull
//        @Override
//        //뷰홀더가 새로 만들어지는 시점에 호출된다. 레이아웃인플레이터를 통해 아이템의 레이아웃을 객체화 시켜준 후 새 뷰홀더에 담아 반환해준다.
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
//            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
//            View itemView = inflater.inflate(R.layout.item_payment_history,viewGroup,false);
//            return new ViewHolder(itemView, listener);
//        }
//
//        @Override
//        //뷰홀더가 재사용되는 시점에 호출된다. 화면에 새롭게 보이게 되는 아이템의 포지션을 참조한 후 뷰홀더에서 아이템의 데이터를 세팅해준다.
//        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
//            PaymentHistoryData item = mItems.get(position);
//            viewHolder.setItem(item);
//            Log.i("알림",String.valueOf(position));
//        }
//        //데이터 리스트의 갯수를 반환해준다. 리싸이클러뷰에서 어댑터에서 관리하는 아이템의 개수를 알아야 할 때 사용된다.
//        @Override
//        public int getItemCount() {
//            if(mItems != null){
//                return mItems.size();
//            }
//            return 0;
//        }
//
//        //뷰홀더 클래스를 만든다.
//        static class ViewHolder extends RecyclerView.ViewHolder{
//            TextView textView_itemName;
//            TextView textView_date;
//            Button button_cancelPayment;
//
//            public ViewHolder(final View itemView, final OnCancelPayClickListener listner){
//                super(itemView);
//                textView_itemName = itemView.findViewById(R.id.textView_itemName);
//                textView_date = itemView.findViewById(R.id.textView_date);
//                button_cancelPayment = itemView.findViewById(R.id.button_cancelPayment);
//                button_cancelPayment.setOnClickListener(new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//                        int position = getAdapterPosition();
//                        if(listner !=null){
//                            listner.onItemClick(position);
//                        }
//                    }
//                });
//            }
//            //아이템에 데이터를 세팅해준다.
//            public void setItem(PaymentHistoryData item){
//                textView_itemName.setText(item.getItem_name());
//                textView_date.setText(item.getPurchased_at());
//            }
//        }
//
//        public void addItem(PaymentHistoryData item){
//            mItems.add(item);
//        }
//
//        public PaymentHistoryData getItem(int position){
//            return mItems.get(position);
//        }
//
//        public void setOnItemClickListener(OnCancelPayClickListener listener){
//            this.listener = listener;
//        }
//
//        public void setmItems(ArrayList<PaymentHistoryData> mItems){
//            this.mItems = mItems;
//        }
//    }
