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

public class HistIdoomFragment extends Fragment {

    String id_user = null;

    private DatabaseReference mDatabase;

    private ListView list;
    private HistIdoomAdapter hia;
    private ArrayList<HistIdoom> listHistIdoom = new ArrayList<>();

    public HistIdoomFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_hist_idoom, container, false);

        if(getArguments() != null){
            id_user = getArguments().getString("id");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        list = rootView.findViewById(R.id.list_hist_idoom);
        hia = new HistIdoomAdapter(getContext(), listHistIdoom);
        list.setAdapter(hia);

        mDatabase.child(id_user).child("HistoriqueIdoom").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listHistIdoom.clear();
                Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
                while(i.hasNext()){
                    DataSnapshot data = i.next();
                    String date = data.getKey();
                    Iterator<DataSnapshot> i2 = data.getChildren().iterator();
                    while (i2.hasNext()) {
                        HistIdoom p = i2.next().getValue(HistIdoom.class);
                        listHistIdoom.add(p);
                    }
                }
                hia.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    class HistIdoomAdapter extends BaseAdapter{

        ArrayList<HistIdoom> listHistIdoom;
        Context context;

        public HistIdoomAdapter(Context context, ArrayList<HistIdoom> listHistIdoom){
            this.context = context;
            this.listHistIdoom = listHistIdoom;
        }

        @Override
        public int getCount() {
            return listHistIdoom.size();
        }

        @Override
        public Object getItem(int i) {
            return listHistIdoom.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(String.valueOf(i));
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            HistIdoom histIdoom = listHistIdoom.get(i);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_hist_idoom, null);

            TextView date = view.findViewById(R.id.date_idoom);
            TextView nom = view.findViewById(R.id.idoom_nom);
            TextView recette = view.findViewById(R.id.recette_idoom);
            TextView quantite = view.findViewById(R.id.achat_idoom);

            date.setText(histIdoom.getDate());
            nom.setText(histIdoom.getIdoom().getNom());
            recette.setText(String.valueOf(histIdoom.getIdoom().getPrix()));
            quantite.setText(String.valueOf(histIdoom.getIdoom().getQuantite()));


            return view;
        }
    }

}