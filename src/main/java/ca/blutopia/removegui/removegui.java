package ca.blutopia.removegui;

import ca.blutopia.removegui.gui.configScreen;
import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmlclient.registry.ClientRegistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("removegui")
public class removegui
{

    public static final Logger LOGGER = LogManager.getLogger();

    private float paused;
    public int defguiscale;
    KeyMapping keynmap;
    KeyMapping keynmap2;

    public removegui() {

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientsetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModConfigHolder.COMMON_SPEC);
        MinecraftForge.EVENT_BUS.register(this);

    }

    private void clientsetup(final FMLClientSetupEvent event) {
        keynmap = new KeyMapping("Toggle Mod", 296, "Remove Gui But not Hand");
        keynmap2 = new KeyMapping("Open Settings", 297, "Remove Gui But not Hand");
        ClientRegistry.registerKeyBinding(keynmap);
        ClientRegistry.registerKeyBinding(keynmap2);
    }

    private void setup(final FMLCommonSetupEvent event) {

        LOGGER.info("GUI HAVENT BEENETH REMOVEETH");
        defguiscale = Minecraft.getInstance().options.guiScale;
    }

    @SubscribeEvent
    public void MouseOver(DrawSelectionEvent event) {

        if (!ModConfigHolder.COMMON.highlightBlocks.get()) {
            event.setCanceled(true);
        }

    }

    @SubscribeEvent
    public void RenderHandEvent(RenderHandEvent event) {
        event.setCanceled(ModConfigHolder.COMMON.removeHand.get());
    }

    @SubscribeEvent
    public void GuiScreen(GuiScreenEvent event) {

        Window window = Minecraft.getInstance().getWindow();


        if (ModConfigHolder.COMMON.toggleMod.get()) {

                Screen gui = event.getGui();

                if (gui != null) {

                    if (window.getGuiScale() == 0.0) {

                        try {
                            window.setGuiScale(defguiscale);
                            gui.changeFocus(true);
                            if (gui.getTitle().getString().equals("Chat screen")) {
                                // just placeholder code, i have no idea what this does.
                                gui.setBlitOffset(2);
                            } else {
                                // This fixed an error where when the gui scale was restored to its original value, thegui would still be invisible.
                                // gui.resize(Minecraft.getInstance(), window.getWidth()/Minecraft.getInstance().options.guiScale, window.getHeight()/Minecraft.getInstance().options.guiScale);
                                gui.resize(Minecraft.getInstance(), window.getWidth()/defguiscale, window.getHeight()/defguiscale);
                            }

                        } catch (Error err) {
                            LOGGER.error(err);
                        }

                    }
                    paused = 0.4f;
                }
        }

    }

    @SubscribeEvent
    public void ClientTick(TickEvent.ClientTickEvent event) {

        Window window = Minecraft.getInstance().getWindow();

        if (paused > 0) {
            paused -= 0.1f;
        }


        if (event.side.isClient()) {

            if (ModConfigHolder.COMMON.toggleMod.get() && paused <= 0) {
                if (window.getGuiScale() > 0.0 ) {

                    window.setGuiScale(0.0);
                    paused = 0;

                }
            } else {
                window.setGuiScale(defguiscale);
            }
        }


    }

    @SubscribeEvent
    public void keydown(InputEvent.KeyInputEvent event) {

        int action = event.getAction();
        int key = event.getKey();

        if (key == keynmap2.getKey().getValue()) {
            if (action == 0) {
                Minecraft.getInstance().setScreen(new configScreen(this));
            }
        }

        if (key == keynmap.getKey().getValue()) {
            if (action == 0) {

                if (!ModConfigHolder.COMMON.toggleMod.get()) {
                    ModConfigHolder.COMMON.toggleMod.set(true);
                } else {

                    ModConfigHolder.COMMON.toggleMod.set(false);
                }

            }
        }
    }

}