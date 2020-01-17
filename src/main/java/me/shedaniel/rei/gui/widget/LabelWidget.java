/*
 * Roughly Enough Items by Danielshe.
 * Licensed under the MIT License.
 */

package me.shedaniel.rei.gui.widget;

import me.shedaniel.math.api.Point;
import me.shedaniel.math.api.Rectangle;
import me.shedaniel.rei.impl.ScreenHelper;
import net.minecraft.client.gui.Element;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LabelWidget extends WidgetWithBounds {
    
    private Point pos;
    private String text;
    private int defaultColor;
    private boolean hasShadows = true;
    private boolean centered = true;
    private Supplier<String> tooltipSupplier;
    
    @ApiStatus.Internal
    public LabelWidget(int x, int y, String text) {
        this(new Point(x, y), text);
    }
    
    @ApiStatus.Internal
    public LabelWidget(Point point, String text) {
        this.pos = point;
        this.text = text;
        this.defaultColor = ScreenHelper.isDarkModeEnabled() ? 0xFFBBBBBB : -1;
    }
    
    public static LabelWidget create(Point point, String text) {
        return new LabelWidget(point, text);
    }
    
    public static ClickableLabelWidget createClickable(Point point, String text, Consumer<ClickableLabelWidget> onClicked) {
        return new ClickableActionedLabelWidget(point, text, onClicked);
    }
    
    public LabelWidget tooltip(Supplier<String> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
        return this;
    }
    
    public boolean isCentered() {
        return centered;
    }
    
    public void setCentered(boolean centered) {
        this.centered = centered;
    }
    
    public LabelWidget centered() {
        setCentered(true);
        return this;
    }
    
    public LabelWidget leftAligned() {
        setCentered(false);
        return this;
    }
    
    public boolean isHasShadows() {
        return hasShadows;
    }
    
    public void setHasShadows(boolean hasShadows) {
        this.hasShadows = hasShadows;
    }
    
    public LabelWidget noShadow() {
        setHasShadows(false);
        return this;
    }
    
    public int getDefaultColor() {
        return defaultColor;
    }
    
    public void setDefaultColor(int defaultColor) {
        this.defaultColor = defaultColor;
    }
    
    public Point getPosition() {
        return pos;
    }
    
    public LabelWidget setPosition(Point position) {
        this.pos = position;
        return this;
    }
    
    public String getText() {
        return text;
    }
    
    public LabelWidget setText(String text) {
        this.text = text;
        return this;
    }
    
    public LabelWidget color(int defaultColor) {
        this.defaultColor = defaultColor;
        return this;
    }
    
    public Optional<String> getTooltips() {
        return Optional.ofNullable(tooltipSupplier).map(Supplier::get);
    }
    
    @Override
    public Rectangle getBounds() {
        int width = font.getStringWidth(text);
        Point pos = getPosition();
        if (isCentered())
            return new Rectangle(pos.x - width / 2 - 1, pos.y - 5, width + 2, 14);
        return new Rectangle(pos.x - 1, pos.y - 5, width + 2, 14);
    }
    
    @Override
    public List<? extends Element> children() {
        return Collections.emptyList();
    }
    
    @Override
    public void render(int mouseX, int mouseY, float delta) {
        int width = font.getStringWidth(text);
        Point pos = getPosition();
        if (isCentered()) {
            if (hasShadows)
                font.drawWithShadow(text, pos.x - width / 2f, pos.y, defaultColor);
            else
                font.draw(text, pos.x - width / 2f, pos.y, defaultColor);
        } else {
            if (hasShadows)
                font.drawWithShadow(text, pos.x, pos.y, defaultColor);
            else
                font.draw(text, pos.x, pos.y, defaultColor);
        }
    }
    
    protected void drawTooltips(int mouseX, int mouseY) {
        if (getTooltips().isPresent())
            if (containsMouse(mouseX, mouseY))
                ScreenHelper.getLastOverlay().addTooltip(QueuedTooltip.create(getTooltips().get().split("\n")));
    }
}
