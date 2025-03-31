package com.pozdro.nuclearindustry.blocks.entity;

import com.pozdro.nuclearindustry.blocks.custom.StillBlock;
import com.pozdro.nuclearindustry.fluid.deuterium.ModFluidsDeuterium;
import com.pozdro.nuclearindustry.fluid.heavywater.ModFluidHeavyWater;
import com.pozdro.nuclearindustry.fluid.heavywater.ModFluidsHeavyWater;
import com.pozdro.nuclearindustry.fluid.purifiedwater.ModFluidsPurifiedWater;
import com.pozdro.nuclearindustry.items.ModItems;
import com.pozdro.nuclearindustry.networking.ModMessages;
import com.pozdro.nuclearindustry.networking.packet.StillEnergySyncS2CPacket;
import com.pozdro.nuclearindustry.networking.packet.StillFluidSyncS2CPacket;
import com.pozdro.nuclearindustry.recipe.StillRecipe;
import com.pozdro.nuclearindustry.screen.ModMenuTypes;
import com.pozdro.nuclearindustry.screen.StillMenu;
import com.pozdro.nuclearindustry.util.ModEnergyStorage;
import com.pozdro.nuclearindustry.util.ModTags;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StillBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(3) //size of inv
    {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot){
                case 0 -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
                case 1 -> true;
                case 2 -> false;

                default -> super.isItemValid(slot,stack);
            };
        }
    };

    private final ModEnergyStorage ENERGY_STORAGE = new ModEnergyStorage(70000,260) {
        @Override
        public void onEnergyChanged() {
            setChanged();

            ModMessages.sendToClients(new StillEnergySyncS2CPacket(this.energy,getBlockPos()));

        }
    };

    private static final int ENERGY_REQ = 50;

    private final FluidTank FLUID_TANK_IN = new FluidTank(64000){
        @Override
        protected void onContentsChanged() {
            setChanged();

            if(!level.isClientSide()){
                ModMessages.sendToClients(new StillFluidSyncS2CPacket(FLUID_TANK_IN.getFluid(),
                        FLUID_TANK_OUT.getFluid(),worldPosition));
            }

        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
           return stack.getFluid().is(ModTags.Fluids.STILL_INP_FLUIDS);
        }
    };

    private final FluidTank FLUID_TANK_OUT = new FluidTank(64000){
        @Override
        protected void onContentsChanged() {
            setChanged();

            if(!level.isClientSide()){
                ModMessages.sendToClients(new StillFluidSyncS2CPacket(FLUID_TANK_IN.getFluid(),
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

    public  void setFluidInOUTTank(FluidStack stack){
        this.FLUID_TANK_OUT.setFluid(stack);
    }

    public FluidStack getFluidStackInINTank(){
        return FLUID_TANK_IN.getFluid();
    }

    public FluidStack getFluidStackInOUTTank(){
        return FLUID_TANK_OUT.getFluid();
    }


    private LazyOptional<IFluidHandler> lazyFluidHandlerIN = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyFluidHandlerOUT = LazyOptional.empty();


    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(
                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 2, (i, s) -> false)),

                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 1,
                            (index, stack) -> itemHandler.isItemValid(1, stack))),

                    Direction.UP, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 0,
                            (index, stack) -> itemHandler.isItemValid(0, stack))));

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress =100;

    public StillBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.STILL.get(), pPos, pBlockState);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> StillBlockEntity.this.progress;
                    case 1 -> StillBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> StillBlockEntity.this.progress = value;
                    case 1 -> StillBlockEntity.this.maxProgress = value;
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
        return Component.literal("Still");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        ModMessages.sendToClients(new StillEnergySyncS2CPacket(this.ENERGY_STORAGE.getEnergyStored(),getBlockPos()));
        ModMessages.sendToClients(new StillFluidSyncS2CPacket(FLUID_TANK_IN.getFluid(),FLUID_TANK_OUT.getFluid(),worldPosition));

        return new StillMenu(pContainerId,pPlayerInventory,this,this.data);
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
                Direction localDir = this.getBlockState().getValue(StillBlock.FACING);

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
        lazyFluidHandlerOUT= LazyOptional.of(()->FLUID_TANK_OUT);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
        lazyFluidHandlerIN.invalidate();
        lazyFluidHandlerOUT.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory",itemHandler.serializeNBT());
        pTag.putInt("still.progress",this.progress);
        pTag.putInt("still.energy",ENERGY_STORAGE.getEnergyStored());
        pTag.put("still.InTank", FLUID_TANK_IN.writeToNBT(new CompoundTag()));
        pTag.put("still.OutTank", FLUID_TANK_OUT.writeToNBT(new CompoundTag()));
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress= pTag.getInt("still.progress");
        ENERGY_STORAGE.setEnergy(pTag.getInt("still.energy"));

        if (pTag.contains("still.InTank")) {
            FLUID_TANK_IN.readFromNBT(pTag.getCompound("still.InTank"));
        }
        if (pTag.contains("still.OutTank")) {
            FLUID_TANK_OUT.readFromNBT(pTag.getCompound("still.OutTank"));
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


    public static void tick(Level level, BlockPos blockPos, BlockState state, StillBlockEntity pEntity)
    {
        if(level.isClientSide()){return;}
        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        Optional<StillRecipe> recipe =level.getRecipeManager().getRecipeFor(StillRecipe.Type.INSTANCE, inventory,level);

        if((hasJSONRecipe(recipe,inventory,pEntity)  || hasEnoughEnergy(pEntity) && hasManualRecipes(pEntity))&&canInsertAmountIntoOutputSlot(inventory)){
            pEntity.progress++;
            pEntity.ENERGY_STORAGE.extractEnergy(ENERGY_REQ,false);
            setChanged(level,blockPos,state);
            if(pEntity.progress >= pEntity.maxProgress){
                spawnItemInInv(pEntity);
                pEntity.resetProgress();
            }
        }
        else
        {
            pEntity.resetProgress();
            setChanged(level,blockPos,state);
        }

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
    }

    private static boolean hasManualRecipes(StillBlockEntity pEntity){
        return
                (Fluids.WATER.getFlowing()==(pEntity.FLUID_TANK_IN.getFluid().getFluid()) && pEntity.FLUID_TANK_OUT.isEmpty()
                        || pEntity.FLUID_TANK_OUT.getFluid()
                        .getAmount() <=64000-300 && pEntity.FLUID_TANK_IN.getFluidAmount() >= 1000);
    }


    private static boolean hasEnoughEnergy(StillBlockEntity pEntity) {
        return pEntity.ENERGY_STORAGE.getEnergyStored() >=ENERGY_REQ* pEntity.maxProgress;
    }

    private void resetProgress() {
        this.progress =0;
    }

    private static void spawnItemInInv(StillBlockEntity pEntity) {
        Level level = pEntity.level;
        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        for (int i = 0; i < pEntity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, pEntity.itemHandler.getStackInSlot(i));
        }

        Optional<StillRecipe> recipe =level.getRecipeManager().getRecipeFor(StillRecipe.Type.INSTANCE, inventory,level);

        if(hasJSONRecipe(recipe,inventory,pEntity)) {
            pEntity.FLUID_TANK_IN.drain(recipe.get().getFluidStackIN().getAmount(), IFluidHandler.FluidAction.EXECUTE);
            pEntity.itemHandler.extractItem(1,1,false);
            pEntity.itemHandler.setStackInSlot(2,new ItemStack(recipe.get().getResultItem().getItem(),
                    pEntity.itemHandler.getStackInSlot(2).getCount()+1));
            pEntity.FLUID_TANK_OUT.fill(new FluidStack(recipe.get().getFluidStackOUT().getFluid(),recipe.get()
                            .getFluidStackOUT().getAmount()),
                    IFluidHandler.FluidAction.EXECUTE);

            pEntity.resetProgress();
        }

        if(hasManualRecipes(pEntity)){
            if(pEntity.FLUID_TANK_IN.getFluid().getFluid()== Fluids.WATER && canInsertItemIntoOutputSlot(inventory,new ItemStack(ModItems.SALT.get(),1))&&canInsertAmountIntoOutputSlot(inventory)){
                pEntity.itemHandler.setStackInSlot(2,new ItemStack(ModItems.SALT.get(),
                        pEntity.itemHandler.getStackInSlot(2).getCount()+1));

                pEntity.FLUID_TANK_IN.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                pEntity.FLUID_TANK_OUT.fill(new FluidStack(ModFluidsPurifiedWater.SOURCE_PURIFIEDWATER.get(),500), IFluidHandler.FluidAction.EXECUTE);
            }

            if(pEntity.FLUID_TANK_IN.getFluid().getFluid()== ModFluidsHeavyWater.SOURCE_HEAVYWATER.get()){

                pEntity.FLUID_TANK_IN.drain(1600, IFluidHandler.FluidAction.EXECUTE);
                pEntity.FLUID_TANK_OUT.fill(new FluidStack(ModFluidsDeuterium.SOURCE_DEUTERIUM.get(),1000),
                        IFluidHandler.FluidAction.EXECUTE);
            }


        }



        if ((Fluids.WATER.getFlowing()==(pEntity.FLUID_TANK_IN.getFluid().getFluid()) && (pEntity.FLUID_TANK_OUT.isEmpty()
                || pEntity.FLUID_TANK_OUT.getFluid()
                .getAmount() <=64000-300 && pEntity.FLUID_TANK_IN.getFluidAmount() >= 1000))) {

            pEntity.FLUID_TANK_IN.drain(1000, IFluidHandler.FluidAction.EXECUTE);

            pEntity.FLUID_TANK_OUT.fill(new FluidStack(Fluids.WATER.getSource(),300), //ZMIEN NA CZYSTA WODE!
                    IFluidHandler.FluidAction.EXECUTE);
            pEntity.itemHandler.setStackInSlot(2,new ItemStack(ModItems.SALT.get(),
                    pEntity.itemHandler.getStackInSlot(2).getCount()+1));

            pEntity.resetProgress();
        }

    }

    private static boolean hasJSONRecipe(Optional<StillRecipe> recipe, SimpleContainer inventory, StillBlockEntity entity){
        return recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) &&
                canInsertItemIntoOutputSlot(inventory,recipe.get()
                        .getResultItem())&&recipe.get().getFluidStackIN()
                .equals(entity.FLUID_TANK_IN.getFluid())&&canInsertFluid(recipe,entity)&&entity.FLUID_TANK_IN.getFluidAmount() >= recipe.get()
                .getFluidStackIN().getAmount();
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack itemStack) {
        return inventory.getItem(2).getItem() == itemStack.getItem() || inventory.getItem(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return  inventory.getItem(2).getMaxStackSize() > inventory.getItem(2).getCount();
    }

    public IEnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    public void setEnergyLevel(int energy) {
        this.ENERGY_STORAGE.setEnergy(energy);
    }


    private static boolean canInsertFluid(Optional<StillRecipe> recipe,StillBlockEntity entity){
        return (entity.FLUID_TANK_OUT.isEmpty() || entity.FLUID_TANK_OUT.getFluid()
                .getAmount() <=64000-recipe.get().getFluidStackOUT().getAmount());
    }

}
