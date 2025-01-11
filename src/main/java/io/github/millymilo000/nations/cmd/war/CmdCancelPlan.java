package io.github.millymilo000.nations.cmd.war;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.perms.PermissibleActions;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import io.github.millymilo000.nations.WarPlan;

/*
* Cancel an accepted war plan, if your faction is the only one who accepted it, it gets deleted.
*
*/
public class CmdCancelPlan extends FCommand {

    public CmdCancelPlan() {
        this.aliases.add("cancelplan");

        this.requiredArgs.add("faction tag");
        this.requiredArgs.add("id");

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

        if (id < 0 || id > plugin.getMemoryWars().getWars().get(info[0]).getWarPlans().size()-1) {
            context.msg(TL.COMMAND_WAR_ID_INVALID);
            return;
        }

        if (info[1]==0) {
            if (plugin.getMemoryWars().getWars().get(info[0]).getWarPlans().get(id).getDefFacAgree()) {
                plugin.getMemoryWars().getWars().get(info[0]).getWarPlans().get(id).setDefFacAgree(false);
                context.msg(TL.COMMAND_WAR_CANCEL_SUCCESS_ONE);
            } else {
                context.msg(TL.COMMAND_WAR_CANCEL_ALREADY);
                return;
            }
        } else {
            if (plugin.getMemoryWars().getWars().get(info[0]).getWarPlans().get(id).getAtFacAgree()) {
                plugin.getMemoryWars().getWars().get(info[0]).getWarPlans().get(id).setAtFacAgree(false);
                context.msg(TL.COMMAND_WAR_CANCEL_SUCCESS_ONE);
            } else {
                context.msg(TL.COMMAND_WAR_CANCEL_ALREADY);
                return;
            }
        }

        WarPlan warPlan = plugin.getMemoryWars().getWars().get(info[0]).getWarPlans().get(id);

        if (warPlan.getBothAgree()) {
            if (info[1] == 0 && warPlan.getAtFacAgree() || info[1] == 1 && warPlan.getDefFacAgree()) {
                context.msg(TL.COMMAND_WAR_CANCEL_SUCCESS_BOTHAGREE);
            }
        }
        if (!warPlan.getDefFacAgree() & !warPlan.getAtFacAgree()) {
            plugin.getMemoryWars().getWars().get(info[0]).removeWarPlan(id);
            context.msg(TL.COMMAND_WAR_CANCEL_SUCCESS_DELETE.format(id));
            opFaction.msg(TL.COMMAND_WAR_CANCEL_SUCCESS_DELETE.format(id));
        }
    }
}
