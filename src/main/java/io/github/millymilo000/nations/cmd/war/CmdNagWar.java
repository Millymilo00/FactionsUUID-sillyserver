package io.github.millymilo000.nations.cmd.war;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.perms.PermissibleActions;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;

/*
* Tell the opposing faction about the war, and how they should realllllyyy accept or make a plan.
*/
public class CmdNagWar extends FCommand {

    public CmdNagWar() {
        this.aliases.add("nagwar");
        this.aliases.add("nag");

        this.requiredArgs.add("opposing faction");

        this.requirements = new CommandRequirements.Builder(Permission.WAR_NAG)
                .memberOnly()
                .withAction(PermissibleActions.WAR_NAG)
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

        context.msg(TL.COMMAND_WAR_NAG_SUCCESS);
        opFaction.msg(TL.COMMAND_WAR_NAG_NAG.format(plrFactTag));
    }
}
