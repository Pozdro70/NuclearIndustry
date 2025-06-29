package com.pozdro.nuclearindustry.blocks.custom;

import com.pozdro.nuclearindustry.blocks.ModBlocks;
import com.pozdro.nuclearindustry.blocks.entity.ModBlockEntities;
import com.pozdro.nuclearindustry.blocks.entity.OreWashingPlantBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class OreWashingPlantBlock extends BaseEntityBlock {
    public OreWashingPlantBlock(Properties pProperties) {
        super(pProperties);
    }

    public static DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE= Block.box(0,0,0,16,16,16);

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    //blok entyty

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof OreWashingPlantBlockEntity) {
                ((OreWashingPlantBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            if(isMultiblockSet(pLevel,pPos)){
                BlockEntity entity = pLevel.getBlockEntity(pPos);
                if(entity instanceof OreWashingPlantBlockEntity) {
                    NetworkHooks.openScreen(((ServerPlayer)pPlayer), (OreWashingPlantBlockEntity)entity, pPos);
                } else {
                    throw new IllegalStateException("Our Container provider is missing!");
                }
            }
            else {
                pPlayer.sendSystemMessage(Component.literal("Multi-Block structure not formed!"));
            }

        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new OreWashingPlantBlockEntity(pPos,pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.ORE_WASHING_PLANT.get(),OreWashingPlantBlockEntity::tick);
    }

    public boolean isMultiblockSet(Level level,BlockPos mainBlockPos){

        /*
        M-MachineHull
        C-Controller
        T-Insulator

          LAYER1        LAYER2       LAYER3       LAYER4
        [M][T][M]     [M][T][M]    [ ][T][ ]    [ ][ ][ ]
        [T][T][T]     [T][ ][T]    [T][T][T]    [ ][C][ ]
        [M][T][M]     [M][T][M]    [ ][T][ ]    [ ][ ][ ]

                   PLAYER
         */

        return (
            //first layer
            level.getBlockState(mainBlockPos.offset(0, -3, 0)).getBlock() == ModBlocks.THERMAL_ISOLATOR.get()&&
            level.getBlockState(mainBlockPos.offset(0, -3, 1)).getBlock() == ModBlocks.THERMAL_ISOLATOR.get()&&
            level.getBlockState(mainBlockPos.offset(0, -3, -1)).getBlock() == ModBlocks.THERMAL_ISOLATOR.get()&&
            level.getBlockState(mainBlockPos.offset(1, -3, 0)).getBlock() == ModBlocks.THERMAL_ISOLATOR.get()&&
            level.getBlockState(mainBlockPos.offset(-1, -3, 0)).getBlock() == ModBlocks.THERMAL_ISOLATOR.get()&&
            level.getBlockState(mainBlockPos.offset(1, -3, 1)).getBlock() == ModBlocks.MACHINE_HULL.get()&&
            level.getBlockState(mainBlockPos.offset(-1, -3, 1)).getBlock() == ModBlocks.MACHINE_HULL.get()&&
            level.getBlockState(mainBlockPos.offset(1, -3, -1)).getBlock() == ModBlocks.MACHINE_HULL.get()&&
            level.getBlockState(mainBlockPos.offset(-1, -3, -1)).getBlock() == ModBlocks.MACHINE_HULL.get()&&

            //second layer
            level.getBlockState(mainBlockPos.offset(0, -2, 1)).getBlock() == ModBlocks.THERMAL_ISOLATOR.get()&&
            level.getBlockState(mainBlockPos.offset(0, -2, -1)).getBlock() == ModBlocks.THERMAL_ISOLATOR.get()&&
            level.getBlockState(mainBlockPos.offset(1, -2, 0)).getBlock() == ModBlocks.THERMAL_ISOLATOR.get()&&
            level.getBlockState(mainBlockPos.offset(-1, -2, 0)).getBlock() == ModBlocks.THERMAL_ISOLATOR.get()&&
            level.getBlockState(mainBlockPos.offset(1, -2, 1)).getBlock() == ModBlocks.MACHINE_HULL.get()&&
            level.getBlockState(mainBlockPos.offset(-1, -2, 1)).getBlock() == ModBlocks.MACHINE_HULL.get()&&
            level.getBlockState(mainBlockPos.offset(1, -2, -1)).getBlock() == ModBlocks.MACHINE_HULL.get()&&
            level.getBlockState(mainBlockPos.offset(-1, -2, -1)).getBlock() == ModBlocks.MACHINE_HULL.get()&&

            //third layer
            level.getBlockState(mainBlockPos.offset(0, -1, 0)).getBlock() == ModBlocks.THERMAL_ISOLATOR.get()&&
            level.getBlockState(mainBlockPos.offset(0, -1, 1)).getBlock() == ModBlocks.THERMAL_ISOLATOR.get()&&
            level.getBlockState(mainBlockPos.offset(0, -1, -1)).getBlock() == ModBlocks.THERMAL_ISOLATOR.get()&&
            level.getBlockState(mainBlockPos.offset(1, -1, 0)).getBlock() == ModBlocks.THERMAL_ISOLATOR.get()&&
            level.getBlockState(mainBlockPos.offset(-1, -1, 0)).getBlock() == ModBlocks.THERMAL_ISOLATOR.get()

        );
    }


}
