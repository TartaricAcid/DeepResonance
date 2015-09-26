package mcjty.deepresonance.blocks.valve;

import mcjty.container.GenericGuiContainer;
import mcjty.deepresonance.DeepResonance;
import mcjty.gui.Window;
import mcjty.gui.events.TextEvent;
import mcjty.gui.layout.HorizontalLayout;
import mcjty.gui.layout.VerticalLayout;
import mcjty.gui.widgets.Label;
import mcjty.gui.widgets.Panel;
import mcjty.gui.widgets.TextField;
import mcjty.gui.widgets.Widget;
import mcjty.network.Argument;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiValve extends GenericGuiContainer<ValveTileEntity> {
    public static final int VALVE_WIDTH = 180;
    public static final int VALVE_HEIGHT = 152;

    private static final ResourceLocation iconLocation = new ResourceLocation(DeepResonance.MODID, "textures/gui/valve.png");

    private TextField minPurity;
    private TextField minStrength;
    private TextField minEfficiency;

    public GuiValve(ValveTileEntity valveTileEntity, ValveContainer container) {
        super(DeepResonance.instance, DeepResonance.networkHandler.getNetworkWrapper(), valveTileEntity, container, 0, "valve");

        xSize = VALVE_WIDTH;
        ySize = VALVE_HEIGHT;
    }

    @Override
    public void initGui() {
        super.initGui();

        Panel toplevel = new Panel(mc, this).setBackground(iconLocation).setLayout(new VerticalLayout());

        minPurity = new TextField(mc, this).setTooltips("The minimum purity % to", "accept the liquid").addTextEvent(new TextEvent() {
            @Override
            public void textChanged(Widget parent, String newText) {
                updateSettings();
            }
        });
        minStrength = new TextField(mc, this).setTooltips("The minimum strength % to", "accept the liquid").addTextEvent(new TextEvent() {
            @Override
            public void textChanged(Widget parent, String newText) {
                updateSettings();
            }
        });
        minEfficiency = new TextField(mc, this).setTooltips("The minimum efficiency % to", "accept the liquid").addTextEvent(new TextEvent() {
            @Override
            public void textChanged(Widget parent, String newText) {
                updateSettings();
            }
        });
        minPurity.setText(Integer.toString((int) (tileEntity.getMinPurity() * 100)));
        minStrength.setText(Integer.toString((int) (tileEntity.getMinStrength() * 100)));
        minEfficiency.setText(Integer.toString((int) (tileEntity.getMinEfficiency() * 100)));
        toplevel.addChild(new Panel(mc, this).setLayout(new HorizontalLayout()).addChild(new Label(mc, this).setText("Purity")).addChild(minPurity));
        toplevel.addChild(new Panel(mc, this).setLayout(new HorizontalLayout()).addChild(new Label(mc, this).setText("Strength")).addChild(minStrength));
        toplevel.addChild(new Panel(mc, this).setLayout(new HorizontalLayout()).addChild(new Label(mc, this).setText("Efficiency")).addChild(minEfficiency));

        toplevel.setBounds(new Rectangle(guiLeft, guiTop, xSize, ySize));

        window = new Window(this, toplevel);
    }

    private void updateSettings() {
        int purity = 0;
        try {
            purity = Integer.parseInt(minPurity.getText());
        } catch (NumberFormatException e) {
        }
        int strength = 0;
        try {
            strength = Integer.parseInt(minStrength.getText());
        } catch (NumberFormatException e) {
        }
        int efficiency = 0;
        try {
            efficiency = Integer.parseInt(minEfficiency.getText());
        } catch (NumberFormatException e) {
        }
        tileEntity.setMinPurity(purity / 100.0f);
        tileEntity.setMinStrength(strength / 100.0f);
        tileEntity.setMinEfficiency(efficiency / 100.0f);
        sendServerCommand(DeepResonance.networkHandler.getNetworkWrapper(), ValveTileEntity.CMD_SETTINGS,
                new Argument("purity", purity / 100.0f),
                new Argument("strength", strength / 100.0f),
                new Argument("efficiency", efficiency / 100.0f));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i2) {
        drawWindow();
    }
}
