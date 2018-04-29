package tech.yassouel.com.stock;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by youpes on 31/03/18.
 */

public class HistFlexyFragment extends Fragment {

    String id_user = null;

    private DatabaseReference mDatabase;

    private ListView list;
    private HistFlexyAdapter hfa;
    private ArrayList<HistFlexy> listHistFlexy = new ArrayList<>();


    public HistFlexyFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_hist_flexy, container, false);

        if(getArguments() != null){
            id_user = getArguments().getString("id");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        list = rootView.findViewById(R.id.list_hist_flexy);
        hfa = new HistFlexyAdapter(getContext(), listHistFlexy);
        list.setAdapter(hfa);

        mDatabase.child(id_user).child("HistoriqueFlexy").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listHistFlexy.clear();
                Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
                while(i.hasNext()){
                    DataSnapshot data = i.next();
                    String date = data.getKey();
                    Iterator<DataSnapshot> i2 = data.getChildren().iterator();
                    while (i2.hasNext()) {
                        HistFlexy p = i2.next().getValue(HistFlexy.class);
                        listHistFlexy.add(p);
                    }
                }
                hfa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    class HistFlexyAdapter extends BaseAdapter{

        ArrayList<HistFlexy> list;
        Context context;

        public HistFlexyAdapter(Context context, ArrayList<HistFlexy> list){
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(String.valueOf(i));
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            HistFlexy histFlexy = list.get(i);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_hist_flexy, null);

            TextView date = view.findViewById(R.id.date3);
            TextView nom = view.findViewById(R.id.flexy_nom);
            TextView recette = view.findViewById(R.id.recette_flexy);

            date.setText(histFlexy.getDate());
            nom.setText(histFlexy.getFlexy().getNom());
            recette.setText(String.valueOf(histFlexy.getFlexy().getMontant()));

            return view;
        }
    }
}