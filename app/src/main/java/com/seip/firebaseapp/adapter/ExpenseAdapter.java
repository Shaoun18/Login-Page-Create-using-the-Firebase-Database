package com.seip.firebaseapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.seip.firebaseapp.databinding.ExpenseRowBinding;
import com.seip.firebaseapp.models.Expense;
import com.seip.firebaseapp.viewmodel.ExpenseViewModel;

public class ExpenseAdapter extends ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder> {
    public ExpenseAdapter() {
        super(new ExpenseDiffCallback());
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ExpenseRowBinding binding = ExpenseRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ExpenseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        final Expense expense = getItem(position);
        holder.bind(expense);

    }

    static class  ExpenseViewHolder extends RecyclerView.ViewHolder{
        private ExpenseRowBinding binding;
        public ExpenseViewHolder(ExpenseRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
            public void bind(Expense expense){
            binding.setExpense(expense);
            }
    }
    static class ExpenseDiffCallback extends DiffUtil.ItemCallback<Expense> {

        @Override
        public boolean areItemsTheSame(@NonNull Expense oldItem, @NonNull Expense newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Expense oldItem, @NonNull Expense newItem) {
            return oldItem.getId().equals(newItem.getId());
        }
    }


}
