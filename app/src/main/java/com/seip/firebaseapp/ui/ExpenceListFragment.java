package com.seip.firebaseapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.seip.firebaseapp.R;
import com.seip.firebaseapp.adapter.ExpenseAdapter;
import com.seip.firebaseapp.databinding.BottomSheetLayoutBinding;
import com.seip.firebaseapp.databinding.FragmentExpenceListBinding;
import com.seip.firebaseapp.models.Expense;
import com.seip.firebaseapp.viewmodel.ExpenseViewModel;
import com.seip.firebaseapp.viewmodel.LoginViewModel;

import java.io.ByteArrayOutputStream;

public class ExpenceListFragment extends Fragment {
    private final  String TAG = ExpenceListFragment.class.getSimpleName();
    private FragmentExpenceListBinding binding;
    private BottomSheetLayoutBinding bottomSheetLayoutBinding;
    private boolean issaving = false;
    private LoginViewModel loginViewModel;
    private ExpenseViewModel expenseViewModel;
    private ImageView memoimageView;
    private  String imageUrl;

    private ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Uri photoUri = result.getData().getData();
                        memoimageView.setImageURI(photoUri);

                    }
                }
            });

    private void uploadphoto(String tittleW, double amountW) {
        final StorageReference photoRef = FirebaseStorage.getInstance().getReference().
                child("memos/" + System.currentTimeMillis());
        // Get the data from an ImageView as bytes
        memoimageView.setDrawingCacheEnabled(true);
        memoimageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) memoimageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = photoRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG,"onfailue: "+ exception.getLocalizedMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return photoRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();

                    final Expense expense = new Expense(null, tittleW, amountW, System.currentTimeMillis(),
                            loginViewModel.getUser().getUid(),imageUrl);

                    expenseViewModel.addExpense(expense);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }

    public ExpenceListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExpenceListBinding.inflate(inflater);
        bottomSheetLayoutBinding = BottomSheetLayoutBinding.inflate(LayoutInflater.from(getActivity()));
        loginViewModel= new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        expenseViewModel= new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        loginViewModel.authLiveData.observe(getViewLifecycleOwner(), authstate -> {
            if(authstate == LoginViewModel.Authstate.UNAUTHENTICATED){
                Navigation.findNavController(container).navigate(R.id.login_action);
            }
        });

        final ExpenseAdapter adapter = new ExpenseAdapter();
        binding.expenseRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.expenseRV.setAdapter(adapter);

        if(loginViewModel.getUser() != null) {
            expenseViewModel.getAllExpenseByUserID(loginViewModel.getUser().getUid()).
                    observe(getViewLifecycleOwner(),
                    expenses -> {
                        adapter.submitList(expenses);
                    });
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        final CardView cv = view.findViewById(R.id.addExpenselayout);
        final BottomSheetBehavior<CardView> behavior = BottomSheetBehavior.from(cv);

        final EditText titleET = cv.findViewById(R.id.title);
        final EditText amountET = cv.findViewById(R.id.amount);
        final Button saveBtn = cv.findViewById(R.id.saveBtn);
        final Button gallerBTN = cv.findViewById(R.id.gallerBTN);
        memoimageView = cv.findViewById(R.id.memoImage);

                binding.addExpensebutton.setOnClickListener(view1 -> {
            if(behavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }else {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
                gallerBTN.setOnClickListener(v -> {
                    final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    launcher.launch(intent);
                    startActivityForResult(intent,999);
                });

        saveBtn.setOnClickListener(view12 -> {
        final String tittleW = titleET.getText().toString();
        final double amountW = Double.parseDouble(amountET.getText().toString());



        uploadphoto(tittleW,amountW);
        titleET.setText("");
        amountET.setText("");
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if(requestCode == 999 && requestCode == Activity.RESULT_OK){
//            Uri photouri = data.getData();
//            Log.e("phototest",photouri.toString());
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}