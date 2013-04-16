package org.iplantc.core.uiapps.client.views;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.client.models.autobeans.App;
import org.iplantc.core.uiapps.client.models.autobeans.AppProperties;
import org.iplantc.core.uiapps.client.views.cells.AppHyperlinkCell;
import org.iplantc.core.uiapps.client.views.cells.AppInfoCell;
import org.iplantc.core.uiapps.client.views.cells.AppRatingCell;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

public class AppColumnModel extends ColumnModel<App> {


    public AppColumnModel(AppsView view) {
        super(createColumnConfigList(view));

    }

    public static List<ColumnConfig<App, ?>> createColumnConfigList(AppsView view) {
        AppProperties props = GWT.create(AppProperties.class);
        List<ColumnConfig<App, ?>> list = new ArrayList<ColumnConfig<App, ?>>();

        ColumnConfig<App, App> info = new ColumnConfig<App, App>(
                new IdentityValueProvider<App>(), 20);

        ColumnConfig<App, App> name = new ColumnConfig<App, App>(
                new IdentityValueProvider<App>(), 180, I18N.DISPLAY.name());
        ColumnConfig<App, String> integrator = new ColumnConfig<App, String>(
                props.integratorName(), 130, I18N.DISPLAY.integratedby());
        ColumnConfig<App, App> rating = new ColumnConfig<App, App>(new IdentityValueProvider<App>(),
                105, "Rating"); //$NON-NLS-1$

        info.setSortable(false);

        info.setResizable(false);
        name.setResizable(true);
        rating.setResizable(false);

        info.setFixed(true);
        rating.setFixed(true);

        info.setCell(new AppInfoCell(view));
        name.setCell(new AppHyperlinkCell(view));
        rating.setCell(new AppRatingCell());

        rating.setAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        list.add(info);
        list.add(name);
        list.add(integrator);
        list.add(rating);
        return list;
    }

}


