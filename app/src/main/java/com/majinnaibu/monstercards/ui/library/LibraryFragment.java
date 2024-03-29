package com.majinnaibu.monstercards.ui.library;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.ui.shared.MCFragment;
import com.majinnaibu.monstercards.ui.shared.SwipeToDeleteCallback;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LibraryFragment extends MCFragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_library, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        assert fab != null;
        setupAddMonsterButton(fab);

        final RecyclerView recyclerView = root.findViewById(R.id.monster_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        return root;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Context context = requireContext();
        MonsterRepository repository = this.getMonsterRepository();

        LibraryRecyclerViewAdapter adapter = new LibraryRecyclerViewAdapter(
                context,
                repository.getMonsters(),
                (monster) -> navigateToMonsterDetail(monster.id),
                (monster) -> repository
                        .deleteMonster(monster)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                Logger.logError(e);
                            }
                        }));
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(requireContext(), (position, direction) -> adapter.deleteItem(position), null));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupAddMonsterButton(@NonNull FloatingActionButton fab) {
        fab.setOnClickListener(view -> {
            Monster monster = new Monster();
            monster.name = getString(R.string.default_monster_name);
            MonsterRepository repository = this.getMonsterRepository();
            repository.addMonster(monster)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new DisposableCompletableObserver() {
                                @Override
                                public void onComplete() {
                                    View view = getView();
                                    assert view != null;
                                    Snackbar.make(
                                            view,
                                            getString(R.string.snackbar_monster_created, monster.name),
                                            Snackbar.LENGTH_LONG)
                                            .setAction("Action", (_view) -> navigateToMonsterDetail(monster.id))
                                            .show();
                                }

                                @Override
                                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                    Logger.logError("Error creating monster", e);
                                    View view = getView();
                                    assert view != null;
                                    Snackbar.make(view, getString(R.string.snackbar_failed_to_create_monster), Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            });
        });
    }

    protected void navigateToMonsterDetail(@NonNull UUID monsterId) {
        NavDirections action = LibraryFragmentDirections.actionNavigationLibraryToNavigationMonster(monsterId.toString());
        Navigation.findNavController(requireView()).navigate(action);
    }
}
