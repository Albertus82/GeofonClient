package it.albertus.geofon.client.model;

import java.net.URL;
import java.util.Date;

public class Earthquake implements Comparable<Earthquake> {

	private final String guid;
	private final Date time;
	private final float magnitudo;
	private final float latitude;
	private final float longitude;
	private final int depth;
	private final Status status;
	private final String region;
	private final URL link;
	private final URL enclosure;

	public Earthquake(String guid, Date time, float magnitudo, float latitude, float longitude, int depth, Status status, String region, URL link, URL enclosure) {
		this.guid = guid;
		this.time = time;
		this.magnitudo = magnitudo;
		this.latitude = latitude;
		this.longitude = longitude;
		this.depth = depth;
		this.status = status;
		this.region = region;
		this.link = link;
		this.enclosure = enclosure;
	}

	public String getGuid() {
		return guid;
	}

	public Date getTime() {
		return time;
	}

	public float getMagnitudo() {
		return magnitudo;
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public int getDepth() {
		return depth;
	}

	public Status getStatus() {
		return status;
	}

	public String getRegion() {
		return region;
	}

	public URL getLink() {
		return link;
	}

	public URL getEnclosure() {
		return enclosure;
	}

	@Override
	public String toString() {
		return "Earthquake [guid=" + guid + ", time=" + time + ", magnitudo=" + magnitudo + ", latitude=" + latitude + ", longitude=" + longitude + ", depth=" + depth + ", status=" + status + ", region=" + region + ", link=" + link + ", enclosure=" + enclosure + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((guid == null) ? 0 : guid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Earthquake)) {
			return false;
		}
		Earthquake other = (Earthquake) obj;
		if (guid == null) {
			if (other.guid != null) {
				return false;
			}
		}
		else if (!guid.equals(other.guid)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(final Earthquake o) {
		if (this.equals(o)) {
			return 0;
		}
		else {
			return this.time.compareTo(o.time);
		}
	}

}