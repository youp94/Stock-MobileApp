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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

/**
 * Created by youpes on 31/03/18.
 */

public class NouveauFlexyFragment extends Fragment {

    String id_user = null;

    private DatabaseReference mDatabase;

    ListView list;
    FlexyAdapter fa;
    ArrayList<Flexy> listFlexy = new ArrayList<>();

    Button ajouter;

    int mon;
    int ent;

    public NouveauFlexyFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_nouveau_flexy, container, false);

        if(getArguments() != null){
            id_user = getArguments().getString("id");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        list = rootView.findViewById(R.id.list_flexy);
        fa = new FlexyAdapter(getContext(), listFlexy);
        list.setAdapter(fa);

        ajouter = rootView.findViewById(R.id.ajouter_flexy);
        ajouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                b.setTitle("Nouveau Flexy");

                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.dialog_nouveau_flexy, null);
                b.setView(dialoglayout);

                final EditText input = dialoglayout.findViewById(R.id.dialog_nom_flexy);
                final EditText input2 = dialoglayout.findViewById(R.id.dialog_montant_flexy);
                final EditText input3 = dialoglayout.findViewById(R.id.dialog_id_flexy);

                b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(!input.getText().toString().equals("") && !input2.getText().toString().equals("") && !input3.getText().toString().equals("")) {
                            Flexy flexy = new Flexy(Integer.parseInt(input3.getText().toString()), input.getText().toString(), Integer.parseInt(input2.getText().toString()));
                            mDatabase.child(id_user).child("Flexy").child(String.valueOf(flexy.getID())).setValue(flexy);
                        }else{
                            Toast.makeText(getContext(), "Entrez une valeur valide",Toast.LENGTH_LONG ).show();
                        }
                    }
                });
                b.setNegativeButton("CANCEL", null);
                b.show();
            }
        });

        mDatabase.child(id_user).child("Flexy").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listFlexy.clear();
                Iterator<DataSnapshot> i = dataSnapshot.getChildren().iterator();
                while(i.hasNext()){
                    Flexy p = i.next().getValue(Flexy.class);
                    listFlexy.add(p);
                }
                fa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    class FlexyAdapter extends BaseAdapter{

        private ArrayList<Flexy> listFlexy;
        private Context context;

        public FlexyAdapter(Context context, ArrayList<Flexy> listFlexy){
            this.context = context;
            this.listFlexy = listFlexy;
        }

        @Override
        public int getCount() {
            return listFlexy.size();
        }

        @Override
        public Object getItem(int i) {
            return listFlexy.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(String.valueOf(i));
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final Flexy flexy = listFlexy.get(i);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_nouveau_flexy,null);

            TextView nom = view.findViewById(R.id.nom_flexy);
            TextView montant = view.findViewById(R.id.montant_flexy);
            ImageView image = view.findViewById(R.id.edit_flexy);

            nom.setText(flexy.getNom());
            montant.setText(String.valueOf(flexy.getMontant()));

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                    b.setTitle("Nouveau Flexy");

                    LayoutInflater inflater = getLayoutInflater();
                    View dialoglayout = inflater.inflate(R.layout.dialog_flexy, null);
                    b.setView(dialoglayout);

                    final EditText input = dialoglayout.findViewById(R.id.dialog_montant);

                    b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if(!input.getText().toString().equals("")) {
                                Flexy flexy2 = new Flexy(flexy.getID(), flexy.getNom(), Integer.parseInt(input.getText().toString()));
                                mDatabase.child(id_user).child("Flexy").child(String.valueOf(flexy.getID())).setValue(flexy2);
                            }else{
                                Toast.makeText(getContext(), "Entrez une valeur valide",Toast.LENGTH_LONG ).show();
                            }
                        }
                    });
                    b.setNegativeButton("CANCEL", null);
                    b.show();
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //final String  currentDateString = DateFormat.getDateInstance().format(new Date());

                    final String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    final String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                    AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                    b.setTitle("Flexy");

                    LayoutInflater inflater = getLayoutInflater();
                    View dialoglayout = inflater.inflate(R.layout.dialog_flexy, null);
                    b.setView(dialoglayout);

                    final EditText input = dialoglayout.findViewById(R.id.dialog_montant);
                    final EditText input2 = dialoglayout.findViewById(R.id.dialog_entrant);

                    b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if(!input.getText().toString().equals("") /*&& !input2.getText().toString().equals("")*/) {
                                mon = Integer.parseInt(input.getText().toString());
                                //ent = Integer.parseInt(input2.getText().toString());
                                if(mon<=flexy.getMontant()) {
                                    Random r = new Random();
                                    int i1 = r.nextInt(1000);

                                    Flexy fVendu = new Flexy(flexy.getID(), flexy.getNom(), mon);
                                    HistFlexy histFlexy = new HistFlexy(datetime, fVendu);
                                    mDatabase.child(id_user).child("HistoriqueFlexy").child(date).child(String.valueOf(fVendu.getID() + i1)).setValue(histFlexy);

                                    Toast.makeText(getContext(), "Flexy réussi", Toast.LENGTH_LONG).show();

                                    flexy.reduireMontant(mon);
                                    mDatabase.child(id_user).child("Flexy").child(String.valueOf(flexy.getID())).setValue(flexy);
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
