package org.iplantc.core.uiapplications.client.views.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOUT;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOVER;

import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.Services;
import org.iplantc.core.uiapplications.client.events.AppFavoritedEvent;
import org.iplantc.core.uiapplications.client.models.autobeans.App;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserInfo;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AppFavoriteCell extends AbstractCell<App> {

    interface AppFavoriteCellStyle extends CssResource {
        String appFavorite();
        
        String appFavoriteAdd();
        
        String appFavoriteDelete();
        
        String appFavoriteDisabled();
        
        String appUnavailable();
    }

    interface Resources extends ClientBundle {
        @Source("AppFavoriteCell.css")
        AppFavoriteCellStyle css();

        @Source("images/award_star_gold_3.png")
        ImageResource favIcon();

        @Source("images/award_star_add.png")
        ImageResource favIconAdd();

        @Source("images/award_star_delete.png")
        ImageResource favIconDelete();

        @Source("images/award_star_silver_3.png")
        ImageResource disabledFavIcon();

        @Source("images/exclamation.png")
        ImageResource appUnavailableIcon();
    }

    /**
     * The HTML templates used to render the cell.
     */
    interface Templates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<span name=\"{0}\" class=\"{1}\" qtip=\"{2}\"> </span>")
        SafeHtml cell(String imgName, String imgClassName, String imgToolTip);
    }

    final Resources res = GWT.create(Resources.class);
    final Templates templates = GWT.create(Templates.class);

    public AppFavoriteCell() {
        super(CLICK, MOUSEOVER, MOUSEOUT);
        res.css().ensureInjected();
    }

    @Override
    public void render(Cell.Context context, App value, SafeHtmlBuilder sb) {
        if (value == null) {
            return;
        }

        if (!value.isDisabled() && value.isFavorite()) {
            sb.append(templates.cell("fav", res.css().appFavorite(), I18N.DISPLAY.remAppFromFav()));
        } else if (!value.isDisabled() && !value.isFavorite()) {
            sb.append(templates.cell("fav", res.css().appFavoriteDisabled(), I18N.DISPLAY.addAppToFav()));
        } else{
            sb.append(templates.cell("disabled", res.css().appFavoriteDisabled(), I18N.DISPLAY.appUnavailable()));
        }
    }

    @Override
    public void onBrowserEvent(Cell.Context context, Element parent, App value, NativeEvent event,
            ValueUpdater<App> valueUpdater) {
        if (value == null) {
            return;
        }

        Element eventTarget = Element.as(event.getEventTarget());
        if (parent.isOrHasChild(eventTarget) && eventTarget.getAttribute("name").equalsIgnoreCase("fav")
                && !value.isDisabled()) {

            switch (Event.as(event).getTypeInt()) {
                case Event.ONCLICK:
                    doOnClick(eventTarget, value, valueUpdater);
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

        if (value.isFavorite()) {
            eventTarget.setClassName(res.css().appFavorite());
            eventTarget.setAttribute("qtip", I18N.DISPLAY.remAppFromFav());
            eventTarget.setTitle(I18N.DISPLAY.remAppFromFav());
        } else {
            eventTarget.setClassName(res.css().appFavoriteDisabled());
            eventTarget.setAttribute("qtip", I18N.DISPLAY.addAppToFav());
            eventTarget.setTitle(I18N.DISPLAY.addAppToFav());
        }
    }

    private void doOnMouseOver(Element eventTarget, App value) {

        if (value.isFavorite()) {
            eventTarget.setClassName(res.css().appFavoriteDelete());
        } else {
            eventTarget.setClassName(res.css().appFavoriteAdd());
        }
    }

    private void doOnClick(final Element eventTarget, final App value, final ValueUpdater<App> valueUpdater) {

        Services.USER_APP_SERVICE.favoriteApp(UserInfo.getInstance().getWorkspaceId(), value.getId(),
                !value.isFavorite(), new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                value.setFavorite(!value.isFavorite());
                valueUpdater.update(value);

                // Reset favorite icon
                doOnMouseOut(eventTarget, value);
                EventBus.getInstance().fireEvent(
                        new AppFavoritedEvent(value.getId(), value.isFavorite()));
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.ERROR.favServiceFailure(), caught);
            }
        });
    }

}
