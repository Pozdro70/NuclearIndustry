package com.pozdro.nuclearindustry.blocks.entity;

import com.pozdro.nuclearindustry.blocks.custom.ElechamberBlock;
import com.pozdro.nuclearindustry.fluid.chlorine.ModFluidsChloride;
import com.pozdro.nuclearindustry.fluid.hydrochloricacid.ModFluidsHydrochloricAcid;
import com.pozdro.nuclearindustry.fluid.hydrogen.ModFluidsHydrogen;
import com.pozdro.nuclearindustry.fluid.hydrogenchloride.ModFluidsHydrogenChloride;
import com.pozdro.nuclearindustry.fluid.sulfuricacid.ModFluidsSulfuricAcid;
import com.pozdro.nuclearindustry.items.ModItems;
import com.pozdro.nuclearindustry.networking.ModMessages;
import com.pozdro.nuclearindustry.networking.packet.GasOvenEnergySyncS2CPacket;
import com.pozdro.nuclearindustry.networking.packet.GasOvenFluidSyncS2CPacket;
import com.pozdro.nuclearindustry.recipe.GasOvenRecipe;
import com.pozdro.nuclearindustry.screen.GasOvenMenu;
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

public class GasOvenBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(4)//size of inv
    {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }


        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {

            return switch (slot) {
                case 0 -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
                case 1 -> stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
                case 2 -> true;
                case 3 -> false;
                default -> super.isItemValid(slot, stack);
            };
        }


    };

    private final ModEnergyStorage ENERGY_STORAGE = new ModEnergyStorage(100000, 400) {
        @Override
        public void onEnergyChanged() {
            setChanged();
            ModMessages.sendToClients(new GasOvenEnergySyncS2CPacket(this.energy, getBlockPos()));
        }


    };

    private static final int ENERGY_REQ = 50;

    private final FluidTank FLUID_TANK_IN = new FluidTank(64000) {
        @Override
        protected void onContentsChanged() {
            setChanged();

            if (!level.isClientSide()) {
                ModMessages.sendToClients(new GasOvenFluidSyncS2CPacket(FLUID_TANK_IN.getFluid(), FLUID_TANK_IN1.getFluid(),
                        FLUID_TANK_OUT.getFluid(), worldPosition));
            }

        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid().is(ModTags.Fluids.OVEN_INP_FLUIDS);
        }

    };

    private final FluidTank FLUID_TANK_IN1 = new FluidTank(64000) {
        @Override
        protected void onContentsChanged() {
            setChanged();

            if (!level.isClientSide()) {
                ModMessages.sendToClients(new GasOvenFluidSyncS2CPacket(FLUID_TANK_IN.getFluid(), FLUID_TANK_IN1.getFluid(),
                        FLUID_TANK_OUT.getFluid(), worldPosition));
            }

        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid().is(ModTags.Fluids.OVEN_INP2_FLUIDS);
        }

    };

    private final FluidTank FLUID_TANK_OUT = new FluidTank(64000) {
        @Override
        protected void onContentsChanged() {
            setChanged();

            if (!level.isClientSide()) {
                ModMessages.sendToClients(new GasOvenFluidSyncS2CPacket(FLUID_TANK_IN.getFluid(), FLUID_TANK_IN1.getFluid(),
                        FLUID_TANK_OUT.getFluid(), worldPosition));
            }
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return true;
        }

    };

    public void setFluidInINTank(FluidStack stack) {
        this.FLUID_TANK_IN.setFluid(stack);
    }

    public void setFluidInIN1Tank(FluidStack stack) {
        this.FLUID_TANK_IN1.setFluid(stack);
    }

    public void setFluidInOUTTank(FluidStack stack) {
        this.FLUID_TANK_OUT.setFluid(stack);
    }

    public FluidStack getFluidStackInINTank() {
        return FLUID_TANK_IN.getFluid();
    }

    public FluidStack getFluidStackInIN1Tank() {
        return FLUID_TANK_IN1.getFluid();
    }

    public FluidStack getFluidStackInOUTTank() {
        return FLUID_TANK_OUT.getFluid();
    }

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            Map.of(
                    Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (i) -> i == 3, (i, s) -> false)),

                    Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemHandler, (index) -> index == 2,
                            (index, stack) -> itemHandler.isItemValid(2, stack))));


    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyFluidHandlerIN = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyFluidHandlerIN1 = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyFluidHandlerOUT = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200;

    public GasOvenBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GAS_OVEN.get(), pPos, pBlockState);

        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> GasOvenBlockEntity.this.progress;
                    case 1 -> GasOvenBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> GasOvenBlockEntity.this.progress = value;
                    case 1 -> GasOvenBlockEntity.this.maxProgress = value;
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
        return Component.literal("Gas Oven");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        ModMessages.sendToClients(new GasOvenFluidSyncS2CPacket(FLUID_TANK_IN.getFluid(), FLUID_TANK_IN1.getFluid(),
                FLUID_TANK_OUT.getFluid(), worldPosition));
        ModMessages.sendToClients(new GasOvenEnergySyncS2CPacket(this.ENERGY_STORAGE.getEnergyStored(), getBlockPos()));
        return new GasOvenMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    public IEnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    public void setEnergyLevel(int energy) {
        this.ENERGY_STORAGE.setEnergy(energy);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

        if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }

        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return lazyItemHandler.cast();
            }

            if (directionWrappedHandlerMap.containsKey(side)) {
                Direction localDir = this.getBlockState().getValue(ElechamberBlock.FACING);

                return switch (localDir) {
                    default -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
                    case EAST -> directionWrappedHandlerMap.get(side.getClockWise()).cast();
                    case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
                };
            }
        }

        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            if (side == Direction.NORTH) {
                return lazyFluidHandlerOUT.cast();
            } else if (side == Direction.SOUTH) {
                return lazyFluidHandlerIN.cast();
            } else if (side == Direction.UP) {
                return lazyFluidHandlerIN1.cast();
            }
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
        lazyFluidHandlerIN = LazyOptional.of(() -> FLUID_TANK_IN);
        lazyFluidHandlerIN1 = LazyOptional.of(() -> FLUID_TANK_IN1);
        lazyFluidHandlerOUT = LazyOptional.of(() -> FLUID_TANK_OUT);
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
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("gasoven.progress", this.progress);
        tag.putInt("gasoven.energy", ENERGY_STORAGE.getEnergyStored());
        tag.put("gasoven.InTank", FLUID_TANK_IN.writeToNBT(new CompoundTag()));
        tag.put("gasoven.InTank1", FLUID_TANK_IN1.writeToNBT(new CompoundTag()));
        tag.put("gasoven.OutTank", FLUID_TANK_OUT.writeToNBT(new CompoundTag()));
        super.saveAdditional(tag);
    }


    @Override
    public void load(CompoundTag tag) {
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("gasoven.progress");
        ENERGY_STORAGE.setEnergy(tag.getInt("gasoven.energy"));

        if (tag.contains("gasoven.InTank")) {
            FLUID_TANK_IN.readFromNBT(tag.getCompound("gasoven.InTank"));
        }

        if (tag.contains("gasoven.InTank1")) {
            FLUID_TANK_IN1.readFromNBT(tag.getCompound("gasoven.InTank1"));
        }

        if (tag.contains("gasoven.OutTank")) {
            FLUID_TANK_OUT.readFromNBT(tag.getCompound("gasoven.OutTank"));
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

    public static void AddRecipe(GasOvenBlockEntity pEntity, FluidStack fluidIn1, FluidStack fluidIn2, FluidStack fluidOut, ItemStack itemIn, ItemStack itemOut){
        if(pEntity.getEnergyStorage().getEnergyStored() >= ENERGY_REQ* pEntity.maxProgress &&
                pEntity.FLUID_TANK_OUT.getFluidAmount()+800 < pEntity.FLUID_TANK_OUT.getCapacity() &&
                pEntity.FLUID_TANK_IN.getFluid().equals(fluidIn1) &&
                pEntity.FLUID_TANK_IN1.getFluid().equals(fluidIn2)
                &&      pEntity.itemHandler.getStackInSlot(2).getItem()==itemIn.getItem()&&
                pEntity.itemHandler.getStackInSlot(3).getCount()<itemOut.getItem().getMaxStackSize()&&
                (pEntity.FLUID_TANK_OUT.getFluid().getFluid().equals(fluidOut.getFluid())||pEntity.FLUID_TANK_OUT.isEmpty())
        )
        {
            if(pEntity.progress<pEntity.maxProgress){
                pEntity.progress++;
                pEntity.getEnergyStorage().extractEnergy(ENERGY_REQ,false);
            }
            else{
                pEntity.progress=0;
                pEntity.FLUID_TANK_OUT.fill(fluidOut, IFluidHandler.FluidAction.EXECUTE);

                pEntity.FLUID_TANK_IN.drain(800, IFluidHandler.FluidAction.EXECUTE);
                pEntity.FLUID_TANK_IN1.drain(800, IFluidHandler.FluidAction.EXECUTE);

                pEntity.itemHandler.setStackInSlot(2,new ItemStack(itemIn.getItem(),pEntity.itemHandler.getStackInSlot(2).getCount()-1));
                pEntity.itemHandler.setStackInSlot(3,new ItemStack(itemOut.getItem(),pEntity.itemHandler.getStackInSlot(3).getCount()+1));

            }
            setChanged(pEntity.level,pEntity.getBlockPos(),pEntity.getBlockState());
        }
        else {
            pEntity.progress=0;
        }
    }
    public static void AddRecipe(GasOvenBlockEntity pEntity, FluidStack fluidIn1, FluidStack fluidIn2, FluidStack fluidOut){
        if(pEntity.getEnergyStorage().getEnergyStored() >= ENERGY_REQ* pEntity.maxProgress &&
                pEntity.FLUID_TANK_OUT.getFluidAmount()+800 < pEntity.FLUID_TANK_OUT.getCapacity() &&
                pEntity.FLUID_TANK_IN.getFluid().equals(fluidIn1) &&
                pEntity.FLUID_TANK_IN1.getFluid().equals(fluidIn2) &&
                (pEntity.FLUID_TANK_OUT.getFluid().getFluid().equals(fluidOut.getFluid())||pEntity.FLUID_TANK_OUT.isEmpty())
        )
        {
            if(pEntity.progress<pEntity.maxProgress){
                pEntity.progress++;
                pEntity.getEnergyStorage().extractEnergy(ENERGY_REQ,false);
            }
            else{
                pEntity.progress=0;
                pEntity.FLUID_TANK_OUT.fill(fluidOut, IFluidHandler.FluidAction.EXECUTE);

                pEntity.FLUID_TANK_IN.drain(800, IFluidHandler.FluidAction.EXECUTE);
                pEntity.FLUID_TANK_IN1.drain(800, IFluidHandler.FluidAction.EXECUTE);

            }
            setChanged(pEntity.level,pEntity.getBlockPos(),pEntity.getBlockState());
        }
        else {
            pEntity.progress=0;
        }
    }

    public static void tick(Level level, BlockPos blockPos, BlockState state, GasOvenBlockEntity pEntity) {
        if (level.isClientSide()) {
            return;
        }
        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        Optional<GasOvenRecipe> recipe = level.getRecipeManager().getRecipeFor(GasOvenRecipe.Type.INSTANCE, inventory, level);


        if (pEntity.itemHandler.getStackInSlot(1).getCount() > 0) {
            pEntity.itemHandler.getStackInSlot(1).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
                int drainAmount = Math.min(pEntity.FLUID_TANK_IN.getSpace(), 1000);
                FluidStack stack = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
                if (pEntity.FLUID_TANK_IN.isFluidValid(stack)) {
                    stack = handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);

                    pEntity.FLUID_TANK_IN.fill(stack, IFluidHandler.FluidAction.EXECUTE);
                    pEntity.itemHandler.extractItem(1, 1, false);
                    pEntity.itemHandler.insertItem(1, handler.getContainer(), false);
                }
            });
        }

        if (pEntity.itemHandler.getStackInSlot(0).getCount() > 0) {
            pEntity.itemHandler.getStackInSlot(0).getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> {
                int drainAmount = Math.min(pEntity.FLUID_TANK_IN1.getSpace(), 1000);
                FluidStack stack = handler.drain(drainAmount, IFluidHandler.FluidAction.SIMULATE);
                if (pEntity.FLUID_TANK_IN1.isFluidValid(stack)) {
                    stack = handler.drain(drainAmount, IFluidHandler.FluidAction.EXECUTE);

                    pEntity.FLUID_TANK_IN1.fill(stack, IFluidHandler.FluidAction.EXECUTE);
                    pEntity.itemHandler.extractItem(0, 1, false);
                    pEntity.itemHandler.insertItem(0, handler.getContainer(), false);
                }
            });
        }

        //funkcjonalność

        //LITHIUM CARBONATE RECIPE
        AddRecipe(pEntity,
                new FluidStack(ModFluidsSulfuricAcid.SOURCE_SULFURICACID.get(),1600), //INPUT FLUID
                new FluidStack(ModFluidsSulfuricAcid.SOURCE_SULFURICACID.get(),1600),//INPUT1 FLUID
                new FluidStack(ModFluidsSulfuricAcid.SOURCE_SULFURICACID.get(),800),//OUTPUT FLUID
                new ItemStack(ModItems.SPODUMENE.get(),1),//INPUT ITEM
                new ItemStack(ModItems.LITHIUM_CARBONATE.get(),1)//OUTPUT ITEM
        );

        //HYDROGEN CHLORIDE RECIPE
        AddRecipe(pEntity,
                new FluidStack(ModFluidsHydrogen.SOURCE_HYDROGEN.get(),800),
                new FluidStack(ModFluidsChloride.SOURCE_CHLORIDE.get(),800),
                new FluidStack(ModFluidsHydrogenChloride.SOURCE_HYDROGENCHLORIDE.get(),1600)
        );

        //LITHIUM CHLORIDE RECIPE
        AddRecipe(pEntity,
                new FluidStack(ModFluidsHydrochloricAcid.SOURCE_HYDROCHLORICACID.get(),1600),
                new FluidStack(ModFluidsHydrochloricAcid.SOURCE_HYDROCHLORICACID.get(),1600),
                new FluidStack(ModFluidsHydrochloricAcid.SOURCE_HYDROCHLORICACID.get(),800),
                new ItemStack(ModItems.LITHIUM_CARBONATE.get(),1),
                new ItemStack(ModItems.LITHIUM_CHLORIDE.get(),1)
        );


    }



    private static boolean hasJSONrecipe(Optional<GasOvenRecipe> recipe, SimpleContainer inventory, GasOvenBlockEntity entity){
        return recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) &&
                canInsertItemIntoOutputSlot(inventory,recipe.get().getResultItem())&&
                recipe.get().getFluidStackIN().equals(entity.FLUID_TANK_IN.getFluid())&&
                recipe.get().getFluidStackIN2().equals(entity.FLUID_TANK_IN1.getFluid())
                &&canInsertFluid(recipe,entity)
                &&entity.FLUID_TANK_IN.getFluidAmount() >= recipe.get().getFluidStackIN().getAmount()
                &&entity.FLUID_TANK_IN1.getFluidAmount() >= recipe.get().getFluidStackIN2().getAmount();
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack itemStack) {
        return inventory.getItem(3).getItem() == itemStack.getItem() || inventory.getItem(3).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(3).getMaxStackSize() >inventory.getItem(3).getCount();
    }

    private static boolean canInsertFluid(Optional<GasOvenRecipe> recipe,GasOvenBlockEntity entity){
        return entity.FLUID_TANK_OUT.isEmpty() || entity.FLUID_TANK_OUT.getFluid()
                .getAmount() <=64000-recipe.get().getFluidStackOUT().getAmount();
    }

    private void resetProgress() {
        this.progress=0;
    }
}
