package tech.yassouel.com.stock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanFragment extends Fragment {

    String id_user = null;

    private DatabaseReference mDatabase;

    Button bouton;
    TextView text_content;
    TextView text_format;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_scan, container, false);

        if(getArguments() != null){
            id_user = getArguments().getString("id");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        bouton = rootView.findViewById(R.id.button4);
        text_content = rootView.findViewById(R.id.scan_content);
        text_content.setText("Content");
        text_format = rootView.findViewById(R.id.scan_format);
        text_format.setText("Format");

        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt("Scan a barcode");
                integrator.setResultDisplayDuration(0);
                integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.initiateScan();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (scanningResult != null) {
            //we have a result
            Toast.makeText(getActivity().getApplicationContext(),"scan data received!", Toast.LENGTH_SHORT);
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            Log.d("tag", scanContent+ " "+ scanFormat);

            // display it on screen
            text_format.setText("FORMAT: " + scanFormat);
            text_content.setText("CONTENT: " + scanContent);

        }else{
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),"No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
