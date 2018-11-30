package com.example.candid_20.dcrapp.bean.fullview_timeline_doctor_bean;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class TimeLineMainResultDocstockiest_Bean implements List<TimeLineResultDoctor_Bean> {

    @SerializedName("result")
    @Expose
    private List<TimeLineResultDoctor_Bean> resultDoctor = null;
    /*@SerializedName("result_stockist")
    @Expose
    private List<TimeLineResultStockiest> resultStockist = null;*/

    public List<TimeLineResultDoctor_Bean> getResultDoctor() {
        return resultDoctor;
    }

    public void setResultDoctor(List<TimeLineResultDoctor_Bean> resultDoctor) {
        this.resultDoctor = resultDoctor;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @NonNull
    @Override
    public Iterator<TimeLineResultDoctor_Bean> iterator() {
        return null;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] a) {
        return null;
    }

    @Override
    public boolean add(TimeLineResultDoctor_Bean timeLineResultDoctor_bean) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends TimeLineResultDoctor_Bean> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, @NonNull Collection<? extends TimeLineResultDoctor_Bean> c) {
        return false;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public TimeLineResultDoctor_Bean get(int index) {
        return null;
    }

    @Override
    public TimeLineResultDoctor_Bean set(int index, TimeLineResultDoctor_Bean element) {
        return null;
    }

    @Override
    public void add(int index, TimeLineResultDoctor_Bean element) {

    }

    @Override
    public TimeLineResultDoctor_Bean remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @NonNull
    @Override
    public ListIterator<TimeLineResultDoctor_Bean> listIterator() {
        return null;
    }

    @NonNull
    @Override
    public ListIterator<TimeLineResultDoctor_Bean> listIterator(int index) {
        return null;
    }

    @NonNull
    @Override
    public List<TimeLineResultDoctor_Bean> subList(int fromIndex, int toIndex) {
        return null;
    }

   /* public List<TimeLineResultStockiest> getResultStockist() {
        return resultStockist;
    }*/

  /*  public void setResultStockist(List<TimeLineResultStockiest> resultStockist) {
        this.resultStockist = resultStockist;
    }*/
}
