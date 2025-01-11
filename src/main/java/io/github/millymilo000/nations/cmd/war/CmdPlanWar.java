package io.github.millymilo000.nations.cmd.war;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.perms.PermissibleActions;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import io.github.millymilo000.nations.WarPlan;

import java.time.LocalDateTime;

/*
* Command for planning wars
*/

public class CmdPlanWar extends FCommand {

    public CmdPlanWar() {
        this.aliases.add("war");
        this.aliases.add("plan");

        this.requiredArgs.add("opposing faction");
        this.requiredArgs.add("month");
        this.requiredArgs.add("day");
        this.requiredArgs.add("hour");
        this.requiredArgs.add("minute");
        this.optionalArgs.put("can grief","true||false");

        this.requirements = new CommandRequirements.Builder(Permission.WAR)
                .memberOnly()
                .withAction(PermissibleActions.WAR)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        String plrFactionTag = context.faction.getTag();

        Faction opFaction = context.argAsFaction(0);
        Double monthD = context.argAsDouble(1); // Returns null if it's not a double
        Double dayD = context.argAsDouble(2);
        Double hourD = context.argAsDouble(3);
        Double minuteD = context.argAsDouble(4);

        if (opFaction == null) {
            context.sender.sendMessage(getHelpShort());
            return;
        }

        int[] info = plugin.getMemoryWars().getWarId(plrFactionTag, opFaction.getTag());

        if (info[0] == -1) {
            // I found https://stackoverflow.com/a/51837794 Which tells me I can use "plugin.getMemoryWars().getWars().stream().map(War::getAttackingFactionTag).anyMatch(plrFactionTag::equals)"
            // to basically also check if there is war based on the faction tag, but I do also need to get the id/index of the war later, and I don't know if my method is any less optimized than this anyway, so I'll just keep this as is
            context.msg(TL.COMMAND_WAR_NOTINANY);
            return;
        }
        //Check if what they put in, was in fact a double
        if (monthD == null || dayD == null || hourD == null || minuteD == null) {
            context.msg(TL.COMMAND_WAR_PLAN_TIME_1);
            context.msg(TL.COMMAND_WAR_PLAN_TIME_2);
            return;
        }

        int month = monthD.intValue(); // Just to make sure there's no impossible decimals.
        int day = dayD.intValue();
        int hour = hourD.intValue();
        int minute = minuteD.intValue();

        if (month < 1 || day < 1 || hour < 1 || minute < 0 || month > 12 || day > 31 || hour > 23) {
            context.msg(TL.COMMAND_WAR_PLAN_TIME_1);
            context.msg(TL.COMMAND_WAR_PLAN_TIME_2);
            return;
        }

        LocalDateTime timeOfWar = LocalDateTime.now();
        timeOfWar = timeOfWar.minusMonths(timeOfWar.getMonthValue() - month);
        timeOfWar = timeOfWar.minusDays(timeOfWar.getDayOfMonth() - day);
        timeOfWar = timeOfWar.minusHours(timeOfWar.getHour() - hour);
        timeOfWar = timeOfWar.minusMinutes(timeOfWar.getMinute() - minute);
        timeOfWar = timeOfWar.minusSeconds(timeOfWar.getSecond());
        // Issue with this, Someone can say the 31st of a month that doesn't have a 31st,
        // So like if they say the "31st of November", it just gets put on the 1st of December. I could fix this, or ignore it until it becomes an issue :shrug:
        //                           Thanks Intellij, I didn't know you can tell me that lol.

        if (timeOfWar.isBefore(LocalDateTime.now())) {
            context.msg(TL.COMMAND_WAR_PLAN_PAST);
            return;
        }

        //If the Attacking faction doesn't include a can grief statement here it errors
        int canGrief = -1;

        if (context.argIsSet(5)) {
            if (info[1] == 0) {
                context.msg(TL.COMMAND_WAR_CANGRIEF_DEFFAC);
            } else {
                String canGriefString = context.argAsString(5);

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
            context.msg(TL.COMMAND_WAR_CANGRIEF_EMPTYAT);
            return;
        }

        WarPlan warPlan = new WarPlan(timeOfWar);
        if (info[1]==1) {
            warPlan.setAtFacAgree(true);
            warPlan.setCanGrief(canGrief);
        } else {
            warPlan.setDefFacAgree(true);
        }
        plugin.getMemoryWars().getWars().get(info[0]).addWarPlan(warPlan);

        context.msg(TL.COMMAND_WAR_PLAN_SUCCESS);
        opFaction.msg(TL.COMMAND_WAR_PLAN_THEM.format(plrFactionTag));
    }
}
