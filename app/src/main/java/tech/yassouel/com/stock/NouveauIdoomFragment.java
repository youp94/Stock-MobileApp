package tech.yassouel.com.stock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

public class NouveauIdoomFragment extends Fragment {

    String id_user = null;

    private DatabaseReference mDatabase;

    ListView list;
    IdoomAdapter ia;
    ArrayList<Idoom> listIdoom = new ArrayList<>();

    Button ajouter;

    int mon;


    public NouveauIdoomFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_nouveau_idoom, container, false);

        if(getArguments() != null){
            id_user = getArguments().getString("id");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        list = rootView.findViewById(R.id.list_idoom);
        ia = new IdoomAdapter(getContext(), listIdoom);
        list.setAdapter(ia);

        ajouter = rootView.findViewById(R.id.ajouter_idoom);
        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                b.setTitle("Nouveau Idoom");

                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.dialog_nouveau_idoom, null);
                b.setView(dialoglayout);

                final EditText input = dialoglayout.findViewById(R.id.dialog_nom_idoom);
                final EditText input2 = dialoglayout.findViewById(R.id.dialog_prix_idoom);
                final EditText input3 = dialoglayout.findViewById(R.id.dialog_id_idoom);
                final EditText input4 = dialoglayout.findViewById(R.id.dialog_quantite_idoom);

                b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(!input.getText().toString().equals("") && !input2.getText().toString().equals("") && !input3.getText().toString().equals("") && !input4.getText().toString().equals("")) {
                            Idoom idoom = new Idoom(Integer.parseInt(input3.getText().toString()), input.getText().toString(), Integer.parseInt(input2.getText().toString()), Integer.parseInt(input4.getText().toString()));
                            mDatabase.child(id_user).child("Idoom").child(String.valueOf(idoom.getID())).setValue(idoom);
                        }else{
                            Toast.makeText(getContext(), "Entrez une valeur valide",Toast.LENGTH_LONG ).show();
                        }
                    }
                });
                b.setNegativeButton("CANCEL", null);
                b.show();
            }
        });

        mDatabase.child(id_user).child("Idoom").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listIdoom.clear();
                Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
                while(i.hasNext()){
                    Idoom p = i.next().getValue(Idoom.class);
                    listIdoom.add(p);
                }
                ia.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    class IdoomAdapter extends BaseAdapter{

        ArrayList<Idoom> listIdoom;
        Context context;

        public IdoomAdapter(Context context, ArrayList<Idoom> listIdoom){
            this.context = context;
            this.listIdoom = listIdoom;
        }

        @Override
        public int getCount() {
            return listIdoom.size();
        }

        @Override
        public Object getItem(int i) {
            return listIdoom.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(String.valueOf(i));
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final Idoom idoom = listIdoom.get(i);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_idoom, null);

            TextView nom = view.findViewById(R.id.nom_idoom);
            TextView prix = view.findViewById(R.id.prix_idoom);
            TextView quantite = view.findViewById(R.id.quantite_idoom);
            ImageView image = view.findViewById(R.id.edit_flexy);

            nom.setText(idoom.getNom());
            prix.setText(String.valueOf(idoom.getPrix()));
            quantite.setText(String.valueOf(idoom.getQuantite()));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //final String  currentDateString = DateFormat.getDateInstance().format(new Date());

                    final String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    final String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                    AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                    b.setTitle("Idoom");

                    LayoutInflater inflater = getLayoutInflater();
                    View dialoglayout = inflater.inflate(R.layout.dialog_idoom, null);
                    b.setView(dialoglayout);

                    final EditText input = dialoglayout.findViewById(R.id.dialog_quantite);

                    b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if(!input.getText().toString().equals("")) {
                                mon = Integer.parseInt(input.getText().toString());
                                if(mon<=idoom.getQuantite()) {
                                    Random r = new Random();
                                    int i1 = r.nextInt(1000);

                                    Idoom fVendu = new Idoom(idoom.getID(), idoom.getNom(), idoom.getPrix(), mon);
                                    HistIdoom histFlexy = new HistIdoom(datetime, fVendu);
                                    mDatabase.child(id_user).child("HistoriqueIdoom").child(date).child(String.valueOf(fVendu.getID() + i1)).setValue(histFlexy);

                                    Toast.makeText(getContext(), "Vente carte idoom réussie", Toast.LENGTH_LONG).show();

                                    idoom.reduire(mon);
                                    mDatabase.child(id_user).child("Idoom").child(String.valueOf(idoom.getID())).setValue(idoom);
                                }else{
                                    Toast.makeText(getContext(), "Montant insuffisant",Toast.LENGTH_LONG ).show();
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
}
