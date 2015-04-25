/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dragonet.net.translator.tope;

import net.glowstone.entity.GlowPlayer;
import net.glowstone.net.message.play.entity.SpawnPlayerMessage;
import org.dragonet.entity.metadata.EntityMetaData;
import org.dragonet.net.DragonetSession;
import org.dragonet.net.packet.minecraft.AddPlayerPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.translator.MessageTranslatorToPE;
import org.dragonet.net.translator.Translator_v0_11;

public class SpawnPlayerMessageTranslator extends MessageTranslatorToPE<Translator_v0_11, SpawnPlayerMessage> {

    public SpawnPlayerMessageTranslator(Translator_v0_11 translator, DragonetSession session) {
        super(translator, session);
    }

    @Override
    public PEPacket[] handleSpecific(SpawnPlayerMessage packet) {
            if (!this.getTranslator().cachedEntityIDs.contains(packet.id)) {
                this.getTranslator().cachedEntityIDs.add(packet.id); //Add to the spawned entity list
            }
            this.getTranslator().cachedPlayerEntities.add(packet.id); //Register this id as a player
            AddPlayerPacket pkAddPlayer = new AddPlayerPacket();
            pkAddPlayer.clientID = 0;
            pkAddPlayer.eid = packet.getId();
            pkAddPlayer.username = this.getSession().getServer().getPlayer(packet.getUuid()).getDisplayName();
            pkAddPlayer.x = (float) packet.getX();
            pkAddPlayer.y = (float) packet.getY();
            pkAddPlayer.z = (float) packet.getZ();
            pkAddPlayer.speedX = 0.0f;
            pkAddPlayer.speedY = 0.0f;
            pkAddPlayer.speedZ = 0.0f;
            pkAddPlayer.yaw = packet.getRotation();
            pkAddPlayer.pitch = packet.getPitch();
            pkAddPlayer.skin = String.format("http://s3.amazonaws.com/MinecraftSkins/%s.png", pkAddPlayer.username);
            pkAddPlayer.slim = true;
            pkAddPlayer.metadata = EntityMetaData.getMetaDataFromPlayer((GlowPlayer) this.getSession().getPlayer().getWorld().getEntityManager().getEntity(packet.getId()));
            return new PEPacket[]{pkAddPlayer};
    }

}