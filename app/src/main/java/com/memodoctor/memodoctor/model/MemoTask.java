package com.memodoctor.memodoctor.model;

import com.memodoctor.memodoctor.application.MemoDoctorApplication;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class MemoTask extends RealmObject implements Serializable {

    @PrimaryKey
    private int Id;
    @Required
    private String Observations;
    @Required
    private Date CreateDate;
    @Required
    private Date MemoDate;

    private MemoType MemoType;

    public MemoTask(){}

    public MemoTask(String observations, Date memoDate, MemoType type) {
        this.Id = MemoDoctorApplication.MemoTaskID.incrementAndGet();
        this.Observations = observations;
        this.CreateDate = new Date();
        //TODO: REVISAR GUARDADO DE FECHA, LE METE UN MES POR QUE SI
        this.MemoDate = memoDate;
        this.MemoType = type;
    }

    public int GetId(){
        return this.Id;
    }

    public void SetObservations(String observations){
        this.Observations = observations;
    }

    public String GetObservations(){
        return this.Observations;
    }

    public Date GetCreateDate(){
        return this.CreateDate;
    }

    public void SetMemoDate(Date memoDate){
        this.MemoDate = memoDate;
    }

    public Date GetMemoDate(){
        return this.MemoDate;
    }

    public void SetMemoType(MemoType memoType){
        this.MemoType = memoType;
    }

    public MemoType GetMemoType(){
        return this.MemoType;
    }
}
