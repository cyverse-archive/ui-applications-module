package org.iplantc.core.uiapplications.client.views.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import org.iplantc.core.uiapplications.client.Constants;
import org.iplantc.core.uiapplications.client.models.autobeans.Analysis;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.events.UserEvent;

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
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Event;

public class AnalysisRunCell extends AbstractCell<Analysis> {

    interface MyCss extends CssResource {
        @ClassName("analysis_run")
        String analysisRun();
    }

    interface Resources extends ClientBundle {
        @Source("AnalysisRunCell.css")
        MyCss css();

        @Source("images/run.png")
        ImageResource run();
    }

    interface Templates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<img class=\"{0}\" src=\"{1}\"/>")
        SafeHtml cell(String imgClassName, SafeUri img);
    }

    private static final Resources resources = GWT.create(Resources.class);
    private static final Templates templates = GWT.create(Templates.class);

    public AnalysisRunCell() {
        // super(CLICK, MOUSEOVER, MOUSEOUT);
        super(CLICK);
        resources.css().ensureInjected();
    }

    @Override
    public void render(Cell.Context context, Analysis value, SafeHtmlBuilder sb) {
        sb.append(templates.cell(resources.css().analysisRun(), resources.run().getSafeUri()));
    }

    @Override
    public void onBrowserEvent(Cell.Context context, Element parent, Analysis value, NativeEvent event,
            ValueUpdater<Analysis> valueUpdater) {
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

    private void doOnMouseOut(Element eventTarget, Analysis value) {
        // XXX JDS Place holder for switching images on hover
    }

    private void doOnMouseOver(Element eventTarget, Analysis value) {
        // XXX JDS Place holder for switching images on hover
    }

    private void doOnClick(Element eventTarget, Analysis value) {
        UserEvent e = new UserEvent(Constants.CLIENT.windowTag(), value.getId());
        EventBus.getInstance().fireEvent(e);
    }

}
