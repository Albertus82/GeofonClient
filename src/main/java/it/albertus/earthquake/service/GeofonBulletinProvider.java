package it.albertus.earthquake.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import it.albertus.earthquake.EarthquakeBulletin;
import it.albertus.earthquake.model.Earthquake;
import it.albertus.earthquake.model.Format;
import it.albertus.earthquake.resources.Messages;
import it.albertus.earthquake.service.html.TableData;
import it.albertus.earthquake.service.html.transformer.HtmlTableDataTransformer;
import it.albertus.earthquake.service.net.HttpConnector;
import it.albertus.earthquake.service.rss.transformer.RssItemTransformer;
import it.albertus.earthquake.service.rss.xml.Rss;
import it.albertus.util.NewLine;

public class GeofonBulletinProvider implements BulletinProvider {

	@Override
	public List<Earthquake> getEarthquakes(final SearchJobVars jobVariables) throws FetchException, DecodeException {
		final StringBuilder url = new StringBuilder(EarthquakeBulletin.BASE_URL).append("/eqinfo/list.php?fmt=").append(jobVariables.getParams().get("fmt"));
		for (final Entry<String, String> param : jobVariables.getParams().entrySet()) {
			if (param.getValue() != null && !param.getValue().isEmpty() && !"fmt".equals(param.getKey())) {
				url.append('&').append(param.getKey()).append('=').append(param.getValue());
			}
		}

		Rss rss = null;
		TableData html = null;
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = getConnection(url.toString());
			final String responseContentEncoding = urlConnection.getContentEncoding(); // Connection starts here
			final boolean gzip = responseContentEncoding != null && responseContentEncoding.toLowerCase().contains("gzip");
			try (final InputStream internalInputStream = urlConnection.getInputStream(); final InputStream inputStream = gzip ? new GZIPInputStream(internalInputStream) : internalInputStream) {
				if (Format.RSS.equals(jobVariables.getFormat())) {
					rss = fetchRss(inputStream);
				}
				else if (Format.HTML.equals(jobVariables.getFormat())) {
					html = fetchHtml(inputStream);
				}
				else {
					throw new UnsupportedOperationException(String.valueOf(jobVariables.getFormat()));
				}
			}
		}
		catch (final Exception e) {
			throw new FetchException(Messages.get("err.job.fetch"), e);
		}
		finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}

		// Decode
		try {
			if (rss != null) {
				return RssItemTransformer.fromRss(rss);
			}
			else if (html != null) {
				return HtmlTableDataTransformer.fromHtml(html);
			}
			else {
				throw new IllegalStateException();
			}
		}
		catch (final Exception e) {
			throw new DecodeException(Messages.get("err.job.decode"), e);
		}
	}

	private Rss fetchRss(final InputStream is) throws JAXBException {
		final JAXBContext jaxbContext = JAXBContext.newInstance(Rss.class);
		final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		return (Rss) jaxbUnmarshaller.unmarshal(is);
	}

	private TableData fetchHtml(final InputStream is) throws IOException {
		final TableData td = new TableData();
		try (final BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.trim().toLowerCase().contains("<tr")) {
					final StringBuilder block = new StringBuilder();
					while (!(line = br.readLine()).toLowerCase().contains("</tr")) {
						block.append(line.trim()).append(NewLine.SYSTEM_LINE_SEPARATOR);
					}
					td.addItem(block.toString());
				}
			}
		}
		return td;
	}

	private HttpURLConnection getConnection(final String url) throws IOException {
		final HttpURLConnection urlConnection = HttpConnector.getConnection(url);
		urlConnection.setRequestProperty("Accept", "*/html,*/xml,*/*;q=0.9");
		urlConnection.setRequestProperty("Accept-Encoding", "gzip");
		return urlConnection;
	}

}