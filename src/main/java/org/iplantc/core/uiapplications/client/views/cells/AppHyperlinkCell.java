package org.iplantc.core.uiapplications.client.views.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOUT;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOVER;

import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.Services;
import org.iplantc.core.uiapplications.client.events.AppFavoritedEvent;
import org.iplantc.core.uiapplications.client.events.AppSelectedEvent;
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
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author jstroot
 * 
 */
public class AppHyperlinkCell extends AbstractCell<App> {


    interface MyCss extends CssResource {
        @ClassName("app_name")
        String appName();

        @ClassName("fav_app")
        String favApp();
    }

    interface Resources extends ClientBundle {
        @Source("AppHyperlinkCell.css")
        MyCss css();

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

        @SafeHtmlTemplates.Template("<img name=\"{0}\" class=\"{1}\" title=\"{2}\" src=\"{3}\">&nbsp;<span class=\"{4}\">{5}</span>")
        SafeHtml cell(String imgName, String imgClassName, String imgTitle, SafeUri img,
                String textClassName,
                SafeHtml name);
    }

    final Resources resources = GWT.create(Resources.class);
    final Templates templates = GWT.create(Templates.class);

    public AppHyperlinkCell() {
        super(CLICK, MOUSEOVER, MOUSEOUT);
        resources.css().ensureInjected();
    }

    @Override
    public void render(Cell.Context context, App value, SafeHtmlBuilder sb) {
        if (value == null) {
            return;
        }
        SafeHtml safeHtmlName = SafeHtmlUtils.fromString(value.getName());
        if (!value.isDisabled() && value.isFavorite()) {
            // Set Normal favorite
            sb.append(templates.cell("fav", resources.css().favApp(), "", resources.favIcon()
                    .getSafeUri(),
                    resources.css().appName(),
                    safeHtmlName));
        } else if (!value.isDisabled() && !value.isFavorite()) {
            // Set disabled favorite
            sb.append(templates.cell("fav", resources.css().favApp(), "", resources.disabledFavIcon()
                    .getSafeUri(), resources.css()
                    .appName(), safeHtmlName));
        } else {
            sb.append(templates.cell("disabled", resources.css().favApp(),
                    I18N.DISPLAY.appUnavailable(), resources
                    .appUnavailableIcon().getSafeUri(), resources.css().appName(), safeHtmlName));
        }

    }

    @Override
    public void onBrowserEvent(Cell.Context context, Element parent, App value, NativeEvent event,
            ValueUpdater<App> valueUpdater) {
        if (value == null) {
            return;
        }

        Element eventTarget = Element.as(event.getEventTarget());
        if (parent.isOrHasChild(eventTarget)) {

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

        if (eventTarget.getNodeName().equalsIgnoreCase("span")) {
            eventTarget.getStyle().setTextDecoration(TextDecoration.NONE);
        } else if (eventTarget.getAttribute("name").equalsIgnoreCase("fav")) {
            if (value.isFavorite()) {
                eventTarget.setAttribute("src", resources.favIcon().getSafeUri().asString());
            } else {
                eventTarget.setAttribute("src", resources.disabledFavIcon().getSafeUri().asString());
            }
        }
    }

    private void doOnMouseOver(Element eventTarget, App value) {

        if (eventTarget.getNodeName().equalsIgnoreCase("span")) {
            eventTarget.getStyle().setTextDecoration(TextDecoration.UNDERLINE);
        } else if (eventTarget.getAttribute("name").equalsIgnoreCase("fav")) {
            if (value.isFavorite()) {
                eventTarget.setAttribute("src", resources.favIconDelete().getSafeUri().asString());
            } else {
                eventTarget.setAttribute("src", resources.favIconAdd().getSafeUri().asString());
            }
        }
    }

    private void doOnClick(final Element eventTarget, final App value) {

        if (eventTarget.getNodeName().equalsIgnoreCase("span")) {
            EventBus.getInstance().fireEvent(new AppSelectedEvent(value.getId(), this));
        } else if (eventTarget.getAttribute("name").equalsIgnoreCase("fav")) {
            Services.USER_APP_SERVICE.favoriteApp(UserInfo.getInstance().getWorkspaceId(),
                    value.getId(), !value.isFavorite(), new AsyncCallback<String>() {

                        @Override
                        public void onSuccess(String result) {
                            value.setFavorite(!value.isFavorite());
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
}
