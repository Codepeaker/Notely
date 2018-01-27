package in.codepeaker.notely.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import in.codepeaker.notely.R;

/**
 * Created by github.com/codepeaker on 25/1/18.
 */

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder> {
    public static HashMap<Integer, Boolean> hashMap = new HashMap<>();
    String[] filterNames;
    Context context;

    public FilterAdapter(Context context, String[] filterNames) {
        this.filterNames = filterNames;
        this.context = context;

        for (int i = 0; i < filterNames.length; i++)
            hashMap.put(i, false);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_list_view, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        holder.filterText.setText(filterNames[position]);
    }

    @Override
    public int getItemCount() {
        return filterNames.length;
    }

    public interface InteractionListener {
        void getDrawerListMap(HashMap<Integer, Boolean> hashMap);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView filterText;

        public MyViewHolder(View itemView) {
            super(itemView);
            filterText = itemView.findViewById(R.id.filter_text);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.filter_layout_id) {
                if (!hashMap.get(getAdapterPosition())) {
                    hashMap.put(getAdapterPosition(), true);
                } else {
                    hashMap.put(getAdapterPosition(), false);
                }
                ((InteractionListener) context).getDrawerListMap(hashMap);
            }
        }
    }
}
