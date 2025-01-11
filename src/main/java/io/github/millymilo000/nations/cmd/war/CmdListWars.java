package io.github.millymilo000.nations.cmd.war;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.perms.PermissibleActions;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import io.github.millymilo000.nations.War;
import io.github.millymilo000.nations.WarPlan;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/*
* Lists plans for wars, and planned wars that the user's faction has, with an option arg of the faction they could have a war with.
*/
public class CmdListWars extends FCommand {

    public CmdListWars() {
        this.aliases.add("listwars");

        this.requiredArgs.add("opposing faction");

        this.requirements = new CommandRequirements.Builder(Permission.WAR_LIST)
                .memberOnly()
                .withAction(PermissibleActions.WAR_LIST)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        String plrFactionTag = context.faction.getTag();
        Faction opFaction = context.argAsFaction(0);

        int[] info = plugin.getMemoryWars().getWarId(plrFactionTag, opFaction.getTag());

        if (info[0] == -1) {
            context.msg(TL.COMMAND_WAR_NOTINANY);
            return;
        }

        War war = plugin.getMemoryWars().getWars().get(info[0]);
        ArrayList<WarPlan> warPlans = war.getWarPlans();
        DateTimeFormatter coolFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

        String plans = "\n"+TL.COMMAND_WAR_LIST_PLANS;
        String griefing;

        if (warPlans.isEmpty()) {
            plans += "\n"+TL.COMMAND_WAR_LIST_NONE;
        } else {
            for (int i=0;i<warPlans.size();i++) {
                griefing = " ";
                if (info[1]==1) {
                    if (warPlans.get(i).getCanGrief() == -1) {
                        griefing += TL.COMMAND_WAR_LIST_GRIEF_UNDECIDED;
                    } else if (warPlans.get(i).getCanGrief() == 0) {
                        griefing += TL.COMMAND_WAR_LIST_GRIEF_NONE;
                    } else {
                        griefing += TL.COMMAND_WAR_LIST_GRIEF_ALLOWED;
                    }
                } else {
                    griefing = "";
                }
                if (warPlans.get(i).getBothAgree()) {
                    plans += "\n&a["+i+"] &6" + warPlans.get(i).getWarStartTime().format(coolFormat)+griefing+" &a"+TL.COMMAND_WAR_LIST_BOTHAGREED;
                } else {
                    if (warPlans.get(i).getAtFacAgree()) {
                        plans += "\n&4[" + i + "] &6";
                    } else {
                        plans += "\n&d[" + i + "] &6";
                    }
                    plans += warPlans.get(i).getWarStartTime().format(coolFormat) + griefing;
                }
            }
        }

        context.msg(TL.COMMAND_WAR_LIST_TITLE_1.format(war.getAttackingFactionTag(),war.getDefendingFactionTag())
                +"\n"+TL.COMMAND_WAR_LIST_TITLE_2.format(war.getDateAccepted().format(coolFormat))
                +plans
        );
    }
}
