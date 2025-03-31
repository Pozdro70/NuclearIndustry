package com.pozdro.nuclearindustry.blocks.entity;

import com.pozdro.nuclearindustry.blocks.custom.AbsorberBlock;
import com.pozdro.nuclearindustry.blocks.custom.ElechamberBlock;
import com.pozdro.nuclearindustry.fluid.chlorine.ModFluidsChloride;
import com.pozdro.nuclearindustry.fluid.heavywater.ModFluidsHeavyWater;
import com.pozdro.nuclearindustry.fluid.hydrochloricacid.ModFluidsHydrochloricAcid;
import com.pozdro.nuclearindustry.fluid.hydrogen.ModFluidsHydrogen;
import com.pozdro.nuclearindustry.fluid.hydrogenchloride.ModFluidsHydrogenChloride;
import com.pozdro.nuclearindustry.fluid.purifiedwater.ModFluidsPurifiedWater;
import com.pozdro.nuclearindustry.items.ModItems;
import com.pozdro.nuclearindustry.networking.ModMessages;
import com.pozdro.nuclearindustry.networking.packet.AbsorberEnergySyncS2CPacket;
import com.pozdro.nuclearindustry.networking.packet.AbsorberFluidSyncS2CPacket;
import com.pozdro.nuclearindustry.networking.packet.ElechamberEnergySyncS2CPacket;
import com.pozdro.nuclearindustry.networking.packet.ElechamberFluidSyncS2CPacket;
import com.pozdro.nuclearindustry.recipe.AbsorberRecipe;
import com.pozdro.nuclearindustry.recipe.GasOvenRecipe;
import com.pozdro.nuclearindustry.screen.AbsorberMenu;
import com.pozdro.nuclearindustry.screen.ElechamberMenu;
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

