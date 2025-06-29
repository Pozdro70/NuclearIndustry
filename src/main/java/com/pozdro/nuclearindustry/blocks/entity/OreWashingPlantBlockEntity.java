package com.pozdro.nuclearindustry.blocks.entity;

import com.pozdro.nuclearindustry.blocks.custom.OreWashingPlantBlock;
import com.pozdro.nuclearindustry.fluid.sodiumhydroxidesolution.ModFluidsSodiumHydroxideSolution;
import com.pozdro.nuclearindustry.items.ModItems;
import com.pozdro.nuclearindustry.networking.ModMessages;
import com.pozdro.nuclearindustry.networking.packet.OreWashingPlantEnergySyncS2CPacket;
import com.pozdro.nuclearindustry.networking.packet.OreWashingPlantFluidSyncS2CPacket;
import com.pozdro.nuclearindustry.screen.OreWashingPlantMenu;
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
import java.util.Optional;

public class OreWashingPlantBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(6)//size of inv
    {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }


        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {

            return switch (slot){
                case 0 -> true;
                case 1 -> true;
                case 2 -> true;
                case 3 -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
                case 4 -> false;
                case 5 -> false;
                default -> super.isItemValid(slot,stack);
            };
        }
    };

    private final ModEnergyStorage ENERGY_STORAGE = new ModEnergyStorage(100000,270) {
        @Override
        public void onEnergyChanged() {
            setChanged();

            ModMessages.sendToClients(new OreWashingPlantEnergySyncS2CPacket(this.energy,getBlockPos()));
        }
    };
    private static final int ENERGY_REQ = 100;

    private final FluidTank FLUID_TANK_IN = new FluidTank(64000){
        @Override
        protected void onContentsChanged() {
            setChanged();

            if(!level.isClientSide()){
                ModMessages.sendToClients(new OreWashingPlantFluidSyncS2CPacket(FLUID_TANK_IN.getFluid(),worldPosition));
            }

        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            //return stack.getFluid().is(ModTags.Fluids.OREWASHINGPLANT_INP_FLUIDS);
            return true;
        }

    };

    public  void setFluidInINTank(FluidStack stack){
        this.FLUID_TANK_IN.setFluid(stack);
    }
    public FluidStack getFluidStackInINTank(){
        return FLUID_TANK_IN.getFluid();
    }
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(
                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 3, (i, s) -> false)),

                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 2,
                            (index, stack) -> itemHandler.isItemValid(2, stack))),

                    Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 1, (i, s) -> false)),

                    Direction.UP, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> itemHandler.isItemValid(0, stack))));


    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyFluidHandlerIN = LazyOptional.empty();

    protected final ContainerData data;
    private int progress=0;
    private int maxProgress=200;

    public OreWashingPlantBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ORE_WASHING_PLANT.get(),pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> OreWashingPlantBlockEntity.this.progress;
                    case 1 -> OreWashingPlantBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> OreWashingPlantBlockEntity.this.progress = value;
                    case 1 -> OreWashingPlantBlockEntity.this.maxProgress = value;
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
        return Component.literal("Ore Washing Plant");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        ModMessages.sendToClients(new OreWashingPlantFluidSyncS2CPacket(FLUID_TANK_IN.getFluid(),worldPosition));
        ModMessages.sendToClients(new OreWashingPlantEnergySyncS2CPacket(this.ENERGY_STORAGE.getEnergyStored(),getBlockPos()));

        return new OreWashingPlantMenu(id, inventory, this, this.data);
    }

    public IEnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    public void setEnergyLevel(int energy) {
        this.ENERGY_STORAGE.setEnergy(energy);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }

        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if(side == null) {
                return lazyItemHandler.cast();
            }

            if(directionWrappedHandlerMap.containsKey(side)) {
                Direction localDir = this.getBlockState().getValue(OreWashingPlantBlock.FACING);

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
            if (side == Direction.SOUTH) {
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
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
        lazyFluidHandlerIN.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory",itemHandler.serializeNBT());
        tag.putInt("orewashingplant.progress",this.progress);
        tag.putInt("orewashingplant.energy",ENERGY_STORAGE.getEnergyStored());
        tag.put("orewashingplant.InTank", FLUID_TANK_IN.writeToNBT(new CompoundTag()));
        super.saveAdditional(tag);
    }



    @Override
    public void load(CompoundTag tag) {
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("orewashingplant.progress");
        ENERGY_STORAGE.setEnergy(tag.getInt("orewashingplant.energy"));

        if (tag.contains("orewashingplant.InTank")) {
            FLUID_TANK_IN.readFromNBT(tag.getCompound("orewashingplant.InTank"));
        }



        super.load(tag);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
    public static void tick(Level level, BlockPos blockPos, BlockState state, OreWashingPlantBlockEntity pEntity) {
        if (level.isClientSide()) {
            return;
        }
        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());

        if(pEntity.itemHandler.getStackInSlot(3).getCount() >0){

            pEntity.itemHandler.getStackInSlot(3).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler->{
                int drainAmount = Math.min(pEntity.FLUID_TANK_IN.getSpace(), 1000);
                FluidStack stack = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
                if(pEntity.FLUID_TANK_IN.isFluidValid(stack)){
                    stack = handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);

                    pEntity.FLUID_TANK_IN.fill(stack, IFluidHandler.FluidAction.EXECUTE);
                    pEntity.itemHandler.extractItem(3,1,false);
                    pEntity.itemHandler.insertItem(3,handler.getContainer(),false);
                }
            });
        }

        if(
            pEntity.itemHandler.getStackInSlot(0).getItem() == ModItems.RAW_SYLVINITE.get()&&
            (pEntity.itemHandler.getStackInSlot(4).getItem() == ModItems.REFINED_SYLVINITE.get() ||pEntity.itemHandler.getStackInSlot(4).isEmpty())&&
            (pEntity.itemHandler.getStackInSlot(4).getCount() < pEntity.itemHandler.getStackInSlot(4).getMaxStackSize() ||
                    pEntity.itemHandler.getStackInSlot(4).isEmpty())&&
            pEntity.getEnergyStorage().getEnergyStored() >= ENERGY_REQ*pEntity.maxProgress&&
            pEntity.FLUID_TANK_IN.getFluid().getFluid()==ModFluidsSodiumHydroxideSolution.SOURCE_SODIUMHYDROXIDESOLUTION.get()&&
            pEntity.FLUID_TANK_IN.getFluidAmount()>=200
        )
        {
            if(pEntity.progress<pEntity.maxProgress){
                pEntity.progress++;
                pEntity.getEnergyStorage().extractEnergy(ENERGY_REQ,false);
            }
            else {
                pEntity.progress=0;
                pEntity.FLUID_TANK_IN.drain(200, IFluidHandler.FluidAction.EXECUTE);
                pEntity.itemHandler.setStackInSlot(0,new ItemStack(pEntity.itemHandler.getStackInSlot(0).getItem(),
                        pEntity.itemHandler.getStackInSlot(0).getCount()-1));
                pEntity.itemHandler.setStackInSlot(4,new ItemStack(ModItems.REFINED_SYLVINITE.get(),pEntity.itemHandler.getStackInSlot(4).getCount()+1));
            }
            setChanged(pEntity.level,pEntity.getBlockPos(),pEntity.getBlockState());
        }
        else if(
                pEntity.itemHandler.getStackInSlot(1).getItem() == ModItems.RAW_SYLVINITE.get()&&
                        (pEntity.itemHandler.getStackInSlot(5).getItem() == ModItems.REFINED_SYLVINITE.get() ||pEntity.itemHandler.getStackInSlot(5).isEmpty())&&
                        (pEntity.itemHandler.getStackInSlot(5).getCount() < pEntity.itemHandler.getStackInSlot(5).getMaxStackSize() ||
                                pEntity.itemHandler.getStackInSlot(5).isEmpty())&&
                        pEntity.getEnergyStorage().getEnergyStored() >= ENERGY_REQ*pEntity.maxProgress&&
                        pEntity.FLUID_TANK_IN.getFluid().getFluid()==ModFluidsSodiumHydroxideSolution.SOURCE_SODIUMHYDROXIDESOLUTION.get()&&
                        pEntity.FLUID_TANK_IN.getFluidAmount()>=200
        )
        {
            if(pEntity.progress<pEntity.maxProgress){
                pEntity.progress++;
                pEntity.getEnergyStorage().extractEnergy(ENERGY_REQ,false);
            }
            else {
                pEntity.progress=0;
                pEntity.FLUID_TANK_IN.drain(200, IFluidHandler.FluidAction.EXECUTE);
                pEntity.itemHandler.setStackInSlot(1,new ItemStack(pEntity.itemHandler.getStackInSlot(0).getItem(),
                        pEntity.itemHandler.getStackInSlot(1).getCount()-1));
                pEntity.itemHandler.setStackInSlot(5,new ItemStack(ModItems.REFINED_SYLVINITE.get(),pEntity.itemHandler.getStackInSlot(5).getCount()+1));
            }
            setChanged(pEntity.level,pEntity.getBlockPos(),pEntity.getBlockState());
        }
        else {
            pEntity.progress=0;
        }

    }
}
