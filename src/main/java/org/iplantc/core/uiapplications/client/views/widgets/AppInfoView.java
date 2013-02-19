package org.iplantc.core.uiapplications.client.views.widgets;

import java.util.Date;

import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.models.autobeans.App;
import org.iplantc.core.uicommons.client.widgets.IPlantAnchor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;

public class AppInfoView implements IsWidget {

    interface AppInfoViewUiBinder extends UiBinder<Widget, AppInfoView> {
    }
    

    public interface AppDetailsRenderer extends XTemplates {
        @XTemplate(source = "appDetails.html")
        public SafeHtml render();
    }

    private static AppInfoViewUiBinder BINDER = GWT.create(AppInfoViewUiBinder.class);
    

    @UiField
    AppFavoriteCellWidget favIcon;

    @UiField
    HTML appDesc;

    @UiField
    HorizontalPanel appDetailsPnl;

    private final Widget widget;

  
    public AppInfoView(final App app) {
        widget = BINDER.createAndBindUi(this);
        favIcon.setValue(app);
        appDesc.setHTML("<i>" + I18N.DISPLAY.description() + ": " + "</i>" + app.getDescription());
        AppDetailsRenderer templates = GWT.create(AppDetailsRenderer.class);
        HtmlLayoutContainer c = new HtmlLayoutContainer(templates.render());
        addPubDate(app, c);
        addIntegratorsInfo(app, c);
        addDocLinks(app, c);
        addRating(app, c);
        appDetailsPnl.add(c);
    }


    private void addIntegratorsInfo(final App app, HtmlLayoutContainer c) {
        c.add(new Label(I18N.DISPLAY.integratorName() + ": "), new HtmlData(".cell3"));
        c.add(new Label(app.getIntegratorName()), new HtmlData(".cell4"));
        c.add(new Label(I18N.DISPLAY.integratorEmail() + ": "), new HtmlData(".cell5"));
        c.add(new Label(app.getIntegratorEmail()), new HtmlData(".cell6"));
    }


    private void addDocLinks(final App app, HtmlLayoutContainer c) {
        c.add(new Label(I18N.DISPLAY.help() + ": "), new HtmlData(".cell7"));
        IPlantAnchor doc = new IPlantAnchor(I18N.DISPLAY.documentation(), 100, new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent arg0) {
                Window.open(app.getWikiUrl(), "_blank", "");
                
            }
        });

        c.add(doc, new HtmlData(".cell8"));
    }


    private void addRating(final App app, HtmlLayoutContainer c) {
        c.add(new Label(I18N.DISPLAY.rating() + ": "), new HtmlData(".cell9"));
        AppRatingCellWidget rcell  = new AppRatingCellWidget();
        rcell.setValue(app);
        c.add(rcell, new HtmlData(".cell10"));
    }


    private void addPubDate(final App app, HtmlLayoutContainer c) {
        c.add(new Label(I18N.DISPLAY.publishedOn() + ": "), new HtmlData(".cell1"));
        Date pub_date = app.getIntegrationDate();
        if (pub_date != null) {
            c.add(new Label(pub_date.toString()), new HtmlData(".cell2"));
        } else {
            c.add(new Label("-"), new HtmlData(".cell2"));
        }
    }


    @Override
    public Widget asWidget() {
        return widget;
    }


}
