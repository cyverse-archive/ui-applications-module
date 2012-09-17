package org.iplantc.core.uiapplications.client.views.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEDOWN;

import org.iplantc.core.uiapplications.client.CommonAppDisplayStrings;
import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.models.autobeans.Analysis;
import org.iplantc.core.uicommons.client.Constants;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.events.UserEvent;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.impl.HyperlinkImpl;

public class HyperlinkCell extends AbstractCell<Analysis> {

    private final EventBus eventBus;
    private final CommonAppDisplayStrings displayStrings;

    public HyperlinkCell() {
        super(CLICK, MOUSEDOWN);
        this.eventBus = EventBus.getInstance();
        this.displayStrings = I18N.DISPLAY;
    }

    @Override
    public void render(Cell.Context context, Analysis value, SafeHtmlBuilder sb) {
        if (value == null) {
            return;
        }
        String name = null;

        if (value.isFavorite()) {
            name = "<img src='./images/fav.png'/>&nbsp;" + value.getName(); //$NON-NLS-1$
        } else {
            name = value.getName();
        }

        if (!value.isDisabled()) {
            Hyperlink link = new Hyperlink(SafeHtmlUtils.fromTrustedString(name), "");
            link.setStyleName("analysis_name"); //$NON-NLS-1$
            link.setWidth(Integer.toString(value.getName().length()));

            sb.append(SafeHtmlUtils.fromTrustedString(link.toString()));
        } else {
            name = "<img title ='" //$NON-NLS-1$
                    + displayStrings.appUnavailable()
                    + "' src='./images/exclamation.png'/>&nbsp;" + name; //$NON-NLS-1$

            sb.append(SafeHtmlUtils.fromTrustedString(name));
        }
    }

    @Override
    public void onBrowserEvent(Cell.Context context, Element parent, Analysis value, NativeEvent event,
            ValueUpdater<Analysis> valueUpdater) {
        if (((HyperlinkImpl)GWT.create(HyperlinkImpl.class)).handleAsClick(Event.as(event))) {
            event.preventDefault();
            UserEvent e = new UserEvent(Constants.CLIENT.windowTag(), value.getId());
            eventBus.fireEvent(e);
        }
    }
}
