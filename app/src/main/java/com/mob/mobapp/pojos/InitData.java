package com.mob.mobapp.pojos;

import java.util.ArrayList;

public class InitData {
    private Integer count;
    ArrayList<Promo> promos;

    public InitData(Integer count, ArrayList<Promo> promos) {
        this.count = count;
        this.promos = promos;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ArrayList<Promo> getPromos() {
        return promos;
    }

    public void setPromos(ArrayList<Promo> promos) {
        this.promos = promos;
    }
}
