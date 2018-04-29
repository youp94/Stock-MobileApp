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

import java.util.Random;

public class CaisseFlexyFragment extends Fragment {

    String id_user = null;

    private DatabaseReference mDatabase;

    TextView textCaisse;
    Button boutonCaisse;

    public CaisseFlexyFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_caisse_flexy, container, false);

        if(getArguments() != null){
            id_user = getArguments().getString("id");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child(id_user).child("Caisse").child("CaisseFlexy").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CaisseFlexy caisseFlexy = dataSnapshot.getValue(CaisseFlexy.class);
                textCaisse.setText(String.valueOf(caisseFlexy.getMontant()) + " DA");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        textCaisse = rootView.findViewById(R.id.textView2);
        boutonCaisse = rootView.findViewById(R.id.button2);

        boutonCaisse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                b.setTitle("Caisse Flexy");

                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.dialog_caisse, null);
                b.setView(dialoglayout);

                final EditText input = dialoglayout.findViewById(R.id.dialog_montant_caisse);

                b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(!input.getText().toString().equals("")) {
                            CaisseFlexy caisseFlexy = new CaisseFlexy(Integer.parseInt(input.getText().toString()));
                            mDatabase.child(id_user).child("Caisse").child("CaisseFlexy").setValue(caisseFlexy);
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
