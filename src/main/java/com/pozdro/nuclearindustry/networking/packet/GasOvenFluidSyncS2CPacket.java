package com.pozdro.nuclearindustry.networking.packet;

import com.pozdro.nuclearindustry.blocks.entity.GasOvenBlockEntity;
import com.pozdro.nuclearindustry.blocks.entity.StillBlockEntity;
import com.pozdro.nuclearindustry.screen.GasOvenMenu;
import com.pozdro.nuclearindustry.screen.StillMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GasOvenFluidSyncS2CPacket {
    private final FluidStack fluidStackIN;
    private final FluidStack fluidStackIN1;
    private final FluidStack fluidStackOUT;
    private final BlockPos pos;

    public GasOvenFluidSyncS2CPacket(FluidStack fluidStackIN,FluidStack fluidStackIN1, FluidStack fluidStackOUT, BlockPos pos) {
        this.fluidStackIN = fluidStackIN;
        this.fluidStackIN1 = fluidStackIN1;
        this.fluidStackOUT = fluidStackOUT;

        this.pos = pos;
    }

    public GasOvenFluidSyncS2CPacket(FriendlyByteBuf buf) {
        this.fluidStackIN = buf.readFluidStack();
        this.fluidStackIN1 = buf.readFluidStack();
        this.fluidStackOUT = buf.readFluidStack();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFluidStack(fluidStackIN);
        buf.writeFluidStack(fluidStackIN1);
        buf.writeFluidStack(fluidStackOUT);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof GasOvenBlockEntity blockEntity) {
                blockEntity.setFluidInINTank(fluidStackIN);
                blockEntity.setFluidInIN1Tank(fluidStackIN1);
                blockEntity.setFluidInOUTTank(fluidStackOUT);

                if(Minecraft.getInstance().player.containerMenu instanceof GasOvenMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(pos)) {
                    menu.setFluidInINTank(fluidStackIN);
                    menu.setFluidInIN1Tank(fluidStackIN1);
                    menu.setFluidInOUTTank(fluidStackOUT);
                }
            }
        });
        return true;
    }
}
