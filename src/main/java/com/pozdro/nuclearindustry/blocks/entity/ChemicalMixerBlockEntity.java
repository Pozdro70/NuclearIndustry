package com.pozdro.nuclearindustry.blocks.entity;

import com.pozdro.nuclearindustry.blocks.custom.ChemicalMixerBlock;
import com.pozdro.nuclearindustry.fluid.fumingsulfuricacid.ModFluidsFumingSulfuricAcid;
import com.pozdro.nuclearindustry.fluid.oxygen.ModFluidsOxygen;
import com.pozdro.nuclearindustry.fluid.purifiedwater.ModFluidsPurifiedWater;
import com.pozdro.nuclearindustry.fluid.sulfurdioxide.ModFluidsSulfurDioxide;
import com.pozdro.nuclearindustry.fluid.sulfuricacid.ModFluidsSulfuricAcid;
import com.pozdro.nuclearindustry.fluid.sulfurtrioxide.ModFluidsSulfurTrioxide;
import com.pozdro.nuclearindustry.networking.ModMessages;
import com.pozdro.nuclearindustry.networking.packet.ChemicalMixerEnergySyncS2CPacket;
import com.pozdro.nuclearindustry.networking.packet.ChemicalMixerFluidSyncS2CPacket;
import com.pozdro.nuclearindustry.screen.ChemicalMixerMenu;
import com.pozdro.nuclearindustry.util.ModEnergyStorage;
import com.pozdro.nuclearindustry.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ChemicalMixerBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(5) //size of inv
    {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot){
                case 0 -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
                case 1 -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
                case 2 -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
                case 3 -> true;
                case 4 -> true;

                default -> super.isItemValid(slot,stack);
            };
        }
    };

    private final ModEnergyStorage ENERGY_STORAGE = new ModEnergyStorage(70000,260) {
        @Override
        public void onEnergyChanged() {
            setChanged();

            ModMessages.sendToClients(new ChemicalMixerEnergySyncS2CPacket(this.energy,getBlockPos()));

        }
    };

    private static final int ENERGY_REQ = 50;

    private final FluidTank FLUID_TANK_IN = new FluidTank(64000){
        @Override
        protected void onContentsChanged() {
            setChanged();

            if(!level.isClientSide()){
                ModMessages.sendToClients(new ChemicalMixerFluidSyncS2CPacket(FLUID_TANK_IN.getFluid(),FLUID_TANK_IN1.getFluid(),
                        FLUID_TANK_OUT.getFluid(),worldPosition));
            }

        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid().is(ModTags.Fluids.CHEMICAL_MIXER_INP_FLUIDS);
        }
    };

    private final FluidTank FLUID_TANK_IN1 = new FluidTank(64000){
        @Override
        protected void onContentsChanged() {
            setChanged();

            if(!level.isClientSide()){
                ModMessages.sendToClients(new ChemicalMixerFluidSyncS2CPacket(FLUID_TANK_IN.getFluid(),FLUID_TANK_IN1.getFluid(),
                        FLUID_TANK_OUT.getFluid(),worldPosition));
            }

        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid().is(ModTags.Fluids.CHEMICAL_MIXER_INP1_FLUIDS);
        }
    };


    private final FluidTank FLUID_TANK_OUT = new FluidTank(64000){
        @Override
        protected void onContentsChanged() {
            setChanged();

            if(!level.isClientSide()){
                ModMessages.sendToClients(new ChemicalMixerFluidSyncS2CPacket(FLUID_TANK_IN.getFluid(),FLUID_TANK_IN1.getFluid(),
                        FLUID_TANK_OUT.getFluid(),worldPosition));
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return true;
        }
    };

    public  void setFluidInINTank(FluidStack stack){
        this.FLUID_TANK_IN.setFluid(stack);
    }
    public  void setFluidInIN1Tank(FluidStack stack){this.FLUID_TANK_IN1.setFluid(stack);}

    public  void setFluidInOUTTank(FluidStack stack){
        this.FLUID_TANK_OUT.setFluid(stack);
    }

    public FluidStack getFluidStackInINTank(){
        return FLUID_TANK_IN.getFluid();
    }
    public FluidStack getFluidStackInIN1Tank(){return FLUID_TANK_IN1.getFluid();}

    public FluidStack getFluidStackInOUTTank(){
        return FLUID_TANK_OUT.getFluid();
    }


    private LazyOptional<IFluidHandler> lazyFluidHandlerIN = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyFluidHandlerIN1 = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyFluidHandlerOUT = LazyOptional.empty();


    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(
                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 2, (i, s) -> false)),

                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 4,
                            (index, stack) -> itemHandler.isItemValid(4, stack))),

                    Direction.UP, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> itemHandler.isItemValid(0, stack))));

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress =100;

    public ChemicalMixerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CHEMICAL_MIXER.get(), pPos, pBlockState);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ChemicalMixerBlockEntity.this.progress;
                    case 1 -> ChemicalMixerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ChemicalMixerBlockEntity.this.progress = value;
                    case 1 -> ChemicalMixerBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Chemical Mixer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        ModMessages.sendToClients(new ChemicalMixerEnergySyncS2CPacket(this.ENERGY_STORAGE.getEnergyStored(),getBlockPos()));
        ModMessages.sendToClients(new ChemicalMixerFluidSyncS2CPacket(FLUID_TANK_IN.getFluid(),FLUID_TANK_IN1.getFluid(),FLUID_TANK_OUT.getFluid(),worldPosition));

        return new ChemicalMixerMenu(pContainerId,pPlayerInventory,this,this.data);
    }

    public IEnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    public void setEnergyLevel(int energy) {
        this.ENERGY_STORAGE.setEnergy(energy);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ENERGY){
            return lazyEnergyHandler.cast();
        }

        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if(side == null) {
                return lazyItemHandler.cast();
            }

            if(directionWrappedHandlerMap.containsKey(side)) {
                Direction localDir = this.getBlockState().getValue(ChemicalMixerBlock.FACING);

                if(side == Direction.UP || side == Direction.DOWN) {
                    return directionWrappedHandlerMap.get(side).cast();
                }

                return switch (localDir) {
                    default -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
                    case EAST -> directionWrappedHandlerMap.get(side.getClockWise()).cast();
                    case SOUTH -> directionWrappedHandlerMap.get(side).cast();
                    case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
                };
            }
        }

        if(cap==ForgeCapabilities.FLUID_HANDLER){
            if(side == Direction.NORTH){
                return lazyFluidHandlerOUT.cast();
            }
            else if (side == Direction.SOUTH) {
                return  lazyFluidHandlerIN.cast();
            }
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(()->itemHandler);
        lazyEnergyHandler = LazyOptional.of(()->ENERGY_STORAGE);
        lazyFluidHandlerIN = LazyOptional.of(()->FLUID_TANK_IN);
        lazyFluidHandlerIN1 = LazyOptional.of(()->FLUID_TANK_IN1);
        lazyFluidHandlerOUT= LazyOptional.of(()->FLUID_TANK_OUT);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
        lazyFluidHandlerIN.invalidate();
        lazyFluidHandlerIN1.invalidate();
        lazyFluidHandlerOUT.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory",itemHandler.serializeNBT());
        pTag.putInt("chemicalmixer.progress",this.progress);
        pTag.putInt("chemicalmixer.energy",ENERGY_STORAGE.getEnergyStored());
        pTag.put("chemicalmixer.InTank", FLUID_TANK_IN.writeToNBT(new CompoundTag()));
        pTag.put("chemicalmixer.In1Tank", FLUID_TANK_IN1.writeToNBT(new CompoundTag()));
        pTag.put("chemicalmixer.OutTank", FLUID_TANK_OUT.writeToNBT(new CompoundTag()));
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress= pTag.getInt("chemicalmixer.progress");
        ENERGY_STORAGE.setEnergy(pTag.getInt("chemicalmixer.energy"));

        if (pTag.contains("chemicalmixer.InTank")) {
            FLUID_TANK_IN.readFromNBT(pTag.getCompound("chemicalmixer.InTank"));
        }
        if (pTag.contains("chemicalmixer.In1Tank")) {
            FLUID_TANK_IN1.readFromNBT(pTag.getCompound("chemicalmixer.In1Tank"));
        }
        if (pTag.contains("chemicalmixer.OutTank")) {
            FLUID_TANK_OUT.readFromNBT(pTag.getCompound("chemicalmixer.OutTank"));
        }

        super.load(pTag);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, ChemicalMixerBlockEntity pEntity)
    {
        if(level.isClientSide()){return;}
        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());


        if(pEntity.itemHandler.getStackInSlot(0).getCount() >0){

            pEntity.itemHandler.getStackInSlot(0).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler->{
                int drainAmount = Math.min(pEntity.FLUID_TANK_IN.getSpace(), 1000);
                FluidStack stack = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
                if(pEntity.FLUID_TANK_IN.isFluidValid(stack)){
                    stack = handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);

                    pEntity.FLUID_TANK_IN.fill(stack, IFluidHandler.FluidAction.EXECUTE);
                    pEntity.itemHandler.extractItem(0,1,false);
                    pEntity.itemHandler.insertItem(0,handler.getContainer(),false);
                }
            });
        }

        if(pEntity.itemHandler.getStackInSlot(1).getCount() >0){

            pEntity.itemHandler.getStackInSlot(1).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler->{
                int drainAmount = Math.min(pEntity.FLUID_TANK_IN1.getSpace(), 1000);
                FluidStack stack = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
                if(pEntity.FLUID_TANK_IN1.isFluidValid(stack)){
                    stack = handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);

                    pEntity.FLUID_TANK_IN1.fill(stack, IFluidHandler.FluidAction.EXECUTE);
                    pEntity.itemHandler.extractItem(1,1,false);
                    pEntity.itemHandler.insertItem(1,handler.getContainer(),false);
                }
            });
        }

        /*
        if(pEntity.itemHandler.getStackInSlot(2).getCount() >0){

            pEntity.itemHandler.getStackInSlot(1).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler->{
                int drainAmount = Math.min(pEntity.FLUID_TANK_IN2.getSpace(), 1000);
                FluidStack stack = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
                if(pEntity.FLUID_TANK_IN2.isFluidValid(stack)){
                    stack = handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);

                    pEntity.FLUID_TANK_IN2.fill(stack, IFluidHandler.FluidAction.EXECUTE);
                    pEntity.itemHandler.extractItem(2,1,false);
                    pEntity.itemHandler.insertItem(2,handler.getContainer(),false);
                }
            });
        }
         */

        //funkcjonalność

        if(pEntity.getEnergyStorage().getEnergyStored() >= ENERGY_REQ* pEntity.maxProgress &&
                pEntity.FLUID_TANK_OUT.getFluidAmount()+800 < pEntity.FLUID_TANK_OUT.getCapacity() &&
                pEntity.FLUID_TANK_IN.getFluid().equals(new FluidStack(ModFluidsSulfurDioxide.SOURCE_SULFURDIOXIDE.get(), 800)) &&
                pEntity.FLUID_TANK_IN1.getFluid().equals(new FluidStack(ModFluidsOxygen.SOURCE_OXYGEN.get(), 800)))
        {
            if(pEntity.progress<pEntity.maxProgress){
                pEntity.progress++;
                pEntity.getEnergyStorage().extractEnergy(ENERGY_REQ,false);
            }
            else{
                pEntity.progress=0;
                pEntity.FLUID_TANK_OUT.fill(new FluidStack(ModFluidsSulfurTrioxide.SOURCE_SULFURTRIOXIDE.get(),pEntity.FLUID_TANK_OUT.getFluidAmount()+800), IFluidHandler.FluidAction.EXECUTE);
                pEntity.FLUID_TANK_IN.drain(800, IFluidHandler.FluidAction.EXECUTE);
                pEntity.FLUID_TANK_IN1.drain(800, IFluidHandler.FluidAction.EXECUTE);
            }
            setChanged(level,blockPos,state);
        }

        if(pEntity.getEnergyStorage().getEnergyStored() >= ENERGY_REQ* pEntity.maxProgress &&
                pEntity.FLUID_TANK_OUT.getFluidAmount()+800 < pEntity.FLUID_TANK_OUT.getCapacity() &&
                pEntity.FLUID_TANK_IN.getFluid().equals(new FluidStack(ModFluidsFumingSulfuricAcid.SOURCE_FUMINGSULFURICACID.get(), 800)) &&
                pEntity.FLUID_TANK_IN1.getFluid().equals(new FluidStack(ModFluidsPurifiedWater.SOURCE_PURIFIEDWATER.get(), 800)))
        {
            if(pEntity.progress<pEntity.maxProgress){
                pEntity.progress++;
                pEntity.getEnergyStorage().extractEnergy(ENERGY_REQ,false);
            }
            else{
                pEntity.progress=0;
                pEntity.FLUID_TANK_OUT.fill(new FluidStack(ModFluidsSulfuricAcid.SOURCE_SULFURICACID.get(),pEntity.FLUID_TANK_OUT.getFluidAmount()+800), IFluidHandler.FluidAction.EXECUTE);
                pEntity.FLUID_TANK_IN.drain(800, IFluidHandler.FluidAction.EXECUTE);
                pEntity.FLUID_TANK_IN1.drain(800, IFluidHandler.FluidAction.EXECUTE);
            }
            setChanged(level,blockPos,state);
        }
    }
}
