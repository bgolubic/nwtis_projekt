package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Dragutin Kermek
 * @version 2.3.0
 */
@AllArgsConstructor()
public class Airport {

	@Getter
	@Setter
	private String ident;
	@Getter
	@Setter
	private String type;
	@Getter
	@Setter
	private String name;
	@Getter
	@Setter
	private String elevation_ft;
	@Getter
	@Setter
	private String continent;
	@Getter
	@Setter
	private String iso_country;
	@Getter
	@Setter
	private String iso_region;
	@Getter
	@Setter
	private String municipality;
	@Getter
	@Setter
	private String gps_code;
	@Getter
	@Setter
	private String iata_code;
	@Getter
	@Setter
	private String local_code;
	@Getter
	@Setter
	private String coordinates;

	public Airport() {
	}
}
