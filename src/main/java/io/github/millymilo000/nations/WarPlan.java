package io.github.millymilo000.nations;

import java.time.LocalDateTime;

public class WarPlan {
    LocalDateTime warStartTime;
    boolean atFacAgree;
    boolean defFacAgree;
    boolean bothAgree;
    int canGrief;

    public WarPlan(LocalDateTime warStartTime) {
        this.warStartTime = warStartTime;
        atFacAgree = false;
        defFacAgree = false;
        bothAgree = false;
        canGrief = -1; // -1: Not set, 0: Cannot grief, 1: Can grief
    }

    public LocalDateTime getWarStartTime() {
        return warStartTime;
    }
    public boolean getAtFacAgree() {
        return atFacAgree;
    }
    public boolean getDefFacAgree() {
        return defFacAgree;
    }
    public boolean getBothAgree() {
        return bothAgree;
    }
    public int getCanGrief() {
        return canGrief;
    }
    public void setAtFacAgree(boolean atFacAgree) {
        this.atFacAgree = atFacAgree;
    }
    public void setDefFacAgree(boolean defFacAgree) {
        this.defFacAgree = defFacAgree;
    }
    public void setBothAgree(boolean bothAgree) {
        this.bothAgree = bothAgree;
    }
    public void setCanGrief(int canGrief) {
        this.canGrief = canGrief;
    }
}
