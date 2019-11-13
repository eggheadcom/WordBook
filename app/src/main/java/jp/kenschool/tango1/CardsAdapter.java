package jp.kenschool.tango1;



import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder>  {

    //―――――――――― フィールド  ――――――――――
    private List<Word> itemList;
    private int langMode;


    //―――――――――― インナークラス ViewHolder  ――――――――――
    //（これが1枚ずつのカードアイテム）
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView1;
        CardView card;
        //Button btnRemove;
        boolean mode;       //true=英語  false=日本語

        ViewHolder(View v) {
            super(v);
            textView1 = v.findViewById(R.id.text_view1);
            //btnRemove = v.findViewById(R.id.btn_remove);
            card = (CardView) v;
            mode = true;
        }

        //色セットするメソッド
        public void setColor(int color){
            card.setCardBackgroundColor(color);
        }
    }

    //―――――――――― コンストラクタ  ――――――――――
    CardsAdapter(List<Word> list, int mode){
        this.itemList = list;
        this.langMode = mode;
    }


    //―――――――――― onCreate 生成  ――――――――――
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards, parent, false);

        return new ViewHolder(view);
    }

    //―――――――――― onBind  ――――――――――
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position){

        //初期セッティング
        String jpn = itemList.get(position).getJpn();
        String eng = itemList.get(position).getEng();

        if(langMode == 1){ //日本語から
            holder.textView1.setText(jpn); //textViewに文字セット
            holder.setColor(Color.parseColor("#56ADBF"));
            holder.mode = true;

        }else if(langMode == 2){ //英語から
            holder.textView1.setText(eng); //textViewに文字セット
            holder.setColor(Color.parseColor("#D81B60"));
            holder.mode = false;
        }

        // カードタップ用リスナー
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCard(holder, position);   //チェンジメソッドに飛ぶ
            }
        });

        //削除
//        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                removeItem(position);
//            }
//        });
    }

    //―――――――――― 追加 ――――――――――
    public void addItem(Word data){
        itemList.add(data);
        notifyDataSetChanged();  //これで反映させる
    }

    //―――――――――― カードタップ ――――――――――
    public void changeCard(ViewHolder holder, int position){

        String jpn = itemList.get(position).getJpn();
        String eng = itemList.get(position).getEng();

        if(holder.mode) {               //裏返して英語に
            holder.textView1.setText(eng);
            holder.setColor(Color.parseColor("#D81B60"));
            holder.mode = false;
        }else{                                  //裏返して日本語に
            holder.textView1.setText(jpn);
            holder.setColor(Color.parseColor("#56ADBF"));
            holder.mode = true;
        }
    }


//    //―――――――――― 削除  ――――――――――
//    public void removeItem(int position){
//        itemList.remove(position);
//        notifyItemRemoved(position);
//        notifyDataSetChanged();
//    }

    //―――――――――― カウント ――――――――――
    @Override
    public int getItemCount(){
        return itemList.size();
    }


}