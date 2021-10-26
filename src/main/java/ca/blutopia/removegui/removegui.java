package ca.blutopia.removegui;

import ca.blutopia.removegui.gui.configScreen;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("removegui")
public class removegui {

    public static final Logger LOGGER = LogManager.getLogger();

    public float paused;
    public int defguiscale;
    KeyBinding keynmap;
    KeyBinding keynmap2;

    public removegui() {

        FMLJavaModLoadingContext jModLoadingContext = FMLJavaModLoadingContext.get();

        jModLoadingContext.getModEventBus().addListener(this::setup);
        jModLoadingContext.getModEventBus().addListener(this::clientsetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModConfigHolder.COMMON_SPEC);
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY,
                () -> (mc, screen) -> new configScreen());
    }

    private void clientsetup(final FMLClientSetupEvent event) {
        keynmap = new KeyBinding("Toggle Mod", 296, "Remove HUD but not Hand");
        keynmap2 = new KeyBinding("Open Settings", 297, "Remove HUD but not Hand");
        ClientRegistry.registerKeyBinding(keynmap2);
        ClientRegistry.registerKeyBinding(keynmap);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("GUI HAVETH BEENETH REMOVETH");
        defguiscale = 3;
    }

    @SubscribeEvent
    public void MouseOver(DrawHighlightEvent event) {
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
        MainWindow window = Minecraft.getInstance().getWindow();

        if (ModConfigHolder.COMMON.toggleMod.get()) {
            Screen gui = event.getGui();

            if (gui != null) {
                if (window.getGuiScale() == 0.0) {
                    try {
                        window.setGuiScale(defguiscale);
                        gui.changeFocus(true);
                        LOGGER.info(gui.getTitle().getString());
                        if (gui.getClass().equals(ChatScreen.class)) {
                            gui.setBlitOffset(2);
                        } else {
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
        MainWindow window = Minecraft.getInstance().getWindow();

        if (paused > 0) {
            paused -= 0.1f;
        }

        if (event.side.isClient()) {
            if (ModConfigHolder.COMMON.toggleMod.get() && paused <= 0) {
                if (window.getGuiScale() > 0.0) {
                    window.setGuiScale(0.0);
                    paused = 0;
                }
            } else {
                window.setGuiScale(defguiscale);
            }
        }
    }

    @SubscribeEvent
    public void keyDown(InputEvent.KeyInputEvent event) {
        int action = event.getAction();
        int key = event.getKey();

        if (key == keynmap2.getKey().getValue()) {
            if (action == 0) {
                Minecraft.getInstance().setScreen(new configScreen());
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
