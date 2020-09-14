package com.example.projects.cloudoverdrive.Models;

public class Info {
    String valn, valu, vale,valpa;

    public Info() {
    }

    public Info(String valn, String valu, String vale,  String valpa) {
        this.valn = valn;
        this.valu = valu;
        this.vale = vale;
        this.valpa = valpa;
    }

    public String getValn() {
        return valn;
    }

    public void setValn(String valn) {
        this.valn = valn;
    }

    public String getValu() {
        return valu;
    }

    public void setValu(String valu) {
        this.valu = valu;
    }

    public String getVale() {
        return vale;
    }

    public void setVale(String vale) {
        this.vale = vale;
    }

    public String getValpa() {
        return valpa;
    }

    public void setValpa(String valpa) {
        this.valpa = valpa;
    }
}
