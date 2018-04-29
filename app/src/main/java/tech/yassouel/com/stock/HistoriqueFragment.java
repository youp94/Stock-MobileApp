package tech.yassouel.com.stock;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by youpes on 21/03/18.
 */

public class HistoriqueFragment extends Fragment {

    String id_user = null;

    private DatabaseReference mDatabase;

    ListView list;
    VenduAdapter va;
    ArrayList<Vendu> listProduitVendus = new ArrayList<>();

    public HistoriqueFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_historique, container, false);

        if(getArguments() != null){
            id_user = getArguments().getString("id");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        list = rootView.findViewById(R.id.list_produit2);
        va = new VenduAdapter(getContext(), listProduitVendus);
        list.setAdapter(va);

        mDatabase.child(id_user).child("Vendus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listProduitVendus.clear();
                Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
                while(i.hasNext()){
                    DataSnapshot data = i.next();
                    String date = data.getKey();
                    Iterator<DataSnapshot> i2 = data.getChildren().iterator();
                    while (i2.hasNext()) {
                        Vendu p = i2.next().getValue(Vendu.class);
                        listProduitVendus.add(p);
                    }
                }
                va.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    class VenduAdapter extends BaseAdapter{

        ArrayList<Vendu> listProduitVendus;
        Context context;

        public VenduAdapter(Context context, ArrayList<Vendu> listProduitVendus){
            this.listProduitVendus = listProduitVendus;
            this.context = context;
        }

        @Override
        public int getCount() {
            return listProduitVendus.size();
        }

        @Override
        public Object getItem(int i) {
            return listProduitVendus.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(String.valueOf(i));
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Vendu vendu = listProduitVendus.get(i);
            Produit p = vendu.getProduit();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.item_vendu,null);
            TextView nom = view.findViewById(R.id.nom);
            TextView date = view.findViewById(R.id.date);
            TextView pri = view.findViewById(R.id.pri);
            TextView quan = view.findViewById(R.id.quan);
            ImageView image = view.findViewById(R.id.ivProduit2);

            nom.setText(p.getNom());
            date.setText(vendu.getDate());
            pri.setText(p.getPrix_vente()+ " DA");
            quan.setText(String.valueOf(p.getQuantite()));
            Picasso.with(getContext()).load(p.getImageURl()).noPlaceholder().centerCrop().fit().into(image);

            return view;
        }
    }
}
