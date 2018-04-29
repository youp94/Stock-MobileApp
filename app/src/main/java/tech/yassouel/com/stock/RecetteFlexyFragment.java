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
import java.util.SimpleTimeZone;

/**
 * Created by youpes on 31/03/18.
 */

public class RecetteFlexyFragment extends Fragment {

    String id_user = null;

    private DatabaseReference mDatabase;

    ArrayList<RecetteFlexy> listRecetteFlexy = new ArrayList<>();
    RecetteFlexyAdapter rfa;
    ListView list;

    public RecetteFlexyFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recette_flexy, container, false);

        if(getArguments() != null){
            id_user = getArguments().getString("id");
        }

        list = rootView.findViewById(R.id.list_recette_flexy);
        rfa = new RecetteFlexyAdapter(getContext(), listRecetteFlexy);
        list.setAdapter(rfa);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(id_user).child("HistoriqueFlexy").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int r;
                listRecetteFlexy.clear();
                Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
                while(i.hasNext()){
                    r=0;
                    DataSnapshot data = i.next();
                    String date = data.getKey();
                    Iterator<DataSnapshot> i2 = data.getChildren().iterator();
                    while (i2.hasNext()) {
                        HistFlexy p = i2.next().getValue(HistFlexy.class);
                        r = r + (p.getFlexy().getMontant());
                    }
                    RecetteFlexy re = new RecetteFlexy(date, r);
                    listRecetteFlexy.add(re);
                }
                rfa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    public class RecetteFlexyAdapter extends BaseAdapter{

        ArrayList<RecetteFlexy> recetteFlexyList;
        Context context;

        public RecetteFlexyAdapter(Context context, ArrayList<RecetteFlexy> recetteFlexyList){
            this.recetteFlexyList = recetteFlexyList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return recetteFlexyList.size();
        }

        @Override
        public Object getItem(int i) {
            return recetteFlexyList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(String.valueOf(i));
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            RecetteFlexy recetteFlexy = recetteFlexyList.get(i);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_recette_flexy, null);

            TextView nom = view.findViewById(R.id.recette_totale_flexy);
            TextView date = view.findViewById(R.id.date_recette_flexy);

            nom.setText(String.valueOf(recetteFlexy.getRecette())+" DA");
            date.setText(recetteFlexy.getDate());

            return view;
        }
    }
}
