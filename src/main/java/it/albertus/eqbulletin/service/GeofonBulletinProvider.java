package it.albertus.eqbulletin.service;

import java.util.Collection;
import java.util.function.BooleanSupplier;

import it.albertus.eqbulletin.model.Earthquake;
import it.albertus.eqbulletin.service.decode.DecodeException;
import it.albertus.eqbulletin.service.net.BulletinDownloader;
import it.albertus.eqbulletin.service.net.FetchException;
import it.albertus.eqbulletin.service.net.HtmlBulletinDownloader;
import it.albertus.eqbulletin.service.net.RssBulletinDownloader;

public class GeofonBulletinProvider implements BulletinProvider {

	private BulletinDownloader downloader;

	@Override
	public Collection<Earthquake> getEarthquakes(final SearchRequest request, final BooleanSupplier canceled) throws FetchException, DecodeException, InterruptedException {
		switch (request.getFormat()) {
		case HTML:
			downloader = new HtmlBulletinDownloader();
			return downloader.download(request, canceled);
		case RSS:
			downloader = new RssBulletinDownloader();
			return downloader.download(request, canceled);
		default:
			throw new UnsupportedOperationException(request.toString());
		}
	}

	@Override
	public void cancel() {
		if (downloader != null) {
			downloader.cancel();
		}
	}

}