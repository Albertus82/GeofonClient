package it.albertus.eqbulletin.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.albertus.eqbulletin.config.EarthquakeBulletinConfig;
import it.albertus.eqbulletin.gui.preference.Preference;
import it.albertus.eqbulletin.model.MapImage;
import it.albertus.jface.preference.IPreferencesConfiguration;
import it.albertus.util.logging.LoggerFactory;

public class MapImageCache implements Cache<String, MapImage> {

	private static final long serialVersionUID = 14746911870762927L;

	private static final Logger logger = LoggerFactory.getLogger(MapImageCache.class);

	public static class Defaults {
		public static final byte CACHE_SIZE = 20;

		private Defaults() {
			throw new IllegalAccessError("Constants class");
		}
	}

	private static final IPreferencesConfiguration configuration = EarthquakeBulletinConfig.getInstance();

	private static MapImageCache instance;

	public static synchronized MapImageCache getInstance() {
		if (instance == null) {
			instance = new MapImageCache();
		}
		return instance;
	}

	private MapImageCache() {}

	private final Map<String, MapImage> cache = new LinkedHashMap<>(16, 0.75f, true);

	@Override
	public synchronized void put(final String guid, final MapImage map) {
		cache.put(guid, map);
		while (cache.size() > 0 && cache.size() > configuration.getByte(Preference.MAP_CACHE_SIZE, Defaults.CACHE_SIZE)) {
			final String firstKey = cache.keySet().iterator().next();
			cache.remove(firstKey);
		}
	}

	@Override
	public synchronized MapImage get(final String guid) {
		return cache.get(guid);
	}

	@Override
	public synchronized boolean contains(final String guid) {
		return cache.containsKey(guid);
	}

	@Override
	public int getSize() {
		return cache.size();
	}

	@Override
	public String toString() {
		return "MapImageCache [size=" + getSize() + "]";
	}

	public static synchronized void serialize() {
		if (instance != null) {
			final File file = new File(EarthquakeBulletinConfig.MAP_CACHE_FILE);
			file.getParentFile().mkdirs();
			try (final FileOutputStream fos = new FileOutputStream(file); final ObjectOutputStream oos = new ObjectOutputStream(fos)) {
				logger.log(Level.CONFIG, "Serializing {0} into \"{1}\"...", new Serializable[] { instance, file });
				oos.writeObject(instance);
				logger.log(Level.CONFIG, "{0} serialized successfully.", instance);
			}
			catch (final IOException e) {
				logger.log(Level.WARNING, "Cannot serialize " + instance + ':', e);
			}
		}
	}

	public static synchronized void deserialize() {
		final File file = new File(EarthquakeBulletinConfig.MAP_CACHE_FILE);
		if (file.isFile()) {
			try (final FileInputStream fis = new FileInputStream(file); final ObjectInputStream ois = new ObjectInputStream(fis)) {
				logger.log(Level.CONFIG, "Deserializing {0} from \"{1}\"...", new Serializable[] { MapImageCache.class, file });
				MapImageCache.instance = (MapImageCache) ois.readObject();
				logger.log(Level.CONFIG, "{0} deserialized successfully.", instance);
			}
			catch (final IOException | ClassNotFoundException e) {
				logger.log(Level.WARNING, "Cannot deserialize " + MapImageCache.class + ':', e);
			}
		}
	}

}
