package com.seip.firebaseapp.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.seip.firebaseapp.models.Expense;
import com.seip.firebaseapp.repos.ExpenseRepository;

import java.util.List;

public class ExpenseViewModel extends ViewModel {
    private final ExpenseRepository repository;

    public ExpenseViewModel(){
        repository = new ExpenseRepository();
    }

    public void addExpense(Expense expense){
        repository.addNewExpense(expense).addOnSuccessListener(unused -> {

        }).addOnFailureListener(e -> {

        });
    }
    public LiveData<List<Expense>> getAllExpenseByUserID(String uid){
        return repository.getAllExpense(uid);
    }
}
