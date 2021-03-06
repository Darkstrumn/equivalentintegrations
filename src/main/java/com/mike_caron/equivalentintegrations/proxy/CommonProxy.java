package com.mike_caron.equivalentintegrations.proxy;

import com.mike_caron.equivalentintegrations.EquivalentIntegrationsMod;
import com.mike_caron.equivalentintegrations.item.ModItems;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.SERVER)
public class CommonProxy
    implements IModProxy
{

    //public static Configuration config;

    @SuppressWarnings("EmptyMethod")
    public void preInit(FMLPreInitializationEvent e)
    {
        //File directory = e.getModConfigurationDirectory();
        //config = new Configuration(new File(directory.getPath(), "equivalentintegrations.cfg"));
        //ModConfig.readConfig();
    }

    public void init(FMLInitializationEvent e)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(EquivalentIntegrationsMod.instance, new GuiProxy());
        //CapabilityManager.INSTANCE.register(IEMCManager.class, new DummyIStorage<>(), new ManagedEMCManager.Factory());

        ModItems.registerEvents();
    }

    @SuppressWarnings("EmptyMethod")
    public void postInit(FMLPostInitializationEvent e)
    {
        //if(config.hasChanged())
        //{
        //    config.save();
        //}
    }

}
