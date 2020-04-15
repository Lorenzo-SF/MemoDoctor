package com.memodoctor.memodoctor.model;

import com.memodoctor.memodoctor.application.MemoDoctorApplication;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


public class MemoType extends RealmObject implements Serializable{

    @PrimaryKey
    private int Id;
    @Required
    private String Description;
    //TODO: Cuando se vea como referenciar a una imagen desde bdd, poner @Required
    private String Image;

    public MemoType(){}

    public MemoType(String description){
        this.Id = MemoDoctorApplication.MemoTypeID.incrementAndGet();
        this.Description = description;
    }

    public int GetId(){
        return Id;
    }

    public String GetDescription(){
        return Description;
    }

    public void SetDescription(String description){
        this.Description = description;
    }

    public String GetImage(){
        return Image;
    }

    public void SetImage(String image){
        this.Image = image;
    }

}
