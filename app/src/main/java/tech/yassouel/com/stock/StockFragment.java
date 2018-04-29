package tech.yassouel.com.stock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

/**
 * Created by youpes on 19/03/18.
 */

public class StockFragment extends Fragment {

    String id_user = null;

    private DatabaseReference mDatabase;

    ListView list;
    ProduitAdapter pa;
    ArrayList<Produit> listProduit = new ArrayList<>();

    int quantite;
    int reduc;

    public StockFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_stock, container, false);

        if(getArguments() != null){
            id_user = getArguments().getString("id");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        list = rootView.findViewById(R.id.list_produit);
        pa= new ProduitAdapter(getContext(),listProduit);
        list.setAdapter(pa);

        mDatabase.child(id_user).child("Produits").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listProduit.clear();
                Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
                while(i.hasNext()){
                    Produit p = i.next().getValue(Produit.class);
                    listProduit.add(p);
                }
                pa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    public class ProduitAdapter extends BaseAdapter {

        private ArrayList<Produit> listProduit;
        private Context context;

        ProduitAdapter(Context context, ArrayList<Produit> listProduit){
            this.context = context;
            this.listProduit = listProduit;
        }

        @Override
        public int getCount() {
            return listProduit.size();
        }

        @Override
        public Object getItem(int i) {
            return listProduit.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final Produit p = listProduit.get(i);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.item,null);
            TextView nom = view.findViewById(R.id.tvTitle);
            TextView prix = view.findViewById(R.id.tvDes);
            final TextView quan = view.findViewById(R.id.tvQuan);
            ImageView edit = view.findViewById(R.id.ivEdit);
            ImageView delete = view.findViewById(R.id.ivDelete);
            ImageView send = view.findViewById(R.id.ivVen);
            ImageView pro = view.findViewById(R.id.ivProduit);

            nom.setText(p.getNom());
            prix.setText(String.valueOf(p.getPrix_vente())+" DA");
            quan.setText(String.valueOf(p.getQuantite()));
            Picasso.with(getContext()).load(p.getImageURl()).noPlaceholder().centerCrop().fit().into(pro);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle b = new Bundle();
                    b.putInt("ID",p.getID());
                    b.putString("nom",p.getNom());
                    b.putInt("achat",p.getPrix_achat());
                    b.putInt("vente",p.getPrix_vente());
                    b.putInt("quantite",p.getQuantite());
                    b.putString("image",p.getImageURl());
                    b.putString("id",id_user);
                    AjoutFragment ajoutFragment = new AjoutFragment();
                    ajoutFragment.setArguments(b);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.content_frame, ajoutFragment).commit();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDatabase.child(id_user).child("Produits").child(String.valueOf(p.getID())).removeValue();
                }
            });

            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //final String  currentDateString = DateFormat.getDateInstance().format(new Date("dd-MMM-yyyy  hh:mm a"));
                    //final String  currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

                    final String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    final String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                    AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                    b.setTitle("Entrez la quantité de produit à vendre");

                    LayoutInflater inflater = getLayoutInflater();
                    View dialogLayout = inflater.inflate(R.layout.dialog, null);
                    b.setView(dialogLayout);
                    final EditText input = dialogLayout.findViewById(R.id.dialog_quan);
                    final EditText input2 = dialogLayout.findViewById(R.id.dialog_reduc);

                    b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if(!input.getText().toString().equals("")) {
                                quantite = Integer.parseInt(input.getText().toString());
                                if(!input2.getText().toString().equals("")){
                                    reduc = Integer.parseInt(input2.getText().toString());
                                }
                                if(quantite<=p.getQuantite()) {
                                    Random r = new Random();
                                    int i1 = r.nextInt(1000);

                                    Produit pVendu = new Produit(p.getID(), p.getNom(), p.getPrix_achat(),
                                            p.getPrix_vente()-reduc, quantite, p.getImageURl());
                                    Vendu vendu = new Vendu(datetime, pVendu);
                                    mDatabase.child(id_user).child("Vendus").child(date).child(String.valueOf(pVendu.getID() + i1)).setValue(vendu);

                                    Toast.makeText(getContext(), "Produit vendu", Toast.LENGTH_LONG).show();

                                    p.vendre(quantite);
                                    mDatabase.child(id_user).child("Produits").child(String.valueOf(p.getID())).setValue(p);
                                }else{
                                    Toast.makeText(getContext(), "Quantité insuffisante",Toast.LENGTH_LONG ).show();
                                }
                            }else{
                                Toast.makeText(getContext(), "Entrez une valeur valide",Toast.LENGTH_LONG ).show();
                            }
                        }
                    });
                    b.setNegativeButton("CANCEL", null);
                    b.show();
                }
            });

            return view;
        }
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }*/
}