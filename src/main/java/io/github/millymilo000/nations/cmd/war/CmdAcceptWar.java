package io.github.millymilo000.nations.cmd.war;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.perms.PermissibleActions;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;

/*
* For accepting a war plan made by another faction, likely will be used as a chat button
*/
public class CmdAcceptWar extends FCommand {

    public CmdAcceptWar() {
        this.aliases.add("acceptwar");

        this.requiredArgs.add("faction tag");
        this.requiredArgs.add("id");
        this.optionalArgs.put("can grief","true||false");

        this.requirements = new CommandRequirements.Builder(Permission.WAR)
                .memberOnly()
                .withAction(PermissibleActions.WAR)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        Faction opFaction = context.argAsFaction(0);
        Double idDouble = context.argAsDouble(1);

        if (opFaction == null) {
            context.sender.sendMessage(getHelpShort());
            return;
        }

        String plrFactionTag = context.faction.getTag();

        int[] info = plugin.getMemoryWars().getWarId(plrFactionTag, opFaction.getTag());

        if (info[0] == -1) {
            context.msg(TL.COMMAND_WAR_NOTINANY);
            return;
        }

        if (idDouble == null) {
            context.msg(TL.COMMAND_WAR_ID_NOT);
            return;
        }

        int id = idDouble.intValue();

        if (id < 0 || id > plugin.getMemoryWars().getWars().get(info[0]).getWarPlans().size()) {
            context.msg(TL.COMMAND_WAR_ID_INVALID);
            return;
        }

        int canGrief = -1;

        if (context.argIsSet(2)) {
            if (info[1] == 0) {
                context.msg(TL.COMMAND_WAR_CANGRIEF_DEFFAC);
            } else {
                String canGriefString = context.argAsString(2);

                if (canGriefString.equalsIgnoreCase("true")) {
                    canGrief = 1;
                } else if (canGriefString.equalsIgnoreCase("false")) {
                    canGrief = 0;
                } else {
                    context.msg(TL.COMMAND_WAR_CANGRIEF_UNRECOGNIZED);
                    return;
                }
            }
        } else if (info[1]==1) {
            if (plugin.getMemoryWars().getWars().get(info[0]).getWarPlans().get(id).getCanGrief() == -1) {
                context.msg(TL.COMMAND_WAR_CANGRIEF_EMPTYAT);
                return;
            }
        }

        if (info[1]==0) {
            if (plugin.getMemoryWars().getWars().get(info[0]).getWarPlans().get(id).getDefFacAgree()){
                context.msg(TL.COMMAND_WAR_ACCEPT_ALREADY);
                return;
            } else {
                plugin.getMemoryWars().getWars().get(info[0]).getWarPlans().get(id).setDefFacAgree(true);
            }
        } else {
            if (plugin.getMemoryWars().getWars().get(info[0]).getWarPlans().get(id).getAtFacAgree()) {
                if (context.argIsSet(2)) {
                    context.msg(TL.COMMAND_WAR_ACCEPT_ALREADY + " " + TL.COMMAND_WAR_ACCEPT_SWITCH);
                    plugin.getMemoryWars().getWars().get(info[0]).getWarPlans().get(id).setCanGrief(canGrief);
                } else {
                    context.msg(TL.COMMAND_WAR_ACCEPT_ALREADY);
                }
                return;
            } else {
                plugin.getMemoryWars().getWars().get(info[0]).getWarPlans().get(id).setAtFacAgree(true);
                plugin.getMemoryWars().getWars().get(info[0]).getWarPlans().get(id).setCanGrief(canGrief);
            }
        }

        context.msg(TL.COMMAND_WAR_ACCEPT_SUCCESS);
        if (plugin.getMemoryWars().getWars().get(info[0]).getWarPlans().get(id).getDefFacAgree() && plugin.getMemoryWars().getWars().get(info[0]).getWarPlans().get(id).getAtFacAgree()) {
            plugin.getMemoryWars().getWars().get(info[0]).getWarPlans().get(id).setBothAgree(true);
            context.msg(TL.COMMAND_WAR_ACCEPT_BOTHAGREE_PLR);
            opFaction.msg(TL.COMMAND_WAR_ACCEPT_BOTHAGREE_THEM);
        }
    }
}
