package tech.yassouel.com.stock;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by youpes on 20/03/18.
 */

public class AjoutFragment extends Fragment {

    String id_user = null;

    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    EditText ID;
    EditText nom;
    EditText prix_achat;
    EditText prix_vente;
    EditText quantite;
    Button valider;
    ImageView image;

    Uri imageURI;
    String imageUrl;
    public static final int REQUEST_CODE = 22200;
    public static final int IMAGE_CODE = 22255;

    public AjoutFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ajout, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        ID = rootView.findViewById(R.id.editText5);
        nom = rootView.findViewById(R.id.editText);
        prix_achat = rootView.findViewById(R.id.editText2);
        prix_vente = rootView.findViewById(R.id.editText3);
        quantite = rootView.findViewById(R.id.editText4);
        valider = rootView.findViewById(R.id.button);
        image = rootView.findViewById(R.id.imageView);

        if(getArguments() != null){
            if(getArguments().get("ID") != null) {
                ID.setText(String.valueOf(getArguments().getInt("ID")));
                nom.setText(getArguments().getString("nom"));
                prix_achat.setText(String.valueOf(getArguments().getInt("achat")));
                prix_vente.setText(String.valueOf(getArguments().getInt("vente")));
                quantite.setText(String.valueOf(getArguments().getInt("quantite")));
                Picasso.with(getContext()).load(getArguments().getString("image")).noPlaceholder().centerCrop().fit().into(image);
            }
            id_user = getArguments().getString("id");
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String path = id_user + "/images/" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date()) + ".jpg";
                StorageReference ref = mStorage.child(path);

                image.setDrawingCacheEnabled(true);
                image.buildDrawingCache();
                BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                byte[] bytes = baos.toByteArray();

                //UploadTask uploadTask = ref.putFile(imageURI);
                UploadTask uploadTask = ref.putBytes(bytes);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageUrl = taskSnapshot.getDownloadUrl().toString();
                        Produit p = new Produit(Integer.parseInt(ID.getText().toString()), nom.getText().toString(), Integer.parseInt(prix_achat.getText().toString()),
                                Integer.parseInt(prix_vente.getText().toString()),
                                Integer.parseInt(quantite.getText().toString()), imageUrl);

                        mDatabase.child(id_user).child("Produits").child(String.valueOf(p.getID())).setValue(p);
                        Toast.makeText(getContext(), "Produit ajouté avec succés", Toast.LENGTH_LONG).show();

                        ID.setText("");
                        nom.setText("");
                        prix_achat.setText("");
                        prix_vente.setText("");
                        quantite.setText("");
                    }
                });
            }
        });

        return rootView;
    }

    void checkPermission(){
        if(Build.VERSION.SDK_INT >= 23){
            if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                String[] request = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(request, REQUEST_CODE);
                return;
            }
        }
        loadImage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    loadImage();
                }else {
                    Toast.makeText(getContext(), "Permission non accordé", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void loadImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_CODE && data != null && resultCode == RESULT_OK){
            imageURI = data.getData();
            /*String[] path = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(uri,path,null,null,null);
            cursor.moveToFirst();
            int i = cursor.getColumnIndex(path[0]);
            imageUrl = cursor.getString(i);
            cursor.close();*/
            //image.setImageBitmap(BitmapFactory.decodeFile(imageUrl));
            Picasso.with(getContext()).load(data.getData()).noPlaceholder().centerCrop().fit().into(image);
        }
    }

}
