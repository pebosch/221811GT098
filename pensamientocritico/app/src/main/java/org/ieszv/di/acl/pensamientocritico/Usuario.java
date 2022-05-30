package org.ieszv.di.acl.pensamientocritico;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Usuario implements Parcelable {
    String id;
    String name;
    String fSurname;
    String sSurname;
    String centro;
    String curso;
    String letraCurso;

    public Usuario(String name, String fSurname, String sSurname, String centro, String curso, String letraCurso) {
        this.name = name;
        this.fSurname = fSurname;
        this.sSurname = sSurname;
        this.centro = centro;
        this.curso = curso;
        this.letraCurso = letraCurso;
    }

    protected Usuario(Parcel in) {
        id = in.readString();
        name = in.readString();
        fSurname = in.readString();
        sSurname = in.readString();
        centro = in.readString();
        curso = in.readString();
        letraCurso = in.readString();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getfSurname() {
        return fSurname;
    }

    public String getsSurname() {
        return sSurname;
    }

    public String getCentro() {
        return centro;
    }

    public String getCurso() {
        return curso;
    }

    public String getLetraCurso() {
        return letraCurso;
    }

    private String generateId(){
        String id = "";
        id += name.substring(0, 1);
        id += fSurname.substring(0, 1);
        id += sSurname.substring(0, 1);
        id += curso.replace("º", "");
        id += letraCurso.toString();

        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmm");
        Date date = new Date();
        id += formatter.format(date);

        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(fSurname);
        parcel.writeString(sSurname);
        parcel.writeString(centro);
        parcel.writeString(curso);
        parcel.writeString(letraCurso);
    }
}
