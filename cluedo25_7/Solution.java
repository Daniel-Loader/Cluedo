package cluedo25_7;

public class Solution {
    private Suspect actualSuspect;
    private Weapon actualWeapon;
    private Estate actualEstate;

    public Solution(Suspect suspect, Weapon weapon, Estate estate) {
        this.actualSuspect = suspect;
        this.actualWeapon = weapon;
        this.actualEstate = estate;
    }

    public Suspect getActualSuspect() {
        return actualSuspect;
    }

    public Weapon getActualWeapon() {
        return actualWeapon;
    }

    public Estate getActualEstate() {
        return actualEstate;
    }
}
