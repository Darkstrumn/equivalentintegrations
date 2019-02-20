package com.mike_caron.equivalentintegrations.block;

import com.mike_caron.equivalentintegrations.EquivalentIntegrationsMod;
import com.mike_caron.equivalentintegrations.item.ModItems;
import com.mike_caron.mikesmodslib.block.BlockBase;
import com.mike_caron.mikesmodslib.integrations.ITOPInfoProvider;
import com.mike_caron.mikesmodslib.integrations.IWailaInfoProvider;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class TransmutationBlockBase
    extends BlockBase
        implements ITileEntityProvider, ITOPInfoProvider, IWailaInfoProvider
{
    public TransmutationBlockBase(String id)
    {
        super(Material.IRON, id);
        setHardness(4);
        setHarvestLevel("pickaxe", 1);
        setCreativeTab(EquivalentIntegrationsMod.creativeTab);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }


    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, getDefaultState(), 2);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    @Nonnull
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState();
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if(!worldIn.isRemote)
        {
            TransmutationTileEntityBase te = getTE(worldIn, pos);
            ItemStackHandler inventory = te.getInventory();

            for (int i = 0; i < inventory.getSlots(); ++i)
            {
                ItemStack itemstack = inventory.getStackInSlot(i);

                if (!itemstack.isEmpty())
                {
                    InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), itemstack);
                }
            }
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Nullable
    private TransmutationTileEntityBase getTE(IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity ret = worldIn.getTileEntity(pos);
        if(ret instanceof TransmutationTileEntityBase) return (TransmutationTileEntityBase)ret;
        return null;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        TileEntity te = accessor.getTileEntity();
        TransmutationTileEntityBase tileEntity = null;
        if(te instanceof TransmutationTileEntityBase)
        {
            tileEntity = (TransmutationTileEntityBase)te;
        }

        if(tileEntity != null)
        {
            if(tileEntity.hasOwner())
            {
                currenttip.add(new TextComponentTranslation("item.soulbound_talisman.bound", tileEntity.getOwnerName()).getFormattedText());
            }
        }

        return currenttip;
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data)
    {
        TransmutationTileEntityBase tileEntity = getTE(world, data.getPos());

        if(tileEntity == null) return;

        if(tileEntity.hasOwner())
        {
            probeInfo
                    .horizontal(new LayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                    .item(new ItemStack(ModItems.soulboundTalisman))
                    .text(new TextComponentTranslation("item.soulbound_talisman.bound", tileEntity.getOwnerName()).getFormattedText())
            ;
        }

    }

}
