package org.iplantc.core.uiapplications.client.views.widgets;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.models.autobeans.App;
import org.iplantc.core.uiapplications.client.views.widgets.events.AppSearch3ResultSelectedEvent;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;

/**
 * TODO JDS
 * 
 * @author jstroot
 * 
 */
public class AppListViewCell extends AbstractCell<App> {

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
    
        //            template.append("<tpl for=\".\"><div class=\"search-item\">"); //$NON-NLS-1$
        //
        //            template.append("<h3>"); //$NON-NLS-1$
        //
        //            template.append("<tpl if=\"is_favorite\">"); //$NON-NLS-1$
        //            template.append("<img src='./images/fav.png'></img> &nbsp;"); //$NON-NLS-1$
        //            template.append("</tpl>"); //$NON-NLS-1$
        //
        //            template.append("{name}"); //$NON-NLS-1$
        //
        //            template.append("<span><b>"); //$NON-NLS-1$
        // template.append(I18N.DISPLAY.avgRating());
        //            template.append(":</b> {average} "); //$NON-NLS-1$
        // template.append(I18N.DISPLAY.ratingOutOfTotal());
        //            template.append("</span>"); //$NON-NLS-1$
        //
        //            template.append("</h3>"); //$NON-NLS-1$
        //
        //            template.append("<h4>"); //$NON-NLS-1$
        //            template.append("<span>{group_name}</span>"); //$NON-NLS-1$
        //            template.append("<br />{description}"); //$NON-NLS-1$
        //            template.append("</h4>"); //$NON-NLS-1$
        //
        //            template.append("</div></tpl>"); //$NON-NLS-1$
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
                EventBus.getInstance().fireEvent(
                        new AppSearch3ResultSelectedEvent(value.getId(), value.getGroupId()));
            }
        }
    }
}