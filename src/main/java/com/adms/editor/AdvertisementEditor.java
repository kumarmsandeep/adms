package com.adms.editor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.adms.model.Advertisement;
import com.adms.model.PricingFactor;
import com.adms.repo.AdvertisementRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * A simple example to introduce building forms. As your real application is
 * probably much more complicated than this example, you could re-use this form
 * in multiple places. This example component is only used in MainView.
 * <p>
 * In a real world application you'll most likely using a common super class for
 * all your forms - less code, better UX.
 */
@SpringComponent
@UIScope
public class AdvertisementEditor extends VerticalLayout implements KeyNotifier {

	private final AdvertisementRepository repository;

	/* Fields to edit properties in Advertisement entity */
	private TextField name = new TextField("Name");
	private TextField url = new TextField("Url");
	private TextField cost = new TextField("Cost");
	private TextField duration = new TextField("Duration (Sec)");
	
	private Checkbox premium = new Checkbox("Premium cost");
	private Checkbox priority = new Checkbox("Priority cost");
	private Checkbox standard = new Checkbox("Standard cost");

	private ComboBox<String> premiumTimeFrom = new ComboBox<String>();
	private ComboBox<String> premiumTimeTo = new ComboBox<String>();
	
	private ComboBox<String> priorityTimeFrom = new ComboBox<String>();
	private ComboBox<String> priorityTimeTo = new ComboBox<String>();
	
	private ComboBox<String> standardTimeFrom = new ComboBox<String>();
	private ComboBox<String> standardTimeTo = new ComboBox<String>();
	
	/* Action buttons */
	// TODO why more code?
	Button save = new Button("Add Advertisement", VaadinIcon.PLUS.create());
	HorizontalLayout actions = new HorizontalLayout(save);

	Binder<Advertisement> binder = new Binder<>(Advertisement.class);
	private ChangeHandler changeHandler;

	@Autowired
	public AdvertisementEditor(AdvertisementRepository repository) {
		this.repository = repository;

		name.setRequired(true);
		name.setRequiredIndicatorVisible(true);
		name.setWidth("100%");
		
		url.setRequired(true);
		url.setWidth("100%");
		url.setRequiredIndicatorVisible(true);

		cost.setPattern("[0-9]*");
		cost.setWidth("100px");
		cost.setPreventInvalidInput(true);
		cost.setPrefixComponent(new Span("₹"));
		cost.setRequired(true);
		cost.setRequiredIndicatorVisible(true);		
		
		duration.setPattern("[0-9]*");
		duration.setWidth("120px");
		duration.setPreventInvalidInput(true);
		duration.setRequired(true);
		duration.setRequiredIndicatorVisible(true);
		
		add(name, url, new HorizontalLayout(cost, duration), preparePricingFactor(premium, premiumTimeFrom, premiumTimeTo),
				preparePricingFactor(priority, priorityTimeFrom, priorityTimeTo),
				preparePricingFactor(standard, standardTimeFrom, standardTimeTo), actions);

		// binder.bindInstanceFields(this);

		// Configure and style components
		setSpacing(false);
		setPadding(false);
		setMargin(false);
		save.getElement().getThemeList().add("primary");

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> save());
	}

	private Component preparePricingFactor(Checkbox cb, final ComboBox<String> from, final ComboBox<String> to) {
		from.setWidth("110px");
		to.setWidth("110px");
		from.setAllowCustomValue(false);
		to.setAllowCustomValue(false);
		from.setEnabled(false);
		to.setEnabled(false);
		List<String> getFixedTimeStrings = getFixedTimeStrings();
		from.setItems(getFixedTimeStrings);
		to.setItems(getFixedTimeStrings);
		cb.addValueChangeListener(new ValueChangeListener<ValueChangeEvent<?>>() {

			@Override
			public void valueChanged(ValueChangeEvent<?> event) {
				from.setEnabled(!from.isEnabled());
				to.setEnabled(!to.isEnabled());				
			}
		});

		HorizontalLayout horizontalLayout = new HorizontalLayout(from, to);
		VerticalLayout verticalLayout = new VerticalLayout(cb, horizontalLayout);
		verticalLayout.setMargin(false);
		verticalLayout.setSpacing(false);
		verticalLayout.setPadding(false);
		horizontalLayout.setPadding(false);		
		horizontalLayout.setMargin(false);
		return verticalLayout;
	}

	private List<String> getFixedTimeStrings() {
		List<String> values = new ArrayList<>();
		for (int i = 0; i <= 23; i++) {
			for (int j = 0; j < 60; j += 15) {
				values.add(String.format("%02d", i) + ":" + String.format("%02d", j));
			}
		}
		return values;
	}

	void save() {
		String v_name = name.getValue();
		String v_url = url.getValue();
		int v_cost = Integer.parseInt(cost.getValue());
		int v_duraion = Integer.parseInt(duration.getValue());
		Advertisement advt = new Advertisement();
		advt.setName(v_name);
		advt.setUrl(v_url);
		advt.setCost(v_cost);
		advt.setDuration(v_duraion);
		ArrayList<PricingFactor> pricingFactors = new ArrayList<>();
		if(BooleanUtils.isTrue(priority.getValue())) {
			PricingFactor pf = new PricingFactor();
			pf.setType("Priority");
			pf.setFromTime(prepareTime(priorityTimeFrom.getValue()));
			pf.setToTime(prepareTime(priorityTimeTo.getValue()));
			pf.setAdvertisement(advt);
			pricingFactors.add(pf);
		}
		if(BooleanUtils.isTrue(premium.getValue())) {
			PricingFactor pf = new PricingFactor();
			pf.setType("Premium");
			pf.setFromTime(prepareTime(premiumTimeFrom.getValue()));
			pf.setToTime(prepareTime(premiumTimeTo.getValue()));
			pf.setAdvertisement(advt);
			pricingFactors.add(pf);
		}
		if(BooleanUtils.isTrue(standard.getValue())) {
			PricingFactor pf = new PricingFactor();
			pf.setType("Standard");
			pf.setFromTime(prepareTime(standardTimeFrom.getValue()));
			pf.setToTime(prepareTime(standardTimeTo.getValue()));
			pf.setAdvertisement(advt);
			pricingFactors.add(pf);
		}
		advt.setPricingFactors(pricingFactors);
		repository.save(advt);
		changeHandler.onChange();
	}
	
	private int prepareTime(String value) {
		String str[] = value.replace(" ", "").split(":");
		return Integer.parseInt(str[0])*100 + Integer.parseInt(str[1]);
	}

	public interface ChangeHandler {
		void onChange();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		changeHandler = h;
	}

}
