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
 * Created by youpes on 30/03/18.
 */

public class JourFragment extends Fragment {

    String id_user = null;

    private DatabaseReference mDatabase;

    ListView list;
    RecetteAdapter ra;
    ArrayList<Recette> listRecettes = new ArrayList<>();

    public JourFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_jour, container, false);

        if(getArguments() != null){
            id_user = getArguments().getString("id");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        list = rootView.findViewById(R.id.list_produit3);
        ra = new RecetteAdapter(getContext(), listRecettes);
        list.setAdapter(ra);

        mDatabase.child(id_user).child("Vendus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int r;
                listRecettes.clear();
                Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
                while(i.hasNext()){
                    r=0;
                    DataSnapshot data = i.next();
                    String date = data.getKey();
                    Iterator<DataSnapshot> i2 = data.getChildren().iterator();
                    while (i2.hasNext()) {
                        Vendu p = i2.next().getValue(Vendu.class);
                        r = r + ((p.getProduit().getPrix_vente() - p.getProduit().getPrix_achat()) * p.getProduit().getQuantite());
                    }
                    Recette re = new Recette(date, r);
                    listRecettes.add(re);
                }
                ra.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    class RecetteAdapter extends BaseAdapter{

        ArrayList<Recette> listRecettes;
        Context context;

        public RecetteAdapter(Context context, ArrayList<Recette> listRecettes){
            this.listRecettes = listRecettes;
            this.context = context;
        }

        @Override
        public int getCount() {
            return listRecettes.size();
        }

        @Override
        public Object getItem(int i) {
            return listRecettes.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(String.valueOf(i));
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Recette recette = listRecettes.get(i);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.item_jour,null);
            TextView nom = view.findViewById(R.id.recette);
            TextView date = view.findViewById(R.id.date2);

            nom.setText(String.valueOf(recette.getRecette())+" DA");
            date.setText(recette.getDate());
            return view;
        }
    }
}
