package com.pozdro.nuclearindustry.networking.packet;

import com.pozdro.nuclearindustry.blocks.entity.OreWashingPlantBlockEntity;
import com.pozdro.nuclearindustry.screen.OreWashingPlantMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OreWashingPlantFluidSyncS2CPacket {
    private final FluidStack fluidStackIN;
    private final BlockPos pos;

    public OreWashingPlantFluidSyncS2CPacket(FluidStack fluidStackIN,BlockPos pos) {
        this.fluidStackIN = fluidStackIN;
        this.pos = pos;
    }

    public OreWashingPlantFluidSyncS2CPacket(FriendlyByteBuf buf) {
        this.fluidStackIN = buf.readFluidStack();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFluidStack(fluidStackIN);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof OreWashingPlantBlockEntity blockEntity) {
                blockEntity.setFluidInINTank(fluidStackIN);
                if(Minecraft.getInstance().player.containerMenu instanceof OreWashingPlantMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(pos)) {
                    menu.setFluidInINTank(fluidStackIN);
                }
            }
        });
        return true;
    }
}
