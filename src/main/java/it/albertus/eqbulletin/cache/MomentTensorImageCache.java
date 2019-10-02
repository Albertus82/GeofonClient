package it.albertus.eqbulletin.cache;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import it.albertus.eqbulletin.config.EarthquakeBulletinConfig;
import it.albertus.eqbulletin.gui.preference.Preference;
import it.albertus.eqbulletin.model.MomentTensorImage;
import it.albertus.jface.preference.IPreferencesConfiguration;

public class MomentTensorImageCache implements Cache<String, MomentTensorImage> {

	private static final long serialVersionUID = 8229169461451106599L;

	private static final String CACHE_FILE = CacheManager.CACHE_DIRECTORY + File.separator + "mticache.ser";

	public static class Defaults {
		public static final byte CACHE_SIZE = 20;
		public static final boolean CACHE_SAVE = true;

		private Defaults() {
			throw new IllegalAccessError("Constants class");
		}
	}

	private static final IPreferencesConfiguration configuration = EarthquakeBulletinConfig.getPreferencesConfiguration();

	private static final CacheManager<MomentTensorImageCache> manager = new CacheManager<>();

	private static MomentTensorImageCache instance;

	public static synchronized MomentTensorImageCache getInstance() {
		if (instance == null) {
			if (configuration.getBoolean(Preference.MTI_CACHE_SAVE, Defaults.CACHE_SAVE)) {
				instance = manager.deserialize(CACHE_FILE, MomentTensorImageCache.class);
			}
			else {
				manager.delete(CACHE_FILE);
			}
			if (instance == null) {
				instance = new MomentTensorImageCache();
			}
		}
		return instance;
	}

	private MomentTensorImageCache() {}

	private final Map<String, MomentTensorImage> cache = new LinkedHashMap<>(16, 0.75f, true);

	@Override
	public synchronized void put(final String guid, final MomentTensorImage image) {
		cache.put(guid, image);
		while (cache.size() > 0 && cache.size() > configuration.getByte(Preference.MTI_CACHE_SIZE, Defaults.CACHE_SIZE)) {
			final String firstKey = cache.keySet().iterator().next();
			cache.remove(firstKey);
		}
		if (configuration.getBoolean(Preference.MTI_CACHE_SAVE, Defaults.CACHE_SAVE)) {
			manager.serialize(instance, CACHE_FILE);
		}
	}

	@Override
	public synchronized MomentTensorImage get(final String guid) {
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
		return "MomentTensorImageCache [size=" + getSize() + "]";
	}

}