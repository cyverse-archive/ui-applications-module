package org.iplantc.core.uiapplications.client.views.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOUT;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOVER;

import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.models.autobeans.App;
import org.iplantc.core.uiapplications.client.views.AppsView;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;

/**
 * This is a custom cell which combines the functionality of the {@link AppFavoriteCell} with a clickable
 * hyper-link of an app name.
 * 
 * @author jstroot
 * 
 */
public class AppHyperlinkCell extends AbstractCell<App> {


    interface MyCss extends CssResource {
        String appName();
        
        String appDisabled();
    }

    interface Resources extends ClientBundle {
        @Source("AppHyperlinkCell.css")
        MyCss css();
    }

    /**
     * The HTML templates used to render the cell.
     */
    interface Templates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<span name=\"{3}\" class=\"{0}\" qtip=\"{2}\">{1}</span>")
        SafeHtml cell(String textClassName, SafeHtml name, String textToolTip, String elementName);
    }

    private final Resources resources = GWT.create(Resources.class);
    private final Templates templates = GWT.create(Templates.class);
    private final AppFavoriteCell favoriteCell = new AppFavoriteCell();
    private final AppsView view;
    private static final String ELEMENT_NAME = "appName";

    public AppHyperlinkCell(AppsView view) {
        super(CLICK, MOUSEOVER, MOUSEOUT);
        this.view = view;
        resources.css().ensureInjected();
    }

    @Override
    public void render(Cell.Context context, App value, SafeHtmlBuilder sb) {
        if (value == null) {
            return;
        }
        favoriteCell.render(context, value, sb);
        sb.appendHtmlConstant("&nbsp;");
        SafeHtml safeHtmlName = SafeHtmlUtils.fromString(value.getName());
        if (!value.isDisabled()) {
            sb.append(templates.cell(resources.css().appName(), safeHtmlName,
                    I18N.DISPLAY.clickAppInfo(), ELEMENT_NAME));
        } else {
            sb.append(templates.cell(resources.css().appDisabled(), safeHtmlName,
                    I18N.DISPLAY.appUnavailable(), ELEMENT_NAME));
        }

    }

    @Override
    public void onBrowserEvent(Cell.Context context, Element parent, App value, NativeEvent event,
            ValueUpdater<App> valueUpdater) {
        Element eventTarget = Element.as(event.getEventTarget());
        if ((value == null) && !parent.isOrHasChild(eventTarget)) {
            return;
        }
        favoriteCell.onBrowserEvent(context, parent, value, event, valueUpdater);

        if (eventTarget.getAttribute("name").equalsIgnoreCase(ELEMENT_NAME)) {

            switch (Event.as(event).getTypeInt()) {
                case Event.ONCLICK:
                    doOnClick(eventTarget, value);
                    break;
                case Event.ONMOUSEOVER:
                    doOnMouseOver(eventTarget, value);
                    break;
                case Event.ONMOUSEOUT:
                    doOnMouseOut(eventTarget, value);
                    break;
                default:
                    break;
            }
        }
    }

    private void doOnMouseOut(Element eventTarget, App value) {
        eventTarget.getStyle().setTextDecoration(TextDecoration.NONE);
    }

    private void doOnMouseOver(Element eventTarget, App value) {
        eventTarget.getStyle().setTextDecoration(TextDecoration.UNDERLINE);
    }

    private void doOnClick(final Element eventTarget, final App value) {
        view.onAppHyperlinkSelected(value);
    }
}