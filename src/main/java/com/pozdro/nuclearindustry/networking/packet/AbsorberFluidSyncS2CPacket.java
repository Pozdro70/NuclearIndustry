package com.pozdro.nuclearindustry.networking.packet;

import com.pozdro.nuclearindustry.blocks.entity.AbsorberBlockEntity;
import com.pozdro.nuclearindustry.blocks.entity.GasOvenBlockEntity;
import com.pozdro.nuclearindustry.screen.AbsorberMenu;
import com.pozdro.nuclearindustry.screen.GasOvenMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AbsorberFluidSyncS2CPacket {
    private final FluidStack fluidStackIN;
    private final FluidStack fluidStackABS;
    private final FluidStack fluidStackOUT;
    private final BlockPos pos;

    public AbsorberFluidSyncS2CPacket(FluidStack fluidStackIN, FluidStack fluidStackABS, FluidStack fluidStackOUT, BlockPos pos) {
        this.fluidStackIN = fluidStackIN;
        this.fluidStackABS = fluidStackABS;
        this.fluidStackOUT = fluidStackOUT;

        this.pos = pos;
    }

    public AbsorberFluidSyncS2CPacket(FriendlyByteBuf buf) {
        this.fluidStackIN = buf.readFluidStack();
        this.fluidStackABS = buf.readFluidStack();
        this.fluidStackOUT = buf.readFluidStack();
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFluidStack(fluidStackIN);
        buf.writeFluidStack(fluidStackABS);
        buf.writeFluidStack(fluidStackOUT);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof AbsorberBlockEntity blockEntity) {
                blockEntity.setFluidInINTank(fluidStackIN);
                blockEntity.setFluidInAbsTank(fluidStackABS);
                blockEntity.setFluidInOUTTank(fluidStackOUT);

                if(Minecraft.getInstance().player.containerMenu instanceof AbsorberMenu menu &&
                        menu.getBlockEntity().getBlockPos().equals(pos)) {
                    menu.setFluidInINTank(fluidStackIN);
                    menu.setFluidInAbsTank(fluidStackABS);
                    menu.setFluidInOUTTank(fluidStackOUT);
                }
            }
        });
        return true;
    }
}
