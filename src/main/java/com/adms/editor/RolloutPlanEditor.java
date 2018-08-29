package com.adms.editor;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.adms.model.Advertisement;
import com.adms.model.Customer;
import com.adms.model.RolloutPlan;
import com.adms.repo.AdvertisementRepository;
import com.adms.repo.CustomerRepository;
import com.adms.repo.RolloutPlanRepository;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
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
public class RolloutPlanEditor extends VerticalLayout implements KeyNotifier {

	private final RolloutPlanRepository repository;

	private final CustomerRepository customerRepo;

	private final AdvertisementRepository advtRepo;

	/* Fields to edit properties in Customer entity */
	private DatePicker dateFrom = new DatePicker("From");
	private ComboBox<String> timeFrom = new ComboBox<String>();
	private DatePicker dateTo = new DatePicker("To");
	private ComboBox<String> timeTo = new ComboBox<String>();
	private ComboBox<Customer> address = new ComboBox<Customer>("Customer");
	private ComboBox<Advertisement> contact = new ComboBox<Advertisement>("Advertisement");

	/* Action buttons */
	// TODO why more code?
	Button save = new Button("Add RolloutPlan", VaadinIcon.PLUS.create());
	HorizontalLayout actions = new HorizontalLayout(save);

	Binder<RolloutPlan> binder = new Binder<>(RolloutPlan.class);
	private ChangeHandler changeHandler;

	@Autowired
	public RolloutPlanEditor(RolloutPlanRepository repository, CustomerRepository customerRepo,
			AdvertisementRepository advtRepo) {
		this.repository = repository;
		this.customerRepo = customerRepo;
		this.advtRepo = advtRepo;

		add(dateFrom, timeFrom, dateTo, timeTo, address, contact, actions);
		dateFrom.setRequired(true);
		dateFrom.setRequiredIndicatorVisible(true);

		dateTo.setRequired(true);
		dateTo.setRequiredIndicatorVisible(true);

		address.setRequired(true);
		address.setRequiredIndicatorVisible(true);

		contact.setRequired(true);
		contact.setRequiredIndicatorVisible(true);

		List<String> fixedTimeStrings = getFixedTimeStrings();
		timeFrom.setItems(fixedTimeStrings);
		timeTo.setItems(fixedTimeStrings);

		reloadValues();

		// Configure and style components
		setSpacing(false);

		save.getElement().getThemeList().add("primary");

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> save());
	}

	private List<String> getFixedTimeStrings() {
		List<String> values = new ArrayList<>();
		for (int i = 0; i <= 23; i++) {
			for (int j = 0; j < 60; j += 15) {
				values.add(String.format("%02d", i) + " : " + String.format("%02d", j));
			}
		}
		return values;
	}

	void save() {
		RolloutPlan rolloutPlan = new RolloutPlan();
		rolloutPlan.setCustomer(address.getValue());
		rolloutPlan.setAdvertisement(contact.getValue());
		rolloutPlan.setFromDate(getFromDate());
		rolloutPlan.setToDate(getToDate());
		repository.save(rolloutPlan);
		changeHandler.onChange();
	}

	private Date getFromDate() {
		Date from = Date.from(dateFrom.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		String str[] = timeFrom.getValue().replace(" ", "").split(":");
		int h = Integer.parseInt(str[0]);
		int m = Integer.parseInt(str[0]);
		Date addHours = DateUtils.addHours(from, h);		
		return DateUtils.addMinutes(addHours, m);
	}

	private Date getToDate() {
		Date to = Date.from(dateTo.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		String str[] = timeTo.getValue().replace(" ", "").split(":");
		int h = Integer.parseInt(str[0]);
		int m = Integer.parseInt(str[0]);
		Date addHours = DateUtils.addHours(to, h);		
		return DateUtils.addMinutes(addHours, m);
	}

	public interface ChangeHandler {
		void onChange();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		changeHandler = h;
	}

	public void reloadValues() {
		address.setItems(customerRepo.findAll());
		contact.setItems(advtRepo.findAll());
	}

}
