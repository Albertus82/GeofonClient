package it.albertus.geofon.client.gui;

import it.albertus.geofon.client.gui.job.DownloadMapJob;
import it.albertus.geofon.client.gui.listener.MapCanvasPaintListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

public class MapCanvas {

	private final MapCache cache = new MapCache();
	private final Canvas canvas;
	private Image image;
	private DownloadMapJob downloadMapJob;

	public MapCanvas(final Composite parent) {
		canvas = new Canvas(parent, SWT.BORDER);
		canvas.setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		canvas.addPaintListener(new MapCanvasPaintListener(this));
	}

	public Image getImage() {
		return image;
	}

	public void setImage(final Image image) {
		this.image = image;
		canvas.notifyListeners(SWT.Paint, new Event());
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public MapCache getCache() {
		return cache;
	}

	public DownloadMapJob getDownloadMapJob() {
		return downloadMapJob;
	}

	public void setDownloadMapJob(DownloadMapJob downloadMapJob) {
		this.downloadMapJob = downloadMapJob;
	}

}