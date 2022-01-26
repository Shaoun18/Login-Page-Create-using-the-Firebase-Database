package com.seip.firebaseapp.repos;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.seip.firebaseapp.models.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseRepository {
    private final String COOLECTON_EXPENSE = "Expenses";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Task <Void> addNewExpense(Expense expense){
       final DocumentReference docRef = db.collection(COOLECTON_EXPENSE).document();
       expense.setId(docRef.getId());
       return docRef.set(expense);

    }
    public LiveData<List<Expense>> getAllExpense (String id){
        final MutableLiveData<List<Expense>> expenseListLiveData = new MutableLiveData<>();
        db.collection(COOLECTON_EXPENSE).whereEqualTo("usid",id).
                orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
            if(error != null) return;
            final List<Expense> tempList = new ArrayList<>();
            for (DocumentSnapshot doc : value.getDocuments()){
                tempList.add(doc.toObject(Expense.class));
            }
            expenseListLiveData.postValue(tempList);
        });
        return expenseListLiveData;
    }
}
