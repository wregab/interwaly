package com.realizationtime.interwaly.trening;

import com.realizationtime.interwaly.ListOfInterwaly;
import com.realizationtime.interwaly.interwaly.Interwal;
import com.realizationtime.interwaly.interwaly.Koniec;
import com.realizationtime.interwaly.interwaly.Sprint;

public class TreningArbiter {
    private final ListOfInterwaly interwaly;
    private final Interwal koniec = new Koniec();
    private final long treningStartMS = System.currentTimeMillis();

    public TreningArbiter(ListOfInterwaly interwaly) {
        this.interwaly = interwaly;
        buildTabliceKoncow();
    }

    private long[] tablicaKoncow;
    private void buildTabliceKoncow() {
        tablicaKoncow = new long[interwaly.getList().size()];
        long koniecObecnego = treningStartMS;
        for (int i = 0; i < tablicaKoncow.length; i++) {
            Interwal interwal = interwaly.getList().get(i);
            koniecObecnego += interwal.getCzasMS();
            tablicaKoncow[i] = koniecObecnego;
        }
    }

    public Interwal getCurrentInterval() {
        for (int i = 0; i < tablicaKoncow.length; i++) {
            if (System.currentTimeMillis() < tablicaKoncow[i]) {
                return interwaly.getList().get(i);
            }
        }
        return koniec;
    }

    public boolean isCurrentSprint() {
        return getCurrentInterval() instanceof Sprint;
    }

    public boolean isTreningDone() {
        return getCurrentInterval() == koniec;
    }

    public int getCurrentPercentage(){
        Interwal current = getCurrentInterval();
        if (current == koniec) {
            return 100;
        }
        return (int) (getCurrentTimeMS() * 100 / current.getCzasMS());
    }

    public int getCurrentTimeMS() {
        Interwal current = getCurrentInterval();
        if (current == koniec) {
            return (int) (tablicaKoncow.length == 0 ? System.currentTimeMillis() - treningStartMS
                                : System.currentTimeMillis() - tablicaKoncow[tablicaKoncow.length-1]);
        }
        Interwal pierwszy = interwaly.getList().get(0);
        if (current == pierwszy) {
            return (int) (System.currentTimeMillis() - treningStartMS);
        }
        int indexOfCurrent = interwaly.getList().indexOf(current);
        return (int) (System.currentTimeMillis() - tablicaKoncow[indexOfCurrent-1]);
    }

    public int getTotalPercentage(){
        int ret = (int) ((System.currentTimeMillis() - treningStartMS ) *100L / interwaly.getTotalTimeInMS());
        if (ret > 100) {
            ret = 100;
        }
        return ret;
    }

    public int getTotalTimeMS() {
        return (int) (System.currentTimeMillis() - treningStartMS);
    }
}
