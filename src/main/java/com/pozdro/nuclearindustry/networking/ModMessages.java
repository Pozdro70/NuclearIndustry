package com.pozdro.nuclearindustry.networking;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.networking.packet.*;
import com.pozdro.nuclearindustry.screen.StillMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(NuclearIndustry.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(ElechamberEnergySyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ElechamberEnergySyncS2CPacket::new)
                .encoder(ElechamberEnergySyncS2CPacket::toBytes)
                .consumerMainThread(ElechamberEnergySyncS2CPacket::handle)
                .add();

        net.messageBuilder(ElechamberFluidSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ElechamberFluidSyncS2CPacket::new)
                .encoder(ElechamberFluidSyncS2CPacket::toBytes)
                .consumerMainThread(ElechamberFluidSyncS2CPacket::handle)
                .add();

        net.messageBuilder(StillEnergySyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(StillEnergySyncS2CPacket::new)
                .encoder(StillEnergySyncS2CPacket::toBytes)
                .consumerMainThread(StillEnergySyncS2CPacket::handle)
                .add();

        net.messageBuilder(StillFluidSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(StillFluidSyncS2CPacket::new)
                .encoder(StillFluidSyncS2CPacket::toBytes)
                .consumerMainThread(StillFluidSyncS2CPacket::handle)
                .add();

        net.messageBuilder(ChemicalMixerEnergySyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ChemicalMixerEnergySyncS2CPacket::new)
                .encoder(ChemicalMixerEnergySyncS2CPacket::toBytes)
                .consumerMainThread(ChemicalMixerEnergySyncS2CPacket::handle)
                .add();

        net.messageBuilder(ChemicalMixerFluidSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ChemicalMixerFluidSyncS2CPacket::new)
                .encoder(ChemicalMixerFluidSyncS2CPacket::toBytes)
                .consumerMainThread(ChemicalMixerFluidSyncS2CPacket::handle)
                .add();

        net.messageBuilder(GasOvenEnergySyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(GasOvenEnergySyncS2CPacket::new)
                .encoder(GasOvenEnergySyncS2CPacket::toBytes)
                .consumerMainThread(GasOvenEnergySyncS2CPacket::handle)
                .add();

        net.messageBuilder(GasOvenFluidSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(GasOvenFluidSyncS2CPacket::new)
                .encoder(GasOvenFluidSyncS2CPacket::toBytes)
                .consumerMainThread(GasOvenFluidSyncS2CPacket::handle)
                .add();

        net.messageBuilder(AbsorberEnergySyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(AbsorberEnergySyncS2CPacket::new)
                .encoder(AbsorberEnergySyncS2CPacket::toBytes)
                .consumerMainThread(AbsorberEnergySyncS2CPacket::handle)
                .add();

        net.messageBuilder(AbsorberFluidSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(AbsorberFluidSyncS2CPacket::new)
                .encoder(AbsorberFluidSyncS2CPacket::toBytes)
                .consumerMainThread(AbsorberFluidSyncS2CPacket::handle)
                .add();

            net.messageBuilder(OreWashingPlantEnergySyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(OreWashingPlantEnergySyncS2CPacket::new)
                .encoder(OreWashingPlantEnergySyncS2CPacket::toBytes)
                .consumerMainThread(OreWashingPlantEnergySyncS2CPacket::handle)
                .add();

        net.messageBuilder(OreWashingPlantFluidSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(OreWashingPlantFluidSyncS2CPacket::new)
                .encoder(OreWashingPlantFluidSyncS2CPacket::toBytes)
                .consumerMainThread(OreWashingPlantFluidSyncS2CPacket::handle)
                .add();

    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }

}
