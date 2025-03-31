package com.pozdro.nuclearindustry.networking.packet;

import com.pozdro.nuclearindustry.blocks.entity.ElechamberBlockEntity;
import com.pozdro.nuclearindustry.screen.ElechamberMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ElechamberFluidSyncS2CPacket {
    private final FluidStack fluidStackIN;
    private final FluidStack fluidStackOUT;
    private final BlockPos pos;

    public ElechamberFluidSyncS2CPacket(FluidStack fluidStackIN, FluidStack fluidStackOUT, BlockPos pos) {
        this.fluidStackIN = fluidStackIN;
        this.fluidStackOUT = fluidStackOUT;
        this.pos = pos;
    }

    public ElechamberFluidSyncS2CPacket(FriendlyByteBuf buf) {
        this.fluidStackIN = buf.readFluidStack();
        this.fluidStackOUT = buf.readFluidStack();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFluidStack(fluidStackIN);
        buf.writeFluidStack(fluidStackOUT);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof ElechamberBlockEntity blockEntity) {
                blockEntity.setFluidInINTank(fluidStackIN);
                blockEntity.setFluidInOUTTank(fluidStackOUT);

                if(Minecraft.getInstance().player.containerMenu instanceof ElechamberMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(pos)) {
                    menu.setFluidInINTank(fluidStackIN);
                    menu.setFluidInOUTTank(fluidStackOUT);
                }
            }
        });
        return true;
    }
}
