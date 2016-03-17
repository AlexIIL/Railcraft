/* 
 * Copyright (c) CovertJaguar, 2014 http://railcraft.info
 * 
 * This code is the property of CovertJaguar
 * and may only be used with explicit written
 * permission unless otherwise specified on the
 * license page at http://railcraft.info/wiki/info:license.
 */
package mods.railcraft.common.blocks.signals;

import mods.railcraft.api.core.IPostConnection;
import mods.railcraft.common.core.RailcraftConfig;
import mods.railcraft.common.items.IActivationBlockingItem;
import mods.railcraft.common.plugins.forge.PowerPlugin;
import mods.railcraft.common.util.misc.Game;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

public abstract class BlockSignalBase extends BlockContainer implements IPostConnection {

    private final int renderType;

    public BlockSignalBase(int renderType) {
        super(new MaterialStructure());
        this.renderType = renderType;
        setStepSound(Block.soundTypeMetal);
        setResistance(50);
        setCreativeTab(CreativeTabs.tabTransport);
//        setHarvestLevel("pickaxe", 2);
        setHarvestLevel("crowbar", 0);
    }

    public abstract ISignalTileDefinition getSignalType(int meta);


    @Override
    public int damageDropped(IBlockState state) {
        return meta;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack current = playerIn.getCurrentEquippedItem();
        if (current != null)
            if (current.getItem() instanceof IActivationBlockingItem)
                return false;
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileSignalFoundation)
            return ((TileSignalFoundation) tile).blockActivated(side, playerIn);
        return false;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileSignalFoundation)
            return ((TileSignalFoundation) tile).rotateBlock(axis);
        return false;
    }

    @Override
    public EnumFacing[] getValidRotations(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileSignalFoundation)
            return ((TileSignalFoundation) tile).getValidRotations();
        return super.getValidRotations(world, pos);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (RailcraftConfig.printSignalDebug()) {
            Game.logTrace(Level.INFO, 10, "Signal Block onBlockPlacedBy. [{0}]", pos);
        }
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileSignalFoundation)
            ((TileSignalFoundation) tile).onBlockPlacedBy(placer, stack);
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        try {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TileSignalFoundation) {
                TileSignalFoundation structure = (TileSignalFoundation) tile;
                if (structure.getSignalType().needsSupport() && !worldIn.isSideSolid(pos.down(), EnumFacing.UP))
                    worldIn.func_147480_a(x, y, z, true);
                else
                    structure.onNeighborBlockChange(neighborBlock);
            }
        } catch (StackOverflowError error) {
            Game.logThrowable(Level.ERROR, "Error in BlockSignalBase.onNeighborBlockChange()", 10, error);
            throw error;
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (RailcraftConfig.printSignalDebug()) {
            Game.logTrace(Level.INFO, 10, "Signal Block breakBlock. [{0}]", pos);
        }
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileSignalFoundation)
            ((TileSignalFoundation) tile).onBlockRemoval();
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileSignalFoundation)
            ((TileSignalFoundation) tile).setBlockBoundsBasedOnState(worldIn, pos);
        else
            setBlockBounds(0, 0, 0, 1, 1, 1);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileSignalFoundation)
            return ((TileSignalFoundation) tile).getCollisionBoundingBoxFromPool(worldIn, pos);
        setBlockBounds(0, 0, 0, 1, 1, 1);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileSignalFoundation)
            return ((TileSignalFoundation) tile).getSelectedBoundingBoxFromPool(worldIn, pos);
        return AxisAlignedBB.fromBounds((double) pos.getX() + minX, (double) pos.getY() + minY, (double) pos.getZ() + minZ, (double) pos.getX() + maxX, (double) pos.getY() + maxY, (double) pos.getZ() + maxZ);
    }

    @Override
    public int getLightValue(IBlockAccess world, BlockPos pos) {
        if (pos.getY() < 0)
            return 0;
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof ISignalTile)
            return ((ISignalTile) tile).getLightValue();
        return 0;
    }

    @Override
    public float getBlockHardness(World worldIn, BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileSignalFoundation)
            return ((TileSignalFoundation) tile).getHardness();
        return 3;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileSignalFoundation)
            return ((TileSignalFoundation) tile).isSideSolid(world, pos, side);
        return false;
    }

    //    @Override
//    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side) {
//        int md = world.getBlockMetadata(x, y, z);
//        EnumSignal type = EnumSignal.fromId(md);
//        return super.canPlaceBlockOnSide(world, x, y, z, side) && (!type.needsSupport() || world.isSideSolid(x, y - 1, z, EnumFacing.UP));
//    }
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public int getRenderType() {
        return renderType;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int meta) {
        return null;
    }

    @Override
    public abstract TileEntity createTileEntity(World world, IBlockState state);

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, BlockPos pos, EnumFacing side) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileSignalFoundation)
            return ((TileSignalFoundation) tile).canConnectRedstone(side);
        return false;
    }

    @Override
    public boolean isBlockNormalCube() {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileSignalFoundation)
            return ((TileSignalFoundation) tile).getPowerOutput(side);
        return PowerPlugin.NO_POWER;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canCreatureSpawn(IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
        return false;
    }

    @Override
    public ConnectStyle connectsToPost(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        TileEntity t = world.getTileEntity(x, y, z);
        if (t instanceof ISignalTile)
            return ConnectStyle.TWO_THIN;
        return ConnectStyle.NONE;
    }
}
