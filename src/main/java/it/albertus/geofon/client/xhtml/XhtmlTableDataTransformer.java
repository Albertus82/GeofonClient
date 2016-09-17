package it.albertus.geofon.client.xhtml;

import it.albertus.geofon.client.GeofonClient;
import it.albertus.geofon.client.model.Depth;
import it.albertus.geofon.client.model.Earthquake;
import it.albertus.geofon.client.model.Latitude;
import it.albertus.geofon.client.model.Longitude;
import it.albertus.geofon.client.model.Status;
import it.albertus.util.NewLine;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

public class XhtmlTableDataTransformer {

	private static final String guidPrefix = "id=";
	private static final String guidSuffix = "'>";
	private static final String timePrefix = guidSuffix;
	private static final String timeSuffix = "</a";

	/** Use {@link #parseRssDate} method instead. */
	@Deprecated
	private static final DateFormat xhtmlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	static {
		xhtmlDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	private static synchronized Date parseXhtmlDate(final String source) {
		try {
			return xhtmlDateFormat.parse(source);
		}
		catch (final ParseException pe) {
			throw new IllegalArgumentException(pe);
		}
	}

	public static Set<Earthquake> fromXhtml(final TableData tableData) throws MalformedURLException {
		final Set<Earthquake> earthquakes = new TreeSet<>();
		if (tableData != null && tableData.getItems().size() > 1) {
			// Discards first and last <td>
			for (int index = 1; index < tableData.getItems().size() - 1; index++) {
				final Earthquake converted = fromXhtml(tableData.getItems().get(index));
				if (converted != null) {
					earthquakes.add(converted);
				}
			}
		}
		return earthquakes;
	}

	private static Earthquake fromXhtml(final String td) throws MalformedURLException {
		try {
			final String lines[] = td.split(NewLine.SYSTEM_LINE_SEPARATOR);
			final Calendar time = Calendar.getInstance();
			time.setTime(parseXhtmlDate(lines[0].substring(lines[0].lastIndexOf(timePrefix) + timePrefix.length(), lines[0].indexOf(timeSuffix)).trim()));
			final String guid = lines[0].substring(lines[0].indexOf(guidPrefix) + guidPrefix.length(), lines[0].lastIndexOf(guidSuffix)).trim();
			final float magnitude = Float.parseFloat(lines[1].substring(lines[1].indexOf(">") + 1, lines[1].indexOf("</")).trim());
			final float latitude = Float.parseFloat(lines[2].substring(lines[2].indexOf(">") + 1, lines[2].indexOf("&deg;")).trim());
			final float longitude = Float.parseFloat(lines[3].substring(lines[3].indexOf(">") + 1, lines[3].indexOf("&deg;")).trim());
			final int depth = Integer.parseInt(lines[4].substring(lines[4].indexOf(">") + 1, lines[4].indexOf("</")).trim());
			final Status status = Status.valueOf(lines[5].substring(lines[5].lastIndexOf("'>") + 2, lines[5].indexOf("</")).trim());
			final String region = lines[6].substring(lines[6].lastIndexOf("'>") + 2, lines[6].lastIndexOf("</")).trim();
			final URL link = new URL(GeofonClient.BASE_URL + "/eqinfo/event.php?id=" + guid);
			final URL enclosure = new URL(GeofonClient.BASE_URL + "/data/alerts/" + time.get(Calendar.YEAR) + "/" + guid + "/" + guid + ".jpg");
			return new Earthquake(guid, time.getTime(), magnitude, new Latitude(latitude), new Longitude(longitude), new Depth(depth), status, region, link, enclosure);
		}
		catch (final Exception e) {
			throw new IllegalArgumentException(td, e);
		}
	}

}
