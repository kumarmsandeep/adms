package com.adms.view;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Route("adms")
@Theme(variant = Lumo.DARK, value = Lumo.class)
public class MainView extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<Tab, Component> tabsToPages = new HashMap<>();

	private Set<Component> pagesShown = new HashSet<>();

	private HorizontalLayout header = new HorizontalLayout();

	private HorizontalLayout content = new HorizontalLayout();

	private Tabs tabs = new Tabs();

	public MainView(CustomerView customerView, AdvertisementView advertisementView, RolloutPlanView rolloutPlanView,
			ReportView reportView) {

		Image logo = new Image(new StreamResource("", new InputStreamFactory() {
			
			@Override
			public InputStream createInputStream() {
				return MainView.class.getResourceAsStream("/logo.png");
			}
		}), "");
 
		logo.setWidth("120px");
		Label label = new Label("Ad Management System");
		header.setVerticalComponentAlignment(Alignment.CENTER, label);
		header.setHeight("50px");
		header.add(logo, label);

		content.setWidth("100%");
		content.setHeight("100%");

		this.add(header, content);

		content.add(tabs);

		this.addTab("Customer", customerView, true);

		this.addTab("Advertisement", advertisementView, false);

		this.addTab("RolloutPlan", rolloutPlanView, false);

		this.addTab("Report", reportView, false);

		tabs.setOrientation(Tabs.Orientation.VERTICAL);

		tabs.addSelectedChangeListener((event) -> {
			pagesShown.forEach(page -> page.setVisible(false));
			pagesShown.clear();
			Tab selectedTab = tabs.getSelectedTab();
			System.out.println(selectedTab.getLabel());
			Component selectedPage = tabsToPages.get(selectedTab);
			if (selectedPage != null) {
				selectedPage.setVisible(true);
				rolloutPlanView.refresh();
				reportView.refresh();
				pagesShown.add(selectedPage);
			}
		});
	}

	private void addTab(String tabLabel, Component view, boolean isSelected) {
		Tab tab = new Tab(tabLabel);
		Div page = new Div();
		page.setSizeFull();
		if (view != null) {
			page.add(view);
		}
		tab.setSelected(isSelected);
		if (isSelected && view != null) {
			pagesShown.add(page);
		}
		page.setVisible(isSelected);
		tabs.add(tab);
		tabsToPages.put(tab, page);
		content.add(page);
	}

}
