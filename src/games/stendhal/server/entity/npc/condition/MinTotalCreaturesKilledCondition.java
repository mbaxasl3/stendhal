/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.entity.npc.condition;

import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.rule.EntityManager;
import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.creature.Creature;
import games.stendhal.server.entity.npc.ChatCondition;
import games.stendhal.server.entity.player.Player;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * ChatCondition that makes sure a player has killed (at least) a set number of
 * creatures of any kind
 * 
 * @author filipe
 */
public class MinTotalCreaturesKilledCondition implements ChatCondition {
    final int total;

    /**
     * Default constructor (Defaults to at least 100 total kills)
     */
    public MinTotalCreaturesKilledCondition() {
        this(100);
    }

    /**
     * Constructor
     *
     * @param total The target number of kills required
     */
    public MinTotalCreaturesKilledCondition(int total) {
        this.total = total;
    }

    /**
     * Does the checking for number of creatures killed
     * 
     * @param player The player to check the kills for
     * @param sentence The sentence the player typed
     * @param npc The NPC the player is speaking to
     *
     * @return True if the player has killed the correct number of creatures or more, false otherwise
     */
    public boolean fire(Player player, Sentence sentence, Entity npc) {
        EntityManager manager = SingletonRepository.getEntityManager();
        int totalKills = 0;

        for(final Creature c : manager.getCreatures()) {
            totalKills += player.getSharedKill(c.getName()) + player.getSoloKill(c.getName());
        }

        if (totalKills < total) {
            return false;
        }

        return true;
    }

    @Override
	public String toString() {
		return "total kills >= " + total;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(final Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, false,
                            MinTotalCreaturesKilledCondition.class);
	}
}