public class AbsorberBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(3)//size of inv
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
                case 2 -> false;
                default -> super.isItemValid(slot,stack);
            };
        }
    };

    private final ModEnergyStorage ENERGY_STORAGE = new ModEnergyStorage(100000,270) {
        @Override
        public void onEnergyChanged() {
            setChanged();

            ModMessages.sendToClients(new AbsorberEnergySyncS2CPacket(this.energy,getBlockPos()));
        }
    };
    private static final int ENERGY_REQ = 100;


    private final FluidTank FLUID_TANK_IN = new FluidTank(64000){
        @Override
        protected void onContentsChanged() {
            setChanged();

            if(!level.isClientSide()){
                ModMessages.sendToClients(new AbsorberFluidSyncS2CPacket(FLUID_TANK_IN.getFluid(),FLUID_TANK_ABSORBATION.getFluid(),FLUID_TANK_OUT.getFluid(),worldPosition));
            }

        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid().is(ModTags.Fluids.ABSORBER_INP_FLUIDS);
        }

    };

    private final FluidTank FLUID_TANK_ABSORBATION = new FluidTank(64000){
        @Override
        protected void onContentsChanged() {
            setChanged();

            if(!level.isClientSide()){
                ModMessages.sendToClients(new AbsorberFluidSyncS2CPacket(FLUID_TANK_IN.getFluid(),FLUID_TANK_ABSORBATION.getFluid(),FLUID_TANK_OUT.getFluid(),worldPosition));
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid().is(ModTags.Fluids.ABSORBER_ABS_FLUIDS);
        }

    };

    private final FluidTank FLUID_TANK_OUT = new FluidTank(64000){
        @Override
        protected void onContentsChanged() {
            setChanged();

            if(!level.isClientSide()){
                ModMessages.sendToClients(new AbsorberFluidSyncS2CPacket(FLUID_TANK_IN.getFluid(),FLUID_TANK_ABSORBATION.getFluid(),FLUID_TANK_OUT.getFluid(),worldPosition));
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
    public  void setFluidInAbsTank(FluidStack stack){
        this.FLUID_TANK_ABSORBATION.setFluid(stack);
    }

    public  void setFluidInOUTTank(FluidStack stack){
        this.FLUID_TANK_OUT.setFluid(stack);
    }

    public FluidStack getFluidStackInINTank(){
        return FLUID_TANK_IN.getFluid();
    }
    public FluidStack getFluidStackInAbsTank(){
        return FLUID_TANK_ABSORBATION.getFluid();
    }

    public FluidStack getFluidStackInOUTTank(){
        return FLUID_TANK_OUT.getFluid();
    }

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 1, (i, s) -> false)));


    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyFluidHandlerIN = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyFluidHandlerAbs = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyFluidHandlerOUT = LazyOptional.empty();

    protected final ContainerData data;
    private int progress=0;
    private int maxProgress=200;

    public AbsorberBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ABSORBER.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> AbsorberBlockEntity.this.progress;
                    case 1 -> AbsorberBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> AbsorberBlockEntity.this.progress = value;
                    case 1 -> AbsorberBlockEntity.this.maxProgress = value;
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
        return Component.literal("Absorber");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        ModMessages.sendToClients(new AbsorberFluidSyncS2CPacket(FLUID_TANK_IN.getFluid(),FLUID_TANK_ABSORBATION.getFluid(),FLUID_TANK_OUT.getFluid(),worldPosition));
        ModMessages.sendToClients(new AbsorberEnergySyncS2CPacket(this.ENERGY_STORAGE.getEnergyStored(),getBlockPos()));

        return new AbsorberMenu(id, inventory, this, this.data);
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
                Direction localDir = this.getBlockState().getValue(ElechamberBlock.FACING);

                return switch (localDir) {
                    default -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
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
            else if (side == Direction.UP) {
                return  lazyFluidHandlerAbs.cast();
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
        lazyFluidHandlerAbs = LazyOptional.of(()->FLUID_TANK_ABSORBATION);
        lazyFluidHandlerOUT= LazyOptional.of(()->FLUID_TANK_OUT);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
        lazyFluidHandlerIN.invalidate();
        lazyFluidHandlerAbs.invalidate();
        lazyFluidHandlerOUT.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory",itemHandler.serializeNBT());
        tag.putInt("elechamber.progress",this.progress);
        tag.putInt("elechamber.energy",ENERGY_STORAGE.getEnergyStored());
        tag.put("elechamber.InTank", FLUID_TANK_IN.writeToNBT(new CompoundTag()));
        tag.put("elechamber.AbsTank", FLUID_TANK_ABSORBATION.writeToNBT(new CompoundTag()));
        tag.put("elechamber.OutTank", FLUID_TANK_OUT.writeToNBT(new CompoundTag()));
        super.saveAdditional(tag);
    }



    @Override
    public void load(CompoundTag tag) {
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("elechamber.progress");
        ENERGY_STORAGE.setEnergy(tag.getInt("elechamber.energy"));

        if (tag.contains("elechamber.InTank")) {
            FLUID_TANK_IN.readFromNBT(tag.getCompound("elechamber.InTank"));
        }
        if (tag.contains("elechamber.OutTank")) {
            FLUID_TANK_OUT.readFromNBT(tag.getCompound("elechamber.OutTank"));
        }
        if (tag.contains("elechamber.AbsTank")) {
            FLUID_TANK_ABSORBATION.readFromNBT(tag.getCompound("elechamber.AbsTank"));
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


    public static void tick(Level level, BlockPos blockPos, BlockState state,AbsorberBlockEntity pEntity) {

        if(level.isClientSide()){
            return;
        }

        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        Optional<AbsorberRecipe> recipe = level.getRecipeManager().getRecipeFor(AbsorberRecipe.Type.INSTANCE,inventory,level);

        if(hasCorrectRecipe(pEntity)&& hasEnoughEnergy(pEntity)){
            pEntity.progress++;
            pEntity.ENERGY_STORAGE.extractEnergy(ENERGY_REQ,false);
            setChanged(level,blockPos,state);

            if(pEntity.progress >= pEntity.maxProgress){
                craftItem(pEntity);pEntity.resetProgress();
            }
        }else if (hasJSONrecipe(recipe,inventory,pEntity))
        {
            if(!recipe.get().getId().toString().equals("nuclearindustry:non_e")){
                pEntity.FLUID_TANK_IN.drain(recipe.get().getFluidStackIN().getAmount(), IFluidHandler.FluidAction.EXECUTE);
                pEntity.FLUID_TANK_ABSORBATION.drain(recipe.get().getFluidStackIN().getAmount(), IFluidHandler.FluidAction.EXECUTE);
                pEntity.itemHandler.extractItem(2,1,false);
                pEntity.itemHandler.setStackInSlot(3,new ItemStack(recipe.get().getResultItem().getItem(),
                        pEntity.itemHandler.getStackInSlot(3).getCount()+1));
                pEntity.FLUID_TANK_OUT.fill(new FluidStack(recipe.get().getFluidStackOUT().getFluid(),recipe.get()
                                .getFluidStackOUT().getAmount()),
                        IFluidHandler.FluidAction.EXECUTE);

                pEntity.resetProgress();
            }


        } else{
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

        if(pEntity.itemHandler.getStackInSlot(1).getCount() >0){
            pEntity.itemHandler.getStackInSlot(1).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler->{
                int drainAmount = Math.min(pEntity.FLUID_TANK_ABSORBATION.getSpace(), 1000);
                FluidStack stack = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
                if(pEntity.FLUID_TANK_ABSORBATION.isFluidValid(stack)){
                    stack = handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);

                    pEntity.FLUID_TANK_ABSORBATION.fill(stack, IFluidHandler.FluidAction.EXECUTE);
                    pEntity.itemHandler.extractItem(1,1,false);
                    pEntity.itemHandler.insertItem(1,handler.getContainer(),false);
                }
            });
        }
    }

    private static boolean hasJSONrecipe(Optional<AbsorberRecipe> recipe, SimpleContainer inventory, AbsorberBlockEntity entity){
        return recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) &&
                canInsertItemIntoOutputSlot(inventory,recipe.get().getResultItem())&&
                recipe.get().getFluidStackIN().equals(entity.FLUID_TANK_IN.getFluid())&&
                recipe.get().getFluidStackABS().equals(entity.FLUID_TANK_ABSORBATION.getFluid())
                &&canInsertFluid(recipe,entity)
                &&entity.FLUID_TANK_IN.getFluidAmount() >= recipe.get().getFluidStackIN().getAmount()
                &&entity.FLUID_TANK_ABSORBATION.getFluidAmount() >= recipe.get().getFluidStackABS().getAmount();
    }


    private static boolean hasEnoughEnergy(AbsorberBlockEntity pEntity) {
        return pEntity.ENERGY_STORAGE.getEnergyStored() >= ENERGY_REQ * pEntity.maxProgress;
    }

    private static void craftItem(AbsorberBlockEntity pEntity) {


        pEntity.FLUID_TANK_IN.drain(1600, IFluidHandler.FluidAction.EXECUTE);
        pEntity.FLUID_TANK_OUT.fill(new FluidStack(ModFluidsHydrochloricAcid.SOURCE_HYDROCHLORICACID.get(), 800),
                IFluidHandler.FluidAction.EXECUTE);

        if(pEntity.ENERGY_STORAGE.getEnergyStored()<100000-6500){
            pEntity.ENERGY_STORAGE.receiveEnergy(6500,false);
        }

        pEntity.resetProgress();
    }

    private static boolean hasCorrectRecipe(AbsorberBlockEntity entity) {
        return ModFluidsHydrogenChloride.SOURCE_HYDROGENCHLORIDE.get()==entity.FLUID_TANK_IN.getFluid().getFluid() && ModFluidsPurifiedWater.SOURCE_PURIFIEDWATER.get()==entity.FLUID_TANK_ABSORBATION.getFluid().getFluid() && (entity.FLUID_TANK_OUT.isEmpty() || entity.FLUID_TANK_OUT.getFluid()
                .getAmount() <= 64000 - 800 && entity.FLUID_TANK_IN.getFluidAmount() >= 1600 && entity.FLUID_TANK_ABSORBATION.getFluidAmount() >= 1600);

    }

    private static boolean canInsertFluid(Optional<AbsorberRecipe> recipe, AbsorberBlockEntity entity){
        return entity.FLUID_TANK_OUT.isEmpty() || entity.FLUID_TANK_OUT.getFluid()
                .getAmount() <=64000-recipe.get().getFluidStackOUT().getAmount();
    }
    private void resetProgress() {
        this.progress=0;
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack itemStack) {
        return inventory.getItem(3).getItem() == itemStack.getItem() || inventory.getItem(3).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(3).getMaxStackSize() >inventory.getItem(3).getCount();
    }


}
