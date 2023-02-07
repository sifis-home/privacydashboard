package com.privacydashboard.application.views.usefulComponents;
import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.shared.Registration;

@Tag("vcf-toggle-button")
@NpmPackage(value = "@vaadin-component-factory/vcf-toggle-button", version = "1.0.3")
@JsModule("@vaadin-component-factory/vcf-toggle-button")
@CssImport(value = "./themes/privacydashboard/views/usefulComponents/vaadin-checkbox.css", themeFor = "vaadin-checkbox")
//@SuppressWarnings("serial")
public class ToggleButton extends
        AbstractSinglePropertyField<ToggleButton, Boolean> implements HasStyle,
        HasSize, Focusable<ToggleButton>, ClickNotifier<ToggleButton> {

    public ToggleButton() {
        super("checked", false, false);
    }

    public ToggleButton(String labelText) {
        this();
        setLabel(labelText);
    }

    public ToggleButton(boolean initialValue) {
        this();
        setValue(initialValue);
    }

    public ToggleButton(String labelText, boolean initialValue) {
        this(labelText);
        setValue(initialValue);
    }

    public ToggleButton(String label,
                        ValueChangeListener<ComponentValueChangeEvent<ToggleButton, Boolean>> listener) {
        this(label);
        addValueChangeListener(listener);
    }

    public String getLabel() {
        return getElement().getProperty("label");
    }

    public void setLabel(String label) {
        getElement().setProperty("label", label);
    }

    protected boolean isDisabled() {
        return getElement().getProperty("disabled", false);
    }

    public void setDisabled(boolean disabled) {
        getElement().setProperty("disabled", disabled);
    }

    @DomEvent("change")
    public static class ChangeEvent extends ComponentEvent<ToggleButton> {
        public ChangeEvent(ToggleButton source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    //@SuppressWarnings({ "rawtypes", "unchecked" })
    protected Registration addChangeListener(
            ComponentEventListener<ChangeEvent> listener) {
        return addListener(ChangeEvent.class, listener);
    }

}
