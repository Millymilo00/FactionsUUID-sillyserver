package io.github.millymilo000.nations;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class War {
    String attackingFactionTag;
    String defendingFactionTag;
    LocalDateTime dateAccepted;
    ArrayList<WarPlan> warPlans;

    public War(String attackingFaction, String defendingFaction, LocalDateTime dateAccepted) {
        this.attackingFactionTag = attackingFaction;
        this.defendingFactionTag = defendingFaction;
        this.dateAccepted = dateAccepted;
        warPlans = new ArrayList<>();
    }

    public String getAttackingFactionTag() {
        return attackingFactionTag;
    }

    public String getDefendingFactionTag() {
        return defendingFactionTag;
    }
    public LocalDateTime getDateAccepted() {
        return dateAccepted;
    }
    public void addWarPlan(WarPlan warPlan) {
        warPlans.add(warPlan);
    }
    public void removeWarPlan(int id) {
        warPlans.remove(id);
    }
    public ArrayList<WarPlan> getWarPlans() {
        return warPlans;
    }
}
