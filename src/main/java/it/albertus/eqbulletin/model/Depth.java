package it.albertus.eqbulletin.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;

import lombok.Value;
import lombok.extern.java.Log;

@Log
@Value
public class Depth implements Serializable, Comparable<Depth> {

	private static final long serialVersionUID = 894681925414643001L;

	private static final int EARTH_RADIUS_KM = 6371;

	private static final LinkedHashMap<Short, Depth> cache = new LinkedHashMap<Short, Depth>(16, 0.75f, true) {
		private static final long serialVersionUID = -3229317830656593292L;

		private static final short MAX_ENTRIES = 0xFF;

		@Override
		protected boolean removeEldestEntry(final Entry<Short, Depth> eldest) {
			final int size = size();
			log.log(Level.FINER, "Depth cache size: {0}.", size);
			return size > MAX_ENTRIES;
		}
	};

	private final short value; // Earth radius is about 6371 km (3959 mi).

	private Depth(final short value) {
		this.value = value;
		cache.put(value, this);
	}

	public static Depth valueOf(final int km) {
		if (km < 0 || km > EARTH_RADIUS_KM) {
			throw new IllegalArgumentException(Integer.toString(km));
		}
		final Depth cached = cache.get((short) km);
		return cached != null ? cached : new Depth((short) km);
	}

	@Override
	public String toString() {
		return Short.toString(value) + " km";
	}

	@Override
	public int compareTo(final Depth o) {
		return Short.compare(this.value, o.value);
	}

}
