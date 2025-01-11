package io.github.millymilo000.nations.data;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import io.github.millymilo000.nations.War;

import java.util.ArrayList;
import java.util.HashMap;

/*
* MemoryWars, has an ArrayList that holds a bunch of wars and its plans
* Will load from a json, and saves into one when onDisable is called.
*
* I imagine there's a better way to do this, but my beginner java brain doesn't know yet :3
*
* I should probably rename this to "DataWars" but ehhhhhh I already use MemoryWars so much itd take so longgggg to fixxxx
*
* TODO: Make a function that saves this into a JSON, and loads from a JSON. Run the first one in onDisable(), and the last one in onEnable(). Oh also add this whole thing into /f help
*/

public class MemoryWars {
    ArrayList<War> wars = new ArrayList<>();
    HashMap<String, String> inWar = new HashMap<>();

    public void addWar(War war) {
        wars.add(war);
    }

    public void addInWar(String attackingFac, String defendingFac) {
        inWar.put(attackingFac,defendingFac);
    }

    public void removeInWar(String attackingFac) {
        inWar.remove(attackingFac);
    }

    public void removeWar(int id) {
        wars.remove(id);
    }

    public ArrayList<War> getWars() {
        return wars;
    }

    public HashMap<String, String> getInWar() {
        return inWar;
    }

    public int[] getWarId(String plrFacTag, String opFacTag) {
        //Should be renamed to "getWarInfo", oh well.
        int id = -1;
        int attacking = 1; //1 means they're attacking, 0 means they're defending
        for (War i : wars) { //With how this for loop is handled, I should probably de-enhance-ify it...
            if (i.getAttackingFactionTag().equals(plrFacTag) & i.getDefendingFactionTag().equalsIgnoreCase(opFacTag)) { //I don't really *need* IgnoreCase but to be safe I'll add it.
                id = wars.indexOf(i);
            } else if (i.getDefendingFactionTag().equals(plrFacTag) & i.getAttackingFactionTag().equalsIgnoreCase(opFacTag)) {
                id = wars.indexOf(i);
                attacking = 0;
            }
        }
        return new int[] {id, attacking};
    }

    public Faction getOpposingFactionInWar(String factionTag) {
        if (inWar.containsKey(factionTag)) {
            return Factions.getInstance().getByTag(inWar.get(factionTag));
        } else if (inWar.containsValue(factionTag)) {
            //there is probably a better way but I this works so im not complaining
            for (String entry : inWar.keySet()) {
                if (inWar.get(entry).equals(factionTag)) {
                    return Factions.getInstance().getByTag(entry);
                }
            }
        }
        return null;
    }
}
