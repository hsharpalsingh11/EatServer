package com.example.admin.eatserver;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.admin.eatserver.Common.Common;
import com.example.admin.eatserver.Model.Banner;
import com.example.admin.eatserver.Model.Food;
import com.example.admin.eatserver.ViewHolder.BannerViewHolder;
import com.example.admin.eatserver.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.admin.eatserver.Common.Common.PICK_IMAGE_REQUEST;

public class BannerActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton fab;
    RelativeLayout rootLayout;


    FirebaseDatabase db;
    DatabaseReference banners;
    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseRecyclerAdapter<Banner,BannerViewHolder> adapter;


    MaterialEditText edtName,edtFoodId;
    Button btnUpload,btnSelect;
    Banner newBanner;
    Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_banner );

        //Init Database
        db = FirebaseDatabase.getInstance();
        banners = db.getReference("Banner");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //re
        recyclerView = (RecyclerView)findViewById( R.id.recyclerBanner );
        recyclerView.setHasFixedSize( true );
        layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );

        fab = (FloatingActionButton)findViewById( R.id.fButton );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddBanner();
            }
        } );

        loadListBanner();
    }

    private void loadListBanner() {



        adapter = new FirebaseRecyclerAdapter<Banner, BannerViewHolder>(
                Banner.class,
                R.layout.banner_layout,
                BannerViewHolder.class,
                banners

                ) {


            @NonNull
            @Override
            public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from( parent.getContext() ).inflate(R.layout.banner_layout,parent,false);
                return new BannerViewHolder( itemView );
            }

            @Override
            protected void populateViewHolder(BannerViewHolder viewHolder, Banner model, int position) {
                viewHolder.txtbannerName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.bannerimageView);
            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void showAddBanner() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BannerActivity.this);
        alertDialog.setTitle("Add new Banner");
        alertDialog.setMessage("Please fill all information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_banner = inflater.inflate(R.layout.add_new_banner,null);

        edtFoodId = add_banner.findViewById(R.id.edtFoodId);
        edtName = add_banner.findViewById( R.id.edtFoodName);
        btnSelect = add_banner.findViewById( R.id.btnSelect );
        btnUpload = add_banner.findViewById( R.id.btnUpload );

        btnSelect.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        } );
        btnUpload.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        } );
        alertDialog.setView(add_banner);
        alertDialog.setIcon(R.drawable.ic_laptop_black_24dp);

        alertDialog.setPositiveButton( "CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            if(newBanner != null)
                banners.push().setValue(newBanner);
            loadListBanner();
            }
        } );

        alertDialog.setNegativeButton( "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                newBanner = null;
                loadListBanner();
            }
        } );
        alertDialog.show();
    }

    private void uploadImage() {
        if (filePath != null)
        {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading....");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFOlder = storageReference.child("images/"+imageName);
            imageFOlder.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.show();
                            Toast.makeText(BannerActivity.this, "Uploaded !", Toast.LENGTH_SHORT).show();
                            imageFOlder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set Value for newCategory if image upload and we can get download link
                                    newBanner = new Banner(  );
                                    newBanner.setName( edtName.getText().toString() );
                                    newBanner.setId( edtFoodId.getText().toString() );
                                    newBanner.setImage(uri.toString());
                                    mDialog.dismiss();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(BannerActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded "+progress+"%");


                        }
                    });
        }
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if(resultCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data!= null && data.getData() != null)
        {
            filePath = data.getData();
            btnSelect.setText("Image Selected");
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals( Common.UPDATE ))
        {
            showUpdateBannerDialog(adapter.getRef( item.getOrder()).getKey(),adapter.getItem( item.getOrder() ));
        }
        else if(item.getTitle().equals( Common.DELETE ))
        {
            deleteBanner(adapter.getRef( item.getOrder() ).getKey());
        }
        return super.onContextItemSelected( item );

    }

    private void deleteBanner(String key) {
        banners.child(key).removeValue();
    }

    private void showUpdateBannerDialog(final String key, final Banner item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BannerActivity.this);
        alertDialog.setTitle("Edit Banner");
        alertDialog.setMessage("Please fill all information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_banner_layout = inflater.inflate(R.layout.add_new_banner,null);

        edtName = add_banner_layout.findViewById( R.id.edtFoodName );

        edtFoodId = add_banner_layout.findViewById( R.id.edtFoodId );
        btnSelect = add_banner_layout.findViewById(R.id.btnSelect);
        btnUpload = add_banner_layout.findViewById(R.id.btnUpload);

        edtName.setText( item.getName() );
        edtFoodId.setText( item.getId() );
        //button events
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);
            }
        });

        alertDialog.setView(add_banner_layout);
        alertDialog.setIcon(R.drawable.ic_laptop_black_24dp);

        //set button
        alertDialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();

                item.setName(edtName.getText().toString());
                item.setId( edtFoodId.getText().toString() );

                Map<String,Object> update = new HashMap<>();
                update.put("id",item.getId());
                update.put("name",item.getName());
                update.put("image",item.getImage());

                banners.child(key)
                        .updateChildren(update)
                        .addOnCompleteListener( new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Snackbar.make( rootLayout,"Updated",Snackbar.LENGTH_SHORT ).show();
                                loadListBanner();
                            }
                        } );

                Snackbar.make(rootLayout,"Food "+item.getName()+" was added", Snackbar.LENGTH_SHORT).show();
                //Toast.makeText(Home.this, "New Category", Toast.LENGTH_SHORT).show();

                loadListBanner();

            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                loadListBanner();
            }
        });
        alertDialog.show();
    }

    private void changeImage(final Banner item) {
        if (filePath != null)
        {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading....");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFOlder = storageReference.child("images/"+imageName);
            imageFOlder.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.show();
                            Toast.makeText(BannerActivity.this, "Uploaded !", Toast.LENGTH_SHORT).show();
                            imageFOlder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set Value for newCategory if image upload and we can get download link
                                    item.setImage( uri.toString() );
                                    mDialog.dismiss();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(BannerActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded "+progress+"%");


                        }
                    });
        }
    }
}
