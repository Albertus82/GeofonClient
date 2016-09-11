package it.albertus.geofon.client.gui;

import it.albertus.geofon.client.gui.job.SearchJob;
import it.albertus.geofon.client.gui.listener.AutoRefreshButtonSelectionListener;
import it.albertus.geofon.client.gui.listener.ClearButtonSelectionListener;
import it.albertus.geofon.client.gui.listener.FormTextTraverseListener;
import it.albertus.geofon.client.gui.listener.FormatRadioSelectionListener;
import it.albertus.geofon.client.gui.listener.SearchButtonSelectionListener;
import it.albertus.geofon.client.gui.listener.StopButtonSelectionListener;
import it.albertus.geofon.client.resources.Messages;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SearchForm {

	private final Composite formComposite;

	private final Label periodLabel;
	private final Label periodFromLabel;
	private final Label periodToLabel;
	private final Text periodFromText;
	private final Text periodToText;
	private final Label periodFromNote;
	private final Label periodToNote;

	private final Label latitudeLabel;
	private final Label latitudeFromLabel;
	private final Text latitudeFromText;
	private final Label latitudeFromNote;
	private final Label latitudeToLabel;
	private final Text latitudeToText;
	private final Label latitudeToNote;

	private final Label longitudeLabel;
	private final Label longitudeFromLabel;
	private final Text longitudeFromText;
	private final Label longitudeFromNote;
	private final Label longitudeToLabel;
	private final Text longitudeToText;
	private final Label longitudeToNote;

	private final Label minimumMagnitudeLabel;
	private final Text minimumMagnitudeText;
	private final Button restrictButton;

	private final Label outputFormatLabel;
	private final Composite radioComposite;
	private final Map<String, Button> formatRadios = new LinkedHashMap<String, Button>();
	private final Label resultsLabel;
	private final Text resultsText;

	private final Group criteriaGroup;

	private final Composite buttonsComposite;
	private final Button searchButton;
	private final Label resultsNote;
	private final Button autoRefreshButton;
	private final Text autoRefreshText;
	private final Button stopButton;
	private final Button clearButton;

	private final FormTextTraverseListener formTextTraverselistener = new FormTextTraverseListener(this);

	private SearchJob searchJob;

	public SearchForm(final GeofonClientGui gui) {
		formComposite = new Composite(gui.getShell(), SWT.NONE);
		GridLayoutFactory.swtDefaults().margins(0, 0).numColumns(2).applyTo(formComposite);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(formComposite);

		criteriaGroup = new Group(formComposite, SWT.NONE);
		criteriaGroup.setText(Messages.get("lbl.form.criteria.group"));
		GridLayoutFactory.swtDefaults().numColumns(7).applyTo(criteriaGroup);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(criteriaGroup);

		periodLabel = new Label(criteriaGroup, SWT.NONE);
		periodLabel.setText(Messages.get("lbl.form.criteria.period"));
		periodFromLabel = new Label(criteriaGroup, SWT.NONE);
		periodFromLabel.setText(Messages.get("lbl.form.criteria.period.from"));
		periodFromText = new Text(criteriaGroup, SWT.BORDER);
		periodFromText.setTextLimit(10);
		periodFromText.addTraverseListener(formTextTraverselistener);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(periodFromText);
		periodFromNote = new Label(criteriaGroup, SWT.NONE);
		periodFromNote.setText(Messages.get("lbl.form.criteria.period.from.note"));
		periodToLabel = new Label(criteriaGroup, SWT.NONE);
		periodToLabel.setText(Messages.get("lbl.form.criteria.period.to"));
		periodToText = new Text(criteriaGroup, SWT.BORDER);
		periodToText.setTextLimit(10);
		periodToText.addTraverseListener(formTextTraverselistener);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(periodToText);
		periodToNote = new Label(criteriaGroup, SWT.NONE);
		periodToNote.setText(Messages.get("lbl.form.criteria.period.to.note"));

		latitudeLabel = new Label(criteriaGroup, SWT.NONE);
		latitudeLabel.setText(Messages.get("lbl.form.criteria.latitude"));
		latitudeFromLabel = new Label(criteriaGroup, SWT.NONE);
		latitudeFromLabel.setText(Messages.get("lbl.form.criteria.latitude.from"));
		latitudeFromText = new Text(criteriaGroup, SWT.BORDER);
		latitudeFromText.setTextLimit(7);
		latitudeFromText.addTraverseListener(formTextTraverselistener);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(latitudeFromText);
		latitudeFromNote = new Label(criteriaGroup, SWT.NONE);
		latitudeFromNote.setText(Messages.get("lbl.form.criteria.latitude.from.note"));
		latitudeToLabel = new Label(criteriaGroup, SWT.NONE);
		latitudeToLabel.setText(Messages.get("lbl.form.criteria.latitude.to"));
		latitudeToText = new Text(criteriaGroup, SWT.BORDER);
		latitudeToText.setTextLimit(7);
		latitudeToText.addTraverseListener(formTextTraverselistener);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(latitudeToText);
		latitudeToNote = new Label(criteriaGroup, SWT.NONE);
		latitudeToNote.setText(Messages.get("lbl.form.criteria.latitude.to.note"));

		longitudeLabel = new Label(criteriaGroup, SWT.NONE);
		longitudeLabel.setText(Messages.get("lbl.form.criteria.longitude"));
		longitudeFromLabel = new Label(criteriaGroup, SWT.NONE);
		longitudeFromLabel.setText(Messages.get("lbl.form.criteria.longitude.from"));
		longitudeFromText = new Text(criteriaGroup, SWT.BORDER);
		longitudeFromText.setTextLimit(7);
		longitudeFromText.addTraverseListener(formTextTraverselistener);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(longitudeFromText);
		longitudeFromNote = new Label(criteriaGroup, SWT.NONE);
		longitudeFromNote.setText(Messages.get("lbl.form.criteria.longitude.from.note"));
		longitudeToLabel = new Label(criteriaGroup, SWT.NONE);
		longitudeToLabel.setText(Messages.get("lbl.form.criteria.longitude.to"));
		longitudeToText = new Text(criteriaGroup, SWT.BORDER);
		longitudeToText.setTextLimit(7);
		longitudeToText.addTraverseListener(formTextTraverselistener);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(longitudeToText);
		longitudeToNote = new Label(criteriaGroup, SWT.NONE);
		longitudeToNote.setText(Messages.get("lbl.form.criteria.longitude.to.note"));

		minimumMagnitudeLabel = new Label(criteriaGroup, SWT.NONE);
		minimumMagnitudeLabel.setText(Messages.get("lbl.form.criteria.magnitude"));
		GridDataFactory.swtDefaults().span(2, 1).applyTo(minimumMagnitudeLabel);
		minimumMagnitudeText = new Text(criteriaGroup, SWT.BORDER);
		minimumMagnitudeText.setTextLimit(3);
		minimumMagnitudeText.addTraverseListener(formTextTraverselistener);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(minimumMagnitudeText);
		restrictButton = new Button(criteriaGroup, SWT.CHECK);
		restrictButton.setText(Messages.get("lbl.form.criteria.restrict"));
		GridDataFactory.swtDefaults().span(4, 1).applyTo(restrictButton);

		outputFormatLabel = new Label(criteriaGroup, SWT.NONE);
		outputFormatLabel.setText(Messages.get("lbl.form.format"));
		radioComposite = new Composite(criteriaGroup, SWT.NONE);
		radioComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		GridDataFactory.swtDefaults().grab(false, false).span(2, 1).applyTo(radioComposite);
		for (final String mode : new String[] { "RSS", "KML" }) {
			final Button radio = new Button(radioComposite, SWT.RADIO);
			radio.addSelectionListener(new FormatRadioSelectionListener(this, radio, mode));
			radio.setText(mode);
			radio.setSelection("RSS".equalsIgnoreCase(mode));
			radio.setEnabled("RSS".equalsIgnoreCase(mode));
			formatRadios.put(mode, radio);
		}
		resultsLabel = new Label(criteriaGroup, SWT.NONE);
		resultsLabel.setText(Messages.get("lbl.form.limit"));
		GridDataFactory.swtDefaults().grab(false, false).span(2, 1).applyTo(resultsLabel);
		resultsText = new Text(criteriaGroup, SWT.BORDER);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(resultsText);
		resultsText.setTextLimit(3);
		resultsText.addTraverseListener(formTextTraverselistener);
		resultsNote = new Label(criteriaGroup, SWT.NONE);
		resultsNote.setText(Messages.get("lbl.form.limit.note"));

		// Buttons
		buttonsComposite = new Composite(formComposite, SWT.NONE);
		GridLayoutFactory.swtDefaults().numColumns(1).applyTo(buttonsComposite);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(buttonsComposite);

		autoRefreshButton = new Button(buttonsComposite, SWT.CHECK);
		autoRefreshButton.setText(Messages.get("lbl.form.button.autorefresh"));
		GridDataFactory.swtDefaults().applyTo(autoRefreshButton);

		autoRefreshText = new Text(buttonsComposite, SWT.BORDER);
		autoRefreshText.setTextLimit(9);
		autoRefreshText.addTraverseListener(formTextTraverselistener);
		GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(autoRefreshText);

		searchButton = new Button(buttonsComposite, SWT.NONE);
		searchButton.setText(Messages.get("lbl.form.button.submit"));
		GridDataFactory.fillDefaults().grab(true, false).applyTo(searchButton);

		stopButton = new Button(buttonsComposite, SWT.NONE);
		stopButton.setText(Messages.get("lbl.form.button.stop"));
		GridDataFactory.fillDefaults().grab(true, false).applyTo(stopButton);
		stopButton.setEnabled(false);

		clearButton = new Button(buttonsComposite, SWT.NONE);
		clearButton.setText(Messages.get("lbl.form.button.clear"));
		GridDataFactory.fillDefaults().grab(true, false).applyTo(clearButton);

		// Listeners
		searchButton.addSelectionListener(new SearchButtonSelectionListener(gui));
		stopButton.addSelectionListener(new StopButtonSelectionListener(this));
		clearButton.addSelectionListener(new ClearButtonSelectionListener(this));
		autoRefreshButton.addSelectionListener(new AutoRefreshButtonSelectionListener(this));
		autoRefreshButton.notifyListeners(SWT.Selection, null);
	}

	public void updateTexts() {
		criteriaGroup.setText(Messages.get("lbl.form.criteria.group"));
		periodLabel.setText(Messages.get("lbl.form.criteria.period"));
		periodFromLabel.setText(Messages.get("lbl.form.criteria.period.from"));
		periodFromNote.setText(Messages.get("lbl.form.criteria.period.from.note"));
		periodToLabel.setText(Messages.get("lbl.form.criteria.period.to"));
		periodToNote.setText(Messages.get("lbl.form.criteria.period.to.note"));
		latitudeLabel.setText(Messages.get("lbl.form.criteria.latitude"));
		latitudeFromLabel.setText(Messages.get("lbl.form.criteria.latitude.from"));
		latitudeFromNote.setText(Messages.get("lbl.form.criteria.latitude.from.note"));
		latitudeToLabel.setText(Messages.get("lbl.form.criteria.latitude.to"));
		latitudeToNote.setText(Messages.get("lbl.form.criteria.latitude.to.note"));
		longitudeLabel.setText(Messages.get("lbl.form.criteria.longitude"));
		longitudeFromLabel.setText(Messages.get("lbl.form.criteria.longitude.from"));
		longitudeFromNote.setText(Messages.get("lbl.form.criteria.longitude.from.note"));
		longitudeToLabel.setText(Messages.get("lbl.form.criteria.longitude.to"));
		longitudeToNote.setText(Messages.get("lbl.form.criteria.longitude.to.note"));
		minimumMagnitudeLabel.setText(Messages.get("lbl.form.criteria.magnitude"));
		restrictButton.setText(Messages.get("lbl.form.criteria.restrict"));
		outputFormatLabel.setText(Messages.get("lbl.form.format"));
		resultsLabel.setText(Messages.get("lbl.form.limit"));
		resultsNote.setText(Messages.get("lbl.form.limit.note"));
		autoRefreshButton.setText(Messages.get("lbl.form.button.autorefresh"));
		searchButton.setText(Messages.get("lbl.form.button.submit"));
		stopButton.setText(Messages.get("lbl.form.button.stop"));
		clearButton.setText(Messages.get("lbl.form.button.clear"));
	}

	public SearchJob getSearchJob() {
		return searchJob;
	}

	public void setSearchJob(SearchJob searchJob) {
		this.searchJob = searchJob;
	}

	public Composite getFormComposite() {
		return formComposite;
	}

	public Label getPeriodLabel() {
		return periodLabel;
	}

	public Label getPeriodFromLabel() {
		return periodFromLabel;
	}

	public Label getPeriodToLabel() {
		return periodToLabel;
	}

	public Text getPeriodFromText() {
		return periodFromText;
	}

	public Text getPeriodToText() {
		return periodToText;
	}

	public Label getPeriodFromNote() {
		return periodFromNote;
	}

	public Label getPeriodToNote() {
		return periodToNote;
	}

	public Label getLatitudeLabel() {
		return latitudeLabel;
	}

	public Label getLatitudeFromLabel() {
		return latitudeFromLabel;
	}

	public Text getLatitudeFromText() {
		return latitudeFromText;
	}

	public Label getLatitudeFromNote() {
		return latitudeFromNote;
	}

	public Label getLatitudeToLabel() {
		return latitudeToLabel;
	}

	public Text getLatitudeToText() {
		return latitudeToText;
	}

	public Label getLatitudeToNote() {
		return latitudeToNote;
	}

	public Label getLongitudeLabel() {
		return longitudeLabel;
	}

	public Label getLongitudeFromLabel() {
		return longitudeFromLabel;
	}

	public Text getLongitudeFromText() {
		return longitudeFromText;
	}

	public Label getLongitudeFromNote() {
		return longitudeFromNote;
	}

	public Label getLongitudeToLabel() {
		return longitudeToLabel;
	}

	public Text getLongitudeToText() {
		return longitudeToText;
	}

	public Label getLongitudeToNote() {
		return longitudeToNote;
	}

	public Label getMinimumMagnitudeLabel() {
		return minimumMagnitudeLabel;
	}

	public Text getMinimumMagnitudeText() {
		return minimumMagnitudeText;
	}

	public Button getRestrictButton() {
		return restrictButton;
	}

	public Label getOutputFormatLabel() {
		return outputFormatLabel;
	}

	public Composite getRadioComposite() {
		return radioComposite;
	}

	public Map<String, Button> getFormatRadios() {
		return formatRadios;
	}

	public Label getResultsLabel() {
		return resultsLabel;
	}

	public Text getResultsText() {
		return resultsText;
	}

	public Group getCriteriaGroup() {
		return criteriaGroup;
	}

	public Composite getButtonsComposite() {
		return buttonsComposite;
	}

	public Button getSearchButton() {
		return searchButton;
	}

	public Label getResultsNote() {
		return resultsNote;
	}

	public Button getAutoRefreshButton() {
		return autoRefreshButton;
	}

	public Text getAutoRefreshText() {
		return autoRefreshText;
	}

	public Button getStopButton() {
		return stopButton;
	}

	public Button getClearButton() {
		return clearButton;
	}

}
