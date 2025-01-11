package io.github.millymilo000.nations.cmd.war;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import io.github.millymilo000.nations.War;

import java.time.LocalDateTime;

/*
* Admin only command that allows a faction plan a war on another faction
*/
public class CmdLetWar extends FCommand {

    public CmdLetWar() {
        this.aliases.add("letwar");

        this.requiredArgs.add("attacking faction");
        this.requiredArgs.add("defending faction");

        this.requirements = new CommandRequirements.Builder(Permission.LET_WAR).build();
    }

    @Override
    public void perform(CommandContext context) {
        Faction attackingFaction = context.argAsFaction(0);
        Faction defendingFaction = context.argAsFaction(1);

        if (attackingFaction == null || defendingFaction == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        String atFactTag = attackingFaction.getTag();
        String defFactTag = defendingFaction.getTag();

        War war = new War(atFactTag, defFactTag, now);

        plugin.getMemoryWars().addWar(war);

        context.msg("Successfully let "+atFactTag+" have a war with "+defFactTag);
        attackingFaction.msg(TL.COMMAND_WAR_LETWAR_THEM_ATTACKING.format(defFactTag)+" "+TL.COMMAND_WAR_LETWAR_THEM_BASIC); //Didn't expect this to work lol, (initially this looked way worse, since I forgot about ".format").
        defendingFaction.msg(TL.COMMAND_WAR_LETWAR_THEM_DEFENDING.format(atFactTag)+" "+TL.COMMAND_WAR_LETWAR_THEM_BASIC);
    }
}
