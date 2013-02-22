package org.iplantc.core.uiapplications.client.views.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.models.autobeans.App;
import org.iplantc.core.uiapplications.client.views.widgets.events.AppSearch3ResultSelectedEvent;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.CommonModelAutoBeanFactory;
import org.iplantc.core.uicommons.client.models.HasId;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

/**
 * TODO JDS This cell needs to be refactored to use safehtml templates.
 * 
 * @author jstroot
 * 
 */
public class AppListViewCell extends AbstractCell<App> {

    private final CommonModelAutoBeanFactory factory = GWT.create(CommonModelAutoBeanFactory.class);

    public AppListViewCell() {
        super(CLICK);
    }

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context, App value,
            SafeHtmlBuilder sb) {
        String searchItem = "search-item";//$NON-NLS-1$
        // sb.append(template.render(value));
        sb.append(SafeHtmlUtils.fromTrustedString("<div class=\"" + searchItem + "\""));
        sb.append(SafeHtmlUtils.fromTrustedString("<h3>"));
        if (value.isFavorite()) {
            // TODO JDS Fix hard-coded use of image.
            sb.append(SafeHtmlUtils.fromTrustedString("<img src='./images/fav.png'></img> &nbsp;"));//$NON-NLS-1$
        }
        sb.append(SafeHtmlUtils.fromString(value.getName()));
        sb.append(SafeHtmlUtils.fromTrustedString("<span><b>"));
        sb.append(SafeHtmlUtils.fromTrustedString(I18N.DISPLAY.avgRating()));
        sb.append(SafeHtmlUtils.fromTrustedString("</b> " + value.getRating().getAverageRating()
                + " "));
        sb.append(SafeHtmlUtils.fromTrustedString(I18N.DISPLAY.ratingOutOfTotal()));
        sb.append(SafeHtmlUtils.fromTrustedString("</span>"));
        sb.append(SafeHtmlUtils.fromTrustedString("</h3>"));
        sb.append(SafeHtmlUtils.fromTrustedString("<h4>"));
        sb.append(SafeHtmlUtils.fromTrustedString("<span>"));
        sb.append(SafeHtmlUtils.fromString(value.getGroupName()));
        sb.append(SafeHtmlUtils.fromTrustedString("</span>"));
        sb.append(SafeHtmlUtils.fromTrustedString("<br />"));
        sb.append(SafeHtmlUtils.fromString(value.getDescription()));
        sb.append(SafeHtmlUtils.fromTrustedString("</h4>"));
        sb.append(SafeHtmlUtils.fromTrustedString("</div>"));
    
    }
    @Override
    public void onBrowserEvent(Context context, Element parent, App value, NativeEvent event,
            ValueUpdater<App> valueUpdater) {
        if (value == null) {
            return;
        }

        Element eventTarget = Element.as(event.getEventTarget());
        if (parent.isOrHasChild(eventTarget)) {
            if (Event.as(event).getTypeInt() == Event.ONCLICK) {
                AutoBean<HasId> hasId = AutoBeanCodex.decode(factory, HasId.class, "{\"id\": \"" + value.getGroupId() + "\"}");
                EventBus.getInstance().fireEvent(new AppSearch3ResultSelectedEvent(value, hasId.as()));
            }
        }
    }
}