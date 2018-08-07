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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.admin.eatserver.Common.Common;
import com.example.admin.eatserver.Interface.ItemClickListener;
import com.example.admin.eatserver.Model.Category;
import com.example.admin.eatserver.Model.Food;
import com.example.admin.eatserver.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import static com.example.admin.eatserver.Common.Common.PICK_IMAGE_REQUEST;

public class FoodList extends AppCompatActivity {
        RecyclerView recyclerView;
        RecyclerView.LayoutManager layoutManager;
        FloatingActionButton fab;
    RelativeLayout rootLayout;

    //Firebase
       FirebaseDatabase db;
       DatabaseReference foodList;
       FirebaseStorage storage;
       StorageReference storageReference;
        //New Food
        MaterialEditText edtName,edtDescription,edtPrice,edtDiscount;
        Button btnSelect,btnUpload;
        Food newFood;
    Uri saveUri;
    String categoryId="";
       FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_food_list );


        //Firebase
        db = FirebaseDatabase.getInstance();
        foodList = db.getReference("Food");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        rootLayout = (RelativeLayout) findViewById( R.id.rootLayout );
        //Init
        recyclerView = (RecyclerView)findViewById( R.id.recyclerViewFoods );
        recyclerView.setHasFixedSize( true );
        layoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( layoutManager );
        fab = (FloatingActionButton)findViewById( R.id.fButton );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddFoodDialog();
            }
        } );

        if(getIntent() != null)
            categoryId= getIntent().getStringExtra( "CategoryId" );
        if(!categoryId.isEmpty())
            loadListFood(categoryId);
    }

    private void loadListFood(CharSequence categoryId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild( "menuId" ).equalTo((String) categoryId)
        ) {
            @Override
            protected void populateViewHolder(final FoodViewHolder viewHolder, final Food model,final int position) {
                viewHolder.txtFoodName.setText( model.getName() );
                Picasso.with( getBaseContext() ).load( model.getImage() ).into( viewHolder.FoodimageView );
                final Food local = model;
                viewHolder.setItemClickListener( new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //code Here
                    }
                } );
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter( adapter );
    }

    private void showAddFoodDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodList.this);
        alertDialog.setTitle("Add new Food");
        alertDialog.setMessage("Please fill all information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_food_layout,null);

        edtName = add_menu_layout.findViewById( R.id.edtName );
        edtDescription = add_menu_layout.findViewById( R.id.edtDescription );
        edtPrice = add_menu_layout.findViewById( R.id.edtPrice );
        edtDiscount     = add_menu_layout.findViewById( R.id.edtDiscount );
        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);

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
                uploadImage();
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();

                // create new category
                if(newFood != null)
                {
                    foodList.push().setValue(newFood);
                    Snackbar.make(rootLayout,"New category "+newFood.getName()+" was added", Snackbar.LENGTH_SHORT).show();
                    //Toast.makeText(Home.this, "New Category", Toast.LENGTH_SHORT).show();
                }


            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    private void uploadImage()
    {
        if (saveUri != null)
        {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading....");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFOlder = storageReference.child("images/"+imageName);
            imageFOlder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.show();
                            Toast.makeText(FoodList.this, "Uploaded !", Toast.LENGTH_SHORT).show();
                            imageFOlder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set Value for newCategory if image upload and we can get download link
                                    newFood = new Food(  );
                                    newFood.setName( edtName.getText().toString() );
                                    newFood.setDescription( edtDescription.getText().toString() );
                                    newFood.setPrice( edtPrice.getText().toString() );
                                    newFood.setDiscount( edtDiscount.getText().toString() );
                                    newFood.setMenuId( categoryId);
                                    newFood.setImage(uri.toString());

                                    mDialog.dismiss();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(FoodList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void chooseImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter( resultCode, data );

        if(resultCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data!= null && data.getData() != null)
        {
            saveUri = data.getData();
            btnSelect.setText("Image Selected");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data!= null && data.getData() != null)
        {
            saveUri = data.getData();
            btnSelect.setText("Image Selected");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals( Common.UPDATE ))
        {
            showUpdateFoodDialog(adapter.getRef( item.getOrder()).getKey(),adapter.getItem( item.getOrder() ));
        }
        else if(item.getTitle().equals( Common.DELETE ))
        {
            deleteFood(adapter.getRef( item.getOrder() ).getKey());
        }
        return super.onContextItemSelected( item );

    }

    private void deleteFood(String key) {
        foodList.child(key).removeValue();
    }

    private void showUpdateFoodDialog(final String key, final Food item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodList.this);
        alertDialog.setTitle("Edit Food");
        alertDialog.setMessage("Please fill all information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_food_layout,null);

        edtName = add_menu_layout.findViewById( R.id.edtName );
        edtDescription = add_menu_layout.findViewById( R.id.edtDescription );
        edtPrice = add_menu_layout.findViewById( R.id.edtPrice );
        edtDiscount     = add_menu_layout.findViewById( R.id.edtDiscount );
        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);

        edtName.setText( item.getName() );
        edtDiscount.setText( item.getDiscount() );
        edtPrice.setText( item.getPrice() );
        edtDescription.setText( item.getDescription() );

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

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();


                    item.setName( edtName.getText().toString() );
                    item.setPrice( edtPrice.getText().toString() );
                    item.setDiscount( edtDiscount.getText().toString() );
                    item.setDescription( edtDescription.getText().toString() );
                    foodList.child(key).setValue(item);

                    Snackbar.make(rootLayout,"Food "+item.getName()+" was added", Snackbar.LENGTH_SHORT).show();
                    //Toast.makeText(Home.this, "New Category", Toast.LENGTH_SHORT).show();



            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    private void changeImage(final Food item)
    {
        if (saveUri != null)
        {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading....");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFOlder = storageReference.child("images/"+imageName);
            imageFOlder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.show();
                            Toast.makeText(FoodList.this, "Uploaded !", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(FoodList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
