package tech.yassouel.com.stock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

/**
 * Created by youpes on 30/03/18.
 */

public class CaisseFragment extends Fragment {

    String id_user = null;

    private DatabaseReference mDatabase;

    TextView textCaisse;
    Button boutonCaisse;

    public CaisseFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_caisse, container, false);

        if(getArguments() != null){
            id_user = getArguments().getString("id");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child(id_user).child("Caisse").child("CaisseStock").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CaisseStock caisseStock = dataSnapshot.getValue(CaisseStock.class);
                textCaisse.setText(String.valueOf(caisseStock.getMontant()) + " DA");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        textCaisse = rootView.findViewById(R.id.textView3);
        boutonCaisse = rootView.findViewById(R.id.button3);

        boutonCaisse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                b.setTitle("Caisse stock");

                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.dialog_caisse, null);
                b.setView(dialoglayout);

                final EditText input = dialoglayout.findViewById(R.id.dialog_montant_caisse);

                b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(!input.getText().toString().equals("")) {
                            CaisseStock caisseStock = new CaisseStock(Integer.parseInt(input.getText().toString()));
                            mDatabase.child(id_user).child("Caisse").child("CaisseStock").setValue(caisseStock);
                            textCaisse.setText(input.getText().toString() + " DA");
                        }else{
                            Toast.makeText(getContext(), "Entrez une valeur valide",Toast.LENGTH_LONG ).show();
                        }
                    }
                });
                b.setNegativeButton("CANCEL", null);
                b.show();
            }
        });

        return rootView;
    }
}
