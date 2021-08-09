package com.majinnaibu.monstercards.ui.dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.majinnaibu.monstercards.AppDatabase;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

public class DashboardViewModel extends AndroidViewModel {
    private final AppDatabase mDB;
    private final MutableLiveData<List<Monster>> mMonsters;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        mDB = AppDatabase.getInstance(application);
        mMonsters = new MutableLiveData<>(new ArrayList<>());
        mDB.monsterDao()
                .get()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSubscriber<List<Monster>>() {
                    @Override
                    public void onNext(List<Monster> monsters) {
                        Collections.sort(monsters);
                        mMonsters.setValue(monsters);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Logger.logError(t);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public LiveData<List<Monster>> getMonsters() {
        return mMonsters;
    }
}
