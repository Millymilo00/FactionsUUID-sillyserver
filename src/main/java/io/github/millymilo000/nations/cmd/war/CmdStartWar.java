package io.github.millymilo000.nations.cmd.war;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.landraidcontrol.DTRControl;
import com.massivecraft.factions.perms.PermissibleActions;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import io.github.millymilo000.nations.WarPlan;

import java.time.LocalDateTime;
import java.util.ArrayList;

/*
* Start an accepted war plan
*/
public class CmdStartWar extends FCommand {

    public CmdStartWar() {
        this.aliases.add("startwar");

        this.requiredArgs.add("faction tag");

        this.requirements = new CommandRequirements.Builder(Permission.WAR)
                .memberOnly()
                .withAction(PermissibleActions.WAR)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        String plrFactTag = context.faction.getTag();
        Faction opFaction = context.argAsFaction(0);

        int[] info = plugin.getMemoryWars().getWarId(plrFactTag, opFaction.getTag());

        if (info[0] == -1) {
            context.msg(TL.COMMAND_WAR_NOTINANY);
            return;
        }

        ArrayList<WarPlan> warPlans = plugin.getMemoryWars().getWars().get(info[0]).getWarPlans();

        for (WarPlan i : warPlans) {
            if (i.getBothAgree() & i.getWarStartTime().isBefore(LocalDateTime.now())) {
                    //Start war
                //Reset DTR
                DTRControl dtr = (DTRControl) FactionsPlugin.getInstance().getLandRaidControl();

                context.faction.setDTR(dtr.getMaxDTR(context.faction));
                opFaction.setDTR(dtr.getMaxDTR(opFaction));

                //If can grief then open factions, otherwise close them since /f open will be disabled.
                if (i.getCanGrief()==1) {
                    context.faction.setOpen(true);
                    opFaction.setOpen(true);
                } else {
                    context.faction.setOpen(false);
                    opFaction.setOpen(false);
                }

                //Add factions to inWar HashMap
                if (info[1]==1) {
                    plugin.getMemoryWars().addInWar(plrFactTag, opFaction.getTag());
                } else {
                    plugin.getMemoryWars().addInWar(opFaction.getTag(), plrFactTag);
                }

                //Make enemies
                context.faction.setRelationWish(opFaction, Relation.ENEMY);

                //Announce to both sides
                context.faction.msg("WAR HAS BEGUN!");
                opFaction.msg("WAR HAS BEGUN!");
                return;
            }
        }

        //If the for loop succeeds and a war starts a "return;" will be called, if the for loop fails then this will happen.
        context.msg(TL.COMMAND_WAR_START_FAIL);
    }
}
