package it.albertus.geofon.client.gui;

import it.albertus.geofon.client.GeofonClient;
import it.albertus.geofon.client.gui.listener.CloseListener;
import it.albertus.geofon.client.resources.Messages;
import it.albertus.util.Configuration;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;

public class GeofonClientGui extends ApplicationWindow {

	public interface Defaults {
		boolean START_MINIMIZED = false;
		boolean SEARCH_ON_START = false;
	}

	private static final Configuration configuration = GeofonClient.configuration;

	private static final float SASH_MAGNIFICATION_FACTOR = 1.5f;
	private static final int[] SASH_WEIGHTS = { 3, 2 };

	public static void run() {
		final Display display = Display.getDefault();
		final GeofonClientGui gui = new GeofonClientGui(display);
		gui.open();

		if (configuration.getBoolean("search.on.start", Defaults.SEARCH_ON_START)) {
			gui.getSearchForm().getSearchButton().notifyListeners(SWT.Selection, null);
		}

		final Shell shell = gui.getShell();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				Display.getCurrent().sleep();
			}
		}
		display.dispose();
	}

	private SearchForm searchForm;
	private ResultsTable resultsTable;
	private MapCanvas mapCanvas;
	private SashForm sashForm;
	private TrayIcon trayIcon;
	private MenuBar menuBar;

	public GeofonClientGui(final Display display) {
		super(null);
	}

	@Override
	protected void configureShell(final Shell shell) {
		super.configureShell(shell);
		shell.setImages(Images.MAIN_ICONS);
		shell.setMinimized(configuration.getBoolean("start.minimized", Defaults.START_MINIMIZED));
		shell.setText(Messages.get("lbl.window.title"));
	}

	@Override
	protected Control createContents(final Composite parent) {
		trayIcon = new TrayIcon(this);

		menuBar = new MenuBar(this);

		searchForm = new SearchForm(this);

		sashForm = new SashForm(parent, SWT.HORIZONTAL);
		sashForm.setSashWidth((int) (sashForm.getSashWidth() * SASH_MAGNIFICATION_FACTOR));
		sashForm.setLayout(new GridLayout());
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		resultsTable = new ResultsTable(sashForm, new GridData(SWT.FILL, SWT.FILL, true, true), this);

		mapCanvas = new MapCanvas(sashForm);
		sashForm.setWeights(SASH_WEIGHTS);

		return parent;
	}

	@Override
	public int open() {
		int code = super.open();
		for (final Button radio : getSearchForm().getFormatRadios().values()) {
			if (radio.getSelection()) {
				radio.notifyListeners(SWT.Selection, null);
				break;
			}
		}
		return code;
	}

	@Override
	protected void handleShellCloseEvent() {
		final Event event = new Event();
		new CloseListener(this).handleEvent(event);
		if (event.doit) {
			super.handleShellCloseEvent();
		}
	}

	@Override
	protected void initializeBounds() {/* Do not pack the shell */}

	@Override
	protected void createTrimWidgets(final Shell shell) {/* Not needed */}

	@Override
	protected Layout getLayout() {
		return new GridLayout();
	}

	public SearchForm getSearchForm() {
		return searchForm;
	}

	public ResultsTable getResultsTable() {
		return resultsTable;
	}

	public MapCanvas getMapCanvas() {
		return mapCanvas;
	}

	public TrayIcon getTrayIcon() {
		return trayIcon;
	}

	public SashForm getSashForm() {
		return sashForm;
	}

	public MenuBar getMenuBar() {
		return menuBar;
	}

}
