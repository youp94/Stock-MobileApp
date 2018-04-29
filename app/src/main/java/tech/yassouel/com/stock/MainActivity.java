package tech.yassouel.com.stock;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout dl;
    NavigationView nv;

    String user = null;
    String email = null;
    String picture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            user = bundle.getString("user");
            email = bundle.getString("email");
            picture = bundle.getString("picture");
        }

        final android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        dl = findViewById(R.id.drawer_layout);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        nv = findViewById(R.id.nav_view);
        View header = nv.getHeaderView(0);
        TextView t = header.findViewById(R.id.header_title);
        t.setText(user);
        CircleImageView profile = header.findViewById(R.id.profile_image);
        Picasso.with(this).load(picture).noPlaceholder().centerCrop().fit().into(profile);
        final Bundle b = new Bundle();
        b.putString("id",email.split("@")[0].replace('.','_'));
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                dl.closeDrawers();
                switch (item.getItemId()){
                    case R.id.home:
                        HomeFragment homeFragment = new HomeFragment();
                        homeFragment.setArguments(b);
                        Toast.makeText(getApplicationContext(),"Accueil",Toast.LENGTH_LONG).show();
                        fm.beginTransaction().replace(R.id.content_frame, homeFragment).commit();
                        break;
                    case R.id.caisse:
                        CaisseFragment caisseFragment = new CaisseFragment();
                        caisseFragment.setArguments(b);
                        Toast.makeText(getApplicationContext(),"La ciasse",Toast.LENGTH_LONG).show();
                        fm.beginTransaction().replace(R.id.content_frame, caisseFragment).commit();
                        break;
                    case R.id.caisse_flexy:
                        CaisseFlexyFragment caisseFlexyFragment = new CaisseFlexyFragment();
                        caisseFlexyFragment.setArguments(b);
                        Toast.makeText(getApplicationContext(),"La ciasse de flexy",Toast.LENGTH_LONG).show();
                        fm.beginTransaction().replace(R.id.content_frame, caisseFlexyFragment).commit();
                        break;
                    case R.id.jour:
                        JourFragment jourFragment = new JourFragment();
                        jourFragment.setArguments(b);
                        Toast.makeText(getApplicationContext(),"La journée",Toast.LENGTH_LONG).show();
                        fm.beginTransaction().replace(R.id.content_frame, jourFragment).commit();
                        break;
                    case R.id.stock:
                        StockFragment stockFragment = new StockFragment();
                        stockFragment.setArguments(b);
                        Toast.makeText(getApplicationContext(),"Le stock",Toast.LENGTH_LONG).show();
                        fm.beginTransaction().replace(R.id.content_frame, stockFragment).commit();
                        break;
                    case R.id.scan:
                        ScanFragment scanFragment = new ScanFragment();
                        scanFragment.setArguments(b);
                        Toast.makeText(getApplicationContext(),"Le scan",Toast.LENGTH_LONG).show();
                        fm.beginTransaction().replace(R.id.content_frame, scanFragment).commit();
                        break;
                    case R.id.his:
                        HistoriqueFragment historiqueFragment = new HistoriqueFragment();
                        historiqueFragment.setArguments(b);
                        Toast.makeText(getApplicationContext(),"L'historique",Toast.LENGTH_LONG).show();
                        fm.beginTransaction().replace(R.id.content_frame, historiqueFragment).commit();
                        break;
                    case R.id.ajout:
                        AjoutFragment ajoutFragment = new AjoutFragment();
                        ajoutFragment.setArguments(b);
                        Toast.makeText(getApplicationContext(),"L'ajout",Toast.LENGTH_LONG).show();
                        fm.beginTransaction().replace(R.id.content_frame, ajoutFragment).commit();
                        break;
                    case R.id.nouv_flexy:
                        NouveauFlexyFragment nouveauFlexyFragment = new NouveauFlexyFragment();
                        nouveauFlexyFragment.setArguments(b);
                        Toast.makeText(getApplicationContext(),"Nouveau Flexy",Toast.LENGTH_LONG).show();
                        fm.beginTransaction().replace(R.id.content_frame, nouveauFlexyFragment).commit();
                        break;
                    case R.id.recette_flexy:
                        RecetteFlexyFragment recetteFlexyFragment = new RecetteFlexyFragment();
                        recetteFlexyFragment.setArguments(b);
                        Toast.makeText(getApplicationContext(),"Recette de flexy",Toast.LENGTH_LONG).show();
                        fm.beginTransaction().replace(R.id.content_frame, recetteFlexyFragment).commit();
                        break;
                    case R.id.his_flexy:
                        HistFlexyFragment histFlexyFragment = new HistFlexyFragment();
                        histFlexyFragment.setArguments(b);
                        Toast.makeText(getApplicationContext(),"Historique de flexy",Toast.LENGTH_LONG).show();
                        fm.beginTransaction().replace(R.id.content_frame, histFlexyFragment).commit();
                        break;
                    case R.id.nouv_cartes:
                        NouveauIdoomFragment nouveauIdoomFragment = new NouveauIdoomFragment();
                        nouveauIdoomFragment.setArguments(b);
                        Toast.makeText(getApplicationContext(),"Nouveau cartes",Toast.LENGTH_LONG).show();
                        fm.beginTransaction().replace(R.id.content_frame, nouveauIdoomFragment).commit();
                        break;
                    case R.id.recette_cartes:
                        RecetteIdoomFragment recetteIdoomFragment = new RecetteIdoomFragment();
                        recetteIdoomFragment.setArguments(b);
                        Toast.makeText(getApplicationContext(),"Recette de cates",Toast.LENGTH_LONG).show();
                        fm.beginTransaction().replace(R.id.content_frame, recetteIdoomFragment).commit();
                        break;
                    case R.id.his_carte:
                        HistIdoomFragment histIdoomFragment = new HistIdoomFragment();
                        histIdoomFragment.setArguments(b);
                        Toast.makeText(getApplicationContext(),"Historique de cartes",Toast.LENGTH_LONG).show();
                        fm.beginTransaction().replace(R.id.content_frame, histIdoomFragment).commit();
                        break;
                }

                return false;
            }
        });

        dl.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Respond when the drawer's position changes
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Respond when the drawer is opened
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // Respond when the drawer is closed
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Respond when the drawer motion state changes
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                dl.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
