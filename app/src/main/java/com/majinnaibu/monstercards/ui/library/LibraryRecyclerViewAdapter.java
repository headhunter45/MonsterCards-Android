package com.majinnaibu.monstercards.ui.library;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.majinnaibu.monstercards.databinding.SimpleListItemBinding;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.ui.shared.SimpleListItemViewHolder;
import com.majinnaibu.monstercards.utils.ItemCallback;

public class LibraryRecyclerViewAdapter extends ListAdapter<Monster, SimpleListItemViewHolder<Monster>> {
    private static final DiffUtil.ItemCallback<Monster> DIFF_CALLBACK = new DiffUtil.ItemCallback<Monster>() {
        @Override
        public boolean areItemsTheSame(@NonNull Monster oldItem, @NonNull Monster newItem) {
            return Monster.areItemsTheSame(oldItem, newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Monster oldItem, @NonNull Monster newItem) {
            return Monster.areContentsTheSame(oldItem, newItem);
        }
    };
    private final ItemCallback<Monster> mOnItemClicked;

    protected LibraryRecyclerViewAdapter(ItemCallback<Monster> onItemClicked) {
        super(DIFF_CALLBACK);
        mOnItemClicked = onItemClicked;
    }

    @Override
    @NonNull
    public SimpleListItemViewHolder<Monster> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SimpleListItemBinding binding = SimpleListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SimpleListItemViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(final @NonNull SimpleListItemViewHolder<Monster> holder, int position) {
        Monster item = getItem(position);
        holder.item = item;
        holder.content.setText(item.name);
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClicked != null) {
                mOnItemClicked.onItem(holder.item);
            }
        });
    }
}
