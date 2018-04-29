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

public class RecetteIdoomFragment extends Fragment {

    String id_user = null;

    private DatabaseReference mDatabase;

    ArrayList<RecetteIdoom> listRecetteIdoom = new ArrayList<>();
    RecetteIdoomAdapter rfa;
    ListView list;

    public RecetteIdoomFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recette_idoom, container, false);

        if(getArguments() != null){
            id_user = getArguments().getString("id");
        }

        list = rootView.findViewById(R.id.list_recette_idoom);
        rfa = new RecetteIdoomAdapter(getContext(), listRecetteIdoom);
        list.setAdapter(rfa);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child(id_user).child("HistoriqueIdoom").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int r;
                listRecetteIdoom.clear();
                Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
                while(i.hasNext()){
                    r=0;
                    DataSnapshot data = i.next();
                    String date = data.getKey();
                    Iterator<DataSnapshot> i2 = data.getChildren().iterator();
                    while (i2.hasNext()) {
                        HistIdoom p = i2.next().getValue(HistIdoom.class);
                        r = r + (p.getIdoom().getPrix() * p.getIdoom().getQuantite());
                    }
                    RecetteIdoom re = new RecetteIdoom(date, r);
                    listRecetteIdoom.add(re);
                }
                rfa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    class RecetteIdoomAdapter extends BaseAdapter {

        ArrayList<RecetteIdoom> listRecetteIdoom;
        Context context;

        public RecetteIdoomAdapter(Context context, ArrayList<RecetteIdoom> listRecetteIdoom){
            this.context = context;
            this.listRecetteIdoom = listRecetteIdoom;
        }

        @Override
        public int getCount() {
            return listRecetteIdoom.size();
        }

        @Override
        public Object getItem(int i) {
            return listRecetteIdoom.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(String.valueOf(i));
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            RecetteIdoom recetteIdoom = listRecetteIdoom.get(i);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_recette_idoom, null);

            TextView nom = view.findViewById(R.id.recette_totale_idoom);
            TextView date = view.findViewById(R.id.date_recette_idoom);

            nom.setText(String.valueOf(recetteIdoom.getRecette())+" DA");
            date.setText(recetteIdoom.getDate());

            return view;
        }
    }
}
