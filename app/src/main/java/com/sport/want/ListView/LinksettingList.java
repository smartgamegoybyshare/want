package com.sport.want.ListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sport.want.ListView.InnerItem.GetInnerItem;
import com.sport.want.ListView.InnerItem.ViewHolder;
import com.sport.want.R;
import com.sport.want.Support.Value;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class LinksettingList extends BaseAdapter{

    private String TAG= "LinksettingList";
    private GetInnerItem getInnerItem;
    private List<String> userLink;
    private List<View> saveView;
    private List<ViewHolder> saveHolder;
    private String nowcompany, nowaccount;

    public LinksettingList(Context context, List<String> userLink, GetInnerItem getInnerItem,
                           String company, String account) {
        this.userLink = userLink;
        LayoutInflater inflater = LayoutInflater.from(context);
        this.getInnerItem = getInnerItem;
        this.nowcompany = company;
        this.nowaccount= account;
        saveView = new ArrayList<>();
        saveHolder = new ArrayList<>();
        saveView.clear();
        saveHolder.clear();
        for(int i = 0; i < userLink.size(); i++){
            @SuppressLint("InflateParams")
            View view = inflater.inflate(R.layout.linksetlist, null);
            ViewHolder viewHolder = new ViewHolder();
            saveView.add(view);
            saveHolder.add(viewHolder);
        }
    }

    @Override
    public int getCount() {
        return userLink.size();
    }

    @Override
    public Object getItem(int position) {
        return userLink.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        View view;

        view = saveView.get(position);
        viewHolder = saveHolder.get(position);

        try {
            JSONObject jsonObject = new JSONObject(userLink.get(position));
            String company = jsonObject.get("link_from_code").toString();
            String account = jsonObject.get("link_from_user").toString();

            LinearLayout linearLayout1 = view.findViewById(R.id.linearLayout1);
            LinearLayout linearLayout3 = view.findViewById(R.id.linearLayout3);
            TextView textView1 = view.findViewById(R.id.textView1);
            Button button = view.findViewById(R.id.button1);
            TextView textView3 = view.findViewById(R.id.textView3);

            if(position == userLink.size() - 1){
                linearLayout1.setBackgroundResource(R.drawable.linksettingstyle_left);
                linearLayout3.setBackgroundResource(R.drawable.liststyle_right);
            }
            if(nowcompany.matches(company) && nowaccount.matches(account)){
                button.setBackgroundResource(R.drawable.accountlinkbutton_mine);
            }else {
                button.setBackgroundResource(R.drawable.accountlinkbutton_other);
            }
            textView1.setText(company);
            button.setText(account);
            if(Value.language_flag == 0){  //flag = 0 => Eng, flag = 1 => Cht, flag = 2 => Chs
                textView3.setText("Cancel");
            }else if(Value.language_flag == 1){
                textView3.setText("取消");
            }else if(Value.language_flag == 2){
                textView3.setText("取消");
            }
            textView3.setTextColor(Color.RED);
            viewHolder.textView = textView3;
            viewHolder.button = button;
            viewHolder.textView.setOnClickListener(view1 -> getInnerItem.clickItem(view1,
                    company, account));
            viewHolder.button.setOnClickListener(view1 -> getInnerItem.clickItem(view1,
                    company, account));
            viewHolder.textView.setTag(position);
            viewHolder.button.setTag(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
