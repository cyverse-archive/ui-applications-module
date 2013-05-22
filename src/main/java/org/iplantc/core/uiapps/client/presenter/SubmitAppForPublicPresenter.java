package org.iplantc.core.uiapps.client.presenter;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.client.Services;
import org.iplantc.core.uiapps.client.models.autobeans.AppAutoBeanFactory;
import org.iplantc.core.uiapps.client.models.autobeans.AppGroup;
import org.iplantc.core.uiapps.client.models.autobeans.AppRefLink;
import org.iplantc.core.uiapps.client.presenter.proxy.PublicAppGroupProxy;
import org.iplantc.core.uiapps.client.views.SubmitAppForPublicUseView;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.DEProperties;
import org.iplantc.de.shared.services.ConfluenceServiceFacade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;

public class SubmitAppForPublicPresenter
		implements
		org.iplantc.core.uiapps.client.views.SubmitAppForPublicUseView.Presenter {

	private SubmitAppForPublicUseView view;
	private AsyncCallback<String> callback;

	@Inject
	public SubmitAppForPublicPresenter(SubmitAppForPublicUseView view,
			AsyncCallback<String> asyncCallback) {
		this.view = view;
		this.callback = asyncCallback;
		this.view.setPresenter(this);
		getAppDetails();
	}

	@Override
	public void go(HasOneWidget container) {
		container.setWidget(view);
		// Fetch AppGroups
		PublicAppGroupProxy appGroupProxy = new PublicAppGroupProxy();
		appGroupProxy.load(null, new AsyncCallback<List<AppGroup>>() {
			@Override
			public void onSuccess(List<AppGroup> result) {
				addAppGroup(null, result);
				view.expandAppGroups();
				// remove workspace node from store
				view.getTreeStore().remove(
						view.getTreeStore().findModelWithKey(
								DEProperties.getInstance()
										.getDefaultBetaCategoryId()));
			}

			private void addAppGroup(AppGroup parent, List<AppGroup> children) {
				if ((children == null) || children.isEmpty()) {
					return;
				}
				if (parent == null) {
					view.getTreeStore().add(children);
				} else {
					view.getTreeStore().add(parent, children);
				}

				for (AppGroup ag : children) {
					addAppGroup(ag, ag.getGroups());
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				ErrorHandler.post(caught);
			}
		});
	}

	@Override
	public void onSubmit() {
		if (view.validate()) {
			createDocumentationPage(view.toJson());
		} else {
			AlertMessageBox amb = new AlertMessageBox(I18N.DISPLAY.warning(),
					I18N.DISPLAY.publicSubmitTip());
			amb.show();
		}
	}

	private void getAppDetails() {
		Services.USER_APP_SERVICE.getAppDetails(view.getSelectedApp().getId(),
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						ErrorHandler.post(caught.toString());

					}

					@Override
					public void onSuccess(String result) {
						JSONObject obj = JsonUtil.getObject(result);
						JSONArray arr = JsonUtil.getArray(obj, "references");
						if (arr != null && arr.size() > 0) {
							view.loadReferences(parseRefLinks(arr));
						}
					}
				});
	}

	private void createDocumentationPage(final JSONObject obj) {
		ConfluenceServiceFacade.getInstance().createDocumentationPage(
				JsonUtil.getString(obj, "name"),
				JsonUtil.getString(obj, "desc"), new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {
						ErrorHandler.post(I18N.ERROR
								.cantCreateConfluencePage(JsonUtil.getString(
										obj, "name")), caught);
					}

					@Override
					public void onSuccess(final String url) {
						obj.put("wiki_url", new JSONString(url));
						Services.USER_APP_SERVICE.publishToWorld(obj,
								new AsyncCallback<String>() {
									@Override
									public void onSuccess(String result) {
										callback.onSuccess(url);
									}

									@Override
									public void onFailure(Throwable caught) {
										callback.onFailure(caught);
									}
								});
					}
				});
	}

	private List<AppRefLink> parseRefLinks(JSONArray arr) {
		AppAutoBeanFactory factory = GWT
				.create(AppAutoBeanFactory.class);
		List<AppRefLink> linksList = new ArrayList<AppRefLink>();
		for (int i = 0; i < arr.size(); i++) {
			AutoBean<AppRefLink> bean = AutoBeanCodex
					.decode(factory, AppRefLink.class, "{}");
			AppRefLink link = bean.as();
			String stringValue = arr.get(i).isString()
					.stringValue();
			link.setId(stringValue);
			link.setRefLink(stringValue);
			linksList.add(link);
		}

		return linksList;
	}

}
