package io.github.millymilo000.nations.cmd.war;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.landraidcontrol.DTRControl;
import com.massivecraft.factions.perms.PermissibleActions;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
/*
*   Command for ending a war
*
*   TODO: Make some way that admins can see who won the war, Some ideas are
*    * Make it send a message in the discord server using a webhook saying that stuff
*    * Make some kind of log file that admins can look into either using a command, or the file itself.
*/
public class CmdEndWar extends FCommand {
    public CmdEndWar() {
        this.aliases.add("endwar");

        this.optionalArgs.put("if your surrendering","surrender");

        this.requirements = new CommandRequirements.Builder(Permission.WAR)
                .memberOnly()
                .withAction(PermissibleActions.WAR)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        String type = context.argAsString(0);

        Faction opFaction = plugin.getMemoryWars().getOpposingFactionInWar(context.faction.getTag());

        if (opFaction==null) {
            context.msg(TL.COMMAND_WAR_NOT);
            return;
        }

        if (context.argIsSet(0)) {

            if (type.equalsIgnoreCase("surrender")) {
                // If they have 75% DTR or less
                if ((context.faction.getDTR() / ((DTRControl) FactionsPlugin.getInstance().getLandRaidControl()).getMaxDTR(context.faction)) <= 0.75) {
                    //Surrender
                } else {
                    context.msg(TL.COMMAND_WAR_END_DTR_NOT75);
                }
            } else {
                context.msg(TL.COMMAND_WAR_END_PARAM);
            }

        } else {

            if (opFaction.getDTR()<=0) {
                //End war, win for sending faction
            } else {
                context.msg(TL.COMMAND_WAR_END_DTR_NOT0);
            }
        }
    }
}
