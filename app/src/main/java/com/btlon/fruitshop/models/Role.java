package com.btlon.fruitshop.models;

public class Role {
    public String getMaRole() {
        return maRole;
    }
    public Role(){};

    public void setMaRole(String maRole) {
        this.maRole = maRole;
    }

    public String getTenRole() {
        return tenRole;
    }

    public void setTenRole(String tenRole) {
        this.tenRole = tenRole;
    }

    private String maRole;
    private String tenRole;
}
